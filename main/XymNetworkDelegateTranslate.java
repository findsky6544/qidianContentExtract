package main;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.teamdev.jxbrowser.chromium.DataReceivedParams;
import com.teamdev.jxbrowser.chromium.RequestCompletedParams;
import com.teamdev.jxbrowser.chromium.dom.By;
import com.teamdev.jxbrowser.chromium.javafx.DefaultNetworkDelegate;

import Entity.Menu.ChapterNode;
import Entity.Menu.Cs;
import Entity.Menu.Data;
import Entity.Menu.Vs;
import Entity.Menu.Xml;
import Listener.GetMenuListener;

public class XymNetworkDelegateTranslate extends DefaultNetworkDelegate{
	List < byte[] > blist=new ArrayList<byte[]>();
	String data;
	
    @Override
    public void onDataReceived(DataReceivedParams params) {
        String url=params.getURL();
        if(url.indexOf("https://www.qidian.com/ajax/book/category")!=-1) {
            //System.out.println("接收数据URL..."+url);
            blist.add(params.getData());
        }
         
        super.onDataReceived(params);
    }
    
    @Override
    public void onCompleted(RequestCompletedParams params) {
        String url=params.getURL();
        if(url.indexOf("https://www.qidian.com/ajax/book/category")!=-1) {
            System.out.println(url);
            //System.out.println("数据接收完成...");
            try {
                saveByteList();
                
            	GetMenuListener.xml = GetMenuListener.gson.fromJson(data, Xml.class);
                Data data = GetMenuListener.xml.data;
                ChapterNode bookNameNode = new ChapterNode(GetMenuListener.bookName);
                GetMenuListener.addBook(DeleteFrame.bookUrlField.getText(),GetMenuListener.bookName);
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
            }catch(Exception e) {
                e.printStackTrace();
            }
            blist.clear();
            data = "";
        }
         
        super.onCompleted(params);
    }
    
   public void saveByteList() {
       if(blist.isEmpty()) {
           return;
       }
       int length=0;
       for(int i=0;i<blist.size();i++) {
           length+=blist.get(i).length;
       }
       byte[] b1=new byte[length];
       length=0;
       for(int i=0;i<blist.size();i++) {
           System.arraycopy(blist.get(i), 0, b1,length, blist.get(i).length);
           length+=blist.get(i).length;
       }
        
       try {
               data=new String(b1, "UTF-8");
               //System.out.println(data);
           }
           catch (UnsupportedEncodingException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
   }
}
