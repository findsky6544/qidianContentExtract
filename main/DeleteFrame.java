package main;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import CheckBoxTree.CheckBoxTreeCellRenderer;
import CheckBoxTree.CheckBoxTreeNode;
import CheckBoxTree.CheckBoxTreeNodeSelectionListener;
import Debug.Log;
import Entity.Book;
import Entity.Config;
import Listener.ComboBoxChangeListener;
import Listener.ConfigChangeListener;
import Listener.ExtractListener;
import Listener.GetMenuListener;
import Listener.LoginListener;

public class DeleteFrame extends JFrame {
	public JTabbedPane tabPanel = new JTabbedPane(JTabbedPane.NORTH);
	//登录
	public static JPanel loginPanel = new JPanel();
	public static JPanel loginInputPanel = new JPanel();
	public static JLabel useridInputLabel = new JLabel("ywguid");
	public static JTextField useridField = new JTextField();
	public static JLabel passwordInputLabel = new JLabel("ywkey");
	public static JTextField passwordField = new JTextField();
	public static JButton loginButton = new JButton("保存");
	//目录
	public static JPanel extractPanel = new JPanel(new BorderLayout());
	public static JPanel extractControlPanel = new JPanel(new BorderLayout());
	public static JPanel extractControlInputPanel = new JPanel(new GridLayout());
	public static JPanel bookNamePanel = new JPanel(new BorderLayout());
	public static JLabel bookNameLabel = new JLabel("书名：");
	public static JComboBox bookNameComboBox = new JComboBox();
	public static JPanel urlPanel = new JPanel(new BorderLayout());
	public static JLabel bookIdLabel = new JLabel("书籍ID：");
	public static JTextField bookUrlField = new JTextField();
	public static JButton getMenuButton = new JButton("获取目录");
	
	public static JPanel bookPanel = new JPanel(new GridLayout());
	public static JTree menuTree = new JTree();
	public static JScrollPane scrollPaneInput = new JScrollPane(menuTree);
	
	public static JTextArea outputArea = new JTextArea();
	public static JScrollPane scrollPaneOutput = new JScrollPane(outputArea);
	
	public static JProgressBar progressBar = new JProgressBar();
	public static JButton extractButton = new JButton("提取");
	public static JPanel bottomPanel = new JPanel(new GridLayout(2,1));

	//设置
	public static JPanel configPanel = new JPanel(new FlowLayout());
	public static JCheckBox addVolumeCheckBox = new JCheckBox("添加卷名");
	public static JRadioButton addOnlyFirstRadioButton = new JRadioButton("仅在每卷开头添加",true);
	public static JRadioButton addEveryCharacterRadioButton = new JRadioButton("在每一章开头添加");
	
	//使用说明
	public static JPanel useInstructionPanel = new JPanel();
	public static JLabel useInstructionLabel = new JLabel();
	
	
	public static List<Book> books = new ArrayList<Book>();
	Gson gson = new Gson();

