import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class _ModulePropList extends JPanel {
	private static final long serialVersionUID = -7350767788079808338L;
	
	private PropManager propManager;
	
	private JScrollPane listPane;
	private JPanel content;

	public _ModulePropList(PropManager propManager) {
		super(new BorderLayout());
		this.listPane = new JScrollPane();
		this.listPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.listPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.content = new JPanel();
		this.content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		this.content.setBackground(Constants.LIST_PRIMARY);
		this.listPane.setViewportView(content);
		this.propManager = propManager;
		this.add(listPane, BorderLayout.CENTER);
		this.setVisible(true);
		updateView();
	}

	public void updateView() {
		content.removeAll();
		addLegendToView();
		List<PropType> types = propManager.getTypes();
		types.sort(new Comparator<PropType>() {

			@Override
			public int compare(PropType o1, PropType o2) {
				return o1.compareTo(o2);
			}
		});
		
		for (PropType type : types) {
			System.out.println("Adding type: " + type.getID());
			addTypeToView(type);
			List<Property> props = propManager.getProps(type.getID());
			props.sort(new Comparator<Property>() {

				@Override
				public int compare(Property o1, Property o2) {
					return o1.compareTo(o2);
				}
			});
			for (int i = 0; i < props.size(); i++) {
				System.out.println(" - Adding prop: " + props.get(i).getCode());
				addSpecToView(props.get(i), i);
			}
		}
		content.revalidate();
		content.repaint();
	}

	private void addTypeToView(PropType type) {
		JPanel descPanel = new JPanel(new BorderLayout());
		descPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 20)),
						BorderFactory.createMatteBorder(1, 0, 0, 0, Constants.WHITE)),
				BorderFactory.createEmptyBorder(0, 15, 0, 15)));
		descPanel.add(new JLabel(type.getName("EN")), BorderLayout.LINE_START);
		JButton edit = new JButton("Edit");
		edit.setForeground(Constants.BLACK);
		edit.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		edit.setBackground(new Color(240, 240, 240));
		edit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new _ModuleCreateType("Update type", type);
			}
		});
		JButton delete = new JButton("Delete");
		delete.setForeground(Constants.ACTION_DELETE);
		delete.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		delete.setBackground(new Color(240, 240, 240));
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				propManager.remType(type);
				updateView();
			}
		});
		JPanel actions = new JPanel(new GridLayout(1, 2));
		actions.add(delete);
		actions.add(edit);
		descPanel.add(actions, BorderLayout.LINE_END);
		setFixedSize(descPanel, getWidth(), 24);
		content.add(descPanel);
	}

	private void addLegendToView() {
		JPanel legend = new JPanel(new GridLayout(1, 4));
		legend.add(new JLabel("Code"));
		legend.add(new JLabel("Description LT"));
		legend.add(new JLabel("Description EN"));
		legend.add(new JLabel());
		legend.setBackground(Constants.BACKGROUND_COLOR_DARK);
		legend.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 10));
		for (Component comp : legend.getComponents()) {
			comp.setForeground(Constants.WHITE_TRANSPARENT);
		}
		setFixedSize(legend, getWidth(), 30);
		this.add(legend, BorderLayout.PAGE_START);
	}

	private void addSpecToView(Property prop, int index) {
		JPanel panel = new JPanel(new GridLayout(1, 4));
		panel.add(new JLabel(prop.getCode()));
		panel.add(new JLabel(prop.getDesc("LT")));
		panel.add(new JLabel(prop.getDesc("EN")));
		JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		addActionsToPanel(actions, prop, index);
		panel.add(actions);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 10));
		actions.setBackground((index % 2 == 0) ? Constants.LIST_PRIMARY : Constants.LIST_SECONDARY);
		panel.setBackground((index % 2 == 0) ? Constants.LIST_PRIMARY : Constants.LIST_SECONDARY);
		setFixedSize(panel, getWidth(), 24);
		content.add(panel);
	}

	private void addActionsToPanel(JPanel panel, Property prop, int index) {
		JButton buttonDelete = new JButton("Delete");
		JButton buttonEdit = new JButton("Edit");
		_ModulePropList tempPropList = this;
		buttonEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new _ModuleCreateProp("Update value", propManager, tempPropList, prop);
			}
		});
		buttonDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				propManager.remProp(prop);
				updateView();
			}
		});
		buttonDelete.setForeground(Constants.ACTION_DELETE);
		buttonEdit.setForeground(Constants.ACTION_PRIMARY);
		buttonDelete.setBackground((index % 2 == 0) ? Constants.LIST_PRIMARY : Constants.LIST_SECONDARY);
		buttonEdit.setBackground((index % 2 == 0) ? Constants.LIST_PRIMARY : Constants.LIST_SECONDARY);
		buttonDelete.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
		buttonEdit.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
		panel.add(buttonDelete);
		panel.add(buttonEdit);
	}

	public void addSpec(Property prop) {
		propManager.addProp(prop);
		updateView();
	}

	private void setFixedSize(Component component, int width, int height) {
		Dimension dimension = new Dimension(width, height);
		component.setMinimumSize(dimension);
		component.setPreferredSize(dimension);
		;
		component.setMaximumSize(dimension);
	}


	public JPanel getContent() {
		return content;
	}

}
