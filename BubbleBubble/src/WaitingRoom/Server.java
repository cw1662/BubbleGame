package WaitingRoom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.*;
import java.awt.*;

public class Server extends JFrame {
    private JTextArea serverLog;
    private static ArrayList<Socket> users = new ArrayList<>();

    public Server() {
        initUI();
        startServer();
    }

    private void initUI() {
        setTitle("Chat Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        serverLog = new JTextArea();
        serverLog.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(serverLog);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void startServer() {
        int port = 3000;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            appendLog("Server opened on port " + port);

            while (true) {
                Socket user = serverSocket.accept();
                users.add(user);  // Add the connected user to the list
                appendLog("Client connected: " + user.getInetAddress() + " : " + user.getPort());
                new ServerThread(user).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendLog(String message) {
        serverLog.append(message + "\n");
    }

    class ServerThread extends Thread {
        private Socket socket;

        public ServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String s;
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));

                    if ((s = reader.readLine()) != null) {
                        appendLog(s);

                        // 수정된 부분: 모든 클라이언트에게 메시지 전송
                        for (Socket userSocket : users) {
                            if (!userSocket.isClosed()) {
                                OutputStream out = userSocket.getOutputStream();
                                PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out, "UTF-8")), true);
                                writer.println(s);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                // 연결 해제 처리
                users.remove(socket);
                appendLog("클라이언트 연결 해제: " + socket.getInetAddress() + " : " + socket.getPort());
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Server().setVisible(true);
            }
        });
    }
}
