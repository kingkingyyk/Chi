package FrameEntityManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FrameDayScheduleRuleManagementContextMenu extends FrameContextMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newMenu;
	private JMenuItem editMenu;
	private JMenuItem deleteMenu;
	
	public FrameDayScheduleRuleManagementContextMenu (FrameDayScheduleRuleManagement m) {
		this.newMenu=addMenuItem("New...","AddIcon","INSERT");
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameDayScheduleRuleManagementActions.add();}
		});
		
		this.editMenu=addMenuItem("Edit...","EditIcon","ENTER");
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameDayScheduleRuleManagementActions.edit(m);}
		});
		
		this.deleteMenu=addMenuItem("Delete...","DeleteIcon","DELETE");
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameDayScheduleRuleManagementActions.delete(m);}
		});
		
		this.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				editMenu.setEnabled(m.getSelectedCount()==1);
				deleteMenu.setEnabled(m.getSelectedCount()>0);
			};
		
		});

	}
	
}
