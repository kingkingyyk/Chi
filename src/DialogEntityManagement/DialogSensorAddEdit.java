package DialogEntityManagement;

import java.awt.FlowLayout;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Chi.Config;
import Chi.Logger;
import Chi.Theme;
import Chi.Utility;
import Chi.WaitUI;
import Database.Cache;
import Database.DatabaseSensor;

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
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

public class DialogSensorAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	public boolean updated=false;
	private JTextField textFieldMinValue;
	private JTextField textFieldMaxValue;
	private JTextField textFieldTransFactor;
	private JTextField textFieldUnit;
	private JLabel lblMinValueInfo;
	private JLabel lblMaxValueInfo;
	private JLabel lblTransFacInfo;
	private JLabel lblUnitInfo;
	private JComboBox<String> comboBoxClass;
	private JButton cancelButton;
	private JButton okButton;
	private JComboBox<String> comboBoxController;
	private JTextField textFieldMinThreshold;
	private JTextField textFieldMaxThreshold;
	private JLabel lblMinThresholdInfo;
	private JLabel lblMaxThresholdInfo;
	private JLabel lblPositionMap;
	private JPanel panelMap;
	private JLabel lblPositionTarget;
	private boolean positionTargetDragged=false;
	private int [] positionTargetCoor=new int [2];
	private double [] positionTargetFactor=new double [2];
	private String currentMapURL;

	public DialogSensorAddEdit() {
		positionTargetFactor[0]=0.5;
		positionTargetFactor[1]=0.5;
		create();
		
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Sensor ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.Sensors.map.containsKey(s));
		
		uiActionsNormal();
		prefill(s,"DefaultClass",0,1,1,"","",0,1);
		uiActionsAdd();
	}
	
	public DialogSensorAddEdit(String n, String c, double min, double max, double t, String u, String con, double minT, double maxT, double px, double py) {
		positionTargetFactor[0]=px;
		positionTargetFactor[1]=py;
		create();
		uiActionsNormal();
		prefill(n,c,min,max,t,u,con,minT,maxT);
		uiActionsEdit(n);
	}
	
	private boolean validateInput () {
		JTextField [] numberFields=new JTextField [] {textFieldMinValue,textFieldMaxValue,textFieldTransFactor,textFieldMinThreshold,textFieldMaxThreshold};
		JTextField [] fillFields=new JTextField[] {textFieldName,textFieldUnit};
		for (int i=0;i<numberFields.length;i++) {
			try {
				Double.parseDouble(numberFields[i].getText());
			} catch (Exception e) {
				return false;
			}
		}
		for (int i=0;i<fillFields.length;i++) {
			if (fillFields[i].getText()==null || fillFields[i].getText().isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	private void create () {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 605, 554);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 554, 191);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JLabel lblName = new JLabel("Name :");
			lblName.setBounds(29, 11, 55, 14);
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(89, 8, 246, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
		}
		{
			lblNameInfo = new JLabel("");
			lblNameInfo.setBounds(345, 9, 124, 16);
			contentPanel.add(lblNameInfo);
		}
		{
			JLabel lblClass = new JLabel("Class :");
			lblClass.setBounds(31, 41, 53, 14);
			lblClass.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblClass);
		}
		
		comboBoxClass = new JComboBox<>();
		comboBoxClass.setMaximumRowCount(6);
		comboBoxClass.setBounds(89, 38, 246, 20);
		for (String s : Cache.SensorClasses.map.keySet()) {
			comboBoxClass.addItem(s);
		}
		contentPanel.add(comboBoxClass);
		
		textFieldMinValue = new JTextField();
		textFieldMinValue.setColumns(30);
		textFieldMinValue.setBounds(89, 69, 55, 20);
		contentPanel.add(textFieldMinValue);
		
		JLabel lblMinValue = new JLabel("Min Value :");
		lblMinValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMinValue.setBounds(31, 72, 53, 14);
		contentPanel.add(lblMinValue);
		
		textFieldMaxValue = new JTextField();
		textFieldMaxValue.setText("1");
		textFieldMaxValue.setColumns(30);
		textFieldMaxValue.setBounds(355, 69, 55, 20);
		contentPanel.add(textFieldMaxValue);
		
		JLabel lblMaxValue = new JLabel("Max Value :");
		lblMaxValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaxValue.setBounds(271, 72, 74, 14);
		contentPanel.add(lblMaxValue);
		
		lblMinValueInfo = new JLabel("");
		lblMinValueInfo.setBounds(154, 71, 124, 16);
		contentPanel.add(lblMinValueInfo);
		
		lblMaxValueInfo = new JLabel("");
		lblMaxValueInfo.setBounds(420, 71, 124, 16);
		contentPanel.add(lblMaxValueInfo);
		
		textFieldUnit = new JTextField();
		textFieldUnit.setBounds(355, 98, 55, 20);
		contentPanel.add(textFieldUnit);
		textFieldUnit.setColumns(30);
		
		JLabel lblTransFactor = new JLabel("Trans. Factor :");
		lblTransFactor.setBounds(10, 101, 74, 14);
		contentPanel.add(lblTransFactor);
		lblTransFactor.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldTransFactor = new JTextField();
		textFieldTransFactor.setBounds(89, 100, 55, 20);
		contentPanel.add(textFieldTransFactor);
		textFieldTransFactor.setText("1");
		textFieldTransFactor.setColumns(30);
		
		lblTransFacInfo = new JLabel("");
		lblTransFacInfo.setBounds(154, 97, 124, 16);
		contentPanel.add(lblTransFacInfo);
		
		JLabel lblUnit = new JLabel("Unit :");
		lblUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUnit.setBounds(271, 97, 74, 14);
		contentPanel.add(lblUnit);
		
		lblUnitInfo = new JLabel();
		lblUnitInfo.setBounds(420, 98, 124, 16);
		contentPanel.add(lblUnitInfo);
		
		comboBoxController = new JComboBox<>();
		comboBoxController.setBounds(89, 131, 246, 20);
		comboBoxController.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String url=Cache.Controllers.map.get(comboBoxController.getSelectedItem()).getSite().getSitemapurl();
				if (currentMapURL==null || !currentMapURL.equals(url)) {
					currentMapURL=url;
					drawMap(currentMapURL);
				}
			}
		});
		contentPanel.add(comboBoxController);
		
		JLabel lblController = new JLabel("Attached On :");
		lblController.setHorizontalAlignment(SwingConstants.RIGHT);
		lblController.setBounds(10, 134, 74, 14);
		contentPanel.add(lblController);
		
		textFieldMinThreshold = new JTextField();
		textFieldMinThreshold.setBounds(89, 162, 55, 20);
		contentPanel.add(textFieldMinThreshold);
		textFieldMinThreshold.setText("0.0");
		textFieldMinThreshold.setColumns(30);
		
		lblMinThresholdInfo = new JLabel("");
		lblMinThresholdInfo.setBounds(154, 163, 124, 16);
		contentPanel.add(lblMinThresholdInfo);
		
		JLabel lblMinThreshold = new JLabel("Min Threshold :");
		lblMinThreshold.setBounds(10, 165, 74, 14);
		contentPanel.add(lblMinThreshold);
		lblMinThreshold.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblMaxThreshold = new JLabel("Max Threshold :");
		lblMaxThreshold.setBounds(278, 165, 84, 14);
		contentPanel.add(lblMaxThreshold);
		lblMaxThreshold.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldMaxThreshold = new JTextField();
		textFieldMaxThreshold.setBounds(367, 162, 55, 20);
		contentPanel.add(textFieldMaxThreshold);
		textFieldMaxThreshold.setText("1.0");
		textFieldMaxThreshold.setColumns(30);
		
		lblMaxThresholdInfo = new JLabel("");
		lblMaxThresholdInfo.setBounds(430, 163, 124, 16);
		contentPanel.add(lblMaxThresholdInfo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 495, 599, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
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
		
		panelMap = new JPanel();
		panelMap.setLayout(null);
		panelMap.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelMap.setBounds(5, 206, 584, 278);
		getContentPane().add(panelMap);
		
		lblPositionTarget = new JLabel("");
		lblPositionTarget.setIcon(Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/POINT.png")),32,32));
		lblPositionTarget.setBounds(270, 102, 32, 32);
		panelMap.add(lblPositionTarget);
		
		lblPositionMap = new JLabel("Loading Image");
		lblPositionMap.setHorizontalAlignment(SwingConstants.CENTER);
		lblPositionMap.setForeground(Color.BLACK);
		lblPositionMap.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPositionMap.setBackground(Color.WHITE);
		lblPositionMap.setBounds(0, 0, 584, 278);
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
		panelMap.add(lblPositionMap);
	}
	
	public void prefill(String n, String c, double min, double max, double t, String u, String con, double minT, double maxT) {
		textFieldName.setText(n);
		comboBoxClass.setSelectedItem(c);
		textFieldMinValue.setText(String.valueOf(min));
		textFieldMaxValue.setText(String.valueOf(max));
		textFieldTransFactor.setText(String.valueOf(t));
		textFieldUnit.setText(u);
		comboBoxController.setSelectedItem(con);
		textFieldMinThreshold.setText(String.valueOf(minT));
		textFieldMaxThreshold.setText(String.valueOf(maxT));
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
						Logger.log(Logger.LEVEL_WARNING,"DialogControllerAddEdit - drawMap - "+e.getMessage());
					}
				}
			}
		};
		t.start();
	}
	
	public void uiActionsNormal () {
		for (String s : Cache.Controllers.map.keySet()) {
			comboBoxController.addItem(s);
		}
		
		textFieldMinValue.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					Double.parseDouble(textFieldMinValue.getText());
					lblMinValueInfo.setText("<html><font color=\"green\">OK!</font></html>");
				} catch (NumberFormatException ex) {
					lblMinValueInfo.setText("<html><font color=\"red\">Value must be a number!</font></html>");
				}
			}
		});
		
		textFieldMaxValue.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					Double.parseDouble(textFieldMaxValue.getText());
					lblMaxValueInfo.setText("<html><font color=\"green\">OK!</font></html>");
				} catch (NumberFormatException ex) {
					lblMaxValueInfo.setText("<html><font color=\"red\">Value must be a number!</font></html>");
				}
			}
		});
		
		textFieldMinThreshold.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					Double.parseDouble(textFieldMinThreshold.getText());
					lblMinThresholdInfo.setText("<html><font color=\"green\">OK!</font></html>");
				} catch (NumberFormatException ex) {
					lblMinThresholdInfo.setText("<html><font color=\"red\">Value must be a number!</font></html>");
				}
			}
		});
		
		textFieldMaxThreshold.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					Double.parseDouble(textFieldMaxThreshold.getText());
					lblMaxThresholdInfo.setText("<html><font color=\"green\">OK!</font></html>");
				} catch (NumberFormatException ex) {
					lblMaxThresholdInfo.setText("<html><font color=\"red\">Value must be a number!</font></html>");
				}
			}
		});
		
		textFieldUnit.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldUnit.getText();
				if (txt==null || txt.isEmpty()) { 
					lblUnitInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (txt.length()>10) {
					lblUnitInfo.setText("<html><font color=\"red\">Too long!</font></html>");
				} else if (txt.contains(Config.PACKET_FIELD_DELIMITER)) {
					lblUnitInfo.setText("<html><font color=\"red\">Semicolon is not allowed!</font></html>");
				} else {
					lblUnitInfo.setText("<html><font color=\"green\">OK!</font></html>");
				}
			}
		});
		
		textFieldTransFactor.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				try {
					Double.parseDouble(textFieldTransFactor.getText());
					lblTransFacInfo.setText("<html><font color=\"green\">OK!</font></html>");
				} catch (NumberFormatException ex) {
					lblTransFacInfo.setText("<html><font color=\"red\">Value must be a number!</font></html>");
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
	
	public void uiActionsAdd() {
		setTitle("Add Sensor");
		lblUnitInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (txt.length()>100) {
					lblNameInfo.setText("<html><font color=\"red\">Too long!</font></html>");
				} else if (Cache.Sensors.map.containsKey(txt)) {
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
				if (!validateInput() || Cache.Sensors.map.containsKey(txt) ||
						!Utility.validateName(txt) || comboBoxController.getItemCount()==0 ||
						comboBoxClass.getItemCount()==0 || txt.length()>100 || textFieldUnit.getText().length()>10) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Sensor",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating sensor");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseSensor.createSensor(textFieldName.getText(),(String)comboBoxClass.getSelectedItem(),
									Double.parseDouble(textFieldMinValue.getText()),Double.parseDouble(textFieldMaxValue.getText()),
									Double.parseDouble(textFieldTransFactor.getText()),textFieldUnit.getText(),
									(String)comboBoxController.getSelectedItem(),Double.parseDouble(textFieldMinThreshold.getText()),
									Double.parseDouble(textFieldMaxThreshold.getText()),positionTargetFactor[0],positionTargetFactor[1]);
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
	
	public void uiActionsEdit(String n) {
		setTitle("Edit Sensor "+n);
		lblUnitInfo.setText("<html><font color=\"green\">OK!</font></html>");
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (txt.length()>100) {
					lblNameInfo.setText("<html><font color=\"red\">Too long!</font></html>");
				} else if (Cache.Sensors.map.containsKey(txt) && !txt.equals(n)) {
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
				if (!validateInput() || (Cache.Sensors.map.containsKey(txt) && !txt.equals(n)) || !Utility.validateName(txt) ||
						comboBoxController.getSelectedIndex()==-1 || comboBoxClass.getSelectedIndex()==-1 || txt.length()>100 ||
						textFieldUnit.getText().length()>10) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Sensor",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating sensor");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseSensor.updateSensor(n,textFieldName.getText(),(String)comboBoxClass.getSelectedItem(),
									Double.parseDouble(textFieldMinValue.getText()),Double.parseDouble(textFieldMaxValue.getText()),
									Double.parseDouble(textFieldTransFactor.getText()), textFieldUnit.getText(),
									(String)comboBoxController.getSelectedItem(),Double.parseDouble(textFieldMinThreshold.getText()),
									Double.parseDouble(textFieldMaxThreshold.getText()),positionTargetFactor[0],positionTargetFactor[1]);
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
