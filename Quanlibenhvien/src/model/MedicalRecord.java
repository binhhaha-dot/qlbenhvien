package model;

import java.util.Date;

public class MedicalRecord {
    private int recordID;         // ID hồ sơ y tế
    private int patientID;        // ID bệnh nhân
    private String diseaseName;   // Tên bệnh
    private Date recordDate;      // Ngày lập hồ sơ
    private String doctor;        // Bác sĩ

    // Constructor không tham số
    public MedicalRecord() {
    }

    // Constructor đầy đủ tham số
    public MedicalRecord(int recordID, int patientID, String diseaseName, Date recordDate, String doctor) {
        this.recordID = recordID;
        this.patientID = patientID;
        this.diseaseName = diseaseName;
        this.recordDate = recordDate;
        this.doctor = doctor;
    }

    // Getter và Setter
    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "recordID=" + recordID +
                ", patientID=" + patientID +
                ", diseaseName='" + diseaseName + '\'' +
                ", recordDate=" + recordDate +
                ", doctor='" + doctor + '\'' +
                '}';
    }
}
