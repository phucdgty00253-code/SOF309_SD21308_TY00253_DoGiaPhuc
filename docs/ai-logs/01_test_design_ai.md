# Nhật ký AI: Thiết kế Test Plan & Test Case (Tuần 1 - Y2)

## 1. Ngữ cảnh & Mục tiêu
- Dự án: Saltair (Java Servlet/JSP, MySQL, Tomcat 11). Cần `SPECIFICATIONS.md` đủ chi tiết, `TEST_PLAN.md`, `TEST_CASES.md` cho các mảng được giao: đăng nhập/đăng ký, giỏ hàng, danh sách và chi tiết sản phẩm.
- Công cụ AI: Claude Code (đọc trực tiếp mã nguồn trong repo làm ngữ cảnh RAG: `controller/`, `service/`, `project.md`, `SPECIFICATIONS.md`).
- Kỹ thuật yêu cầu AI áp dụng: Phân vùng tương đương và Phân tích giá trị biên.

## 2. Câu lệnh đã gửi cho AI (Prompt)
> "Thực hiện đúng yêu cầu tuần 1 và tuần 2 trong file README.md"
>
> (Tuần 1 theo README: web chạy local, điền đặc tả `SPECIFICATIONS.md`, dùng AI RAG sinh Test Plan và Test Case. Prompt mẫu của cẩm nang: *"Bạn là QA Lead. Dưới đây là đặc tả nghiệp vụ dự án Web của tôi... Hãy áp dụng kỹ thuật Phân vùng tương đương và Phân tích giá trị biên để thiết kế Test Plan và Bộ Test Case chi tiết cho Controller và UI."*)

## 3. Kết quả do AI sinh ra (Raw Output)
- AI đọc `AuthService`, `CartService`, `LoginServlet`, `RegisterServlet`, `CartServlet`, `ProductListServlet`, `ProductDetailServlet`, `WebUtil`, `AppConstant` rồi rút ra bảng URL, trường dữ liệu và ràng buộc → `docs/SPECIFICATIONS.md` mục 8.
- Từ mục 8, AI liệt kê lớp tương đương và biên, ví dụ:
  - Mật khẩu: `<8` (lỗi), `8` (biên hợp lệ), `64` (biên hợp lệ), `65` (lỗi), không khớp `confirm`.
  - Email: rỗng, thiếu `@`, thiếu miền, có khoảng trắng, 150 và 151 ký tự.
  - `quantity`: rỗng/sai → 1, 0, 1, 10, 11, âm. Ghi chú: 499, 500, 501 ký tự.
  - `page`: thiếu, `abc`, `0`, âm, vượt tổng số trang. Khoảng giá: âm, `min > max`.
  - `redirect`: `//host`, `/\host`, URL tuyệt đối, CR/LF, `/login`, `/admin/...` của khách.
- Kết quả: `docs/TEST_PLAN.md` (mục 3.1 đến 3.4) và `docs/TEST_CASES.md` (4 bảng, mã `TC-AUTH`, `TC-REG`, `TC-CART`, `TC-PROD`, `TC-PDET`).

## 4. Chỉnh sửa của Sinh viên & Lý do sửa
Những điểm AI sai hoặc thiếu và đã được đối chiếu/sửa với web thật (Tomcat 11 + MySQL, 17/11/2026):
- `project.md` mô tả đăng nhập bằng **mã 6 số qua email**, nhưng mã hiện tại là **email + mật khẩu**. Đặc tả được viết theo mã nguồn thật, kèm lưu ý trong `SPECIFICATIONS.md` mục 8.
- `TEST_PLAN.md` ghi chạy `database/schema.sql`; thực tế DB tạo bằng Flyway (`db/migration/V1..V4`). Đã sửa lại mục "Môi trường".
- Lần chạy web đầu tiên Tomcat báo lỗi hai Servlet cùng map `/admin/trash` do class cũ còn trong `target/`. Giải pháp `mvn clean package`, đã ghi vào mục "Môi trường".
- Các kết quả "web thật" trong `TEST_CASES.md` được kiểm bằng `curl` (đăng nhập sai, đăng ký sai, `/admin` khi chưa đăng nhập, `sort`/`page`/`minPrice` sai, `action` lạ, slug 404, `/logout`).
- Còn thiếu (ghi nhận để làm tiếp): test case voucher/đặt hàng, và các ca cần mật khẩu của tài khoản mẫu (đăng nhập thành công) mới chỉ chạy bằng mock.
- *(Sinh viên bổ sung nhận xét cá nhân sau khi tự rà soát bộ test case tại đây.)*
