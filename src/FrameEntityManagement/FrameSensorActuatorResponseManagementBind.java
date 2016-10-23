package FrameEntityManagement;

import Database.DatabaseSensorActuatorResponse;

public class FrameSensorActuatorResponseManagementBind {

	public static boolean OnResponseDeleteFireLock=false;
	
	public static class OnResponseCreate implements DatabaseSensorActuatorResponse.OnCreateAction {
		@Override
		public void run (int id, String an, String trigAct, String nTrigAct,
						String expression, boolean en, int timeout) {
			FrameSensorActuatorResponseManagement.refresh();
		}
	}
	public static class OnResponseUpdate implements DatabaseSensorActuatorResponse.OnUpdateAction {
		@Override
		public void run (int id, String an, String trigAct, String nTrigAct,
						String expression, boolean en, int timeout) {
			FrameSensorActuatorResponseManagement.refresh();
		}
	}
	public static class OnResponseDelete implements DatabaseSensorActuatorResponse.OnDeleteAction {
		public void run (int id) {
			if (!OnResponseDeleteFireLock) FrameSensorActuatorResponseManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseSensorActuatorResponse.registerOnCreateAction(new OnResponseCreate());
		DatabaseSensorActuatorResponse.registerOnUpdateAction(new OnResponseUpdate());
		DatabaseSensorActuatorResponse.registerOnDeleteAction(new OnResponseDelete());
	}
	
}
