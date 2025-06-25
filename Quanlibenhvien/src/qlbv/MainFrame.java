package qlbv;

import Chat.ChatForm;
import Chat.ChatServer;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
import javax.swing.*;

public class MainFrame extends JFrame {
    public JPanel contentPanel;
    private Thread serverThread;
    private volatile boolean isServerRunning;
    private ServerSocket serverSocket;

    public MainFrame() {
        setTitle("Quản lý bệnh viện");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);

        URL Iconview = MainFrame.class.getResource("/images/iconlogin.png");
        Image img = Toolkit.getDefaultToolkit().createImage(Iconview);
        this.setIconImage(img);

        add(createHeader(), BorderLayout.NORTH);
        add(createMenu(), BorderLayout.WEST);
        add(createContentPanel(), BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                stopChatServer();
            }
        });

        setVisible(true);
    }

    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 153, 204));

        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ BỆNH VIỆN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createMenu() {
        JPanel menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBackground(new Color(245, 255, 250));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnManagePatients = createMenuButton("Quản lý bệnh nhân", "/images/iconpatient.png");
        JButton btnManageDoctors = createMenuButton("Quản lý bác sĩ", "/images/icondoctor.png");
        JButton btnManageAppointments = createMenuButton("Quản lý lịch hẹn", "/images/iconapm.png");
        JButton btnReports = createMenuButton("Báo cáo", "/images/iconreport.png");
        JButton btnManageRecord = createMenuButton("Quản lý hồ sơ", "/images/iconmedical.png");
        JButton btnCustomerSupport = createMenuButton("Chăm sóc khách hàng", "/images/CSKH.jpg");
        JButton btnPrintInvoice = createMenuButton("In hóa đơn", "/images/iconinvoice.png");

        btnManagePatients.addActionListener(e -> switchContentPanel(new PatientManagementForm()));
        btnManageDoctors.addActionListener(e -> switchContentPanel(new DoctorManagementForm()));
        btnManageAppointments.addActionListener(e -> switchContentPanel(new AppointmentManagementForm()));
        btnReports.addActionListener(e -> switchContentPanel(new ReportForm()));
        btnManageRecord.addActionListener(e -> switchContentPanel(new MedicalRecordManagementForm()));
        btnCustomerSupport.addActionListener(e -> {
            startChatServer();
            switchContentPanel(new ChatForm());
        });
        btnPrintInvoice.addActionListener(e -> switchContentPanel(new InvoicePrintForm()));

        menuPanel.add(btnManagePatients);
        menuPanel.add(btnManageDoctors);
        menuPanel.add(btnManageAppointments);
        menuPanel.add(btnReports);
        menuPanel.add(btnManageRecord);
        menuPanel.add(btnCustomerSupport);
        menuPanel.add(btnPrintInvoice);

        return menuPanel;
    }

    public JPanel createContentPanel() {
        contentPanel = new BackgroundPanel("/images/mainjava.jpg");
        contentPanel.setLayout(new BorderLayout());
        return contentPanel;
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(20);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            Image scaled = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            System.err.println("Không tìm thấy icon: " + iconPath);
        }

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

    private void switchContentPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void startChatServer() {
        synchronized (this) {
            if (!isServerRunning) {
                serverThread = new Thread(() -> {
                    try {
                        serverSocket = new ServerSocket(12345);
                        isServerRunning = true;
                        System.out.println("Chat Server đang chạy...");
                        while (isServerRunning) {
                            try {
                                new ChatServer.ClientHandler(serverSocket.accept()).start();
                            } catch (IOException e) {
                                if (isServerRunning) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (IOException e) {
                        System.err.println("Không thể khởi động ChatServer: " + e.getMessage());
                    }
                });
                serverThread.start();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void stopChatServer() {
        synchronized (this) {
            isServerRunning = false;
            if (serverSocket != null && !serverSocket.isClosed()) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (serverThread != null) {
                serverThread.interrupt();
                serverThread = null;
            }
        }
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
