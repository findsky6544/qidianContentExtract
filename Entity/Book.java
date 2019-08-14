package Entity;

public class Book {
	public String id;
	public String name;
	
	public Book(String id,String name) {
		this.id = id;
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}
