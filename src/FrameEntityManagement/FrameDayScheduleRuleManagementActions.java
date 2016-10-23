package FrameEntityManagement;

import javax.swing.JOptionPane;

import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseDayScheduleRule;
import DialogEntityManagement.DialogDayScheduleRuleAddEdit;
import Entity.Dayschedulerule;

public class FrameDayScheduleRuleManagementActions {

	public static void add() {
		DialogDayScheduleRuleAddEdit diag=new DialogDayScheduleRuleAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameDayScheduleRuleManagement m) {
		Dayschedulerule r=m.getSelectedRule();
		DialogDayScheduleRuleAddEdit diag=new DialogDayScheduleRuleAddEdit(r.getRulename(),r.getStarthour(),r.getStartminute(),r.getEndhour(),r.getEndminute());
		diag.setVisible(true);
	}
	
	public static void delete(FrameDayScheduleRuleManagement m) {
		Dayschedulerule [] rs=m.getSelectedRules();
		StringBuilder sb=new StringBuilder();
		for (Dayschedulerule r : rs) {
			sb.append('\t');
			sb.append(r.getRulename()); 
			sb.append('\n');
		}
		if (JOptionPane.showConfirmDialog(null, "Delete the following rules?\n"+sb.toString()+"\nLinked schedules will be removed too.","Delete rule",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting rule...");
			u.setProgressBarMax(rs.length);
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameDayScheduleRuleManagementBind.OnDayScheduleRuleDeleteFireLock=true;
					for (int i=0;i<rs.length;i++) {
						flag&=DatabaseDayScheduleRule.deleteDayScheduleRule(rs[i].getRulename());
						u.setProgressBarValue(i+1);
					}
					FrameDayScheduleRuleManagementBind.OnDayScheduleRuleDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameDayScheduleRuleManagement.refresh();
		}
	}
}
