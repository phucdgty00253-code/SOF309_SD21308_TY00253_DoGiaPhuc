# Workflow tạo ảnh cho website Saltair

Tài liệu dành cho agent thực hiện (ví dụ Codex). Mục tiêu: tạo và lưu toàn bộ ảnh sản phẩm và ảnh banner cho website bán đồ basic phong cách biển của thương hiệu giả **Saltair**.

Cách chạy: đọc file này, làm **Phase 1** (tạo ảnh) xong rồi làm **Phase 2** (gắn logo Saltair). Logo đã có sẵn trong dự án.

## Quy tắc bắt buộc

1. Chỉ **tự tạo ảnh bằng công cụ sinh ảnh** của bạn. Nếu không có công cụ sinh ảnh, **dừng lại và báo rõ**, không thay bằng cách khác.
2. **Không tải hay sao chép ảnh từ website bán hàng hay thương hiệu nào.** Không dùng ảnh của người khác rồi xóa tên hay logo.
3. Không nhắc tên thương hiệu thật hoặc "phong cách giống hãng X" trong prompt.
4. Ảnh phải **không có logo, chữ, nhãn mác, tem, hình mờ** (watermark). Phần logo Saltair sẽ gắn riêng ở Phase 2.
5. Không ghi đè ảnh đã có `status=done` trừ khi được yêu cầu.
6. Đặt tên file đúng quy ước bên dưới, chữ thường, không dấu, nối bằng gạch ngang.

## Dữ liệu đầu vào

- `docs/product-catalog.csv`: 24 sản phẩm. Các cột dùng ở đây: `slug`, `name`, `category_slug`, `main_color`, `item_en`, `image_file`, `status`.
- `docs/product-catalog.md`: bảng màu (mã màu sang tên tiếng Anh dùng trong prompt).

## Thư mục đầu ra (tạo nếu chưa có)

```
src/main/webapp/assets/images/
  products/     ảnh sản phẩm (24 file)
  banners/      ảnh banner
  categories/   ảnh bìa danh mục (4 file)
  _raw/         bản gốc chưa gắn logo (Phase 2 sẽ đọc từ đây)
```

Mỗi ảnh lưu **hai bản**: bản gốc vào `_raw/<cùng đường dẫn con>/`, bản dùng cho web vào thư mục chính. Ở Phase 1 hai bản giống nhau.

## Phase 1: Tạo ảnh

### 1A. Ảnh sản phẩm (24 ảnh)

- Lưu vào `products/<image_file>` (ví dụ `products/ao-thun-co-tron.jpg`).
- Tỉ lệ 1:1, 1200x1200, JPG, nén khoảng chất lượng 85, nhẹ hơn 300 KB nếu được.
- Một ảnh cho mỗi sản phẩm, dùng **màu chính** (`main_color`).

Đổi mã màu sang tiếng Anh khi điền prompt:

| Mã màu | Màu trong prompt |
|---|---|
| `trang-nga` | off-white ivory |
| `den` | black |
| `xanh-navy` | navy blue |
| `xanh-bien-nhat` | light sea blue, pale aqua-blue |
| `xam` | heather gray |
| `be-cat` | sand beige |

**Mẫu prompt cho áo thun, áo nỉ, hoodie, áo khoác** (đặt thông tin từ CSV vào `{item_en}` và `{color_en}`):