	public DeleteFrame() {
		setTitle("起点文本提取器");
		setLayout(new BorderLayout());
		this.setSize(800, 500);
		//登录选项卡
		loginInputPanel.setLayout(null);
		useridInputLabel.setBounds(50,20, 50, 30);
		loginInputPanel.add(useridInputLabel);
		passwordInputLabel.setBounds(50,60, 50, 30);
		loginInputPanel.add(passwordInputLabel);
		useridField.setBounds(110,20, 150, 30);
		loginInputPanel.add(useridField);
		passwordField.setBounds(110,60, 150, 30);
		loginInputPanel.add(passwordField);
		loginButton.setBounds(120,100, 100, 30);
		loginButton.addActionListener(new LoginListener());
		loginInputPanel.add(loginButton);
		
		loginInputPanel.setSize(360, 250);
		loginInputPanel.setBounds((this.getWidth()-loginInputPanel.getWidth())/2, (this.getHeight()-loginInputPanel.getHeight())/2, loginInputPanel.getWidth(), loginInputPanel.getHeight());
	
		loginPanel.setLayout(null);
		loginPanel.add(loginInputPanel);
		
		tabPanel.add("登录",loginPanel);
		
		//提取选项卡
		bookNamePanel.add(bookNameLabel,"West");
		bookNamePanel.add(bookNameComboBox,"Center");
		bookNameComboBox.addActionListener(new ComboBoxChangeListener());
		extractControlInputPanel.add(bookNamePanel);
		
		urlPanel.add(bookUrlField,"Center");
		urlPanel.add(bookIdLabel,"West");
		extractControlInputPanel.add(urlPanel);

		extractControlPanel.add(extractControlInputPanel,"Center");
		extractControlPanel.add(getMenuButton,"East");
		getMenuButton.addActionListener(new GetMenuListener());
		
		extractPanel.add(extractControlPanel,"North");

		CheckBoxTreeNode nullNode = new CheckBoxTreeNode();
        TreeModel treeModel = new DefaultTreeModel(nullNode);
        menuTree.setModel(treeModel);
		DeleteFrame.menuTree.setCellRenderer(new CheckBoxTreeCellRenderer());
		DeleteFrame.menuTree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
        
		bookPanel.add(scrollPaneInput);
	    outputArea.setLineWrap(true);
	    outputArea.setWrapStyleWord(true);
		//bookPanel.add(scrollPaneOutput);
		extractPanel.add(bookPanel,"Center");
		
		bottomPanel.add(progressBar);
		progressBar.setStringPainted(true);//设置进度条显示提示信息
		extractButton.addActionListener(new ExtractListener());
		bottomPanel.add(extractButton);
		extractPanel.add(bottomPanel,"South");
		
		tabPanel.add("提取",extractPanel);
		
		//设置选项卡
		configPanel.add(addVolumeCheckBox);
		configPanel.add(addOnlyFirstRadioButton);
		configPanel.add(addEveryCharacterRadioButton);
		ButtonGroup buttonGroup=new ButtonGroup();
		buttonGroup.add(addOnlyFirstRadioButton);
		buttonGroup.add(addEveryCharacterRadioButton);
		addOnlyFirstRadioButton.setEnabled(false);
		addEveryCharacterRadioButton.setEnabled(false);
		addVolumeCheckBox.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if(addVolumeCheckBox.isSelected()) {
					addOnlyFirstRadioButton.setEnabled(true);
					addEveryCharacterRadioButton.setEnabled(true);
				}
				else {
					addOnlyFirstRadioButton.setEnabled(false);
					addEveryCharacterRadioButton.setEnabled(false);
				}
			}
		});
		addVolumeCheckBox.addActionListener(new ConfigChangeListener());
		addOnlyFirstRadioButton.addActionListener(new ConfigChangeListener());
		addEveryCharacterRadioButton.addActionListener(new ConfigChangeListener());
		tabPanel.add("设置",configPanel);
		
		useInstructionLabel.setText(
				"<html><body>"
			  + "1.登录起点，找到cookie中的ywguid和ywkey，填入登录界面并保存（如果只提取免费章节，可跳过此步骤）<br/>"
			  + "2.找到想提取的书籍的ID（即书籍介绍页网址最后的一串数字），填入提取页，然后获取目录<br/>"
			  + "3.选择想提取的章节，点击提取按钮，等待完成<br/>"
			  + "4.在软件根目录下即可获得提取出的文本。"
			  + "</body></html>"
				);
		useInstructionPanel.add(useInstructionLabel);
		tabPanel.add("使用说明",useInstructionPanel);
		
		add(tabPanel,"Center");
		try {
			readUserInfo();
			readBookList();
			readConfig();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.error(e1.toString());
		}
		
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentMoved(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentResized(ComponentEvent arg0) {
				// TODO Auto-generated method stub

				loginInputPanel.setBounds((arg0.getComponent().getWidth()-loginInputPanel.getWidth())/2, (arg0.getComponent().getHeight()-loginInputPanel.getHeight())/2, loginInputPanel.getWidth(), loginInputPanel.getHeight());
			}

			@Override
			public void componentShown(ComponentEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void readUserInfo() throws FileNotFoundException {
		// 读取用户信息
		File userInfoFile = new File("userInfo.txt");
		if (userInfoFile.exists()) {
			Scanner input = new Scanner(userInfoFile);
			useridField.setText(input.next());
			passwordField.setText(input.next());
			ExtractListener.cookies.put("ywguid", useridField.getText());
			ExtractListener.cookies.put("ywkey", passwordField.getText());
		} else {
			// 若不存在卷名txt则写入
			PrintWriter writer = new PrintWriter(userInfoFile);
			String title = "";
			writer.print(title);
			writer.close();
		}
	}
	
	public void readBookList() throws FileNotFoundException {
		// 读取书籍列表
		File bookListFile = new File("bookList.txt");
		if (bookListFile.exists()) {
			Scanner input = new Scanner(bookListFile);
			Gson gson = new Gson();
			java.lang.reflect.Type type = new TypeToken<List<Book>>() {}.getType();
			String bookListStr = input.next();
			if(!bookListStr.equals("")) {
				books = gson.fromJson(bookListStr,type);
			}
			for(int i = 0;i < books.size();i++) {
				bookNameComboBox.addItem(books.get(i).name);
			}
		} else {
			// 若不存在卷名txt则写入
			PrintWriter writer = new PrintWriter(bookListFile);
			String bookListJson = gson.toJson(DeleteFrame.books);
			writer.println(bookListJson);
			writer.close();
		}
	}
	
	public void readConfig() throws FileNotFoundException {
		// 读取设置
		File configFile = new File("config.txt");
		if (configFile.exists()) {
			Scanner input = new Scanner(configFile);
			Config config = gson.fromJson(input.next(),Config.class);
			this.addVolumeCheckBox.setSelected(config.isAddVolumeName);
			if(config.addVolumeNameWhere.equals("addOnlyFirst")) {
				this.addOnlyFirstRadioButton.setSelected(true);
			}
			else if(config.addVolumeNameWhere.equals("addEveryCharacter")) {
				this.addEveryCharacterRadioButton.setSelected(true);
			}
		} else {
			// 若不存在设置txt则写入
			PrintWriter writer = new PrintWriter(configFile);
			Config config = new Config();
			String configJson = gson.toJson(config);
			writer.println(configJson);
			writer.close();
		}
	}
}
