package Login;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignupForm extends JFrame {
	  private JTextField newUserField;
	    private JPasswordField newPassField;
	    private JPasswordField confirmPassField;

	    public SignupForm() {
	        this.setTitle("Sign up");
	        this.setSize(400, 300);
	        this.setLayout(null);
	        this.setLocationRelativeTo(null);

	        JLabel registerLabel = new JLabel("Sign up");
	        registerLabel.setFont(new Font("Arial", Font.BOLD, 16));
	        registerLabel.setBounds(150, 10, 100, 30);
	        this.add(registerLabel);

	        JLabel userLabel = new JLabel("New User");
	        userLabel.setBounds(50, 50, 100, 25);
	        this.add(userLabel);

	        newUserField = new JTextField();
	        newUserField.setBounds(150, 50, 200, 25);
	        this.add(newUserField);

	        JLabel passLabel = new JLabel("New Pass");
	        passLabel.setBounds(50, 90, 100, 25);
	        this.add(passLabel);

	        newPassField = new JPasswordField();
	        newPassField.setBounds(150, 90, 200, 25);
	        this.add(newPassField);

	        JLabel confirmPassLabel = new JLabel("Confirm Pass");
	        confirmPassLabel.setBounds(50, 130, 100, 25);
	        this.add(confirmPassLabel);

	        confirmPassField = new JPasswordField();
	        confirmPassField.setBounds(150, 130, 200, 25);
	        this.add(confirmPassField);

	        JButton registerButton = new JButton("Sign up");
	        registerButton.setBounds(150, 180, 100, 25);
	        registerButton.addActionListener(e -> {
	            String newUser = newUserField.getText();
	            String newPass = new String(newPassField.getPassword());
	            String confirmPass = new String(confirmPassField.getPassword());

	            if (newUser.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
	                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
	            } else if (!newPass.equals(confirmPass)) {
	                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
	            }else {
	                logincontroll.registerUser(newUser, newPass);
	                JOptionPane.showMessageDialog(this, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
	                this.dispose();
	            }
	        });
	        this.add(registerButton);

	        this.setVisible(true);
	    }
	
}
