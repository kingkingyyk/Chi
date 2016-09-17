package Chi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

public class MenuUI extends JFrame {
	private static final long serialVersionUID = -1439297640667763037L;
	private JPanel contentPane;
	private JButton btnStartDataServer;
	private JButton btnStopDataServer;
	private JTextField textFieldSQL;
	private JButton btnStopGWTServer;
	private JButton btnStopSchedulingServer;
	private JButton btnStartSchedulingServer;
	private JButton btnStartGWTServer;
	private JButton btnOngoingSchedules;
	private JButton btnStartNotificationServer;
	private JButton btnStopNotificationServer;

	public MenuUI() {
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
		tabbedPane.setBounds(10, 11, 419, 181);
		contentPane.add(tabbedPane);
		
		JPanel panelServer = new JPanel();
		tabbedPane.addTab("Server", null, panelServer, null);
		panelServer.setLayout(null);
		
		btnStartDataServer = new JButton("Start Data Server");
		btnStartDataServer.setBounds(34, 11, 153, 23);
		panelServer.add(btnStartDataServer);
		btnStartDataServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataServer.start();
				if (DataServer.started()) {
					btnStartDataServer.setEnabled(false);
					btnStopDataServer.setEnabled(true);
				}
			}
		});
		
		btnStopDataServer = new JButton("Stop Data Server");
		btnStopDataServer.setBounds(229, 11, 153, 23);
		panelServer.add(btnStopDataServer);
		btnStopDataServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DataServer.stop();
				if (!DataServer.started()) {
					btnStartDataServer.setEnabled(true);
					btnStopDataServer.setEnabled(false);
				}
			}
		});
		btnStopDataServer.setEnabled(false);
		
		btnStartGWTServer = new JButton("Start GWT Server");
		btnStartGWTServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (GWTServer.start()) {
					btnStartGWTServer.setEnabled(false);
					btnStopGWTServer.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null,"Failed to start GWT server.","GWT Server",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnStartGWTServer.setBounds(34, 46, 153, 23);
		panelServer.add(btnStartGWTServer);
		
		btnStopGWTServer = new JButton("Stop GWT Server");
		btnStopGWTServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (GWTServer.stop()) {
					btnStopGWTServer.setEnabled(false);
					btnStartGWTServer.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null,"Failed to stop GWT server.","GWT Server",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnStopGWTServer.setEnabled(false);
		btnStopGWTServer.setBounds(229, 45, 153, 23);
		panelServer.add(btnStopGWTServer);
		
		btnStartSchedulingServer = new JButton("Start Scheduling Server");
		btnStartSchedulingServer.setBounds(34, 80, 153, 23);
		panelServer.add(btnStartSchedulingServer);
		btnStartSchedulingServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (SchedulingServer.start()) {
					btnStartSchedulingServer.setEnabled(false);
					btnStopSchedulingServer.setEnabled(true);
					btnOngoingSchedules.setEnabled(true);
				} else {
					JOptionPane.showMessageDialog(null,"Failed to connect to database. Server will now shut down.","Scheduling Server",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		btnStopSchedulingServer = new JButton("Stop Scheduling Server");
		btnStopSchedulingServer.setEnabled(false);
		btnStopSchedulingServer.setBounds(229, 79, 153, 23);
		btnStopSchedulingServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SchedulingServer.stop();
				if (!SchedulingServer.started()) {
					btnStartSchedulingServer.setEnabled(true);
					btnStopSchedulingServer.setEnabled(false);
					btnOngoingSchedules.setEnabled(false);
				}
			}
		});
		panelServer.add(btnStopSchedulingServer);
		
		btnStartNotificationServer = new JButton("Start Notification Server");
		btnStartNotificationServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NotificationServer.start();
				btnStartNotificationServer.setEnabled(false);
				btnStopNotificationServer.setEnabled(true);
			}
		});
		btnStartNotificationServer.setBounds(34, 114, 153, 23);
		panelServer.add(btnStartNotificationServer);
		
		btnStopNotificationServer = new JButton("Stop Notification Server");
		btnStopNotificationServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NotificationServer.stop();
				btnStartNotificationServer.setEnabled(true);
				btnStopNotificationServer.setEnabled(false);
			}
		});
		btnStopNotificationServer.setEnabled(false);
		btnStopNotificationServer.setBounds(229, 114, 153, 23);
		panelServer.add(btnStopNotificationServer);
		
		JPanel panelDatabase = new JPanel();
		tabbedPane.addTab("Database", null, panelDatabase, null);
		panelDatabase.setLayout(null);
		
		JButton btnCreateTables = new JButton("Create Tables");
		btnCreateTables.setBounds(10, 5, 109, 23);
		panelDatabase.add(btnCreateTables);
		
		JButton btnResetDatabase = new JButton("Reset Database");
		btnResetDatabase.setBounds(10, 35, 109, 23);
		panelDatabase.add(btnResetDatabase);
		
		JButton btnUser = new JButton("User");
		btnUser.setBounds(129, 5, 130, 23);
		panelDatabase.add(btnUser);
		btnUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameUserManagement f=FrameUserManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		
		JButton btnSensor = new JButton("Sensor");
		btnSensor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameSensorManagement f=FrameSensorManagement.getInstance();
				f.setVisible(true);
			}
		});
		btnSensor.setBounds(129, 35, 130, 23);
		panelDatabase.add(btnSensor);
		
		JButton btnController = new JButton("Controller");
		btnController.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameControllerManagement f=FrameControllerManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnController.setBounds(129, 103, 130, 23);
		panelDatabase.add(btnController);
		
		JButton btnSite = new JButton("Site");
		btnSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameSiteManagement f=FrameSiteManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnSite.setBounds(130, 69, 130, 23);
		panelDatabase.add(btnSite);
		
		JButton btnSensorClass = new JButton("Sensor Class");
		btnSensorClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameSensorClassManagement f=FrameSensorClassManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnSensorClass.setBounds(269, 5, 135, 23);
		panelDatabase.add(btnSensorClass);
		
		JButton btnActuator = new JButton("Actuator");
		btnActuator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameActuatorManagement f=FrameActuatorManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnActuator.setBounds(269, 35, 135, 23);
		panelDatabase.add(btnActuator);
		
		JButton btnClearReadings = new JButton("Clear Readings");
		btnClearReadings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DatabaseReading.clearReading("testTemp");
			}
		});
		btnClearReadings.setBounds(10, 69, 109, 23);
		panelDatabase.add(btnClearReadings);
		
		JPanel panelScheduling = new JPanel();
		tabbedPane.addTab("Scheduling", null, panelScheduling, null);
		panelScheduling.setLayout(null);
		
		JButton btnDayScheduleRules = new JButton("Day Schedule Rules");
		btnDayScheduleRules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameDayScheduleRuleManagement f=FrameDayScheduleRuleManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnDayScheduleRules.setBounds(10, 11, 127, 23);
		panelScheduling.add(btnDayScheduleRules);
		
		JButton btnRegularSchedules = new JButton("Regular Schedules");
		btnRegularSchedules.setBounds(145, 11, 127, 23);
		btnRegularSchedules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameRegularScheduleManagement f=FrameRegularScheduleManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		panelScheduling.add(btnRegularSchedules);
		
		JButton btnSpecialSchedules = new JButton("Special Schedules");
		btnSpecialSchedules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameSpecialScheduleManagement f=FrameSpecialScheduleManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnSpecialSchedules.setBounds(277, 11, 127, 23);
		panelScheduling.add(btnSpecialSchedules);
		
		btnOngoingSchedules = new JButton("Ongoing Schedules");
		btnOngoingSchedules.setEnabled(false);
		btnOngoingSchedules.setBounds(10, 119, 127, 23);
		btnOngoingSchedules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameOngoingSchedules f=FrameOngoingSchedules.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		panelScheduling.add(btnOngoingSchedules);
		
		JPanel panelNotification = new JPanel();
		tabbedPane.addTab("Notification", null, panelNotification, null);
		panelNotification.setLayout(null);
		
		JButton btnNotification = new JButton("Notification Center");
		btnNotification.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameNotification f=FrameNotification.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnNotification.setBounds(10, 11, 131, 23);
		panelNotification.add(btnNotification);
		
		JPanel panelSQL = new JPanel();
		tabbedPane.addTab("SQL", null, panelSQL, null);
		panelSQL.setLayout(null);
		
		JButton btnViewReadings = new JButton("View All Readings");
		btnViewReadings.setBounds(10, 76, 377, 23);
		panelSQL.add(btnViewReadings);
		btnViewReadings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameReading fr=new FrameReading();
				fr.setVisible(true);/*
				ArrayList<SensorReading> list=DatabaseReading.getReadingMonthly("testTemp",2016,9);
				ReadingExport.export(list); */
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
		
		JPanel panelConfig = new JPanel();
		tabbedPane.addTab("Configurations", null, panelConfig, null);
		panelConfig.setLayout(null);
		
		JButton btnConfig = new JButton("Config");
		btnConfig.setBounds(10, 11, 96, 23);
		panelConfig.add(btnConfig);
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.log("Config UI started.");
				ConfigUI ui=new ConfigUI();
				Logger.log("Config UI done.");
				ui.setLocationRelativeTo(null);
				ui.setVisible(true);
				Logger.log("Config UI closed.");
			}
		});
		btnLoadSQLFromFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc=new JFileChooser();
				if (fc.showOpenDialog(MenuUI.this)==JFileChooser.APPROVE_OPTION) {
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
		            		JOptionPane.showMessageDialog(MenuUI.this,"No matching keyspace to reset!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
		                	u.dispose();
		            		return;
						}
						u.setText("Step (2/3) Cassandra - Reseting keyspace");
						u.setProgressBarValue(3);
		            	flag=DatabaseCassandra.reset();
		            	if (!flag) {
		            		JOptionPane.showMessageDialog(MenuUI.this,"Failed to reset keyspace!\nPlease check the availability of the Cassandra server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
		            	}
		            	
						u.setText("Step (3/3) HSQL - Dropping tables");
						u.setProgressBarValue(4);
		            	flag=DatabaseHSQL.reset();
		            	if (flag) {
		            		JOptionPane.showMessageDialog(MenuUI.this,"Dropped tables successully!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
		            	} else {
		            		JOptionPane.showMessageDialog(MenuUI.this,"Failed to drop tables!\nPlease check the availability of the HSQL server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
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
		                		JOptionPane.showMessageDialog(MenuUI.this,"Failed to create keyspace!\nPlease check the availability of the Cassandra server.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
		                    	u.dispose();
		                		return;
		                	}
						}
						u.setText("Step (2/3) Cassandra - Creating tables");
						u.setProgressBarValue(3);
						flag=DatabaseCassandra.createTables();
		            	if (!flag) {
		            		JOptionPane.showMessageDialog(MenuUI.this,"Failed to create table!\nPlease check if existing table is removed.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
						}
		            	
						u.setText("Step (3/3) HSQL - Creating tables");
						u.setProgressBarValue(4);
						flag=DatabaseHSQL.createTables();
		            	if (flag) {
		            		JOptionPane.showMessageDialog(MenuUI.this,"Tables created successfully!",Config.APP_NAME,JOptionPane.INFORMATION_MESSAGE);
						} else { 
		            		JOptionPane.showMessageDialog(MenuUI.this,"Failed to create table!\nPlease check if existing table is removed.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
						}
		            	u.dispose();
					}
				};
				t.start();
				u.setVisible(true);
            	u.dispose();
			}
		});
	}
}
