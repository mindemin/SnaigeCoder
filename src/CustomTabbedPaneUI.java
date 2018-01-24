import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class CustomTabbedPaneUI extends BasicTabbedPaneUI {

	private Color selectColor;
	private Color deSelectColor;
	private int inclTab = 4;
	private int anchoCarpetas = 18;
	private Polygon shape;

	public static ComponentUI createUI(JComponent c) {
		return new CustomTabbedPaneUI();
	}

	@Override
	protected void installDefaults() {
		super.installDefaults();
		selectColor = Constants.BACKGROUND_COLOR;
		deSelectColor = Constants.BACKGROUND_COLOR_LIGHT;
		tabAreaInsets.right = anchoCarpetas;
	}

	@Override
	protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
		Graphics2D g2D = (Graphics2D) g;
		int xp[] = null;
		int yp[] = null;
		switch (tabPlacement) {
		case LEFT:
			xp = new int[] { x, x, x + w, x + w, x };
			yp = new int[] { y, y + h - 3, y + h - 3, y, y };
			break;
		case RIGHT:
			xp = new int[] { x, x, x + w - 2, x + w - 2, x };
			yp = new int[] { y, y + h - 3, y + h - 3, y, y };
			break;
		case BOTTOM:
			xp = new int[] { x, x, x + 3, x + w - inclTab - 6, x + w - inclTab - 2, x + w - inclTab, x + w - 3, x };
			yp = new int[] { y, y + h - 3, y + h, y + h, y + h - 1, y + h - 3, y, y };
			break;
		case TOP:
		default:
			xp = new int[] { x, x, x + 3, x + w - inclTab - 6, x + w - inclTab - 2, x + w - inclTab, x + w - inclTab, x };
			yp = new int[] { y + h, y + 3, y, y, y + 1, y + 3, y + h, y + h };
			break;
		}
		shape = new Polygon(xp, yp, xp.length);
		if (isSelected) {
			g2D.setColor(selectColor);
			g2D.setPaint(selectColor);
		} else {
			if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
				g2D.setColor(deSelectColor);
				g2D.setPaint(deSelectColor);
			} else {
				g2D.setPaint(deSelectColor);
			}
		}
		g2D.fill(shape);
		if (runCount > 1) {
			g2D.setColor(hasAlpha(getRunForTab(tabPane.getTabCount(), tabIndex) - 1));
			g2D.fill(shape);
		}
		g2D.fill(shape);
	}

	@Override
	protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
	}

		protected Color hasAlpha(int fila) {
			int alfa = 0;
			if (fila >= 0) {
				alfa = 50 + (fila > 7 ? 70 : 10 * fila);
			}
		return new Color(0, 0, 0, alfa);
	}
}
