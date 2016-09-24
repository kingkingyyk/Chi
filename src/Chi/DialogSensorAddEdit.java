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
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;

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

	public DialogSensorAddEdit() {
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
	
	public DialogSensorAddEdit(String n, String c, double min, double max, double t, String u, String con, double minT, double maxT) {
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
		setBounds(100, 100, 485, 360);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 479, 216);
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
		textFieldMinValue.setBounds(89, 69, 246, 20);
		contentPanel.add(textFieldMinValue);
		
		JLabel lblMinValue = new JLabel("Min Value :");
		lblMinValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMinValue.setBounds(31, 72, 53, 14);
		contentPanel.add(lblMinValue);
		
		textFieldMaxValue = new JTextField();
		textFieldMaxValue.setText("1");
		textFieldMaxValue.setColumns(30);
		textFieldMaxValue.setBounds(89, 100, 246, 20);
		contentPanel.add(textFieldMaxValue);
		
		JLabel lblMaxValue = new JLabel("Max Value :");
		lblMaxValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaxValue.setBounds(10, 103, 74, 14);
		contentPanel.add(lblMaxValue);
		
		lblMinValueInfo = new JLabel("");
		lblMinValueInfo.setBounds(345, 70, 124, 16);
		contentPanel.add(lblMinValueInfo);
		
		lblMaxValueInfo = new JLabel("");
		lblMaxValueInfo.setBounds(345, 101, 124, 16);
		contentPanel.add(lblMaxValueInfo);
		
		textFieldUnit = new JTextField();
		textFieldUnit.setBounds(89, 161, 246, 20);
		contentPanel.add(textFieldUnit);
		textFieldUnit.setColumns(30);
		
		JLabel lblTransFactor = new JLabel("Trans. Factor :");
		lblTransFactor.setBounds(10, 133, 74, 14);
		contentPanel.add(lblTransFactor);
		lblTransFactor.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldTransFactor = new JTextField();
		textFieldTransFactor.setBounds(89, 131, 246, 20);
		contentPanel.add(textFieldTransFactor);
		textFieldTransFactor.setText("1");
		textFieldTransFactor.setColumns(30);
		
		lblTransFacInfo = new JLabel("");
		lblTransFacInfo.setBounds(345, 132, 124, 16);
		contentPanel.add(lblTransFacInfo);
		
		JLabel lblUnit = new JLabel("Unit :");
		lblUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUnit.setBounds(10, 163, 74, 14);
		contentPanel.add(lblUnit);
		
		lblUnitInfo = new JLabel();
		lblUnitInfo.setBounds(345, 163, 124, 16);
		contentPanel.add(lblUnitInfo);
		
		comboBoxController = new JComboBox<>();
		comboBoxController.setBounds(89, 192, 246, 20);
		for (String s : Cache.Controllers.map.keySet()) {
			comboBoxController.addItem(s);
		}
		contentPanel.add(comboBoxController);
		
		JLabel lblController = new JLabel("Attached On :");
		lblController.setHorizontalAlignment(SwingConstants.RIGHT);
		lblController.setBounds(10, 195, 74, 14);
		contentPanel.add(lblController);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 298, 479, 33);
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
		
		JLabel lblMinThreshold = new JLabel("Min Threshold :");
		lblMinThreshold.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMinThreshold.setBounds(10, 239, 74, 14);
		getContentPane().add(lblMinThreshold);
		
		textFieldMinThreshold = new JTextField();
		textFieldMinThreshold.setText("0.0");
		textFieldMinThreshold.setColumns(30);
		textFieldMinThreshold.setBounds(89, 236, 246, 20);
		getContentPane().add(textFieldMinThreshold);
		
		JLabel lblMaxThreshold = new JLabel("Max Threshold :");
		lblMaxThreshold.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMaxThreshold.setBounds(0, 270, 84, 14);
		getContentPane().add(lblMaxThreshold);
		
		textFieldMaxThreshold = new JTextField();
		textFieldMaxThreshold.setText("1.0");
		textFieldMaxThreshold.setColumns(30);
		textFieldMaxThreshold.setBounds(89, 267, 246, 20);
		getContentPane().add(textFieldMaxThreshold);
		
		lblMinThresholdInfo = new JLabel("");
		lblMinThresholdInfo.setBounds(345, 237, 124, 16);
		getContentPane().add(lblMinThresholdInfo);
		
		lblMaxThresholdInfo = new JLabel("");
		lblMaxThresholdInfo.setBounds(345, 268, 124, 16);
		getContentPane().add(lblMaxThresholdInfo);
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
	
	public void uiActionsNormal () {
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
				} else if (Cache.Sensors.map.containsKey(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
				} else if (txt.contains(Config.PACKET_FIELD_DELIMITER)) {
					lblNameInfo.setText("<html><font color=\"red\">Semicolon is not allowed!</font></html>");
				} else {
					lblNameInfo.setText("<html><font color=\"green\">OK!</font></html>");
				}
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public boolean flag;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!validateInput() || Cache.Sensors.map.containsKey(textFieldName.getText()) || textFieldName.getText().contains(Config.PACKET_FIELD_DELIMITER) && comboBoxController.getSelectedIndex()!=-1) {
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
									Double.parseDouble(textFieldMaxThreshold.getText()));
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
				} else if (Cache.Sensors.map.containsKey(txt) && !txt.equals(n)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
				} else if (txt.contains(Config.PACKET_FIELD_DELIMITER)) {
					lblNameInfo.setText("<html><font color=\"red\">Semicolon is not allowed!</font></html>");
				} else {
					lblNameInfo.setText("<html><font color=\"green\">OK!</font></html>");
				}
			}
		});
		
		okButton.addActionListener(new ActionListener() {
			public boolean flag;
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!validateInput() || (Cache.Sensors.map.containsKey(textFieldName.getText()) && !textFieldName.getText().equals(n)) || textFieldName.getText().contains(Config.PACKET_FIELD_DELIMITER)) {
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
									Double.parseDouble(textFieldMaxThreshold.getText()));
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
