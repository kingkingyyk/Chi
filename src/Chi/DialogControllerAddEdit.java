package Chi;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import javax.swing.border.BevelBorder;

public class DialogControllerAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private static String [] reportTimeoutUnit={"Seconds","Minutes","Hours"};
	private static int [] reportTimeoutUnitFactor={1,60,3600};
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	public boolean updated=false;
	private JButton okButton;
	private JButton cancelButton;
	private JTextField textFieldReportTimeout;
	private JComboBox<String> comboBoxReportTimeoutUnit;
	private JComboBox<String> comboBoxSite;
	private JLabel lblPositionTarget;
	private boolean positionTargetDragged=false;
	private int [] positionTargetCoor=new int [2];
	private JLabel lblPositionMap;
	private JPanel panelMap;
	private String currentMapURL;
	private JLabel lblReportTimeoutInfo;
	private double [] positionTargetFactor=new double [2];
	
	public DialogControllerAddEdit() {
		positionTargetFactor[0]=0.5;
		positionTargetFactor[1]=0.5;
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogControllerAddEdit(String n, String s, double px, double py, int t) {
		positionTargetFactor[0]=px;
		positionTargetFactor[1]=py;
		create();
		prefill(n,t);
		uiActionsNormal();
		uiActionsEdit(n,s);
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Controller");
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 610, 480);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 604, 409);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JLabel lblName = new JLabel("Name :");
			lblName.setBounds(10, 11, 85, 14);
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(105, 8, 241, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
		}
		{
			lblNameInfo = new JLabel("");
			lblNameInfo.setBounds(356, 9, 238, 16);
			contentPanel.add(lblNameInfo);
		}
		
		JLabel lblReportTimeout = new JLabel("Report Timeout :");
		lblReportTimeout.setHorizontalAlignment(SwingConstants.RIGHT);
		lblReportTimeout.setBounds(10, 42, 85, 14);
		contentPanel.add(lblReportTimeout);
		
		textFieldReportTimeout = new JTextField();
		textFieldReportTimeout.setText("10");
		textFieldReportTimeout.setBounds(105, 39, 86, 20);
		contentPanel.add(textFieldReportTimeout);
		textFieldReportTimeout.setColumns(10);
		
		comboBoxReportTimeoutUnit = new JComboBox<>();
		comboBoxReportTimeoutUnit.setModel(new DefaultComboBoxModel<String>(reportTimeoutUnit));
		comboBoxReportTimeoutUnit.setBounds(201, 39, 65, 20);
		contentPanel.add(comboBoxReportTimeoutUnit);
		
		JLabel lblSite = new JLabel("Site :");
		lblSite.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSite.setBounds(10, 73, 85, 14);
		contentPanel.add(lblSite);
		
		comboBoxSite = new JComboBox<>();
		comboBoxSite.setBounds(105, 70, 201, 20);
		comboBoxSite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String url=Cache.Sites.map.get(comboBoxSite.getSelectedItem()).getSitemapurl();
				if (currentMapURL==null || !currentMapURL.equals(url)) {
					currentMapURL=url;
					drawMap(currentMapURL);
				}
			}
		});
		contentPanel.add(comboBoxSite);
		
		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setHorizontalAlignment(SwingConstants.LEFT);
		lblPosition.setBounds(12, 98, 75, 14);
		contentPanel.add(lblPosition);
		
		panelMap = new JPanel();
		panelMap.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelMap.setBounds(10, 120, 584, 278);
		contentPanel.add(panelMap);
		panelMap.setLayout(null);
		
		lblPositionTarget = new JLabel();
		lblPositionTarget.setIcon(Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/POINT.png")),32,32));
		lblPositionTarget.setFont(new Font("Tahoma", Font.PLAIN, 29));
		lblPositionTarget.setHorizontalAlignment(SwingConstants.CENTER);
		lblPositionTarget.setForeground(Color.WHITE);
		lblPositionTarget.setVisible(false);
		lblPositionTarget.setBounds(270, 102, 32, 32);
		lblPositionTarget.addMouseListener(new MouseListener() {
			@Override public void mouseClicked(MouseEvent e) {}
			@Override public void mouseEntered(MouseEvent e) {}
			@Override public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {
				positionTargetDragged=true;
				positionTargetCoor[0]=e.getX();
				positionTargetCoor[1]=e.getY();
			}
			@Override public void mouseReleased(MouseEvent e) {positionTargetDragged=false;}
		});
		lblPositionTarget.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				if (positionTargetDragged) {
					int x=lblPositionTarget.getX()+e.getX()-positionTargetCoor[0];
					int y=lblPositionTarget.getY()+e.getY()-positionTargetCoor[1];
					x=Math.max(lblPositionMap.getX(), x);
					x=Math.min(x, lblPositionMap.getX()+lblPositionMap.getWidth()-lblPositionTarget.getWidth());
					y=Math.max(lblPositionMap.getY(), y);
					y=Math.min(y, lblPositionMap.getY()+lblPositionMap.getHeight()-lblPositionTarget.getHeight());
					lblPositionTarget.setLocation(x,y);
					
					positionTargetFactor[0]=(lblPositionTarget.getX()-lblPositionMap.getX())/(lblPositionMap.getWidth()+0.0);
					positionTargetFactor[1]=(lblPositionTarget.getY()-lblPositionMap.getY())/(lblPositionMap.getHeight()+0.0);
				}
			}

			@Override public void mouseMoved(MouseEvent e) {}
			
		});
		panelMap.add(lblPositionTarget);
		
		lblPositionMap = new JLabel("Loading Image");
		lblPositionMap.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPositionMap.setBackground(Color.WHITE);
		lblPositionMap.setBounds(0, 0, 584, 278);
		lblPositionMap.setHorizontalAlignment(SwingConstants.CENTER);
		lblPositionMap.setForeground(Color.BLACK);
		panelMap.add(lblPositionMap);
		
		lblReportTimeoutInfo = new JLabel("");
		lblReportTimeoutInfo.setBounds(275, 42, 104, 16);
		contentPanel.add(lblReportTimeoutInfo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 418, 604, 33);
			getContentPane().add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				okButton = new JButton("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
	
	private void prefill(String n, int t) {
		textFieldName.setText(n);
		for (int i=reportTimeoutUnit.length-1;i>=0;i--) {
			if (t%reportTimeoutUnitFactor[i]==0) {
				textFieldReportTimeout.setText(String.valueOf(t/reportTimeoutUnitFactor[i]));
				comboBoxReportTimeoutUnit.setSelectedIndex(i);
				break;
			}
		}
	}
	
	private void drawMap (String u) {
		lblPositionMap.setIcon(null);
		lblPositionMap.setText("Loading Image");
		lblPositionTarget.setVisible(false);
		Thread t=new Thread() {
			public void run () {
				try {
					URL imgURL=new URL(u);
					BufferedImage img=ImageIO.read(imgURL);
					if (currentMapURL.equals(u)) {
						//The loading might be slow. Once loaded, we should check whether it is the current selected map or not, before changing the picture.
						double resizeFactor=Math.min((lblPositionMap.getWidth()+0.0)/img.getWidth(),(lblPositionMap.getHeight()+0.0)/img.getHeight());
						ImageIcon ic=new ImageIcon(img.getScaledInstance((int)(img.getWidth()*resizeFactor), (int)(img.getHeight()*resizeFactor),Image.SCALE_SMOOTH));
						lblPositionMap.setSize(ic.getIconWidth(),ic.getIconHeight());
						lblPositionMap.setText("");
						lblPositionMap.setIcon(ic);
						
						int offsetX=(panelMap.getWidth()-ic.getIconWidth())/2;
						int offsetY=(panelMap.getHeight()-ic.getIconHeight())/2;
						lblPositionMap.setLocation(offsetX, offsetY);
						
						lblPositionTarget.setVisible(true);
						lblPositionTarget.setLocation(offsetX+(int)(positionTargetFactor[0]*ic.getIconWidth()),offsetY+(int)(positionTargetFactor[1]*ic.getIconHeight()));
					}
				} catch (IOException e) {
					if (currentMapURL.equals(u)) {
						lblPositionMap.setIcon(null);
						lblPositionMap.setText("Image loading failed.");
						Logger.log(Logger.LEVEL_ERROR,"DialogControllerAddEdit - drawMap - "+e.getMessage());
					}
				}
			}
		};
		t.start();
	}
	
	private void uiActionsNormal() {
		for (String s : Cache.Sites.map.keySet()) {
			comboBoxSite.addItem(s);
		}
		
		textFieldReportTimeout.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldReportTimeout.getText();
				if (txt==null || txt.isEmpty()) { 
					lblReportTimeoutInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else {
					try {
						int i=Integer.parseInt(txt);
						if (i>0) {
							lblReportTimeoutInfo.setText("<html><font color=\"green\">OK!</font></html>");
						} else {
							lblReportTimeoutInfo.setText("<html><font color=\"red\">Must be more than 0.</font></html>");
						}
					} catch (NumberFormatException ex) {
						lblReportTimeoutInfo.setText("<html><font color=\"red\">Must be a number!</font></html>");
					}
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
	
	private void uiActionsAdd() {
		setTitle("Add Controller");
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Controller ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.Controllers.map.containsKey(s));
		textFieldName.setText(s);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.Controllers.map.containsKey(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
				} else if (!Utility.validateName(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Contains invalid character</font></html>");
				} else {
					lblNameInfo.setText("<html><font color=\"green\">OK!</font></html>");
				}
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public boolean flag;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String txt=textFieldName.getText();
				boolean reportTimeOK=false;
				try {
					int i=Integer.parseInt(textFieldReportTimeout.getText());
					reportTimeOK=(i>0);
				} catch (NumberFormatException e) {};
				
				if (txt==null || txt.isEmpty() || Cache.Controllers.map.containsKey(txt) || !reportTimeOK || !Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Site",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating controller");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseController.createController(textFieldName.getText(),(String)comboBoxSite.getSelectedItem(),positionTargetFactor[0],positionTargetFactor[1],Integer.parseInt(textFieldReportTimeout.getText())*DialogControllerAddEdit.reportTimeoutUnitFactor[comboBoxReportTimeoutUnit.getSelectedIndex()]);
							u.dispose();
						}
					};
					t.start();
					u.setVisible(true);
					
					if (flag) {
						updated=true;
						dispose();
					} else {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	private void uiActionsEdit(String n, String s) {
		setTitle("Edit Controller "+n);
		comboBoxSite.setSelectedItem(s);
		lblNameInfo.setText("<html><font color=\"green\">OK!</font></html>");
		lblReportTimeoutInfo.setText("<html><font color=\"green\">OK!</font></html>");
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.Controllers.map.containsKey(txt) && !txt.equals(n)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
				} else if (!Utility.validateName(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Contains invalid character</font></html>");
				} else {
					lblNameInfo.setText("<html><font color=\"green\">OK!</font></html>");
				}
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public boolean flag;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String txt=textFieldName.getText();
				boolean reportTimeOK=false;
				try {
					int i=Integer.parseInt(textFieldReportTimeout.getText());
					reportTimeOK=(i>0);
				} catch (NumberFormatException e) {};
				
				if (txt==null || txt.isEmpty() || (Cache.Controllers.map.containsKey(txt) && !txt.equals(n)) || !reportTimeOK || !Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Site",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating controller");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseController.updateController(n,textFieldName.getText(),(String)comboBoxSite.getSelectedItem(),positionTargetFactor[0],positionTargetFactor[1],Integer.parseInt(textFieldReportTimeout.getText())*DialogControllerAddEdit.reportTimeoutUnitFactor[comboBoxReportTimeoutUnit.getSelectedIndex()]);
							u.dispose();
						}
					};
					t.start();
					u.setVisible(true);
					
					if (flag) {
						updated=true;
						dispose();
					} else {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
}
