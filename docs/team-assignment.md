# Phân công nhóm

Mỗi người nhận một mảng chức năng, gồm các issue có nhãn riêng trên GitHub.

## Tổng quan

| Nhãn | Thành viên | GitHub | Mảng phụ trách | Issue | Số việc |
|---|---|---|---|---|---|
| `nguoi-A` | Thương | `thuongnvhtv00417-cell` | Tài khoản và bảo mật | #1 #2 #4 #5 #6 | 5 |
| `nguoi-B` | Nam | `thanhnam1x` | Hiển thị sản phẩm, trang chủ, nội dung | #7 đến #11 | 5 |
| `nguoi-C` | Khoa | `khoanbtv00444` | Giỏ hàng, voucher, thanh toán | #12 đến #15 | 4 |
| `nguoi-D` | Minh | `HoangMinh219` | Đơn hàng, đánh giá | #16 đến #21 | 6 |
| `nguoi-E` | Châu | `chauthkty00256-hue` | Dữ liệu sản phẩm: danh mục, màu, size, sản phẩm | #22 đến #26 | 5 |
| `nguoi-L` | Trưởng nhóm | `nhan0x` | Review, merge, Quên mật khẩu (gửi email SMTP), tích hợp, kiểm thử toàn luồng | #3 #27 | 2 |

## Chi tiết từng người

### Thương (A): tài khoản và bảo mật

| Issue | Việc | Giai đoạn |
|---|---|---|
| #1 | Đăng nhập, đăng xuất, đăng ký | Phase 1 |
| #2 | AuthFilter và AdminFilter (phân quyền) | Phase 1 |
| #4 | Hồ sơ cá nhân và đổi mật khẩu | Phase 2 |
| #5 | Sổ địa chỉ giao hàng và danh sách yêu thích | Phase 2 |
| #6 | Quản lý người dùng | Phase 3 |

### Nam (B): hiển thị sản phẩm, trang chủ, nội dung

| Issue | Việc | Giai đoạn |
|---|---|---|
| #7 | Danh sách sản phẩm: phân trang, lọc, sắp xếp, tìm kiếm | Phase 1 |
| #8 | Chi tiết sản phẩm: ảnh, màu, size, tồn kho, đánh giá | Phase 1 |
| #9 | Trang chủ và trang giới thiệu | Phase 2 |
| #10 | Blog (xem bài) và quản lý blog | Phase 2 |
| #11 | Quản lý banner | Phase 3 |

### Khoa (C): giỏ hàng, voucher, thanh toán

| Issue | Việc | Giai đoạn |
|---|---|---|
| #12 | Giỏ hàng: thêm, tăng giảm, xóa, kiểm tra tồn kho | Phase 1 |
| #13 | Kiểm tra mã giảm giá và tính giảm giá (VoucherService) | Phase 1 |
| #14 | Thanh toán COD và đặt hàng (transaction) | Phase 2 |
| #15 | Quản lý voucher | Phase 3 |

### Minh (D): đơn hàng, đánh giá

| Issue | Việc | Giai đoạn |
|---|---|---|
| #16 | Lịch sử đơn hàng và chi tiết đơn | Phase 2 |
| #17 | Hủy đơn và xác nhận đã nhận hàng | Phase 2 |
| #18 | Đánh giá sản phẩm (tạo, sửa, xóa) | Phase 2 |
| #19 | Quản lý đơn hàng: danh sách, tìm kiếm, lọc | Phase 3 |
| #20 | Cập nhật trạng thái đơn và hủy đơn kèm lý do | Phase 3 |
| #21 | Quản lý bình luận (ẩn, hiện) | Phase 3 |

### Châu (E): dữ liệu sản phẩm

| Issue | Việc | Giai đoạn |
|---|---|---|
| #22 | Danh mục: thêm, sửa, xóa mềm, khôi phục (**làm đầu tiên**) | Phase 1 |
| #23 | Quản lý màu và size | Phase 1 |
| #24 | Sản phẩm: danh sách, thêm, sửa, biến thể và tồn kho | Phase 2 |
| #25 | Xóa mềm, khôi phục sản phẩm và Thùng rác chung | Phase 3 |
| #26 | Dashboard thống kê | Phase 3 |

### Trưởng nhóm

| Issue | Việc | Giai đoạn |
|---|---|---|
| #3 | Quên mật khẩu và đặt lại mật khẩu (gửi email thật qua SMTP, làm sau khi #1 merge) | Phase 2 |
| #27 | Xóa dữ liệu giả, tắt chế độ demo, kiểm thử toàn luồng | Phase 3 |

## Thứ tự làm

| Giai đoạn | Việc | Ghi chú |
|---|---|---|
| **Phase 1** (nền tảng, làm trước) | A: #1 #2. B: #7 #8. C: #12 #13. E: #22 #23 | E làm danh mục (#22) đầu tiên vì nhiều trang cần danh mục |
| **Phase 2** | A: #4 #5. B: #9 #10. C: #14. D: #16 #17 #18. E: #24. L: #3 | D đọc được đơn mẫu trong `data.sql`, không cần chờ #14 xong |
| **Phase 3** | A: #6. B: #11. C: #15. D: #19 #20 #21. E: #25 #26. L: #27 | Làm cuối, #27 chỉ làm khi mọi người xong |

Mỗi issue có sẵn: mô tả, danh sách file cần làm, và tiêu chí nghiệm thu. Đọc kỹ phần **Nghiệm thu** trước khi mở PR.

## Quy tắc ngắn gọn

1. Mỗi issue một nhánh: `feature/<số-issue>-<tên-ngắn>`, tách từ `develop`.
2. Commit theo dạng `feat(<scope>): ...`.
3. Mở PR vào `develop`, ghi `Closes #<số-issue>`, điền đủ mẫu PR.
4. **Không** push thẳng vào `develop` hoặc `main`, **không** tự merge.
5. Kẹt quá 30 phút thì hỏi ngay trong nhóm.

## File dùng chung, dễ xung đột

`web.xml`, `style.css`, `schema.sql`, thư mục `entity/`. Sửa các file này thì ghi rõ trong PR, và `git pull origin develop` trước khi mở PR.
