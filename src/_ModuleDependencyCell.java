

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;

public class _ModuleDependencyCell implements ListCellRenderer<Object> {
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private final static Dimension preferredSize = new Dimension(0, 20);
	public _ModuleDependencyCell() {
		super();
	}
	
	public Component getListCellRendererComponent(JList<? extends Object> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		JTextArea renderer = new JTextArea(1, 20);
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
		if (value instanceof Property) {
			Property prop = (Property)value;
			renderer.setText(prop.getCode());
		}
		if (value instanceof PropType) {
			PropType type = (PropType)value;
			renderer.setText(type.getName("EN"));
			renderer.setBackground(Color.DARK_GRAY);
			renderer.setForeground(Color.WHITE);
			renderer.setDisabledTextColor(Color.WHITE);
			renderer.setEnabled(false);
			renderer.setFocusable(false);
		}
		renderer.setPreferredSize(preferredSize);
		return renderer;
	}
}
