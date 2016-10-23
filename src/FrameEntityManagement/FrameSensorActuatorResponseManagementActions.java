package FrameEntityManagement;

import javax.swing.JOptionPane;
import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseSensorActuatorResponse;
import DialogEntityManagement.DialogSensorActuatorResponseAddEdit;
import Entity.Sensoractuatorresponse;

public class FrameSensorActuatorResponseManagementActions {

	public static void add() {
		DialogSensorActuatorResponseAddEdit diag=new DialogSensorActuatorResponseAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameSensorActuatorResponseManagement m) {
		Sensoractuatorresponse res =m.getSelectedResponse();
		DialogSensorActuatorResponseAddEdit diag=new DialogSensorActuatorResponseAddEdit(res.getId(),res.getActuator().getName(),res.getOntriggeraction(),
																						res.getOnnottriggeraction(),res.getExpression(),res.getEnabled(),
																						res.getTimeout());
		diag.setVisible(true);
	}
	
	public static void delete(FrameSensorActuatorResponseManagement m) {
		Sensoractuatorresponse sars []=m.getSelectedResponses();
		StringBuilder sb=new StringBuilder();
		for (Sensoractuatorresponse sar : sars) {
			sb.append("\t#");
			sb.append(sar.getId());
			sb.append(" - ");
			sb.append(sar.getActuator().getName()); 
			sb.append('\n');
		}
		
		if (JOptionPane.showConfirmDialog(null, "Delete the following responses?\n"+sb.toString(),"Delete response",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting response...");
			u.setProgressBarMax(sars.length);
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameSensorActuatorResponseManagementBind.OnResponseDeleteFireLock=true;
					for (int i=0;i<sars.length;i++) {
						flag&=DatabaseSensorActuatorResponse.deleteSensorActuatorResponse(sars[i].getId());
						u.setProgressBarValue(i+1);
					}
					FrameSensorActuatorResponseManagementBind.OnResponseDeleteFireLock=false;
					
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameSensorActuatorResponseManagement.refresh();
		}
	}
}
