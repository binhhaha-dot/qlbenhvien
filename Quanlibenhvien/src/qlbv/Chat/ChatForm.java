package qlbv.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatForm extends JPanel implements ChatInterface {
    protected JTextArea chatArea;
    protected JTextField messageField;
    protected ChatClient client;

    public ChatForm() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 255, 250));

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton sendButton = new JButton("Gửi");
        sendButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sendButton.setBackground(new Color(0, 153, 204));
        sendButton.setForeground(Color.WHITE);

        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        String userName = JOptionPane.showInputDialog(this, "Nhập tên của bạn:", "Đăng nhập chat", JOptionPane.PLAIN_MESSAGE);
        if (userName == null || userName.trim().isEmpty()) {
            userName = "Khách";
        }

        client = new ChatClient("localhost", 12345, this);
        client.setUserName(userName);
        client.execute();

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        addWindowListener();
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessage(message);
            messageField.setText("");
        }
    }

    @Override
    public void appendMessage(String message) {
        chatArea.append(message + "\n");
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    @Override
    public void setTitle(String s) {
        // Không cần thiết trong JPanel, có thể bỏ qua hoặc triển khai nếu cần
    }

    private void addWindowListener() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (client != null) {
                        client.close();
                    }
                }
            });
        }
    }
}