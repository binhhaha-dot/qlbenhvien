package qlbv;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.Doctor;
import serveice.DoctorDAO;

public class DoctorManagementForm extends JPanel{
	private JTable doctorTable;
	private DefaultTableModel tableModel;
	public DoctorManagementForm() {
        setLayout(new BorderLayout());

        // Tiêu đề
        JLabel title = new JLabel("Quản lý Bác Sĩ", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        // Bảng hiển thị danh sách bác sĩ
        String[] columns = {"ID", "Họ và Tên", "Chuyên Môn", "Số Điện Thoại"};
        Object[][] data = {}; // Dữ liệu sẽ được thêm từ CSDL
        JTable doctorTable = new JTable(data, columns);
        JScrollPane tableScrollPane = new JScrollPane(doctorTable);
        add(tableScrollPane, BorderLayout.CENTER);

        // Form nhập liệu
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Bác Sĩ"));

        formPanel.add(new JLabel("Họ và Tên:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Chuyên Môn:"));
        JTextField specialtyField = new JTextField();
        formPanel.add(specialtyField);

        formPanel.add(new JLabel("Số Điện Thoại:"));
        JTextField phoneField = new JTextField();
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Tìm Bác Sĩ Theo ID:"));
        JTextField searchField = new JTextField();
        formPanel.add(searchField);
        // Các nút hành động
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Thêm");
        JButton updateButton = new JButton("Sửa");
        JButton deleteButton = new JButton("Xóa");
        JButton refreshButton = new JButton("Làm mới");
        JButton searchButton = new JButton("Tìm kiếm");
     
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(searchButton);

        // Thêm form nhập liệu và nút vào giao diện
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String specialty = specialtyField.getText();
            String phone = phoneField.getText();

            // Tạo đối tượng bác sĩ
            Doctor newDoctor = new Doctor(0, name, specialty, phone);

            // Thêm vào CSDL
            DoctorDAO doctorDAO = new DoctorDAO();
            boolean success = doctorDAO.addDoctor(newDoctor);

            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm bác sĩ thành công!");
                loadDoctorData(); 
                nameField.setText("");
                specialtyField.setText("");
                phoneField.setText("");// Làm mới danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Thêm bác sĩ thất bại!");
            }
        });
      
        updateButton.addActionListener(e -> {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Hãy chọn một bác sĩ để sửa!");
                return;
            }

            // Lấy dữ liệu từ bảng và form nhập
            int doctorID = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText();
            String specialty = specialtyField.getText();
            String phone = phoneField.getText();

            // Tạo đối tượng bác sĩ
            Doctor updatedDoctor = new Doctor(doctorID, name, specialty, phone);

            // Cập nhật trong CSDL
            DoctorDAO doctorDAO = new DoctorDAO();
            boolean success = doctorDAO.updateDoctor(updatedDoctor);

            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật bác sĩ thành công!");
                nameField.setText("");
                specialtyField.setText("");
                phoneField.setText("");
                loadDoctorData(); // Làm mới danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật bác sĩ thất bại!");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Hãy chọn một bác sĩ để xóa!");
                return;
            }

            int doctorID = (int) tableModel.getValueAt(selectedRow, 0);

            // Xóa bác sĩ trong CSDL
            DoctorDAO doctorDAO = new DoctorDAO();
            boolean success = doctorDAO.deleteDoctor(doctorID);

            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa bác sĩ thành công!");
                loadDoctorData(); // Làm mới danh sách
            } else {
                JOptionPane.showMessageDialog(this, "Xóa bác sĩ thất bại!");
            }
        });
        refreshButton.addActionListener(e -> {
            loadDoctorData(); // Làm mới danh sách bác sĩ
        });
        doctorTable.getSelectionModel().addListSelectionListener(event -> {
    	    int selectedRow = doctorTable.getSelectedRow();
    	    if (selectedRow != -1) {
    	    	nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
    	    	specialtyField.setText(tableModel.getValueAt(selectedRow, 2).toString());
    	    	phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
    	    	
    	    }
    	});

        tableModel = new DefaultTableModel(columns, 0); // Sử dụng DefaultTableModel
        doctorTable.setModel(tableModel);              // Gắn model vào JTable
        loadDoctorData();                              // Tải dữ liệu từ CSDL
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText().trim();
            if (searchText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập ID bác sĩ để tìm kiếm!");
                return;
            }

            try {
                int doctorID = Integer.parseInt(searchText);
                DoctorDAO doctorDAO = new DoctorDAO();
                Doctor doctor = doctorDAO.getDoctorById(doctorID);

                if (doctor != null) {
                    tableModel.setRowCount(0); // Xóa dữ liệu cũ
                    Object[] rowData = {
                        doctor.getDoctorID(),
                        doctor.getFullName(),
                        doctor.getSpecialty(),
                        doctor.getPhoneNumber()
                    };
                    tableModel.addRow(rowData);
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy bác sĩ với ID: " + doctorID);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID phải là một số hợp lệ!");
            }
        });

        doctorTable.getSelectionModel().addListSelectionListener(event -> {
            int selectedRow = doctorTable.getSelectedRow();
            if (selectedRow != -1) {
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                specialtyField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        loadDoctorData();
    }
    
	
	private void loadDoctorData() {
	    DoctorDAO doctorDAO = new DoctorDAO();
	    List<Doctor> doctors = doctorDAO.getAllDoctors();

	    // Xóa dữ liệu cũ
	    tableModel.setRowCount(0);

	    // Thêm dữ liệu từ CSDL vào bảng
	    for (Doctor doctor : doctors) {
	        Object[] rowData = {
	            doctor.getDoctorID(),
	            doctor.getFullName(),
	            doctor.getSpecialty(),
	            doctor.getPhoneNumber()
	        };
	        tableModel.addRow(rowData);
	    }
	}
}

