# Nhật ký AI: Viết Unit Test cho Controller (Tuần 2 - Y3)

## 1. Ngữ cảnh & Mục tiêu
- Viết Unit Test cho Controller (Servlet) bằng JUnit 5, áp dụng `@ParameterizedTest` + `@CsvSource` kiểm tra biên form, `assertThrows()` cho lỗi nghiệp vụ, `@Timeout`.
- Phạm vi: `LoginServlet`, `RegisterServlet`, `LogoutServlet`, `LoginSupport`, `CartServlet`, `ProductListServlet`, `ProductDetailServlet`.
- Công cụ AI: Claude Code. Yêu cầu: `mvn test` đạt 100% Pass, không cần Tomcat/MySQL.

## 2. Câu lệnh đã gửi cho AI (Prompt)
> "Thực hiện đúng yêu cầu tuần 1 và tuần 2 trong file README.md"
>
> (Tuần 2 theo README: Unit Test Controller, `@ParameterizedTest` + `@CsvSource` kiểm tra biên Form, `assertThrows()` kiểm thử ngoại lệ nghiệp vụ, `mvn test` đạt 100% Pass.)

## 3. Kết quả do AI sinh ra (Raw Output)
- Thiết kế: Servlet tạo Service bằng `new` trong field nên dùng Mockito `mockConstruction(...)` để thay Service bằng mock lúc tạo Servlet; request/response/session giả ở `src/test/java/com/fashionweb/controller/FakeWeb.java`.
- Các file test (`src/test/java/com/fashionweb/controller/`):
  - `auth/LoginSupportTest`, `auth/LoginServletTest`, `auth/RegisterServletTest` (kèm LogoutServlet), `auth/AuthTestSupport`
  - `cart/CartServletTest`
  - `shop/ProductListServletTest`, `shop/ProductDetailServletTest`
- Kỹ thuật: 182 ca (tính từng dòng `@CsvSource`). Ví dụ biên: mật khẩu 7/8/64/65 ký tự, họ tên 100/101, email 151, ghi chú 500/501, `quantity`, `page`, khoảng giá, `sort`, `redirect` độc hại. `assertThrows` cho `ValidationException` (`AuthService.register/login`) và cho `DataAccessException` phải được Servlet ném tiếp. `@Timeout(2s)` ở mọi class.
- Kết quả: `mvn test` → 218 test (182 mới + 36 có sẵn), 0 Failure, 0 Error, BUILD SUCCESS.

## 4. Chỉnh sửa của Sinh viên & Lý do sửa
Lỗi thật gặp khi chạy và cách sửa:
- `LoginServletTest.emailLongerThan150`: AI dùng email 150 ký tự, nhưng 150 là biên **hợp lệ** (`length() > 150` mới lỗi), nên test chạm DB và sai. Sửa thành 151 ký tự, đổi tên ca cho đúng biên.
- `RegisterServletTest.dataAccessExceptionPropagates`: mock dùng `anyString()` không khớp tham số `null` (form không gửi `fullName`...), mock trả `null` rồi gây `NullPointerException`. Sửa dùng `any()`.
- `LoginSupport` có `CartService` tĩnh tạo lúc nạp lớp: nếu nạp lúc chưa mock thì test đăng nhập chạm DB. Thêm `AuthTestSupport.prepareLoginSupport()` nạp lớp dưới `mockConstruction` một lần.
- Giữ ca kiểm tra Controller + Service thật cho các lỗi validation xảy ra trước khi chạm DB (email sai định dạng, mật khẩu rỗng) để test sát hành vi thật.
- Ghi chú lệch so với cẩm nang: cẩm nang đặt test ở `src/test/java/com/poly/sof309/controllers/` và chạy `-Dtest=*ControllerTest`; repo này dùng package `com.fashionweb` và Servlet kết thúc bằng `Servlet`, nên test đặt ở `com/fashionweb/controller/...` và chạy bằng `mvn test -Dtest='*ServletTest,LoginSupportTest'` (hoặc `mvn test` để chạy tất cả).
- *(Sinh viên bổ sung nhận xét cá nhân sau khi tự đọc và giải thích từng test tại đây, cần để vấn đáp.)*
