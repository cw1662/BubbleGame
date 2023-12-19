package bubble.game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import bubble.game.component.Enemy;
import bubble.game.component.Player;
import bubble.game.music.BGM;
import bubble.game.state.EnemyWay;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BubbleFrame extends JFrame {
	
    private BubbleFrame mContext = this;
    private JLabel backgroundMap;
    private Player player1;
    private Player player2;
    private List<Enemy> enemys;

    public boolean gameRunning = true;
    private int gameLevel = 1;
   
	
    public BubbleFrame() {
        initObject();
        initSetting();
        initListener();
        setVisible(true);
        
        enemys = new ArrayList<>(); // enemys 초기화 위치
        enemys.add(new Enemy(this, EnemyWay.RIGHT));
        enemys.add(new Enemy(this, EnemyWay.LEFT));
        for (Enemy e : enemys)
            add(e);
        new BGM();
        setVisible(true);
    }

    private void initObject() {
        backgroundMap = new JLabel(new ImageIcon("image/backgroundMap.png"));
        setContentPane(backgroundMap);
        player1 = new Player(this, 1);
        player2 = new Player(this, 2);
        add(player1);
        add(player2);
    }

    private void initListener() {  // 키보드 리스너
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) { // Player1
                    case KeyEvent.VK_LEFT:
                        if (!player1.isLeft() && !player1.isLeftWallCrash() && player1.getState() == 0) {
                            player1.left();
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!player1.isRight() && !player1.isRightWallCrash() && player1.getState() == 0) {
                            player1.right();
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (!player1.isUp() && !player1.isDown() && player1.getState() == 0) {
                            player1.up();
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        if (player1.getState() == 0) {
                            player1.attack();
                            checkGameEnd(); // 게임 끝났는지 확인
                            break;
                        }
                }

                switch (e.getKeyCode()) {  // Player2
                    case KeyEvent.VK_A:
                        if (!player2.isLeft() && !player2.isLeftWallCrash() && player2.getState() == 0) {
                            player2.left();
                        }
                        break;
                    case KeyEvent.VK_D:
                        if (!player2.isRight() && !player2.isRightWallCrash() && player2.getState() == 0) {
                            player2.right();
                        }
                        break;
                    case KeyEvent.VK_W:
                        if (!player2.isUp() && !player2.isDown() && player2.getState() == 0) {
                            player2.up();
                        }
                        break;
                    case KeyEvent.VK_F:
                        if (player2.getState() == 0) {
                            player2.attack();
                            checkGameEnd(); // 게임 끝났는지 확인
                            break;
                        }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) { // Player1
                    case KeyEvent.VK_LEFT:
                        player1.setLeft(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        player1.setRight(false);
                        break;
                }

                switch (e.getKeyCode()) {  // Player2
                    case KeyEvent.VK_A:
                        player2.setLeft(false);
                        break;
                    case KeyEvent.VK_D:
                        player2.setRight(false);
                        break;
                }
            }
        });
    }

    private void initSetting() {
        setSize(1000, 640);
        getContentPane().setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void checkGameEnd() {
        boolean player1Defeated = (player1.getState() != 0);
        boolean player2Defeated = (player2.getState() != 0);

        // 두 플레이어가 모두 죽었는지 확인
        if (player1Defeated && player2Defeated) {
            if (gameRunning) {
                System.out.println("두 플레이어 모두 패배 ㅠㅠ ");
                gameOver(); // 두 플레이어가 모두 죽으면 게임 오버 실행
            }
        } else {
            // 적들이 모두 죽었는지 확인하고, 기존 조건에 따라 게임 클리어 로직을 수행
            if (enemys.isEmpty()) {
                if (gameRunning) {
                    // 기존의 게임 클리어 로직은 변경하지 않고 유지
                    System.out.println("모든 적을 처치완료! 게임 클리어!");
                    gameRunning = false;
                    showRestartGameDialog();
                }
            }
        }
    }

    private void resetGame() { // 게임 재시작(초기화)
        gameRunning = true;
        gameLevel = 1;

        enemys.clear();
        for (int i = 0; i < calculateInitialEnemyCount(); i++) {  // enemy 배치 (추가)
            enemys.add(new Enemy(this, EnemyWay.RIGHT));
            enemys.add(new Enemy(this, EnemyWay.LEFT));
        }
        for (Enemy e : enemys)
            add(e);

        player1.reset(1);
        player2.reset(2);

        checkGameEnd();
    }
    
    private void gameOver() { // 게임 오버
        JOptionPane.showMessageDialog(
                this,
                "Game Over!  모든 플레이어 실패 !",
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );
        System.exit(0);
    }


    public void nextLevel() {
        // 다음 레벨로 넘어가기 전에 레벨이 10인지 확인
        if (gameLevel <= 5) {
            System.out.println("Starting Level " + gameLevel + "!");
            
            enemys.clear(); // enemys 초기화
            for (int i = 0; i < calculateInitialEnemyCount(); i++) {
                enemys.add(new Enemy(this, EnemyWay.RIGHT));
                enemys.add(new Enemy(this, EnemyWay.LEFT));
            }
            for (Enemy e : enemys)
                add(e);

            player1.reset(1);   // player1 초기화
            player2.reset(2);   // player2 초기화
            // 새로운 게임 시작 후 gameRunning을 true로 설정
            gameRunning = true;
        } else {
            // 레벨 5일 때 게임 종료
            System.out.println("축하합니다! 모든 레벨 클리어!");
            gameRunning = false;
            checkGameEnd();
        }
    }

    private void showRestartGameDialog() { // 게임 재시작/종료 선택
        Object[] options = {"다음 레벨", "종료"};
        int result = JOptionPane.showOptionDialog(
                this,
                "Game Clear! 현재 레벨: " + gameLevel,
                getTitle(), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == JOptionPane.YES_OPTION) {
            gameRunning = false;  // 레벨 증가 전에 gameRunning을 false로 설정
            gameLevel++;
            nextLevel();
        } else {
            dispose();   // "게임 종료"를 선택한 경우
        }
    }


    private int calculateInitialEnemyCount() {  // 초기 적의 수를 계산
        return gameLevel;
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new BubbleFrame());
    }
}