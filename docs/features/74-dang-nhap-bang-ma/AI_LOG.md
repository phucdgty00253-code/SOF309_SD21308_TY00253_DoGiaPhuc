# NHẬT KÝ DÙNG AI: Đăng nhập bằng email + mã 6 số (#74)
> *Ví dụ đã điền theo mẫu `_TEMPLATE/AI_LOG.md`.*

---

## Lần 1: Viết issue và đặc tả từ ảnh chụp luồng đăng nhập mẫu
* **Bước trong quy trình:** Bước 1, 2
* **Công cụ:** Claude Code

### 1. Ngữ cảnh & mục tiêu
- Có ảnh chụp luồng đăng nhập bằng mã của trang mẫu (bấm icon người, nhập email, nhận mã 6 số). Cần tách thành issue logic (#74) và issue giao diện (#75), có quy tắc cụ thể.

### 2. Prompt đã gửi
> "Hiện tại tôi đang cần sửa lại giao diện những chỗ sau, và tôi cần bạn tạo issue để tôi tạo nhánh và create pr để code rõ ràng các phần giao diện và logic sau: ... icon user ... đăng nhập bằng email luôn ... lần lượt có các logic step như hình."

### 3. Kết quả AI trả về
- Hai issue #74 và #75 với các mục Mô tả, Cần làm, Nghiệm thu; đề xuất hạn mã 15 phút, sai 5 lần, gửi lại sau 60 giây, tối đa 5 mã/giờ, chỉ lưu băm SHA-256.
- Đề xuất cột `users.marketing_opt_in` và giữ mật khẩu song song.
- Đưa ra 3 phương án đăng nhập cho admin.

### 4. Chỉnh sửa của người làm & lý do
- **Bỏ hẳn mật khẩu** thay vì giữ song song: tránh hai luồng đăng nhập, ít code phải bảo trì.
- **Chọn phương án 1 cho admin** (trang riêng, mã ngắn hạn hơn): tách rủi ro, admin không lẫn vào luồng khách.
- **Không thêm `marketing_opt_in`**: chỉ giữ cột thật sự dùng, để sơ đồ ERD và tài liệu gọn.
- **Thêm `auth.devCode`**: tài khoản mẫu trong `data.sql` là email giả nên không nhận được mail.

---

## Lần 2: Viết code và kiểm thử
* **Bước trong quy trình:** Bước 4, 5
* **Công cụ:** Claude Code

### 1. Ngữ cảnh & mục tiêu
- Code toàn bộ #74 theo đặc tả, gỡ phần mật khẩu, cập nhật bộ kiểm thử toàn luồng sang đăng nhập bằng mã.

### 2. Prompt đã gửi
> "Làm #74 trước"

### 3. Kết quả AI trả về
- Bảng `login_codes`, migration, `LoginCodeRepository`, `AuthService.requestCode/verifyCode`, 4 Servlet mới, sửa `AdminFilter`, xóa đăng ký/quên mật khẩu/đổi mật khẩu và `jbcrypt`.
- Bộ kiểm thử toàn luồng chạy 90/90.

### 4. Chỉnh sửa của người làm & lý do
- **Lỗi AI tự phát hiện khi chạy thử:** trang nhập mã dùng `${ctx}` trước khi `layout.tag` gán biến, form gửi sai đường dẫn. Đã sửa.
- **Rủi ro được ghi lại trong PR:** khi SMTP lỗi, trang admin chỉ báo lỗi với email admin nên có thể đoán email admin. Chấp nhận vì chỉ xảy ra khi cấu hình mail hỏng.
- **Sau khi merge:** trưởng nhóm sửa thêm kiểu trang nhập mã 6 ô và chống cache CSS cũ (commit `fix(auth)`), vì trình duyệt giữ bản `app.js` cũ làm icon người không mở được khung.
