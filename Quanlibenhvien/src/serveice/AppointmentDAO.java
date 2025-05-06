package serveice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Appointment;

public class AppointmentDAO {
	    // Thêm lịch hẹn
	    public boolean addAppointment(Appointment appointment) {
	        String sql = "INSERT INTO Appointments (patientID, doctorID, appointmentDate, status) VALUES (?, ?, ?, ?)";
	        try (Connection conn = ketnoi.getConnection(); 
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, appointment.getPatientID());
	            pstmt.setInt(2, appointment.getDoctorID());
	            pstmt.setTimestamp(3, appointment.getAppointmentDate());
	            pstmt.setString(4, appointment.getStatus());
	            return pstmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    public List<Appointment> getAllAppointmentsWithDetails() {
	        List<Appointment> appointments = new ArrayList<>();
	        String sql = """
	            SELECT 
	                a.appointmentID,
	                a.patientID,
	                p.fullName AS patientName,
	                a.doctorID,
	                d.fullName AS doctorName,
	                a.appointmentDate,
	                a.status
	            FROM 
	                Appointments a
	            INNER JOIN Patients p ON a.patientID = p.patientID
	            INNER JOIN Doctors d ON a.doctorID = d.doctorID
	        """;
	        try (Connection conn = ketnoi.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Appointment appointment = new Appointment(
	                    rs.getInt("appointmentID"),
	                    rs.getInt("patientID"),
	                    rs.getInt("doctorID"),
	                    rs.getTimestamp("appointmentDate"),
	                    rs.getString("status")
	                );
	                // Bổ sung thông tin chi tiết
	                appointment.setPatientName(rs.getString("patientName"));
	                appointment.setDoctorName(rs.getString("doctorName"));
	                appointments.add(appointment);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return appointments;
	    }


	    // Cập nhật lịch hẹn
	    public boolean updateAppointment(Appointment appointment) {
	        String sql = "UPDATE Appointments SET patientID = ?, doctorID = ?, appointmentDate = ?, status = ? WHERE appointmentID = ?";
	        try (Connection conn = ketnoi.getConnection(); 
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, appointment.getPatientID());
	            pstmt.setInt(2, appointment.getDoctorID());
	            pstmt.setTimestamp(3, appointment.getAppointmentDate());
	            pstmt.setString(4, appointment.getStatus());
	            pstmt.setInt(5, appointment.getAppointmentID());
	            return pstmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    // Xóa lịch hẹn
	    public boolean deleteAppointment(int appointmentID) {
	        String sql = "DELETE FROM Appointments WHERE appointmentID = ?";
	        try (Connection conn = ketnoi.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)){
	        	  pstmt.setInt(1, appointmentID); // Thiếu dòng này
	            return pstmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	    // Lấy thông tin lịch hẹn theo ID
	    public Appointment getAppointmentByID(int appointmentID) {
	        String sql = "SELECT * FROM Appointments WHERE appointmentID = ?";
	        try (Connection conn = ketnoi.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, appointmentID);
	            try (ResultSet rs = pstmt.executeQuery()) {
	                if (rs.next()) {
	                    return new Appointment(
	                        rs.getInt("appointmentID"),
	                        rs.getInt("patientID"),
	                        rs.getInt("doctorID"),
	                        rs.getTimestamp("appointmentDate"),
	                        rs.getString("status")
	                    );
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	  
	    public List<Appointment> searchAppointmentsByDate(String date) {
	        List<Appointment> appointments = new ArrayList<>();
	        String sql = """
	            SELECT 
	                a.appointmentID,
	                a.patientID,
	                p.fullName AS patientName,
	                a.doctorID,
	                d.fullName AS doctorName,
	                a.appointmentDate,
	                a.status
	            FROM 
	                Appointments a
	            INNER JOIN Patients p ON a.patientID = p.patientID
	            INNER JOIN Doctors d ON a.doctorID = d.doctorID
	            WHERE CAST(a.appointmentDate AS DATE) = ?
	        """;
	        try (Connection conn = ketnoi.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, date);
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    Appointment appointment = new Appointment(
	                        rs.getInt("appointmentID"),
	                        rs.getInt("patientID"),
	                        rs.getInt("doctorID"),
	                        rs.getTimestamp("appointmentDate"),
	                        rs.getString("status")
	                    );
	                    // Bổ sung thông tin chi tiết
	                    appointment.setPatientName(rs.getString("patientName"));
	                    appointment.setDoctorName(rs.getString("doctorName"));
	                    appointments.add(appointment);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return appointments;
	    }
	    public boolean isAppointmentConflict(int doctorID, Timestamp dateTime) {
	        try (Connection conn = ketnoi.getConnection()) {
	            String query = "SELECT COUNT(*) FROM Appointments WHERE doctorID = ? AND appointmentDate = ?";
	            PreparedStatement stmt = conn.prepareStatement(query);
	            stmt.setInt(1, doctorID);
	            stmt.setTimestamp(2, dateTime);

	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                return rs.getInt(1) > 0; 
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
}
