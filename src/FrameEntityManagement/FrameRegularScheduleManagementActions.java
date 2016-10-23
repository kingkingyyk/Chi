package FrameEntityManagement;

import javax.swing.JOptionPane;
import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseRegularSchedule;
import DialogEntityManagement.DialogRegularScheduleAddEdit;
import Entity.Regularschedule;

public class FrameRegularScheduleManagementActions {

	public static void add() {
		DialogRegularScheduleAddEdit diag=new DialogRegularScheduleAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameRegularScheduleManagement m) {
		Regularschedule r=m.getSelectedSchedule();
		DialogRegularScheduleAddEdit diag=new DialogRegularScheduleAddEdit(r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		diag.setVisible(true);
	}
	
	public static void delete(FrameRegularScheduleManagement m) {
		Regularschedule rs []=m.getSelectedSchedules();
		StringBuilder sb=new StringBuilder();
		for (Regularschedule r : rs) {
			sb.append('\t');
			sb.append(r.getSchedulename()); 
			sb.append('\n');
		}
		if (JOptionPane.showConfirmDialog(null, "Delete the following schedules?\n"+sb.toString(),"Delete schedule",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting schedule");
			u.setProgressBarMax(rs.length);
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameRegularScheduleManagementBind.OnRegularScheduleDeleteFireLock=true;
					for (int i=0;i<rs.length;i++) {
						flag&=DatabaseRegularSchedule.deleteRegularSchedule(rs[i].getSchedulename());
						u.setProgressBarValue(i+1);
					}
					FrameRegularScheduleManagementBind.OnRegularScheduleDeleteFireLock=false;
					if (!flag) JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameRegularScheduleManagement.refresh();
		}
	}
}
