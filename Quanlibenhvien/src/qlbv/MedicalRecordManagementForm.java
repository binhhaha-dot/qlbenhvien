package qlbv;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import model.MedicalRecord;
import serveice.MedicalRecordDAO;

public class MedicalRecordManagementForm extends JPanel {
	  private JTable recordTable;
	   private DefaultTableModel tableModel;
	   private JTextField patientIDField;
	   private  JTextField diseaseField;
	   private JTextField dateField;
	   private JTextField doctorField;
	   private MedicalRecordDAO medicalRecordDAO;
	   private JTextField searchField;
	   public MedicalRecordManagementForm() {
		   this.medicalRecordDAO = new MedicalRecordDAO();
	        setLayout(new BorderLayout());

	        // Tiêu đề
	        JLabel title = new JLabel("Quản lý Hồ Sơ Y Tế", JLabel.CENTER);
	        title.setFont(new Font("Arial", Font.BOLD, 20));
	        add(title, BorderLayout.NORTH);

	        // Bảng hiển thị danh sách hồ sơ
	        String[] columns = {"ID", "ID Bệnh Nhân", "Tên Bệnh", "Ngày Lập", "Bác Sĩ"};
	        tableModel = new DefaultTableModel(columns, 0);
	        recordTable = new JTable(tableModel);
	        JScrollPane tableScrollPane = new JScrollPane(recordTable);
	        add(tableScrollPane, BorderLayout.CENTER);

	        // Form nhập liệu
	        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
	        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Hồ Sơ Y Tế"));

	        formPanel.add(new JLabel("ID Bệnh Nhân:"));
	        patientIDField = new JTextField();
	        formPanel.add(patientIDField);

	        formPanel.add(new JLabel("Tên Bệnh:"));
	        diseaseField = new JTextField();
	        formPanel.add(diseaseField);

	        formPanel.add(new JLabel("Ngày Lập:"));
	        dateField = new JTextField();
	        formPanel.add(dateField);

	        formPanel.add(new JLabel("Bác sĩ:"));
	        doctorField = new JTextField();
	        formPanel.add(doctorField);
	        searchField = new JTextField(20);
	        formPanel.add(new JLabel("Tìm kiếm:"));
	        formPanel.add(searchField);
	        add(formPanel, BorderLayout.NORTH);

	        // Các nút hành động
	        JPanel buttonPanel = new JPanel(new FlowLayout());
	       
	        JButton btnAddRecord = new JButton("Thêm");
	        JButton btnUpdateRecord = new JButton("Sửa");
	        JButton btnDeleteRecord = new JButton("Xóa");
	        JButton btnSearch = new JButton("Tìm kiếm");
	        buttonPanel.add(btnAddRecord);
	        buttonPanel.add(btnUpdateRecord);
	        buttonPanel.add(btnDeleteRecord);
	        buttonPanel.add(btnSearch);

	        add(buttonPanel, BorderLayout.SOUTH);
	        btnAddRecord.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                addMedicalRecord();
	            }
	        });

	        btnUpdateRecord.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                updateMedicalRecord();
	            }
	        });

	        btnDeleteRecord.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                deleteMedicalRecord();
	            }
	        });
	        btnSearch.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                searchMedicalRecords();
	            }
	        });

	        // Tải dữ liệu ban đầu
	        loadMedicalRecords();
	        recordTable.getSelectionModel().addListSelectionListener(event -> {
	    	    int selectedRow = recordTable.getSelectedRow();
	    	    if (selectedRow != -1) {
	    	    	patientIDField.setText(tableModel.getValueAt(selectedRow, 1).toString());
	    	    	diseaseField.setText(tableModel.getValueAt(selectedRow, 2).toString());
	    	    	dateField.setText(tableModel.getValueAt(selectedRow, 3).toString());
	    	    	doctorField.setText(tableModel.getValueAt(selectedRow, 4).toString());
	    	    }
	    	});
	 }
	   private void addMedicalRecord() {
	        try {
	            int patientID = Integer.parseInt(patientIDField.getText());
				String diseaseName = diseaseField.getText();
	            Date recordDate = java.sql.Date.valueOf(dateField.getText());
	            String doctor = doctorField.getText();
	            MedicalRecord record = new MedicalRecord(0, patientID, diseaseName, recordDate, doctor);
	            if (medicalRecordDAO.addMedicalRecord(record)) {
	                JOptionPane.showMessageDialog(this, "Thêm hồ sơ thành công!");
	                loadMedicalRecords();
	                clearForm();
	            } else {
	                JOptionPane.showMessageDialog(this, "Thêm hồ sơ thất bại.");
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ.");
	        }
	    }

	    private void updateMedicalRecord() {
	        int selectedRow = recordTable.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this, "Vui lòng chọn hồ sơ để sửa.");
	            return;
	        }

	        try {
	            int recordID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
	            int patientID = Integer.parseInt(patientIDField.getText());
	            String diseaseName = diseaseField.getText();
	            Date recordDate = java.sql.Date.valueOf(dateField.getText());
	            String doctor = doctorField.getText();

	            MedicalRecord record = new MedicalRecord(recordID, patientID, diseaseName, recordDate, doctor);
	            if (medicalRecordDAO.updateMedicalRecord(record)) {
	                JOptionPane.showMessageDialog(this, "Cập nhật hồ sơ thành công!");
	                loadMedicalRecords();
	                clearForm();
	            } else {
	                JOptionPane.showMessageDialog(this, "Cập nhật hồ sơ thất bại.");
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(this, "Dữ liệu nhập vào không hợp lệ.");
	        }
	    }

	    private void deleteMedicalRecord() {
	        int selectedRow = recordTable.getSelectedRow();
	        if (selectedRow == -1) {
	            JOptionPane.showMessageDialog(this, "Vui lòng chọn hồ sơ để xóa.");
	            return;
	        }

	        int recordID = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
	        if (medicalRecordDAO.deleteMedicalRecord(recordID)) {
	            JOptionPane.showMessageDialog(this, "Xóa hồ sơ thành công!");
	            loadMedicalRecords();
	            clearForm();
	        } else {
	            JOptionPane.showMessageDialog(this, "Xóa hồ sơ thất bại.");
	        }
	    }
	    private void searchMedicalRecords() {
	        try {
	            String searchText = searchField.getText().trim();
	            if (searchText.isEmpty()) {
	                JOptionPane.showMessageDialog(this, "Vui lòng nhập ID bệnh nhân để tìm kiếm.");
	                return;
	            }

	            int patientID = Integer.parseInt(searchText);
	            List<MedicalRecord> records = medicalRecordDAO.getMedicalRecordsByPatientID(patientID);

	            tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
	            if (records.isEmpty()) {
	                JOptionPane.showMessageDialog(this, "Không tìm thấy hồ sơ cho ID bệnh nhân: " + patientID);
	            } else {
	                for (MedicalRecord record : records) {
	                    tableModel.addRow(new Object[]{
	                        record.getRecordID(),
	                        record.getPatientID(),
	                        record.getDiseaseName(),
	                        record.getRecordDate(),
	                        record.getDoctor()
	                    });
	                }
	            }
	        } catch (NumberFormatException e) {
	            JOptionPane.showMessageDialog(this, "ID bệnh nhân phải là số.");
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi tìm kiếm hồ sơ.");
	            e.printStackTrace();
	        }
	    }

	    private void loadMedicalRecords() {
	        List<MedicalRecord> records = medicalRecordDAO.getAllMedicalRecords();
	        tableModel.setRowCount(0);
	        for (MedicalRecord record : records) {
	            tableModel.addRow(new Object[]{
	                record.getRecordID(),
	                record.getPatientID(),
	                record.getDiseaseName(),
	                record.getRecordDate(),
	                record.getDoctor()
	            });
	        }
	    }
	    private void clearForm() {
	        patientIDField.setText("");;
	        diseaseField.setText("");
	        dateField.setText("");
	        doctorField.setText("");
	    }
}
