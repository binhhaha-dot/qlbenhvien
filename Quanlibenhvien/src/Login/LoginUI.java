package Login;

import qlbv.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class LoginUI extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private DatabaseManager dbManager;
    private EmailService emailService;

    public LoginUI() {
        dbManager = new DatabaseManager();
        emailService = new EmailService();

        setTitle("Login & Register");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Thêm biểu tượng cửa sổ
        try {
            URL iconURL = getClass().getResource("/Login/iconlogin.png");
            if (iconURL == null) {
                throw new Exception("Không tìm thấy iconlogin.png trong thư mục resources/Login");
            }
            Image icon = new ImageIcon(iconURL).getImage();
            setIconImage(icon);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải biểu tượng cửa sổ: " + e.getMessage());
        }

        JTabbedPane tabbedPane = new JTabbedPane();

        // Register Tab
        JPanel registerPanel = new JPanel(new BorderLayout(10, 10));

        // Hình ảnh bên trái
        JPanel imagePanel = new JPanel();
        try {
            URL logoURL = getClass().getResource("/Login/iconlogin.png");
            if (logoURL == null) {
                throw new Exception("Không tìm thấy iconlogin.png trong thư mục resources/Login");
            }
            ImageIcon logoIcon = new ImageIcon(logoURL);
            Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            imagePanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải logo trong tab Register: " + e.getMessage());
            imagePanel.add(new JLabel("Không thể hiển thị hình ảnh"));
        }
        registerPanel.add(imagePanel, BorderLayout.WEST);

        // Panel nhập liệu bên phải
        JPanel inputPanel = new JPanel(new BorderLayout(10, 10));
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fieldsPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        fieldsPanel.add(usernameField);

        fieldsPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        fieldsPanel.add(emailField);

        fieldsPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        fieldsPanel.add(passwordField);

        inputPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Nút Register ở dưới
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(registerButton);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        registerPanel.add(inputPanel, BorderLayout.CENTER);

        // Login Tab
        JPanel loginPanel = new JPanel(new BorderLayout(10, 10));

        // Hình ảnh bên trái
        JPanel loginImagePanel = new JPanel();
        try {
            URL logoURL = getClass().getResource("/Login/iconlogin.png");
            if (logoURL == null) {
                throw new Exception("Không tìm thấy iconlogin.png trong thư mục resources/Login");
            }
            ImageIcon logoIcon = new ImageIcon(logoURL);
            Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            loginImagePanel.add(logoLabel);
        } catch (Exception e) {
            System.err.println("Lỗi khi tải logo trong tab Login: " + e.getMessage());
            loginImagePanel.add(new JLabel("Không thể hiển thị hình ảnh"));
        }
        loginPanel.add(loginImagePanel, BorderLayout.WEST);

        // Panel nhập liệu bên phải
        JPanel loginInputPanel = new JPanel(new BorderLayout(10, 10));
        JPanel loginFieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField loginUsernameField = new JTextField();
        JPasswordField loginPasswordField = new JPasswordField();

        loginFieldsPanel.add(new JLabel("Username:"));
        loginFieldsPanel.add(loginUsernameField);

        loginFieldsPanel.add(new JLabel("Password:"));
        loginFieldsPanel.add(loginPasswordField);

        loginInputPanel.add(loginFieldsPanel, BorderLayout.CENTER);

        // Nút Login và Forgot Password ở dưới
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = loginUsernameField.getText();
            String password = new String(loginPasswordField.getPassword());
            if (dbManager.loginUser(username, password, LoginUI.this)) {
                dispose();
                new MainFrame().setVisible(true);
            }
        });
        loginButtonPanel.add(loginButton);

        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(e -> showForgotPasswordDialog());
        loginButtonPanel.add(forgotPasswordButton);

        loginInputPanel.add(loginButtonPanel, BorderLayout.SOUTH);

        loginPanel.add(loginInputPanel, BorderLayout.CENTER);

        tabbedPane.addTab("Register", registerPanel);
        tabbedPane.addTab("Login", loginPanel);

        add(tabbedPane);
        setVisible(true);
    }

    private void registerUser() {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!email.endsWith("@gmail.com")) {
            JOptionPane.showMessageDialog(this, "Email must be a Gmail address!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters, contain uppercase, number, and special character!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        dbManager.registerUser(username, email, password, this);
    }

    private void showForgotPasswordDialog() {
        JDialog forgotPasswordDialog = new JDialog(this, "Forgot Password", true);
        forgotPasswordDialog.setSize(300, 150);
        forgotPasswordDialog.setLocationRelativeTo(this);
        forgotPasswordDialog.setLayout(new GridLayout(3, 2, 10, 10));

        forgotPasswordDialog.add(new JLabel("Enter your email:"));
        JTextField emailInput = new JTextField();
        forgotPasswordDialog.add(emailInput);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String email = emailInput.getText();
            if (email.isEmpty()) {
                JOptionPane.showMessageDialog(forgotPasswordDialog, "Please enter your email!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            emailService.sendVerificationCode(email, dbManager, this);
            forgotPasswordDialog.dispose();
            if (emailService.getVerificationCode() != null) {
                showVerificationCodeDialog(email);
            }
        });
        forgotPasswordDialog.add(submitButton);

        forgotPasswordDialog.setVisible(true);
    }

    private void showVerificationCodeDialog(String email) {
        JDialog codeDialog = new JDialog(this, "Enter Verification Code", true);
        codeDialog.setSize(300, 150);
        codeDialog.setLocationRelativeTo(this);
        codeDialog.setLayout(new GridLayout(3, 2, 10, 10));

        codeDialog.add(new JLabel("Enter the 6-digit code:"));
        JTextField codeInput = new JTextField();
        codeDialog.add(codeInput);

        JButton verifyButton = new JButton("Verify");
        verifyButton.addActionListener(e -> {
            String enteredCode = codeInput.getText();
            if (System.currentTimeMillis() > emailService.getCodeExpirationTime()) {
                JOptionPane.showMessageDialog(codeDialog, "Verification code has expired!", "Error", JOptionPane.ERROR_MESSAGE);
                codeDialog.dispose();
                return;
            }
            if (enteredCode.equals(emailService.getVerificationCode())) {
                codeDialog.dispose();
                showResetPasswordDialog(email);
            } else {
                JOptionPane.showMessageDialog(codeDialog, "Invalid code!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        codeDialog.add(verifyButton);

        codeDialog.setVisible(true);
    }

    private void showResetPasswordDialog(String email) {
        JDialog resetPasswordDialog = new JDialog(this, "Reset Password", true);
        resetPasswordDialog.setSize(300, 200);
        resetPasswordDialog.setLocationRelativeTo(this);
        resetPasswordDialog.setLayout(new GridLayout(4, 2, 10, 10));

        resetPasswordDialog.add(new JLabel("New Password:"));
        JPasswordField newPasswordField = new JPasswordField();
        resetPasswordDialog.add(newPasswordField);

        resetPasswordDialog.add(new JLabel("Confirm Password:"));
        JPasswordField confirmPasswordField = new JPasswordField();
        resetPasswordDialog.add(confirmPasswordField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(resetPasswordDialog, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")) {
                JOptionPane.showMessageDialog(resetPasswordDialog, "Password must be at least 6 characters, contain uppercase, number, and special character!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            dbManager.resetPassword(email, newPassword, resetPasswordDialog);
        });
        resetPasswordDialog.add(submitButton);

        resetPasswordDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}