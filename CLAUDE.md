# Fashion Web - JAV102

Website thương mại điện tử bán quần áo thời trang. Đồ án môn Java 1 (FPT Polytechnic), nhóm 6 người. Scale nhỏ, ưu tiên chạy ổn định.

## Kiến trúc

- Ứng dụng web **Java Servlet/JSP thuần**, mô hình **MVC + DAO + Service**, render phía server.
- **Một codebase duy nhất** (một project Maven), không tách Frontend/Backend, không dùng Spring Boot.

## Thương hiệu và phạm vi sản phẩm

- Thương hiệu giả: **Saltair**. Đồ basic phong cách biển, thoải mái, **unisex**. Khẩu hiệu gợi ý: "Đồ basic cho những ngày nắng".
- Dự án chỉ để học và thi, không dùng thương hiệu thật.
- Khoảng **24 sản phẩm**, **4 danh mục**:

| Danh mục | Sản phẩm mẫu |
|---|---|
| Áo thun và Polo | Thun cổ tròn, thun oversize, thun kẻ sọc ngang, polo, áo ba lỗ, thun dài tay |
| Áo nỉ và Hoodie | Hoodie, nỉ cổ tròn, hoodie zip, áo len mỏng, nỉ cổ lọ |
| Áo khoác | Áo khoác gió, bomber, áo khoác jeans, áo khoác linen, áo khoác dù |
| Quần | Short, quần ống suông, jogger, kaki, short biển, quần linen |

- **Màu** (6): trắng ngà, đen, xanh navy, xanh biển nhạt, xám, be cát.
- **Size** (4): S, M, L, XL.
- **Giá tham khảo**: áo thun 199.000 đến 349.000đ, hoodie và nỉ 399.000 đến 599.000đ, áo khoác 499.000 đến 899.000đ, quần 299.000 đến 549.000đ.
- Mỗi sản phẩm có 2 đến 3 màu. Dữ liệu tạo bằng script `database/data.sql`, không nhập tay.
- Không có danh mục phụ kiện.
- Danh sách đầy đủ 24 sản phẩm (giá, màu): `docs/product-catalog.csv` (dùng sinh `data.sql`).
- Ảnh sản phẩm, banner, danh mục đã tạo và gắn logo Saltair xong, nằm ở `src/main/webapp/assets/images/` (`products/`, `banners/`, `categories/`).

**Ảnh sản phẩm**
- 1 ảnh chính cho mỗi sản phẩm, nền trơn sáng, chỉ có sản phẩm (không người mẫu), không logo, không chữ. Màu hiển thị bằng ô chọn màu (swatch).
- Nguồn ảnh: ảnh do ChatGPT tạo, hoặc ảnh stock miễn phí bản quyền (Unsplash, Pexels, Pixabay) và phải đọc giấy phép. **Không dùng ảnh lấy từ web bán hàng của hãng khác.**
- Logo Saltair đã được gắn sẵn vào ảnh sản phẩm.
- Ảnh banner: lifestyle bãi biển sáng, khoảng 2 đến 3 ảnh.

## Phong cách giao diện

Tham khảo phong cách của template A-dam (light, airy, flat). **Chỉ mượn phong cách**, không dùng logo, ảnh hay câu chữ của họ.

**Số liệu thật đo từ a-dam.com** (kích thước, hiệu ứng, bố cục): `docs/a-dam-measurements.md`. Khi mô tả trong file này khác số liệu đo, **theo số liệu đo**.

**Màu**
- Nền trang `#ffffff`; ô ảnh, thẻ sản phẩm, hộp thông tin `#f4f4f4`
- Chữ chính và nút `#000e1f` (navy đậm), chữ phụ 60% của chữ chính
- Viền mảnh `#e6e7e9`, đường kẻ `#dcdddf`
- Màu nhấn `#0000c5` (xanh cobalt): thanh thông báo trên cùng và ngôi sao đánh giá
- Màu chức năng (lỗi, thành công, chờ xác nhận) chỉ dùng cho trạng thái

**Chữ**
- **Manrope** (Google Fonts, đủ dấu tiếng Việt), thay cho GT Walsheim vì là font trả phí. Chọn bằng cách đo độ rộng 11 chuỗi mẫu: sai lệch trung bình 2,3% so với font gốc
- Nội dung 15px/21px. Tiêu đề khối 37,6px, đậm 700. Tiêu đề lớn 57,6px, đậm 800, `letter-spacing: -2.3px`

