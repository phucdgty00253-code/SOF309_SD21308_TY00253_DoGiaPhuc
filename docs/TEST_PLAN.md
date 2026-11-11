# KẾ HOẠCH KIỂM THỬ (TEST PLAN) - SALTAIR

* **Dự án:** Saltair - website bán quần áo basic (JAV102)
* **Công nghệ:** Java 17, Jakarta Servlet/JSP, Tomcat 11, MySQL 8
* **URL chạy local:** `http://localhost:8080/saltair/home`
* **Quy trình áp dụng:** [QUY_TRINH_PHAT_TRIEN_FEATURE.md](QUY_TRINH_PHAT_TRIEN_FEATURE.md) (Bước 3 và Bước 5)

---

## 1. MỤC TIÊU KIỂM THỬ
* Kiểm thử nghiệp vụ ở tầng Service bằng **JUnit 5** (giá trị biên, lỗi nghiệp vụ).
* Kiểm thử từng feature theo `docs/features/<số>-<tên>/TEST_CASES.md` trước khi mở PR.
* Kiểm thử toàn luồng (khách xem hàng → giỏ → đặt hàng → admin xử lý đơn) trước mỗi lần merge lớn vào `develop` và trước khi đưa lên `main`.

## 2. PHẠM VI KIỂM THỬ
* **Trong phạm vi:**
  * Đăng nhập bằng email + mã 6 số (khách và admin), phân quyền `AuthFilter`/`AdminFilter`.
  * Danh sách, tìm kiếm, lọc, chi tiết sản phẩm.
  * Giỏ hàng (cả khách chưa đăng nhập), voucher, phí ship, đặt hàng, hủy đơn, tồn kho.
  * Tài khoản: hồ sơ, sổ địa chỉ, yêu thích, đơn hàng, đánh giá.
  * Trang quản trị: sản phẩm, danh mục, màu/size, đơn hàng, voucher, người dùng, banner, blog, dashboard.
  * Giao diện ở khổ máy tính (1440px) và điện thoại (375px).
* **Ngoài phạm vi:** kiểm thử hiệu năng, bảo mật chuyên sâu, thanh toán thật, gửi mail thật trong máy chấm (dùng `auth.devCode=true`).

## 3. CẤP ĐỘ KIỂM THỬ

| Cấp độ | Công cụ | Thư mục / Tài liệu | Khi nào chạy |
| :--- | :--- | :--- | :--- |
| Unit test | JUnit 5 (`mvn test`) | `src/test/java/com/fashionweb/` | Mỗi lần commit có sửa Service |
| Test case feature | Làm tay theo bảng | `docs/features/<số>-<tên>/TEST_CASES.md` | Bước 5, trước khi mở PR |
| Toàn luồng | Làm tay (hoặc script curl) | Mục 5 bên dưới | Trước khi merge PR lớn, trước khi lên `main` |

## 4. MÔI TRƯỜNG & DỮ LIỆU
* Chạy `database/schema.sql` rồi `database/data.sql` để có dữ liệu sạch.
* `src/main/resources/mail.properties` có `auth.devCode=true` để mã đăng nhập hiện trên trang (email mẫu là email giả).
* Tài khoản mẫu: `admin@saltair.vn` (ADMIN), `khach@saltair.vn` (có giỏ, đơn đủ trạng thái), `mai.pham@example.com` (đang bị khóa).
* Voucher mẫu: `WELCOME10`, `GIAM50K` (dùng được), `HETHAN20`, `HETLUOT`, `TAMDUNG` (để thử lỗi).

## 5. CÁC LUỒNG CHÍNH PHẢI LUÔN CHẠY ĐƯỢC (SMOKE)
1. Khách chưa đăng nhập mở trang chủ, tìm kiếm, mở chi tiết sản phẩm, thêm vào giỏ.
2. Khách đăng nhập bằng mã, quay về đúng trang trước đó, giỏ hàng giữ nguyên.
3. Đặt hàng có voucher `WELCOME10`: phí ship bằng 0, tồn kho giảm.
4. Khách hủy đơn "Chờ xác nhận": tồn kho và lượt dùng voucher được trả lại.
5. Admin đăng nhập ở `/admin/login`, chuyển đơn sang "Đang giao" rồi "Hoàn thành".
6. Khách đánh giá đơn "Hoàn thành".
7. Tài khoản bị khóa không xin được mã đăng nhập.

## 6. TIÊU CHÍ HOÀN THÀNH
* `mvn test`: 100% Pass.
* Mọi `TEST_CASES.md` của các feature trong đợt merge đều Pass.
* 7 luồng ở mục 5 đều chạy được, log Tomcat không có `SEVERE`/`Exception`.
