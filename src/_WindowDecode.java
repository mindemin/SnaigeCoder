import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

public class _WindowDecode extends Tab {
	private static Decoder decoder;
	private static JPanel results = new JPanel();
	private static JCheckBox isFastDecoding;
	private static JCheckBox isChecking;
	private static JComboBox<String> language;
	private static PropManager propManager;
	
	public _WindowDecode(Decoder decoder, PropManager propManager) {
		super("Decoder", createToolbar(propManager), createGUI());
		_WindowDecode.decoder = decoder;
	}

	private static Toolbar createToolbar(PropManager propManager) {
		setPropManager(propManager);
		Toolbar toolbar = new Toolbar();
		JTextField code = new JTextField(30);
		language = new JComboBox<String>(new String[]{"LT", "EN"});
		isFastDecoding = new JCheckBox("Disable images");
		isFastDecoding.setToolTipText("Check this if faster processing is required");
		isChecking = new JCheckBox("Must exist");
		isChecking.setToolTipText("Check this if the code must exist");
		code.setToolTipText("Enter manufacturer code");
		JButton enter = new JButton("Decode");
		enter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				decode(code);
			}
		});
		code.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == 10) {
					decode(code);
				}
				// DETECT CTRL+Z
				if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					code.setText("");
                }
			}
		});
		toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
		toolbar.addComponent(new JLabel("Language"));
		toolbar.add(language);
		toolbar.add(code);
		toolbar.addButton(enter);
		toolbar.addComponent(isFastDecoding);
		toolbar.addComponent(isChecking);
		return toolbar;
	}
	
	private static JPanel createGUI() {
		results.setLayout(new GridLayout(1, 2));
		return results;
	}
	
	private static void decode(JTextField code) {
		code.setText(code.getText().trim());
		if(!code.getText().isEmpty()) {
			results.removeAll();
			List<Property> props = decoder.decode(code.getText(), language.getSelectedItem().toString(), isChecking.isSelected());
			if(props != null) {
				JPanel description = new JPanel(new GridLayout(0,  1));
				JScrollPane pane = new JScrollPane(description);
				for(Property item : props) {
					JPanel itemContainer = new JPanel(new GridLayout(2,  1));
					JLabel title = new JLabel(propManager.getType(item.getTypeID()).getName(language.getSelectedItem().toString()));
					title.setFont(new Font("Arial", Font.BOLD, 16));
					itemContainer.add(title);
					itemContainer.add(new JLabel(item.getDesc(language.getSelectedItem().toString())));
					itemContainer.setBackground(new Color(240, 240, 240));
					itemContainer.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 20)),
									BorderFactory.createMatteBorder(1, 0, 0, 0, Constants.WHITE)),
							BorderFactory.createEmptyBorder(2, 5, 2, 5)));
					description.add(itemContainer);
				}
				results.add(pane);
			}
			if(!isFastDecoding.isSelected()) {
				JScrollPane images = decoder.getImages(code.getText());
				results.add(images);
			}
			results.revalidate();
			results.repaint();
		}
	}

	public static void setPropManager(PropManager propManager) {
		_WindowDecode.propManager = propManager;
	}
}
