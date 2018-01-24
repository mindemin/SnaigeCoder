

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class _ModuleEditDependency extends JFrame{
	private static final long serialVersionUID = -8881077120715987524L;
	private JComboBox<Object> propBox;
	private JPanel depPanel;
	private Lineup lineup;
	private PropManager propManager;
	
	public _ModuleEditDependency(Lineup lineup, PropManager propManager) {
		super("Manage Dependencies");
		this.lineup = lineup;
		this.propManager = propManager;
		setLayout(new BorderLayout());
		setSize(600, 600);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addComponents();
	}
	
	void addComponents() {
		propBox = new JComboBox<Object>();
		propBox.setRenderer(new _ModuleDependencyCell());
		propBox.setModel(new _CustomModel());
		List<PropType> types = propManager.getTypesByProps(lineup.getProps());
		types.sort(new Comparator<PropType>() {

			@Override
			public int compare(PropType o1, PropType o2) {
				return o1.compareTo(o2);
			}
		});
		for(PropType type : types) {
			propBox.addItem(type);
			for(Property prop : propManager.getPropsWithType(lineup.getProps(), type.getID())) {
				propBox.addItem(prop);
			}
		}
		depPanel = new JPanel();
		depPanel.setLayout(new BoxLayout(depPanel, BoxLayout.PAGE_AXIS));
		JScrollPane scrollPane = new JScrollPane(depPanel);
		add(propBox, BorderLayout.PAGE_START);
		add(scrollPane, BorderLayout.CENTER);
		
		// Add actions
		propBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				drawDependencies(lineup.getDependency(((Property)e.getItem()).getID()).getCases());
			}
		});
	}
	
	void drawDependencies(List<List<Case>> dependencies) {
		depPanel.removeAll();
		for(int row = 0; row < dependencies.size(); row++) {
			JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
			List<Case> selectRow = dependencies.get(row);
			for(int col = 0; col < dependencies.get(row).size(); col++) {
				if (!(selectRow.get(col) instanceof Case)) {
					selectRow.remove(col);
				} else {
					Case ca = selectRow.get(col);
					JButton dep = new JButton(propManager.getProp(ca.getPropID()).getCode());
					dep.setToolTipText(propManager.getTypeByProps(ca.getPropID()).getName("EN"));
					dep.setBackground((ca.isNot())? Constants.ALARM_BG : Constants.HIGHLIGHT_COLOR);
					dep.setMinimumSize(new Dimension(50, 28));
					dep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
					int c = col; int r = row;
					dep.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							dependencies.get(r).remove(c);
							drawDependencies(dependencies);
						}
					});
					rowPanel.add(dep);
				}
			}
			JButton or = new JButton("OR");
			or.setMinimumSize(new Dimension(50, 28));
			or.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
			or.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JFrame frame = new JFrame("Select a dependency");
					frame.setSize(500, 120);
					frame.setVisible(true);
					frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					frame.setLayout(new BorderLayout());
					JComboBox<Object> comboBox = new JComboBox<>();
					comboBox.setRenderer(new _ModuleDependencyCell());
					comboBox.setModel(new _CustomModel());
					List<PropType> types = propManager.getTypesByProps(lineup.getProps());
					types.sort(new Comparator<PropType>() {

						@Override
						public int compare(PropType o1, PropType o2) {
							return o1.compareTo(o2);
						}
					});
					for(PropType type : types) {
						comboBox.addItem(type);
						for(Property prop : propManager.getPropsWithType(lineup.getProps(), type.getID())) {
							comboBox.addItem(prop);
						}
					}
					JButton add = new JButton("Add");
					JButton cancel = new JButton("Cancel");
					JPanel panel = new JPanel(new GridLayout(1, 2));
					panel.add(cancel);
					panel.add(add);
					JPanel top = new JPanel(new FlowLayout());
					JCheckBox not = new JCheckBox("Must be not");
					top.add(not);
					top.add(comboBox);
					frame.add(top, BorderLayout.PAGE_START);
					cancel.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							frame.dispose();
						}
					});
					add.addActionListener(new ActionListener() {
						
						@Override
						public void actionPerformed(ActionEvent e) {
							selectRow.add(new Case(((Property)comboBox.getSelectedItem()).getID(), not.isSelected()));
							drawDependencies(dependencies);
						}
					});
					frame.add(panel, BorderLayout.PAGE_END);
				}
			});
			rowPanel.add(or);
			JButton delete = new JButton("X");
			delete.setMinimumSize(new Dimension(28, 28));
			delete.setMaximumSize(new Dimension(28, 28));
			delete.setForeground(Color.RED);
			delete.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					dependencies.remove(selectRow);
					drawDependencies(dependencies);
				}
			});
			rowPanel.add(delete);
			rowPanel.setMinimumSize(new Dimension(600, 30));
			rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			rowPanel.setBackground((row % 2 == 0) ? Constants.LIST_PRIMARY : Constants.LIST_SECONDARY);
			depPanel.add(rowPanel);
		}
		JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 2));
		JButton and = new JButton("AND");
		and.setMinimumSize(new Dimension(50, 28));
		and.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
		and.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dependencies.add(new ArrayList<Case>());
				drawDependencies(dependencies);
			}
		});
		rowPanel.add(and);
		depPanel.add(rowPanel);
		depPanel.revalidate();
		depPanel.repaint();
	}
}
