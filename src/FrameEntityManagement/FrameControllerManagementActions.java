package FrameEntityManagement;

import javax.swing.JOptionPane;

import Chi.Config;
import Chi.WaitUI;
import ControllerPacket.ControllerPacketRequestAlive;
import Database.DatabaseController;
import DialogEntityManagement.DialogControllerAddEdit;
import Entity.Controller;

public class FrameControllerManagementActions {

	public static void add() {
		DialogControllerAddEdit diag=new DialogControllerAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameControllerManagement f) {
		Controller c=f.getSelectedController();
		DialogControllerAddEdit diag=new DialogControllerAddEdit(c.getControllername(),c.getSite().getSitename(),c.getPositionx(),c.getPositiony(),c.getReporttimeout());
		diag.setVisible(true);
	}
	
	public static void delete(FrameControllerManagement f) {
		Controller [] ctrls=f.getSelectedControllers();
		StringBuilder sb=new StringBuilder();
		for (Controller ctrl : ctrls) {
			sb.append('\t');
			sb.append(ctrl.getControllername()); 
			sb.append('\n');
		}
		if (JOptionPane.showConfirmDialog(null, "Delete the following controllers?\n"+sb.toString()+"\nThe linked entities will be deleted too.","Delete controller",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting controller...");
			u.setProgressBarMax(ctrls.length);
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameControllerManagementBind.OnControllerDeleteFireLock=true;
					for (int i=0;i<ctrls.length;i++) {
						flag&=DatabaseController.deleteController(ctrls[i].getControllername());
						u.setProgressBarValue(i+1);
					}
					FrameControllerManagementBind.OnControllerDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameControllerManagement.refresh();
		}
	}
	
	public static void forceReport (FrameControllerManagement f) {
		ControllerPacketRequestAlive p=new ControllerPacketRequestAlive(f.getSelectedController().getControllername());
		if (p.send()) {
			JOptionPane.showMessageDialog(null,"Request sent successfully.\nThe last report time will be updated once the controller has replied","Force report alive",JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
