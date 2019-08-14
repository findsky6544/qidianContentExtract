package Listener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.DeleteFrame;

public class ComboBoxChangeListener implements ActionListener{

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		DeleteFrame.bookUrlField.setText(DeleteFrame.books.get((DeleteFrame.bookNameComboBox.getSelectedIndex())).id);
	}

}
