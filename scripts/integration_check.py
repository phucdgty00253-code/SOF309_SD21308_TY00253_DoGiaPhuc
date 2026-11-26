#!/usr/bin/env python3
"""Kiểm tra tích hợp PolyCoffee qua HTTP (Tomcat + SQL Server thật).

CHỈ chạy trên CSDL local vừa tạo lại bằng database/polycoffee.sql, vì script thêm và sửa dữ liệu.
Cách dùng: python3 scripts/integration_check.py <file_thu_cua_mail_sink> [base_url]
"""
import datetime
import email
import html
import http.cookiejar
import re
import sys
import urllib.error
import urllib.parse
import urllib.request
import uuid

MAIL_FILE = sys.argv[1]
BASE = sys.argv[2] if len(sys.argv) > 2 else "http://localhost:8080/polycoffee"
PASSWORD = "123456"  # mật khẩu dữ liệu mẫu


class NoRedirect(urllib.request.HTTPRedirectHandler):
    def redirect_request(self, *args):
        return None


class Response:
    def __init__(self, status, headers, body):
        self.status, self.headers, self.raw = status, headers, body
        self.text = html.unescape(body.decode("utf-8", "replace"))
        self.source = body.decode("utf-8", "replace")
        self.location = (headers.get("Location") or "").replace(BASE, "")
        self.location = re.sub(r"^/polycoffee", "", self.location)


class Browser:
    def __init__(self):
        self.opener = urllib.request.build_opener(
            urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()), NoRedirect)

    def request(self, path, data=None, headers=None):
        req = urllib.request.Request(BASE + path, data=data, headers=headers or {})
        try:
            with self.opener.open(req, timeout=60) as r:
                return Response(r.status, r.headers, r.read())
        except urllib.error.HTTPError as e:
            return Response(e.code, e.headers, e.read())

    def get(self, path):
        return self.request(path)

    def post(self, path, **fields):
        return self.request(path, urllib.parse.urlencode(fields).encode(),
                            {"Content-Type": "application/x-www-form-urlencoded"})

    def follow(self, path, **fields):
        """POST rồi mở trang được redirect tới."""
        r = self.post(path, **fields)
        assert r.status == 302, f"{path}: mong đợi 302, nhận {r.status}"
        return self.get(r.location)

    def upload(self, path, fields, filename, content):
        boundary = uuid.uuid4().hex
        parts = [f'--{boundary}\r\nContent-Disposition: form-data; name="{k}"\r\n\r\n{v}\r\n'.encode()
                 for k, v in fields.items()]
        parts.append(f'--{boundary}\r\nContent-Disposition: form-data; name="imageFile"; filename="{filename}"\r\n'
                     "Content-Type: application/octet-stream\r\n\r\n".encode() + content + b"\r\n")
        parts.append(f"--{boundary}--\r\n".encode())
        return self.request(path, b"".join(parts), {"Content-Type": f"multipart/form-data; boundary={boundary}"})

    def login(self, who, password=PASSWORD):
        return self.post("/auth/login", email=f"{who}@polycoffee.vn", password=password)


def mails():
    try:
        raw = open(MAIL_FILE, "rb").read()
    except FileNotFoundError:
        return []
    result = []
    for chunk in raw.split(b"\n=====END=====\n"):
        if chunk.strip():
            m = email.message_from_bytes(chunk)
            result.append((m["To"], m.get_payload(decode=True).decode("utf-8")))
    return result


def mailed_password(to):
    body = [b for t, b in mails() if t == to][-1]
    return re.search(r"Mật khẩu mới của bạn là: (\S+)", body).group(1)


def row_id(page, text):
    """id ở cột đầu của dòng bảng có chứa text."""
    for row in re.findall(r"<tr>(.*?)</tr>", page.text, re.S):
        if text in row:
            found = re.search(r"[?&]id=(\d+)|editId=(\d+)|name=\"id\" value=\"(\d+)\"", row)
            return next(g for g in found.groups() if g)
    raise AssertionError(f"không thấy dòng chứa {text!r}")


CHECKS = []


def check(case_id, title):
    def wrap(fn):
        CHECKS.append((case_id, title, fn))
        return fn
    return wrap


guest, admin, staff = Browser(), Browser(), Browser()


@check("TC_IT_001", "Trang chủ hiển thị đúng tiếng Việt, giá; ẩn đồ uống ngừng bán")
def home():
    r = guest.get("/home")
    assert r.status == 200, r.status
    assert "Cà phê sữa đá" in r.text and "29" in r.text
    assert "Sinh tố bơ" not in r.text
    assert guest.get("/").status == 200 and guest.get("/assets/css/style.css").status == 200


