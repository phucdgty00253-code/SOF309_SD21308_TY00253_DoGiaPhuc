# BỘ KỊCH BẢN KIỂM THỬ: [Tên chức năng] (#[số issue])
> Hướng dẫn: [Bước 3 và Bước 5 - QUY_TRINH_PHAT_TRIEN_FEATURE.md](../../QUY_TRINH_PHAT_TRIEN_FEATURE.md)
> Viết ở Bước 3 (để trống cột Trạng thái), điền Pass/Fail ở Bước 5.

* **Môi trường:** `http://localhost:8080/saltair`, dữ liệu sạch từ `schema.sql` + `data.sql`, `auth.devCode=true`
* **Ngày chạy:** [dd/mm/yyyy] · **Người chạy:** [Tên]

| Test Case ID | Quy tắc | Mô tả ca kiểm thử | Loại (Pos/Neg) | Các bước thực hiện | Dữ liệu kiểm thử | Kết quả mong đợi | Trạng thái |
| :---: | :---: | :--- | :---: | :--- | :--- | :--- | :---: |
| **TC_[MẢNG]_01** | BR-1 | [Trường hợp đúng] | Positive | 1. [...]<br>2. [...] | `[...]` | [...] | |
| **TC_[MẢNG]_02** | BR-1 | [Giá trị biên dưới] | Negative | 1. [...] | `[...]` | Báo "[câu lỗi]" | |
| **TC_[MẢNG]_03** | BR-2 | [...] | Negative | 1. [...] | `[...]` | [...] | |

## Unit test JUnit đi kèm
| Class test | Phương thức | Quy tắc |
| :--- | :--- | :--- |
| `src/test/java/com/fashionweb/service/[mảng]/[...]ServiceTest.java` | `[tên hàm]` (`@ParameterizedTest`) | BR-1 |

## Kết quả
* **Tổng:** [x] ca · **Pass:** [x] · **Fail:** [x]
* **Lỗi phát hiện và đã sửa:** [Mô tả ngắn, commit sửa]
