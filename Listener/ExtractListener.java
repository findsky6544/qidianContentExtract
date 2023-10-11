package Listener;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingWorker;

import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.dom.DOMDocument;

import CheckBoxTree.CheckBoxTreeNode;
import Entity.Menu.ChapterNode;
import main.DeleteFrame;

public class ExtractListener implements ActionListener {
	public File bookFile;
	PrintWriter writer;
	CheckBoxTreeNode root;
	int chapterCount;
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
						Thread.sleep((long) (Math.random()*1000+1000));
						if(DeleteFrame.addVolumeCheckBox.isSelected() && DeleteFrame.addEveryCharacterRadioButton.isSelected()) {
							DeleteFrame.outputArea.append(volumeNode.name+" ");
							writer.print(volumeNode.name+" ");
						}
						try {
						String url2 = "https://www.qidian.com/chapter/"+DeleteFrame.bookUrlField.getText()+"/"+chapterNode.url+"/";
						DeleteFrame.browser.loadURL(url2);

		 				Robot r=new Robot(); 
		 				r.delay(3000); 
						DOMDocument dom = DeleteFrame.browser.getDocument();
						//System.out.println(dom.getDocumentElement().getInnerHTML());
						String chapterTitle = dom.findElement(By.className("title text-1.3em")).getInnerHTML();
						chapterTitle = chapterTitle.substring(0, chapterTitle.indexOf('<'));
						System.out.println(chapterTitle);
						writer.print(chapterTitle+"\r\n");
						DeleteFrame.progressBar.setString("正在提取:"+volumeNode.name+":"+chapterTitle+"   已完成"+index+"/"+chapterCount);
						String chapterContent = dom.findElement(By.tagName("main")).getInnerHTML();
						//chapterContent = chapterContent.replaceAll(" ","\r\n\r\n");
						chapterContent = chapterContent.replaceAll("<p data-type=\"2\">","\r\n\r\n");
						chapterContent = chapterContent.replaceAll("</?p.*?>","");
						chapterContent = chapterContent.replaceAll("<span class=\"review-count\".*?</span>","");
						chapterContent = chapterContent.replaceAll("</?span.*?>","");
						chapterContent = chapterContent.replaceAll("</?i.*?>","");
						chapterContent = chapterContent.replaceAll("</?cite.*?>","");
						chapterContent = chapterContent.replaceAll("<!---->","\r\n\r\n");
						//DeleteFrame.outputArea.append(chapterContent+"\r\n\r\n");
						writer.println(chapterContent+"\r\n");
						System.out.println(chapterContent);
						DeleteFrame.progressBar.setString("正在提取:"+volumeNode.name+":"+chapterTitle+"   已完成"+(index+1)+"/"+chapterCount);
						 DeleteFrame.progressBar.setValue(++index);
						}
						catch(Exception ex) {
							ex.printStackTrace();
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
