# ĐẶC TẢ BỐI CẢNH DỰ ÁN (SPECIFICATIONS)
> *File RIÊNG của dự án. Dùng làm ngữ cảnh (Context) gửi cho AI ở bước Phân tích và Test, và là nơi duy nhất [`HUONG_DAN_QUY_TRINH_PHAT_TRIEN.md`](HUONG_DAN_QUY_TRINH_PHAT_TRIEN.md) tham chiếu khi cần thông tin cụ thể.*
> *Sang dự án mới: giữ nguyên các mục, viết lại nội dung.*

---

## 1. THÔNG TIN DỰ ÁN
* **Tên đề tài:** Saltair - website bán quần áo basic (môn JAV102)
* **Repo:** `nhan0x/saltair-fashion-web`
* **Trưởng nhóm:** `nhan0x` · **Thành viên:** `HoangMinh219`
* **Nghiệp vụ chi tiết** (role, chức năng, trạng thái đơn, voucher, phí ship): [`project.md`](../project.md)

## 2. CÔNG NGHỆ & CÁCH CHẠY
* **Công nghệ:** Java 17, Jakarta Servlet 6.1 / JSP 4 / JSTL 3, JDBC `PreparedStatement`, MySQL 8, Tomcat 11, Maven (WAR), HTML/CSS/JS thuần.
* **URL chạy local:** `http://localhost:8080/saltair/home`
* **Lệnh build:** `mvn package` → deploy `target/saltair.war`
* **Lệnh test:** `mvn test` (JUnit 5, thư mục `src/test/java/com/fashionweb/`)
* **Tạo database:** chạy `database/schema.sql` rồi `database/data.sql`
* **Cấu hình không commit** (copy từ file `.example`): `db.properties`, `mail.properties`, `cloudinary.properties` trong `src/main/resources/`

## 3. KIẾN TRÚC & QUY TẮC CODE
* **Luồng bắt buộc:** `Servlet -> Service -> DAO -> MySQL` (MVC + DAO + Service Layer).
  * Servlet: đọc tham số, gọi Service, forward JSP hoặc redirect. Không viết SQL.
  * Service: nghiệp vụ, kiểm tra dữ liệu, ném `ValidationException` với câu tiếng Việt.
  * DAO: chỉ SQL, luôn `PreparedStatement`.
  * JSP: chỉ hiển thị, đầu file ghi comment route, thuộc tính request, các form.
* **Package:** chia theo tầng rồi theo chức năng: `controller|service|dao/<auth|account|shop|cart|order|admin>/`.
* **Đổi database:** sửa `schema.sql`, `data.sql` (nếu dữ liệu mẫu đổi) và thêm `database/migration_<số>_<tên>.sql`.
* **Giao diện:** chỉ animate `transform`/`opacity`, hỗ trợ `prefers-reduced-motion`, điểm ngắt 990px và 750px. Chỉ mượn bố cục/hiệu ứng của trang mẫu, không dùng logo, ảnh, chữ của họ.

## 4. QUY ƯỚC GIT
* **Nhánh phát triển chính:** `develop` (mặc định). `main` chỉ nhận bản ổn định từ `develop`.
* **Tên nhánh:** `feature/<số-issue>-<ten-ngan>`, `fix/<số-issue>-<ten-ngan>`, `docs/<ten-ngan>`.
* **Commit:** `<loại>(<phạm vi>): <việc đã làm> (#<số-issue>)`, viết tiếng Việt. Phạm vi: `auth`, `account`, `shop`, `cart`, `order`, `admin`, `ui`, `db`.
* **Merge:** *Create a merge commit*, repo đã bật tự xóa nhánh sau khi merge.

## 5. NHÃN ISSUE
| Nhóm | Nhãn |
| :--- | :--- |
| Loại | `logic`, `giao-dien`, `feature`, `bug`, `chore`, `documentation` |
| Mảng | `auth`, `account`, `shop`, `cart`, `order`, `admin` |
| Người nhận | `nguoi-L` (trưởng nhóm), `nguoi-D` (Minh) |
| Trạng thái | `can-quyet-dinh` (chờ trưởng nhóm chốt) |

