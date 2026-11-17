# BỘ KỊCH BẢN KIỂM THỬ (TEST CASES)
> Sinh từ [SPECIFICATIONS.md](SPECIFICATIONS.md) mục 8 bằng **Phân vùng tương đương** và **Phân tích giá trị biên** (xem [ai-logs/01_test_design_ai.md](ai-logs/01_test_design_ai.md)).
> **Cột "Tự động"**: class test JUnit chứa ca đó (`src/test/java/com/fashionweb/controller/...`); "Tay" = kiểm tra tay trên web.
> **Trạng thái** ghi theo lần chạy 17/11/2026: JUnit `mvn test` (218 ca, 0 lỗi) và gọi thật vào web chạy trên Tomcat 11 bằng `curl` (ghi chú "web thật"). "(mock)" nghĩa là chỉ chạy bằng JUnit vì cần mật khẩu tài khoản mẫu hoặc ghi vào DB.

## 1. Đăng nhập (`/login`)
| Test Case ID | Chức năng | Mô tả ca kiểm thử | Loại | Các bước thực hiện | Dữ liệu kiểm thử | Kết quả mong đợi | Tự động | Trạng thái |
| :---: | :---: | :--- | :---: | :--- | :--- | :--- | :---: | :---: |
| **TC-AUTH-01** | Login | Email/mật khẩu sai biên hiện lại form kèm lỗi theo ô | Neg | 1. Mở `/login` 2. Nhập dữ liệu 3. Bấm Đăng nhập | email: `""`, `"   "`, `abc`, `abc@`, `a@b`, `a b@saltair.vn`; mật khẩu `""` với email đúng | Ở lại form; "Vui lòng nhập email." / "Email không đúng định dạng." / "Vui lòng nhập mật khẩu." | LoginServletTest | Pass (web thật) |
| **TC-AUTH-02** | Login | Email vượt biên 150 ký tự | Neg (biên) | Nhập email 151 ký tự | `a`×146 + `@b.vn` | "Email không đúng định dạng." | LoginServletTest | Pass |
| **TC-AUTH-04** | Login | Sai mật khẩu, không lộ email có tồn tại | Neg | Nhập email đúng, mật khẩu sai | `khach@saltair.vn` / `saimk` | "Email hoặc mật khẩu không đúng.", không có session | LoginServletTest | Pass (web thật) |
| **TC-AUTH-05** | Login | Khách đăng nhập đúng | Pos | Nhập tài khoản đúng | `khach@saltair.vn` + mật khẩu hợp lệ | Lưu session, về `/home` | LoginServletTest | Pass (mock) |
| **TC-AUTH-06** | Login | Redirect của khách | Pos/Neg | POST kèm `redirect` | `/cart` ✔; `//evil.com`, `https://evil.com`, `/admin/orders`, `/login` ✘ | Hợp lệ về đúng trang; còn lại về `/home` | LoginServletTest | Pass |
| **TC-AUTH-07** | Login | Admin đăng nhập | Pos | Đăng nhập `admin@saltair.vn` | redirect: rỗng, `/home`, `/admin/orders`, `//evil.com` | Về `/admin` hoặc `/admin/...` đang xem dở; phiên 1800s; gửi mail báo | LoginServletTest | Pass (mock) |
| **TC-AUTH-08** | Login | Lỗi DB không bị nuốt | Neg | Service ném `DataAccessException` | — | Ngoại lệ được ném tiếp (trang 500), không forward | LoginServletTest | Pass |
| **TC-AUTH-09/10** | Login | `/admin/login` chuyển về `/login`, giữ redirect mã hóa | Pos | GET `/admin/login?redirect=...` | `/admin/orders?status=NEW` | 302 → `/login?redirect=%2Fadmin%2F...` | LoginServletTest | Pass |
| **TC-AUTH-11/12** | Login | Đã đăng nhập mở `/login` | Pos | GET `/login` | CUSTOMER / ADMIN / chưa đăng nhập | `/home` / `/admin` / thấy form | LoginServletTest | Pass |
| **TC-AUTH-13/14** | Logout | Đăng xuất (có/không có session) | Pos | GET `/logout` | — | Hủy session, về `/home`; không lỗi khi chưa có session | RegisterServletTest | Pass (web thật) |
| **TC-AUTH-15** | Phân quyền | Vào `/admin` khi chưa đăng nhập | Neg | GET `/admin` | — | 302 → `/login?redirect=%2Fadmin` | Tay | Pass (web thật) |
| **TC-AUTH-RD-01..04** | Redirect an toàn | `safeRedirect` biên: null, rỗng, `//`, `/\`, URL tuyệt đối, CR/LF, `/login`... | Neg (biên) | Gọi hàm | 16 bộ dữ liệu | Chỉ nhận đường dẫn nội bộ hợp lệ, đã trim | LoginSupportTest | Pass |

## 2. Đăng ký (`/register`)
| Test Case ID | Chức năng | Mô tả ca kiểm thử | Loại | Các bước thực hiện | Dữ liệu kiểm thử | Kết quả mong đợi | Tự động | Trạng thái |
| :---: | :---: | :--- | :---: | :--- | :--- | :--- | :---: | :---: |
| **TC-REG-01** | Register | Biên họ tên, độ dài và khớp mật khẩu | Neg | Gửi form | họ tên `""`/`"   "`; mật khẩu 7 ký tự ✘, 8 ký tự ✔, `confirm` khác ✘ | Lỗi đúng ô (`fullName`, `password`, `confirm`) | RegisterServletTest | Pass (web thật) |
| **TC-REG-02** | Register | Biên trên mật khẩu | Neg (biên) | Gửi mật khẩu 64 và 65 ký tự | 64 ✔, 65 ✘ | 65 ký tự báo "Mật khẩu tối đa 64 ký tự." | RegisterServletTest | Pass |
| **TC-REG-03** | Register | Biên trên họ tên | Neg (biên) | Gửi họ tên 100 và 101 ký tự | 100 ✔, 101 ✘ | 101 ký tự báo "Họ tên tối đa 100 ký tự." | RegisterServletTest | Pass |
| **TC-REG-04** | Register | Gom lỗi nhiều ô cùng lúc | Neg | `register("", "", "123", "456")` | — | `ValidationException` có `email`, `fullName`, `password` (`assertThrows`) | RegisterServletTest | Pass |
| **TC-REG-05** | Register | Đăng ký thành công | Pos | Gửi form hợp lệ | `moi@saltair.vn`, `Pass@1234` | Đăng nhập luôn, flash chào mừng, về `/home` | RegisterServletTest | Pass (mock) |
| **TC-REG-06** | Register | Email đã có tài khoản | Neg | Gửi email trùng | `khach@saltair.vn` | "Email này đã có tài khoản.", không đăng nhập | RegisterServletTest | Pass (mock) |
| **TC-REG-07** | Register | Redirect sau đăng ký | Pos/Neg | Gửi kèm `redirect` | `/cart` ✔; `/admin/orders`, `//evil.com` ✘ | Hợp lệ về đúng trang; còn lại `/home` | RegisterServletTest | Pass |
| **TC-REG-08** | Register | Lỗi DB không bị nuốt | Neg | Service ném `DataAccessException` | — | Ngoại lệ ném tiếp (`assertThrows`) | RegisterServletTest | Pass |

