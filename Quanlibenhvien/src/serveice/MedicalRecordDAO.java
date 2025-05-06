package serveice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.MedicalRecord;

public class MedicalRecordDAO {
	public boolean addMedicalRecord(MedicalRecord record) {
        String sql = "INSERT INTO MedicalRecord (PatientID, DiseaseName, RecordDate, Doctor) VALUES (?, ?, ?, ?)";
        try (Connection conn = ketnoi.getConnection();
   	         PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, record.getPatientID());
            ps.setString(2, record.getDiseaseName());
            ps.setDate(3, new java.sql.Date(record.getRecordDate().getTime()));
            ps.setString(4, record.getDoctor());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Sửa hồ sơ y tế
    public boolean updateMedicalRecord(MedicalRecord record) {
        String sql = "UPDATE MedicalRecord SET PatientID = ?, DiseaseName = ?, RecordDate = ?, Doctor = ? WHERE RecordID = ?";
        try (Connection conn = ketnoi.getConnection();
   	         PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, record.getPatientID());
            ps.setString(2, record.getDiseaseName());
            ps.setDate(3, new java.sql.Date(record.getRecordDate().getTime()));
            ps.setString(4, record.getDoctor());
            ps.setInt(5, record.getRecordID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa hồ sơ y tế
    public boolean deleteMedicalRecord(int recordID) {
        String sql = "DELETE FROM MedicalRecord WHERE RecordID = ?";
        try (Connection conn = ketnoi.getConnection();
   	         PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, recordID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách tất cả hồ sơ y tế
    public List<MedicalRecord> getAllMedicalRecords() {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM MedicalRecord";
        try (Connection conn = ketnoi.getConnection(); 
                PreparedStatement stmt = conn.prepareStatement(sql); 
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MedicalRecord record = new MedicalRecord(
                    rs.getInt("RecordID"),
                    rs.getInt("PatientID"),
                    rs.getString("DiseaseName"),
                    rs.getDate("RecordDate"),
                    rs.getString("Doctor")
                );
                records.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    // Lấy danh sách hồ sơ y tế theo ID bệnh nhân
    public List<MedicalRecord> getMedicalRecordsByPatientID(int patientID) {
        List<MedicalRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM MedicalRecord WHERE patientID = ?";
        try (Connection conn = ketnoi.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MedicalRecord record = new MedicalRecord(
                        rs.getInt("recordID"),
                        rs.getInt("patientID"),
                        rs.getString("diseaseName"),
                        rs.getDate("recordDate"),
                        rs.getString("doctor")
                    );
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

}
