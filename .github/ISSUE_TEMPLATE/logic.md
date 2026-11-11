---
name: "[Logic] Chức năng phía server"
about: Servlet, Service, Repository, database, cấu hình
title: "[Logic] "
labels: logic
assignees: ''
---

**Giai đoạn**: <!-- ví dụ: Sprint 1 -->
**Đi cùng**: <!-- issue giao diện liên quan, ví dụ #75. Không có thì xóa dòng này -->
**Tài liệu**: `docs/features/<số>-<ten-ngan>/` (tạo ở Bước 2 của [quy trình](../../docs/QUY_TRINH_PHAT_TRIEN_FEATURE.md))

## Mô tả
<!-- Người dùng nào, làm được gì, vì sao cần. Một đến ba câu. -->

## Cần làm
- **Database**: <!-- bảng/cột thêm, sửa, xóa; file migration. Không đổi thì ghi "Không" -->
- **Repository**:
- **Service**: <!-- quy tắc nghiệp vụ có con số cụ thể -->
- **Servlet / Endpoint**: <!-- phương thức, đường dẫn, tham số -->

## Nghiệm thu
<!-- Mỗi dòng đo được: làm gì, thấy gì. -->
- [ ]
- [ ]
- [ ] `docs/features/<số>-<ten-ngan>/TEST_CASES.md` đều Pass, `mvn test` xanh

---
**Quy tắc chung**: nhánh `feature/<số-issue>-<tên-ngắn>` tách từ `develop`, PR vào `develop` ghi `Closes #<số-issue>`. Luồng `Servlet -> Service -> Repository -> MySQL`, SQL dùng `PreparedStatement`.