## 3. Giỏ hàng (`/cart`)
| Test Case ID | Chức năng | Mô tả ca kiểm thử | Loại | Các bước thực hiện | Dữ liệu kiểm thử | Kết quả mong đợi | Tự động | Trạng thái |
| :---: | :---: | :--- | :---: | :--- | :--- | :--- | :---: | :---: |
| **TC-CART-01** | Cart | `action` thiếu hoặc lạ | Neg | POST `/cart` | thiếu, `""`, `delete`, `ADD`, `clear` | HTTP 400, giỏ không đổi | CartServletTest | Pass (web thật) |
| **TC-CART-02** | Cart | `variantId` sai | Neg | POST `add` | thiếu, `""`, `0`, `-1`, `abc`, `1.5` | "Vui lòng chọn màu và size.", không thêm | CartServletTest | Pass (web thật) |
| **TC-CART-03/04** | Cart | Quay lại trang sản phẩm theo `Referer` | Pos/Neg | POST `add` lỗi kèm `Referer` | cùng host ✔; `evil.com` ✘ | Cùng ứng dụng: về trang sản phẩm; khác host: `/cart` | CartServletTest | Pass |
| **TC-CART-05** | Cart | Biên `quantity` | Pos/Neg (biên) | POST `add` | thiếu, `""`, `abc` → 1; `3`, `" 4 "`, `10` giữ nguyên | Service nhận đúng số lượng | CartServletTest | Pass |
| **TC-CART-06** | Cart | Khách đăng nhập thêm vào giỏ DB | Pos | POST `add` khi đã đăng nhập | user 9, variant 5, qty 2 | `addItem(9,5,2)`, cập nhật số món trên menu | CartServletTest | Pass |
| **TC-CART-07** | Cart | Vượt tồn kho | Neg | Service ném `ValidationException` | qty 10, kho 2 | Flash lỗi, redirect `/cart` | CartServletTest | Pass |
| **TC-CART-08** | Cart | Lỗi DB không bị nuốt | Neg | `DataAccessException` | — | Ngoại lệ ném tiếp, không flash | CartServletTest | Pass |
| **TC-CART-09/10** | Cart | Cập nhật số lượng | Pos/Neg | POST `update` | `0` (xóa), `1`, `7`, `abc`→1, thiếu→1 | Service nhận đúng số | CartServletTest | Pass |
| **TC-CART-11/12** | Cart | Xóa dòng (khách / đã đăng nhập) | Pos | POST `remove` | itemId 5 / 4 | Dòng bị xóa, flash "Đã xóa sản phẩm khỏi giỏ hàng." | CartServletTest | Pass |
| **TC-CART-13** | Cart | Biên ghi chú 500 ký tự | Pos/Neg (biên) | POST `note` | 0, 1, 499, 500, 501, 2000 ký tự | Lưu tối đa 500 ký tự | CartServletTest | Pass |
| **TC-CART-14** | Cart | Ghi chú trim / thiếu | Pos | POST `note` | `"   Giao giờ hành chính  "`, thiếu | Trim; thiếu thì `""` | CartServletTest | Pass |
| **TC-CART-15/16** | Cart | Xem giỏ khách / đã đăng nhập | Pos | GET `/cart` | — | Forward `cart.jsp` kèm `cart`, `shippingFee`=30000, số món trong session | CartServletTest | Pass (web thật: 200) |

