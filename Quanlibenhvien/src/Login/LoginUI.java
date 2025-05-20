package Login;

import javax.swing.*;

import qlbv.MainFrame;

import java.awt.*;
import java.awt.event.*;

public class LoginUI extends JFrame {
    private JTextField usernameField, emailField;
    private JPasswordField passwordField;
    private DatabaseManager dbManager;
    private EmailService emailService;

    public LoginUI() {
        dbManager = new DatabaseManager();
        emailService = new EmailService();

        setTitle("Login & Register");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Register Tab
        JPanel registerPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        registerPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        registerPanel.add(usernameField);

        registerPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        registerPanel.add(emailField);

        registerPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        registerPanel.add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        registerPanel.add(registerButton);

        // Login Tab
        JPanel loginPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField loginUsernameField = new JTextField();
        JPasswordField loginPasswordField = new JPasswordField();

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(loginUsernameField);

        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(loginPasswordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = loginUsernameField.getText();
            String password = new String(loginPasswordField.getPassword());
            if (dbManager.loginUser(username, password, LoginUI.this)) {
                dispose(); // Đóng LoginUI
                new MainFrame().setVisible(true); // Mở MainFrame
            }
        });
        loginPanel.add(loginButton);

        JButton forgotPasswordButton = new JButton("Forgot Password");
        forgotPasswordButton.addActionListener(e -> showForgotPasswordDialog());
        loginPanel.add(forgotPasswordButton);

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
        new LoginUI();
    }
}
