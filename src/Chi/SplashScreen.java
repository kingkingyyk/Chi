package Chi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

public class SplashScreen extends JFrame {
	private static final long serialVersionUID = -1439297640667763037L;
	private JPanel contentPane;
	private JButton btnStartServer;
	private JButton btnStopServer;
	private JTextField textFieldSQL;


	public SplashScreen() {
		setTitle(Config.APP_NAME);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 445, 310);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblAppLogo = new JLabel();
		lblAppLogo.setBounds(10, 194, 80, 80);
		lblAppLogo.setIcon(Utility.resizeImageIcon(Theme.getIcon("ChiLogo"), lblAppLogo.getWidth(), lblAppLogo.getHeight()));
		contentPane.add(lblAppLogo);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(310, 251, 119, 23);
		contentPane.add(btnExit);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 419, 172);
		contentPane.add(tabbedPane);
		
		JPanel panelServer = new JPanel();
		tabbedPane.addTab("Server", null, panelServer, null);
		
		btnStartServer = new JButton("Start Server");
		panelServer.add(btnStartServer);
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.start();
				if (Server.started()) {
					btnStartServer.setEnabled(false);
					btnStopServer.setEnabled(true);
				}
			}
		});
		
		btnStopServer = new JButton("Stop Server");
		panelServer.add(btnStopServer);
		btnStopServer.setEnabled(false);
		
		JPanel panelDatabase = new JPanel();
		tabbedPane.addTab("Database", null, panelDatabase, null);
		panelDatabase.setLayout(null);
		
		JButton btnCreateTables = new JButton("Create Tables");
		btnCreateTables.setBounds(10, 5, 109, 23);
		panelDatabase.add(btnCreateTables);
		
		JButton btnResetDatabase = new JButton("Reset Database");
		btnResetDatabase.setBounds(10, 35, 109, 23);
		panelDatabase.add(btnResetDatabase);
		
		JPanel panelSQL = new JPanel();
		tabbedPane.addTab("SQL", null, panelSQL, null);
		panelSQL.setLayout(null);
		
		JButton btnViewReadings = new JButton("View All Readings");
		btnViewReadings.setBounds(10, 76, 377, 23);
		panelSQL.add(btnViewReadings);
		btnViewReadings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ResultSet rs=Database.getSensorReading(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
				ArrayList<Row> rows=new ArrayList<>();
				for (Row row : rs) {
					rows.add(row);
				}
				//ReadingExport.export(rows);
			}
		});
		
		textFieldSQL = new JTextField();
		textFieldSQL.setBounds(119, 11, 268, 20);
		panelSQL.add(textFieldSQL);
		textFieldSQL.setColumns(10);
		
		JButton btnRunSQL = new JButton("Run SQL");
		btnRunSQL.setBounds(10, 10, 101, 23);
		panelSQL.add(btnRunSQL);
		btnRunSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Database.runSQL("Run SQL", Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)), textFieldSQL.getText());
			}
		});
		
		JButton btnLoadSQLFromFile = new JButton("Run SQL From File");
		btnLoadSQLFromFile.setBounds(10, 42, 377, 23);
		panelSQL.add(btnLoadSQLFromFile);
		btnLoadSQLFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc=new JFileChooser();
				if (fc.showOpenDialog(SplashScreen.this)==JFileChooser.APPROVE_OPTION) {
					File selectedFile=fc.getSelectedFile();
					Database.runSQLFromFile("Run SQL From File",Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)), selectedFile.getPath());
				}
			}
		});
		

		
		JPanel panelUsers = new JPanel();
		tabbedPane.addTab("Users", null, panelUsers, null);
		panelUsers.setLayout(null);
		
		JButton btnCreateUser = new JButton("Create User");
		btnCreateUser.setBounds(10, 11, 192, 23);
		btnCreateUser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
			
		});
		panelUsers.add(btnCreateUser);
		
		JButton btnViewUsers = new JButton("View Users");
		btnViewUsers.setBounds(212, 11, 192, 23);
		panelUsers.add(btnViewUsers);
		
		JButton btnModifyUser = new JButton("Modify User");
		btnModifyUser.setBounds(10, 45, 192, 23);
		panelUsers.add(btnModifyUser);
		
		JButton btnDeleteUser = new JButton("Delete User");
		btnDeleteUser.setBounds(212, 45, 192, 23);
		panelUsers.add(btnDeleteUser);
		
		JPanel panelSensor = new JPanel();
		tabbedPane.addTab("Sensors", null, panelSensor, null);
		panelSensor.setLayout(null);
		
		JButton btnCreateSensor = new JButton("Create Sensor");
		btnCreateSensor.setBounds(10, 11, 193, 23);
		panelSensor.add(btnCreateSensor);
		
		JButton btnViewSensors = new JButton("View Sensors");
		btnViewSensors.setBounds(213, 11, 191, 23);
		panelSensor.add(btnViewSensors);
		
		JButton btnDeleteSensor = new JButton("Delete Sensor");
		btnDeleteSensor.setBounds(213, 45, 191, 23);
		panelSensor.add(btnDeleteSensor);
		
		JButton btnModifySensor = new JButton("Modify Sensor");
		btnModifySensor.setBounds(10, 45, 193, 23);
		panelSensor.add(btnModifySensor);
		
		JPanel panelSettings = new JPanel();
		tabbedPane.addTab("Settings", null, panelSettings, null);
		
		JButton btnManage = new JButton("Manage");
		panelSettings.add(btnManage);
		
		JButton btnConfig = new JButton("Config");
		panelSettings.add(btnConfig);
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.log("Config UI started.");
				ConfigUI ui=new ConfigUI();
				Logger.log("Config UI done.");
				ui.setVisible(true);
				Logger.log("Config UI closed.");
			}
		});
		btnManage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnResetDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitUI u=new WaitUI();
				u.setProgressBarMin(1);
				u.setProgressBarMax(3);
				Thread t=new Thread() {
					public void run () {
						u.setText("Step (1/2) Checking keyspace existence");
						u.setProgressBarValue(2);
						boolean flag=Database.testKeyspace(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
						if (!flag) {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"No matching keyspace to reset!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
		            		u.dispose();
		            		return;
						}
						u.setText("Step (2/2) Reseting keyspace");
						u.setProgressBarValue(3);
		            	flag=Database.reset(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
		            	if (flag) {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Keyspace reset successully!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
		            	} else {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Failed to reset keyspace!\nPlease check the availability of the Cassandra server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
		            	}
		            	u.dispose();
					}
				};
				t.start();
				u.setVisible(true);
			}
		});
		btnCreateTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitUI u=new WaitUI();
				u.setProgressBarMin(1);
				u.setProgressBarMax(3);
				Thread t=new Thread() {
					public void run () {
						u.setText("Step (1/2) Checking keyspace existence");
						u.setProgressBarValue(2);
						boolean flag=Database.testKeyspace(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
						if (!flag) {
							flag=Database.freshStart(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
		                	if (!flag) {
		                		JOptionPane.showMessageDialog(SplashScreen.this,"Failed to create keyspace!\nPlease check the availability of the Cassandra server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
				            	u.dispose();
				            	return;
		                	}
						}
						u.setText("Step (2/2) Creating tables");
						u.setProgressBarValue(3);
						flag=Database.createTables(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
		            	if (flag) {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Tables created successfully!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
						} else { 
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Failed to create table!\nPlease check if existing table is removed.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
		            	}
		            	u.dispose();
					}
				};
				t.start();
				u.setVisible(true);
			}
		});
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.stop();
				if (!Server.started()) {
					btnStartServer.setEnabled(true);
					btnStopServer.setEnabled(false);
				}
			}
		});
	}
}
