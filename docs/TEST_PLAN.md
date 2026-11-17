# KẾ HOẠCH KIỂM THỬ (TEST PLAN)
> *File RIÊNG của dự án. Bối cảnh dự án: [SPECIFICATIONS.md](SPECIFICATIONS.md). Quy trình: [HUONG_DAN_QUY_TRINH_PHAT_TRIEN.md](HUONG_DAN_QUY_TRINH_PHAT_TRIEN.md) (Bước 5).*

* **Dự án:** Saltair - website bán quần áo basic (JAV102), kiểm thử theo môn SOF309
* **Người phụ trách:** Trưởng nhóm (`nhan0x`); phần SOF309 của Phúc (`phucdgty00253-code`): sản phẩm, giỏ hàng, đăng nhập/đăng ký
* **Bộ test case chi tiết:** [TEST_CASES.md](TEST_CASES.md) · **Nhật ký AI:** [ai-logs/01_test_design_ai.md](ai-logs/01_test_design_ai.md), [ai-logs/02_junit_script_ai.md](ai-logs/02_junit_script_ai.md)

---

## 1. MỤC TIÊU KIỂM THỬ
* Kiểm thử nghiệp vụ ở tầng Service bằng **JUnit 5** (giá trị biên, lỗi nghiệp vụ).
* Kiểm thử từng yêu cầu theo `docs/requirements/REQ_<số>_<TEN>/03_TEST_CASES.md` trước khi mở PR.
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
| Unit test | JUnit 5 (`mvn test`), `@Tag("unit")` | `src/test/java/com/fashionweb/<tầng>/<mảng>/` | Mỗi lần commit có sửa Service |
| Unit test Controller (Y3) | JUnit 5 + Mockito (`mockConstruction`), `@Tag("unit")`; request/response/session giả ở `controller/FakeWeb` | `src/test/java/com/fashionweb/controller/{auth,cart,shop}/` | Mỗi lần commit có sửa Servlet |
| Giao diện | Selenium + Page Object (`mvn test -Pui`), `@Tag("ui")` | `src/test/java/com/fashionweb/ui/{pages,tests}/` | Khi web đang chạy local, trước khi mở PR có sửa JSP |
| Test case theo yêu cầu | Làm tay theo bảng | `docs/requirements/REQ_<số>_<TEN>/03_TEST_CASES.md` | Bước 5, trước khi mở PR |
| Toàn luồng | Làm tay (hoặc script curl) | Mục 5 bên dưới | Trước khi merge PR lớn, trước khi lên `main` |

### 3.1. Cách đặt test
* Test nằm cùng package với class được test: `service/cart/VoucherService` → `src/test/java/com/fashionweb/service/cart/VoucherServiceTest.java`.
* Mỗi class test gắn đúng một tag: `unit` (không cần DB, không cần server), `integration` (cần MySQL), `ui` (cần web đang chạy).
* `mvn test` mặc định chỉ chạy `unit`; `mvn test -Pui` chỉ chạy `ui` (thêm `-Dheadless=true` để không mở cửa sổ Chrome).
* `@DisplayName` ghi mã test case trong `03_TEST_CASES.md` (ví dụ `TC-CART-03: ...`) để tra ngược được.

### 3.2. Phân công kiểm thử SOF309
Mỗi người sở hữu test trong package của mảng mình; `ui/pages/` dùng chung.

| Thành viên | Mảng kiểm thử | Package test |
| :--- | :--- | :--- |
| Nhân (`nhan0x`) | Đăng nhập, phân quyền, tài khoản | `service/auth`, `service/account`, `ui/tests/Login*` |
| Minh (`HoangMinh219`) | Đơn hàng, đánh giá | `service/order`, `ui/tests/Order*` |
| Phúc (`phucdgty00253-code`) | Sản phẩm, giỏ hàng, voucher | `service/shop`, `service/cart`, `util`, `ui/tests/Cart*` |

### 3.3. Kỹ thuật thiết kế test và kỹ thuật JUnit (Controller)
* **Phân vùng tương đương + giá trị biên** (đặc tả ở [SPECIFICATIONS.md](SPECIFICATIONS.md) mục 8): mật khẩu 7/8/64/65 ký tự, họ tên 100/101, email 150/151, ghi chú 500/501, `quantity` 0/1/10, `page` 0/1/vượt trang, khoảng giá min > max...
* `@ParameterizedTest` + `@CsvSource`: quét nhiều bộ dữ liệu biên trong một hàm test.
* `assertThrows`: `ValidationException` từ Service, và `DataAccessException` phải được Servlet ném tiếp (không nuốt).
* `@Timeout(2s)` ở mọi class test Controller để phát hiện vòng lặp treo.
* Không cần Tomcat hay MySQL: Service/DAO được thay bằng mock, nên chạy trong vài giây.
* Chạy riêng nhóm Controller: `mvn test -Dtest='*ServletTest,LoginSupportTest'`.

### 3.4. Kết quả chạy gần nhất (17/11/2026)
| Nhóm | Số ca (đã tính từng dòng `@CsvSource`) | Kết quả |
| :--- | :---: | :---: |
| `controller/auth` (Login, Register, Logout, LoginSupport) | 68 | Pass |
| `controller/cart` (CartServlet) | 40 | Pass |
| `controller/shop` (ProductList, ProductDetail) | 74 | Pass |
| Test có từ trước (util, service/order) | 36 | Pass |
| **Tổng `mvn test`** | **218** | **BUILD SUCCESS, 0 Failure, 0 Error** |

## 4. MÔI TRƯỜNG & DỮ LIỆU
* Cách chạy, cấu hình và dữ liệu mẫu: [SPECIFICATIONS.md](SPECIFICATIONS.md) mục 2 và mục 6.
* Dữ liệu mẫu tạo bởi Flyway (`src/main/resources/db/migration/V1..V4`) khi web khởi động; `db.properties` trỏ MySQL của máy.
* Chạy web local: `mvn clean package` rồi deploy `target/saltair.war` lên Tomcat 11, mở `http://localhost:8080/saltair/home`. **Dùng `clean`**: class cũ còn sót trong `target/` từng làm Tomcat không khởi động (hai Servlet trùng `/admin/trash`).

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
* Mọi `03_TEST_CASES.md` của các yêu cầu trong đợt merge đều Pass.
* 7 luồng ở mục 5 đều chạy được, log Tomcat không có `SEVERE`/`Exception`.
