package model;

public class Doctor {
		private int doctorID;
	    private String fullName;
	    private String specialty;  // Chuyên khoa
	    private String phoneNumber;
	    // Constructor
	    public Doctor(int doctorID, String fullName, String specialty, String phoneNumber) {
	        this.doctorID = doctorID;
	        this.fullName = fullName;
	        this.specialty = specialty;
	        this.phoneNumber = phoneNumber;
	    }

	    // Getter and Setter methods
	    public int getDoctorID() {
	        return doctorID;
	    }

	    public void setDoctorID(int doctorID) {
	        this.doctorID = doctorID;
	    }

	    public String getFullName() {
	        return fullName;
	    }

	    public void setFullName(String fullName) {
	        this.fullName = fullName;
	    }

	    public String getSpecialty() {
	        return specialty;
	    }

	    public void setSpecialty(String specialty) {
	        this.specialty = specialty;
	    }

		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
	    
}