**Hình khối**
- Nút, ô nhập, nhãn: `border-radius: 30px` (pill). Nút chính nền `#000e1f`, chữ trắng
- Thẻ sản phẩm: nền trắng, **không viền, không bo, không đổ bóng**
- Toàn site **không dùng box-shadow**

**Bố cục**
- Banner hero cao khoảng 610px, tràn ngang; lề hai bên nội dung 48px
- Trang danh mục: lưới 4 cột tràn rộng, khoảng cách 12px (2 cột trên điện thoại). Trang chủ: băng chuyền cuộn ngang khoảng 5 thẻ
- Điểm ngắt responsive: 990px và 750px
- Menu một hàng: liên kết bên trái, logo giữa, tìm kiếm, tài khoản, giỏ hàng bên phải; header dính trên cùng, thu nhỏ khi cuộn

**Trang chủ**: thanh thông báo -> menu (đè lên hero) -> banner -> dải 4 lợi ích -> hàng mới về -> khối chữ nền navy -> 2 thẻ ảnh lớn -> 4 ô danh mục -> bán chạy -> đánh giá khách hàng -> blog -> khối ưu đãi WELCOME10 -> footer.

**Không làm theo template**: chọn tiền tệ, ngôn ngữ, đăng ký định kỳ, tích điểm, hộp bật lên đăng ký email, huy hiệu đánh giá bên thứ ba.

**Hiệu ứng**: bám trang gốc (hover 0.2s, gạch chân liên kết chạy, menu thả xuống 0.15s, header thu nhỏ, băng chuyền); các hiệu ứng thêm ngoài trang gốc được đánh dấu `[EXTRA]` trong CSS/JS (hiện dần khi cuộn, hero zoom nhẹ, đếm số ở dashboard).

File CSS dùng chung: `webapp/assets/css/style.css` (khách hàng) và `admin.css` (quản trị); CSS riêng từng nhóm trang nằm ở `assets/css/pages/`. JS dùng chung: `assets/js/app.js` (kích hoạt bằng thuộc tính `data-*`).

## Chạy xem giao diện (khi chưa có backend)

- Mọi trang JSP đã có Servlet khung chỉ có `doGet`, lấy dữ liệu giả từ `com.fashionweb.mock.MockData`. Khi làm logic thật: thay `MockData.xxx()` bằng Service/DAO, thêm `doPost`, rồi xóa package `mock`.
- **Ảnh rỗng**: `MockData.SAMPLE_IMAGES = false` nên mọi ô ảnh hiện khung xám (chờ Admin tải ảnh lên). Đặt `true` để xem bằng ảnh mẫu trong `assets/images`.
- Giả lập đăng nhập bằng tham số URL (`DevPreviewFilter`, xóa khi làm xong đăng nhập thật): `?as=customer`, `?as=admin`, `?as=guest`.
- `demoMode` trong `web.xml` (mặc định `true`): form chưa có logic chỉ hiện thông báo. Đổi `false` khi nhóm làm xong logic.
- Trang nào cần dữ liệu gì: đọc comment đầu mỗi file JSP (route, thuộc tính request, kiểu dữ liệu) và Javadoc `TODO(logic)` trong Servlet tương ứng (việc cần làm, các `action` của form).
- Khung file cho thành viên: Servlet (đang dùng `MockData`), Service, DAO, Filter đã tạo sẵn trong từng package chức năng. Service và DAO chỉ có chữ ký phương thức, thân hàm ném `UnsupportedOperationException("TODO ...")`; `AuthFilter` và `AdminFilter` đang cho mọi request đi qua. Làm xong mục nào thì thay `MockData` ở Servlet bằng Service tương ứng; khi hết chỗ dùng thì xóa package `mock`.
- Cách chạy: Java 17, Tomcat 11 (Jakarta). Build bằng Maven (`mvn package`), deploy `target/saltair.war`, mở `/saltair/home`.

## Phân quyền (Roles)

Hệ thống có **3 role**:

| Role | Mã | Ghi chú |
|---|---|---|
| Guest | (không lưu DB) | Chưa đăng nhập. Chỉ xem sản phẩm công khai, đăng ký, đăng nhập. |
| Customer | `CUSTOMER` | Khách hàng đã đăng nhập, mua sắm. |
| Admin | `ADMIN` | Quản trị hệ thống. |

Cột `role` trong bảng user chỉ lưu `CUSTOMER` hoặc `ADMIN`.

