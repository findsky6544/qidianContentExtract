package Listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import CheckBoxTree.CheckBoxTreeCellRenderer;
import CheckBoxTree.CheckBoxTreeNode;
import CheckBoxTree.CheckBoxTreeNodeSelectionListener;
import Entity.Book;
import Entity.ChapterNode;
import Entity.Cs;
import Entity.Data;
import Entity.Vs;
import Entity.Xml;
import main.DeleteFrame;

public class GetMenuListener implements ActionListener {
	Gson gson = new Gson();
	String menuUrl = "https://book.qidian.com/ajax/book/category?_csrfToken=GBtUpdbfDGrkiPBaQ4fWpruQ117Sxz0PNZDHNRB3&bookId=";
	String jsonData;
	public static String bookName;
	public static Xml xml;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String bookId = DeleteFrame.bookUrlField.getText();
		try {
			bookName = getBookName(bookId);
			jsonData = Jsoup.connect(menuUrl+bookId).execute().body();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        xml = gson.fromJson(jsonData, Xml.class);
        Data data = xml.data;
        ChapterNode bookNameNode = new ChapterNode(bookName);
        addBook(bookId,bookName);
        for(int i = 0;i<data.vs.size();i++) {
        	Vs vs = data.vs.get(i);
        	ChapterNode volumeNode = new ChapterNode(vs.vN);
        	bookNameNode.add(volumeNode);
        	//DeleteFrame.outputArea.append(vs.vN+"\n");
        	for(int j = 0;j < vs.cs.size();j++) {
        		Cs cs = vs.cs.get(j);
            	//DeleteFrame.outputArea.append(cs.cN+cs.cU+"\n");
        		ChapterNode chapterNode = new ChapterNode(cs.cN,cs.id);
            	volumeNode.add(chapterNode);
        	}
        }
        TreeModel treeModel = new DefaultTreeModel(bookNameNode);
        DeleteFrame.menuTree.setModel(treeModel);
	}
	
	public String getBookName(String bookId) throws IOException {
		String bookUrl = "https://book.qidian.com/info/"+bookId;
		Document doc = Jsoup.connect(bookUrl).get();
		Elements bookInfo = doc.select(".book-info");
		return bookInfo.get(0).getElementsByTag("h1").get(0).getElementsByTag("em").get(0).html();
	}
	
	public void addBook(String id,String name) {
		for(int i = 0; i < DeleteFrame.books.size();i++) {
			if(DeleteFrame.books.get(i).id.equals(id)) {
				DeleteFrame.bookNameComboBox.setSelectedIndex(i);
				return;
			}
		}
		Book newBook = new Book(id,name);
		DeleteFrame.books.add(newBook);
		DeleteFrame.bookNameComboBox.addItem(newBook.name);
		DeleteFrame.bookNameComboBox.setSelectedIndex(DeleteFrame.books.size()-1);
		

		File bookListFile = new File("bookList.txt");
		try {
			Scanner input = new Scanner(bookListFile);
			String bookListJson = gson.toJson(DeleteFrame.books);
			PrintWriter writer = new PrintWriter(bookListFile);
			writer.println(bookListJson);
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

