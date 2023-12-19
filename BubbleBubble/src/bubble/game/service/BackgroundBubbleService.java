package bubble.game.service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import bubble.game.component.Bubble;

public class BackgroundBubbleService {

	private BufferedImage image;
	private Bubble bubble1;
	private Bubble bubble2;

	public BackgroundBubbleService(Bubble bubble1, Bubble bubble2) {
		this.bubble1 = bubble1;
		this.bubble2 = bubble2;

		try {
			image = ImageIO.read(new File("image/backgroundMapService.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean leftWall() {
        Color leftcolor1 = new Color(image.getRGB(bubble1.getX() - 10, bubble1.getY() + 25));
        Color leftcolor2 = new Color(image.getRGB(bubble2.getX() - 10, bubble2.getY() + 25));
        if (leftcolor1.getRed() == 255 && leftcolor1.getGreen() == 0 && leftcolor1.getBlue() == 0) {
            return true;
        }
        if (leftcolor2.getRed() == 255 && leftcolor2.getGreen() == 0 && leftcolor2.getBlue() == 0) {
            return true;
        }
        return false;
    }

    public boolean rightWall() {
        Color rightcolor1 = new Color(image.getRGB(bubble1.getX() + 50 + 15, bubble1.getY() + 25));
        Color rightcolor2 = new Color(image.getRGB(bubble2.getX() + 50 + 15, bubble2.getY() + 25));
        if (rightcolor1.getRed() == 255 && rightcolor1.getGreen() == 0 && rightcolor1.getBlue() == 0) {
            return true;
        }
        if (rightcolor2.getRed() == 255 && rightcolor2.getGreen() == 0 && rightcolor2.getBlue() == 0) {
            return true;
        }
        return false;
    }

    public boolean topWall() {
        Color topcolor1 = new Color(image.getRGB(bubble1.getX() + 25, bubble1.getY() - 10));
        Color topcolor2 = new Color(image.getRGB(bubble2.getX() + 25, bubble2.getY() - 10));
        if (topcolor1.getRed() == 255 && topcolor1.getGreen() == 0 && topcolor1.getBlue() == 0) {
            return true;
        }
        if (topcolor2.getRed() == 255 && topcolor2.getGreen() == 0 && topcolor2.getBlue() == 0) {
            return true;
        }
        return false;
    }

}
