package lolthx.baidu.index;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

public class ImageUtils {

	public static BufferedImage getScaledInstance(BufferedImage image, int targetWidth, int targetHeight) {
		int type = (image.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage tmp = new BufferedImage(targetWidth, targetHeight, type);
		Graphics2D g2 = tmp.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.drawImage(image, 0, 0, targetWidth, targetHeight, null);
		g2.dispose();
		return tmp;
	}

	public static BufferedImage reverse(BufferedImage image) {
		BufferedImage tmp = image;
		int minY = tmp.getMinY();
		int height = tmp.getHeight();
		int minX = tmp.getMinX();
		int width = tmp.getWidth();
		for (int y = minY; y < height; y++) {
			for (int x = minX; x < width; x++) {
				int rgb = tmp.getRGB(x, y);
				Color color = new Color(rgb); // 根据rgb的int值分别取得r,g,b颜色。
				Color newColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
				tmp.setRGB(x, y, newColor.getRGB());
			}
		}
		return tmp;
	}

	public static BufferedImage elimination(BufferedImage image, int average) {
		BufferedImage tmp = image;
		int minY = tmp.getMinY();
		int height = tmp.getHeight();
		int minX = tmp.getMinX();
		int width = tmp.getWidth();
		for (int y = minY; y < height; y++) {
			for (int x = minX; x < width; x++) {
				int rgb = tmp.getRGB(x, y);
				Color color = new Color(rgb); // 根据rgb的int值分别取得r,g,b颜色。
				int value = 255 - color.getBlue();
				if (value > average) {
					Color newColor = new Color(0, 0, 0);
					tmp.setRGB(x, y, newColor.getRGB());
				} else {
					Color newColor = new Color(255, 255, 255);
					tmp.setRGB(x, y, newColor.getRGB());
				}
			}
		}
		return tmp;
	}
}
