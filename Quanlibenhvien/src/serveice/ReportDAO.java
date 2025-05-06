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
	                GETDATE(),
	                (SELECT COUNT(*) FROM Patients),
	                (SELECT COUNT(*) FROM Doctors),
	                (SELECT COUNT(*) FROM Appointments WHERE CONVERT(DATE, AppointmentDate) = CONVERT(DATE, GETDATE())),
	                (SELECT COUNT(*) FROM Appointments WHERE Status = N'Đã khám'),
	                ( SELECT COUNT(*) FROM [dbo].[Appointments] WHERE Status <> N'Đã khám'),
	                (SELECT COUNT(*) FROM MedicalRecord WHERE MONTH(RecordDate) = MONTH(GETDATE()) AND YEAR(RecordDate) = YEAR(GETDATE()))
	            )
	        """;
	        try (Connection conn = ketnoi.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
    // Lấy báo cáo mới nhất
    public Report getLatestReport() {
        String sql = "SELECT TOP 1 * FROM Reports ORDER BY ReportDate DESC";
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

