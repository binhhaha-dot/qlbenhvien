package model;

import java.sql.Date;

public class Report {
    private int reportID;
    private Date reportDate;            // Ngày tạo báo cáo
    private int totalPatients;          // Tổng số bệnh nhân
    private int totalDoctors;           // Tổng số bác sĩ
    private int todayAppointments;      // Lịch hẹn trong ngày
    private int completedAppointments;  // Lịch hẹn đã hoàn thành
    private int cancelledAppointments;
    private int newPatientsThisMonth;

    // Constructor không tham số
    public Report() {}

    // Constructor đầy đủ tham số
   

    // Getter và Setter
    public int getReportID() {
        return reportID;
    }

    public Report(int reportID, Date reportDate, int totalPatients, int totalDoctors, int todayAppointments,
			int completedAppointments, int cancelledAppointments, int newPatientsThisMonth) {
		super();
		this.reportID = reportID;
		this.reportDate = reportDate;
		this.totalPatients = totalPatients;
		this.totalDoctors = totalDoctors;
		this.todayAppointments = todayAppointments;
		this.completedAppointments = completedAppointments;
		this.cancelledAppointments = cancelledAppointments;
		this.newPatientsThisMonth = newPatientsThisMonth;
	}

	public void setReportID(int reportID) {
        this.reportID = reportID;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public int getTotalPatients() {
        return totalPatients;
    }

    public void setTotalPatients(int totalPatients) {
        this.totalPatients = totalPatients;
    }

    public int getTotalDoctors() {
        return totalDoctors;
    }

    public void setTotalDoctors(int totalDoctors) {
        this.totalDoctors = totalDoctors;
    }

    public int getTodayAppointments() {
        return todayAppointments;
    }

    public void setTodayAppointments(int todayAppointments) {
        this.todayAppointments = todayAppointments;
    }

    public int getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(int completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    public int getCancelledAppointments() {
		return cancelledAppointments;
	}

	public void setCancelledAppointments(int cancelledAppointments) {
		this.cancelledAppointments = cancelledAppointments;
	}

	public int getNewPatientsThisMonth() {
		return newPatientsThisMonth;
	}

	public void setNewPatientsThisMonth(int newPatientsThisMonth) {
		this.newPatientsThisMonth = newPatientsThisMonth;
	}

	// Phương thức toString (nếu cần để debug hoặc hiển thị)
    @Override
    public String toString() {
        return "Report{" +
                "reportID=" + reportID +
                ", reportDate=" + reportDate +
                ", totalPatients=" + totalPatients +
                ", totalDoctors=" + totalDoctors +
                ", todayAppointments=" + todayAppointments +
                ", completedAppointments=" + completedAppointments +
                '}';
    }
}
