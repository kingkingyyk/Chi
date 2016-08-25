package Chi;

public class FrameSiteManagementBind {

	public static class OnSiteCreate implements DatabaseSite.OnCreateAction {
		public void run (String name, String url) {
			FrameSiteManagement.refresh();
		}
	}
	
	public static class OnSiteUpdate implements DatabaseSite.OnUpdateAction {
		public void run (String oldName, String name, String url) {
			FrameSiteManagement.refresh();
		}
	}
	
	public static class OnSiteDelete implements DatabaseSite.OnDeleteAction {
		public void run (String name) {
			FrameSiteManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseSite.registerOnCreateAction(new OnSiteCreate());
		DatabaseSite.registerOnUpdateAction(new OnSiteUpdate());
		DatabaseSite.registerOnDeleteAction(new OnSiteDelete());
	}
	
}
