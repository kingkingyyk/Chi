package FrameEntityManagement;

import javax.swing.JDialog;
import javax.swing.JLabel;

import Chi.Theme;

public class FrameActuatorManagementFeedbackWait extends JDialog {
	private static final long serialVersionUID = 4973286001560655413L;
	private static FrameActuatorManagementFeedbackWait f=null;

	public FrameActuatorManagementFeedbackWait() {
		setModal(true);
		setUndecorated(true);
		setBounds(100, 100, 100, 100);
		getContentPane().setLayout(null);
		
		JLabel lblIcon = new JLabel();
		lblIcon.setIcon(Theme.getIcon("LoadingIcon"));
		lblIcon.setBounds(0, 0, 100, 100);
		getContentPane().add(lblIcon);
		
		f=this;
	}
	
	public static FrameActuatorManagementFeedbackWait getCurrent() {
		if (FrameActuatorManagementFeedbackWait.f!=null && FrameActuatorManagementFeedbackWait.f.isVisible()) return FrameActuatorManagementFeedbackWait.f;
		else return null;
	}
}
