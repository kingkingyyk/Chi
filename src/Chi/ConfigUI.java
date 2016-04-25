package Chi;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ConfigUI extends JDialog {
	private static final long serialVersionUID = -4765449778876379813L;
	private JPanel contentPane;
	private JTextField textFieldListeningPort;
	private JTextField textFieldDatabasePort;
	private JTextField textFieldDatabaseIP;

	public ConfigUI() {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle(Config.APP_NAME+" Configuration");
		setResizable(false);
		setBounds(100, 100, 560, 354);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 5, 549, 286);
		contentPane.add(tabbedPane);
		
		JPanel panelServerSettings = new JPanel();
		tabbedPane.addTab("Server Settings", null, panelServerSettings, null);
		panelServerSettings.setLayout(null);
		
		JLabel lblListeningPort = new JLabel("Listening Port :");
		lblListeningPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblListeningPort.setBounds(10, 11, 82, 14);
		panelServerSettings.add(lblListeningPort);
		
		textFieldListeningPort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY));
		textFieldListeningPort.setBounds(102, 8, 86, 20);
		panelServerSettings.add(textFieldListeningPort);
		textFieldListeningPort.setColumns(10);
		
		JLabel lblListeningPortDetail = new JLabel("<html><i>The port to receive data from sink node.</i></html>");
		lblListeningPortDetail.setBounds(198, 11, 336, 14);
		panelServerSettings.add(lblListeningPortDetail);
		
		JPanel panelDatabaseSettings = new JPanel();
		tabbedPane.addTab("Database Server Settings", null, panelDatabaseSettings, null);
		panelDatabaseSettings.setLayout(null);
		
		JLabel lblDatabasePort = new JLabel("Database Port :");
		lblDatabasePort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabasePort.setBounds(10, 42, 82, 14);
		panelDatabaseSettings.add(lblDatabasePort);
		
		textFieldDatabasePort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY));
		textFieldDatabasePort.setColumns(10);
		textFieldDatabasePort.setBounds(102, 39, 86, 20);
		panelDatabaseSettings.add(textFieldDatabasePort);
		
		JLabel lblthePortOf = new JLabel("<html><i>The port of database server to connect to.</i></html>");
		lblthePortOf.setBounds(198, 42, 336, 14);
		panelDatabaseSettings.add(lblthePortOf);
		
		JLabel lblDatabaseIP = new JLabel("Database IP :");
		lblDatabaseIP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabaseIP.setBounds(10, 14, 82, 14);
		panelDatabaseSettings.add(lblDatabaseIP);
		
		textFieldDatabaseIP = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY));
		textFieldDatabaseIP.setColumns(10);
		textFieldDatabaseIP.setBounds(102, 11, 86, 20);
		panelDatabaseSettings.add(textFieldDatabaseIP);
		
		JLabel lblDatabaseIPDetail = new JLabel("<html><i>The IP of database server to connect to.</i></html>");
		lblDatabaseIPDetail.setBounds(198, 14, 336, 14);
		panelDatabaseSettings.add(lblDatabaseIPDetail);
		
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveConfigurations();
			}
		});
		btnApply.setBounds(455, 297, 89, 23);
		contentPane.add(btnApply);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(356, 297, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveConfigurations();
				dispose();
			}
		});
		btnOK.setBounds(257, 297, 89, 23);
		contentPane.add(btnOK);
		
		JLabel lblVersion = new JLabel(Config.VERSION);
		lblVersion.setEnabled(false);
		lblVersion.setBounds(5, 306, 111, 14);
		contentPane.add(lblVersion);
	}
	
	private void saveConfigurations() {
		Config.setConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY, textFieldListeningPort.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY, textFieldDatabaseIP.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY, textFieldDatabasePort.getText());
		
		Config.writeConfigFile();
	}
}
