package model;

import java.sql.Date;

public class Patient {
	 private int patientID;
	    private String fullName;
	    private String gender;
	    private Date dateOfBirth;
	    private String phoneNumber;
	    private String address;

	    // Constructor
	    public Patient(int patientID, String fullName, String gender, Date dateOfBirth, String phoneNumber, String address) {
	        this.patientID = patientID;
	        this.fullName = fullName;
	        this.gender = gender;
	        this.dateOfBirth = dateOfBirth;
	        this.phoneNumber = phoneNumber;
	        this.address = address;
	    }

	    // Getter and Setter methods
	    public int getPatientID() {
	        return patientID;
	    }

	    public void setPatientID(int patientID) {
	        this.patientID = patientID;
	    }

	    public String getFullName() {
	        return fullName;
	    }

	    public void setFullName(String fullName) {
	        this.fullName = fullName;
	    }

	    public String getGender() {
	        return gender;
	    }
	    public void setGender(String gender) {
	        this.gender = gender;
	    }

	    public Date getDateOfBirth() {
	        return dateOfBirth;
	    }

	    public void setDateOfBirth(Date dateOfBirth) {
	        this.dateOfBirth = dateOfBirth;
	    }

	    public String getPhoneNumber() {
	        return phoneNumber;
	    }

	    public void setPhoneNumber(String phoneNumber) {
	        this.phoneNumber = phoneNumber;
	    }

	    public String getAddress() {
	        return address;
	    }

	    public void setAddress(String address) {
	        this.address = address;
	    }
}
