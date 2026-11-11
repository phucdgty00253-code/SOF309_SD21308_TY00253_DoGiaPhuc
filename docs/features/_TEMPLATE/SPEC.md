# ĐẶC TẢ FEATURE: [Tên chức năng] (#[số issue])
> *Dùng làm ngữ cảnh gửi cho AI để sinh test case, và để người review hiểu feature trước khi đọc code.*
> Hướng dẫn: [Bước 2 - QUY_TRINH_PHAT_TRIEN_FEATURE.md](../../QUY_TRINH_PHAT_TRIEN_FEATURE.md)

---

## 1. THÔNG TIN CHUNG
* **Issue:** #[số] ([Logic] / [Giao diện]) · **Đi cùng:** #[số issue liên quan, nếu có]
* **Người làm:** [Tên - GitHub]
* **Nhánh:** `feature/[số]-[tên-ngắn]`
* **Mảng:** auth / account / shop / cart / order / admin
* **Trạng thái:** Đang làm / Chờ review / Đã merge (PR #[số])

## 2. MỤC TIÊU
[Một đến ba câu: người dùng nào, làm được gì, vì sao cần.]

## 3. QUY TẮC NGHIỆP VỤ (BUSINESS RULES)
> Mỗi quy tắc một dòng, có con số hoặc điều kiện kiểm tra được. Đánh mã BR-x để test case trỏ tới.

* **BR-1:** [Ví dụ: Tên sản phẩm bắt buộc, 2-150 ký tự, không trùng.]
* **BR-2:** [...]

## 4. LUỒNG XỬ LÝ
1. [Người dùng làm gì]
2. [Hệ thống kiểm tra gì, lưu gì]
3. [Kết quả hiển thị / chuyển trang]

## 5. ENDPOINT

| Phương thức | Đường dẫn | Tham số | Thành công | Lỗi |
| :---: | :--- | :--- | :--- | :--- |
| GET | `/[...]` | | Hiện `views/[...].jsp` | |
| POST | `/[...]` | `[...]` | Redirect `/[...]` + flash | Hiện lại form kèm `errors` |

## 6. THAY ĐỔI DATABASE
* **Bảng / cột:** [Thêm, sửa, xóa gì. Không đổi thì ghi "Không".]
* **Migration:** `database/migration_[số]_[tên].sql`
* **Dữ liệu mẫu (`data.sql`):** [Có đổi không]

## 7. CÁC FILE SẼ SỬA / THÊM

| Tầng | File |
| :--- | :--- |
| Controller | `controller/[mảng]/[...]Servlet.java` |
| Service | `service/[mảng]/[...]Service.java` |
| Repository | `repository/[mảng]/[...]Repository.java` |
| View | `WEB-INF/views/[mảng]/[...].jsp` |
| CSS / JS | `assets/css/[...]`, `assets/js/[...]` |

## 8. GIAO DIỆN & TRẠNG THÁI
* **Trạng thái trống:** [...]
* **Đang xử lý:** [...]
* **Lỗi:** [Câu thông báo tiếng Việt chính xác]
* **Thành công:** [...]
* **Điện thoại (dưới 750px):** [...]

## 9. NGOÀI PHẠM VI
* [Những gì cố ý không làm trong feature này.]

## 10. QUYẾT ĐỊNH ĐÃ CHỐT
| Ngày | Câu hỏi | Quyết định | Người chốt |
| :--- | :--- | :--- | :--- |
| [dd/mm] | [...] | [...] | Trưởng nhóm |
