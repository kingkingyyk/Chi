package FrameEntityManagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import SchedulingServer.SchedulingServer;

public class FrameActuatorManagementContextMenu extends FrameContextMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newMenu;
	private JMenuItem toggleMenu;
	private JMenuItem editMenu;
	private JMenuItem deleteMenu;
	
	public FrameActuatorManagementContextMenu (FrameActuatorManagement m) {
		super();
		
		this.newMenu=addMenuItem("New...","AddIcon","INSERT");
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameActuatorManagementActions.add();}
		});
		
		this.toggleMenu=addMenuItem("Toggle Status","ActuatorIcon","SPACE");
		toggleMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameActuatorManagementActions.toggle(m);}
		});
		
		this.editMenu=addMenuItem("Edit...","EditIcon","ENTER");
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameActuatorManagementActions.edit(m);}
		});
		
		this.deleteMenu=addMenuItem("Delete...","DeleteIcon","DELETE");
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {FrameActuatorManagementActions.delete(m);}
		});
		
		
		this.addPopupMenuListener(new PopupMenuListener() {
			@Override public void popupMenuCanceled(PopupMenuEvent arg0) {}
			@Override public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				toggleMenu.setEnabled(m.getSelectedCount()==1 && !SchedulingServer.isActuatorLocked(m.getSelectedActuator().getName()));
				editMenu.setEnabled(m.getSelectedCount()==1);
				deleteMenu.setEnabled(m.getSelectedCount()>0);
			};
		
		});

	}
	
}
