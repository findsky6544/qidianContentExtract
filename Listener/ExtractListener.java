package Listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.SwingWorker;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import CheckBoxTree.CheckBoxTreeNode;
import Entity.ChapterNode;
import main.DeleteFrame;

public class ExtractListener implements ActionListener {
	public File bookFile;
	PrintWriter writer;
	CheckBoxTreeNode root;
	int chapterCount;
	
	public static Map<String,String> cookies = new HashMap<String,String>();
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		//创建导出文件
		try {
			Date date = new Date();
			SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmmss");
			bookFile = new File(GetMenuListener.bookName+"_"+ft.format(date)+".txt");
			if (!bookFile.exists()) {
				bookFile.createNewFile();
			}
			writer = new PrintWriter(bookFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		root = (CheckBoxTreeNode)DeleteFrame.menuTree.getModel().getRoot();
		//设定进度条
		chapterCount = 0;
		for(Object volumeNodeObj:root.getChildren()) {
			ChapterNode volumeNode = (ChapterNode)volumeNodeObj;
			for(Object chapterNodeObj:volumeNode.getChildren()) {
				ChapterNode chapterNode = (ChapterNode)chapterNodeObj;
				if(chapterNode.isSelected) {
					chapterCount++;
				}
			}
		}
		DeleteFrame.progressBar.setValue(0);
		DeleteFrame.progressBar.setMaximum(chapterCount);
		new updateProcessBar().execute();
	}
	
	class updateProcessBar extends SwingWorker<Void,Integer> {

		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			int index = 0;
			//提取
			DeleteFrame.outputArea.setText("");
			for(Object volumeNodeObj:root.getChildren()) {
				ChapterNode volumeNode = (ChapterNode)volumeNodeObj;
				boolean hasChildSelected = false;
				for(Object chapterNodeObj:volumeNode.getChildren()) {
					ChapterNode chapterNode = (ChapterNode)chapterNodeObj;
					if(chapterNode.isSelected) {
						hasChildSelected = true;
						break;
					}
				}
				if(DeleteFrame.addVolumeCheckBox.isSelected() && DeleteFrame.addOnlyFirstRadioButton.isSelected() && hasChildSelected) {
					DeleteFrame.outputArea.append(volumeNode.name+"\r\n\r\n");
					writer.println(volumeNode.name+"\n");
				}
				for(Object chapterNodeObj:volumeNode.getChildren()) {
					ChapterNode chapterNode = (ChapterNode)chapterNodeObj;
					if(chapterNode.isSelected) {
						if(DeleteFrame.addVolumeCheckBox.isSelected() && DeleteFrame.addEveryCharacterRadioButton.isSelected()) {
							DeleteFrame.outputArea.append(volumeNode.name+" ");
							writer.print(volumeNode.name+" ");
						}
						try {
							Document doc = Jsoup.connect("https://vipreader.qidian.com/chapter/"+DeleteFrame.bookUrlField.getText()+"/"+chapterNode.url).cookies(cookies).get();
							String chapterTitle = doc.getElementsByClass("content-wrap").get(0).html();
							DeleteFrame.outputArea.append(chapterTitle);
							writer.print(chapterTitle);
							DeleteFrame.progressBar.setString("正在提取:"+volumeNode.name+":"+chapterTitle+"   已完成"+index+"/"+chapterCount);
							String chapterContent = doc.getElementsByClass("read-content j_readContent").get(0).html();
							chapterContent = chapterContent.replaceAll("<p.*?>","\r\n\r\n");
							chapterContent = chapterContent.replaceAll("</p.*?>","");
							DeleteFrame.outputArea.append(chapterContent+"\r\n\r\n");
							writer.println(chapterContent+"\r\n");
							DeleteFrame.progressBar.setString("正在提取:"+volumeNode.name+":"+chapterTitle+"   已完成"+(index+1)+"/"+chapterCount);
				             DeleteFrame.progressBar.setValue(++index);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			writer.close();
			return null;
		}

	    protected void done() {
	    	DeleteFrame.progressBar.setString("提取完成");
	    }
	}
}
