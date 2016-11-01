package FrameEntityManagement;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.ArrayUtils;

import Chi.Config;
import Chi.Logger;
import Chi.WaitUI;
import ControllerPacket.ControllerPacketActuatorTrigger;
import Database.DatabaseActuator;
import DialogEntityManagement.DialogActuatorAddEdit;
import Entity.Actuator;

public class FrameActuatorManagementActions {

	public static void add() {
		DialogActuatorAddEdit diag=new DialogActuatorAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameActuatorManagement f) {
		Actuator act=f.getSelectedActuator();
		DialogActuatorAddEdit diag=new DialogActuatorAddEdit(act.getName(),act.getController().getControllername(),act.getStatuslist(),act.getPositionx(),act.getPositiony(),act.getControltype());
		diag.setVisible(true);
	}
	
	public static void delete(FrameActuatorManagement f) {
		StringBuilder sb=new StringBuilder();
		Actuator [] acts=f.getSelectedActuators();
		for (Actuator act : acts) {
			sb.append('\t');
			sb.append(act.getName()); 
			sb.append('\n');
		}
		if (JOptionPane.showConfirmDialog(null, "Delete the following actuator(s)?\n"+sb.toString()+"\nThe linked entities will be deleted too.","Delete site",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting actuator");
			u.setProgressBarMax(acts.length);
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameActuatorManagementBind.OnActuatorDeleteFireLock=true;
					for (int i=0;i<acts.length;i++) {
						flag&=DatabaseActuator.deleteActuator(acts[i].getName());
						u.setProgressBarValue(i+1);
					}
					FrameActuatorManagementBind.OnActuatorDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameActuatorManagement.refresh();
		}
	}
	
	public static void toggle (FrameActuatorManagement f) {
		Actuator act=f.getSelectedActuator();
		String status=act.getStatus();
		
		String [] actions=act.getStatuslist().split(";");
		if (status.equals("Pending Update")) status=actions[0];
		else status=actions[(ArrayUtils.indexOf(actions,status)+1)%actions.length];
			
		ControllerPacketActuatorTrigger p=new ControllerPacketActuatorTrigger(act.getController().getControllername(),act.getName(),status);
		p.trigger();
		
		FrameActuatorManagementFeedbackWait fa=new FrameActuatorManagementFeedbackWait();
		fa.setLocationRelativeTo(null);
		Thread t=new Thread() {
			public void run () {
				int wait=Config.CONTROLLER_READY_TIME_MS*(Config.CONTROLLER_MAX_RETRY+1);
				try { Thread.sleep(wait); } catch (InterruptedException e) {};
				if (fa!=null && fa.isVisible()) {
					Logger.log(Logger.LEVEL_ERROR,"FrameActuatorManagement - Communication Error : "+act.getController().getControllername()+" doesn't reply in "+wait/1000+" seconds!");
					fa.setVisible(false);
					JOptionPane.showMessageDialog(null,"Ops, looks like the controller has felt asleep."
													 + "\nYou might perform the following steps :"
													 + "\n  - Try toggling again"
													 + "\n  - Check the network connectivity"
													 + "\n  - Reset the data server"
													 + "\n  - Reset the controller","Actuator Management",JOptionPane.WARNING_MESSAGE);
				}
			}
		};
		t.start();
		fa.setVisible(true);
	}
}
