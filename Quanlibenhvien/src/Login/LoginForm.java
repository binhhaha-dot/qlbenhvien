package Login;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.*;
import Login.logincontroll;
public class LoginForm extends JFrame {
    public JTextField userField;
    public JPasswordField passField;

    public LoginForm() {
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        // Cấu hình JFrame
        this.setTitle("Login");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(350, 300); // Tăng chiều cao để có đủ chỗ cho checkbox
        this.setLayout(null);
        this.setLocationRelativeTo(null);

        // Khởi tạo bộ điều khiển
        logincontroll loginController = new logincontroll(this);

        // Thêm ảnh logo
        JLabel imageLabel = new JLabel();
        imageLabel.setBounds(10, 20, 94, 94);

        URL iconURL = getClass().getResource("iconlogin.png");
        if (iconURL != null) {
            Image img = Toolkit.getDefaultToolkit().createImage(iconURL);
            ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(94, 94, Image.SCALE_SMOOTH));
            imageLabel.setIcon(imageIcon);
        } else {
            System.err.println("Không tìm thấy file iconlogin.png!");
        }
        this.add(imageLabel);

        // Tiêu đề "Login"
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(150, 10, 100, 30);
        this.add(titleLabel);

        // Label và TextField cho Username
        JLabel userLabel = new JLabel("User");
        userLabel.setBounds(100, 50, 80, 25);
        this.add(userLabel);

        userField = new JTextField();
        userField.setBounds(150, 50, 150, 25);
        this.add(userField);

        // Label và PasswordField cho Password
        JLabel passLabel = new JLabel("Pass");
        passLabel.setBounds(100, 90, 80, 25);
        this.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(150, 90, 150, 25);
        passField.setEchoChar('*'); // Ẩn mật khẩu mặc định
        this.add(passField);

        // Checkbox hiển thị mật khẩu
        JCheckBox showPassCheckBox = new JCheckBox();
        showPassCheckBox.setBounds(300, 90, 150, 25);
        this.add(showPassCheckBox);

        // Sự kiện cho checkbox
        showPassCheckBox.addActionListener(e -> {
            if (showPassCheckBox.isSelected()) {
                passField.setEchoChar((char) 0); // Hiển thị mật khẩu
            } else {
                passField.setEchoChar('•'); // Ẩn mật khẩu
            }
        });

        // Nút Login
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 25);
        loginButton.addActionListener(loginController);
        this.add(loginButton);

        // Nút Sign Up
        JButton registerButton = new JButton("Sign up");
        registerButton.setBounds(150, 190, 100, 25);
        registerButton.addActionListener(e -> {
            new SignupForm(); // Đảm bảo rằng SignupForm đã tồn tại
        });
        this.add(registerButton);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
