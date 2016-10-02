package Chi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

public class MenuUI extends JFrame {
	private static final long serialVersionUID = -1439297640667763037L;
	private static MenuUI currObj=null;
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
	private JTextArea textAreaLog;
	private JScrollPane scrollPaneLog;

	public MenuUI() {
		setTitle(Config.APP_NAME);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 530, 412);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setFocusPainted(false);
		btnExit.setBounds(463, 6, 51, 36);
		contentPane.add(btnExit);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBounds(10, 59, 504, 181);
		contentPane.add(tabbedPane);
		
		JPanel panelServer = new JPanel();
		tabbedPane.addTab("Server", null, panelServer, null);
		panelServer.setLayout(null);
		
		btnStartDataServer = new JButton("Start");
		btnStartDataServer.setBounds(123, 11, 130, 23);
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
		
		btnStopDataServer = new JButton("Stop");
		btnStopDataServer.setBounds(273, 11, 130, 23);
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
		
		btnStartGWTServer = new JButton("Start");
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
		btnStartGWTServer.setBounds(123, 45, 130, 23);
		panelServer.add(btnStartGWTServer);
		
		btnStopGWTServer = new JButton("Stop");
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
		btnStopGWTServer.setBounds(273, 45, 130, 23);
		panelServer.add(btnStopGWTServer);
		
		btnStartSchedulingServer = new JButton("Start");
		btnStartSchedulingServer.setBounds(123, 79, 130, 23);
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
		
		btnStopSchedulingServer = new JButton("Stop");
		btnStopSchedulingServer.setEnabled(false);
		btnStopSchedulingServer.setBounds(273, 79, 130, 23);
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
		
