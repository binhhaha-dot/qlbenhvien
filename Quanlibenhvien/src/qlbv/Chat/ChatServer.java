package qlbv.Chat;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static HashSet<PrintWriter> writers = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Chat Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;
        private String name;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                socket.setSoTimeout(60000); // Timeout 60 giây
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // Yêu cầu và lưu tên client
                while (true) {
                    out.println("SUBMITNAME");
                    name = in.readLine();
                    if (name == null) {
                        return;
                    }
                    synchronized (writers) {
                        if (!name.isEmpty()) {
                            break;
                        }
                    }
                }

                out.println("NAMEACCEPTED " + name);
                writers.add(out);

                // Thông báo client mới tham gia
                broadcast(name + " đã tham gia phòng chat!");

                // Nếu là admin, gửi thông báo đặc biệt
                if (name.equalsIgnoreCase("Admin")) {
                    broadcast("Admin đã vào và sẵn sàng hỗ trợ!");
                }

                // Nhận và gửi tin nhắn
                String message;
                while ((message = in.readLine()) != null) {
                    if (!message.isEmpty()) {
                        broadcast(name + ": " + message);
                    }
                }
            } catch (SocketException e) {
                System.out.println("Client " + (name != null ? name : "unknown") + " disconnected: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (name != null) {
                    broadcast(name + " đã rời phòng chat!");
                }
                if (out != null) {
                    writers.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void broadcast(String message) {
            synchronized (writers) {
                for (PrintWriter writer : writers) {
                    writer.println("MESSAGE " + message);
                }
            }
        }
    }
}