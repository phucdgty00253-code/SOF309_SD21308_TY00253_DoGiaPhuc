## Tóm tắt

<!-- Làm gì, vì sao. Một đến ba câu. -->

## Issue

Closes #

## Tài liệu feature

<!-- Đường dẫn thư mục docs/features/<số>-<tên>/ (SPEC.md, TEST_CASES.md, AI_LOG.md). Việc rất nhỏ thì ghi "Không cần". Xem docs/QUY_TRINH_PHAT_TRIEN_FEATURE.md -->

- `docs/features/`

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
- [ ] Đúng luồng `Servlet -> Service -> Repository`; Servlet không viết SQL, JSP không gọi Repository/Service
- [ ] SQL dùng `PreparedStatement`, không nối chuỗi từ dữ liệu người dùng
- [ ] Đã chạy thử trên máy mình, các mục nghiệm thu trong issue đều đạt
- [ ] `TEST_CASES.md` của feature đã điền Pass/Fail, `mvn test` xanh
- [ ] Đổi `schema.sql` thì có file `database/migration_<số>_<tên>.sql`
- [ ] Không commit `db.properties`, `mail.properties`, `cloudinary.properties`, `target/`, mật khẩu
- [ ] Commit theo dạng `feat(scope): ... (#số)`
