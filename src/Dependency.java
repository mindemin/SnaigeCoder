import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Dependency implements Serializable {
	private static final long serialVersionUID = -539040879512157146L;
	
	private int ownerID;
	private List<List<Case>> cases;
	
	public Dependency(int ownerID) {
		this.ownerID = ownerID;
		cases = new ArrayList<List<Case>>();
	}
	
	public int addRow() {
		cases.add(new ArrayList<Case>());
		return cases.size() - 1;
	}
	
	public void addCase(Case c, int row) {
		cases.get(row).add(c);
	}
	
	public void remCase(Case c, int row) {
		cases.get(row).remove(c);
	}
	
	public List<List<Case>> getCases() {
		if(cases == null) {
			cases = new ArrayList<List<Case>>();
		}
		return cases;
	}
	
	public int getOwnerID() {
		return ownerID;
	}
}