## Chức năng theo role (ĐÃ CHỐT, tất cả đều phải làm)

### 1. GUEST (chưa đăng nhập)

- Xem trang chủ: banner, sản phẩm mới, sản phẩm nổi bật
- Xem danh sách sản phẩm có phân trang
- Lọc theo danh mục và khoảng giá
- Sắp xếp theo giá hoặc mới nhất
- Tìm kiếm theo tên
- Xem chi tiết sản phẩm: ảnh, mô tả, giá, màu, size, tồn kho, đánh giá
- Xem blog
- Đăng ký tài khoản
- Đăng nhập
- Quên mật khẩu
- Bấm "Thêm vào giỏ" hoặc "Mua" khi chưa đăng nhập: chuyển sang trang đăng nhập

### 2. CUSTOMER

Có mọi quyền xem của Guest, cộng thêm:

- Đăng xuất
- Xem và sửa profile (họ tên, SĐT)
- Đổi mật khẩu
- Sổ địa chỉ giao hàng: thêm, sửa, xóa, đặt mặc định
- Giỏ hàng: thêm sản phẩm theo màu và size, tăng giảm số lượng, xóa, kiểm tra tồn kho
- Danh sách yêu thích: thêm, bỏ, xem
- Đặt hàng: chọn địa chỉ giao, ghi chú, thanh toán COD
- Nhập mã giảm giá khi đặt hàng
- Xem lịch sử đơn hàng, lọc theo trạng thái
- Xem chi tiết đơn
- Hủy đơn khi đơn còn "Chờ xác nhận"
- Xác nhận "Đã nhận hàng"
- Đánh giá sao và bình luận sản phẩm, chỉ khi đơn đã hoàn thành
- Sửa hoặc xóa đánh giá của mình

### 3. ADMIN

- Đăng nhập trang quản trị
- **Sản phẩm**: xem danh sách, tìm theo tên, thêm, sửa, xóa mềm, khôi phục. Mỗi sản phẩm gắn biến thể (màu + size) và số lượng tồn
- **Danh mục**: thêm, sửa, xóa mềm, khôi phục
- **Màu và Size**: thêm, sửa, xóa
- **Đơn hàng**: xem danh sách, tìm theo tên khách hoặc mã đơn, lọc theo trạng thái, xem chi tiết, cập nhật trạng thái (Xác nhận, Đang giao, Hoàn thành), hủy đơn kèm lý do
- **Người dùng**: xem danh sách, tìm theo email, khóa hoặc mở khóa tài khoản, đổi role
- **Banner**: thêm, sửa, xóa, bật hoặc tắt hiển thị, sắp xếp thứ tự
- **Blog**: thêm, sửa, xóa, ẩn hoặc hiện bài viết
- **Mã giảm giá (voucher)**: thêm, sửa, vô hiệu hóa, xem số lần đã dùng, chọn danh mục áp dụng (xem mục "Quy tắc voucher và phí ship")
- **Bình luận**: xem theo sản phẩm, ẩn hoặc hiện bình luận
- **Thống kê**: tổng đơn, tổng doanh thu, lọc theo ngày bắt đầu và ngày kết thúc, top sản phẩm bán chạy
- **Thùng rác chung**: khôi phục dữ liệu đã xóa mềm

### Không làm

Role Staff và Marketing, thanh toán online (VNPay, MoMo, QR), đăng nhập Google, quản lý nhập kho riêng, đổi trả hàng, export Excel, log thao tác, màn hình Admin để cấu hình phí ship (phí ship là hằng số cố định trong code).

## Trạng thái đơn hàng

`Chờ xác nhận -> Đang giao -> Hoàn thành`, và nhánh `Đã hủy`.

- Customer chỉ hủy được khi đơn là "Chờ xác nhận".
- Khi đặt hàng, trừ tồn kho. Khi hủy đơn, cộng trả lại.
- Chỉ đơn "Hoàn thành" mới được đánh giá.

## Quy tắc voucher và phí ship

