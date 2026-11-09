package com.fashionweb.util;

import com.fashionweb.exception.ValidationException;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Lưu ảnh tải lên (JPG, PNG, WebP, tối đa 2MB) vào một thư mục assets của ứng dụng đang chạy.
 * Lưu ý: ảnh nằm trong thư mục deploy, deploy lại sạch thì mất ảnh đã tải lên.
 */
public final class ImageUploadUtil {

    public static final long MAX_BYTES = 2L * 1024 * 1024;

    private ImageUploadUtil() {
    }

    /** Có chọn file không. */
    public static boolean hasFile(Part part) {
        return part != null && part.getSize() > 0;
    }

    /**
     * Kiểm tra rồi ghi ảnh, trả đường dẫn tương đối (ví dụ assets/images/blogs/ten-123.jpg) để lưu DB.
     *
     * @param field tên trường form, dùng khi báo lỗi
     * @param dir   thư mục tương đối so với gốc web, ví dụ "assets/images/blogs"
     * @param base  phần đầu tên file (thường là slug)
     */
    public static String store(ServletContext ctx, Part part, String field, String dir, String base) throws IOException {
        String ext = switch (part.getContentType() == null ? "" : part.getContentType()) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> null;
        };
        if (ext == null) {
            throw new ValidationException(field, "Chỉ nhận ảnh JPG, PNG hoặc WebP.");
        }
        if (part.getSize() > MAX_BYTES) {
            throw new ValidationException(field, "Ảnh tối đa 2MB.");
        }
        String realDir = ctx.getRealPath("/" + dir);
        if (realDir == null) {
            throw new IOException("Không xác định được thư mục lưu ảnh");
        }
        File folder = new File(realDir);
        if (!folder.exists() && !folder.mkdirs()) {
            throw new IOException("Không tạo được thư mục lưu ảnh: " + realDir);
        }
        String safeBase = SlugUtil.toSlug(base == null ? "" : base);
        String fileName = (safeBase.isEmpty() ? "image" : safeBase) + "-" + System.currentTimeMillis() + ext;
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, Path.of(realDir, fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        return dir + "/" + fileName;
    }

    /** Xóa file ảnh vừa ghi (khi lưu DB thất bại). Bỏ qua nếu không xóa được. */
    public static void deleteQuietly(ServletContext ctx, String relativePath) {
        if (relativePath == null) {
            return;
        }
        String real = ctx.getRealPath("/" + relativePath);
        if (real != null) {
            try {
                Files.deleteIfExists(Path.of(real));
            } catch (IOException ignored) {
                // không ảnh hưởng dữ liệu
            }
        }
    }
}
