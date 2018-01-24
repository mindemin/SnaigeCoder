import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropManager implements Serializable {
	private static final long serialVersionUID = -7559267924340589305L;
	
	private List<PropType> types;
	private List<Property> props;
	
	public PropManager () {
		types = new ArrayList<PropType>();
		props = new ArrayList<Property>();
	}
	// TYPE FUNCTIONS
	public PropType getType (int typeID) {
		for(PropType item : types) {
			if(item.getID() == typeID) return item;
		}
		System.out.println("PropManager.getType(): Type Not Found");
		return null;
	}
	public PropType getTypeByProps (int IDs) {
		Property prop = getProp(IDs);
		PropType item = getType(prop.getTypeID());
		return item;
	}
	public List<PropType> getTypes () {
		return types;
	}
	public List<PropType> getTypes (List<Integer> typeIDs) {
		List<PropType> l = new ArrayList<PropType>();
		for(PropType item : types) {
			if(typeIDs.contains(item.getID())) l.add(item);
		}
		return l;
	}
	public List<PropType> getTypesByProps (List<Integer> IDs) {
		List<PropType> l = new ArrayList<PropType>();
		for(Property prop : getProps(IDs)) {
			PropType item = getType(prop.getTypeID());
			if(!l.contains(item)) l.add(item);
		}
		return l;
	}
	public List<PropType> getHeadTypes () {
		List<PropType> l = new ArrayList<PropType>();
		for(PropType item : types) {
			if(item.getStartPos() == 0) l.add(item);
		}
		return l;
	}
	public void addType (PropType type) {
		types.add(type);
	}
	public void remType (PropType type) {
		// Delete all child props
		for(Property child : props) {
			if(child.getTypeID() == type.getID()) props.remove(child);
		}
		// Delete type
		types.remove(type);
	}
	
	// PROP FUNCTIONS
	public Property getProp (int ID) {
		for(Property item : props) {
			if(item.getID() == ID) return item;
		}
		System.out.println("PropManager.getProp(): Prop Not Found");
		return null;
	}
	public List<Property> getProps () {
		return props;
	}
	public List<Property> getProps (int typeID) {
		List<Property> l = new ArrayList<Property>();
		for(Property item : props) {
			if(item.getTypeID() == typeID) l.add(item);
		}
		return l;
	}
	public List<Property> getProps(List<Integer> IDs) {
		List<Property> l = new ArrayList<Property>();
		for(int ID : IDs) {
			for(Property item : props) {
				if(item.getID() == ID) {
					l.add(item);
					break;
				}
			}
		}
		return l;
	}
	public List<Property> getPropsWithType(List<Integer> IDs, int typeID) {
		List<Property> l = new ArrayList<Property>();
		for(int ID : IDs) {
			for(Property item : getProps(typeID)) {
				if(item.getID() == ID) {
					l.add(item);
					break;
				}
			}
		}
		return l;
	}
	public List<Property> getHeadProps () {
		List<Property> l = new ArrayList<Property>();
		for(PropType p : getHeadTypes()) {
			l.addAll(getProps(p.getID()));
		}
		return l;
	}
	public void addProp (Property prop) {
		props.add(prop);
	}
	public void remProp (Property prop) {
		props.remove(prop);
	}
	
	
}
