package FrameEntityManagement;

import javax.swing.JOptionPane;
import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseSpecialSchedule;
import DialogEntityManagement.DialogSpecialScheduleAddEdit;
import Entity.Specialschedule;

public class FrameSpecialScheduleManagementActions {

	public static void add() {
		DialogSpecialScheduleAddEdit diag=new DialogSpecialScheduleAddEdit();
		diag.setVisible(true);
	}
	
	public static void toggle(FrameSpecialScheduleManagement m) {
		StringBuilder sb=new StringBuilder("Operation failed on the following schedules : \n");
		boolean failFlag=false;
		for (Specialschedule rs : m.getSelectedSchedules()) {
			boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(rs.getSchedulename(), rs.getSchedulename(),
														 rs.getActuator().getName(),rs.getYear(),rs.getMonth(),rs.getDay(),
														 rs.getDayschedulerule().getRulename(),
														 rs.getOnstartaction(),rs.getOnendaction(),
														 rs.getLockmanual(), rs.getPriority(), !rs.getEnabled());
			failFlag=failFlag || !flag;
			if (flag) {
				sb.append(rs.getSchedulename());
				sb.append('\n');
			}
		}
		if (failFlag) JOptionPane.showMessageDialog(m,sb.toString(),"Toggle status",JOptionPane.ERROR_MESSAGE);
	}
	
	public static void edit(FrameSpecialScheduleManagement m) {
		Specialschedule s=m.getSelectedSchedule();
		DialogSpecialScheduleAddEdit diag=new DialogSpecialScheduleAddEdit(s.getSchedulename(),s.getActuator().getName(),s.getYear(),s.getMonth(),s.getDay(),s.getDayschedulerule().getRulename(),s.getOnstartaction(),s.getOnendaction(),s.getLockmanual(),s.getPriority(),s.getEnabled());
		diag.setVisible(true);
	}
	
	public static void delete(FrameSpecialScheduleManagement m) {
		Specialschedule ss []=m.getSelectedSchedules();
		StringBuilder sb=new StringBuilder();
		for (Specialschedule s : ss) {
			sb.append("\t");
			sb.append(s.getSchedulename()); 
			sb.append('\n');
		}
		
		if (JOptionPane.showConfirmDialog(null, "Delete the following schedules?\n"+sb.toString()+"\nThe linked entities will be deleted too.","Delete special schedule",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting schedule");
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameSpecialScheduleManagementBind.OnScheduleDeleteFireLock=true;
					for (int i=0;i<ss.length;i++) {
						flag&=DatabaseSpecialSchedule.deleteSpecialSchedule(ss[i].getSchedulename());
						u.setProgressBarValue(i+1);
					}
					FrameSpecialScheduleManagementBind.OnScheduleDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameSpecialScheduleManagement.refresh();
		}
	}
}
