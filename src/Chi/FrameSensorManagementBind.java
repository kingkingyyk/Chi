package Chi;

public class FrameSensorManagementBind {

	public static class OnSensorCreate implements DatabaseSensor.OnCreateAction {
		public void run (String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
			FrameSensorManagement.refresh();
		}
	}
	public static class OnSensorUpdate implements DatabaseSensor.OnUpdateAction {
		public void run (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
			FrameSensorManagement.refresh();
		}
	}
	public static class OnSensorDelete implements DatabaseSensor.OnDeleteAction {
		public void run (String sn) {
			FrameSensorManagement.refresh();
		}
	}
	
	public static class OnSensorClassCreate implements DatabaseSensorClass.OnCreateAction {
		public void run (String name) {
			FrameSensorManagement.refresh();
		}
	}
	
	public static class OnSensorClassUpdate implements DatabaseSensorClass.OnUpdateAction {
		public void run (String oldName, String name) {
			FrameSensorManagement.refresh();
		}
	}
	
	public static class OnSensorClassDelete implements DatabaseSensorClass.OnDeleteAction {
		public void run (String name) {
			FrameSensorManagement.refresh();
		}
	}
	
	public static class OnControllerCreate implements DatabaseController.OnCreateAction {
		public void run (String n, String s, double x, double y, int t) {
			FrameSensorManagement.refresh();
		}
	}
	
	public static class OnControllerUpdate implements DatabaseController.OnUpdateAction {
		public void run (String oldName, String n, String s, double x, double y, int t) {
			FrameSensorManagement.refresh();
		}
	}
	
	public static class OnControllerDelete implements DatabaseController.OnDeleteAction {
		public void run (String name) {
			FrameSensorManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseSensor.registerOnCreateAction(new OnSensorCreate());
		DatabaseSensor.registerOnUpdateAction(new OnSensorUpdate());
		DatabaseSensor.registerOnDeleteAction(new OnSensorDelete());
		
		DatabaseSensorClass.registerOnCreateAction(new OnSensorClassCreate());
		DatabaseSensorClass.registerOnUpdateAction(new OnSensorClassUpdate());
		DatabaseSensorClass.registerOnDeleteAction(new OnSensorClassDelete());
		
		DatabaseController.registerOnCreateAction(new OnControllerCreate());
		DatabaseController.registerOnUpdateAction(new OnControllerUpdate());
		DatabaseController.registerOnDeleteAction(new OnControllerDelete());
	}
	
}
