package Reading;

import Database.DatabaseController;
import Database.DatabaseSensor;
import Database.DatabaseSite;

public class FrameLatestReadingBind {

	public static boolean OnSensorDeleteFireLock=false;
	
	public static class OnSensorCreate implements DatabaseSensor.OnCreateAction {
		public void run (String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
			FrameLatestReading.refresh();
		}
	}
	public static class OnSensorUpdate implements DatabaseSensor.OnUpdateAction {
		public void run (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
			FrameLatestReading.refresh();
		}
	}
	public static class OnSensorDelete implements DatabaseSensor.OnDeleteAction {
		public void run (String sn) {
			if (!OnSensorDeleteFireLock) FrameLatestReading.refresh();
		}
	}
	
	public static class OnSiteCreate implements DatabaseSite.OnCreateAction {
		public void run (String name, String url) {
			FrameLatestReading.refresh();
		}
	}
	
	public static class OnSiteUpdate implements DatabaseSite.OnUpdateAction {
		public void run (String oldName, String name, String url) {
			FrameLatestReading.refresh();
		}
	}
	
	public static class OnSiteDelete implements DatabaseSite.OnDeleteAction {
		public void run (String name) {
			FrameLatestReading.refresh();
		}
	}
	
	public static class OnControllerCreate implements DatabaseController.OnCreateAction {
		public void run (String n, String s, double x, double y, int t) {
			FrameLatestReading.refresh();
		}
	}
	
	public static class OnControllerUpdate implements DatabaseController.OnUpdateAction {
		public void run (String oldName, String n, String s, double x, double y, int t) {
			FrameLatestReading.refresh();
		}
	}
	
	public static class OnControllerDelete implements DatabaseController.OnDeleteAction {
		public void run (String name) {
			FrameLatestReading.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseSensor.registerOnCreateAction(new OnSensorCreate());
		DatabaseSensor.registerOnUpdateAction(new OnSensorUpdate());
		DatabaseSensor.registerOnDeleteAction(new OnSensorDelete());
		
		DatabaseSite.registerOnCreateAction(new OnSiteCreate());
		DatabaseSite.registerOnUpdateAction(new OnSiteUpdate());
		DatabaseSite.registerOnDeleteAction(new OnSiteDelete());
		
		DatabaseController.registerOnCreateAction(new OnControllerCreate());
		DatabaseController.registerOnUpdateAction(new OnControllerUpdate());
		DatabaseController.registerOnDeleteAction(new OnControllerDelete());
	}
	
}
