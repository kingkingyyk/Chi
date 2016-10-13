package Chi;

import javax.swing.UIManager;

public class Chi {

	public static void main (String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		Logger.initialize();
		
		Logger.log("Loading theme started.");
		Theme.initialize();
		Logger.log("Loading theme done.");
		
		StartScreen diag=new StartScreen();
		diag.setProgBarMax(5);
		diag.setLocationRelativeTo(null);
		Thread t=new Thread() {
			public void run () {
				
				Logger.log("Initializing Utility class.");
				diag.setText("Loading utilities...");
				Utility.initialize();
				diag.setProgBarValue(1);
				
				Logger.log("Reading configuration started.");
				diag.setText("Loading configurations...");
				Config.initialize();
				diag.setProgBarValue(2);
				Logger.log("Reading configuration done.");
				
				Logger.log("Initializing Cache");
				diag.setText("Loading cache...");
				Cache.initialize(diag);
				DatabaseCassandra.initialize();
				diag.setProgBarValue(3);
				
				Logger.log("GUI & data binding started.");
				diag.setText("Binding GUI & Data...");
				//In topological order
				FrameUserManagementBind.initialize();
				FrameSiteManagementBind.initialize();
				FrameControllerManagementBind.initialize();
				FrameSensorClassManagementBind.initialize();
				FrameSensorManagementBind.initialize();
				FrameActuatorManagementBind.initialize();
				FrameDayScheduleRuleManagementBind.initialize();
				FrameRegularScheduleManagementBind.initialize();
				FrameSpecialScheduleManagementBind.initialize();
				FrameOngoingSchedulesBind.initialize();
				FrameNotificationBind.initialize();
				CacheRelationshipBind.initialize();
				
				diag.setProgBarValue(4);
				Logger.log("GUI & data binding done.");
				
				Logger.log("Data server to database started.");
				diag.setText("Preloading data server...");
				DataServerReadingToDatabase.initialize();
				diag.setProgBarValue(5);
				Logger.log("Data server to database done.");
				
				diag.setVisible(false);
			}
		}; t.start();
		diag.setVisible(true);
		
		Logger.log("MainUI started.");
		MenuUI ss=new MenuUI();
		ss.setLocationRelativeTo(null);
		Logger.log("MainUI done.");
		ss.setVisible(true);
		ss.setAlwaysOnTop(false);
	}
	
}
