package Login;

import java.awt.event.ActionEvent;



import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import qlbv.MainFrame;

public class logincontroll implements ActionListener{
	 LoginForm loginForm;
	 private static Map<String, String> userDatabase = new HashMap<>();
	public logincontroll(LoginForm loginForm) {
			this.loginForm = loginForm;
	}
	 public void openProgram() {
		 MainFrame mf = new MainFrame();
	    }
	 public static void registerUser(String username, String password) {
	        userDatabase.put(username, password);
	    }

	public void actionPerformed(ActionEvent e) {
		String user = this.loginForm.userField.getText();
		String pass = new String(this.loginForm.passField.getPassword());
		  if (userDatabase.containsKey(user) && userDatabase.get(user).equals(pass)) {
			  JOptionPane.showMessageDialog(loginForm, "Login thành công!");
			  loginForm.dispose();
			  this.openProgram();
			  
          } else {
              JOptionPane.showMessageDialog(loginForm, "Sai thông tin đăng nhập!", "Error", JOptionPane.ERROR_MESSAGE);
          }
	}

}
