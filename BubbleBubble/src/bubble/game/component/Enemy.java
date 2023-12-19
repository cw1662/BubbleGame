package bubble.game.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.*;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.service.BackgroundEnemyService;
import bubble.game.state.EnemyWay;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Enemy extends JLabel implements Moveable {

	private BubbleFrame bubbleFrame;
    private Player player1;
    private Player player2;

    private int x;
    private int y;

    private EnemyWay enemyWay;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;

    private int state;
    private boolean bubbled = false; // 적의 버블 상태를 추적하기 위한 새로운 필드

    private final int SPEED = 3;
    private final int JUMP = 1;

    private ImageIcon enemyR, enemyL;

    public Enemy(BubbleFrame bubbleFrame, EnemyWay enemyWay) {
        this.bubbleFrame = bubbleFrame;
        this.player1 = bubbleFrame.getPlayer1();
        this.player2 = bubbleFrame.getPlayer2();
        initObject();
        initSetting(enemyWay);
        initBackgroundEnemyService();
    }
   

    private void initObject() {
        enemyR = new ImageIcon("image/enemyR.png");
        enemyL = new ImageIcon("image/enemyL.png");
    }

    private void initSetting(EnemyWay enemyWay) {  // enemy 생성 위치
    	Random random = new Random();
    	
    	x = random.nextInt(900);
        y = 178;
        left = false;
        right = false;
        up = false;
        setSize(50, 50);
        setLocation(x, y);
        state = 0;

        if (EnemyWay.RIGHT == enemyWay) {
            this.enemyWay = EnemyWay.RIGHT;
            setIcon(enemyR);
            right();
        } else {
            this.enemyWay = EnemyWay.LEFT;
            setIcon(enemyL);
            left();
        }
    }
    
    public void reset() {
        // 여기에 적을 다시 시작할 때 필요한 초기화 코드 작성
        initSetting(enemyWay);
        bubbled = false;
    }

    private void initBackgroundEnemyService() {
        new Thread(new BackgroundEnemyService(this)).start();
    }

    
    // Player1
    @Override
    public void left() {
        enemyWay = EnemyWay.LEFT;
        left = true;
        new Thread(() -> {
            while (left) {
                setIcon(enemyL);
                x = x - SPEED;
                setLocation(x, y);
                if (Math.abs(x - player1.getX()) < 50 && Math.abs(y - player1.getY()) < 50) {
                    if (player1.getState() == 0 && getState() == 0)
                        player1.die();
                }
                if (Math.abs(x - player2.getX()) < 50 && Math.abs(y - player2.getY()) < 50) {
                    if (player2.getState() == 0 && getState() == 0)
                        player2.die();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void right() {
        enemyWay = EnemyWay.RIGHT;
        right = true;
        new Thread(() -> {
            while (right) {
                setIcon(enemyR);
                x = x + SPEED;
                setLocation(x, y);
                if (Math.abs(x - player1.getX()) < 50 && Math.abs(y - player1.getY()) < 50) {
                    if (player1.getState() == 0 && getState() == 0)
                        player1.die();
                }
                if (Math.abs(x - player2.getX()) < 50 && Math.abs(y - player2.getY()) < 50) {
                    if (player2.getState() == 0 && getState() == 0)
                        player2.die();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void up() {
        up = true;
        new Thread(() -> {
            for (int i = 0; i < 130 / JUMP; i++) {
                y = y - JUMP;
                setLocation(x, y);
                if (Math.abs(x - player1.getX()) < 50 && Math.abs(y - player1.getY()) < 50) {
                    if (player1.getState() == 0 && getState() == 0)
                        player1.die();
                }
                if (Math.abs(x - player2.getX()) < 50 && Math.abs(y - player2.getY()) < 50) {
                    if (player2.getState() == 0 && getState() == 0)
                        player2.die();
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            up = false;
            down();
        }).start();
    }

    @Override
    public void down() {
        if (down == false) {
            down = true;
            new Thread(() -> {
                while (down) {
                    y = y + JUMP;
                    setLocation(x, y);
                    if (Math.abs(x - player1.getX()) < 50 && Math.abs(y - player1.getY()) < 50) {
                        if (player1.getState() == 0 && getState() == 0)
                            player1.die();
                    }
                    if (Math.abs(x - player2.getX()) < 50 && Math.abs(y - player2.getY()) < 50) {
                        if (player2.getState() == 0 && getState() == 0)
                        	player2.die();
                    }
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                down = false;
            }).start();
        }
    }

    public void die() {
        state = 1;
        bubbleFrame.checkGameEnd();
    }

    
    
    public int getState() { return state; }
    public boolean isUp() {return up; }
    public boolean isDown() { return down; }
}
