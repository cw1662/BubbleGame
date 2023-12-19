package bubble.game;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class Intro extends JFrame {

    public Intro() {
        setTitle("Bubble Game Intro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 640);
        setLocationRelativeTo(null); // ȭ�� �߾ӿ� ��ġ

        setLayout(new BorderLayout());

        ImageIcon imageIcon = new ImageIcon("image/intro.gif");
        JLabel background = new JLabel(imageIcon);
        add(background);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new BubbleFrame();
                        dispose(); // Close the Intro window after opening BubbleFrame
                    }
                });
            }
        }, 5000); // 5 seconds delay before opening BubbleFrame

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Intro());
    }
}
