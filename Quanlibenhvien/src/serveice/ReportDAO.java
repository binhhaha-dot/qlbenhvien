package serveice;
import model.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {


	// Lấy tất cả báo cáo
	public List<Report> getAllReports() {
		List<Report> reports = new ArrayList<>();
		String sql = "SELECT * FROM Reports";
		try (Connection conn = ketnoi.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				Report report = new Report(
						rs.getInt("ReportID"),
						rs.getDate("ReportDate"),
						rs.getInt("TotalPatients"),
						rs.getInt("TotalDoctors"),
						rs.getInt("TodayAppointments"),
						rs.getInt("CompletedAppointments"),
						rs.getInt("CancelledAppointments"),
						rs.getInt("NewPatientsThisMonth")
				);
				reports.add(report);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reports;
	}
	public void updateReport() {
		String sql = """
        INSERT INTO Reports 
        (ReportDate, TotalPatients, TotalDoctors, TodayAppointments, CompletedAppointments, CancelledAppointments, NewPatientsThisMonth)
        VALUES (
            CURDATE(),
            (SELECT COUNT(*) FROM Patients),
            (SELECT COUNT(*) FROM Doctors),
            (SELECT COUNT(*) FROM Appointments WHERE DATE(AppointmentDate) = CURDATE()),
            (SELECT COUNT(*) FROM Appointments WHERE Status = N'Đã khám'),
            (SELECT COUNT(*) FROM Appointments WHERE Status = N'Hủy'),
            (SELECT COUNT(*) FROM MedicalRecord WHERE MONTH(RecordDate) = MONTH(CURDATE()) AND YEAR(RecordDate) = YEAR(CURDATE()))
        )
        """;
		try (Connection conn = ketnoi.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(sql)) {
			int rowsAffected = pstmt.executeUpdate();
			System.out.println("Rows inserted into Reports: " + rowsAffected);
			// Debug truy vấn TodayAppointments
			String debugSql = "SELECT COUNT(*) FROM Appointments WHERE DATE(AppointmentDate) = CURDATE()";
			try (Statement stmt = conn.createStatement();
				 ResultSet rs = stmt.executeQuery(debugSql)) {
				if (rs.next()) {
					System.out.println("TodayAppointments: " + rs.getInt(1));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// Lấy báo cáo mới nhất
	public Report getLatestReport() {
		String sql = "SELECT * FROM Reports ORDER BY ReportID DESC LIMIT 1";
		try (Connection conn = ketnoi.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql);
			 ResultSet rs = stmt.executeQuery()) {
			if (rs.next()) {
				return new Report(
						rs.getInt("ReportID"),
						rs.getDate("ReportDate"),
						rs.getInt("TotalPatients"),
						rs.getInt("TotalDoctors"),
						rs.getInt("TodayAppointments"),
						rs.getInt("CompletedAppointments"),
						rs.getInt("CancelledAppointments"),
						rs.getInt("NewPatientsThisMonth")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

