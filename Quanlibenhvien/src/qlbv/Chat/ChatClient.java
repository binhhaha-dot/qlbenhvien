package qlbv.Chat;

import java.io.*;
import java.net.*;

public class ChatClient {
    private String hostname;
    private int port;
    private String userName;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;
    private ChatInterface chatForm; // Sử dụng interface chung

    public ChatClient(String hostname, int port, ChatInterface chatForm) {
        this.hostname = hostname;
        this.port = port;
        this.chatForm = chatForm; // Không ép kiểu
    }

    public void execute() {
        try {
            socket = new Socket(hostname, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String serverMessage = in.readLine();
                if (serverMessage == null) {
                    chatForm.appendMessage("Mất kết nối với server!");
                    return;
                }
                if (serverMessage.equals("SUBMITNAME")) {
                    out.println(userName);
                } else if (serverMessage.startsWith("NAMEACCEPTED")) {
                    chatForm.setTitle("Chat - " + userName);
                    break;
                }
            }

            new Thread(() -> {
                String message;
                try {
                    while ((message = in.readLine()) != null) {
                        if (message.startsWith("MESSAGE")) {
                            chatForm.appendMessage(message.substring(8));
                        }
                    }
                } catch (SocketException e) {
                    chatForm.appendMessage("Mất kết nối với server: " + e.getMessage());
                } catch (IOException e) {
                    chatForm.appendMessage("Lỗi kết nối: " + e.getMessage());
                }
            }).start();

        } catch (IOException e) {
            chatForm.appendMessage("Không thể kết nối tới server: " + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void close() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}