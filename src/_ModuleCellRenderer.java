

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

class _ModuleCellRenderer implements ListCellRenderer<Property> {
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private final static Dimension preferredSize = new Dimension(0, 20);
	private String lang;
	private List<Integer> invalid;
	public _ModuleCellRenderer (String language, List<Integer> invalid) {
		super();
		this.lang = language;
		this.invalid = invalid;
	}
	
	public Component getListCellRendererComponent(JList<? extends Property> list, Property value, int index, boolean isSelected, boolean cellHasFocus) {

		JTextArea renderer = new JTextArea(2, 20);
		renderer.setWrapStyleWord(true);
		renderer.setLineWrap(true);
		renderer.setEditable(false);
		if(index % 2 == 0) {
			renderer.setBackground(Constants.LIST_PRIMARY);
			renderer.setForeground(list.getSelectionForeground());
		} else {
			renderer.setBackground(Constants.LIST_SECONDARY);
			renderer.setForeground(list.getSelectionForeground());
		}
		if (isSelected) {
			renderer.setBackground(list.getSelectionBackground());
			renderer.setForeground(list.getSelectionForeground());
        }
		if(invalid.contains(index)) {
			renderer.setBackground(Constants.ALARM_BG);
			renderer.setFocusable(false);
		} else {
			renderer.setFocusable(true);
		}
		if (value instanceof Property) {
			renderer.setText(((Property) value).getDesc(lang));
		}
		renderer.setPreferredSize(preferredSize);
		return renderer;
	}
}
