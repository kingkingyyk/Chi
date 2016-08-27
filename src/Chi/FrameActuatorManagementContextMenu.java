package Chi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FrameActuatorManagementContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newMenu;
	private JMenuItem toggleMenu;
	private JMenuItem editMenu;
	private JMenuItem deleteMenu;
	
	public FrameActuatorManagementContextMenu (FrameActuatorManagement m) {
		this.newMenu=new JMenuItem("New...",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/ADD.png")),14,14));
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Cache.updateController() & Cache.updateActuator()) {
					DialogActuatorAddEdit diag=new DialogActuatorAddEdit();
					diag.setVisible(true);
				}
			}
		});
		
		this.toggleMenu=new JMenuItem("Toggle Status",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/ACTUATOR.png")),14,14));
		toggleMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Cache.updateController() & Cache.updateActuator()) {
					Object [] o=m.getSelectedObj();
					String aname=(String)o[0];
					String cname=(String)o[1];
					String status=(String)o[2];
					if (!status.equals("Pending Update")) {
						if (status.equals("Pending Update")) status="OFF";
						else status="ON";
						
						ControllerPacketActuatorTrigger p=new ControllerPacketActuatorTrigger(cname,aname,status);
						p.trigger();
						JOptionPane.showMessageDialog(null,"Request sent successfully.\nThe status will be updated once the controller has replied.","Toggle Status",JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null,"Looks like the actuator has not registered itself to the server.","Toggle Status",JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		this.editMenu=new JMenuItem("Edit...",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/EDIT.png")),14,14));
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (Cache.updateController() & Cache.updateActuator()) {
					Object [] o=m.getSelectedObj();
					DialogActuatorAddEdit diag=new DialogActuatorAddEdit((String)o[0],(String)o[1]);
					diag.setVisible(true);
				}
			}
		});
		
		this.deleteMenu=new JMenuItem("Delete...",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/DELETE.png")),14,14));
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete actuator "+m.getSelectedObj()[0].toString()+"?","Delete site",JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
					WaitUI u=new WaitUI();
					u.setText("Deleting actuator");
					Thread t=new Thread() {
						public void run () {
							boolean flag=DatabaseActuator.deleteActuator(m.getSelectedObj()[0].toString());
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
		this.add(toggleMenu);
		this.add(editMenu);
		this.add(deleteMenu);
		this.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				toggleMenu.setEnabled(m.getSelectedRow()!=-1);
				editMenu.setEnabled(m.getSelectedRow()!=-1);
				deleteMenu.setEnabled(m.getSelectedRow()!=-1);
			};
		
		});

	}
	
}