**Thông tin một voucher**
- Mã: duy nhất, viết hoa, không dấu, không sửa sau khi tạo (ví dụ `SALE10`).
- Loại giảm: `PERCENT` (1 đến 100) hoặc `AMOUNT` (số tiền > 0).
- Giảm tối đa: chỉ dùng cho `PERCENT`, có thể để trống (không giới hạn).
- Đơn tối thiểu: tổng tiền hàng của cả đơn phải từ mức này trở lên (có thể bằng 0).
- Ngày bắt đầu, ngày kết thúc.
- Tổng lượt dùng của voucher.
- Số lần mỗi khách được dùng (Admin đặt, mặc định 1).
- Danh mục áp dụng: Admin chọn một danh mục, hoặc để trống để áp dụng cho mọi danh mục.
- Trạng thái: hoạt động hoặc vô hiệu hóa.

**Quy tắc áp dụng**
- Mỗi đơn dùng tối đa 1 voucher.
- Voucher có danh mục áp dụng: chỉ dùng được khi đơn có ít nhất 1 sản phẩm thuộc danh mục đó.
- Khi hợp lệ, giảm giá tính trên **tổng tiền hàng của cả đơn** (không chỉ riêng sản phẩm thuộc danh mục).
- `PERCENT`: giảm = tiền hàng x % (không vượt "giảm tối đa" nếu có). `AMOUNT`: giảm = số tiền cố định. Giảm giá không vượt quá tiền hàng.

**Phí ship**
- Mỗi đơn có phí ship cố định **30.000đ**, đặt thành hằng số trong package `constant` (không lưu DB, Admin không cấu hình).
- Đơn dùng voucher hợp lệ thì **phí ship bằng 0**.
- `tổng thanh toán = tiền hàng - giảm giá + phí ship`.

**Kiểm tra khi khách nhập mã** (báo lỗi theo thứ tự)
1. Mã không tồn tại
2. Voucher bị vô hiệu hóa
3. Chưa đến hạn hoặc đã quá hạn
4. Hết tổng lượt dùng
5. Khách đã dùng đủ số lần cho phép
6. Chưa đạt đơn tối thiểu
7. Đơn không có sản phẩm thuộc danh mục áp dụng

**Vòng đời lượt dùng**
- Tăng lượt dùng khi đặt hàng thành công.
- Đơn bị hủy (do khách hoặc Admin) thì trả lại lượt dùng.
- Đơn lưu sẵn mã voucher, số tiền được giảm và phí ship tại thời điểm đặt, sửa voucher sau này không đổi đơn cũ.
- Số lần khách đã dùng một voucher = số đơn của khách có mã đó và chưa bị hủy (không cần bảng riêng).

**Quy tắc cho Admin**
- Voucher đã có người dùng thì không xóa, chỉ vô hiệu hóa.
- Khi tạo: ngày kết thúc không trước ngày bắt đầu, `PERCENT` không vượt 100, tổng lượt dùng > 0.

**Dữ liệu mẫu để demo**
- `WELCOME10`: giảm 10%, tối đa 50.000đ, đơn từ 200.000đ, mọi danh mục
- `GIAM50K`: giảm 50.000đ, đơn từ 500.000đ, gắn một danh mục cụ thể
- Một voucher đã hết hạn và một voucher đã hết lượt để test thông báo lỗi

## Tech stack

Đã chốt:
- **Java 17**, Servlet 6.1 / JSP 4 (**Jakarta EE, `jakarta.*`**), JSTL 3 (`jakarta.tags.core`, `jakarta.tags.fmt`, `jakarta.tags.functions`), EL (không viết Java trong JSP)
- JDBC thuần với `PreparedStatement`
- Database: **MySQL 8**
- Maven (`pom.xml`, đóng gói WAR `saltair.war`), **Apache Tomcat 11**
- HTML5, CSS3, JavaScript thuần (không framework, không Bootstrap)
- Font Manrope (Google Fonts), bộ icon đường nét kiểu Lucide (sprite SVG nội bộ)
- Git + GitHub

Chưa chốt:
- [ ] Cách làm "Quên mật khẩu" (gửi email bằng JavaMail + SMTP, hay cách đơn giản hơn)

## Design pattern áp dụng

| Pattern | Dùng ở đâu | Tác dụng |
|---|---|---|
| **MVC** | Toàn project: Servlet = Controller, JSP = View, `entity`/`dao`/`service` = Model | Tách giao diện, điều phối, dữ liệu |
| **DAO** | Package `dao` | Gom toàn bộ SQL vào một chỗ |
| **Service Layer** | Package `service` | Logic nghiệp vụ và transaction giữa Controller và DAO |
| **Intercepting Filter** | Package `filter` | Kiểm tra đăng nhập, quyền Admin, encoding trước khi vào Servlet |
| **Singleton** (tùy chọn) | `DBContext` | Quản lý kết nối DB tại một nơi |

