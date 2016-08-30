package Chi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FrameSpecialScheduleManagementContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newMenu;
	private JMenuItem editMenu;
	private JMenuItem deleteMenu;
	
	public FrameSpecialScheduleManagementContextMenu (FrameSpecialScheduleManagement m) {
		this.newMenu=new JMenuItem("New...",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/ADD.png")),14,14));
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Cache.Actuators.updateWithWait() && Cache.DayScheduleRules.updateWithWait() && Cache.SpecialSchedules.updateWithWait() && Cache.RegularSchedules.updateWithWait() ) {
					DialogSpecialScheduleAddEdit diag=new DialogSpecialScheduleAddEdit();
					diag.setVisible(true);
				}
			}
		});
		
		this.editMenu=new JMenuItem("Edit...",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/EDIT.png")),14,14));
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Cache.Actuators.updateWithWait() && Cache.DayScheduleRules.updateWithWait() && Cache.SpecialSchedules.updateWithWait() && Cache.RegularSchedules.updateWithWait()) {
					Specialschedule s=m.getSelectedSchedule();
					DialogSpecialScheduleAddEdit diag=new DialogSpecialScheduleAddEdit(s.getSchedulename(),s.getActuator().getName(),s.getYear(),s.getMonth(),s.getDay(),s.getDayschedulerule().getRulename(),s.getActuatoron(),s.getPriority(),s.getEnabled());
					diag.setVisible(true);
				}
			}
		});
		
		this.deleteMenu=new JMenuItem("Delete...",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/DELETE.png")),14,14));
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete schedule "+m.getSelectedSchedule().getSchedulename()+"?","Delete schedule",JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
					WaitUI u=new WaitUI();
					u.setText("Deleting schedule");
					Thread t=new Thread() {
						public void run () {
							boolean flag=DatabaseSpecialSchedule.deleteSpecialSchedule(m.getSelectedSchedule().getSchedulename());
							if (!flag) {
								JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.WARNING_MESSAGE);
							}
							u.dispose();
						}
					};
					t.start();
					u.setVisible(true);
				}
			}
		});
		
		this.add(newMenu);
		this.add(editMenu);
		this.add(deleteMenu);
		this.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				editMenu.setEnabled(m.getSelectedRow()!=-1);
				deleteMenu.setEnabled(m.getSelectedRow()!=-1);
			};
		
		});

	}
	
}
