package WaitingRoom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {
    private Socket socket;
    private JTextArea chatArea;
    private JTextField inputField;
    private String myName;
    private String opponentName;
    private JLabel opponentLabel;
    private JLabel myNameLabel;

    private boolean playersJoined = false;
    private int playerNum; // Player number: 1 or 2

    public Client() {
        initUI();
        connectToServer();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        /*// opponentLabel을 초기화
        opponentLabel = new JLabel("Wait...");
        add(opponentLabel, BorderLayout.WEST);

        myNameLabel = new JLabel("Wait...");
        add(myNameLabel, BorderLayout.EAST);*/

        JPanel centerPanel = new JPanel(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());

        inputField = new JTextField();

        // ActionListener 정의
        ActionListener sendMessageAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = inputField.getText();
                sendMessage(message);
            }
        };

        // ActionListener 등록
        inputField.addActionListener(sendMessageAction);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(sendMessageAction);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        centerPanel.add(inputPanel, BorderLayout.SOUTH);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendStartCommand();
            }
        });
        add(startButton, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 3000);
            System.out.println("서버에 연결되었습니다.");

            myName = JOptionPane.showInputDialog("Enter your name:");
            // 보낸 입장 메시지를 서버로 전송
            //sendMessage(myName);
            sendMessage(myName + "님이 입장하였습니다.\n");  // 상대방에게 뜨는 메시지

            // Wait for the opponent's name from the server
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            opponentName = reader.readLine();

            // Update the UI with the opponent's name
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateUI();
                }
            });

            new Receiver(socket).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message) {
        try {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out, "UTF-8")), true);
            writer.println(myName + ": " + message);
            inputField.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendStartCommand() {
        if (playersJoined) {
            // Both players have joined, send "start game !"
            sendMessage("===== ! START GAME ! =====");
            // Start the BubbleFrame
            startBubbleFrame();
        }
    }



    private void startBubbleFrame() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new bubble.game.Main().main(null);
            }
        });
    }

    private void updateUI() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Check if both names are available
                if (opponentName != null && myName != null) {
                    if (opponentName.equals(myName)) {
                        playerNum = 1;
                        myNameLabel.setText("My Name: " + myName);
                        chatArea.append("System: " + myName + "님이 입장하였습니다.\n");
                    } else {
                        playerNum = 2;
                        chatArea.append(opponentName + "\n");
                    }
                    playersJoined = true;
                }
            }
        });
    }



    private void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            String[] parts = message.split(": ", 2);

            if (parts.length == 2) {
                String sender = parts[0];
                String content = parts[1];

                // 수정된 부분: 상대방과 내가 보낸 메시지 모두 출력
                chatArea.append(sender + ": " + content + "\n");
            } else {
                chatArea.append(message + "\n");
            }
        });
    }

    class Receiver extends Thread {
        private Socket socket;

        public Receiver(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream input;
                BufferedReader reader;

                while (true) {
                    String s = null;
                    input = socket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(input, "UTF-8")); // Reading
                    if ((s = reader.readLine()) != null) {
                        if (s.equals("/quit")) break;

                        // Check if the server sent a command to start the game
                        if (s.equals("===== ! START GAME ! =====")) {
                            // Start the BubbleFrame
                            startBubbleFrame();
                        } else if (s.startsWith("System: ")) {
                            // Display system messages for both players
                            appendMessage(s);
                        } else {
                            // Regular chat message
                            appendMessage(s);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client().setVisible(true);
            }
        });
    }
}