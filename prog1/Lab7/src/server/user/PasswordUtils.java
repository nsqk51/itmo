package server.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD2");
            byte[] encoded = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : encoded) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD2 не поддерживается", e);
        }
    }
}
