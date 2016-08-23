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
import javax.swing.JPasswordField;

public class ConfigUI extends JDialog {
	private static final long serialVersionUID = -4765449778876379813L;
	private JPanel contentPane;
	private JTextField textFieldListeningPort;
	private JTextField textFieldDatabaseIP;
	private JTextField textFieldDatabasePort;
	private JLabel lblTestDatabaseResult;
	private JTextField textFieldDatabaseUsername;
	private JPasswordField passwordFieldDatabasePassword;
	private JTextField textFieldDBHSQLIP;
	private JTextField textFieldDBHSQLPort;
	private JTextField textFieldDBHSQLUsername;
	private JPasswordField passwordFieldDBHSQLPassword;
	private JLabel lblDBHSQLTestResult;
	private JTextField textFieldControllerIP;
	private JTextField textFieldControllerPort;
	private JTextField textFieldGWTPort;

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
		tabbedPane.addTab("Server", null, panelServerSettings, null);
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
		
		JLabel lblGWTPort = new JLabel("GWT Port :");
		lblGWTPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGWTPort.setBounds(10, 43, 82, 14);
		panelServerSettings.add(lblGWTPort);
		
		textFieldGWTPort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY));
		textFieldGWTPort.setToolTipText("The port to communicate with GWT server");
		textFieldGWTPort.setColumns(10);
		textFieldGWTPort.setBounds(102, 40, 86, 20);
		panelServerSettings.add(textFieldGWTPort);
		
		JButton btnGWTPort = new JButton(Theme.getIcon("Reset-16x16"));
		btnGWTPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textFieldGWTPort.setText(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnGWTPort.setBounds(198, 39, 24, 20);
		panelServerSettings.add(btnGWTPort);
		
		JPanel panelHSQLSettings = new JPanel();
		panelHSQLSettings.setLayout(null);
		tabbedPane.addTab("HSQL", null, panelHSQLSettings, null);
		
		JLabel lblDBHSQLIP = new JLabel("Database IP :");
		lblDBHSQLIP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDBHSQLIP.setBounds(10, 14, 86, 14);
		panelHSQLSettings.add(lblDBHSQLIP);
		
		textFieldDBHSQLIP = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY));
		textFieldDBHSQLIP.setToolTipText("The IP of database server to connect to.");
		textFieldDBHSQLIP.setColumns(10);
		textFieldDBHSQLIP.setBounds(106, 10, 86, 20);
		panelHSQLSettings.add(textFieldDBHSQLIP);
		
		JLabel lblDBHSQLPort = new JLabel("Database Port :");
		lblDBHSQLPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDBHSQLPort.setBounds(10, 45, 86, 14);
		panelHSQLSettings.add(lblDBHSQLPort);
		
		textFieldDBHSQLPort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY));
		textFieldDBHSQLPort.setToolTipText("The port of database server to connect to.");
		textFieldDBHSQLPort.setColumns(10);
		textFieldDBHSQLPort.setBounds(106, 41, 86, 20);
		panelHSQLSettings.add(textFieldDBHSQLPort);
		
		JButton btnDBHSQLIP = new JButton(Theme.getIcon("Reset-16x16"));
		btnDBHSQLIP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDBHSQLIP.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnDBHSQLIP.setBounds(199, 10, 24, 20);
		panelHSQLSettings.add(btnDBHSQLIP);
		
		JButton btnDBHSQLPort = new JButton(Theme.getIcon("Reset-16x16"));
		btnDBHSQLPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDBHSQLPort.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnDBHSQLPort.setBounds(199, 41, 24, 20);
		panelHSQLSettings.add(btnDBHSQLPort);
		
		JButton btnBDHSQLTest = new JButton("Test Connection");
		btnBDHSQLTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    	lblDBHSQLTestResult.setText("Attempting to connect...");
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                        		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY);
                        		String port=Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY);
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY,textFieldDBHSQLIP.getText());
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY,textFieldDBHSQLPort.getText());
                            	if (DatabaseHSQL.testConnection()) {
                            		lblDBHSQLTestResult.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_TEST_OK_KEY));
                            	} else {
                            		lblDBHSQLTestResult.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_TEST_FAIL_KEY));
                            	}
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY,ip);
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY,port);
                            }
                        });
                    }
                });
			}
		});
		btnBDHSQLTest.setBounds(10, 175, 117, 23);
		panelHSQLSettings.add(btnBDHSQLTest);
		
		lblDBHSQLTestResult = new JLabel("");
		lblDBHSQLTestResult.setBounds(137, 179, 165, 14);
		panelHSQLSettings.add(lblDBHSQLTestResult);
		
		JLabel lblDBHSQLUsername = new JLabel("Username :");
		lblDBHSQLUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDBHSQLUsername.setBounds(10, 76, 86, 14);
		panelHSQLSettings.add(lblDBHSQLUsername);
		
		textFieldDBHSQLUsername = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_USERNAME_KEY));
		textFieldDBHSQLUsername.setToolTipText("The username to login to the database.");
		textFieldDBHSQLUsername.setColumns(10);
		textFieldDBHSQLUsername.setBounds(106, 72, 86, 20);
		panelHSQLSettings.add(textFieldDBHSQLUsername);
		
		JLabel lblDBHSQLPassword = new JLabel("Password :");
		lblDBHSQLPassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDBHSQLPassword.setBounds(10, 106, 86, 14);
		panelHSQLSettings.add(lblDBHSQLPassword);
		
		passwordFieldDBHSQLPassword = new JPasswordField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PASSWORD_KEY));
		passwordFieldDBHSQLPassword.setToolTipText("The password to login to the database.");
		passwordFieldDBHSQLPassword.setBounds(106, 103, 86, 20);
		panelHSQLSettings.add(passwordFieldDBHSQLPassword);
		
		JPanel panelCassandraSettings = new JPanel();
		tabbedPane.addTab("Cassandra", null, panelCassandraSettings, null);
		panelCassandraSettings.setLayout(null);
		
		JLabel lblDatabaseIP = new JLabel("Database IP :");
		lblDatabaseIP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabaseIP.setBounds(10, 14, 86, 14);
		panelCassandraSettings.add(lblDatabaseIP);
		
		textFieldDatabaseIP = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY));
		textFieldDatabaseIP.setColumns(10);
		textFieldDatabaseIP.setBounds(106, 10, 86, 20);
		textFieldDatabaseIP.setToolTipText("The IP of database server to connect to.");
		panelCassandraSettings.add(textFieldDatabaseIP);
		
		JLabel lblDatabasePort = new JLabel("Database Port :");
		lblDatabasePort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabasePort.setBounds(10, 45, 86, 14);
		panelCassandraSettings.add(lblDatabasePort);
		
		textFieldDatabasePort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		textFieldDatabasePort.setColumns(10);
		textFieldDatabasePort.setBounds(106, 41, 86, 20);
		textFieldDatabasePort.setToolTipText("The port of database server to connect to.");
		panelCassandraSettings.add(textFieldDatabasePort);
		
		JButton btnDatabaseIPReset = new JButton(Theme.getIcon("Reset-16x16"));
		btnDatabaseIPReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDatabaseIP.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnDatabaseIPReset.setBounds(199, 10, 24, 20);
		panelCassandraSettings.add(btnDatabaseIPReset);
		
		JButton btnDatabasePortReset = new JButton(Theme.getIcon("Reset-16x16"));
		btnDatabasePortReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldDatabasePort.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		btnDatabasePortReset.setBounds(199, 41, 24, 20);
		panelCassandraSettings.add(btnDatabasePortReset);
		
		JButton btnTestDatabase = new JButton("Test Connection");
		btnTestDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    	lblTestDatabaseResult.setText("Attempting to connect...");
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                        		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
                        		String port=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY);
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY,textFieldDatabaseIP.getText());
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY,textFieldDatabasePort.getText());
                            	if (DatabaseCassandra.testConnection()) {
                            		lblTestDatabaseResult.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_TEST_OK_KEY));
                            	} else {
                            		lblTestDatabaseResult.setText(Config.getConfig(Config.CONFIG_SERVER_DATABASE_TEST_FAIL_KEY));
                            	}
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY,ip);
                        		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY,port);
                            }
                        });
                    }
                });
			}
		});
		btnTestDatabase.setBounds(10, 175, 117, 23);
		panelCassandraSettings.add(btnTestDatabase);
		
		lblTestDatabaseResult = new JLabel("");
		lblTestDatabaseResult.setBounds(137, 179, 165, 14);
		panelCassandraSettings.add(lblTestDatabaseResult);
		
		JLabel lblDatabaseLogo = new JLabel();
		lblDatabaseLogo.setBounds(434, 131, 100, 67);
		lblDatabaseLogo.setIcon(Utility.resizeImageIcon(Theme.getIcon("CassandraLogo"),lblDatabaseLogo.getWidth(),lblDatabaseLogo.getHeight()));
		panelCassandraSettings.add(lblDatabaseLogo);
		
		JLabel lblDatabaseUsername = new JLabel("Username :");
		lblDatabaseUsername.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabaseUsername.setBounds(10, 76, 86, 14);
		panelCassandraSettings.add(lblDatabaseUsername);
		
		textFieldDatabaseUsername = new JTextField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY));
		textFieldDatabaseUsername.setToolTipText("The username to login to the database.");
		textFieldDatabaseUsername.setColumns(10);
		textFieldDatabaseUsername.setBounds(106, 72, 86, 20);
		panelCassandraSettings.add(textFieldDatabaseUsername);
		
		JLabel lblDatabasePassword = new JLabel("Password :");
		lblDatabasePassword.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDatabasePassword.setBounds(10, 106, 86, 14);
		panelCassandraSettings.add(lblDatabasePassword);
		
		passwordFieldDatabasePassword = new JPasswordField(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY));
		passwordFieldDatabasePassword.setToolTipText("The password to login to the database.");
		passwordFieldDatabasePassword.setBounds(106, 103, 86, 20);
		panelCassandraSettings.add(passwordFieldDatabasePassword);
		
		JPanel panelControllerSettings = new JPanel();
		tabbedPane.addTab("Controller", null, panelControllerSettings, null);
		panelControllerSettings.setLayout(null);
		
		JLabel lblControllerIP = new JLabel("Address :");
		lblControllerIP.setHorizontalAlignment(SwingConstants.RIGHT);
		lblControllerIP.setBounds(10, 14, 58, 14);
		panelControllerSettings.add(lblControllerIP);
		
		textFieldControllerIP = new JTextField(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_IP_KEY));
		textFieldControllerIP.setToolTipText("Controller's IP. Use multicast address if multiple controller exists.");
		textFieldControllerIP.setColumns(10);
		textFieldControllerIP.setBounds(78, 11, 86, 20);
		panelControllerSettings.add(textFieldControllerIP);
		
		JButton btnControllerAddress = new JButton(Theme.getIcon("Reset-16x16"));
		btnControllerAddress.setBounds(174, 11, 24, 20);
		btnControllerAddress.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldControllerIP.setText(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_IP_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		panelControllerSettings.add(btnControllerAddress);
		
		JLabel lblControllerPort = new JLabel("Port :");
		lblControllerPort.setHorizontalAlignment(SwingConstants.RIGHT);
		lblControllerPort.setBounds(10, 46, 58, 14);
		panelControllerSettings.add(lblControllerPort);
		
		textFieldControllerPort = new JTextField(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_PORT_KEY));
		textFieldControllerPort.setToolTipText("Controller's port.");
		textFieldControllerPort.setColumns(10);
		textFieldControllerPort.setBounds(78, 43, 86, 20);
		panelControllerSettings.add(textFieldControllerPort);
		
		JButton btnControllerPort = new JButton(Theme.getIcon("Reset-16x16"));
		btnControllerPort.setBounds(174, 43, 24, 20);
		btnControllerPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textFieldControllerPort.setText(Config.getConfig(Config.CONFIG_SERVER_CONTROLLER_PORT_KEY+Config.CONFIG_DEFAULT_KEY));
			}
		});
		panelControllerSettings.add(btnControllerPort);
		
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
		Config.setConfig(Config.CONFIG_SERVER_GWT_PORT_KEY, textFieldGWTPort.getText());
		
		Config.setConfig(Config.CONFIG_SERVER_CONTROLLER_IP_KEY, textFieldControllerIP.getText());
		Config.setConfig(Config.CONFIG_SERVER_CONTROLLER_PORT_KEY, textFieldControllerPort.getText());
		
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY, textFieldDBHSQLIP.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY, textFieldDBHSQLPort.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_USERNAME_KEY, textFieldDBHSQLUsername.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PASSWORD_KEY, new String(passwordFieldDBHSQLPassword.getPassword()));
		
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY, textFieldDatabaseIP.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY, textFieldDatabasePort.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY, textFieldDatabaseUsername.getText());
		Config.setConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY, new String(passwordFieldDatabasePassword.getPassword()));
		
		Config.writeConfigFile();
	}
}
