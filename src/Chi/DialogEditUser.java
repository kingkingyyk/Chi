package Chi;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class DialogEditUser extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private static String prefilledPassword="~!@ZXC";
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldUsername;
	private JPasswordField textFieldPassword;
	private JLabel lblPasswordInfo;
	private JComboBox<Integer> comboBoxLevel;
	private JComboBox<String> comboBoxStatus;
	public boolean userEdited=false;

	public DialogEditUser(String fillU, int fillL, String fillS) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Edit User");
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 448, 222);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 444, 130);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JLabel lblUsername = new JLabel("Username :");
			lblUsername.setBounds(29, 11, 55, 14);
			lblUsername.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblUsername);
		}
		{
			textFieldUsername = new JTextField(fillU);
			textFieldUsername.setBounds(89, 8, 246, 20);
			contentPanel.add(textFieldUsername);
			textFieldUsername.setColumns(30);
			textFieldUsername.setEditable(false);
		}
		{
			JLabel lblPassword = new JLabel("Password :");
			lblPassword.setBounds(31, 41, 53, 14);
			lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblPassword);
		}
		{
			textFieldPassword = new JPasswordField(DialogEditUser.prefilledPassword);
			textFieldPassword.setBounds(89, 39, 246, 20);
			contentPanel.add(textFieldPassword);
			textFieldPassword.setColumns(10);
			textFieldPassword.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					char [] pw=textFieldPassword.getPassword();
					if (pw==null || pw.length==0) { 
						lblPasswordInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
					} else {
						lblPasswordInfo.setText("<html><font color=\"green\">OK!</font></html>");
					}
				}
			});
		}
		{
			lblPasswordInfo = new JLabel();
			lblPasswordInfo.setBounds(345, 38, 89, 20);
			contentPanel.add(lblPasswordInfo);
		}
		{
			JLabel lblLevel = new JLabel("Level :");
			lblLevel.setBounds(52, 71, 32, 14);
			lblLevel.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblLevel);
		}
		{
			comboBoxLevel = new JComboBox<>();
			comboBoxLevel.setBounds(89, 69, 246, 20);
			comboBoxLevel.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {1}));
			comboBoxLevel.setEnabled(false);
			comboBoxLevel.setSelectedIndex(0);
			contentPanel.add(comboBoxLevel);
		}
		{
			JLabel lblStatus = new JLabel("Status :");
			lblStatus.setHorizontalAlignment(SwingConstants.RIGHT);
			lblStatus.setBounds(29, 102, 55, 14);
			contentPanel.add(lblStatus);
		}
		
		comboBoxStatus = new JComboBox<String>();
		comboBoxStatus.setMaximumRowCount(3);
		comboBoxStatus.setModel(new DefaultComboBoxModel<String>(User.USER_STATUS));
		for (int i=0;i<User.USER_STATUS.length;i++) {
			if (User.USER_STATUS[i].equals(fillS)) {
				comboBoxStatus.setSelectedIndex(i);
				break;
			}
		}
		comboBoxStatus.setBounds(89, 99, 246, 20);
		contentPanel.add(comboBoxStatus);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 160, 444, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public boolean flag;
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						char [] pw=textFieldPassword.getPassword();
						if (pw==null || pw.length==0) {
							JOptionPane.showMessageDialog(null,"Invalid credential!",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
						} else {
							WaitUI u=new WaitUI();
							u.setText("Updating user");
							Thread t=new Thread() {
								public void run () {
									if (new String(pw).equals(prefilledPassword)) {
										flag=DatabaseUser.updateUserCredentialNoPassword(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)), fillU, (int)comboBoxLevel.getSelectedItem(),(String)comboBoxStatus.getSelectedItem());
									} else {
										flag=DatabaseUser.updateUserCredentialPassword(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)), fillU, Utility.hashSHA1CharAry(pw), (int)comboBoxLevel.getSelectedItem(),(String)comboBoxStatus.getSelectedItem());
									}
									u.dispose();
								}
							};
							t.start();
							u.setVisible(true);
							
							if (flag) {
								JOptionPane.showMessageDialog(null,"User updated successfully!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
								userEdited=true;
								dispose();
							} else {
								JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
					
				});
				buttonPane.add(cancelButton);
			}
		}
	}
}
