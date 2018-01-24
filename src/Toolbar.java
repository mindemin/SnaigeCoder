import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;


public class Toolbar extends JPanel{
	private static final long serialVersionUID = 5944201615588397260L;
	
	List<JButton> buttons = new ArrayList<JButton>();
	List<JComponent> components = new ArrayList<JComponent>();
	public Toolbar() {
		super();
		this.setLayout(new FlowLayout(FlowLayout.LEFT));;
		this.buttons = new ArrayList<JButton>();
	}
	public Toolbar(List<JButton> buttons) {
		this();
		this.buttons = buttons;
		for(JButton button : buttons) {
			styleButton(button);
			this.add(button);
		}
	}
	
	public void addButton(JButton button) {
		buttons.add(button);
		components.add(button);
		styleButton(button);
		this.add(button);
	}
	
	public void addComponent(JComponent component) {
		components.add(component);
		styleComponent(component);
		this.add(component);
	}

	private void styleButton(JButton button) {
		button.setBackground(Constants.BACKGROUND_COLOR);
		button.setForeground(Constants.WHITE);
		button.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	}
	
	private void styleComponent(JComponent component) {
		component.setBackground(Constants.BACKGROUND_COLOR);
		component.setForeground(Constants.WHITE);
		component.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	}
}
