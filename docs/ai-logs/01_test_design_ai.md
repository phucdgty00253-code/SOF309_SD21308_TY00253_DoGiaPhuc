# Nhật ký AI: Thiết kế đặc tả, Test Plan và Test Case

## 1. Ngữ cảnh & Mục tiêu
- Dự án PolyCoffee (JAV102), tuần 1 và 2 của SOF309.
- Công cụ: Claude Code (mô hình Claude Sonnet 5.5), ngày 26/11/2026.
- Ngữ cảnh AI đã đọc (RAG thủ công từ file): `README.md`, `docs/HUONG_DAN_CHI_TIET_6_TUAN.md`, đề Lab 1-8, đề Assignment và slide môn JAV102.

## 2. Câu lệnh đã gửi cho AI (Prompt, nguyên văn)
> "sau khi commit và push sạch xong thì truy cập vào các bài lab của môn jav102 và làm đúng yêu cầu từng làm và hoàn thiện các file cần có. Sửa file pom.xml đúng với dự án polycoffee và ko còn dùng dự án saltiar fasion nữa. Viêt slaij specìications và viết thêm test plan và test case cho dự án rồi từ test case đó làm theo lộ trình trong readme.md tuần 1 và 2 đều done trong folder test của java thì dùng junit và dựa vào các test case id rồi tạo một folder cha ví dụ như auth và trong folder đó chưua các file controller để test và từng file test case ID riêng lẻ."

Prompt gửi thêm khi AI đang làm:
> "phải viết test plan và test case trơcs mới có id và tạo tiuwnfg file riêng lẻ chứ? tôi muốn tổ chức package rõ ràng để có thể hiểu được luồng đi và test nó gọi ở đâu để test và nó nằm ở test case id số máy?"

## 3. Kết quả do AI sinh ra
- `docs/SPECIFICATIONS.md`: ràng buộc dữ liệu và quy tắc có mã BR.
- `docs/TEST_PLAN.md`, `docs/TEST_CASES.md`.
- Kỹ thuật dùng: phân vùng tương đương, giá trị biên, bảng quyết định, chuyển trạng thái, đoán lỗi.
- Bản đầy đủ nằm trong các file trên.

## 4. Chỉnh sửa và lý do
### 4.1. Điều AI làm chưa đúng, đã sửa trong phiên làm việc
- **Sai thứ tự:** AI sinh 81 file test từ một danh sách tạm trước khi viết `TEST_PLAN.md` và `TEST_CASES.md`. Sau góp ý ở prompt thứ hai, AI viết đặc tả (mã BR), test plan, rồi sinh lại test case và file test có liên kết BR → TC → file. Danh sách ca kiểm thử không được thiết kế lại từ đầu; phần bổ sung là mã BR, truy vết, luồng gọi.
- **Khó theo dõi luồng:** thêm phần mô tả luồng gọi ở đầu mỗi file, `@Tag`, và `controllers/README.md`.

### 4.2. Phần sinh viên tự rà soát (SINH VIÊN TỰ ĐIỀN)
> Đối chiếu từng TC với mã BR và đề bài, ghi điểm AI làm sai hoặc thiếu mà bạn phát hiện. Không điền nội dung bạn chưa tự kiểm tra.

- [ ] ...
