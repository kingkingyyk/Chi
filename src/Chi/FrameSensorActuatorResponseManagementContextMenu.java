package Chi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FrameSensorActuatorResponseManagementContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newMenu;
	private JMenuItem editMenu;
	private JMenuItem deleteMenu;
	
	public FrameSensorActuatorResponseManagementContextMenu (FrameSensorActuatorResponseManagement m) {
		this.newMenu=new JMenuItem("New...",Utility.resizeImageIcon(Theme.getIcon("AddIcon"),14,14));
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogSensorActuatorResponseAddEdit diag=new DialogSensorActuatorResponseAddEdit();
				diag.setVisible(true);
			}
		});
		
		this.editMenu=new JMenuItem("Edit...",Utility.resizeImageIcon(Theme.getIcon("EditIcon"),14,14));
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Sensoractuatorresponse res =m.getSelectedResponse();
				DialogSensorActuatorResponseAddEdit diag=new DialogSensorActuatorResponseAddEdit(res.getId(),res.getActuator().getName(),res.getOntriggeraction(),
																								res.getOnnottriggeraction(),res.getExpression(),res.getEnabled(),
																								res.getTimeout());
				diag.setVisible(true);
			}
		});
		
		this.deleteMenu=new JMenuItem("Delete...",Utility.resizeImageIcon(Theme.getIcon("DeleteIcon"),14,14));
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Sensoractuatorresponse res=m.getSelectedResponse();
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this response?","Delete response",JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
					WaitUI u=new WaitUI();
					u.setText("Deleting actuator");
					Thread t=new Thread() {
						public void run () {
							boolean flag=DatabaseSensorActuatorResponse.deleteSensorActuatorResponse(res.getId());
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
