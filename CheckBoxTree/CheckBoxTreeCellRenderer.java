package CheckBoxTree;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

public class CheckBoxTreeCellRenderer extends JPanel implements TreeCellRenderer {
	protected JCheckBox check;
	protected CheckBoxTreeLabel label;
	
	public CheckBoxTreeCellRenderer() {
		setLayout(null);
		add(check = new JCheckBox());
		add(label = new CheckBoxTreeLabel());
		check.setBackground(UIManager.getColor("Tree.textBackground"));
		label.setForeground(UIManager.getColor("Tree.textForeground"));
	}
	
	public Component getTreeCellRendererComponent(JTree tree,Object value,boolean selected,boolean expanded,boolean leaf,int row,boolean hasFocus) {
		String stringValue = tree.convertValueToText(value, selected, expanded,leaf,row,hasFocus);
		setEnabled(tree.isEnabled());
		check.setSelected(((CheckBoxTreeNode)value).isSelected());
		label.setFont(tree.getFont());
		label.setText(stringValue);
		label.setSelected(selected);
		label.setFocus(hasFocus);
		if(leaf) {
			label.setIcon(UIManager.getIcon("Tree.leafIcon"));
		}
		else if(expanded) {
			label.setIcon(UIManager.getIcon("Tree.openIcon"));
		}
		else {
			label.setIcon(UIManager.getIcon("Tree.closeIcon"));
		}
		
		return this;
	}
	
	public Dimension getPreferredSize() {
		Dimension dCheck = check.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		return new Dimension(dCheck.width+dLabel.width,dCheck.height < dLabel.height ? dLabel.height: dCheck.height);
	}
	
	@Override
	public void doLayout()
	{
		Dimension dCheck = check.getPreferredSize();
		Dimension dLabel = label.getPreferredSize();
		int yCheck = 0;
		int yLabel = 0;
		if(dCheck.height < dLabel.height)
			yCheck = (dLabel.height - dCheck.height) / 2;
		else
			yLabel = (dCheck.height - dLabel.height) / 2;
		check.setLocation(0, yCheck);
		check.setBounds(0, yCheck, dCheck.width, dCheck.height);
		label.setLocation(dCheck.width, yLabel);
		label.setBounds(dCheck.width, yLabel, dLabel.width, dLabel.height);
	}
	
	@Override
	public void setBackground(Color color)
	{
		if(color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}
}
