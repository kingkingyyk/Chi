package Chi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class FrameSensorClassManagementContextMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;
	private JMenuItem newMenu;
	private JMenuItem editMenu;
	private JMenuItem deleteMenu;
	
	public FrameSensorClassManagementContextMenu (FrameSensorClassManagement m) {
		this.newMenu=new JMenuItem("New...",Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/ADD.png")),14,14));
		newMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogSensorClassAddEdit diag=new DialogSensorClassAddEdit();
				diag.setVisible(true);
			}
		});
		
		this.editMenu=new JMenuItem("Edit...",Utility.resizeImageIcon(Theme.getIcon("EditIcon"),14,14));
		editMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DialogSensorClassAddEdit diag=new DialogSensorClassAddEdit(m.getSelectedClass().getClassname());
				diag.setVisible(true);
			}
		});
		
		this.deleteMenu=new JMenuItem("Delete...",Utility.resizeImageIcon(Theme.getIcon("DeleteIcon"),14,14));
		deleteMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to delete sensor class "+m.getSelectedClass().getClassname()+"?\nSensors in this class will be removed to DefaultClass after deletion.","Delete sensor class",JOptionPane.WARNING_MESSAGE)==JOptionPane.YES_OPTION) {
					WaitUI u=new WaitUI();
					u.setText("Deleting sensor class");
					Thread t=new Thread() {
						public void run () {
							boolean flag=DatabaseSensorClass.deleteSensorClass(m.getSelectedClass().getClassname());
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
				editMenu.setEnabled(m.getSelectedRow()!=-1  && !m.getSelectedClass().getClassname().equals("DefaultClass"));
				deleteMenu.setEnabled(m.getSelectedRow()!=-1  && !m.getSelectedClass().getClassname().equals("DefaultClass"));
			};
		
		});

	}
	
}
