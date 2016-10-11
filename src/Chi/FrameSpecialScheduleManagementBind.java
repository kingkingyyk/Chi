package Chi;

public class FrameSpecialScheduleManagementBind {

	public static class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String u, double px, double py, String ctrlType) {
			FrameSpecialScheduleManagement.refresh();
		}
	}
	public static class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String sn) {
			FrameSpecialScheduleManagement.refresh();
		}
	}
	
	public static class OnDayScheduleRuleUpdate implements DatabaseDayScheduleRule.OnUpdateAction {
		@Override
		public void run(String oldN, String n, int sh, int sm, int eh, int em) {
			FrameSpecialScheduleManagement.refresh();
		}
	}
	
	public static class OnDayScheduleRuleDelete implements DatabaseDayScheduleRule.OnDeleteAction {
		public void run (String name) {
			FrameSpecialScheduleManagement.refresh();
		}
	}
	
	public static class OnSpecialScheduleCreate implements DatabaseSpecialSchedule.OnCreateAction {
		@Override
		public void run(String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean flag, int pr, boolean en) {
			FrameSpecialScheduleManagement.refresh();
		}
	}
	
	public static class OnSpecialScheduleUpdate implements DatabaseSpecialSchedule.OnUpdateAction {
		@Override
		public void run(String oldSN, String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean flag, int pr,boolean en) {
			FrameSpecialScheduleManagement.refresh();
		}
	}

	
	public static class OnSpecialScheduleDelete implements DatabaseSpecialSchedule.OnDeleteAction {
		public void run (String name) {
			FrameSpecialScheduleManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseActuator.registerOnUpdateAction(new OnActuatorUpdate());
		DatabaseActuator.registerOnDeleteAction(new OnActuatorDelete());

		DatabaseDayScheduleRule.registerOnUpdateAction(new OnDayScheduleRuleUpdate());
		DatabaseDayScheduleRule.registerOnDeleteAction(new OnDayScheduleRuleDelete());
		
		DatabaseSpecialSchedule.registerOnCreateAction(new OnSpecialScheduleCreate());
		DatabaseSpecialSchedule.registerOnUpdateAction(new OnSpecialScheduleUpdate());
		DatabaseSpecialSchedule.registerOnDeleteAction(new OnSpecialScheduleDelete());
	}
	
}
