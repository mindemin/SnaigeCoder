import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Property implements Serializable, Comparable<Property> {
	private static final long serialVersionUID = 4918033521890683541L;
	
	private int ID;
	private int typeID;
	private String code;
	private Map<String, String> desc;
	
	public Property(int typeID, String code) {
		this.typeID = typeID;
		this.code = code;
		this.desc = new HashMap<String, String>();
		this.ID = this.hashCode();
	}
	
	public Property(int typeID, String code, Map<String, String> desc) {
		this.typeID = typeID;
		this.code = code;
		this.desc = desc;
		this.ID = this.hashCode();
	}
	
	public int getID() {
		return ID; 
	}
	
	public int getTypeID() {
		return typeID;
	}
	
	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getDesc(String lang) {
		if(desc != null && desc.containsKey(lang)) return desc.get(lang);
		else return "No description in " + lang;
	}
	
	public void addDesc(String lang, String desc) {
		this.desc.put(lang, desc);
	}

	@Override
	public int compareTo(Property o) {
		return this.getCode().compareTo(o.getCode());
	}
	
	public String toString() {
		return code;
	}
}
