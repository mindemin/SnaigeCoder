import java.io.Serializable;
import java.util.Map;

public class PropType implements Serializable, Comparable<PropType>  {
	private static final long serialVersionUID = -7333074599959532716L;
	
	private int ID;
	private int startPos;
	private int length;
	private Map<String, String> name;
	
	public PropType (int startPos, int length, Map<String, String> name) {
		this.startPos = startPos;
		this.length = length;
		this.name = name;
		this.ID = this.hashCode();
	}
	
	public int getID () {
		return ID;
	}
	
	public void setStartPos (int startPos) {
		this.startPos = startPos;
	}
	public int getStartPos () {
		return startPos;
	}
	
	public void setLength (int length) {
		this.length = length;
	}
	public int getLength () {
		return length;
	}

	public String getName(String lang) {
		if(name != null && name.containsKey(lang)) return name.get(lang);
		else return "No name in " + lang;
	}
	public void addName(String lang, String name) {
		this.name.put(lang, name);
	}
	
	@Override
	public int compareTo(PropType o) {
		if (this.getStartPos() > o.getStartPos())
			return 1;
		else if (this.getStartPos() < o.getStartPos())
			return -1;
		else
			return 0;
	}
	
	public String toString() {
		return name.get("EN") + " (Size: " + length + ")";
	}
}