Không dùng Front Controller: mỗi chức năng có Servlet riêng.

**Luồng gọi bắt buộc:** `Servlet -> Service -> DAO -> MySQL`
- Servlet không gọi thẳng DAO, không viết SQL.
- DAO chỉ chứa SQL, không chứa nghiệp vụ.
- JSP chỉ hiển thị, không gọi DAO/Service.

## Cấu trúc package (chia theo chức năng)

Mỗi chức năng một thư mục. Một thành viên nhận chức năng nào thì làm gần như trong một thư mục Java và một thư mục JSP. Vẫn là MVC + DAO + Service, luồng gọi không đổi.

```
src/main/java/com/fashionweb/
  entity/            16 lớp ánh xạ bảng (dùng chung)
  constant/ dto/ util/ filter/ listener/    dùng chung (Role, OrderStatus, AppConstant, Page, DBContext, EncodingFilter...)
  mock/              DỮ LIỆU GIẢ chỉ để xem giao diện, xóa khi làm logic thật
  auth/              đăng nhập, đăng ký, quên và đặt lại mật khẩu, đăng xuất, AuthFilter, AdminFilter
  account/           hồ sơ, đổi mật khẩu, sổ địa chỉ, yêu thích
  shop/              trang chủ, danh sách và chi tiết sản phẩm, tìm kiếm, blog, giới thiệu
  cart/              giỏ hàng, thanh toán COD, voucher, phí ship
  order/             đơn hàng của khách (danh sách, chi tiết, hủy, đã nhận), đánh giá
  admin/
    catalog/         sản phẩm, danh mục, màu và size, thùng rác
    sales/           đơn hàng, voucher, bình luận
    system/          dashboard, người dùng, banner, blog
    AdminOpsParams   hàm đọc tham số an toàn dùng chung cho Servlet admin

src/main/webapp/
  assets/            css (style.css, admin.css, pages/), js (app.js, pages/), images, brand
  WEB-INF/
    web.xml
    tags/            layout.tag, admin-layout.tag, icon, img, price, stars, product-card, pagination
    views/
      auth/  account/  shop/  cart/  order/      JSP khách hàng, tên thư mục trùng tên package Java
      admin/catalog/  admin/sales/  admin/system/   JSP quản trị
      common/        icons, flash, account-nav (dùng chung)
      error/         403, 404, 500
```

Trong mỗi package chức năng có `package-info.java` ghi: chức năng, Servlet đã có khung, Service/DAO cần tạo, thư mục JSP tương ứng. Service và DAO của một chức năng đặt **cùng package** với Servlet của chức năng đó (ví dụ `shop/ProductService`, `shop/ProductDAO`); gói `admin` dùng lại Service/DAO của các gói khác.

Tên package đã chốt: `entity` (không dùng `model`), có tầng `service`, chia theo chức năng.

## Quy ước sơ bộ

- Servlet đặt tên kết thúc bằng `Servlet`, DAO bằng `DAO`, Service bằng `Service`.
- JSP viết thường, nối bằng gạch ngang (`product-list.jsp`).
- URL: `/home`, `/products`, `/product?slug=`, `/cart`, `/checkout`, `/orders`, `/order?code=`, `/login`, `/register`, `/admin/*`.
- Mọi câu SQL dùng `PreparedStatement`.
- Mật khẩu băm bằng BCrypt, không lưu thô.
- Thông tin đăng nhập và role lưu trong `HttpSession`.

## Còn cần chốt

- [x] Logo Saltair: đã có, lưu ở `src/main/webapp/assets/brand/` (`saltair-logo.png`, `saltair-logo-white.png`, bản gốc `saltair-logo-original.webp`)
- [x] Gắn logo vào ảnh sản phẩm (đã xong, ảnh ở `src/main/webapp/assets/images/`)
- [x] Màu nhấn cobalt, font Manrope, responsive điện thoại (điểm ngắt 990px và 750px)
- [x] Giao diện JSP của toàn bộ trang và khung file (Servlet, Service, DAO, Filter)
- [ ] Database schema: bản nháp ở `database/schema.sql` (17 bảng), chờ team review, chưa chạy thử trên MySQL. Còn thiếu `database/data.sql`
- [ ] Code convention đầy đủ
- [ ] Quy trình GitHub (branch, PR, giao task)
- [ ] Phân công 6 thành viên