## 6. DỮ LIỆU MẪU ĐỂ TEST
* **Bật `auth.devCode=true`** trong `mail.properties`: mã đăng nhập hiện trên trang (email mẫu là email giả).
* **Tài khoản:** `admin@saltair.vn` (ADMIN, đăng nhập ở `/admin/login`), `khach@saltair.vn` (có giỏ, đơn đủ trạng thái), `lan.tran@example.com`, `hoang.le@example.com`, `duc.vo@example.com`, `mai.pham@example.com` (đang bị khóa).
* **Voucher:** `WELCOME10`, `GIAM50K` (dùng được); `HETHAN20` (hết hạn), `HETLUOT` (hết lượt), `TAMDUNG` (vô hiệu hóa).

## 7. CHECKLIST RIÊNG CỦA DỰ ÁN (DÙNG KHI REVIEW PR)
- [ ] Đúng luồng `Servlet -> Service -> DAO`; Servlet không viết SQL, JSP không gọi DAO/Service.
- [ ] SQL dùng `PreparedStatement`, không nối chuỗi từ dữ liệu người dùng.
- [ ] Không commit `*.properties` thật, `target/`, mật khẩu, khóa API.
- [ ] Đổi `schema.sql` thì có `database/migration_<số>_<tên>.sql`.
- [ ] Giao diện đúng ở 1440px và 375px, Console không có lỗi đỏ.

---

## 8. ĐẶC TẢ MÀN HÌNH, TRƯỜNG DỮ LIỆU VÀ RÀNG BUỘC (DÙNG CHO KIỂM THỬ SOF309)
> *Đối chiếu trực tiếp với mã nguồn: `controller/`, `service/auth/AuthService`, `service/cart/CartService`. Đây là đầu vào cho AI RAG ở bước sinh Test Plan / Test Case (xem `ai-logs/01_test_design_ai.md`).*
> *Lưu ý: đăng nhập hiện tại là **email + mật khẩu** (`AuthService.login`). Phần "mã 6 số" trong `project.md` là thiết kế cũ, không còn dùng.*

### 8.1. Bảng URL
| URL | Phương thức | Servlet | Ai dùng | Hành vi chính |
| :--- | :---: | :--- | :--- | :--- |
| `/home` | GET | `HomeServlet` | Mọi người | Trang chủ |
| `/products` | GET | `ProductListServlet` | Mọi người | Danh sách có lọc, sắp xếp, tìm kiếm, phân trang (12 sản phẩm/trang) |
| `/product?slug=` | GET, POST | `ProductDetailServlet` | GET: mọi người; POST: khách đăng nhập | Chi tiết sản phẩm; gửi/sửa/xóa đánh giá |
| `/cart` | GET, POST | `CartServlet` | Mọi người | Xem giỏ; `action` = `add`, `update`, `remove`, `note` |
| `/login` | GET, POST | `LoginServlet` | Mọi người | Đăng nhập khách và Admin (một form) |
| `/admin/login` | GET | `LoginServlet` | Mọi người | Chuyển về `/login` (đường dẫn cũ) |
| `/register` | GET, POST | `RegisterServlet` | Khách chưa đăng nhập | Tạo tài khoản CUSTOMER và đăng nhập luôn |
| `/logout` | GET | `LogoutServlet` | Đã đăng nhập | Hủy session, về `/home` |
| `/admin/*` | GET, POST | `Admin*Servlet` | ADMIN | Chưa đăng nhập: chuyển `/login?redirect=...` |

### 8.2. Trường dữ liệu và ràng buộc

**Đăng nhập `POST /login`**
| Trường | Ràng buộc | Thông báo lỗi (field) |
| :--- | :--- | :--- |
| `email` | Bắt buộc; sau khi trim + chữ thường dài ≤ 150; đúng dạng `x@y.z` (không khoảng trắng, đúng 1 `@`) | "Vui lòng nhập email." / "Email không đúng định dạng." (`email`) |
| `password` | Bắt buộc (không rỗng) | "Vui lòng nhập mật khẩu." (`password`) |
| (kết quả) | Sai email hoặc sai mật khẩu dùng chung một câu | "Email hoặc mật khẩu không đúng." (`password`) |
| (kết quả) | Tài khoản `is_active = 0` | "Tài khoản đã bị khóa..." (`password`) |
| `redirect` | Chỉ nhận đường dẫn nội bộ bắt đầu bằng `/`; loại `//...`, `/\...`, URL tuyệt đối, có CR/LF, `/login`, `/logout`, `/register`, `/admin/login`. Khách không được về `/admin...`; Admin không có redirect `/admin...` thì về `/admin` | Bỏ qua, về `/home` hoặc `/admin` |

