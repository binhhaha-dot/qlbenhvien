package serveice;

import java.sql.Connection;
import java.sql.DriverManager;
public class ketnoi {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String serverName = "DESKTOP-L1QH45U\\SQLEXPRESS";
            String login = "sa";
            String password = "123456789";
            String databaseName = "qlbenhvien";

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + serverName + ":1433" + ";databaseName=" + databaseName
                    + ";encrypt=true;trustServerCertificate=true";

            connection = DriverManager.getConnection(url, login, password);

            System.out.println("Kết nối thành công với database: " + databaseName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
    public static void closeConnection(Connection connection) {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
