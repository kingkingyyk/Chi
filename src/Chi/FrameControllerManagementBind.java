package Chi;

public class FrameControllerManagementBind {

	public static class OnControllerCreate implements DatabaseController.OnCreateAction {
		public void run (String n, String s, double x, double y, int t) {
			FrameControllerManagement.refresh();
		}
	}
	
	public static class OnControllerUpdate implements DatabaseController.OnUpdateAction {
		public void run (String oldName, String n, String s, double x, double y, int t) {
			FrameControllerManagement.refresh();
		}
	}
	
	public static class OnControllerDelete implements DatabaseController.OnDeleteAction {
		public void run (String name) {
			FrameControllerManagement.refresh();
		}
	}
	
	public static class OnSiteCreate implements DatabaseSite.OnCreateAction {
		public void run (String name, String url) {
			FrameControllerManagement.refresh();
		}
	}
	
	public static class OnSiteUpdate implements DatabaseSite.OnUpdateAction {
		public void run (String oldName, String name, String url) {
			FrameControllerManagement.refresh();
		}
	}
	
	public static class OnSiteDelete implements DatabaseSite.OnDeleteAction {
		public void run (String name) {
			FrameControllerManagement.refresh();
		}
	}

	public static void initialize() {
		DatabaseController.registerOnCreateAction(new OnControllerCreate());
		DatabaseController.registerOnUpdateAction(new OnControllerUpdate());
		DatabaseController.registerOnDeleteAction(new OnControllerDelete());
		
		DatabaseSite.registerOnCreateAction(new OnSiteCreate());
		DatabaseSite.registerOnUpdateAction(new OnSiteUpdate());
		DatabaseSite.registerOnDeleteAction(new OnSiteDelete());
	}
	
}
