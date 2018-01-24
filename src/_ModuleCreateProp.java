import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class _ModuleCreateProp extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3557834331307408782L;
	private JLabel labelType;
	private JLabel labelCode;
	private JLabel labelDescription;
	private JLabel labelDescriptionLT;
	private JComboBox<PropType> typeBox;
	private JTextField code;
	private JTextField description;
	private JTextField descriptionLT;
	private JButton create;
	private JButton cancel;
	private List<PropType> types;
	// IF EDITTING MODE
	private boolean edit = false;
	private Property updateProp;
	private _ModulePropList proplistReference;
	private PropManager propManager;
	public _ModuleCreateProp(String title, PropManager propManager, _ModulePropList propList) {
		super(title);
		this.setLayout(new GridLayout(0, 2, 20, 20));
		this.setSize(800, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.propManager = propManager;
		this.types = propManager.getTypes();
		this.proplistReference = propList;
		initFields();
		addFields();
	}
	// for updates
	public _ModuleCreateProp(String title, PropManager propManager, _ModulePropList propList, Property updateProp) {
		super(title);
		this.setLayout(new GridLayout(0, 2, 20, 20));
		this.setSize(800, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.propManager = propManager;
		this.types = propManager.getTypes();
		this.updateProp = updateProp;
		this.proplistReference = propList;
		this.edit = true;
		initFields();
		addFields();
	}

	private void initFields() {
		labelType = new JLabel("Select Type", JLabel.RIGHT);
		labelCode = new JLabel("Code", JLabel.RIGHT);
		labelDescription = new JLabel("Description EN", JLabel.RIGHT);
		labelDescriptionLT = new JLabel("Description LT", JLabel.RIGHT);
		typeBox = new JComboBox<PropType>();
		types.sort(new Comparator<PropType>() {
			@Override
			public int compare(PropType o1, PropType o2) {
				return o1.compareTo(o2);
			}
		});
		
		for (PropType prop : types) {
			typeBox.addItem(prop);
		}
		if (edit)
			typeBox.setSelectedItem(propManager.getType(updateProp.getTypeID()));
		
		code = new JTextField((edit) ? updateProp.getCode() : "");
		description = new JTextField((edit) ? updateProp.getDesc("EN") : "");
		descriptionLT = new JTextField((edit) ? updateProp.getDesc("LT") : "");
		create = new JButton((edit) ? "Update" : "Create");
		cancel = new JButton("Cancel");
		create.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!edit)
					Create();
				else
					Update();
			}
		});
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void addFields() {
		this.add(labelType);
		this.add(typeBox);
		this.add(labelCode);
		this.add(code);
		this.add(labelDescription);
		this.add(description);
		this.add(labelDescriptionLT);
		this.add(descriptionLT);
		this.add(create);
		this.add(cancel);
	}

	private void Create() {
		unerrorField(code);
		if (code.getText().length() != ((PropType) typeBox.getSelectedItem()).getLength()) {
			JOptionPane.showMessageDialog(this, "Code has to be of length " + ((PropType) typeBox.getSelectedItem()).getLength(), "Error", JOptionPane.ERROR_MESSAGE);
			errorField(code);
		} else {
			Property prop = new Property(((PropType) typeBox.getSelectedItem()).getID(), code.getText());
			prop.addDesc("EN", description.getText());
			prop.addDesc("LT", descriptionLT.getText());
			propManager.addProp(prop);
			JOptionPane.showMessageDialog(this, "The value was created", "Success", JOptionPane.PLAIN_MESSAGE);
			proplistReference.updateView();
			resetFields();
		}
	}

	private void Update() {
		unerrorField(code);
		if (code.getText().length() != ((PropType) typeBox.getSelectedItem()).getLength()) {
			errorField(code);
		} else {
			updateProp.setTypeID(((PropType) typeBox.getSelectedItem()).getID());
			updateProp.setCode(code.getText());
			updateProp.addDesc("EN", description.getText());
			updateProp.addDesc("LT", descriptionLT.getText());
			proplistReference.updateView();
			JOptionPane.showMessageDialog(this, "The value was updated", "Success", JOptionPane.PLAIN_MESSAGE);
			dispose();
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
	
	private void resetFields() {
		code.setText("");
		description.setText("");
		descriptionLT.setText("");
	}
}
