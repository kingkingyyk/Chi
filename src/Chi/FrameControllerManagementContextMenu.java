package Chi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FrameControllerManagementContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newMenu;
	private JMenuItem editMenu;
	private JMenuItem helloMenu;
	private JMenuItem deleteMenu;
	
	public FrameControllerManagementContextMenu (FrameControllerManagement m) {
		this.newMenu=new JMenuItem("New...",Utility.resizeImageIcon(Theme.getIcon("AddIcon"),14,14));
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogControllerAddEdit diag=new DialogControllerAddEdit();
				diag.setVisible(true);
			}
		});
		
		this.helloMenu=new JMenuItem("Force report alive",Utility.resizeImageIcon(Theme.getIcon("HelloIcon"),14,14));
		helloMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ControllerPacketRequestAlive p=new ControllerPacketRequestAlive(m.getSelectedController().getControllername());
				if (p.send()) {
					JOptionPane.showMessageDialog(null,"Request sent successfully.\nThe last report time will be updated once the controller has replied","Force report alive",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		this.editMenu=new JMenuItem("Edit...",Utility.resizeImageIcon(Theme.getIcon("EditIcon"),14,14));
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Controller c=m.getSelectedController();
				DialogControllerAddEdit diag=new DialogControllerAddEdit(c.getControllername(),c.getSite().getSitename(),c.getPositionx(),c.getPositiony(),c.getReporttimeout());
				diag.setVisible(true);
			}
		});
		
		this.deleteMenu=new JMenuItem("Delete...",Utility.resizeImageIcon(Theme.getIcon("DeleteIcon"),14,14));
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete controller "+m.getSelectedController().getControllername()+"?","Delete controller",JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
					WaitUI u=new WaitUI();
					u.setText("Deleting controller");
					Thread t=new Thread() {
						public void run () {
							boolean flag=DatabaseController.deleteController(m.getSelectedController().getControllername());
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
		this.add(helloMenu);
		this.add(editMenu);
		this.add(deleteMenu);
		this.addPopupMenuListener(new PopupMenuListener() {

			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent arg0) {}

			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				editMenu.setEnabled(m.getSelectedRow()!=-1 && !m.getSelectedController().getControllername().equals("DefaultController"));
				helloMenu.setEnabled(m.getSelectedRow()!=-1 && !m.getSelectedController().getControllername().equals("DefaultController"));
				deleteMenu.setEnabled(m.getSelectedRow()!=-1 && !m.getSelectedController().getControllername().equals("DefaultController"));
			};
		
		});

	}
	
}
