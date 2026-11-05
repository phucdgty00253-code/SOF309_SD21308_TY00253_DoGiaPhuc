# Saltair - Website thời trang (JAV102)

Website thương mại điện tử bán đồ basic phong cách biển của thương hiệu giả **Saltair**. Đồ án môn Java 1, FPT Polytechnic, nhóm 6 người.

Java Servlet/JSP thuần, mô hình **MVC + DAO + Service**, render phía server, một project Maven duy nhất.

- Công nghệ: Java 17, Servlet 6.1 / JSP 4 / JSTL 3 (Jakarta EE), JDBC thuần, MySQL 8, Maven, Tomcat 11.
- Mô tả đầy đủ chức năng theo role, quy tắc voucher và phí ship, trạng thái đơn hàng: xem file đặc tả dự án (file `.md` viết hoa ở thư mục gốc của repo).
- Việc cần làm: tab **Issues** của repo (mỗi issue có checklist nghiệm thu).

## 1. Chạy project trên máy mình

1. Cài **JDK 17**, **Maven**, **MySQL 8**, **Tomcat 11**.
2. Tạo database: mở MySQL rồi chạy lần lượt `database/schema.sql` và `database/data.sql`.
   `schema.sql` **xóa và tạo lại** toàn bộ bảng của database `saltair`, nên chỉ chạy trên máy của mình.
3. Sao chép `src/main/resources/db.properties.example` thành `db.properties` (cùng thư mục) rồi điền tài khoản MySQL của bạn. File này đã nằm trong `.gitignore`, **không commit**.
4. Build: `mvn package` (ra `target/saltair.war`) rồi deploy lên Tomcat 11, hoặc chạy từ IDE.
5. Mở `http://localhost:8080/saltair/home`.

Tài khoản demo (mật khẩu chung và các voucher mẫu ghi ở đầu `database/data.sql`):

| Role | Email |
|---|---|
| ADMIN | `admin@saltair.vn` |
| CUSTOMER | `khach@saltair.vn` |

Trong lúc chưa có đăng nhập thật có thể dùng `?as=customer`, `?as=admin`, `?as=guest` trên URL (do `DevPreviewFilter`, sẽ xóa khi xong).

## 2. Cấu trúc code (chia theo chức năng)

```
src/main/java/com/fashionweb/
  auth/      đăng nhập, đăng ký, quên mật khẩu, AuthFilter, AdminFilter
  account/   hồ sơ, địa chỉ, yêu thích
  shop/      trang chủ, sản phẩm, blog, danh mục, màu, size, banner
  cart/      giỏ hàng, thanh toán COD, voucher
  order/     đơn hàng của khách, đánh giá
  admin/     catalog (sản phẩm...), sales (đơn, voucher, bình luận), system (người dùng, banner, blog, dashboard)
  entity/ constant/ dto/ util/ filter/ listener/   dùng chung
  mock/      dữ liệu giả để xem giao diện, sẽ xóa khi làm xong logic
src/main/webapp/WEB-INF/views/   JSP, tên thư mục trùng tên package
```

Luồng gọi bắt buộc: **Servlet -> Service -> DAO -> MySQL**.

- Servlet không gọi thẳng DAO và không viết SQL. DAO chỉ chứa SQL (luôn dùng `PreparedStatement`). JSP chỉ hiển thị.
- Mỗi package có `package-info.java` mô tả chức năng và file cần làm. Service và DAO của một chức năng nằm **cùng package** với Servlet.
- Servlet đặt tên `...Servlet`, DAO `...DAO`, Service `...Service`. JSP viết thường nối bằng gạch ngang (`product-list.jsp`).
- Mật khẩu băm BCrypt, không lưu thô.

## 3. Quy trình làm việc với Git

Hai nhánh chính (không push thẳng vào hai nhánh này):

- `main`: bản ổn định để demo và nộp bài.
- `develop`: nhánh ghép code của cả nhóm.

**Làm một việc (issue):**

1. Nhận issue trên GitHub, đọc kỹ mô tả và checklist.
2. Cập nhật và tạo nhánh từ `develop`, tên theo mẫu `[loại]/[số-issue]-[tên-ngắn]`:

   ```bash
   git checkout develop
   git pull origin develop
   git checkout -b feature/12-cart-add-remove
   ```

   Loại nhánh: `feature` (tính năng mới), `bugfix` (sửa lỗi). Ví dụ: `feature/1-user-login`, `bugfix/23-fix-voucher-validation`.
3. Code, commit nhỏ và thường xuyên, rồi đẩy nhánh lên:

   ```bash
   git push -u origin feature/12-cart-add-remove
   ```
4. Mở **Pull Request vào `develop`**, điền mẫu PR và ghi `Closes #12` để issue tự đóng khi merge.
5. Trưởng nhóm review. Sửa theo góp ý, push thêm vào cùng nhánh. Review xong thì merge.

Trước khi mở PR nhớ `git pull origin develop` vào nhánh của mình và chạy thử, tránh xung đột.

**Quy tắc commit** (Conventional Commits): `<loại>(<phạm vi>): <mô tả ngắn>`

| Loại | Dùng khi |
|---|---|
| `feat` | thêm tính năng mới |
| `fix` | sửa lỗi |
| `refactor` | chỉnh code, không đổi hành vi |
| `docs` | cập nhật tài liệu |
| `chore` | việc nhỏ (cấu hình, thư viện, dọn dẹp) |

Phạm vi lấy theo package: `auth`, `account`, `shop`, `cart`, `order`, `admin-catalog`, `admin-sales`, `admin-system`, `database`, `ui`.

```
feat(auth): add user login with email
fix(cart): correct stock check when adding item
refactor(shop): extract price filter into ProductDAO
docs(readme): update setup steps
```

**File dùng chung** (`web.xml`, `style.css`, `app.js`, `schema.sql`, `data.sql`, `entity/`): sửa thì ghi rõ trong PR. Đổi `schema.sql` thì báo cả nhóm để chạy lại. Hai người cùng sửa một file Service hoặc DAO thì thêm hàm ở cuối file.

**Không commit**: `db.properties`, thư mục `target/`, `.idea/`, mật khẩu hay khóa bí mật.
