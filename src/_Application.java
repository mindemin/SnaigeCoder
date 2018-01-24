import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.UIManager;
	
public class _Application {

	private static PropManager propManager;
	private static List<Lineup> lineups;
	private static Decoder decoder;
	
	public static void main(String[] args) {
		loader();
		imports();
		gui();
	}

	@SuppressWarnings("unchecked")
	private static void loader() {
		propManager = (PropManager) FileManager.loadObject("data/data.dat");
		if (propManager == null)
			propManager = new PropManager();
		
		lineups = (List<Lineup>) FileManager.loadObject("data/lineups.dat");
		if(lineups == null) lineups = new ArrayList<Lineup>();
		
		for(Property headType : propManager.getHeadProps()) {
			boolean add = true;
			for(Lineup lineup : lineups) {
				if(lineup.getName().equals(headType.getCode())) {
					add = false;
					break;
				}
			}
			if(add) lineups.add(new Lineup(headType.getCode()));
		}
		
		decoder = new Decoder(propManager, lineups);
	}

	private static void gui() {
		setStyle();
		_Window window = new _Window("Snaige", 1024, 720);
		
		_ModulePropList modulePropList = new _ModulePropList(propManager);

		window.addTab(new _WindowDecode(decoder));
		window.addTab(new _WindowCombinate(lineups, propManager, decoder));
		window.addTab(new _WindowProps(modulePropList, propManager));
		window.addTab(new _WindowLineups(new _ModuleLineupList(propManager, lineups)));
		
		window.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				FileManager.saveObject("data/data.dat", propManager);
				FileManager.saveObject("data/lineups.dat", lineups);
			}
		});

		window.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				modulePropList.updateView();
			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}

			@Override
			public void componentMoved(ComponentEvent e) {

			}

			@Override
			public void componentShown(ComponentEvent e) {
			}
		});

		modulePropList.updateView();
	}
	
	private static void setStyle() {
		// Tabs Styling
		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
		UIManager.put("TabbedPane.tabInsets", new Insets(2, 10, 2, 10));
		UIManager.put("TabbedPane.tabsOverlapBorder", true);
		UIManager.put("TabbedPane.font", new Font("Regular", 1, 12));
		UIManager.put("TabbedPane.background", Constants.BACKGROUND_COLOR_LIGHT);
		UIManager.put("TabbedPane.darkShadow", Constants.BACKGROUND_COLOR_LIGHT);
		UIManager.put("TabbedPane.light", Constants.BACKGROUND_COLOR_LIGHT);
		UIManager.put("TabbedPane.borderHightlightColor", Constants.BACKGROUND_COLOR);
		UIManager.put("TabbedPane.selectHighlight", Constants.BACKGROUND_COLOR);
		UIManager.put("TabbedPane.selected", Constants.BACKGROUND_COLOR);
		UIManager.put("TabbedPane.focus", Constants.BACKGROUND_COLOR);
		UIManager.put("TabbedPane.foreground", Constants.WHITE);
	}
	
	private static void imports() {
		List<String> lines = null;
		try {
			lines = FileManager.loadLines("import.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(lines != null) {
			int typeID = 0;
			for(String line : lines) {
				System.out.println(line);
				if(line.charAt(0) == 'T') { //TYPE
					String[] split = line.split(":")[1].split(";");
					PropType type = new PropType(Integer.parseInt(split[0].trim()), Integer.parseInt(split[1].trim()), new HashMap<String, String>());
					type.addName("LT", split[2].trim());
					type.addName("EN", split[3].trim());
					typeID = type.getID();
					propManager.addType(type);
				}
				else if (typeID != 0 && line.charAt(0) == 'P') { // PROPERTY
					String[] split = line.split(":")[1].split(";");
					Property prop = new Property(typeID, split[0].trim());
					prop.addDesc("LT", split[1].trim());
					prop.addDesc("EN", split[2].trim());
					propManager.addProp(prop);
				}
			}
		}
	}
}
