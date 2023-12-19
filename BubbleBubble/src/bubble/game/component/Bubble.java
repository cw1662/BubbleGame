package bubble.game.component;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bubble.game.BubbleFrame;
import bubble.game.Moveable;
import bubble.game.service.BackgroundBubbleService;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bubble extends JLabel implements Moveable {

	private Player player1;
	private Player player2;
	private BackgroundBubbleService backgroundBubbleService;
	private BubbleFrame mContext;
	private List<Enemy> enemys;
	private Enemy removeEnemy = null;
	
	

	private int x;
	private int y;

	private boolean left;
	private boolean right;
	private boolean up;

	private int state; 
	private ImageIcon bubble1; 
	private ImageIcon bubble2; 
	private ImageIcon bubbled1; 
	private ImageIcon bubbled2;
	private ImageIcon bomb; 

	public Bubble(BubbleFrame mContext, int playerNumber) {
		this.mContext = mContext;
		this.enemys = mContext.getEnemys();
		
		initObject(playerNumber);
		initSetting();
	}

	private void initObject(int playerNumber) {
		bubble1 = new ImageIcon("image/bubble1.png");
		bubble2 = new ImageIcon("image/bubble2.png");
		bubbled1 = new ImageIcon("image/bubbled1.png");
		bubbled2 = new ImageIcon("image/bubbled2.png");
		bomb = new ImageIcon("image/bomb.png");

		backgroundBubbleService = new BackgroundBubbleService(this, this);
		
		// Determine which player this bubble belongs to
        if (playerNumber == 1) {
            this.player1 = mContext.getPlayer1();
        } else if (playerNumber == 2) {
            this.player2 = mContext.getPlayer2();
        }
    }

	private void initSetting() {
	    up = false;
	    left = false;
	    right = false;

	    if (player1 != null) {
	        x = player1.getX();
	        y = player1.getY();
	    } else if (player2 != null) {
	        x = player2.getX();
	        y = player2.getY();
	    } else {
	        throw new IllegalStateException("Player is not initialized");
	    }

	    setIcon(player1 != null ? bubble1 : bubble2);
	    setSize(50, 50);

	    state = 0;
	}


	@Override
	public void left() {
		left = true;
		for (int i = 0; i < 400; i++) {
			x--;
			setLocation(x, y);

			if (backgroundBubbleService.leftWall()) {
				left = false;
				break;
			}

			// left()와 right() 메서드 내 충돌 검사 부분 수정
			for (Enemy e : enemys) {
			    if (!e.isBubbled() && Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
			        if (e.getState() == 0) {
			            attack(e);
			            break;
			        }
			    }
			}


			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void right() {
		right = true;
		for (int i = 0; i < 400; i++) {
			x++;
			setLocation(x, y);

			if (backgroundBubbleService.rightWall()) {
				right = false;
				break;
			}

			// left()와 right() 메서드 내 충돌 검사 부분 수정
			for (Enemy e : enemys) {
			    if (!e.isBubbled() && Math.abs(x - e.getX()) < 10 && Math.abs(y - e.getY()) > 0 && Math.abs(y - e.getY()) < 50) {
			        if (e.getState() == 0) {
			            attack(e);
			            break;
			        }
			    }
			}


			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		up();
	}

	@Override
	public void up() {
		up = true;
		while (up) {
			y--;
			setLocation(x, y);

			if (backgroundBubbleService.topWall()) {
				up = false;
				break;
			}

			try {
				if (state == 0) { 
					Thread.sleep(1);
				} else { 
					Thread.sleep(10);
				}

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (state == 0) {
			clearBubble();
		}
	}

	@Override
	public void attack(Enemy e) {
	    state = 1;
	    e.setState(1);
	    e.setBubbled(true); // 해당 적의 버블 상태를 true로 설정
	    setIcon(player1 != null ? bubbled1 : bubbled2);
	    removeEnemy = e;
	    mContext.remove(e);
	    mContext.repaint();
	}


	
	private void clearBubble() {
	    try {
	        Thread.sleep(3000);
	        setIcon(bomb);
	        Thread.sleep(2000);
	    
	        mContext.getPlayer1().getBubbleList().remove(this);
	        mContext.getPlayer2().getBubbleList().remove(this);
	        mContext.remove(this); 
	        mContext.repaint(); 
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	public void clearBubbled() {
		new Thread(() -> {
			System.out.println("clearBubbled");
			try {
				up = false;
				setIcon(bomb);
				Thread.sleep(1000);
			
				mContext.getPlayer1().getBubbleList().remove(this);
				mContext.getPlayer2().getBubbleList().remove(this);
				mContext.getEnemys().remove(removeEnemy);
				mContext.remove(this);
				mContext.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
}