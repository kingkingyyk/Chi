package FrameEntityManagement;

import javax.swing.JOptionPane;
import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseUser;
import DialogEntityManagement.DialogUserAddEdit;
import Entity.User;

public class FrameUserManagementActions {

	public static void add() {
		DialogUserAddEdit diag=new DialogUserAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameUserManagement m) {
		User u=m.getSelectedUser();
		DialogUserAddEdit diag=new DialogUserAddEdit(u.getUsername(),u.getLevel(),u.getStatus());
		diag.setVisible(true);
	}
	
	public static void delete(FrameUserManagement m) {
		User ss []=m.getSelectedUsers();
		StringBuilder sb=new StringBuilder();
		for (User s : ss) {
			sb.append("\t");
			sb.append(s.getUsername()); 
			sb.append('\n');
		}
		if (JOptionPane.showConfirmDialog(null, "Delete the following users?\n"+sb.toString()+"\nThe linked entities will be deleted too.","Delete sensor",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting sensor");
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameUserManagementBind.OnUserDeleteFireLock=true;
					for (int i=0;i<ss.length;i++) {
						flag&=DatabaseUser.deleteUser(ss[i].getUsername());
						u.setProgressBarValue(i+1);
					}
					FrameUserManagementBind.OnUserDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameUserManagement.refresh();
		}
	}
}
