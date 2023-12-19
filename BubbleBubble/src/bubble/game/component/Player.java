package bubble.game.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.service.BackgroundPlayerService;
import bubble.game.state.PlayerWay;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Player extends JLabel implements Moveable {

	private BubbleFrame mContext;
	private List<Bubble> bubbleList;
	// 플레이어를 식별하는 새로운 필드 (1 또는 2)
    private int playerNumber;

	// 위치상태
	private int x;
	private int y;

	// player의 방향
	private PlayerWay playerWay;

	// 움직임 상태
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	// player 속도
	private final int SPEED = 4; // 상수는 대문자로!!
	private final int JUMP = 4; // UP,DOWN스피드

	private int state = 0;

	// 벽충돌상태
	private boolean leftWallCrash;
	private boolean rightWallCrash;

	private ImageIcon player1_R, player1_L, player1_RDie, player1_LDie;
	private ImageIcon player2_R, player2_L, player2_RDie, player2_LDie;

	public Player(BubbleFrame mContext, int playerNumber) {
	    this.mContext = mContext;
	    this.playerNumber = playerNumber;
	    initObject();
	    initSetting();
	    initBackgroundPlayerService();
	    //bubbleList = new ArrayList<>(); 
	}

	private void initObject() {
	    bubbleList = new ArrayList<>();

	    // 이미지 아이콘을 Player 1과 Player 2에 따라 설정
	    if (playerNumber == 1) {
	        player1_R = new ImageIcon("image/player1_R.png");
	        player1_L = new ImageIcon("image/player1_L.png");
	        player1_RDie = new ImageIcon("image/player1_RDie.png");
	        player1_LDie = new ImageIcon("image/player1_LDie.png");
	    } else if (playerNumber == 2) {
	        player1_R = new ImageIcon("image/player2_R.png");
	        player1_L = new ImageIcon("image/player2_L.png");
	        player1_RDie = new ImageIcon("image/player2_RDie.png");
	        player1_LDie = new ImageIcon("image/player2_LDie.png");
	    }
	}

	public void reset(int playerNumber) {
        initSetting();
        this.playerNumber = playerNumber;
    }
	
	private void initSetting() {
        x = 80;
        y = 535;
        left = false;
        right = false;
        up = false;
        down = false;
        leftWallCrash = false;
        rightWallCrash = false;
        playerWay = PlayerWay.RIGHT;
        setIcon(player1_R);
        setSize(50, 50);
        setLocation(x, y);
        state = 0;
    } 

	private void initBackgroundPlayerService() {
		new Thread(new BackgroundPlayerService(this, this)).start();
	}

	@Override
    public void attack() {
        new Thread(() -> {
            Bubble bubble = new Bubble(mContext, playerNumber);
            mContext.add(bubble);
            bubbleList.add(bubble);
            if (playerWay == PlayerWay.LEFT) {
                bubble.left();
            } else {
                bubble.right();
            }
        }).start();
    }
	


	@Override
	public void left() {
		playerWay = PlayerWay.LEFT;

		left = true;
		new Thread(() -> {
			while (left && getState() == 0) {
				setIcon(player1_L);
				x = x - SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void right() {
		playerWay = PlayerWay.RIGHT;

		right = true;
		new Thread(() -> {
			while (right && getState() == 0) {
				setIcon(player1_R);
				x = x + SPEED;
				setLocation(x, y);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	@Override
	public void up() {  // 점프
		up = true;
		new Thread(() -> {
			for (int i = 0; i < 130 / JUMP; i++) {
				y = y - JUMP;
				setLocation(x, y);
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
					try {
						Thread.sleep(3);
					} catch (InterruptedException e) {
						// TODO Auto)-generated catch block
						e.printStackTrace();
					}
				}
				down = false;
			}).start();
		}
	}

	public void die() {
	    new Thread(() -> {
	        setState(1);
	        setIcon(PlayerWay.RIGHT == playerWay ? player1_RDie : player1_LDie);
	        try {
	            if (!isUp() && isDown())
	                up();
	            Thread.sleep(2000);
	            mContext.remove(this);
	            mContext.repaint();
	            mContext.checkGameEnd(); // 수정된 부분: 죽은 후에 checkGameEnd() 호출
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        System.out.println("player 죽음");
	    }).start();
	}

	/*public void die2() {
	    new Thread(() -> {
	        setState(1);
	        setIcon(PlayerWay.RIGHT == playerWay ? player1_RDie : player1_LDie);
	        try {
	            if (!isUp() && isDown())
	                up();
	            Thread.sleep(2000);
	            mContext.remove(this);
	            mContext.repaint();
	            mContext.checkGameEnd(); // 수정된 부분: 죽은 후에 checkGameEnd() 호출
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        System.out.println("player2 죽음");
	    }).start();
	}*/

	
	/*private void showGameOver() { // GameOver 알림창
		JOptionPane.showMessageDialog(
	            labelFor, "Game Over!",
	            getText(), JOptionPane.INFORMATION_MESSAGE);
	    
	    // Exit the program
	    System.exit(0);
        
    }*/
	
	public int getState() {
	    return state;
	}

	public void setState(int state) {
	    this.state = state;
	}

	public boolean isUp() {
	    return up;
	}

	public boolean isDown() {
	    return down;
	}

}