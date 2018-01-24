/*	
 * 	@author:	Justas Belevicius
 * 	@created:	8th August 2017
 * 	@purpose:	Manage tabs
 * 
 */

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;

public class Tab {
	private String title;
	private Component toolbar;
	private Component content;

	public Tab(String title, Component toolbar, Component content) {
		this.title = title;
		setToolbar(toolbar);
		setContent(content);
	}

	public String getTitle() {
		return title;
	}

	public Component getToolbar() {
		return toolbar;
	}

	public void setToolbar(Component toolbar) {
		this.toolbar = toolbar;
		toolbar.setBackground(Constants.BACKGROUND_COLOR);
		toolbar.setForeground(Constants.WHITE);
	}

	public void setContent(Component content) {
		this.content = content;
	}

	public Component getInsides() {
		JPanel inside = new JPanel();
		inside.setLayout(new BorderLayout());
		inside.add(toolbar, BorderLayout.PAGE_START);
		inside.add(content, BorderLayout.CENTER);
		return inside;
	}
}
