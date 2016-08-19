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
		
		JButton btnConfig = new JButton("Config");
		panelServer.add(btnConfig);
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.log("Config UI started.");
				ConfigUI ui=new ConfigUI();
				Logger.log("Config UI done.");
				ui.setVisible(true);
				Logger.log("Config UI closed.");
			}
		});
		
		JPanel panelDatabase = new JPanel();
		tabbedPane.addTab("Database", null, panelDatabase, null);
		panelDatabase.setLayout(null);
		
		JButton btnCreateTables = new JButton("Create Tables");
		btnCreateTables.setBounds(10, 5, 109, 23);
		panelDatabase.add(btnCreateTables);
		
		JButton btnResetDatabase = new JButton("Reset Database");
		btnResetDatabase.setBounds(10, 35, 109, 23);
		panelDatabase.add(btnResetDatabase);
		
		JButton btnUserManagement = new JButton("User Management");
		btnUserManagement.setBounds(213, 5, 192, 23);
		panelDatabase.add(btnUserManagement);
		btnUserManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameUserManagement f=FrameUserManagement.getInstance();
				if (f.updateSuccess) {
					f.setVisible(true);
				}
			}
		});
		
		JButton btnSensorManagement = new JButton("Sensor Management");
		btnSensorManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSensorManagement.setBounds(213, 35, 193, 23);
		panelDatabase.add(btnSensorManagement);
		
		JButton btnControllerManagement = new JButton("Controller Management");
		btnControllerManagement.setBounds(213, 97, 191, 23);
		panelDatabase.add(btnControllerManagement);
		
		JButton btnSiteManagement = new JButton("Site Management");
		btnSiteManagement.setBounds(213, 66, 192, 23);
		panelDatabase.add(btnSiteManagement);

		
		JPanel panelSQL = new JPanel();
		tabbedPane.addTab("SQL", null, panelSQL, null);
		panelSQL.setLayout(null);
		
		JButton btnViewReadings = new JButton("View All Readings");
		btnViewReadings.setBounds(10, 76, 377, 23);
		panelSQL.add(btnViewReadings);
		btnViewReadings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ResultSet rs=DatabaseReading.getSensorReading();
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
				DatabaseCassandra.runSQL("Run SQL", textFieldSQL.getText());
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
					DatabaseCassandra.runSQLFromFile("Run SQL From File",selectedFile.getPath());
				}
			}
		});
		btnResetDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitUI u=new WaitUI();
				u.setProgressBarMin(1);
				u.setProgressBarMax(4);
				Thread t=new Thread() {
					public void run () {
						u.setText("Step (1/3) Cassandra - Checking keyspace existence");
						u.setProgressBarValue(2);
						boolean flag=DatabaseCassandra.testKeyspace();
						if (!flag) {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"No matching keyspace to reset!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
		                	u.dispose();
		            		return;
						}
						u.setText("Step (2/3) Cassandra - Reseting keyspace");
						u.setProgressBarValue(3);
		            	flag=DatabaseCassandra.reset();
		            	if (!flag) {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Failed to reset keyspace!\nPlease check the availability of the Cassandra server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
		            	}
		            	
						u.setText("Step (3/3) HSQL - Dropping tables");
						u.setProgressBarValue(4);
		            	flag=DatabaseHSQL.reset();
		            	if (flag) {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Dropped tables successully!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
		            	} else {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Failed to drop tables!\nPlease check the availability of the HSQL server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
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
				u.setProgressBarMax(4);
				Thread t=new Thread() {
					public void run () {
						u.setText("Step (1/3) Cassandra - Checking keyspace existence");
						u.setProgressBarValue(2);
						boolean flag=DatabaseCassandra.testKeyspace();
						if (!flag) {
							flag=DatabaseCassandra.freshStart();
		                	if (!flag) {
		                		JOptionPane.showMessageDialog(SplashScreen.this,"Failed to create keyspace!\nPlease check the availability of the Cassandra server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
		                    	u.dispose();
		                		return;
		                	}
						}
						u.setText("Step (2/3) Cassandra - Creating tables");
						u.setProgressBarValue(3);
						flag=DatabaseCassandra.createTables();
		            	if (!flag) {
		            		JOptionPane.showMessageDialog(SplashScreen.this,"Failed to create table!\nPlease check if existing table is removed.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
						}
		            	
						u.setText("Step (3/3) HSQL - Creating tables");
						u.setProgressBarValue(4);
						flag=DatabaseHSQL.createTables();
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
            	u.dispose();
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
