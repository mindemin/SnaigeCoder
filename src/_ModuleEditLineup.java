

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class _ModuleEditLineup extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 671483031270302461L;
	private JPanel masterPanel;
	private JPanel combinationPanel;
	private PropManager propManager;
	private Lineup lineup;
	private String combinationSelect;
	private _ModuleLineupList lineupList;
	private List<Property> selectedGlobal = new ArrayList<Property>();
	private List<Property> selectedLineup = new ArrayList<Property>();
	public _ModuleEditLineup(Lineup lineup, PropManager propManager, _ModuleLineupList lineupList) {
		super("Edit " + lineup.getName());
		this.setLayout(new GridLayout(0, 2));
		this.setSize(800, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.propManager = propManager;
		this.lineup = lineup;
		this.lineupList = lineupList;
		addComponents();
	}

	private void addComponents() {
		// Master Panel
		masterPanel = new JPanel(new BorderLayout());
		createCards(masterPanel, propManager, "Double click to add", false);
		this.add(masterPanel);
		// Combination Panel
		combinationPanel = new JPanel(new BorderLayout());
		createCards(combinationPanel, lineup, "Double click to remove", true);
		this.add(combinationPanel);
	}
	
	private void createCards(JPanel root, Object combo, String tip, boolean remove) {
		root.removeAll();
		JComboBox<PropType> comboBox = new JComboBox<>();
		JPanel cards = new JPanel(new CardLayout());
		List<PropType> types = (combo instanceof PropManager) ? ((PropManager)combo).getTypes() : propManager.getTypesByProps(((Lineup)combo).getProps());
		types.sort(new Comparator<PropType>() {
			@Override
			public int compare(PropType o1, PropType o2) {
				return o1.compareTo(o2);
			}
		});
		for (PropType type : types) {
			JPanel tempPanel = new JPanel(new GridLayout(0, 1));
			JScrollPane scrollPane = new JScrollPane(tempPanel);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			comboBox.addItem(type);
			List<Property> props = (combo instanceof PropManager) ? ((PropManager)combo).getProps(type.getID()) : propManager.getPropsWithType(((Lineup)combo).getProps(), type.getID());
			
			props.sort(new Comparator<Property>() {

				@Override
				public int compare(Property o1, Property o2) {
					return o1.compareTo(o2);
				}
			});
			for (Property prop : props) {
				JPanel tempWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JLabel tempItem = new JLabel(prop.getCode() + " | " + prop.getDesc("EN"));
				JCheckBox selected = new JCheckBox();
				selected.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(selected.isSelected()) {
							if(remove) selectedLineup.add(prop);
							else selectedGlobal.add(prop);
						} else {
							if(remove) selectedLineup.remove(prop);
							else selectedGlobal.remove(prop);
						}
					}
				});
				tempItem.addMouseListener(new MouseListener() {


					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							combinationSelect = String.valueOf(type.getID());
							if (remove)
								removeFromCombination(prop);
							else
								addToCombination(prop);
							lineupList.updateView();
						}

					}

					@Override
					public void mouseEntered(MouseEvent e) {

					}

					@Override
					public void mouseExited(MouseEvent e) {

					}

					@Override
					public void mousePressed(MouseEvent e) {

					}
					
					@Override
					public void mouseReleased(MouseEvent e) {

					}
				});
				tempWrapper.add(selected);
				tempWrapper.add(tempItem);
				tempPanel.add(tempWrapper);
			}
			cards.add(scrollPane, String.valueOf(type.getID()));
		}
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				System.out.println(((PropType)e.getItem()).getID());
				CardLayout cardLayout = (CardLayout) (cards.getLayout());
				cardLayout.show(cards, String.valueOf(((PropType)e.getItem()).getID()));
			}
		});
		JButton actionButton = new JButton((remove) ? "Remove Selected" : "Add Selected");
		actionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(remove) {
					removeSelected();
				}
				else {
					addSelected();
				}
			}
		});
		
		JButton allButton = new JButton((remove) ? "Remove all" : "Add all");
		allButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(remove) {
					//removeAllItems((PropType)comboBox.getSelectedItem());
				}
				else {
					//addAllItems((PropType)comboBox.getSelectedItem());
				}
			}
		});
		
		root.add(comboBox, BorderLayout.PAGE_START);
		root.add(cards, BorderLayout.CENTER);
		
		JPanel tipAndAction = new JPanel(new GridLayout(1, 2));
		JPanel action = new JPanel(new GridLayout(2, 1));
		tipAndAction.add(new JLabel(tip, JLabel.CENTER));
		tipAndAction.add(action);
		action.add(actionButton);
		action.add(allButton);
		root.add(tipAndAction, BorderLayout.PAGE_END);
		root.revalidate();
		root.repaint();
		if (combinationSelect != "") {
			CardLayout cardLayout = (CardLayout) (cards.getLayout());
			cardLayout.show(cards, combinationSelect);
			if (remove)
				comboBox.setSelectedItem(combinationSelect);
		}
	}

	private void addToCombination(Property prop) {
		if(!lineup.getProps().contains(prop.getID())) lineup.addProp(prop.getID());
		createCards(combinationPanel, lineup, "Double click to add", true);
	}

	private void removeFromCombination(Property prop) {
		lineup.remProp(prop.getID());
		createCards(combinationPanel, lineup, "Double click to remove", true);
	}
	
	private void addSelected() {
		for(Property prop: selectedGlobal)
			if(!lineup.getProps().contains(prop.getID())) lineup.addProp(prop.getID());
		selectedGlobal = new ArrayList<Property>();
		createCards(combinationPanel, lineup, "Double click to add", true);
		lineupList.updateView();
	}
	
	private void removeSelected() {
		for(Property prop: selectedLineup)
			lineup.remProp(prop.getID());
		selectedLineup = new ArrayList<Property>();
		createCards(combinationPanel, lineup, "Double click to remove", true);
		lineupList.updateView();
	}
	
//	private void addAllItems(PropType item) {
//		for(Specification spec : propManager.getSpecificationManager().getSpecsByType(item.getType(), item.getPosition(), item.getCodeLength()))
//			if(!lineup.getSpecs(propManager.getSpecificationManager()).contains(spec)) lineup.addSpec(spec);
//		selectedGlobal = new ArrayList<Property>();
//		createCards(combinationPanel, lineup, "Double click to add", true);
//		lineupList.updateView();
//	}
//	
//	private void removeAllItems(PropType item) {
//		for(Property spec : lineup.(item.getID(), propManager.getSpecificationManager())) {
//			lineup.remove(spec.getID());
//		}
//		selectedLineup = new ArrayList<Property>();
//		createCards(combinationPanel, lineup, "Double click to remove", true);
//		lineupList.updateView();
//	}
}
