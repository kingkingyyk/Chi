package Chi;

import javax.swing.UIManager;

public class Chi {

	public static void main (String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		Logger.initialize();
		
		Logger.log("Initializing Utility class.");
		Utility.initialize();
		
		Logger.log("Reading configuration started.");
		Config.initialize();
		Logger.log("Reading configuration done.");
		
		Logger.log("Loading theme started.");
		Theme.initialize();
		Logger.log("Loading theme done.");
		
		Logger.log("MainUI started.");
		MenuUI ss=new MenuUI();
		ss.setLocationRelativeTo(null);
		Logger.log("MainUI done.");
		ss.setVisible(true);
		Logger.log("MainUI closed.");
	}
	
}
