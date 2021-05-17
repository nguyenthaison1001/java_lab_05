package utility;

import server.AppServer;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    /**
     * Hashes password;.
     *
     * @param password Password itself.
     * @return Hashed password.
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(password.getBytes());
            BigInteger integers = new BigInteger(1, bytes);
            StringBuilder newPassword = new StringBuilder(integers.toString(16));
            while (newPassword.length() < 32) newPassword.insert(0, "0");
            return newPassword.toString();
        } catch (NoSuchAlgorithmException exception) {
            AppServer.LOGGER.severe("Password hashing algorithm not found!");
            throw new IllegalStateException(exception);
        }
    }
}