**Đăng ký `POST /register`**
| Trường | Ràng buộc | Thông báo lỗi (field) |
| :--- | :--- | :--- |
| `email` | Như đăng nhập; chưa có tài khoản (hoặc có nhưng chưa đặt mật khẩu) | "Email này đã có tài khoản." (`email`) |
| `fullName` | Bắt buộc sau khi trim; ≤ 100 ký tự | "Vui lòng nhập họ tên." / "Họ tên tối đa 100 ký tự." (`fullName`) |
| `password` | 8 đến 64 ký tự | "Mật khẩu tối thiểu 8 ký tự." / "Mật khẩu tối đa 64 ký tự." (`password`) |
| `confirm` | Trùng `password` | "Mật khẩu nhập lại không khớp." (`confirm`) |

**Giỏ hàng `POST /cart`**
| `action` | Tham số | Ràng buộc | Kết quả |
| :--- | :--- | :--- | :--- |
| `add` | `variantId`, `quantity` | `variantId` > 0 (sai/thiếu/âm: "Vui lòng chọn màu và size."); `quantity` mặc định 1 khi rỗng/sai; Service nhận 1 đến 10 ("Số lượng mỗi lần thêm từ 1 đến 10."); tổng trong giỏ không vượt tồn kho | Redirect `/cart` (lỗi thì quay lại trang trước theo `Referer` cùng ứng dụng) |
| `update` | `itemId`, `quantity` | `quantity` ≤ 0 thì xóa dòng; không vượt tồn kho | Redirect `/cart` |
| `remove` | `itemId` | — | Xóa dòng, flash "Đã xóa sản phẩm khỏi giỏ hàng." |
| `note` | `note` | Cắt khoảng trắng; tối đa 500 ký tự (dài hơn bị cắt) | Lưu session, flash "Đã lưu ghi chú." |
| khác / thiếu | — | — | HTTP 400 |
* Khách chưa đăng nhập: giỏ lưu trong session (`guestCart`); đăng nhập thì gộp vào giỏ DB. Phí ship cố định 30.000đ.

**Danh sách sản phẩm `GET /products`** (mọi tham số đều tùy chọn, giá trị sai bị bỏ qua, không báo lỗi)
| Tham số | Ràng buộc |
| :--- | :--- |
| `q` | Cắt khoảng trắng; tối đa 100 ký tự (dài hơn bị cắt) |
| `category` | Slug danh mục có thật, không thì bỏ |
| `minPrice`, `maxPrice` | Số nguyên ≥ 0; âm/không phải số bị bỏ; `minPrice > maxPrice` thì đổi chỗ |
| `color` | Mã màu có trong danh sách màu (phân biệt hoa thường) |
| `size` | `S`, `M`, `L`, `XL` (không phân biệt hoa thường) |
| `sort` | `newest`, `price_asc`, `price_desc`, `bestseller`, `rating`; khác thì dùng `newest` |
| `page` | Số nguyên ≥ 1; sai thì trang 1; vượt tổng số trang thì về trang cuối |

**Chi tiết sản phẩm `/product`**
* `GET slug`: thiếu hoặc không tìm thấy thì HTTP 404. Điểm sao trung bình làm tròn 1 chữ số; số sao ngoài 1..5 được kẹp về biên.
* `POST action=review` (`productId`, `rating` 1..5, `comment` 10..1000 ký tự): chỉ khách có đơn "Hoàn thành" chứa sản phẩm; chưa đăng nhập thì chuyển `/login?redirect=...`; `action` lạ thì HTTP 400.
* `POST action=deleteReview` (`productId`): xóa đánh giá của chính mình.

### 8.3. Quy tắc nghiệp vụ liên quan đến test
* Phí ship 30.000đ, bằng 0 khi dùng voucher hợp lệ; tổng thanh toán = tiền hàng − giảm giá + phí ship.
* Voucher kiểm tra theo thứ tự: không tồn tại → vô hiệu hóa → chưa/đã hết hạn → hết lượt tổng → khách dùng đủ lượt → chưa đạt đơn tối thiểu → sai danh mục.
* Lỗi nhập liệu: Service ném `ValidationException` (có `errors` theo ô), Servlet hiện lại form. Lỗi hệ thống: `DataAccessException` không bị Servlet nuốt (Tomcat hiện trang 500).
