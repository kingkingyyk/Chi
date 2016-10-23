package FrameEntityManagement;

import javax.swing.JOptionPane;
import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseSensorClass;
import DialogEntityManagement.DialogSensorClassAddEdit;
import Entity.Sensorclass;

public class FrameSensorClassManagementActions {

	public static void add() {
		DialogSensorClassAddEdit diag=new DialogSensorClassAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameSensorClassManagement m) {
		DialogSensorClassAddEdit diag=new DialogSensorClassAddEdit(m.getSelectedClass().getClassname());
		diag.setVisible(true);
	}
	
	public static void delete(FrameSensorClassManagement m) {
		Sensorclass scs []=m.getSelectedClasses();
		StringBuilder sb=new StringBuilder();
		for (Sensorclass sc : scs) {
			sb.append("\t");
			sb.append(sc.getClassname()); 
			sb.append('\n');
		}
		
		if (JOptionPane.showConfirmDialog(null, "Delete sensor class?\n"+sb.toString()+"\nThe linked entities will be deleted too.","Delete sensor class",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting sensor class...");
			u.setProgressBarMax(scs.length);
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameSensorClassManagementBind.OnSensorClassDeleteFireLock=true;
					for (int i=0;i<scs.length;i++) {
						flag&=DatabaseSensorClass.deleteSensorClass(scs[i].getClassname());
						u.setProgressBarValue(i+1);
					}
					FrameSensorClassManagementBind.OnSensorClassDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameSensorClassManagement.refresh();
		}
	}
}
