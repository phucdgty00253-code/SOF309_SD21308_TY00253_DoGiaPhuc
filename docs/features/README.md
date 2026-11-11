# TÀI LIỆU THEO FEATURE

Mỗi feature có một thư mục riêng `docs/features/<số-issue>-<ten-ngan>/` gồm 3 file:

| File | Bước | Nội dung |
| :--- | :---: | :--- |
| `SPEC.md` | 2 | Đặc tả: quy tắc nghiệp vụ (BR-x), endpoint, database, giao diện, quyết định đã chốt |
| `TEST_CASES.md` | 3, 5 | Bảng ca kiểm thử trỏ tới BR-x, cột Trạng thái điền khi tự kiểm thử |
| `AI_LOG.md` | 3, 5 | Nhật ký dùng AI: prompt, kết quả, chỗ đã sửa và lý do |

**Bắt đầu feature mới:**
```bash
cp -R docs/features/_TEMPLATE docs/features/<số-issue>-<ten-ngan>
```

Quy trình đầy đủ: [QUY_TRINH_PHAT_TRIEN_FEATURE.md](../QUY_TRINH_PHAT_TRIEN_FEATURE.md).
Ví dụ đã điền đầy đủ: [74-dang-nhap-bang-ma](74-dang-nhap-bang-ma/).

---

## BẢNG TỔNG HỢP FEATURE

> Trưởng nhóm thêm một dòng sau mỗi lần merge (Bước 7). Feature làm trước khi có quy trình này để trống cột Tài liệu.

| Issue | Feature | Loại | Mảng | PR | Tài liệu | Trạng thái |
| :---: | :--- | :---: | :---: | :---: | :---: | :---: |
| #72 | Endpoint tìm kiếm nhanh `/search/quick` | Logic | shop | #76 | | Đã merge |
| #73 | Khung tìm kiếm trượt từ phải, nền mờ | Giao diện | shop | #77 | | Đã merge |
| #74 | Đăng nhập bằng email + mã 6 số, bỏ mật khẩu | Logic | auth | #78 | [Có](74-dang-nhap-bang-ma/) | Đã merge |
| #75 | Khung tài khoản dạng popover, trang nhập mã 6 ô | Giao diện | auth | #79 | | Đã merge |
| #80 | JUnit 5 và unit test cho SlugUtil, VoucherService | Test | chung | #80 | | Đã merge |
| #81-#91 | Làm lại giao diện khách theo mẫu, giỏ hàng cho khách | Giao diện, Logic | shop, cart, account | develop | | Đã merge |
