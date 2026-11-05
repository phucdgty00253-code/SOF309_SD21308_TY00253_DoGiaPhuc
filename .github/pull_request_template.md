## Tóm tắt

<!-- Làm gì, vì sao. Một đến ba câu. -->

## Issue

Closes #

## Đã làm

-

## Cách kiểm tra

<!-- Các bước để người review chạy thử, ví dụ: đăng nhập khach@saltair.vn, thêm sản phẩm vào giỏ... -->

1.

## Ảnh chụp màn hình

<!-- Nếu có thay đổi giao diện. Bỏ trống nếu không. -->

## Thay đổi cơ sở dữ liệu / cấu hình

<!-- Có sửa schema.sql, data.sql, web.xml, pom.xml không? Người khác cần làm gì sau khi pull? Nếu không thì ghi "Không". -->

- [ ] Không có
- [ ] Có (mô tả bên dưới):

## Checklist

- [ ] Nhánh tách từ `develop`, PR vào `develop`, đã `git pull origin develop` trước khi mở PR
- [ ] Đúng luồng `Servlet -> Service -> DAO`; Servlet không viết SQL, JSP không gọi DAO/Service
- [ ] SQL dùng `PreparedStatement`, không nối chuỗi từ dữ liệu người dùng
- [ ] Đã bỏ `MockData.xxx()` ở phần mình làm
- [ ] Đã chạy thử trên máy mình, các mục nghiệm thu trong issue đều đạt
- [ ] Không commit `db.properties`, `target/`, mật khẩu
- [ ] Commit theo dạng `feat(scope): ...`
