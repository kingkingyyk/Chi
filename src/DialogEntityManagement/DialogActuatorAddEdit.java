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
import Database.DatabaseActuator;

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

public class DialogActuatorAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBoxController;
	private JLabel lblPositionTarget;
	private JLabel lblPositionMap;
	private boolean positionTargetDragged=false;
	private int [] positionTargetCoor=new int [2];
	private double [] positionTargetFactor=new double [2];
	private String currentMapURL;
	private JPanel panelMap;
	private JComboBox<String> comboBoxControlType;

	
	public DialogActuatorAddEdit() {
		positionTargetFactor[0]=0.5;
		positionTargetFactor[1]=0.5;
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogActuatorAddEdit(String n, String u, double px, double py, String type) {
		positionTargetFactor[0]=px;
		positionTargetFactor[1]=py;
		create();
		uiActionsNormal();
		prefill(n,u,type);
		uiActionsEdit(n);
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 608, 490);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 602, 391);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JLabel lblName = new JLabel("Name :");
			lblName.setBounds(10, 11, 78, 14);
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(98, 8, 206, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
		}
		{
			lblNameInfo = new JLabel("");
			lblNameInfo.setBounds(314, 9, 125, 16);
			contentPanel.add(lblNameInfo);
		}
		
		JLabel lblController = new JLabel("Attached On :");
		lblController.setHorizontalAlignment(SwingConstants.RIGHT);
		lblController.setBounds(10, 42, 78, 14);
		contentPanel.add(lblController);
		
		comboBoxController = new JComboBox<>();
		comboBoxController.setBounds(98, 39, 206, 20);
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
		
		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setHorizontalAlignment(SwingConstants.LEFT);
		lblPosition.setBounds(10, 105, 75, 14);
		contentPanel.add(lblPosition);
		
		panelMap = new JPanel();
		panelMap.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelMap.setBounds(10, 130, 584, 278);
		contentPanel.add(panelMap);
		panelMap.setLayout(null);
		
		lblPositionTarget = new JLabel("");
		lblPositionTarget.setIcon(Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/POINT.png")),32,32));
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
		lblPositionMap.setHorizontalAlignment(SwingConstants.CENTER);
		lblPositionMap.setForeground(Color.BLACK);
		lblPositionMap.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblPositionMap.setBackground(Color.WHITE);
		lblPositionMap.setBounds(0, 0, 584, 278);
		panelMap.add(lblPositionMap);
		
		comboBoxControlType = new JComboBox<String>();
		comboBoxControlType.setBounds(98, 70, 206, 20);
		comboBoxControlType.addItem("Manual");
		comboBoxControlType.addItem("Scheduled");
		comboBoxControlType.addItem("Sensor Response");
		contentPanel.add(comboBoxControlType);
		
		JLabel lblControlType = new JLabel("Control Type :");
		lblControlType.setHorizontalAlignment(SwingConstants.RIGHT);
		lblControlType.setBounds(10, 72, 78, 14);
		contentPanel.add(lblControlType);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 429, 602, 33);
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
	
	private void prefill(String n, String c, String type) {
		textFieldName.setText(n);
		comboBoxController.setSelectedItem(c);
		comboBoxControlType.setSelectedItem(type);
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
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		for (String c : Cache.Controllers.map.keySet()) {
			comboBoxController.addItem(c);
		}
	}
	
	private void uiActionsAdd() {
		setTitle("Add Actuator");
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Actuator ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.Actuators.map.containsKey(s));
		textFieldName.setText(s);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.Actuators.map.containsKey(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
				} else if (!Utility.validateName(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Contains invalid character.</font></html>");
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
				if (txt==null || txt.isEmpty() || Cache.Actuators.map.containsKey(txt) || !Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Actuator",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating actuator");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseActuator.createActuator(textFieldName.getText(),(String) comboBoxController.getSelectedItem(),positionTargetFactor[0],positionTargetFactor[1],(String)comboBoxControlType.getSelectedItem());
							u.dispose();
						}
					};
					t.start();
					u.setVisible(true);
					
					if (flag) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	
	private void uiActionsEdit(String n) {
		setTitle("Edit Actuator "+n);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.Actuators.map.containsKey(txt) && !txt.equals(n)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
				} else if (!Utility.validateName(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Contains invalid character.</font></html>");
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
				if (txt==null || txt.isEmpty() || (Cache.Actuators.map.containsKey(txt) && !txt.equals(n)) || !Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Edit Actuator",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating actuator");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseActuator.updateActuator(n, textFieldName.getText(),(String)comboBoxController.getSelectedItem(),positionTargetFactor[0],positionTargetFactor[1],(String)comboBoxControlType.getSelectedItem());
							u.dispose();
						}
					};
					t.start();
					u.setVisible(true);
					
					if (flag) {
						dispose();
					} else {
						JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
}
