package FrameEntityManagement;

import Database.DatabaseUser;

public class FrameUserManagementBind {
	
	public static boolean OnUserDeleteFireLock=false;
	
	public static class OnUserCreate implements DatabaseUser.OnCreateAction {
		public void run (String user, int lvl, String status) {
			FrameUserManagement.refresh();
		}
	}
	
	public static class OnUserUpdate implements DatabaseUser.OnUpdateAction {
		public void run (String user, int lvl, String status) {
			FrameUserManagement.refresh();
		}
	}
	
	public static class OnUserDelete implements DatabaseUser.OnDeleteAction {
		public void run (String user) {
			if (!OnUserDeleteFireLock) FrameUserManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseUser.registerOnCreateAction(new OnUserCreate());
		DatabaseUser.registerOnUpdateAction(new OnUserUpdate());
		DatabaseUser.registerOnDeleteAction(new OnUserDelete());
	}
}
