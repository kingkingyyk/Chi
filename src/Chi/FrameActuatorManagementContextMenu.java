package Chi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		this.newMenu=new JMenuItem("New...",Utility.resizeImageIcon(Theme.getIcon("AddIcon"),14,14));
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogActuatorAddEdit diag=new DialogActuatorAddEdit();
				diag.setVisible(true);
			}
		});
		
		this.toggleMenu=new JMenuItem("Toggle Status",Utility.resizeImageIcon(Theme.getIcon("ActuatorIcon"),14,14));
		toggleMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Actuator act =m.getSelectedActuator();
				String status=act.getStatus();
				if (status.equals("Pending Update")) status="ON";
				else if (status.equals("OFF")) status="ON";
				else if (status.equals("ON")) status="OFF";
					
				ControllerPacketActuatorTrigger p=new ControllerPacketActuatorTrigger(act.getController().getControllername(),act.getName(),status);
				p.trigger();
				
				FrameActuatorManagementFeedbackWait fa=new FrameActuatorManagementFeedbackWait();
				fa.setLocationRelativeTo(null);
				Thread t=new Thread() {
					public void run () {
						try { Thread.sleep(Config.CONTROLLER_REPLY_TIMEOUT_MS); } catch (InterruptedException e) {};
						if (fa!=null && fa.isVisible()) {
							Logger.log("FrameActuatorManagement - Communication Error : "+act.getController().getControllername()+" doesn't reply in 15 seconds!");
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
		});
		
		this.editMenu=new JMenuItem("Edit...",Utility.resizeImageIcon(Theme.getIcon("EditIcon"),14,14));
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Actuator act =m.getSelectedActuator();
				DialogActuatorAddEdit diag=new DialogActuatorAddEdit(act.getName(),act.getController().getControllername(),act.getPositionx(),act.getPositiony());
				diag.setVisible(true);
			}
		});
		
		this.deleteMenu=new JMenuItem("Delete...",Utility.resizeImageIcon(Theme.getIcon("DeleteIcon"),14,14));
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete actuator "+m.getSelectedActuator().getName()+"?","Delete site",JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
					WaitUI u=new WaitUI();
					u.setText("Deleting actuator");
					Thread t=new Thread() {
						public void run () {
							boolean flag=DatabaseActuator.deleteActuator(m.getSelectedActuator().getName());
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
				toggleMenu.setEnabled(m.getSelectedRow()!=-1 && DataServer.started());
				editMenu.setEnabled(m.getSelectedRow()!=-1);
				deleteMenu.setEnabled(m.getSelectedRow()!=-1);
			};
		
		});

	}
	
}
