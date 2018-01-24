import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class _WindowDecode extends Tab {
	private static Decoder decoder;
	private static JPanel results = new JPanel();
	private static JCheckBox isFastDecoding;
	private static JCheckBox isChecking;
	private static JComboBox<String> language;
	public _WindowDecode(Decoder decoder) {
		super("Decoder", createToolbar(), createGUI());
		_WindowDecode.decoder = decoder;
	}

	private static Toolbar createToolbar() {
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
			if(props != null)
				for(Property item : props) {
					results.add(new JLabel(item.getCode()));
				}
			if(!isFastDecoding.isSelected()) {
				JScrollPane images = decoder.getImages(code.getText());
				results.add(images);
			}
			results.revalidate();
			results.repaint();
		}
	}
}
