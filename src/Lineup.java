import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lineup implements Serializable {
	private static final long serialVersionUID = 8749186502385277993L;

	private String name;
	private Set<Integer> props;
	private List<Dependency> dependencies;
	
	public Lineup(String name) {
		this.name = name;
		props = new HashSet<Integer>();
		dependencies = new ArrayList<Dependency>();
	}
	
	public String getName() {
		return name;
	}
	
	// PROP FUNCTIONS
	public List<Integer> getProps() {
		List<Integer> l = new ArrayList<Integer>();
		l.addAll(props);
		return l;
	}
	
	public void remProp(int propID) {
		props.remove(propID);
	}
	
	public void addProp(int propID) {
		props.add(propID);
	}
	
	public void clearProps() {
		props.clear();
	}
	
	// DEPENDENCY FUNCTIONS
	public List<Dependency> getDependencies(){
		if(dependencies == null) {
			System.out.println("Dependencies is null");
		}
		System.out.println("Dependencies is not null " + dependencies.size());
		return dependencies;
	}
	
	public Dependency getDependency(int ownerID){
		if(dependencies != null) {
			for(Dependency item : dependencies) {
				if(ownerID == item.getOwnerID()) return item;
			}
		}
		Dependency dep = new Dependency(ownerID);
		dependencies.add(dep);
		return dep;
	}
	
	public void addDependency (Dependency dependency) {
		dependencies.add(dependency);
	}
	
	public void remDependency (Dependency dependency) {
		dependencies.remove(dependency);
	}
	
	public String toString() {
		return name;
	}
}
