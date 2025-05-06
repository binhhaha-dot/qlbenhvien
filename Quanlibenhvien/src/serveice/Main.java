package serveice;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) throws SQLException {
		Connection con = ketnoi.getConnection();
		
		if(con != null) {
			System.out.println("Kết nối thành công!");
		} else {
			System.out.println("Kết nối thất bại!");
		}
	}

}
