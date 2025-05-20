package Login;


import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.Session;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.Transport;
import jakarta.mail.MessagingException;
import javax.swing.*;

import java.sql.SQLException;
import java.util.Properties;

public class EmailService {
    private String verificationCode;
    private long codeExpirationTime;

    public void sendVerificationCode(String email, DatabaseManager dbManager, JFrame parent) {
        try {
            if (!dbManager.emailExists(email)) {
                JOptionPane.showMessageDialog(parent, "Email not found!", "Error", JOptionPane.ERROR_MESSAGE);
                verificationCode = null;
                return;
            }

            verificationCode = generateVerificationCode();
            codeExpirationTime = System.currentTimeMillis() + 10 * 60 * 1000;
            sendVerificationEmail(email, verificationCode);

            JOptionPane.showMessageDialog(parent, "A verification code has been sent to your email!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error during password reset!", "Error", JOptionPane.ERROR_MESSAGE);
            verificationCode = null;
        }
    }

    private String generateVerificationCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append((int) (Math.random() * 10));
        }
        return code.toString();
    }

    private void sendVerificationEmail(String toEmail, String code) {
        String fromEmail = "hoangcao726@gmail.com"; //
        String emailPassword = "vdnx dyuh masi zmqg"; //

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(fromEmail, emailPassword);
            }
        });


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Password Reset Verification Code+"
                    + "");
            message.setText("Your verification code is: " + code + "\nThis code will expire in 10 minutes.");

            Transport.send(message);
            System.out.println("Verification code sent to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error sending email!", "Error", JOptionPane.ERROR_MESSAGE);
            verificationCode = null;
        }
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public long getCodeExpirationTime() {
        return codeExpirationTime;
    }
}