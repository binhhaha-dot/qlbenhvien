package qlbv;

import qlbv.Chat.ChatForm;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class MainFrame extends JFrame {
	public JPanel contentPanel;
	public MainFrame() {
        // Cấu hình JFrame
        setTitle("Quản lý bệnh viện");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        URL Iconview = MainFrame.class.getResource("iconlogin.png");
	    Image img = Toolkit.getDefaultToolkit().createImage(Iconview);
	    this.setIconImage(img);
        // Thêm các phần giao diện
        add(createHeader(), BorderLayout.NORTH);  // Header
        add(createMenu(), BorderLayout.WEST);    // Menu
        add(createContentPanel(), BorderLayout.CENTER); // Nội dung
       
        setVisible(true);
    }

    // Tạo phần header
	private JPanel createHeader() {
	    JPanel headerPanel = new JPanel(new BorderLayout());
	    headerPanel.setBackground(new Color(0, 153, 204)); // xanh dương đậm

	    JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ BỆNH VIỆN", JLabel.CENTER);
	    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
	    titleLabel.setForeground(Color.white);
	    titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // padding

	    headerPanel.add(titleLabel, BorderLayout.CENTER);
	    return headerPanel;
	}
   
    // Tạo menu (danh sách chức năng bên trái)
    // Trong class MainFrame, sửa phương thức createMenu()
    private JPanel createMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 10, 10)); // Tăng số hàng lên 6
        menuPanel.setBackground(new Color(245, 255, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnManagePatients = createMenuButton("Quản lý bệnh nhân", "iconpatient.png");
        JButton btnManageDoctors = createMenuButton("Quản lý bác sĩ", "icondoctor.png");
        JButton btnManageAppointments = createMenuButton("Quản lý lịch hẹn", "iconapm.png");
        JButton btnReports = createMenuButton("Báo cáo", "iconreport.png");
        JButton btnManageRecord = createMenuButton("Quản lý hồ sơ", "iconmedical.png");
        JButton btnCustomerSupport = createMenuButton("Chăm sóc khách hàng", "CSKH.jpg"); // Nút mới

        btnManagePatients.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(new PatientManagementForm());
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        btnManageDoctors.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(new DoctorManagementForm());
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        btnManageAppointments.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(new AppointmentManagementForm());
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        btnReports.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(new ReportForm());
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        btnManageRecord.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(new MedicalRecordManagementForm());
            contentPanel.revalidate();
            contentPanel.repaint();
        });
        btnCustomerSupport.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(new ChatForm()); // Liên kết với ChatForm
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        menuPanel.add(btnManagePatients);
        menuPanel.add(btnManageDoctors);
        menuPanel.add(btnManageAppointments);
        menuPanel.add(btnReports);
        menuPanel.add(btnManageRecord);
        menuPanel.add(btnCustomerSupport); // Thêm nút vào menu

        return menuPanel;
    }
	public JPanel createContentPanel() {
	    contentPanel = new BackgroundPanel("mainjava.jpg");
	    contentPanel.setLayout(new BorderLayout());
	    return contentPanel;
	}



    // Tạo panel nội dung chính (hiển thị danh sách, form nhập liệu, v.v.)
	private JButton createMenuButton(String text, String iconPath) {
	    JButton button = new JButton(text);
	    button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    button.setBackground(Color.WHITE);
	    button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
	    button.setFocusPainted(false);
	    button.setHorizontalAlignment(SwingConstants.LEFT);
	    button.setIconTextGap(20); // khoảng cách icon và text

	    try {
	        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
	        Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
	        button.setIcon(new ImageIcon(scaled));
	    } catch (Exception e) {
	        System.err.println("Không tìm thấy icon: " + iconPath);
	    }

	    // Hiệu ứng hover
	    button.addMouseListener(new java.awt.event.MouseAdapter() {
	        public void mouseEntered(java.awt.event.MouseEvent evt) {
	            button.setBackground(new Color(230, 255, 250));
	        }

	        public void mouseExited(java.awt.event.MouseEvent evt) {
	            button.setBackground(Color.WHITE);
	        }
	    });

	    return button;
	}


  
    public static void main(String[] args) {
        new MainFrame();
    }
}
