package Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import Debug.Log;
import main.DeleteFrame;
public class LoginListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub


		// 读取用户信息
		File userInfoFile = new File("userInfo.txt");
		try {
				ExtractListener.cookies.put("ywguid", DeleteFrame.useridField.getText());
				ExtractListener.cookies.put("ywkey", DeleteFrame.passwordField.getText());
				PrintWriter writer = new PrintWriter(userInfoFile);
				writer.println(DeleteFrame.useridField.getText());
				writer.println(DeleteFrame.passwordField.getText());
				writer.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.error(e1.toString());
		}
	}

}
