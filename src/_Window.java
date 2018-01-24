import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class _Window extends JFrame {
	private static final long serialVersionUID = 7258472590040177848L;

	private JTabbedPane tabPane;
	
	public _Window(String title) {
		super(title);
		this.setBackground(Constants.BACKGROUND_COLOR_LIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		tabPane = new JTabbedPane();
		tabPane.setUI(new CustomTabbedPaneUI());
		this.setContentPane(tabPane);
		this.setVisible(true);
		tabPane.setVisible(true);
	}

	public _Window(String title, int width, int height) {
		this(title);
		this.setSize(width, height);
	}
	public void addTab(Tab tab) {
		tabPane.addTab(tab.getTitle(), tab.getInsides());
	}
	public int getSelectedTab() {
		return tabPane.getSelectedIndex();
	}
}
