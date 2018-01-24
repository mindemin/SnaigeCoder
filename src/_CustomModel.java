import java.util.Vector;

import javax.swing.DefaultComboBoxModel;

class _CustomModel extends DefaultComboBoxModel<Object> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1077984728836166024L;
	public _CustomModel() {}
    public _CustomModel(Vector<Object> items) {
        super(items);
    }
    @Override
    public void setSelectedItem(Object item) {
        if (item instanceof PropType) {
            return;
		}
        super.setSelectedItem(item);
    };
}
