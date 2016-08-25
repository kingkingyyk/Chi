package Chi;

public class FrameSensorClassManagementBind {

	public static class OnSensorClassCreate implements DatabaseSensorClass.OnCreateAction {
		public void run (String name) {
			FrameSensorClassManagement.refresh();
		}
	}
	
	public static class OnSensorClassUpdate implements DatabaseSensorClass.OnUpdateAction {
		public void run (String oldName, String name) {
			FrameSensorClassManagement.refresh();
		}
	}
	
	public static class OnSensorClassDelete implements DatabaseSensorClass.OnDeleteAction {
		public void run (String name) {
			FrameSensorClassManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseSensorClass.registerOnCreateAction(new OnSensorClassCreate());
		DatabaseSensorClass.registerOnUpdateAction(new OnSensorClassUpdate());
		DatabaseSensorClass.registerOnDeleteAction(new OnSensorClassDelete());
	}
	
}
