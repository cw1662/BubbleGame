package bubble.game.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import bubble.game.component.Bubble;
import bubble.game.component.Player;

//메인스레드 바쁨 - 키보드 이벤트를 처리하기 바쁘다. 
//background에서 계쏙 관찰함!!
public class BackgroundPlayerService implements Runnable {

    private BufferedImage image;
    private Player player1;
    private Player player2;
    private List<Bubble> bubbleList1;
    private List<Bubble> bubbleList2;

    // player, bubble
    public BackgroundPlayerService(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.bubbleList1 = player1.getBubbleList();
        this.bubbleList2 = player2.getBubbleList();
        try {
            image = ImageIO.read(new File("image/backgroundMapService.png"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
	public void run() {

    	// player1 충돌처리
    	while (player1.getState() == 0) {

			// 1. bubble 충돌 체크
			for (int i = 0; i < bubbleList1.size(); i++) {
				Bubble bubble = bubbleList1.get(i);
				if (bubble.getState() == 1) {

					if (Math.abs(player1.getX() - bubble.getX()) < 10 && Math.abs(player1.getY() - bubble.getY()) > 0
							&& Math.abs(player1.getY() - bubble.getY()) < 50) {
						System.out.println("적군 사살 완료 ");

						bubble.clearBubbled();

						break;
					}
				}
			}

			// 2. 벽 충돌 체크
			// 색상 확인
			Color leftcolor = new Color(image.getRGB(player1.getX() - 10, player1.getY() + 25));
			Color rightcolor = new Color(image.getRGB(player1.getX() + 50 + 15, player1.getY() + 25));

			// -2가 나온다는 뜻은 바닥에 색깔이 없이 흰색이라는 뜻!!
			int bottomcolor = image.getRGB(player1.getX() + 10, player1.getY() + 50 + 5)
					+ image.getRGB(player1.getX() + 50 - 10, player1.getY() + 50 + 5);

			// 바닥 충돌 확인
			if (bottomcolor != -2) {
				// System.out.println("bottom" + bottomcolor);
				// System.out.println("바닥충돌함");
				player1.setDown(false);
			} else { // 오른쪽 왼쪽으로만 이동할 때,,!! (bottomcolor가 -2일 때!)
				if (!player1.isUp() && !player1.isDown()) {
					player1.down();
				}
			}

			// 외벽 충돌 확인
			if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				// System.out.println("왼쪽벽 충돌");
				player1.setLeftWallCrash(true);
				player1.setLeft(false);
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				// System.out.println("오른쪽벽 충돌");
				player1.setRightWallCrash(true);
				player1.setRight(false);
			} else {
				player1.setLeftWallCrash(false);
				player1.setRightWallCrash(false);
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
    	
    	// player2 충돌 처리
    	while (player2.getState() == 0) {

			// 1. bubble 충돌 체크
			for (int i = 0; i < bubbleList2.size(); i++) {
				Bubble bubble = bubbleList2.get(i);
				if (bubble.getState() == 1) {

					if (Math.abs(player2.getX() - bubble.getX()) < 10 && Math.abs(player2.getY() - bubble.getY()) > 0
							&& Math.abs(player2.getY() - bubble.getY()) < 50) {
						System.out.println("적군 사살 완료 ");

						bubble.clearBubbled();

						break;
					}
				}
			}

			// 2. 벽 충돌 체크
			// 색상 확인
			Color leftcolor = new Color(image.getRGB(player2.getX() - 10, player2.getY() + 25));
			Color rightcolor = new Color(image.getRGB(player2.getX() + 50 + 15, player2.getY() + 25));

			// -2가 나온다는 뜻은 바닥에 색깔이 없이 흰색이라는 뜻!!
			int bottomcolor = image.getRGB(player2.getX() + 10, player2.getY() + 50 + 5)
					+ image.getRGB(player2.getX() + 50 - 10, player2.getY() + 50 + 5);

			// 바닥 충돌 확인
			if (bottomcolor != -2) {
				// System.out.println("bottom" + bottomcolor);
				// System.out.println("바닥충돌함");
				player2.setDown(false);
			} else { // 오른쪽 왼쪽으로만 이동할 때,,!! (bottomcolor가 -2일 때!)
				if (!player2.isUp() && !player2.isDown()) {
					player2.down();
				}
			}

			// 외벽 충돌 확인
			if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				// System.out.println("왼쪽벽 충돌");
				player2.setLeftWallCrash(true);
				player2.setLeft(false);
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				// System.out.println("오른쪽벽 충돌");
				player2.setRightWallCrash(true);
				player2.setRight(false);
			} else {
				player2.setLeftWallCrash(false);
				player2.setRightWallCrash(false);
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
