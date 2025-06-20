package Login;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class DatabaseManager {
    private Connection connection;
    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    public DatabaseManager() {
        connection = getConnection();
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/qlbenhvien?useLegacyDatetimeCode=true";
            String user = "root";
            String password = ""; // Mặc định MySQL XAMPP không có mật khẩu

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Kết nối thành công với MySQL!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerUser(String username, String email, String password, JFrame parent) {
        try {
            String checkQuery = "SELECT 1 FROM Users WHERE username = ? OR email = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(parent, "Username or email already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(password, salt);

            String query = "INSERT INTO Users (username, email, password, salt) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, Base64.getEncoder().encodeToString(salt));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(parent, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error during registration!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean loginUser(String username, String password, JFrame parent) {
        try {
            String query = "SELECT password, salt FROM Users WHERE username = ? OR email = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String storedSalt = rs.getString("salt");
                byte[] salt = Base64.getDecoder().decode(storedSalt);

                String hashedInputPassword = hashPassword(password, salt);

                if (hashedInputPassword.equals(storedPassword)) {
                    JOptionPane.showMessageDialog(parent, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(parent, "Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(parent, "Username or email not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error during login!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean emailExists(String email) throws SQLException {
        String query = "SELECT 1 FROM Users WHERE email = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public void resetPassword(String email, String newPassword, JDialog parent) {
        try {
            byte[] salt = generateSalt();
            String hashedPassword = hashPassword(newPassword, salt);

            String query = "UPDATE Users SET password = ?, salt = ? WHERE email = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);
            stmt.setString(3, Base64.getEncoder().encodeToString(salt));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(parent, "Password reset successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error updating password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    private String hashPassword(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
