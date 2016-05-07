package Chi;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JSeparator;

public class SplashScreen extends JFrame {
	private static final long serialVersionUID = -1439297640667763037L;
	private JPanel contentPane;
	private JButton btnStartServer;
	private JButton btnStopServer;


	public SplashScreen() {
		setTitle(Config.APP_NAME);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 403, 355);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.start();
				if (Server.started()) {
					btnStartServer.setEnabled(false);
					btnStopServer.setEnabled(true);
				}
			}
		});
		btnStartServer.setBounds(10, 11, 119, 23);
		contentPane.add(btnStartServer);
		
		JButton btnManage = new JButton("Manage");
		btnManage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnManage.setBounds(268, 11, 119, 23);
		contentPane.add(btnManage);
		
		JButton btnInitDatabase = new JButton("Init Database");
		btnInitDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                    	Database.freshStart(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
                    }
                });
			}
		});
		btnInitDatabase.setBounds(139, 11, 119, 23);
		contentPane.add(btnInitDatabase);
		
		JButton btnConfig = new JButton("Config");
		btnConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.log("Config UI started.");
				ConfigUI ui=new ConfigUI();
				Logger.log("Config UI done.");
				ui.setVisible(true);
				Logger.log("Config UI closed.");
			}
		});
		btnConfig.setBounds(268, 45, 119, 23);
		contentPane.add(btnConfig);
		
		btnStopServer = new JButton("Stop Server");
		btnStopServer.setEnabled(false);
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.stop();
				if (!Server.started()) {
					btnStartServer.setEnabled(true);
					btnStopServer.setEnabled(false);
				}
			}
		});
		btnStopServer.setBounds(10, 45, 119, 23);
		contentPane.add(btnStopServer);
		
		JLabel lblAppLogo = new JLabel();
		lblAppLogo.setBounds(307, 235, 80, 80);
		lblAppLogo.setIcon(Utility.resizeImageIcon(Theme.getIcon("ChiLogo"), lblAppLogo.getWidth(), lblAppLogo.getHeight()));
		contentPane.add(lblAppLogo);
		
		JButton btnResetDatabase = new JButton("Reset Database");
		btnResetDatabase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	Database.reset(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
			}
		});
		btnResetDatabase.setBounds(139, 79, 119, 23);
		contentPane.add(btnResetDatabase);
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(268, 79, 119, 23);
		contentPane.add(btnExit);
		
		JButton btnCreateTables = new JButton("Create Tables");
		btnCreateTables.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            	Database.createTables(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
			}
		});
		btnCreateTables.setBounds(139, 45, 119, 23);
		contentPane.add(btnCreateTables);
		
		JButton btnCreateUser = new JButton("Create User");
		btnCreateUser.setBounds(10, 126, 119, 23);
		contentPane.add(btnCreateUser);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 113, 377, 7);
		contentPane.add(separator);
		
		JButton btnDeleteUser = new JButton("Delete User");
		btnDeleteUser.setBounds(10, 157, 119, 23);
		contentPane.add(btnDeleteUser);
		
		JButton btnViewUsers = new JButton("View Users");
		btnViewUsers.setBounds(10, 188, 119, 23);
		contentPane.add(btnViewUsers);
		
		JButton btnCreateController = new JButton("Create Controller");
		btnCreateController.setBounds(139, 126, 119, 23);
		contentPane.add(btnCreateController);
		
		JButton btnDeleteController = new JButton("Delete Controller");
		btnDeleteController.setBounds(139, 157, 119, 23);
		contentPane.add(btnDeleteController);
		
		JButton btnCreateSensor = new JButton("Create Sensor");
		btnCreateSensor.setBounds(268, 126, 119, 23);
		contentPane.add(btnCreateSensor);
		
		JButton btnDeleteSensor = new JButton("Delete Sensor");
		btnDeleteSensor.setBounds(268, 157, 119, 23);
		contentPane.add(btnDeleteSensor);
		
		JButton btnViewControllers = new JButton("View Controllers");
		btnViewControllers.setBounds(139, 188, 119, 23);
		contentPane.add(btnViewControllers);
		
		JButton btnViewSensors = new JButton("View Sensors");
		btnViewSensors.setBounds(268, 188, 119, 23);
		contentPane.add(btnViewSensors);
		
		JButton btnViewReadings = new JButton("View Readings");
		btnViewReadings.setBounds(10, 235, 119, 23);
		contentPane.add(btnViewReadings);
	}
}
