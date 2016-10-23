package Chi;

import Database.DatabaseController;
import Database.DatabaseEvent;
import Database.DatabaseSensor;

public class FrameNotificationBind {

	public static class OnSensorUpdate implements DatabaseSensor.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String cn, double min, double max, double trans, String unit,
				String con, double minT, double maxT, double px, double py) {
			FrameNotification.refresh();
		}
	}
	public static class OnControllerUpdate implements DatabaseController.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String s, double x, double y, int t) {
			FrameNotification.refresh();
		}
	}
	
	public static class OnSensorEventCreated implements DatabaseEvent.OnSensorEventLoggedAction {
		@Override
		public void run(String name, String eventType, String eventValue) {
			FrameNotification.refresh();
		}
	}
	
	public static class OnControllerEventCreated implements DatabaseEvent.OnControllerEventLoggedAction {
		@Override
		public void run(String name, String eventType, String eventValue) {
			FrameNotification.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseSensor.registerOnUpdateAction(new OnSensorUpdate());
		DatabaseController.registerOnUpdateAction(new OnControllerUpdate());
		
		DatabaseEvent.registerOnSensorEventLoggedAction(new OnSensorEventCreated());
		DatabaseEvent.registerOnControllerEventLoggedAction(new OnControllerEventCreated());
	}
	
}
