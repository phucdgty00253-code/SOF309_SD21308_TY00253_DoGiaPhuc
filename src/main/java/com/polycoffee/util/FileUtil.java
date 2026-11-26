package com.polycoffee.util;

import com.polycoffee.exception.ValidationException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/** Upload và xóa ảnh trong thư mục /uploads (Lab 4). */
public final class FileUtil {
    public static final long MAX_BYTES = 2L * 1024 * 1024;
    private static final Set<String> EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private FileUtil() {
    }

    /** Lưu file ở trường name. Trả về tên file mới, hoặc null nếu người dùng không chọn file. */
    public static String upload(HttpServletRequest request, String name) throws IOException, ServletException {
        String contentType = request.getContentType();
        if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("multipart/")) {
            return null;
        }
        Part part = request.getPart(name);
        if (part == null || part.getSize() == 0) {
            return null;
        }
        String extension = extensionOf(part.getSubmittedFileName());
        if (!EXTENSIONS.contains(extension)) {
            throw new ValidationException("Ảnh chỉ nhận file JPG, PNG hoặc WEBP.");
        }
        if (part.getSize() > MAX_BYTES) {
            throw new ValidationException("Ảnh tối đa 2 MB.");
        }
        // Tên file do server đặt để tránh trùng và tránh path traversal
        String fileName = UUID.randomUUID() + "." + extension;
        Path folder = uploadFolder(request);
        Files.createDirectories(folder);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, folder.resolve(fileName));
        }
        return fileName;
    }

    /** Xóa file trong thư mục upload; bỏ qua tên rỗng hoặc tên trỏ ra ngoài thư mục. */
    public static boolean delete(HttpServletRequest request, String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            return false;
        }
        Path folder = uploadFolder(request).normalize();
        Path target = folder.resolve(fileName).normalize();
        return folder.equals(target.getParent()) && Files.deleteIfExists(target);
    }

    static String extensionOf(String fileName) {
        int dot = fileName == null ? -1 : fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    private static Path uploadFolder(HttpServletRequest request) {
        return Path.of(request.getServletContext().getRealPath("/"), "uploads");
    }
}
