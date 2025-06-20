package qlbv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Appointment;
import model.Doctor;
import model.Patient;
import serveice.AppointmentDAO;
import serveice.DoctorDAO;
import serveice.PatientDAO;

public class AppointmentManagementForm extends JPanel{
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private PatientDAO patientDAO = new PatientDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private JComboBox<String> patientDropdown;
    private   JComboBox<String> doctorDropdown;
    private  JTextField dateField ;
    private  JComboBox<String> statusDropdown ;
    private JTextField searchField;
    private JButton searchButton;
    public AppointmentManagementForm() {
        setLayout(new BorderLayout());

        // Header
        JLabel title = new JLabel("Quản lý lịch hẹn", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Bảng danh sách lịch hẹn
        String[] columns = {"ID", "Bệnh nhân", "Bác sĩ", "Ngày giờ", "Trạng thái"};
        Object[][] data = {}; // Dữ liệu sẽ thêm sau
        appointmentTable = new JTable(data, columns);
        JScrollPane tableScrollPane = new JScrollPane(appointmentTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Form tìm kiếm
        JPanel searchPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm lịch hẹn"));

        searchPanel.add(new JLabel("Ngày (YYYY-MM-DD):"));
        searchField = new JTextField();
        searchPanel.add(searchField);

        searchButton = new JButton("Tìm kiếm");
        searchPanel.add(searchButton);

        // Thêm form tìm kiếm vào giao diện
        add(searchPanel, BorderLayout.NORTH);
        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin lịch hẹn"));

        formPanel.add(new JLabel("Bệnh nhân:"));
        patientDropdown = new JComboBox<>(); // Thêm dữ liệu từ CSDL
        formPanel.add(patientDropdown);

        formPanel.add(new JLabel("Bác sĩ:"));
        doctorDropdown = new JComboBox<>(); // Thêm dữ liệu từ CSDL
        formPanel.add(doctorDropdown);

        formPanel.add(new JLabel("Ngày giờ:"));
        dateField = new JTextField();
        formPanel.add(dateField);

        formPanel.add(new JLabel("Trạng thái:"));
        statusDropdown = new JComboBox<>(new String[]{"Chờ xác nhận", "Chờ khám", "Đã khám", "Hủy"});
        formPanel.add(statusDropdown);
        JButton	btnAddAppointment = new JButton("Thêm");
        JButton	btnRefresh = new JButton("Làm mới");
        JButton	btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        formPanel.add(btnAddAppointment);
        formPanel.add(btnEdit);
        formPanel.add(btnDelete);
        formPanel.add(btnRefresh);

        add(formPanel, BorderLayout.SOUTH);
        tableModel = new DefaultTableModel(columns, 0); // Sử dụng DefaultTableModel
        appointmentTable.setModel(tableModel);         // Gắn model vào JTable
        loadAppointmentData();
        loadDropdownData();
        searchButton.addActionListener(e ->searchAppointments());
        btnEdit.addActionListener(e -> editAppointment());
        btnRefresh.addActionListener(e -> {
            loadAppointmentData(); // Làm mới danh sách lịch hẹn
            loadDropdownData();    // Làm mới dropdown bệnh nhân và bác sĩ
        });
        btnAddAppointment.addActionListener(e -> addAppointment());
        btnDelete.addActionListener(e -> deleteAppointment());

        appointmentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormWithSelectedRow();
            }
        });



    }
    private void loadAppointmentData() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAllAppointmentsWithDetails();

        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);

        // Thêm dữ liệu từ CSDL vào bảng
        for (Appointment appointment : appointments) {
            Object[] rowData = {
                    appointment.getAppointmentID(),
                    appointment.getPatientName(),
                    appointment.getDoctorName(),
                    appointment.getAppointmentDate(),
                    appointment.getStatus()
            };
            tableModel.addRow(rowData);
        }

    }
    private void addAppointment() {
        try {
            String patientName = (String) patientDropdown.getSelectedItem();
            String doctorName = (String) doctorDropdown.getSelectedItem();
            String dateTime = dateField.getText();
            String status = (String) statusDropdown.getSelectedItem();

            int patientID = patientDAO.getPatientIdByName(patientName);
            int doctorID = doctorDAO.getDoctorIdByName(doctorName);
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            if (appointmentDAO.isAppointmentConflict(doctorID, java.sql.Timestamp.valueOf(dateTime))) {
                JOptionPane.showMessageDialog(this, "Lịch hẹn bị trùng! Vui lòng chọn thời gian khác.", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Appointment newAppointment = new Appointment(0, patientID, doctorID, java.sql.Timestamp.valueOf(dateTime), status);

            appointmentDAO.addAppointment(newAppointment);

            JOptionPane.showMessageDialog(this, "Thêm lịch hẹn thành công!");
            loadAppointmentData();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm lịch hẹn: " + e.getMessage());
        }
    }
    private void deleteAppointment() {
        try {
            int selectedRow = appointmentTable.getSelectedRow(); // Lấy dòng được chọn
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn lịch hẹn để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy ID của lịch hẹn từ bảng
            int appointmentID = (int) tableModel.getValueAt(selectedRow, 0);

            // Xác nhận xóa
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lịch hẹn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Gọi DAO để xóa
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            boolean success = appointmentDAO.deleteAppointment(appointmentID);

            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa lịch hẹn thành công!");
                loadAppointmentData(); // Tải lại danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Xóa lịch hẹn thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa lịch hẹn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editAppointment() {
        try {
            // Kiểm tra xem có dòng nào được chọn trong bảng không
            int selectedRow = appointmentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một lịch hẹn để sửa!");
                return;
            }

            // Lấy thông tin từ bảng
            int appointmentID = (int) tableModel.getValueAt(selectedRow, 0);
            String patientName = (String) patientDropdown.getSelectedItem();
            String doctorName = (String) doctorDropdown.getSelectedItem();
            String dateTime = dateField.getText();
            String status = (String) statusDropdown.getSelectedItem();

            // Lấy ID bệnh nhân và bác sĩ từ tên
            int patientID = patientDAO.getPatientIdByName(patientName);
            int doctorID = doctorDAO.getDoctorIdByName(doctorName);

            // Tạo đối tượng Appointment mới với dữ liệu đã chỉnh sửa
            Appointment updatedAppointment = new Appointment(appointmentID, patientID, doctorID, java.sql.Timestamp.valueOf(dateTime), status);

            // Cập nhật vào cơ sở dữ liệu
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            if (appointmentDAO.updateAppointment(updatedAppointment)) {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch hẹn thành công!");
                loadAppointmentData(); // Làm mới dữ liệu trên bảng
                clearForm();           // Xóa các trường nhập liệu
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật lịch hẹn thất bại!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa lịch hẹn: " + e.getMessage());
        }
    }

    private void loadDropdownData() {
        PatientDAO patientDAO = new PatientDAO();
        DoctorDAO doctorDAO = new DoctorDAO();

        // Nạp dữ liệu cho dropdown bệnh nhân
        List<Patient> patients = patientDAO.getAllPatients();
        for (Patient patient : patients) {
            patientDropdown.addItem(patient.getFullName());
        }

        // Nạp dữ liệu cho dropdown bác sĩ
        List<Doctor> doctors = doctorDAO.getAllDoctors();
        for (Doctor doctor : doctors) {
            doctorDropdown.addItem(doctor.getFullName());
        }
    }
    private void searchAppointments() {
        String date = searchField.getText().trim();

        if (date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày cần tìm kiếm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            AppointmentDAO appointmentDAO = new AppointmentDAO();
            List<Appointment> appointments = appointmentDAO.searchAppointmentsByDate(date);

            // Xóa dữ liệu cũ
            tableModel.setRowCount(0);

            // Hiển thị kết quả tìm kiếm
            for (Appointment appointment : appointments) {
                Object[] rowData = {
                        appointment.getAppointmentID(),
                        appointment.getPatientName(),
                        appointment.getDoctorName(),
                        appointment.getAppointmentDate(),
                        appointment.getStatus()
                };
                tableModel.addRow(rowData);
                searchField.setText("");
            }

            if (appointments.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy lịch hẹn nào vào ngày " + date, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fillFormWithSelectedRow() {
        int selectedRow = appointmentTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }

        // Lấy dữ liệu từ dòng được chọn
        String patientName = (String) tableModel.getValueAt(selectedRow, 1);
        String doctorName = (String) tableModel.getValueAt(selectedRow, 2);
        String dateTime = tableModel.getValueAt(selectedRow, 3).toString();
        String status = (String) tableModel.getValueAt(selectedRow, 4);

        // Điền dữ liệu vào form
        patientDropdown.setSelectedItem(patientName);
        doctorDropdown.setSelectedItem(doctorName);
        dateField.setText(dateTime);
        statusDropdown.setSelectedItem(status);
    }
    private void clearForm() {
        patientDropdown.setSelectedIndex(0);
        doctorDropdown.setSelectedIndex(0);
        dateField.setText("");
        statusDropdown.setSelectedIndex(0);
    }
}
