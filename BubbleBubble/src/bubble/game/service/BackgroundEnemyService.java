package bubble.game.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import bubble.game.component.Enemy;

public class BackgroundEnemyService implements Runnable {

	private BufferedImage image;
	private Enemy enemy;
	private int JUMPCOUNT = 0;
	private int FIRST = 0; 

	// player, bubble
	public BackgroundEnemyService(Enemy enemy) {
		this.enemy = enemy;
		try {
			image = ImageIO.read(new File("image/backgroundMapService.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void run() {
		while (enemy.getState() == 0) {

			Color leftcolor = new Color(image.getRGB(enemy.getX() - 10, enemy.getY() + 25));
			Color rightcolor = new Color(image.getRGB(enemy.getX() + 50 + 15, enemy.getY() + 25));

			int bottomcolor = image.getRGB(enemy.getX() + 10, enemy.getY() + 50 + 5)
					+ image.getRGB(enemy.getX() + 50 - 10, enemy.getY() + 50 + 5);

			if (bottomcolor != -2) {
				// System.out.println("bottom" + bottomcolor);
				enemy.setDown(false);
			} else { 
				if (!enemy.isUp() && !enemy.isDown()) {
					enemy.down();
				}
			}

			if (enemy.getY() > 530) {
				FIRST = 1;
			}

			if (enemy.getY() <= 180 && FIRST == 1) {
				JUMPCOUNT = 0;
				FIRST = 0;
			}

			if (JUMPCOUNT < 3 && FIRST == 1 && enemy.getX() > 850 && rightcolor.getRed() == 255
					&& rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				enemy.setRight(false);
				enemy.setLeft(true);
				if (!enemy.isUp() && !enemy.isDown()) {
					JUMPCOUNT++;
					if (JUMPCOUNT == 3)
						enemy.left();
					enemy.up();
				}

			} else if (JUMPCOUNT < 3 && FIRST == 1 && enemy.getX() <= 80 && leftcolor.getRed() == 255
					&& leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				enemy.setLeft(false);
				enemy.setRight(true);
				if (!enemy.isUp() && !enemy.isDown()) {
					JUMPCOUNT++;
					if (JUMPCOUNT == 3)
						enemy.right();
					enemy.up();
				}
			} else if (leftcolor.getRed() == 255 && leftcolor.getGreen() == 0 && leftcolor.getBlue() == 0) {
				enemy.setLeft(false);
				if (!enemy.isRight()) {
					enemy.right();
				}
			} else if (rightcolor.getRed() == 255 && rightcolor.getGreen() == 0 && rightcolor.getBlue() == 0) {
				enemy.setRight(false);
				if (!enemy.isLeft()) {
					enemy.left();
				}
			}
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

}