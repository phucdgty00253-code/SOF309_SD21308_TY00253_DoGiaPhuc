# Kế hoạch kiểm tra giao diện

Mục tiêu: chắc chắn mọi trang JSP hiển thị đúng, không vỡ bố cục, không lỗi chữ, dùng được trên máy tính và điện thoại, trước khi giao cho thành viên gắn logic.

## 1. Phạm vi

32 trang (đo ở bản chạy thử có dữ liệu giả), mỗi trang kiểm ở **2 kích thước**: 1440px (máy tính) và 390px (điện thoại).

| Nhóm | Trang |
|---|---|
| shop | `/home`, `/products` (và có lọc), `/product?slug=`, `/blog`, `/blog/detail?slug=`, `/about` |
| cart | `/cart` (có hàng, giỏ trống), `/checkout` (chưa và đã áp mã) |
| order | `/order-success`, `/orders`, `/order?code=` |
| auth | `/login`, `/register`, `/forgot-password`, `/reset-password` |
| account | `/profile`, `/addresses`, `/wishlist` |
| admin/catalog | `/admin/products`, `/admin/products/form`, `/admin/categories`, `/admin/attributes`, `/admin/trash` |
| admin/sales | `/admin/orders`, `/admin/order?code=`, `/admin/vouchers`, `/admin/reviews` |
| admin/system | `/admin` (dashboard), `/admin/users`, `/admin/banners`, `/admin/blogs`, `/admin/blogs/form` |
| error | 404 |

## 2. Kiểm tra tự động (chạy bằng script trong trình duyệt, mỗi trang x 2 kích thước)

| Mã | Điều kiện đạt |
|---|---|
| A1 | Không có chữ lỗi bảng mã (mojibake) |
| A2 | Không tràn ngang: `scrollWidth` không vượt chiều rộng cửa sổ |
| A3 | Không có phần tử nào nhô ra ngoài mép phải màn hình |
| A4 | Đúng một thẻ `h1` mỗi trang |
| A5 | Không có ảnh bị lỗi tải (ảnh rỗng phải là khung xám `.ph`, không phải ảnh vỡ) |
| A6 | Ảnh có thuộc tính `alt` |
| A7 | Nút chỉ có icon phải có `aria-label` hoặc chữ |
| A8 | Ô nhập có nhãn (`label`, `aria-label`) |
| A9 | Font hiển thị là Manrope (đã nạp) |
| A10 | Không có liên kết `href="#"` rỗng |
| A11 | Không có lỗi JavaScript trong console |
| A12 | Không có box-shadow (quy tắc thiết kế) |

## 3. Kiểm tra bằng mắt (ảnh chụp)

Các trang phức tạp xem ảnh chụp ở cả 2 kích thước: trang chủ, danh sách sản phẩm (kèm ngăn kéo bộ lọc), chi tiết sản phẩm, giỏ hàng, thanh toán, đơn hàng, dashboard, form sản phẩm, danh sách đơn hàng admin, voucher.

Checklist bằng mắt:
- [ ] Menu, thanh thông báo, footer đúng và không đè nhau
- [ ] Lưới sản phẩm đúng số cột (4 / 3 / 2), khoảng cách đều
- [ ] Chữ không bị cắt, không chồng lên nhau, không quá nhỏ trên điện thoại
- [ ] Nút dạng viên thuốc, thẻ phẳng, không đổ bóng
- [ ] Ảnh rỗng hiện khung xám có icon, không để trống trắng
- [ ] Trạng thái rỗng (giỏ trống, không có sản phẩm, chưa có địa chỉ) hiển thị đẹp
- [ ] Bảng admin cuộn ngang được trên điện thoại, không làm vỡ cả trang

## 4. Kiểm tra tương tác

- [ ] Thanh thông báo chuyển câu bằng mũi tên
- [ ] Menu "Cửa hàng" thả xuống (máy tính), menu ngăn kéo (điện thoại)
- [ ] Ô tìm kiếm trượt xuống, nhấn Esc đóng được
- [ ] Băng chuyền: mũi tên, thanh tiến trình, kéo chuột
- [ ] Hộp thoại (thêm sửa địa chỉ, danh mục, voucher...) mở, đóng, đổ dữ liệu cũ vào form
- [ ] Chọn màu và size ở trang chi tiết, size hết hàng bị vô hiệu
- [ ] Nút thêm vào giỏ, yêu thích, gửi form chưa có logic: hiện thông báo "chế độ giao diện"
- [ ] Bộ lọc, sắp xếp, phân trang giữ đúng tham số trên URL

## 5. Ghi kết quả

Mỗi vòng ghi: số trang đạt, danh sách lỗi (trang, mã lỗi, mô tả), cách sửa. Kết quả vòng hiện tại nằm ở cuối file này khi chạy xong.
