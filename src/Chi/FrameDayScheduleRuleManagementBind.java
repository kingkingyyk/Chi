package Chi;

public class FrameDayScheduleRuleManagementBind {

	public static class OnDayScheduleRuleCreate implements DatabaseDayScheduleRule.OnCreateAction {
		@Override
		public void run(String n, int sh, int sm, int eh, int em) {
			FrameDayScheduleRuleManagement.refresh();
		}
	}
	public static class OnDayScheduleRuleUpdate implements DatabaseDayScheduleRule.OnUpdateAction {
		@Override
		public void run(String oldN, String n, int sh, int sm, int eh, int em) {
			FrameDayScheduleRuleManagement.refresh();
		}
	}
	public static class OnDayScheduleRuleDelete implements DatabaseDayScheduleRule.OnDeleteAction {
		public void run (String sn) {
			FrameDayScheduleRuleManagement.refresh();
		}
	}
	
	public static void initialize() {
		DatabaseDayScheduleRule.registerOnCreateAction(new OnDayScheduleRuleCreate());
		DatabaseDayScheduleRule.registerOnUpdateAction(new OnDayScheduleRuleUpdate());
		DatabaseDayScheduleRule.registerOnDeleteAction(new OnDayScheduleRuleDelete());
	}
	
}
