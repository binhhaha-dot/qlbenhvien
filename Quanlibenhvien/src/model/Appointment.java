package model;

import java.sql.Timestamp ;

public class Appointment {
		private int appointmentID;
	    private int patientID;  // Liên kết với bảng Patient
	    private int doctorID;   // Liên kết với bảng Doctor
	    private Timestamp appointmentDate;
	    private String status;  // Trạng thái của lịch hẹn (Ví dụ: Đang chờ, Hoàn thành)
	    private String patientName;
	    private String doctorName;
	    // Constructor
	    public Appointment(int appointmentID, int patientID, int doctorID, Timestamp appointmentDate, String status) {
	        this.appointmentID = appointmentID;
	        this.patientID = patientID;
	        this.doctorID = doctorID;
	        this.appointmentDate = appointmentDate;
	        this.status = status;
	    }

	    public String getPatientName() {
			return patientName;
		}

		public void setPatientName(String patientName) {
			this.patientName = patientName;
		}

		public String getDoctorName() {
			return doctorName;
		}

		public void setDoctorName(String doctorName) {
			this.doctorName = doctorName;
		}

		// Getter and Setter methods
	    public int getAppointmentID() {
	        return appointmentID;
	    }

	    public void setAppointmentID(int appointmentID) {
	        this.appointmentID = appointmentID;
	    }

	    public int getPatientID() {
	        return patientID;
	    }

	    public void setPatientID(int patientID) {
	        this.patientID = patientID;
	    }

	    public int getDoctorID() {
	        return doctorID;
	    }

	    public void setDoctorID(int doctorID) {
	        this.doctorID = doctorID;
	    }

	    public Timestamp getAppointmentDate() {
	        return appointmentDate;
	    }

	    public void setAppointmentDate(Timestamp appointmentDate) {
	        this.appointmentDate = appointmentDate;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }
}
