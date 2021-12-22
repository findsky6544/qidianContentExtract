package CheckBoxTree;
import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;

public class CheckBoxTreeNode extends DefaultMutableTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1373472246138161472L;
	public boolean isSelected;
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public CheckBoxTreeNode()
	{
		this(null);
	}
	
	public CheckBoxTreeNode(Object userObject)
	{
		this(userObject, true, false);
	}
	
	public CheckBoxTreeNode(Object userObject, boolean allowsChildren, boolean isSelected)
	{
		super(userObject, allowsChildren);
		this.isSelected = isSelected;
	}
	
	public Vector getChildren() {
		return children;
	}

	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if(isSelected) {
			if(children != null) {
				for(Object obj :children) {
					CheckBoxTreeNode node = (CheckBoxTreeNode)obj;
					if(isSelected != node.isSelected) {
						node.setSelected(isSelected);
					}
				}
			}
			
			CheckBoxTreeNode pNode = (CheckBoxTreeNode)parent;
			if(pNode != null) {
				int index = 0;
				for(;index < pNode.children.size();index++) {
					CheckBoxTreeNode pChildNode = (CheckBoxTreeNode)pNode.children.get(index);
					if(!pChildNode.isSelected()) {
						break;
					}
					if(index == pNode.children.size()) {
						if(pNode.isSelected() != isSelected) {
							pNode.setSelected(isSelected);
						}
					}
				}
			}
		}
		else {
			if(children != null) {
				int index = 0;
				for(;index < children.size();index++) {
					CheckBoxTreeNode childNode = (CheckBoxTreeNode)children.get(index);
					if(!childNode.isSelected()) {
						break;
					}
				}
				if(index == children.size()) {
					for(int i = 0; i < children.size();i++) {
						CheckBoxTreeNode node = (CheckBoxTreeNode)children.get(i);
						if(node.isSelected() != isSelected) {
							node.setSelected(isSelected);
						}
					}
				}
			}
			CheckBoxTreeNode pNode = (CheckBoxTreeNode) parent;
			if(pNode != null && pNode.isSelected() != isSelected) {
				pNode.setSelected(isSelected);
			}
		}
	}
}
