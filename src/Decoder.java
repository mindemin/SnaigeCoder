import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Decoder {
	private PropManager propManager;
	private List<PropType> types;
	private List<Lineup> lineups;
	private List<String> codes;
	
	public Decoder(PropManager propManager, List<Lineup> lineups) {
		this.propManager = propManager;
		this.lineups = lineups;
		this.types = propManager.getTypes();
		types.sort(new Comparator<PropType>() {
			@Override
			public int compare(PropType o1, PropType o2) {
				return o1.compareTo(o2);
			}
		});
		//READ EXISTING CODES FROM FILE
		codes = new ArrayList<String>();
		try {
			codes = FileManager.loadLines("data/codes.txt");
		} catch (Exception e) {}
	}

	public List<Property> decode(String code, String language, boolean exist) {
		List<Property> properties = new ArrayList<Property>();
		if (codes.contains(code) || !exist) {
		//FIND LINEUP TODO: THIS IS VERY SYSTEM SPECIFIC
		String id = (code.charAt(5) == '-') ? code.substring(0, 5) : code.substring(0, 4);
		System.out.println(id);
		Lineup lineup = null;
		for (Lineup l : lineups) {
			System.out.print(l.getName() + "?=" + id);
			if (l.getName().equals(id)) {
				System.out.println(". Yes!");
				lineup = l;
				break;
			} else {
				System.out.println(". No!");
			}
		}
		if(lineup == null) return null;
		
		int overDistance = 0;
		for (PropType type : types) {
			int start = type.getStartPos() - overDistance;
			int end = start + type.getLength();
			for (Property prop : propManager.getProps(type.getID())) {
				String keyCode = prop.getCode();
				if(end <= code.length()) {
					int distance = (code.substring(start, end).contains("-") && keyCode.contains("#")) ? keyCode.length() - code.substring(start, end).split("-")[0].length() : 0;
					keyCode = keyCode.replace("#", "");
						//FOUND
						if (code.substring(start, end - distance).equals(keyCode) && lineup.getProps().contains(prop.getID())) {
							overDistance += distance;
							System.out.println(prop.getCode() + " was found in lineup: " + lineup.getName());
							properties.add(prop);
							break;
						}
					}
				}
			}
		}
		return properties;
	}
	
	public JScrollPane getImages(String code) {
		JPanel content = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JScrollPane panel = new JScrollPane(content);
		panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		panel.setBorder(BorderFactory.createEmptyBorder());
		content.setLayout(new GridLayout(0, 2));
		//FIND LINEUP TODO: THIS IS VERY SYSTEM SPECIFIC
		String id = (code.charAt(5) == '-') ? code.substring(0, 5) : code.substring(0, 4);
		File folder = new File("data/images/"+id);
		File[] arrayOfFiles = folder.listFiles();
		List<File> listOfFiles = new ArrayList<File>();
		if(arrayOfFiles != null)
			listOfFiles = Arrays.asList(arrayOfFiles);
		for(File file : listOfFiles) {
			String name = file.getName().split("\\.")[0];
			if(code.equals(name)) {
				try {
					BufferedImage img = ImageIO.read(file);
					if(img.getHeight() > 250) img = Constants.resize(img, 0, 250);
					if(img != null) { 
						ImageIcon icon = new ImageIcon(img);
						JLabel label = new JLabel(icon, JLabel.CENTER);
						content.add(label);
					}
				} catch (IOException e) {
					System.out.println("Something is wrong with: " + file.getName());
				}
			}
		}
		if(content.getComponentCount() == 0) {
			content.setLayout(new GridLayout(1, 1));
			content.add(new JLabel("No images of this products found", JLabel.CENTER));
		}
		content.setBackground(Constants.WHITE);
		return panel;
	}
	
	public boolean contains(String code) {
		return codes.contains(code);
	}
}