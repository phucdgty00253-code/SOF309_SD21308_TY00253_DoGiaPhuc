# Danh mục sản phẩm Saltair

Nguồn dữ liệu máy đọc được: [product-catalog.csv](product-catalog.csv). File này là bản đọc cho người.

Thương hiệu giả **Saltair**, đồ basic phong cách biển, unisex. 24 sản phẩm, 4 danh mục. Size cho mọi sản phẩm: S, M, L, XL.

## Bảng màu (6 màu)

| Mã màu | Tên hiển thị | Màu trong prompt ảnh (tiếng Anh) | Mã màu gợi ý |
|---|---|---|---|
| `trang-nga` | Trắng ngà | off-white ivory | `#F3EFE6` |
| `den` | Đen | black | `#111111` |
| `xanh-navy` | Xanh navy | navy blue | `#14213D` |
| `xanh-bien-nhat` | Xanh biển nhạt | light sea blue, pale aqua-blue | `#A9CCE3` |
| `xam` | Xám | heather gray | `#9CA3AF` |
| `be-cat` | Be cát | sand beige | `#D8C3A5` |

## 1. Áo thun và Polo (`ao-thun-polo`)

| # | Sản phẩm | Giá (VNĐ) | Màu |
|---|---|---|---|
| 1 | Áo thun cổ tròn | 199.000 | Trắng ngà, Đen, Xanh navy |
| 2 | Áo thun oversize | 249.000 | Trắng ngà, Xám, Be cát |
| 3 | Áo thun kẻ sọc ngang | 279.000 | Xanh navy, Xanh biển nhạt |
| 4 | Áo polo | 329.000 | Trắng ngà, Xanh navy, Xanh biển nhạt |
| 5 | Áo ba lỗ | 199.000 | Trắng ngà, Đen, Be cát |
| 6 | Áo thun dài tay | 349.000 | Đen, Xám, Be cát |

## 2. Áo nỉ và Hoodie (`ao-ni-hoodie`)

| # | Sản phẩm | Giá (VNĐ) | Màu |
|---|---|---|---|
| 7 | Hoodie cơ bản | 499.000 | Xám, Đen, Be cát |
| 8 | Áo nỉ cổ tròn | 429.000 | Xám, Xanh navy, Trắng ngà |
| 9 | Hoodie khóa kéo | 549.000 | Đen, Xám, Xanh navy |
| 10 | Áo len mỏng | 399.000 | Be cát, Xanh biển nhạt, Xám |
| 11 | Áo nỉ cổ lọ | 459.000 | Đen, Trắng ngà, Be cát |
| 12 | Hoodie oversize | 599.000 | Trắng ngà, Xanh biển nhạt, Xám |

## 3. Áo khoác (`ao-khoac`)

| # | Sản phẩm | Giá (VNĐ) | Màu |
|---|---|---|---|
| 13 | Áo khoác gió | 599.000 | Xanh navy, Đen, Xanh biển nhạt |
| 14 | Áo khoác bomber | 699.000 | Đen, Xanh navy, Be cát |
| 15 | Áo khoác jeans | 799.000 | Xanh biển nhạt, Xanh navy |
| 16 | Áo khoác linen | 749.000 | Be cát, Trắng ngà |
| 17 | Áo khoác dù | 549.000 | Đen, Xám, Xanh navy |
| 18 | Áo khoác chống nắng | 499.000 | Xám, Xanh biển nhạt, Be cát |

## 4. Quần (`quan`)

| # | Sản phẩm | Giá (VNĐ) | Màu |
|---|---|---|---|
| 19 | Quần short | 299.000 | Be cát, Đen, Xanh navy |
| 20 | Quần dài ống suông | 449.000 | Đen, Xám, Be cát |
| 21 | Quần jogger | 429.000 | Xám, Đen, Xanh navy |
| 22 | Quần kaki | 499.000 | Be cát, Xanh navy, Xám |
| 23 | Quần short biển | 349.000 | Xanh biển nhạt, Xanh navy, Trắng ngà |
| 24 | Quần linen | 549.000 | Be cát, Trắng ngà, Xanh biển nhạt |

## Ghi chú khi dùng làm dữ liệu

- Mỗi sản phẩm có 2 đến 3 màu, 4 size. Biến thể (màu x size) sinh bằng script trong `database/data.sql`, không nhập tay.
- Màu đầu tiên trong danh sách là **màu chính**, dùng cho ảnh đại diện (`main_color` trong CSV).
- Mỗi sản phẩm có 1 ảnh chính tên `<slug>.jpg`, màu khác hiển thị bằng ô chọn màu.
- Giá chênh đều để thử lọc theo khoảng giá và voucher đơn tối thiểu (WELCOME10 từ 200.000đ, GIAM50K từ 500.000đ).
