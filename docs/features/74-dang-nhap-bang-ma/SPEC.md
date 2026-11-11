# ĐẶC TẢ FEATURE: Đăng nhập bằng email + mã 6 số, bỏ mật khẩu (#74)
> *Ví dụ đã điền đầy đủ theo mẫu `_TEMPLATE/SPEC.md`. Feature mới copy thư mục `_TEMPLATE`, không copy thư mục này.*

---

## 1. THÔNG TIN CHUNG
* **Issue:** #74 [Logic] · **Đi cùng:** #75 [Giao diện] khung tài khoản và trang nhập mã 6 ô
* **Người làm:** Trưởng nhóm - `nhan0x`
* **Nhánh:** `feature/74-email-login`
* **Mảng:** auth
* **Trạng thái:** Đã merge (PR #78)

## 2. MỤC TIÊU
Khách và admin đăng nhập bằng email, nhận mã 6 số qua email, nhập mã là vào. Bỏ hẳn mật khẩu. Email chưa có tài khoản thì tự tạo tài khoản khách ("đăng nhập hoặc tạo tài khoản" trong cùng một luồng).

## 3. QUY TẮC NGHIỆP VỤ (BUSINESS RULES)

**Mã đăng nhập**
* **BR-1:** Mã gồm 6 chữ số, tạo bằng `SecureRandom`, cho phép số 0 đứng đầu.
* **BR-2:** DB chỉ lưu `SHA-256(email + ":" + mã)` dạng hex, không lưu mã gốc.
* **BR-3:** Xin mã mới thì mọi mã cũ chưa dùng của email đó hết hiệu lực.
* **BR-4:** Mỗi mã chỉ dùng được một lần (kiểm tra trong transaction có `SELECT ... FOR UPDATE`).
* **BR-5:** Hạn dùng tính bằng `NOW()` của MySQL.

**Luồng khách**
* **BR-6:** Mã hết hạn sau **15 phút**. Nhập sai **5 lần** thì mã bị hủy.
* **BR-7:** Xin lại mã sau ít nhất **60 giây**, tối đa **5 mã mỗi giờ** cho một email. Vượt thì báo "Bạn thử lại sau ít phút."
* **BR-8:** Email chưa có tài khoản: nhập đúng mã thì tạo tài khoản `CUSTOMER`, `full_name` là phần trước `@`.
* **BR-9:** Tài khoản bị khóa (`is_active = 0`) không xin được mã: báo "Tài khoản đã bị khóa...".
* **BR-10:** Email của admin nhập ở luồng khách: báo "Tài khoản quản trị vui lòng đăng nhập tại trang quản trị." kèm link `/admin/login`.

**Luồng admin**
* **BR-11:** Mã hết hạn sau **5 phút**, sai **3 lần** thì hủy, tối đa **3 mã mỗi giờ**.
* **BR-12:** Luôn báo cùng một câu dù email có phải admin hay không; email không phải admin thì không gửi mã. Không bao giờ tự tạo tài khoản admin.
* **BR-13:** Phiên admin hết hạn sau **30 phút** không thao tác. Mỗi lần đăng nhập gửi mail báo (thời gian, IP, trình duyệt).

**Chung**
* **BR-14:** Email đang chờ nhập mã lưu trong session, không đặt lên URL. Đăng nhập xong thì đổi session ID.
* **BR-15:** Khách quay về trang đang xem (`redirect` nội bộ an toàn) hoặc `/home`; admin về `/admin`.
* **BR-16:** `auth.devCode=true` trong `mail.properties`: không gửi mail, ghi mã vào log và hiện trên trang nhập mã. Mặc định `false`, khi đó mã không bao giờ xuất hiện ở log hay giao diện.

## 4. LUỒNG XỬ LÝ
1. Khách nhập email ở khung tài khoản hoặc trang `/login`.
2. Hệ thống kiểm tra định dạng, giới hạn gửi, tài khoản khóa, email admin; tạo mã, lưu băm, gửi mail.
3. Lưu email vào session, chuyển sang `/login/code`.
4. Khách nhập mã: đúng thì đổi session ID, lưu `User` vào session, về trang trước đó; sai thì báo số lần còn lại.

## 5. ENDPOINT

| Phương thức | Đường dẫn | Tham số | Thành công | Lỗi |
| :---: | :--- | :--- | :--- | :--- |
| GET | `/login` | `redirect`, `email` | Trang nhập email | |
| POST | `/login/email` | `email`, `redirect` | Redirect `/login/code` (JSON `{ok, redirect}` khi gọi bằng fetch) | Hiện lại `/login` với `errors.email` / `errors.admin` |
| GET, POST | `/login/code` | `code` (hoặc 6 ô `d`) | Redirect về `redirect` hoặc `/home` | Hiện lại trang với `errors.code` |
| POST | `/login/code/resend` | | Flash "Đã gửi mã mới" | Flash lỗi giới hạn |
| GET, POST | `/admin/login` | `email`, `redirect` | Redirect `/admin/login/code` | `errors.email` |
| GET, POST | `/admin/login/code` | `code`, `action=resend` | Redirect `/admin` | `errors.code` |
| GET | `/register`, `/forgot-password`, `/reset-password` | | Redirect `/login` (giữ link cũ) | |

## 6. THAY ĐỔI DATABASE
* **Bảng mới `login_codes`:** `id`, `email VARCHAR(150)`, `code_hash CHAR(64)`, `expires_at DATETIME`, `attempts TINYINT`, `used TINYINT(1)`, `created_at`; index `(email, created_at)`; không có khóa ngoại.
* **Xóa:** cột `users.password_hash`, bảng `password_resets`.
* **Migration:** `database/migration_02_bo_mat_khau.sql` (chạy lại nhiều lần vẫn an toàn).
* **Dữ liệu mẫu:** bỏ chuỗi băm mật khẩu khỏi `data.sql`, ghi chú bật `auth.devCode`.

## 7. CÁC FILE SẼ SỬA / THÊM

| Tầng | File |
| :--- | :--- |
| Controller | `controller/auth/LoginServlet`, `EmailLoginServlet`, `LoginCodeServlet`, `AdminLoginServlet`, `LoginSupport` |
| Filter | `filter/AdminFilter` (cho qua `/admin/login*`) |
| Service | `service/auth/AuthService` (`requestCode`, `verifyCode`, `notifyAdminLogin`) |
| Repository | `repository/auth/LoginCodeRepository`, `UserRepository` |
| View | `views/auth/login.jsp`, `views/auth/login-code.jsp` |
| Cấu hình | `util/MailUtil` (`isDevCode`), `mail.properties.example`, `pom.xml` (bỏ `jbcrypt`) |

## 8. GIAO DIỆN & TRẠNG THÁI
* **Đang gửi:** nút mũi tên đổi thành vòng xoay, ô email bị khóa (#75).
* **Lỗi:** chữ đỏ ngay dưới ô email hoặc dưới 6 ô mã.
* **Mã sai:** "Mã không đúng, bạn còn N lần thử."
* **Gửi lại:** nút khóa và đếm ngược 60 giây.

## 9. NGOÀI PHẠM VI
* Ô "Nhận ưu đãi qua email" và cột `marketing_opt_in`.
* Đăng nhập bằng Google/Facebook.
* Giao diện popover và 6 ô mã: làm ở #75.

## 10. QUYẾT ĐỊNH ĐÃ CHỐT
| Ngày | Câu hỏi | Quyết định | Người chốt |
| :--- | :--- | :--- | :--- |
| 10/11 | Giữ mật khẩu song song không? | Bỏ hẳn mật khẩu | Trưởng nhóm |
| 10/11 | Admin đăng nhập thế nào? | Phương án 1: trang riêng `/admin/login`, mã 5 phút, sai 3 lần | Trưởng nhóm |
| 10/11 | Thêm cột nhận ưu đãi? | Không, chỉ giữ cột thật sự cần | Trưởng nhóm |
| 10/11 | Tài khoản mẫu là email giả, đăng nhập thế nào? | Thêm cấu hình `auth.devCode` | Trưởng nhóm |