		btnStartNotificationServer = new JButton("Start");
		btnStartNotificationServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NotificationServer.start();
				btnStartNotificationServer.setEnabled(false);
				btnStopNotificationServer.setEnabled(true);
			}
		});
		btnStartNotificationServer.setBounds(123, 114, 130, 23);
		panelServer.add(btnStartNotificationServer);
		
		btnStopNotificationServer = new JButton("Stop");
		btnStopNotificationServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NotificationServer.stop();
				btnStartNotificationServer.setEnabled(true);
				btnStopNotificationServer.setEnabled(false);
			}
		});
		btnStopNotificationServer.setEnabled(false);
		btnStopNotificationServer.setBounds(273, 114, 130, 23);
		panelServer.add(btnStopNotificationServer);
		
		JLabel lblDataServer = new JLabel("Data Server :");
		lblDataServer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDataServer.setBounds(10, 15, 103, 14);
		panelServer.add(lblDataServer);
		
		JLabel lblGWTServer = new JLabel("GWT Server :");
		lblGWTServer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblGWTServer.setBounds(10, 49, 103, 14);
		panelServer.add(lblGWTServer);
		
		JLabel lblSchedulingServer = new JLabel("Scheduling Server :");
		lblSchedulingServer.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSchedulingServer.setBounds(10, 84, 103, 14);
		panelServer.add(lblSchedulingServer);
		
		JLabel lblNewLabel = new JLabel("Notification Server :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(10, 118, 103, 14);
		panelServer.add(lblNewLabel);
		
		JPanel panelDatabase = new JPanel();
		tabbedPane.addTab("Database", null, panelDatabase, null);
		panelDatabase.setLayout(null);
		
		JButton btnCreateTables = new JButton("Create Tables");
		btnCreateTables.setBounds(10, 11, 109, 23);
		panelDatabase.add(btnCreateTables);
		
		JButton btnResetDatabase = new JButton("Reset Database");
		btnResetDatabase.setBounds(147, 11, 109, 23);
		panelDatabase.add(btnResetDatabase);
		
		JButton btnClearReadings = new JButton("Clear Readings");
		btnClearReadings.setBounds(278, 11, 109, 23);
		panelDatabase.add(btnClearReadings);
		
		JButton btnRunSQL = new JButton("Run SQL");
		btnRunSQL.setBounds(10, 45, 109, 23);
		panelDatabase.add(btnRunSQL);
		
		textFieldSQL = new JTextField();
		textFieldSQL.setBounds(129, 45, 258, 20);
		panelDatabase.add(textFieldSQL);
		textFieldSQL.setColumns(10);
		
		JButton btnLoadSQLFromFile = new JButton("Run SQL From File");
		btnLoadSQLFromFile.setBounds(10, 79, 377, 23);
		panelDatabase.add(btnLoadSQLFromFile);
		
		JButton btnViewReadings = new JButton("View All Readings");
		btnViewReadings.setBounds(10, 113, 377, 23);
		panelDatabase.add(btnViewReadings);
		btnViewReadings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameReading fr=new FrameReading();
				fr.setVisible(true);/*
				ArrayList<SensorReading> list=DatabaseReading.getReadingMonthly("testTemp",2016,9);
				ReadingExport.export(list); */
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
		btnRunSQL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DatabaseCassandra.runSQL("Run SQL", textFieldSQL.getText());
			}
		});
		btnClearReadings.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DatabaseReading.clearReading("testTemp");
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
		            	
		            	Cache.initialize(null);
		            	u.dispose();
					}
				};
				t.start();
				u.setVisible(true);
            	u.dispose();
			}
		});
		
		JPanel panelModelling = new JPanel();
		tabbedPane.addTab("Modelling", null, panelModelling, null);
		panelModelling.setLayout(null);
		
		JButton btnUser = new JButton("User");
		btnUser.setBounds(150, 5, 134, 23);
		panelModelling.add(btnUser);
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
		btnSensor.setBounds(10, 103, 130, 23);
		panelModelling.add(btnSensor);
		
		JButton btnController = new JButton("Controller");
		btnController.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameControllerManagement f=FrameControllerManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnController.setBounds(10, 35, 130, 23);
		panelModelling.add(btnController);
		
		JButton btnSite = new JButton("Site");
		btnSite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameSiteManagement f=FrameSiteManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnSite.setBounds(10, 5, 130, 23);
		panelModelling.add(btnSite);
		
		JButton btnSensorClass = new JButton("Sensor Class");
		btnSensorClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrameSensorClassManagement f=FrameSensorClassManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnSensorClass.setBounds(10, 69, 130, 23);
		panelModelling.add(btnSensorClass);
		
		JButton btnActuator = new JButton("Actuator");
		btnActuator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!DataServer.started()) JOptionPane.showMessageDialog(null, "Toggle status requires data server to be started.", "Actuator Management", JOptionPane.WARNING_MESSAGE);
				FrameActuatorManagement f=FrameActuatorManagement.getInstance();
				f.setVisible(true);
				f.toFront();
			}
		});
		btnActuator.setBounds(10, 137, 130, 23);
		panelModelling.add(btnActuator);
		
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
		
		JPanel panelConfig = new JPanel();
		tabbedPane.addTab("Configurations", null, panelConfig, null);
		panelConfig.setLayout(null);
		
		JButton btnConfig = new JButton("Config");
		btnConfig.setBounds(10, 11, 96, 23);
		panelConfig.add(btnConfig);
		
		JLabel label = new JLabel("Administration Center");
		label.setForeground(Color.WHITE);
		label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD, label.getFont().getSize() + 1f));
		label.setBounds(10, 12, 140, 23);
		contentPane.add(label);
		
		JLabel labelBackground = new JLabel();
		labelBackground.setBounds(0, 0, 524, 47);
		labelBackground.setIcon(Utility.resizeImageIcon(Theme.getIcon("TopTitleBar"),labelBackground.getWidth(),labelBackground.getHeight()));
		contentPane.add(labelBackground);
		
		scrollPaneLog = new JScrollPane();
		scrollPaneLog.setBounds(10, 251, 504, 125);
		contentPane.add(scrollPaneLog);
		
		textAreaLog = new JTextArea();
		textAreaLog.setFont(UIManager.getFont("ComboBox.font"));
		textAreaLog.setEditable(false);
		scrollPaneLog.setViewportView(textAreaLog);
		
		JLabel lblConsole = new JLabel("Console :");
		lblConsole.setBounds(10, 234, 46, 14);
		contentPane.add(lblConsole);
		
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
		
		currObj=this;
		//Autoscroll
		DefaultCaret caret = (DefaultCaret)textAreaLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	
	public void appendLog(String s) {
		textAreaLog.append(s);
		textAreaLog.append("\n");
	}
	
	public static MenuUI getCurrInstance() {
		return MenuUI.currObj;
	}
}
