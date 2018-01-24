import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class _ModuleCreateType  extends JFrame {
	private static final long serialVersionUID = -9172424725009386521L;
	
	private JLabel labelType;
	private JLabel labelTypeLT;
	private JLabel labelPosition;
	private JLabel labelLength;
	private JTextField type;
	private JTextField typeLT;
	private JTextField position;
	private JTextField length;
	private JButton create;
	private JButton cancel;
	
	private List<PropType> types;
	private PropType updateType;
	private _ModulePropList propReference;
	
	public _ModuleCreateType(String title, List<PropType> types, _ModulePropList propList) {
		super(title);
		this.setLayout(new GridLayout(0, 2, 20, 20));
		this.setSize(800, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.types = types;
		this.propReference = propList;
		initFields(false);
		addFields(false);
	}
	// for updates;
	public _ModuleCreateType(String title, PropType type) {
		super(title);
		this.setLayout(new GridLayout(0, 2, 20, 20));
		this.setSize(800, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.updateType = type;
		initFields(true);
		addFields(true);
	}
	
	private void initFields(boolean edit) {
		labelType = new JLabel("Type (EN)", JLabel.RIGHT);
		labelTypeLT = new JLabel("Type (LT)", JLabel.RIGHT);
		labelPosition = new JLabel("Start Index", JLabel.RIGHT);
		labelLength = new JLabel("Length", JLabel.RIGHT);
		type = new JTextField((edit) ? updateType.getName("EN") : "");
		typeLT = new JTextField((edit) ? updateType.getName("LT") : "");
		position = new JTextField((edit) ? updateType.getStartPos()+"" : "");
		length = new JTextField((edit) ?  updateType.getLength()+"" : "");
		create = new JButton((edit) ? "Update" : "Create");
		cancel = new JButton("Cancel");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(edit) Update();
				else Create();
			}
		});
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	private void addFields(boolean edit) {
		this.add(labelTypeLT);
		this.add(typeLT);
		this.add(labelType);
		this.add(type);
		this.add(labelPosition);
		this.add(position);
		this.add(labelLength);
		this.add(length);
		this.add(create);
		this.add(cancel);
	}
	
	private void Create() {
		int pos = -1, len = -1;
		unerrorField(position);
		unerrorField(length);
		try {
			pos = Integer.parseInt(position.getText());
		} catch (Exception e) {
			errorField(position);
		}
		try {
			len = Integer.parseInt(length.getText());
		} catch (Exception e) {
			errorField(length);
		}
		if(pos >= 0 && len > 0) {
			Map<String, String> desc = new HashMap<String, String>();
			desc.put("EN", type.getText());
			desc.put("LT", typeLT.getText());
			PropType type = new PropType(Integer.parseInt(position.getText()), Integer.parseInt(length.getText()), desc);
			types.add(type);
			propReference.updateView();
			dispose();
		} else {
			if (len <= 0) {
				errorField(length);
			}
			if (pos < 0) {
				errorField(position);
			}
		}
		
	}
	
	public void Update() {
		int pos = -1, len = -1;
		unerrorField(position);
		unerrorField(length);
		try {
			pos = Integer.parseInt(position.getText());
		} catch (Exception e) {
			errorField(position);
		}
		try {
			len = Integer.parseInt(length.getText());
		} catch (Exception e) {
			errorField(length);
		}
		if(pos >= 0 && len > 0) {
			updateType.addName("EN", type.getText());
			updateType.addName("LT", typeLT.getText());
			updateType.setStartPos(Integer.parseInt(position.getText()));
			updateType.setLength(Integer.parseInt(length.getText()));
			propReference.updateView();
			dispose();
		} else {
			if (len <= 0) {
				errorField(length);
			}
			if (pos < 0) {
				errorField(position);
			}
		}
	}
	
	private void errorField(JComponent field) {
		field.setBorder(BorderFactory.createLineBorder(Constants.ALARM, 1));
		field.setBackground(Constants.ALARM_BG);
	}
	
	private void unerrorField(JComponent field) {
		field.setBorder(BorderFactory.createLineBorder(Constants.BLACK, 1));
		field.setBackground(Color.WHITE);
	}
}

