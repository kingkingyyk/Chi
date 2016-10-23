package FrameEntityManagement;

import javax.swing.JOptionPane;
import Chi.Config;
import Chi.WaitUI;
import Database.DatabaseSite;
import DialogEntityManagement.DialogSiteAddEdit;
import Entity.Site;

public class FrameSiteManagementActions {

	public static void add() {
		DialogSiteAddEdit diag=new DialogSiteAddEdit();
		diag.setVisible(true);
	}
	
	public static void edit(FrameSiteManagement m) {
		Site s=m.getSelectedSite();
		DialogSiteAddEdit diag=new DialogSiteAddEdit(s.getSitename(),s.getSitemapurl());
		diag.setVisible(true);
	}
	
	public static void delete(FrameSiteManagement m) {
		Site ss []=m.getSelectedSites();
		StringBuilder sb=new StringBuilder();
		for (Site s : ss) {
			sb.append("\t");
			sb.append(s.getSitename()); 
			sb.append('\n');
		}
		
		if (JOptionPane.showConfirmDialog(null, "Delete the following sites?\n"+sb.toString()+"\nThe linked entities will be deleted too.","Delete sensor",JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
			WaitUI u=new WaitUI();
			u.setText("Deleting site");
			Thread t=new Thread() {
				public void run () {
					boolean flag=true;
					FrameSiteManagementBind.OnSiteDeleteFireLock=true;
					for (int i=0;i<ss.length;i++) {
						flag&=DatabaseSite.deleteSite(ss[i].getSitename());
						u.setProgressBarValue(i+1);
					}
					FrameSiteManagementBind.OnSiteDeleteFireLock=false;
					if (!flag) {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
					}
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			FrameSiteManagement.refresh();
		}
	}
}