@check("TC_IT_002", "Chưa đăng nhập bị chuyển đi; đăng nhập xong quay lại trang cũ; cookie HttpOnly + SameSite")
def redirect_back():
    r = admin.get("/manager/drinks?status=1")
    assert r.status == 302 and r.location == "/auth/login", (r.status, r.location)
    cookie = r.headers.get("Set-Cookie", "")
    assert "HttpOnly" in cookie and "SameSite=Lax" in cookie, cookie
    r = admin.login("admin")
    assert r.status == 302 and r.location == "/manager/drinks?status=1", (r.status, r.location)
    assert admin.get(r.location).status == 200


@check("TC_IT_003", "Sai mật khẩu và tài khoản khóa bị từ chối")
def bad_login():
    assert "Email hoặc mật khẩu không đúng." in guest.login("admin", "sai-mat-khau").text
    assert "bị khóa" in guest.login("khoa").text
    assert guest.get("/employee/pos").status == 302


@check("TC_IT_004", "Nhân viên không vào được /manager/*")
def staff_blocked():
    assert staff.login("nhanvien").location == "/employee/pos"
    for path in ["/manager/drinks", "/manager/staff", "/manager/stats/revenue"]:
        r = staff.get(path)
        assert r.status == 302 and r.location == "/auth/login", (path, r.status)


@check("TC_IT_005", "Không vượt quyền bằng URL mã hóa hoặc tham số đường dẫn")
def url_tricks():
    for path in ["/%6Danager/staff", "/manager;x/staff", "/manager/./staff", "//manager/staff",
                 "/employee/../manager/staff", "/manage%72/staff"]:
        r = staff.get(path)
        assert not (r.status == 200 and "Quản lý nhân viên</h1>" in r.source), f"nhân viên mở được {path}"


@check("TC_IT_006", "CRUD loại đồ uống với tiếng Việt; chặn trùng tên và xóa loại đã dùng")
def categories():
    page = admin.follow("/manager/categories/create", name="Sữa chua đặc biệt", active="on")
    assert "Đã thêm loại đồ uống." in page.text and "Sữa chua đặc biệt" in page.text
    cid = row_id(page, "Sữa chua đặc biệt")
    assert "Tên loại đã tồn tại." in admin.post("/manager/categories/create", name="Sữa chua đặc biệt").text
    page = admin.follow("/manager/categories/update", id=cid, name="Sữa chua dẻo", active="on")
    assert "Sữa chua dẻo" in page.text
    assert "Không thể xóa loại đã có đồ uống" in admin.follow("/manager/categories/delete", id=1).text
    assert "Đã xóa loại đồ uống." in admin.follow("/manager/categories/delete", id=cid).text


@check("TC_IT_007", "Dữ liệu chứa thẻ HTML được escape khi hiển thị")
def xss():
    page = admin.follow("/manager/categories/create", name="<script>x()</script>", active="on")
    assert "<script>x()</script>" not in page.source and "&lt;script&gt;" in page.source
    admin.follow("/manager/categories/delete", id=row_id(page, "<script>x()</script>"))


PNG = bytes.fromhex("89504e470d0a1a0a0000000d4948445200000001000000010806000000"
                    "1f15c4890000000d49444154789c6360000002000100e221bc330000000049454e44ae426082")


@check("TC_IT_008", "Thêm đồ uống có ảnh; ảnh tải được; xóa đồ uống chưa bán")
def drink_with_image():
    fields = dict(id=0, categoryId=1, name="Cà phê kem trứng", price=39000, description="Món mới", active="on")
    r = admin.upload("/manager/drinks/create", fields, "kem.png", PNG)
    assert r.status == 302, (r.status, r.text[:300])
    image = re.search(r'src="/polycoffee(/uploads/[^"]+\.png)"', guest.get("/home").source)
    assert image, "trang chủ không có ảnh vừa upload"
    served = guest.get(image.group(1))
    assert served.status == 200 and served.raw == PNG
    page = admin.get("/manager/drinks?" + urllib.parse.urlencode({"keyword": "kem trứng"}))
    assert "Đã xóa đồ uống." in admin.follow("/manager/drinks/delete", id=row_id(page, "Cà phê kem trứng")).text
    assert guest.get(image.group(1)).status == 404, "ảnh chưa được dọn"


@check("TC_IT_009", "Chặn upload file không phải ảnh")
def upload_type():
    fields = dict(id=0, categoryId=1, name="Món file lạ", price=39000, active="on")
    r = admin.upload("/manager/drinks/create", fields, "shell.jsp", b"<% out.print(1); %>")
    assert r.status == 200 and "Ảnh chỉ nhận file JPG, PNG hoặc WEBP." in r.text