> Professional e-commerce product photo of a {item_en}, color: {color_en}. Front view on an invisible ghost mannequin, centered, the garment fills about 70% of the frame. Plain seamless light gray-white background (#f4f4f4), soft natural studio lighting, only a very soft shadow directly under the product, sharp fabric texture, true-to-life color. Garment only: no person, no model, no hanger, no props. Absolutely no logo, no brand name, no text, no label, no tag, no watermark. Square 1:1 image.

**Mẫu prompt cho quần** (`category_slug = quan`):

> Professional e-commerce product photo of {item_en}, color: {color_en}. Laid flat, front view, centered from the waist to the hem, fills about 70% of the frame. Plain seamless light gray-white background (#f4f4f4), soft natural studio lighting from above, very soft shadow, sharp fabric texture, true-to-life color. Pants only: no person, no model, no props. Absolutely no logo, no brand name, no text, no label, no tag, no watermark. Square 1:1 image.

### 1B. Ảnh banner (3 ảnh)

| File | Tỉ lệ và kích thước | Prompt |
|---|---|---|
| `banners/hero-01.jpg` | 21:9, 2400x1030 | Bright high-key lifestyle photo on a beach in full daylight, cloudless blue sky, turquoise sea, one young person in neutral basic clothing (off-white tee and sand beige shorts) walking along the shoreline, natural light, clean airy mood, empty space on the left for headline text. No logo, no text. |
| `banners/hero-02.jpg` | 21:9, 2400x1030 | Bright lifestyle photo of a young person in a navy windbreaker jacket standing on a coastal pier in the morning, pale blue sky, calm sea, soft natural light, clean airy mood, empty space on the left for text. No logo, no text. |
| `banners/story-01.jpg` | 4:5, 1200x1500 | Flat-lay on a white surface seen from above: folded basic clothes (off-white tee, navy sweatshirt, sand beige shorts) with a few natural objects (a seashell, a sprig of dried grass), soft natural light, minimal and clean. No logo, no text. |

### 1C. Ảnh bìa danh mục (4 ảnh)

- Lưu vào `categories/<category_slug>.jpg`, tỉ lệ 4:5, 1200x1500.
- Prompt chung: *Clean flat-lay product photo from above on a plain light gray-white background (#f4f4f4), 2 to 3 folded {category_en} in off-white, navy blue and sand beige, soft natural light. No person, no logo, no text.*

| File | `{category_en}` |
|---|---|
| `categories/ao-thun-polo.jpg` | t-shirts and a polo shirt |
| `categories/ao-ni-hoodie.jpg` | sweatshirts and a hoodie |
| `categories/ao-khoac.jpg` | lightweight jackets |
| `categories/quan.jpg` | trousers and shorts |

### 1D. Kiểm tra từng ảnh trước khi lưu

- [ ] Đúng loại sản phẩm và đúng màu chính
- [ ] Chỉ có một sản phẩm (ảnh sản phẩm), không người, không đạo cụ
- [ ] Nền trơn sáng, đồng nhất với các ảnh khác
- [ ] **Không có logo, chữ, nhãn, hình mờ**
- [ ] Không méo hình, không thừa tay áo hay ống quần, không lỗi chi tiết rõ rệt
- [ ] Đúng tỉ lệ, kích thước và tên file

Không đạt thì tạo lại, tối đa **3 lần**. Vẫn không đạt thì để `status=needs-review` và ghi lý do vào báo cáo.

### 1E. Cập nhật tiến độ

- Sau mỗi ảnh sản phẩm đạt yêu cầu, sửa cột `status` trong `docs/product-catalog.csv` thành `done` (hoặc `needs-review`).
- Làm theo từng danh mục: Áo thun và Polo, rồi Áo nỉ và Hoodie, Áo khoác, Quần, cuối cùng banner và ảnh bìa danh mục.
- Cuối Phase 1, báo cáo gồm: số ảnh `done`, danh sách `needs-review` kèm lý do, đường dẫn thư mục đầu ra.

## Phase 2: Gắn logo Saltair

Làm sau khi Phase 1 xong. Logo đã có sẵn trong `src/main/webapp/assets/brand/`:

| File | Dùng khi |
|---|---|
| `saltair-logo.png` | Logo đen, nền trong suốt. Dùng trên ảnh có vùng đặt logo sáng |
| `saltair-logo-white.png` | Logo trắng, nền trong suốt. Dùng trên ảnh có vùng đặt logo tối (áo đen, navy, nền tối) |
| `saltair-logo-original.webp` | Bản gốc, chỉ để lưu, không dùng để gắn |

Logo gồm biểu tượng (hình giọt nước có sóng) và chữ "Saltair" bên dưới. **Không** đổi màu ngoài hai bản trên, không kéo giãn, không xoay, không cắt.

### Quy trình

1. Viết script lặp lại được ở `tools/add_logo.py` (Python 3, thư viện Pillow; cài bằng `pip install pillow` nếu thiếu). Script nhận tham số thư mục vào, thư mục ra và đường dẫn logo.
2. Đọc từng ảnh sản phẩm trong `_raw/products/`, gắn logo, ghi vào `products/`. **Không sửa `_raw/`.**
3. Chỉ gắn lên **24 ảnh sản phẩm**. Không gắn lên banner và ảnh bìa danh mục.

### Quy cách gắn logo (ảnh sản phẩm 1200x1200)

- Chiều rộng logo bằng 12% chiều rộng ảnh (khoảng 144 px).
- Đặt ở **góc dưới phải**, cách mép phải và mép dưới 5% (khoảng 60 px).
- Độ mờ 85%.
- Chọn bản logo theo độ sáng trung bình của vùng đặt logo (lấy khung hình chữ nhật đúng chỗ đặt): nếu độ sáng nhỏ hơn 110 trên thang 0 đến 255 thì dùng `saltair-logo-white.png`, ngược lại dùng `saltair-logo.png`. Nền ảnh sản phẩm vốn sáng (#f4f4f4) nên phần lớn dùng logo đen.
- Nếu logo đè lên sản phẩm (ví dụ ống quần dài chạm góc), dời sang góc dưới trái và ghi chú trong báo cáo.

### Kiểm tra

- [ ] Logo rõ nét, không bị răng cưa, đúng tỉ lệ
- [ ] Không che sản phẩm, không sát mép
- [ ] Đủ tương phản (đúng bản đen hoặc trắng)
- [ ] File sau khi gắn vẫn là JPG 1200x1200, dưới 300 KB nếu được

Cuối Phase 2, báo cáo số ảnh đã gắn, danh sách ảnh phải dời vị trí hoặc đổi sang logo trắng.

## Ghi nhớ

- Ảnh dùng cho đồ án học tập, nhưng vẫn phải do chính mình tạo hoặc có giấy phép sử dụng.
- Thư mục `_raw/` có thể thêm vào `.gitignore` nếu dung lượng repo lớn.
