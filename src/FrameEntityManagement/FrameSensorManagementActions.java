package FrameEntityManagement;

import javax.swing.JOptionPane;
import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseSensor;
import DialogEntityManagement.DialogSensorAddEdit;
import Entity.Sensor;

public class FrameSensorManagementActions {

	public static void add() {
		DialogSensorAddEdit diag=new DialogSensorAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameSensorManagement m) {
		Sensor s=m.getSelectedSensor();
		DialogSensorAddEdit diag=new DialogSensorAddEdit(s.getSensorname(),s.getSensorclass().getClassname(),s.getMinvalue(),s.getMaxvalue(),s.getTransformationfactor(),s.getUnit(),s.getController().getControllername(),s.getMinthreshold(),s.getMaxthreshold(),s.getPositionx(),s.getPositiony());
		diag.setVisible(true);
	}
	
	public static void delete(FrameSensorManagement m) {
		Sensor ss []=m.getSelectedSensors();
		StringBuilder sb=new StringBuilder();
		for (Sensor s : ss) {
			sb.append("\t");
			sb.append(s.getSensorname()); 
			sb.append('\n');
		}
		
		if (JOptionPane.showConfirmDialog(null, "Delete the following sensors?\n"+sb.toString()+"\nThe linked entities will be deleted too.","Delete sensor",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting sensor");
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameSensorManagementBind.OnSensorDeleteFireLock=true;
					for (int i=0;i<ss.length;i++) {
						flag&=DatabaseSensor.deleteSensor(ss[i].getSensorname());
						u.setProgressBarValue(i+1);
					}
					FrameSensorManagementBind.OnSensorDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameSensorManagement.refresh();
		}
	}
}
