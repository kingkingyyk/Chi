package Chi;

import javax.swing.UIManager;

public class Chi {

	public static void main (String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		Logger.log("Reading configuration started.");
		Config.initialize();
		Logger.log("Reading configuration done.");
		
		Logger.log("Loading theme started.");
		Theme.initialize();
		Logger.log("Loading theme done.");
		
		Logger.log("Splash screen started.");
		SplashScreen ss=new SplashScreen();
		ss.setLocationRelativeTo(null);
		Logger.log("Splash screen done.");
		ss.setVisible(true);
		Logger.log("Splash screen closed.");
		
		Logger.log("User database started.");
		DatabaseUser.initialize();
		Logger.log("User database closed.");
	}
	
}
