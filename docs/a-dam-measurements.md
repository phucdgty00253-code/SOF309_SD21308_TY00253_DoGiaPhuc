# Số liệu đo từ a-dam.com (để dựng giao diện Saltair)

Đo bằng trình duyệt ở viewport 1440 x 900 (chiều rộng nội dung thực 1425 vì thanh cuộn), ngày 2026-11-05. Chỉ mượn **tỉ lệ, nhịp thiết kế, hiệu ứng**. Không dùng logo, ảnh, câu chữ của A-dam.

## 1. Màu

| Vai trò | Mã |
|---|---|
| Chữ chính, nút chính, nền khối tối | `#000e1f` |
| Chữ phụ (60% của chữ chính) | `rgba(0,14,31,.6)` |
| Chữ mờ (nút size chưa chọn) | `rgba(0,14,31,.5)` |
| Thanh thông báo | `#0000c5`, chữ trắng |
| Nền trang, nền ô ảnh | `#f4f4f4` |
| Viền mảnh | `#e6e7e9` (viền nút size chưa chọn: `rgba(230,231,233,.5)`) |
| Đường kẻ phụ | `#dcdddf` |
| Viền nhấn | `#1a2635` |
| Sao đánh giá (nền) | `#fff48d` (sao trong thẻ sản phẩm vẽ màu navy `#000e1f`, 13x13) |
| Nền trắng | `#ffffff` |

## 2. Chữ

- Font gốc GT Walsheim Pro (trả phí). **Font thay thế đã chọn: Manrope** (Google Fonts, có đủ dấu tiếng Việt). Đo độ rộng 11 chuỗi mẫu ở weight 400 và 700: sai lệch trung bình 2,3% so với font gốc (thấp nhất trong 24 font thử). Outfit, Figtree, DM Sans, Red Hat Display gần hơn về hình dáng nhưng **không có dấu tiếng Việt** trong Google Fonts nên loại.
- Nội dung: 15px, giãn dòng 21px, weight 400.
- Nhãn nhỏ, menu: 12px, weight 500.
- Chữ trong thẻ sản phẩm (hãng, tên, giá): 13px, giãn dòng 18,2px. Điểm "(5.0)": 12px weight 500.
- Tiêu đề phụ trong dải lợi ích: 16,26px weight 700.
- Tiêu đề trang chi tiết sản phẩm (h1): 20,2px weight 700, giãn dòng 22,2px.
- Tiêu đề khối, h1 hero, h1 trang danh mục: 37,6px weight 700, giãn dòng 41,4px.
- Tiêu đề lớn ô đăng ký: 57,6px weight 800, giãn dòng 60,5px, `letter-spacing: -2.3px`.
- Chữ nút: 16px weight 400.

## 3. Hình khối

- Nút, ô nhập, nhãn: `border-radius: 30px`.
- Nút pill chính: cao 53px, nền `#000e1f`, chữ trắng 16px. Nút trong hero: nền trắng, chữ navy, rộng khoảng 177px. Nút viền ("Alles bekijken"): cao 53px, rộng khoảng 176px, viền 1px `#000e1f`, nền trong suốt.
- Nút chọn size: 64 x 41px, padding `9px 18px`, bo 30px, viền 1px. Đang chọn: viền `#000e1f`, chữ `#000e1f`.
- Thẻ sản phẩm, thẻ danh mục, ô ảnh: không viền, không bo, không đổ bóng.
- Trang không dùng `box-shadow`.

## 4. Bố cục trang chủ (1440px)

Thứ tự khối và chiều cao: thanh thông báo 47, menu 95 (đè lên hero), hero 610, dải lợi ích 196, "Must-haves" (tiêu đề + băng chuyền), khối chữ nền navy 329, đường kẻ 15, hai thẻ ảnh 904 cao mỗi thẻ (rộng 701), ô danh mục (3 ô ảnh có chữ trắng lớn), đánh giá khách hàng 169, băng chuyền 507, khối giới thiệu 992, ô đăng ký 388, footer 807. Tổng cao khoảng 7593.

- Lề hai bên nội dung: 48px.
- Hero: chữ nằm trái dưới, cách trái 48px. Nhãn 15px trắng, h1 cách nhãn 8px, nút cách h1 khoảng 32px.
- Dải lợi ích: 4 cột bằng nhau (khoảng 356px), có đường kẻ dọc giữa các cột. Icon ở trên, tiêu đề cách icon 20px, mô tả cách tiêu đề 20px, mô tả màu 60%.
- Thẻ sản phẩm (băng chuyền): rộng 271px, ô ảnh nền `#f4f4f4` cao 406px, phần chữ padding `16px 16px 0`, căn giữa. Thứ tự: hãng (13px, màu 60%), tên (13px), giá, sao và điểm.
- Khối hai thẻ ảnh: chữ trong thẻ cách trái 48px, h 37,6px, đoạn văn cách trên 32px.
- Khối chữ nền navy: nhãn 12px/500, tiêu đề 37,6px canh giữa rộng khoảng 960px, đoạn văn cách trên 32px.
- Băng chuyền: cuộn ngang, có thanh tiến trình mảnh ở dưới.

## 5. Trang danh mục

- Tiêu đề và mô tả canh giữa. Dòng "N sản phẩm", chọn kiểu lưới, nút "Lọc và sắp xếp" nằm bên phải.
- Lưới **4 cột tràn rộng**: thẻ 341px, khoảng cách 12px, lề trái 12px. Ô ảnh cao 512px.

## 6. Trang chi tiết sản phẩm

- Hai cột: ảnh bên trái (lưới 2x2, mỗi ô 330 x 495, khoảng cách 4px, bắt đầu x=48), thông tin bên phải (rộng 520, bắt đầu x=809).
- Thứ tự cột phải: h1 (20,2px), giá, sao và "N beoordelingen", nhãn Size (15px, 60%), các nút size (khoảng cách 12px), liên kết Size chart, nút thêm vào giỏ (520 x 53), dòng điểm thưởng, hộp thông tin (nền xám nhạt), các mục xổ xuống (Description, Materials, Care) mỗi dòng cao 61px, padding `20px 0`, có đường kẻ trên.
- Phía dưới: "We think you'll also like these" (h 20,2px) kèm băng chuyền, rồi phần đánh giá.

## 7. Footer

- Nền trắng. 4 cột bắt đầu ở x = 48, 609, 941, 1200. Tiêu đề cột 12px/500, liên kết 15px, khoảng cách dòng 31px. Cao 807 (bao gồm khoảng trắng).

## 8. Hiệu ứng (đo từ thuộc tính transition)

Trang thật dùng **hiệu ứng rất nhẹ và nhanh**, không có thư viện cuộn/animation:

| Hiệu ứng | Giá trị |
|---|---|
| Đổi màu, nền, viền khi hover | `0.2s ease-in-out` |
| Dịch chuyển, phóng to nhẹ (`transform`) | `0.2s ease` |
| Gạch chân liên kết chạy (transition `background-size`) | `0.2s ease-in-out` (một số `0.3s`) |
| Menu thả xuống, ngăn kéo (`opacity` + `transform` + `visibility`) | `0.15s ease-in-out` |
| Xoay mũi tên mục xổ xuống (`rotate`) | `0.2s ease` |
| Header | `position: sticky; top: 0` |
| Thanh thông báo | các câu chuyển qua lại bằng mũi tên trái phải |

## 9. Chưa đo

Trạng thái focus, điểm ngắt chính xác của giao diện điện thoại, ngăn kéo giỏ hàng, bộ lọc (trang thật dùng ngăn kéo).
