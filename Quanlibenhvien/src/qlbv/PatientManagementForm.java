package qlbv;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

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

import model.Patient;
import serveice.PatientDAO;

public class PatientManagementForm extends JPanel {
	 private JTable patientTable;
	 private DefaultTableModel tableModel;
	 public PatientManagementForm() {
		 
	        setLayout(new BorderLayout());
	        // Tiêu đề
	        JLabel title = new JLabel("Quản lý Bệnh Nhân", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 20));
	        add(title, BorderLayout.NORTH);

	        // Bảng hiển thị danh sách bệnh nhân
	        String[] columns = {"ID", "Họ và Tên", "Giới Tính", "Ngày Sinh", "Số Điện Thoại", "Địa Chỉ"};
	        Object[][] data = {}; // Dữ liệu sẽ được thêm từ CSDL
	        patientTable = new JTable(data, columns);
	        JScrollPane tableScrollPane = new JScrollPane(patientTable);
	        add(tableScrollPane, BorderLayout.CENTER);
	
	        // Form nhập liệu
	        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
	        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Bệnh Nhân"));

	        formPanel.add(new JLabel("Họ và Tên:"));
	        JTextField nameField = new JTextField();
	        formPanel.add(nameField);

	        formPanel.add(new JLabel("Giới Tính:"));
	        JComboBox<String> genderDropdown = new JComboBox<>(new String[]{"Nam", "Nữ"});
	        formPanel.add(genderDropdown);

	        formPanel.add(new JLabel("Ngày Sinh:"));
	        JTextField dobField = new JTextField();
	        formPanel.add(dobField);

	        formPanel.add(new JLabel("Số Điện Thoại:"));
	        JTextField phoneField = new JTextField();
	        formPanel.add(phoneField);

	        formPanel.add(new JLabel("Địa Chỉ:"));
	        JTextField addressField = new JTextField();
	        formPanel.add(addressField);

	        // Các nút hành động
	        JPanel buttonPanel = new JPanel(new FlowLayout());
	        JButton btnAddPatient = new JButton("Thêm");
	        JButton updateButton = new JButton("Sửa");
	        JButton deleteButton = new JButton("Xóa");
	        JButton refreshButton = new JButton("Làm Mới");
	      
	        buttonPanel.add(btnAddPatient);
	        buttonPanel.add(updateButton);
	        buttonPanel.add(deleteButton);
	        buttonPanel.add(refreshButton);

	        // Thêm form nhập liệu và nút vào giao diện
	        JPanel southPanel = new JPanel(new BorderLayout());
	        southPanel.add(formPanel, BorderLayout.CENTER);
	        southPanel.add(buttonPanel, BorderLayout.SOUTH);

	        add(southPanel, BorderLayout.SOUTH);
	        tableModel = new DefaultTableModel(columns, 0); 
	        patientTable.setModel(tableModel);             
	        loadPatientData(); 
	        btnAddPatient.addActionListener(e -> {
	            String fullName = nameField.getText();
	            String gender = genderDropdown.getSelectedItem().toString();
	            String dateOfBirth = dobField.getText();
	            String phoneNumber = phoneField.getText();
	            String address = addressField.getText();

	            // Tạo đối tượng Patient
	            Patient newPatient = new Patient(0, fullName, gender, java.sql.Date.valueOf(dateOfBirth), phoneNumber, address);

	            // Thêm vào CSDL
	            PatientDAO patientDAO = new PatientDAO();
	            boolean success = patientDAO.addPatient(newPatient);

	            if (success) {
	                JOptionPane.showMessageDialog(this, "Thêm bệnh nhân thành công!");
	                loadPatientData(); // Làm mới bảng
	                nameField.setText("");
	                genderDropdown.setSelectedIndex(0);
	                dobField.setText("");
	                phoneField.setText("");
	                addressField.setText("");
	            } else {
	                JOptionPane.showMessageDialog(this, "Thêm bệnh nhân thất bại!");
	            }
	        });
	        updateButton.addActionListener(e -> {
	            int selectedRow = patientTable.getSelectedRow();
	            if (selectedRow == -1) {
	                JOptionPane.showMessageDialog(this, "Hãy chọn một bệnh nhân để sửa!");
	                return;
	            }

	            int patientID = (int) tableModel.getValueAt(selectedRow, 0);
	            String fullName = nameField.getText();
	            String gender = genderDropdown.getSelectedItem().toString();
	            String dateOfBirth = dobField.getText();
	            String phoneNumber = phoneField.getText();
	            String address = addressField.getText();

	            // Tạo đối tượng Patient
	            Patient updatedPatient = new Patient(patientID, fullName, gender, java.sql.Date.valueOf(dateOfBirth), phoneNumber, address);

	            // Sửa trong CSDL
	            PatientDAO patientDAO = new PatientDAO();
	            boolean success = patientDAO.updatePatient(updatedPatient);

	            if (success) {
	                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
	                loadPatientData();
	                nameField.setText("");
	                genderDropdown.setSelectedIndex(0);
	                dobField.setText("");
	                phoneField.setText("");
	                addressField.setText("");// Làm mới bảng
	            } else {
	                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thất bại!");
	            }
	        });
	        deleteButton.addActionListener(e -> {
	            int selectedRow = patientTable.getSelectedRow();
	            if (selectedRow == -1) {
	                JOptionPane.showMessageDialog(this, "Hãy chọn một bệnh nhân để xóa!");
	                return;
	            }

	            int patientID = (int) tableModel.getValueAt(selectedRow, 0);

	            // Xóa bệnh nhân khỏi CSDL
	            PatientDAO patientDAO = new PatientDAO();
	            boolean success = patientDAO.deletePatient(patientID);

	            if (success) {
	                JOptionPane.showMessageDialog(this, "Xóa bệnh nhân thành công!");
	                loadPatientData(); // Làm mới bảng
	            } else {
	                JOptionPane.showMessageDialog(this, "Xóa bệnh nhân thất bại!");
	            }
	        });
	        refreshButton.addActionListener(e -> {
	            loadPatientData(); // Tải lại danh sách bệnh nhân
	        });
	        patientTable.getSelectionModel().addListSelectionListener(event -> {
	    	    int selectedRow = patientTable.getSelectedRow();
	    	    if (selectedRow != -1) {
	    	    	nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
	    	    	genderDropdown.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
	    	    	dobField.setText(tableModel.getValueAt(selectedRow, 3).toString());
	    	    	phoneField.setText(tableModel.getValueAt(selectedRow, 4).toString());
	    	    	addressField.setText(tableModel.getValueAt(selectedRow, 5).toString());
	    	    }
	    	});


	        }
	 private void loadPatientData() {
		    // Tải danh sách bệnh nhân từ DAO
		    PatientDAO patientDAO = new PatientDAO();
		    List<Patient> patients = patientDAO.getAllPatients();

		    // Xóa dữ liệu cũ trong bảng
		    tableModel.setRowCount(0);

		    // Thêm từng bệnh nhân vào bảng
		    for (Patient patient : patients) {
		        Object[] rowData = {
		            patient.getPatientID(),
		            patient.getFullName(),
		            patient.getGender(),
		            patient.getDateOfBirth(),
		            patient.getPhoneNumber(),
		            patient.getAddress()
		        };
		        tableModel.addRow(rowData);
		    }
		}
	    
}
