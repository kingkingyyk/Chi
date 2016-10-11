package Chi;

public class FrameActuatorManagementBind {

	public static class OnActuatorCreate implements DatabaseActuator.OnCreateAction {
		@Override
		public void run(String n, String u, double px, double py) {
			FrameActuatorManagement.refresh();
		}
	}
	public static class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String u, double px, double py, String ctrlType) {
			FrameActuatorManagement.refresh();
		}
	}
	public static class OnActuatorUpdateStatus implements DatabaseActuator.OnUpdateStatusAction {
		@Override
		public void run(String n, String s) {
			FrameActuatorManagement.refresh();
		}
	}
	public static class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String sn) {
			FrameActuatorManagement.refresh();
		}
	}
	
	public static class OnControllerCreate implements DatabaseController.OnCreateAction {
		public void run (String n, String s, double x, double y, int t) {
			FrameActuatorManagement.refresh();
		}
	}
	
	public static class OnControllerUpdate implements DatabaseController.OnUpdateAction {
		public void run (String oldName, String n, String s, double x, double y, int t) {
			FrameActuatorManagement.refresh();
		}
	}
	
	public static class OnControllerDelete implements DatabaseController.OnDeleteAction {
		public void run (String name) {
			FrameActuatorManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseActuator.registerOnCreateAction(new OnActuatorCreate());
		DatabaseActuator.registerOnUpdateAction(new OnActuatorUpdate());
		DatabaseActuator.registerOnUpdateStatusAction(new OnActuatorUpdateStatus());
		DatabaseActuator.registerOnDeleteAction(new OnActuatorDelete());

		DatabaseController.registerOnCreateAction(new OnControllerCreate());
		DatabaseController.registerOnUpdateAction(new OnControllerUpdate());
		DatabaseController.registerOnDeleteAction(new OnControllerDelete());
	}
	
}
