

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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

public class _ModuleLineupList extends JScrollPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7762697324464537225L;
	private List<Lineup> lineups;
	private JPanel content;
	private PropManager propManager;
	public _ModuleLineupList(PropManager propManager, List<Lineup> lineups) {
		super();
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.setBorder(BorderFactory.createEmptyBorder());
		this.content = new JPanel();
		this.content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
		this.content.setBackground(Constants.LIST_PRIMARY);
		this.setViewportView(content);
		this.propManager = propManager;
		this.lineups = lineups;
		updateView();
		this.setVisible(true);
	}

	public void updateView() {
		content.removeAll();
		for (Lineup combination : lineups) {
			JPanel combPanel = new JPanel(new BorderLayout());
			combPanel.add(getHeader(combination), BorderLayout.PAGE_START);
			combPanel.add(getContent(combination), BorderLayout.CENTER);
			content.add(combPanel);
		}
		content.revalidate();
		content.repaint();
	}

	private JPanel getHeader(Lineup lineup) {
		JPanel header = new JPanel(new BorderLayout());
		JPanel actions = new JPanel(new GridLayout(1, 3));
		JLabel text = new JLabel(lineup.getName());
		JButton remove = new JButton("Remove");
		JButton dependencies = new JButton("Dependencies");
		JButton edit = new JButton("Edit");
		JButton hide = new JButton((lineup.getProps().isEmpty()) ? "Show" : "Hide");
		remove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				remove(lineup);

			}
		});
		dependencies.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new _ModuleEditDependency(lineup, propManager);
			}
		});
		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				edit(lineup);

			}
		});
		hide.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				header.getParent().getComponent(1).setVisible((hide.getText() == "Hide") ? false : true);
				hide.setText((hide.getText() == "Hide") ? "Show" : "Hide");
			}
		});
		header.setBackground(Constants.BACKGROUND_COLOR_DARK);
		header.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Constants.SHADOW_COLOR),
						BorderFactory.createMatteBorder(1, 0, 0, 0, Constants.HIGHLIGHT_COLOR)),
				BorderFactory.createEmptyBorder(0, 15, 0, 15)));
		text.setForeground(Constants.WHITE);
		header.add(text, BorderLayout.CENTER);
		remove.setForeground(Constants.ACTION_DELETE);
		dependencies.setForeground(Constants.WHITE);
		edit.setForeground(Constants.WHITE);
		hide.setForeground(Constants.WHITE);
		remove.setBackground(Constants.BACKGROUND_COLOR_DARK);
		dependencies.setBackground(Constants.BACKGROUND_COLOR_DARK);
		edit.setBackground(Constants.BACKGROUND_COLOR_DARK);
		hide.setBackground(Constants.BACKGROUND_COLOR_DARK);
		remove.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		dependencies.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		edit.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		hide.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 20));
		actions.add(remove);
		actions.add(dependencies);
		actions.add(edit);
		actions.add(hide);
		header.add(actions, BorderLayout.LINE_END);
		return header;
	}

	private JPanel getContent(Lineup lineup) {
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(0, 1));
		List<PropType> types = propManager.getTypesByProps(lineup.getProps());
		types.sort(new Comparator<PropType>() {
			@Override
			public int compare(PropType o1, PropType o2) {
				return o1.compareTo(o2);
			}
		});
		for (PropType type : types) {
			JLabel descLabel = new JLabel(type.getName("EN"));
			descLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0, 0, 0, 20)), BorderFactory.createMatteBorder(1, 0, 0, 0, Constants.WHITE)), BorderFactory.createEmptyBorder(0, 15, 0, 15)));
			content.add(descLabel);
			List<Property> props = propManager.getProps(lineup.getProps());
			for (int i = 0; i < props.size(); i++) {
				Property prop = props.get(i);
				if(prop.getTypeID() == type.getID()) {
					JPanel specPanel = new JPanel(new GridLayout(1, 3));
					specPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 10));
					setFixedSize(specPanel, specPanel.getWidth(), 24);
					specPanel.setBackground((i % 2 == 0) ? Constants.LIST_PRIMARY : Constants.LIST_SECONDARY);
					specPanel.add(new JLabel(prop.getCode()));
					specPanel.add(new JLabel(prop.getDesc("LT")));
					specPanel.add(new JLabel(prop.getDesc("EN")));
					content.add(specPanel);
				}
			}
		}
		content.setVisible((lineup.getProps().isEmpty()) ? false : true);
		return content;
	}

	private void remove(Lineup lineup) {
		lineups.remove(lineup);
		updateView();
	}
	
	private void edit(Lineup lineup) {
		new _ModuleEditLineup(lineup, propManager, this);
	}

	public void save() {
		FileManager.saveObject("data/combinations.dat", this.lineups);
	}

	private void setFixedSize(Component component, int width, int height) {
		Dimension dimension = new Dimension(width, height);
		component.setMinimumSize(dimension);
		component.setPreferredSize(dimension);
		;
		component.setMaximumSize(dimension);
	}

}
