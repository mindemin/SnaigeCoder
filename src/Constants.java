import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public final class Constants {
	public static final Color ALARM = new Color(255, 0, 0);
	public static final Color ALARM_BG = new Color(255, 220, 220);
	public static final Color ACTION_PRIMARY = new Color(100, 150, 240);
	public static final Color ACTION_DELETE = new Color(255, 0, 0);
	public static final Color BACKGROUND_COLOR = new Color(9, 49, 69);// new Color(41, 128, 185);
	public static final Color BACKGROUND_COLOR_LIGHT = new Color(13, 61, 86);
	public static final Color BACKGROUND_COLOR_DARK = new Color(6, 32, 45);
	public static final Color BLACK = new Color(10, 20, 30);
	public static final Color DIVIDER_COLOR = new Color(0, 0, 0, 100);
	public static final Color HIGHLIGHT_COLOR = new Color(255, 255, 255, 20);
	public static final Color SHADOW_COLOR = new Color(0, 0, 0, 100);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color WHITE_TRANSPARENT = new Color(255, 255, 255, 150);
	public static final Color TRANSPARENT = new Color(0, 0, 0, 0);
	public static final Color LIST_PRIMARY = new Color(255, 255, 255);
	public static final Color LIST_SECONDARY = new Color(245, 250, 255);
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		try {
			if(newW == 0) {
				newW = (int)((float)img.getWidth() * ((float)newH / (float)img.getHeight()));
			}
		    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	
		    Graphics2D g2d = dimg.createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();
	
		    return dimg;
	    } catch (Exception e) {
	    	return img;
	    }
	}  
}