@check("TC_IT_010", "File ảnh quá lớn (3 MB và 7 MB) báo lỗi thân thiện, không lỗi 500")
def upload_too_big():
    for size in (3, 7):
        fields = dict(id=0, categoryId=1, name=f"Ảnh {size}MB", price=39000, active="on")
        r = admin.upload("/manager/drinks/create", fields, "big.png", b"0" * (size * 1024 * 1024))
        assert r.status == 200 and "Ảnh tối đa 2 MB." in r.text, f"{size} MB -> HTTP {r.status}"


@check("TC_IT_011", "Tìm kiếm kết hợp phân trang đồ uống; ký tự đặc biệt không phá câu SQL")
def drink_search():
    for i in range(1, 12):
        r = admin.upload("/manager/drinks/create",
                         dict(id=0, categoryId=2, name=f"Trà thử {i:02d}", price=20000, active="on"), "", b"")
        assert r.status == 302, r.status
    query = urllib.parse.urlencode({"keyword": "Trà thử", "categoryId": 2, "status": 1})
    first = admin.get("/manager/drinks?" + query)
    assert first.source.count("Trà thử ") >= 10 and "Tổng: 11 bản ghi" in first.text
    second = admin.get(f"/manager/drinks?{query}&page=2")
    assert "Trà thử 01" in second.text and "Trà thử 11" not in second.text
    for keyword in ["' OR 1=1 --", "%", "_", "[a-z]"]:
        r = admin.get("/manager/drinks?" + urllib.parse.urlencode({"keyword": keyword}))
        assert r.status == 200 and "Tổng: 0 bản ghi" in r.text, (keyword, r.status)


def revenue_total():
    r = admin.get("/manager/stats/revenue")
    assert r.status == 200, r.status
    found = re.search(r"Tổng doanh thu: ([\d.,]+) đ", r.text)
    return int(re.sub(r"\D", "", found.group(1))) if found else 0


@check("TC_IT_012", "POS: tạo đơn, tăng/giảm, thanh toán; tổng tiền đúng; doanh thu tăng đúng số tiền")
def pos_flow():
    before = revenue_total()
    assert before == 258000, f"doanh thu dữ liệu mẫu phải là 258000, nhận {before}"
    r = staff.post("/employee/pos/init", billId=0, drinkId=2, cardId=1)
    assert r.status == 302 and "billId=" in r.location, (r.status, r.location)
    bill = r.location.split("billId=")[1]
    globals()["PAID_BILL"] = bill
    staff.follow("/employee/pos/init", billId=bill, drinkId=2)
    staff.follow("/employee/pos/init", billId=bill, drinkId=5)
    staff.follow("/employee/pos/update-quantity", billId=bill, drinkId=2, action="increase")
    page = staff.follow("/employee/pos/update-quantity", billId=bill, drinkId=5, action="decrease")
    assert re.search(r"Tổng: 87[.,]000 đ", page.text), "3 x 29000 phải bằng 87000"
    assert "Thanh toán thành công." in staff.follow("/employee/pos/checkout", billId=bill).text
    assert revenue_total() == before + 87000
    page = staff.follow("/employee/pos/update-quantity", billId=bill, drinkId=2, action="increase")
    assert "Chỉ sửa được hóa đơn đang chờ thanh toán." in page.text


@check("TC_IT_013", "Lịch sử của nhân viên chỉ có hóa đơn của mình; xem hóa đơn người khác bị 403")
def history():
    page = staff.get("/employee/bills")
    assert page.status == 200 and "HD-SEED-001" in page.text and "HD-SEED-003" not in page.text
    assert staff.get("/employee/bills/detail?id=3").status == 403
    assert staff.get("/employee/pos?billId=3").status == 403
    assert admin.get("/manager/bills/detail?id=999999").status == 404


@check("TC_IT_014", "Quản lý xem chi tiết hóa đơn đủ thông tin và hủy hóa đơn đã thanh toán")
def manage_bill():
    detail = admin.get(f"/manager/bills/detail?id={PAID_BILL}")
    for text in ["Cà phê sữa đá", "Nguyễn Văn An", "Ngày tạo:", "finish", "TABLE-01"]:
        assert text in detail.text, text
    before = revenue_total()
    page = admin.follow("/manager/bills/cancel", id=PAID_BILL)
    assert "Đã hủy hóa đơn." in page.text
    assert revenue_total() == before - 87000, "hóa đơn hủy vẫn bị tính doanh thu"
    assert "Không thể chuyển" in admin.follow("/manager/bills/cancel", id=PAID_BILL).text
    assert admin.get("/manager/bills").status == 200


