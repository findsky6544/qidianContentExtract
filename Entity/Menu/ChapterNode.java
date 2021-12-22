package Entity.Menu;
import CheckBoxTree.CheckBoxTreeNode;

public class ChapterNode extends CheckBoxTreeNode {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6818430995099804842L;
	public String name;
	public String url;
	
	public ChapterNode() {
		super();
	}
	
	public ChapterNode(String name) {
		super(name);
		this.name = name;
	}
	
	public ChapterNode(String name,String url) {
		this(name);
		this.url = url;
	}
}
