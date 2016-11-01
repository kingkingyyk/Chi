package Chi;

import Database.DatabaseController;
import Database.DatabaseEvent;
import Database.DatabaseSensor;
import Database.DatabaseActuator;

public class FrameNotificationBind {

	//=========
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
	public static class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String u, String slist, double px, double py, String ctrlType) {
			FrameNotification.refresh();
		}
	}
	//===========
	public static class OnSensorDelete implements DatabaseSensor.OnDeleteAction {
		@Override
		public void run(String sn) {
			FrameNotification.refresh();
		}
	}
	public static class OnControllerDelete implements DatabaseController.OnDeleteAction {
		@Override
		public void run(String n) {
			FrameNotification.refresh();
		}
	}
	public static class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		@Override
		public void run(String n) {
			FrameNotification.refresh();
		}
	}
	
	//===========
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
	
	public static class OnActuatorEventCreated implements DatabaseEvent.OnActuatorEventLoggedAction {
		@Override
		public void run(String name, String eventType, String eventValue) {
			FrameNotification.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseSensor.registerOnUpdateAction(new OnSensorUpdate());
		DatabaseController.registerOnUpdateAction(new OnControllerUpdate());
		DatabaseActuator.registerOnUpdateAction(new OnActuatorUpdate());
		
		DatabaseSensor.registerOnDeleteAction(new OnSensorDelete());
		DatabaseController.registerOnDeleteAction(new OnControllerDelete());
		DatabaseActuator.registerOnDeleteAction(new OnActuatorDelete());
		
		DatabaseEvent.registerOnSensorEventLoggedAction(new OnSensorEventCreated());
		DatabaseEvent.registerOnControllerEventLoggedAction(new OnControllerEventCreated());
		DatabaseEvent.registerOnActuatorEventLoggedAction(new OnActuatorEventCreated());
	}
	
}
