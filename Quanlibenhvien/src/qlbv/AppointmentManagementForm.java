package qlbv;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Appointment;
import model.Doctor;
import model.Patient;
import serveice.AppointmentDAO;
import serveice.DoctorDAO;
import serveice.PatientDAO;

public class AppointmentManagementForm extends JPanel {
    private JTable appointmentTable;
    private DefaultTableModel tableModel;
    private PatientDAO patientDAO = new PatientDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private JComboBox<String> patientDropdown;
    private JComboBox<String> doctorDropdown;
    private JTextField dateField;
    private JComboBox<String> statusDropdown;
    private JTextField searchField;
    private JButton searchButton, btnAddAppointment, btnRefresh, btnEdit, btnDelete;
    private JPanel formPanel;

    public AppointmentManagementForm() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Quản lý lịch hẹn", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        add(createSearchPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm lịch hẹn"));
        searchPanel.add(new JLabel("Ngày (YYYY-MM-DD):"));
        searchField = new JTextField();
        searchPanel.add(searchField);
        searchButton = createStyledButton("Tìm kiếm");
        searchPanel.add(searchButton);
        
        searchButton.addActionListener(e -> searchAppointments());
        
        return searchPanel;
    }

    private Object searchAppointments() {
        return null;
    }

    private JScrollPane createTablePanel() {
        String[] columns = {"ID", "Bệnh nhân", "Bác sĩ", "Ngày giờ", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        appointmentTable = new JTable(tableModel);
        
        return new JScrollPane(appointmentTable);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        btnAddAppointment = createStyledButton("Thêm");
        btnEdit = createStyledButton("Sửa");
        btnDelete = createStyledButton("Xóa");
        btnRefresh = createStyledButton("Làm mới");

        buttonPanel.add(btnAddAppointment);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnRefresh);
        
        btnAddAppointment.addActionListener(e -> showFormPanel("add"));
        btnEdit.addActionListener(e -> showFormPanel("edit"));
        btnDelete.addActionListener(e -> deleteAppointment());
        btnRefresh.addActionListener(e -> loadAppointmentData());

        return buttonPanel;
    }

    private Object deleteAppointment() {
        return null;
    }

    private void showFormPanel(String action) {
        if (formPanel != null) {
            remove(formPanel);
        }
        formPanel = createFormPanel(action);
        add(formPanel, BorderLayout.EAST);
        revalidate();
        repaint();
    }

    private JPanel createFormPanel(String action) {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin lịch hẹn"));

        panel.add(new JLabel("Bệnh nhân:"));
        patientDropdown = new JComboBox<>();
        panel.add(patientDropdown);

        panel.add(new JLabel("Bác sĩ:"));
        doctorDropdown = new JComboBox<>();
        panel.add(doctorDropdown);

        panel.add(new JLabel("Ngày giờ:"));
        dateField = new JTextField();
        panel.add(dateField);

        panel.add(new JLabel("Trạng thái:"));
        statusDropdown = new JComboBox<>(new String[]{"Chờ xác nhận", "Chờ khám", "Đã khám", "Hủy"});
        panel.add(statusDropdown);

        JButton submitButton = createStyledButton("Lưu");
        panel.add(submitButton);
        submitButton.addActionListener(e -> {
            if ("add".equals(action)) {
                addAppointment();
            } else {
                addAppointment();
            }
        });

        return panel;
    }

    private void addAppointment() {
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(Color.BLACK); // Màu nền đen
        button.setForeground(Color.WHITE); // Chữ trắng
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 30, 30)); // Màu đen nhạt hơn khi di chuột
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK); // Quay lại màu đen gốc
            }
        });

        return button;
    }


    private void loadAppointmentData() {
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        List<Appointment> appointments = appointmentDAO.getAllAppointmentsWithDetails();
        tableModel.setRowCount(0);
        for (Appointment appointment : appointments) {
            tableModel.addRow(new Object[]{
                appointment.getAppointmentID(),
                appointment.getPatientName(),
                appointment.getDoctorName(),
                appointment.getAppointmentDate(),
                appointment.getStatus()
            });
        }
    }}
