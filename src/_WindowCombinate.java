

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class _WindowCombinate extends Tab {
	private static JPanel content = new JPanel(new GridLayout(1, 2));
	private static JPanel combinationPanel = new JPanel(new GridLayout(1, 2));
	private static JPanel resultPanel = new JPanel(new BorderLayout());
	private static List<JComboBox<Property>> propBoxes = new ArrayList<JComboBox<Property>>();
	private static Decoder decoder;
	private static JComboBox<String> language;
	private static Set<JComboBox<Property>> influential = new HashSet<JComboBox<Property>>();
	private static boolean validCombination = true;
	private static PropManager propManager;
	
	public _WindowCombinate(List<Lineup> lineups, PropManager propManager, Decoder decoder) {
		super("Combinator", createToolbar(lineups, propManager), createContent(lineups));
		setPropManager(propManager);
		_WindowCombinate.decoder = decoder;
	}

	private static void setPropManager(PropManager propManager) {
		_WindowCombinate.propManager = propManager;
	}

	private static Toolbar createToolbar(List<Lineup> lineups, PropManager propManager) {
		setPropManager(propManager);
		language = new JComboBox<String>(new String[]{"LT", "EN"});
		Toolbar toolbar = new Toolbar();
		toolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
		JComboBox<Lineup> comboBox = new JComboBox<Lineup>();
		comboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateContent((Lineup)comboBox.getSelectedItem(), propManager);				
			}
		});
		comboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateContent((Lineup)e.getItem(), propManager);
			}
		});
		language.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				updateContent((Lineup)comboBox.getSelectedItem(), propManager);
			}
		});
		for(Lineup lineup : lineups)
			comboBox.addItem(lineup);
		toolbar.addComponent(new JLabel("Language"));
		toolbar.add(language);
		toolbar.addComponent(new JLabel("Choose your model"));
		toolbar.add(comboBox);
		return toolbar;
	}
	
	private static JPanel createContent(List<Lineup> lineups) {
		content = new JPanel(new GridLayout(1, 2));
		content.setBorder(BorderFactory.createEmptyBorder());
		combinationPanel = new JPanel(new GridLayout(0, 2));
		combinationPanel.setBorder(BorderFactory.createEmptyBorder(-4, 0, 0, 0));
		resultPanel = new JPanel(new BorderLayout());
		resultPanel.setBorder(BorderFactory.createEmptyBorder());
		content.add(combinationPanel);
		content.add(resultPanel);
		return content;
	}
	
	private static void updateContent(Lineup lineup, PropManager propManager) {
		combinationPanel.removeAll();
		propBoxes = new ArrayList<JComboBox<Property>>();
		List<PropType> types = propManager.getTypesByProps(lineup.getProps());
		types.sort(new Comparator<PropType>() {

			@Override
			public int compare(PropType o1, PropType o2) {
				return o1.compareTo(o2);
			}
		});
		for(PropType type : types) {
			JPanel item = new JPanel(new GridLayout(2, 1));
			JPanel header = new JPanel();
			JLabel text = new JLabel(type.getName((String)language.getSelectedItem()));
			item.setBorder(BorderFactory.createEmptyBorder());
			text.setBorder(BorderFactory.createEmptyBorder());;
			header.setBorder(BorderFactory.createEmptyBorder());
			text.setForeground(Constants.WHITE);
			header.add(text);
			header.setBackground(Constants.BACKGROUND_COLOR_DARK);
			item.add(header);
			JComboBox<Property> propBox = new JComboBox<Property>();
			propBox.setBorder(BorderFactory.createEmptyBorder());
			propBox.setRenderer(new _ModuleCellRenderer((String)language.getSelectedItem(), Arrays.asList()));
			List<Property> props = propManager.getPropsWithType(lineup.getProps(), type.getID());
			props.sort(new Comparator<Property>() {

				@Override
				public int compare(Property o1, Property o2) {
					return o1.compareTo(o2);
				}
			});
			for(Property prop : props) {
				propBox.addItem(prop);
			}
			propBox.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					reloadContent(lineup);
				}
			});
			propBoxes.add(propBox);
			item.add(propBox);
			combinationPanel.add(item);
		}
		JPanel item = new JPanel(new GridLayout(1, 2));
		JButton combinate = new JButton("Check");
		combinate.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				combinate();
			}
		});
		item.add(combinate);
		combinationPanel.add(item);
		reloadContent(lineup);
		combinationPanel.revalidate();
		combinationPanel.repaint();
	}
	
	private static void reloadContent(Lineup lineup) {
		validCombination = true;
		influential.clear();
		for(JComboBox<Property> propBox : propBoxes) {
			List<Integer> invalid = new ArrayList<Integer>();
			for(int i = 0; i < propBox.getItemCount(); i++) {
				if(!checkIsValid(lineup.getDependency(propBox.getItemAt(i).getID()))) invalid.add(i);
			}
			propBox.setRenderer(new _ModuleCellRenderer((String)language.getSelectedItem(), invalid));
			propBox.setBorder((invalid.contains(propBox.getSelectedIndex())) ? BorderFactory.createLineBorder(Constants.ALARM) : BorderFactory.createEmptyBorder());
			if(invalid.contains(propBox.getSelectedIndex())) validCombination = false;
			propBox.revalidate();
			propBox.repaint();
		}
		for(JComboBox<Property> propBox : influential) {
			propBox.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.GREEN), propBox.getBorder()));
			propBox.revalidate();
			propBox.repaint();
		}
	}
	
	private static boolean checkIsValid(Dependency dep) {
		List<List<Case>> cases = dep.getCases();
		for(int and = 0; and < cases.size(); and++) {
			boolean rowValid = false;
			for(int or = 0; or < cases.get(and).size(); or++) {
				for(JComboBox<Property> propBox : propBoxes) {
					if((Property)propBox.getSelectedItem() != null) {
						if((((Property)propBox.getSelectedItem()).getID() == cases.get(and).get(or).getPropID() && !cases.get(and).get(or).isNot()) || 
						(!(((Property)propBox.getSelectedItem()).getID() == cases.get(and).get(or).getPropID()) && ((Property)propBox.getSelectedItem()).getTypeID() == propManager.getProp(cases.get(and).get(or).getPropID()).getTypeID() && cases.get(and).get(or).isNot())){
							rowValid = true;
							influential.remove(propBox);
							break;
						} else if(((Property)propBox.getSelectedItem()).getTypeID() == (propManager.getProp(cases.get(and).get(or).getPropID()).getID())) {
							influential.add(propBox);
						}
					}
				}
				if(rowValid) break;
			}
			if(!rowValid) {
				return false;
			}
		}
		return true;
	}
	
	private static void combinate() {
		resultPanel.removeAll();
		if(validCombination) {
			List<Property> props = new ArrayList<Property>();
			for(JComboBox<Property> propBox : propBoxes) {
				Property prop = (Property)propBox.getSelectedItem();
				props.add(prop);
			}
			
			StringBuilder code = new StringBuilder("-----------------###           ");
			for(Property prop : props) {
				int pos = propManager.getType(prop.getTypeID()).getStartPos();
				int len = pos +  propManager.getType(prop.getTypeID()).getLength();
				for(int i = pos; i < len; i++) {
					code.setCharAt(i, prop.getCode().charAt(i - pos));
				}
			}
			String codeAlligned = code.toString().trim();
			codeAlligned = codeAlligned.replace("#", "");
			JTextField codeLabel = new JTextField(codeAlligned);
			codeLabel.setHorizontalAlignment(JTextField.CENTER);
			codeLabel.setEditable(false);
			codeLabel.setForeground(Constants.WHITE);
			codeLabel.setBackground(Constants.BACKGROUND_COLOR);
			codeLabel.setFont(new Font("Code", Font.PLAIN, 30));
			codeLabel.setBorder(BorderFactory.createEmptyBorder());
			JPanel text = new JPanel();
			text.setLayout(new BoxLayout(text, BoxLayout.PAGE_AXIS));
			text.add(codeLabel);
			if(!decoder.contains(codeAlligned)) {
				JTextField label = new JTextField((language.getSelectedItem().toString().equals("LT")) ? "Naujas kodas" : "New code");
				label.setEditable(false);
				label.setFocusable(false);
				label.setHorizontalAlignment(JTextField.CENTER);
				label.setBackground(Constants.WHITE);
				label.setForeground(Constants.ACTION_DELETE);
				label.setFont(new Font("Note", Font.ITALIC, 20));
				label.setBorder(BorderFactory.createEmptyBorder());
				text.add(label);
			}
			resultPanel.add(text, BorderLayout.PAGE_START);
			resultPanel.add(decoder.getImages(codeAlligned), BorderLayout.CENTER);
		} else {
			JTextField label = new JTextField((language.getSelectedItem().toString().equals("LT")) ? "Neįmanomas variantas" : "Impossible Combination");
			label.setEditable(false);
			label.setFocusable(false);
			label.setBackground(Constants.WHITE);
			label.setForeground(Constants.ACTION_DELETE);
			label.setFont(new Font("Note", Font.BOLD, 20));
			label.setHorizontalAlignment(JTextField.CENTER);
			label.setBorder(BorderFactory.createEmptyBorder());
			resultPanel.add(label, BorderLayout.CENTER);
		}
		resultPanel.setBackground(Constants.WHITE);
		resultPanel.revalidate();
		resultPanel.repaint();
	}

	
}
