package Chi;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class FrameActuatorManagementFeedbackWait extends JDialog {
	private static final long serialVersionUID = 4973286001560655413L;

	public FrameActuatorManagementFeedbackWait() {
		setModal(true);
		setUndecorated(true);
		setBounds(100, 100, 100, 100);
		getContentPane().setLayout(null);
		
		JLabel lblIcon = new JLabel();
		lblIcon.setIcon(Theme.getIcon("LoadingIcon"));
		lblIcon.setBounds(0, 0, 100, 100);
		getContentPane().add(lblIcon);
	}
}
