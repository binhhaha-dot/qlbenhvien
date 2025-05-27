package serveice;

import java.sql.Connection;
import java.sql.DriverManager;

public class ketnoi {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/qlbenhvien?useSSL=false&serverTimezone=UTC";
            String username = "root";
            String password = ""; // Mặc định XAMPP không có password cho user root

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);

            System.out.println("Kết nối thành công với MySQL qua XAMPP!");
        } catch (Exception e) {
            System.out.println("Kết nối thất bại:");
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
