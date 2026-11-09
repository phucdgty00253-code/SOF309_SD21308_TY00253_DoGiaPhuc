package com.fashionweb.service.auth;

import com.fashionweb.constant.Role;
import com.fashionweb.dao.auth.UserDAO;
import com.fashionweb.exception.ValidationException;
import com.fashionweb.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Đăng nhập, đăng ký, quên và đặt lại mật khẩu.
 *
 * <p>Đã làm: đăng nhập, đăng ký (#1), quên mật khẩu (#3).
 * <p>Luồng gọi: Servlet -> Service -> DAO -> MySQL.
 */
public class AuthService {

    /** BCrypt chỉ dùng 72 byte đầu nên giới hạn độ dài tối đa. */
    private static final int MIN_PASSWORD = 6;
    private static final int MAX_PASSWORD = 72;
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("0[0-9]{9}");

    /** Thời hạn liên kết đặt lại mật khẩu (phút). */
    public static final int RESET_TOKEN_MINUTES = 15;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserDAO userDAO = new UserDAO();

    /**
     * Kiểm tra BCrypt. Trả null nếu sai; ném lỗi riêng nếu tài khoản bị khóa (is_active = 0).
     */
    public User login(String email, String password) {
        if (email == null || password == null || email.isBlank() || password.isEmpty()) {
            return null;
        }
        User u = userDAO.findByEmail(email.trim());
        if (u == null || !checkPassword(password, u.getPasswordHash())) {
            return null;
        }
        // Chỉ báo "bị khóa" khi đã nhập đúng mật khẩu, để không lộ thông tin tài khoản cho người đoán mò
        if (!u.isActive()) {
            throw new ValidationException(null, "Tài khoản của bạn đang bị khóa. Vui lòng liên hệ Saltair để được hỗ trợ.");
        }
        u.setPasswordHash(null); // không giữ chuỗi băm trong session
        return u;
    }

    /**
     * Kiểm tra dữ liệu và email chưa tồn tại, băm BCrypt, INSERT role CUSTOMER.
     * Giỏ hàng được tạo khi khách thêm sản phẩm đầu tiên (CartService.getCart).
     * Sai thì ném ValidationException với lỗi theo ô: fullName, email, phone, password.
     */
    public User register(User user, String rawPassword) {
        Map<String, String> errors = new LinkedHashMap<>();
        String fullName = user.getFullName() == null ? "" : user.getFullName().trim();
        String email = user.getEmail() == null ? "" : user.getEmail().trim().toLowerCase(Locale.ROOT);
        String phone = user.getPhone() == null ? "" : user.getPhone().trim();

        if (fullName.length() < 2 || fullName.length() > 100) {
            errors.put("fullName", "Họ và tên từ 2 đến 100 ký tự.");
        }
        if (email.isEmpty() || email.length() > 150 || !EMAIL.matcher(email).matches()) {
            errors.put("email", "Email không đúng định dạng.");
        } else if (userDAO.existsByEmail(email)) {
            errors.put("email", "Email này đã được đăng ký. Hãy đăng nhập hoặc dùng email khác.");
        }
        if (!PHONE.matcher(phone).matches()) {
            errors.put("phone", "Số điện thoại gồm 10 chữ số, bắt đầu bằng 0.");
        }
        if (rawPassword == null || rawPassword.length() < MIN_PASSWORD || rawPassword.length() > MAX_PASSWORD) {
            errors.put("password", "Mật khẩu từ " + MIN_PASSWORD + " đến " + MAX_PASSWORD + " ký tự.");
        } else if (rawPassword.isBlank()) {
            errors.put("password", "Mật khẩu không được chỉ gồm khoảng trắng.");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRole(Role.CUSTOMER);
        user.setActive(true);
        user.setPasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt(10)));
        user.setId(userDAO.insert(user));
        user.setPasswordHash(null);
        return user;
    }

    private static boolean checkPassword(String raw, String hash) {
        try {
            return hash != null && BCrypt.checkpw(raw, hash);
        } catch (IllegalArgumentException e) {
            return false; // chuỗi băm hỏng hoặc không phải BCrypt
        }
    }

    /**
     * Tạo token ngẫu nhiên (32 byte, Base64 URL), lưu băm SHA-256 vào password_resets (hạn 15 phút), trả token để gửi mail.
     * Trả null nếu email không tồn tại hoặc tài khoản bị khóa (Servlet vẫn báo chung chung).
     */
    public String createResetToken(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        User u = userDAO.findByEmail(email.trim().toLowerCase(Locale.ROOT));
        if (u == null || !u.isActive()) {
            return null;
        }
        byte[] raw = new byte[32];
        RANDOM.nextBytes(raw);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(raw);
        userDAO.insertResetToken(u.getId(), sha256(token), RESET_TOKEN_MINUTES);
        return token;
    }

    /**
     * Token tồn tại, chưa dùng, chưa hết hạn.
     */
    public boolean isResetTokenValid(String token) {
        return token != null && !token.isBlank() && userDAO.findUserIdByValidToken(sha256(token.trim())) != null;
    }

    /**
     * Băm BCrypt, cập nhật mật khẩu, đánh dấu token đã dùng (trong một transaction).
     * Mật khẩu 6 đến 72 ký tự; token sai, đã dùng hoặc hết hạn thì ném ValidationException với field "token".
     */
    public void resetPassword(String token, String newPassword) {
        if (newPassword == null || newPassword.isBlank() || newPassword.length() < MIN_PASSWORD || newPassword.length() > MAX_PASSWORD) {
            throw new ValidationException("password", "Mật khẩu từ 6 đến 72 ký tự.");
        }
        if (token == null || token.isBlank()
                || !userDAO.resetPasswordByToken(sha256(token.trim()), BCrypt.hashpw(newPassword, BCrypt.gensalt(10)))) {
            throw new ValidationException("token", "Liên kết đặt lại mật khẩu không hợp lệ, đã dùng hoặc đã hết hạn.");
        }
    }

    /** Chuỗi hex SHA-256 (64 ký tự) để lưu token, không lưu token gốc. */
    private static String sha256(String s) {
        try {
            byte[] d = MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(64);
            for (byte b : d) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
