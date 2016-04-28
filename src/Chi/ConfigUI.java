package Chi;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Font;

public class ConfigUI extends JDialog {
	private static final long serialVersionUID = -4765449778876379813L;
	private JPanel contentPane;
	private JTextField textFieldListeningPort;
	private JTextField textFieldDatabaseIP;
	private JTextField textFieldDatabasePort;
	private JLabel lblTestDatabaseResult;

	public ConfigUI() {
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Chi");
		setResizable(false);
		setBounds(100, 100, 560, 354);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(5, 54, 549, 237);
		contentPane.add(tabbedPane);
		
		JPanel panelServerSettings = new JPanel();
		tabbedPane.addTab("Server Settings", null, panelServerSettings, null);
		panelServerSettings.setLayout(null);
		
		JLabel lblListeningPort = new JLabel("Listening Port :");
		lblListeningPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblListeningPort.setBounds(10, 11, 82, 14);
		panelServerSettings.add(lblListeningPort);
		
		textFieldListeningPort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY));
		textFieldListeningPort.setToolTipText("The port to receive data from sink node.");
		textFieldListeningPort.setBounds(102, 8, 86, 20);
		panelServerSettings.add(textFieldListeningPort);
		textFieldListeningPort.setColumns(10);
		
		JButton btnListeningPort = new JButton(Theme.getIcon("Reset-16x16"));
		btnListeningPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textFieldListeningPort.setText(Config.getConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnListeningPort.setBounds(198, 7, 24, 20);
		panelServerSettings.add(btnListeningPort);
		
		JPanel panelDatabaseSettings = new JPanel();
		tabbedPane.addTab("Database Server Settings", null, panelDatabaseSettings, null);
		panelDatabaseSettings.setLayout(null);
		
		JLabel lblDatabaseIP = new JLabel("Database IP :");
		lblDatabaseIP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabaseIP.setBounds(10, 14, 82, 14);
		panelDatabaseSettings.add(lblDatabaseIP);
		
		textFieldDatabaseIP = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY));
		textFieldDatabaseIP.setColumns(10);
		textFieldDatabaseIP.setBounds(102, 11, 86, 20);
		textFieldDatabaseIP.setToolTipText("The IP of database server to connect to.");
		panelDatabaseSettings.add(textFieldDatabaseIP);
		
		JLabel lblDatabasePort = new JLabel("Database Port :");
		lblDatabasePort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabasePort.setBounds(10, 45, 82, 14);
		panelDatabaseSettings.add(lblDatabasePort);
		
		textFieldDatabasePort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY));
		textFieldDatabasePort.setColumns(10);
		textFieldDatabasePort.setBounds(102, 42, 86, 20);
		textFieldDatabasePort.setToolTipText("The port of database server to connect to.");
		panelDatabaseSettings.add(textFieldDatabasePort);
		
		JButton btnDatabaseIPReset = new JButton(Theme.getIcon("Reset-16x16"));
		btnDatabaseIPReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDatabaseIP.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnDatabaseIPReset.setBounds(195, 11, 24, 20);
		panelDatabaseSettings.add(btnDatabaseIPReset);
		
		JButton btnDatabasePortReset = new JButton(Theme.getIcon("Reset-16x16"));
		btnDatabasePortReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDatabasePort.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnDatabasePortReset.setBounds(195, 42, 24, 20);
		panelDatabaseSettings.add(btnDatabasePortReset);
		
		JButton btnTestDatabase = new JButton("Test Connection");
		btnTestDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    	lblTestDatabaseResult.setText("Attempting to connect...");
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                            	if (Database.testConnection(textFieldDatabaseIP.getText(), Integer.parseInt(textFieldDatabasePort.getText()))) {
                            		lblTestDatabaseResult.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_TEST_OK_KEY));
                            	} else {
                            		lblTestDatabaseResult.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_TEST_FAIL_KEY));
                            	}
                            }
                        });
                    }
                });
			}
		});
		btnTestDatabase.setBounds(10, 89, 117, 23);
		panelDatabaseSettings.add(btnTestDatabase);
		
		lblTestDatabaseResult = new JLabel("");
		lblTestDatabaseResult.setBounds(137, 93, 165, 14);
		panelDatabaseSettings.add(lblTestDatabaseResult);
		
		JLabel lblDatabaseLogo = new JLabel();
		lblDatabaseLogo.setBounds(434, 131, 100, 67);
		lblDatabaseLogo.setIcon(Utility.resizeImageIcon(Theme.getIcon("CassandraLogo"),lblDatabaseLogo.getWidth(),lblDatabaseLogo.getHeight()));
		panelDatabaseSettings.add(lblDatabaseLogo);
		
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
		
		JLabel lblTopTitleBarText = new JLabel("Configuration");
		lblTopTitleBarText.setFont(lblTopTitleBarText.getFont().deriveFont(lblTopTitleBarText.getFont().getStyle() | Font.BOLD, lblTopTitleBarText.getFont().getSize() + 1f));
		lblTopTitleBarText.setForeground(Color.WHITE);
		lblTopTitleBarText.setBounds(10, 11, 140, 23);
		contentPane.add(lblTopTitleBarText);
		
		JLabel lblTopTitleBar = new JLabel();
		lblTopTitleBar.setBounds(0, 0, 554, 47);
		lblTopTitleBar.setIcon(Utility.resizeImageIcon(Theme.getIcon("TopTitleBar"),lblTopTitleBar.getWidth(),lblTopTitleBar.getHeight()));
		contentPane.add(lblTopTitleBar);
	}
	
	private void saveConfigurations() {
		Config.setConfig(Config.CONFIG_SERVER_INCOMING_PORT_KEY, textFieldListeningPort.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY, textFieldDatabaseIP.getText());
		
		Config.writeConfigFile();
	}
}
