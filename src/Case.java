import java.io.Serializable;

public class Case implements Serializable {
	private static final long serialVersionUID = 4572145191909843355L;
	
	private int propID;
	private boolean not;
	
	public Case(int propID, boolean not) {
		this.propID = propID;
		this.not = not;
	}
	
	public boolean isNot() {
		return not;
	}
	
	public int getPropID() {
		return propID;
	}
}
