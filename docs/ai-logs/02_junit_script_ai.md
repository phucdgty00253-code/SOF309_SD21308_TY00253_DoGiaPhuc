# Nhật ký AI: Viết Unit Test JUnit 5 cho Controller

## 1. Ngữ cảnh & Mục tiêu
- Tuần 2: unit test controller, có `@ParameterizedTest` + `@CsvSource`, `assertThrows()`, `@Timeout`; `mvn test` pass 100%.
- Công cụ: Claude Code (Claude Sonnet 5.5), ngày 26/11/2026.

## 2. Prompt
Xem hai prompt nguyên văn trong [01_test_design_ai.md](01_test_design_ai.md). Yêu cầu về test: mỗi nhóm một package, trong đó có file controller và mỗi Test Case ID một file.

## 3. Kết quả do AI sinh
- `support/WebMock.java`, `support/Fixtures.java`.
- 9 package, mỗi package một lớp nền `<Nhóm>ControllerTest` và các file `TC_...ControllerTest` (81 ID).
- Để test được, mỗi servlet có thêm constructor nhận service; unit test dùng controller và service thật, giả DAO bằng Mockito.

Ví dụ: [TC_DRINK_004](../../src/test/java/com/poly/sof309/controllers/drink/) kiểm tra biên giá bằng `@CsvSource`.

## 4. Chỉnh sửa và lý do
### 4.1. Đã sửa trong phiên làm việc
| Vấn đề | Sửa |
|---|---|
| Một số ca (ví dụ TC_AUTH_016, TC_POS_007, TC_BILL_005) gọi thẳng service hoặc chỉ gọi service ở một phần, chưa phải test controller | Bổ sung bước đi qua controller. Riêng TC_BILL_001 vẫn gọi thẳng vì kiểm tra hàm thuần |
| TC_DRINK_010 có một dòng thừa chỉ để cài mock, gây khó hiểu | Thay bằng câu `when(...)` rõ ràng |
| Test do cùng AI viết với code nên có thể luôn xanh | Cố ý đưa 4 lỗi vào code; cả 4 đều làm đỏ đúng test (bảng cuối `TEST_CASES.md`) |

### 4.2. Phần sinh viên tự sửa (SINH VIÊN TỰ ĐIỀN)
> Ghi đoạn code bạn tự đổi, trước và sau, kèm lý do. Nên chuẩn bị giải thích được WebMock, `@CsvSource`, `assertThrows` khi vấn đáp.

- [ ] ...
