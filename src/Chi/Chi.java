package Chi;

import javax.swing.UIManager;

import DataServer.DataServerReadingToDatabase;
import Database.Cache;
import Database.CacheRelationshipBind;
import Database.DatabaseCassandra;
import FrameEntityManagement.FrameActuatorManagementBind;
import FrameEntityManagement.FrameControllerManagementBind;
import FrameEntityManagement.FrameDayScheduleRuleManagementBind;
import FrameEntityManagement.FrameRegularScheduleManagementBind;
import FrameEntityManagement.FrameSensorActuatorResponseManagementBind;
import FrameEntityManagement.FrameSensorClassManagementBind;
import FrameEntityManagement.FrameSensorManagementBind;
import FrameEntityManagement.FrameSiteManagementBind;
import FrameEntityManagement.FrameSpecialScheduleManagementBind;
import FrameEntityManagement.FrameUserManagementBind;
import NotificationServer.FrameNotificationBind;
import SchedulingServer.FrameOngoingSchedulesBind;

public class Chi {

	public static void main (String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		Logger.initialize();
		
		Logger.log(Logger.LEVEL_INFO,"Loading theme started.");
		Theme.initialize();
		Logger.log(Logger.LEVEL_INFO,"Loading theme done.");
		
		StartScreen diag=new StartScreen();
		diag.setProgBarMax(5);
		diag.setLocationRelativeTo(null);
		Thread t=new Thread() {
			public void run () {
				
				Logger.log(Logger.LEVEL_INFO,"Initializing Utility class.");
				diag.setText("Loading utilities...");
				Utility.initialize();
				diag.setProgBarValue(1);
				
				Logger.log(Logger.LEVEL_INFO,"Reading configuration started.");
				diag.setText("Loading configurations...");
				Config.initialize();
				Logger.refreshLogLevel();
				diag.setProgBarValue(2);
				Logger.log(Logger.LEVEL_INFO,"Reading configuration done.");
				
				Logger.log(Logger.LEVEL_INFO,"Initializing Cache");
				diag.setText("Loading cache...");
				Cache.initialize(diag);
				DatabaseCassandra.initialize();
				diag.setProgBarValue(3);
				
				Logger.log(Logger.LEVEL_INFO,"GUI & data binding started.");
				diag.setText("Binding GUI & Data...");
				//In topological order
				CacheRelationshipBind.initialize();
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
				FrameSensorActuatorResponseManagementBind.initialize();
				FrameNotificationBind.initialize();
				
				diag.setProgBarValue(4);
				Logger.log(Logger.LEVEL_INFO,"GUI & data binding done.");
				
				Logger.log(Logger.LEVEL_INFO,"Data server to database started.");
				diag.setText("Preloading data server...");
				DataServerReadingToDatabase.initialize();
				diag.setProgBarValue(5);
				Logger.log(Logger.LEVEL_INFO,"Data server to database done.");
				
				diag.setVisible(false);
			}
		}; t.start();
		diag.setVisible(true);
		
		Logger.log(Logger.LEVEL_INFO,"MainUI started.");
		MenuUI ss=new MenuUI();
		ss.setLocationRelativeTo(null);
		Logger.log(Logger.LEVEL_INFO,"MainUI done.");
		ss.setVisible(true);
		ss.setAlwaysOnTop(false);
	}
	
}
