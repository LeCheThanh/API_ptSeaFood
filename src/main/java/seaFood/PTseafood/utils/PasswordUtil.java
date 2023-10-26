package seaFood.PTseafood.utils;

public class PasswordUtil {
    public static boolean isStrongPassword(String password) {
        // Quy tắc mật khẩu mạnh: ít nhất 8 ký tự, chứa chữ hoa, chữ thường, số và ký tự đặc biệt
        return password != null && password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()].*");
    }
}
