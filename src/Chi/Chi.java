package Chi;

import javax.swing.UIManager;

public class Chi {

	public static void main (String [] args) throws Exception {
    	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		Logger.log("Reading configuration started.");
		Config.initialize();
		Logger.log("Reading configuration done.");
		
		Logger.log("Starting config UI started.");
		ConfigUI ui=new ConfigUI();
		ui.setVisible(true);
		Logger.log("Starting config UI done.");
	}
	
}