## 4. Danh sách và chi tiết sản phẩm
| Test Case ID | Chức năng | Mô tả ca kiểm thử | Loại | Các bước thực hiện | Dữ liệu kiểm thử | Kết quả mong đợi | Tự động | Trạng thái |
| :---: | :---: | :--- | :---: | :--- | :--- | :--- | :---: | :---: |
| **TC-PROD-01** | Product list | Không tham số | Pos | GET `/products` | — | `sort=newest`, trang 1, forward `product-list.jsp` | ProductListServletTest | Pass (web thật: 200, 12 thẻ) |
| **TC-PROD-02/03** | Product list | Biên số trang | Neg (biên) | GET `?page=` | thiếu, `abc`, `0`, `-5`, `1.5`, `99` | Sai → trang 1; vượt tổng → trang cuối | ProductListServletTest | Pass (web thật: `page=-1` → 200) |
| **TC-PROD-04** | Product list | Biên khoảng giá | Neg (biên) | GET `?minPrice=&maxPrice=` | `-1`, `abc`, `0`, min > max, số quá lớn | Sai bị bỏ; min > max đổi chỗ; không báo lỗi | ProductListServletTest | Pass (web thật: `minPrice=-5` → 200) |
| **TC-PROD-05** | Product list | Sắp xếp | Pos/Neg | GET `?sort=` | 5 giá trị hợp lệ; `PRICE_ASC`, `random`, `price_asc; DROP TABLE product` | Lạ → `newest` (không SQL injection) | ProductListServletTest | Pass (web thật: `sort=abc` → 200) |
| **TC-PROD-06/07** | Product list | Lọc size, màu | Pos/Neg | GET `?size=&color=` | `m`→`M`, `XXL`✘, `DEN`✘, `den`✔ | Size không phân biệt hoa thường; màu phân biệt; lạ bị bỏ | ProductListServletTest | Pass |
| **TC-PROD-08** | Product list | Từ khóa | Pos/Neg (biên) | GET `?q=` | 101 ký tự, `"  áo thun  "`, `"   "` | Cắt còn 100; trim; rỗng → không tìm | ProductListServletTest | Pass |
| **TC-PROD-09** | Product list | Danh mục + `baseUrl` + chip lọc | Pos/Neg | GET `?category=&q=&sort=` | `ao-thun-polo`, `khong-ton-tai` | Slug đúng lọc; lạ bị bỏ; URL mã hóa | ProductListServletTest | Pass |
| **TC-PDET-01** | Product detail | Slug sai | Neg | GET `/product` | thiếu, `""`, `khong-ton-tai` | HTTP 404 | ProductDetailServletTest | Pass (web thật) |
| **TC-PDET-02..05** | Product detail | Hiển thị, thống kê sao, quyền đánh giá | Pos | GET `/product?slug=ao-polo` | sao 5,4,4 → TB 4.3; sao DB 0/9 kẹp về 1/5 | Đủ thuộc tính; Admin không được đánh giá | ProductDetailServletTest | Pass |
| **TC-PDET-06/07** | Product detail | POST khi chưa đăng nhập / `action` lạ | Neg | POST `/product` | — | Chuyển `/login?redirect=...` / HTTP 400 | ProductDetailServletTest | Pass |
| **TC-PDET-08..11** | Product detail | Gửi đánh giá | Pos/Neg | POST `review` | `rating` `5`, `abc`, `4.5`...; lỗi ô; lỗi chung | `rating` sai → 0 để Service báo lỗi; thêm/sửa khác thông báo; lỗi ô hiện lại trang | ProductDetailServletTest | Pass |
| **TC-PDET-12** | Product detail | Lỗi DB khi lưu đánh giá | Neg | `DataAccessException` | — | Ngoại lệ ném tiếp (`assertThrows`) | ProductDetailServletTest | Pass |
| **TC-PDET-13/14** | Product detail | Xóa đánh giá; slug có ký tự đặc biệt | Pos | POST `deleteReview` | `áo thun&x=1` | `delete(5,11)`; URL quay lại được mã hóa | ProductDetailServletTest | Pass |

## 5. Ghi chú
* Chưa có test case voucher/đặt hàng trong bộ này: thuộc phạm vi Service (`VoucherService`, `OrderService`), bổ sung ở các tuần sau nếu cần.
* Các ca "(web thật)" được kiểm bằng `curl` vào web chạy trên Tomcat 11 + MySQL 8 ngày 17/11/2026.
