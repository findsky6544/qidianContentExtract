package Listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.google.gson.Gson;

import Entity.Menu.Config;
import main.DeleteFrame;

public class ConfigChangeListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		File configFile = new File("config.txt");
		Scanner input = null;
		try {
			Gson gson = new Gson();
			input = new Scanner(configFile);
			Config config = new Config(DeleteFrame.addVolumeCheckBox.isSelected(),DeleteFrame.addOnlyFirstRadioButton.isSelected()?"addOnlyFirst":DeleteFrame.addEveryCharacterRadioButton.isSelected()?"addEveryCharacter":"");
			String configJson = gson.toJson(config);
			PrintWriter writer = new PrintWriter(configFile);
			writer.println(configJson);
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
