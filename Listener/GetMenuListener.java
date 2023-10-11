package Listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.teamdev.jxbrowser.chromium.Cookie;
import com.teamdev.jxbrowser.chromium.DataReceivedParams;
import com.teamdev.jxbrowser.chromium.LoadURLParams;
import com.teamdev.jxbrowser.chromium.RequestCompletedParams;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;
import com.teamdev.jxbrowser.chromium.dom.DOMElement;
import com.teamdev.jxbrowser.chromium.javafx.DefaultNetworkDelegate;

import Entity.Menu.Book;
import Entity.Menu.ChapterNode;
import Entity.Menu.Cs;
import Entity.Menu.Data;
import Entity.Menu.Vs;
import Entity.Menu.Xml;
import main.DeleteFrame;

public class GetMenuListener implements ActionListener {
	public static Gson gson = new Gson();
	public static String jsonData;
	public static String bookName;
	public static Xml xml;

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		String bookId = DeleteFrame.bookUrlField.getText();
		try {
			bookName = getBookName(bookId);
			List<Cookie> cookies = DeleteFrame.browser.getCookieStorage().getAllCookies();
			String cookie = "";
			for(int i = 0;i < cookies.size();i++) {
				if(cookies.get(i).getName().equals("_csrfToken")) {
					cookie = cookies.get(i).getValue();
					break;
				}
			}
			String menuUrl = "https://www.qidian.com/ajax/book/category?"+"_csrfToken="+cookie+"&bookId="+bookId;
			DeleteFrame.browser.loadURL(menuUrl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	
	
	public String getBookName(String bookId) throws IOException {
		String bookUrl = "https://www.qidian.com/book/"+bookId;
		DeleteFrame.browser.loadURL(bookUrl);
		while(DeleteFrame.browser.isLoading()) {
		}
		DOMDocument dom = DeleteFrame.browser.getDocument();
		DOMElement e = dom.findElement(By.cssSelector(".book-info-top #bookName"));
		return e.getInnerHTML();
	}
	
	public static void addBook(String id,String name) {
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
		Scanner input = null;
		try {
			input = new Scanner(bookListFile);
			String bookListJson = gson.toJson(DeleteFrame.books);
			PrintWriter writer = new PrintWriter(bookListFile);
			writer.println(bookListJson);
			writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally {
			if(input != null) {
				input.close();
			}
		}
	}
}

