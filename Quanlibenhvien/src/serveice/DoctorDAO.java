package serveice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Doctor;

public class DoctorDAO {
	public int getDoctorIdByName(String name) throws SQLException {
	    String query = "SELECT DoctorID FROM Doctors WHERE FullName = ?";
	    try (Connection conn = 	ketnoi.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, name);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("DoctorID");
	            }
	        }
	    }
	    throw new IllegalArgumentException("Không tìm thấy bác sĩ với tên: " + name);
	}


	    // Thêm bác sĩ mới
	    public boolean addDoctor(Doctor doctor) {
	        String sql = "INSERT INTO Doctors (fullName, specialty, phoneNumber) VALUES (?, ?, ?)";
	        try (Connection conn = ketnoi.getConnection(); 
	                PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, doctor.getFullName());
	            stmt.setString(2, doctor.getSpecialty());
	            stmt.setString(3, doctor.getPhoneNumber());
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    // Sửa thông tin bác sĩ
	    public boolean updateDoctor(Doctor doctor) {
	        String sql = "UPDATE Doctors SET FullName = ?, Specialty = ?, PhoneNumber = ? WHERE DoctorID = ?";
	        try (Connection conn = ketnoi.getConnection(); 
	                PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setString(1, doctor.getFullName());
	            stmt.setString(2, doctor.getSpecialty());
	            stmt.setString(3, doctor.getPhoneNumber());
	            stmt.setInt(4, doctor.getDoctorID());
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }

	    // Xóa bác sĩ theo ID
	    public boolean deleteDoctor(int doctorID) {
	        String deleteAppointments = "DELETE FROM Appointments WHERE DoctorID = ?";
	        String deleteDoctor = "DELETE FROM Doctors WHERE DoctorID = ?";
	        try (Connection conn = ketnoi.getConnection();
	             PreparedStatement pstmt1 = conn.prepareStatement(deleteAppointments);
	             PreparedStatement pstmt2 = conn.prepareStatement(deleteDoctor)) {
	            conn.setAutoCommit(false); // Bắt đầu transaction

	            pstmt1.setInt(1, doctorID);
	            pstmt1.executeUpdate();

	            pstmt2.setInt(1, doctorID);
	            pstmt2.executeUpdate();

	            conn.commit(); // Xác nhận transaction
	            return true;
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return false;
	        }
	    }
	/*    public boolean deleteDoctor1(int doctorID) {
	        String sql = "DELETE FROM Doctors WHERE doctorID = ?";
	        try (Connection conn = ketnoi.getConnection(); 
	                PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, doctorID);
	            return stmt.executeUpdate() > 0;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
*/
	    // Lấy danh sách tất cả bác sĩ
	    public List<Doctor> getAllDoctors() {
	        List<Doctor> doctors = new ArrayList<>();
	        String sql = "SELECT doctorID, fullName, specialty, phoneNumber FROM Doctors";
	        try (Connection conn = ketnoi.getConnection(); 
	                PreparedStatement stmt = conn.prepareStatement(sql); 
	                ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                Doctor doctor = new Doctor(
	                    rs.getInt("doctorID"),
	                    rs.getString("fullName"),
	                    rs.getString("specialty"),
	                    rs.getString("phoneNumber")
	                );
	                doctors.add(doctor);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return doctors;
	    }
	 

	    // Lấy thông tin bác sĩ theo ID
	    public Doctor getDoctorById(int doctorID) {
	        String sql = "SELECT doctorID, fullName, specialty, phoneNumber FROM Doctors WHERE doctorID = ?";
	        try (Connection conn = ketnoi.getConnection(); 
	                PreparedStatement stmt = conn.prepareStatement(sql)) {
	            stmt.setInt(1, doctorID);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return new Doctor(
	                        rs.getInt("doctorID"),
	                        rs.getString("fullName"),
	                        rs.getString("specialty"),
	                        rs.getString("phoneNumber")
	                    );
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	
}
