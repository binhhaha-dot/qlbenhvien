package serveice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class ketnoi {
	

    private static final String DB_URL = "jdbc:mysql://localhost:3306/qlbenhvien"; // Địa chỉ cơ sở dữ liệu
    private static final String USER = "root"; // Tên người dùng cơ sở dữ liệu
    private static final String PASSWORD = ""; // Mật khẩu cơ sở dữ liệu

    // Phương thức để thiết lập kết nối tới cơ sở dữ liệu
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    // Phương thức để thực thi truy vấn SELECT và trả về ResultSet
    public static ResultSet executeQuery(String query) {
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            conn = getConnection(); // Thiết lập kết nối
            statement = conn.createStatement(); // Tạo đối tượng Statement
            resultSet = statement.executeQuery(query); // Thực thi truy vấn
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // ResultSet không nên đóng ở đây vì nó sẽ được sử dụng bên ngoài phương thức
        return resultSet;
    }

    // Phương thức để thực thi truy vấn INSERT, UPDATE, hoặc DELETE
    public static int executeUpdate(String query) {
        Connection conn = null;
        Statement statement = null;
        int rowsAffected = 0;
        try {
            conn = getConnection(); // Thiết lập kết nối
            statement = conn.createStatement(); // Tạo đối tượng Statement
            rowsAffected = statement.executeUpdate(query); // Thực thi cập nhật
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo các tài nguyên được đóng trong khối finally
            try {
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rowsAffected;
    }

    // Phương thức phụ trợ để đóng ResultSet, Statement và Connection
    public static void closeResources(ResultSet resultSet, Statement statement, Connection conn) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