@check("TC_IT_015", "Top 5 chỉ tính hóa đơn finish; lọc ngày; khoảng ngày sai báo lỗi")
def statistics():
    page = admin.get("/manager/stats/top-drinks")
    rows = re.findall(r"<tr>(.*?)</tr>", page.text.split("<tbody>")[1], re.S)
    names = [re.findall(r"<td[^>]*>(.*?)</td>", r, re.S)[1] for r in rows]
    assert names == ["Trà đào cam sả", "Cà phê sữa đá", "Matcha đá xay", "Cà phê đen đá"], names
    today = datetime.date.today()
    empty = admin.get(f"/manager/stats/revenue?from={today + datetime.timedelta(days=10)}")
    assert "Không có dữ liệu" in empty.text
    one_day = admin.get(f"/manager/stats/revenue?from={today - datetime.timedelta(days=3)}"
                        f"&to={today - datetime.timedelta(days=3)}")
    assert re.search(r"Tổng doanh thu: 83[.,]000 đ", one_day.text), "lọc đúng một ngày phải ra 83000"
    assert 'class="bar"' in one_day.source
    assert "Ngày bắt đầu không được sau" in admin.get(
        "/manager/stats/revenue?from=2026-02-02&to=2026-02-01").text


NEW_STAFF = "binh@polycoffee.vn"


@check("TC_IT_016", "Thêm, tìm, khóa, mở khóa nhân viên; chặn trùng email")
def staff_crud():
    fields = dict(id=0, fullName="Lê Thị Bình", email=NEW_STAFF, phone="0912345678", password="matkhau1",
                  active="on")
    assert "Đã thêm nhân viên." in admin.follow("/manager/staff/create", **fields).text
    assert "Email đã được sử dụng." in admin.post("/manager/staff/create", **fields).text
    page = admin.get("/manager/staff?" + urllib.parse.urlencode({"name": "Bình", "status": 1}))
    assert "Tổng: 1 bản ghi" in page.text
    sid = row_id(page, NEW_STAFF)
    globals()["NEW_STAFF_ID"] = sid
    admin.follow("/manager/staff/status", id=sid, active="false")
    assert "bị khóa" in Browser().login("binh", "matkhau1").text
    admin.follow("/manager/staff/status", id=sid, active="true")
    assert Browser().login("binh", "matkhau1").status == 302


@check("TC_IT_017", "Cấp lại mật khẩu: thư đến, mật khẩu mới dùng được, mật khẩu cũ hết hiệu lực")
def reset_password():
    page = admin.follow("/manager/staff/reset-password", id=NEW_STAFF_ID)
    assert "Đã gửi mật khẩu mới" in page.text, page.text[:600]
    new = mailed_password(NEW_STAFF)
    assert Browser().login("binh", new).status == 302
    assert "không đúng" in Browser().login("binh", "matkhau1").text


@check("TC_IT_018", "Quên mật khẩu, đổi mật khẩu, sửa thông tin cá nhân, đăng xuất")
def self_service():
    assert Browser().post("/auth/forgot-password", email=NEW_STAFF).status == 302
    temp = mailed_password(NEW_STAFF)
    me = Browser()
    assert me.login("binh", temp).status == 302
    assert "Đã đổi mật khẩu." in me.follow("/account/password", oldPassword=temp, newPassword="moi-123456",
                                           confirmPassword="moi-123456").text
    page = me.follow("/account/profile", fullName="Lê Bình Mới", phone="0987654321")
    assert "Lê Bình Mới" in page.text
    assert me.post("/auth/logout").location == "/auth/login"
    assert me.get("/employee/pos").status == 302
    assert Browser().login("binh", "moi-123456").status == 302


@check("TC_IT_019", "Trang lỗi 404 và gọi sai phương thức")
def error_pages():
    r = guest.get("/khong-ton-tai")
    assert r.status == 404 and "Không tìm thấy" in r.text
    assert admin.get("/employee/pos/checkout").status == 405
    assert admin.get("/manager/categories/delete").status == 405


def main():
    failed = 0
    for case_id, title, fn in CHECKS:
        try:
            fn()
            print(f"PASS {case_id}  {title}")
        except Exception as e:  # ghi lại rồi chạy tiếp
            failed += 1
            print(f"FAIL {case_id}  {title}\n     {type(e).__name__}: {str(e)[:400]}")
    print(f"\nTổng: {len(CHECKS)}, pass: {len(CHECKS) - failed}, fail: {failed}")
    sys.exit(1 if failed else 0)


main()
