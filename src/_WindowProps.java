import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class _WindowProps extends Tab {

	public _WindowProps(_ModulePropList propList, PropManager propManager) {
		super("Values", createToolbar(propList, propManager), propList);
	}

	private static Toolbar createToolbar(_ModulePropList propList, PropManager propManager) {
		Toolbar toolbar = new Toolbar();
		JButton button = new JButton("New value");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new _ModuleCreateProp("Create new Value", propManager, propList);
			}
		});
		toolbar.addButton(button);
		button = new JButton("New Type");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new _ModuleCreateType("Create new Type", propManager.getTypes(), propList);
			}
		});
		toolbar.addButton(button);
		button = new JButton("Refresh");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				propList.updateView();
			}
		});
		toolbar.addButton(button);
		return toolbar;
	}
}
