package serveice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Patient;


public class PatientDAO {
	public int getPatientIdByName(String name) throws SQLException {
	    String query = "SELECT PatientID FROM Patients WHERE FullName = ?";
	    try (Connection conn = ketnoi.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        pstmt.setString(1, name);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("PatientID");
	            }
	        }
	    }
	    throw new IllegalArgumentException("Không tìm thấy bệnh nhân với tên: " + name);
	}

    // Thêm bệnh nhân mới vào CSDL
    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO Patients (FullName, Gender, DateOfBirth, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ketnoi.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getFullName());
            stmt.setString(2, patient.getGender());
            stmt.setDate(3, new java.sql.Date(patient.getDateOfBirth().getTime()));
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getAddress());

            return stmt.executeUpdate() > 0; // Trả về true nếu thêm thành công

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách bệnh nhân từ CSDL
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patients";

        try (Connection conn = ketnoi.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

        	  while (rs.next()) {
                  Patient patient = new Patient(
                  	rs.getInt("PatientID"),
                      rs.getString("FullName"),
                      rs.getString("Gender"),
                      rs.getDate("DateOfBirth"),
                      rs.getString("PhoneNumber"),
                      rs.getString("Address")
                  );
                patients.add(patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Cập nhật thông tin bệnh nhân
    public boolean updatePatient(Patient patient) {
    	String sql = "UPDATE Patients SET FullName = ?, Gender = ?, DateOfBirth = ?, PhoneNumber = ?, Address = ? WHERE PatientID = ?";
        try (Connection conn = ketnoi.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getFullName());
            stmt.setString(2, patient.getGender());
            stmt.setDate(3, new java.sql.Date(patient.getDateOfBirth().getTime()));
            stmt.setString(4, patient.getPhoneNumber());
            stmt.setString(5, patient.getAddress());
            stmt.setInt(6, patient.getPatientID());

            return stmt.executeUpdate() > 0; // Trả về true nếu cập nhật thành công

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deletePatient(int patientID) {
        String deleteAppointments = "DELETE FROM Appointments WHERE patientID = ?";
        String deletePatient = "DELETE FROM Patients WHERE patientID = ?";
        try (Connection conn = ketnoi.getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement(deleteAppointments);
             PreparedStatement pstmt2 = conn.prepareStatement(deletePatient)) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            pstmt1.setInt(1, patientID);
            pstmt1.executeUpdate();

            pstmt2.setInt(1, patientID);
            pstmt2.executeUpdate();

            conn.commit(); // Xác nhận transaction
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa bệnh nhân theo ID
    public boolean deletePatient1(int patientId) {
        String sql = "DELETE FROM Patients WHERE PatientID = ?";
        try (Connection conn = ketnoi.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            return stmt.executeUpdate() > 0; // Trả về true nếu xóa thành công

        } catch (SQLException e) {	
            e.printStackTrace();
            return false;
        }
    }
}

