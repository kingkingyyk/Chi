package Chi;

public class FrameRegularScheduleManagementBind {

	public static class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String u, double px, double py, String ctrlType) {
			FrameRegularScheduleManagement.refresh();
		}
	}
	public static class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String sn) {
			FrameRegularScheduleManagement.refresh();
		}
	}
	
	public static class OnDayScheduleRuleUpdate implements DatabaseDayScheduleRule.OnUpdateAction {
		@Override
		public void run(String oldN, String n, int sh, int sm, int eh, int em) {
			FrameRegularScheduleManagement.refresh();
		}
	}
	
	public static class OnDayScheduleRuleDelete implements DatabaseDayScheduleRule.OnDeleteAction {
		public void run (String name) {
			FrameRegularScheduleManagement.refresh();
		}
	}
	
	public static class OnRegularScheduleCreate implements DatabaseRegularSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
			FrameRegularScheduleManagement.refresh();
		}
	}
	
	public static class OnRegularScheduleUpdate implements DatabaseRegularSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
			FrameRegularScheduleManagement.refresh();
		}
	}

	
	public static class OnRegularScheduleDelete implements DatabaseRegularSchedule.OnDeleteAction {
		public void run (String name) {
			FrameRegularScheduleManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseActuator.registerOnUpdateAction(new OnActuatorUpdate());
		DatabaseActuator.registerOnDeleteAction(new OnActuatorDelete());

		DatabaseDayScheduleRule.registerOnUpdateAction(new OnDayScheduleRuleUpdate());
		DatabaseDayScheduleRule.registerOnDeleteAction(new OnDayScheduleRuleDelete());
		
		DatabaseRegularSchedule.registerOnCreateAction(new OnRegularScheduleCreate());
		DatabaseRegularSchedule.registerOnUpdateAction(new OnRegularScheduleUpdate());
		DatabaseRegularSchedule.registerOnDeleteAction(new OnRegularScheduleDelete());
	}
	
}
