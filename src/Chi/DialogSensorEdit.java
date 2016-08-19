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

public class DialogSensorEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	public boolean sensorEdited=false;
	private JTextField textFieldMinValue;
	private JTextField textFieldMaxValue;
	private JTextField textFieldTransFactor;
	private JTextField textFieldUnit;
	private JLabel lblMinValueInfo;
	private JLabel lblMaxValueInfo;
	private JLabel lblTransFacInfo;
	private JLabel lblUnitInfo;

	private boolean validateInput () {
		JTextField [] numberFields=new JTextField [] {textFieldMinValue,textFieldMaxValue,textFieldTransFactor};
		JTextField [] fillFields=new JTextField[] {textFieldUnit};
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
	
	public DialogSensorEdit(String n, String c, double min, double max, double t, String u) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Add Sensor");
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 485, 269);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 479, 192);
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
			textFieldName = new JTextField(n);
			textFieldName.setEditable(false);
			textFieldName.setBounds(89, 8, 246, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
		}
		{
			JLabel lblClass = new JLabel("Class :");
			lblClass.setBounds(31, 41, 53, 14);
			lblClass.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblClass);
		}
		
		JComboBox<String> comboBoxClass = new JComboBox<>();
		comboBoxClass.setMaximumRowCount(6);
		comboBoxClass.setBounds(89, 38, 246, 20);
		for (String s : Cache.sensorClassList) {
			comboBoxClass.addItem(s);
		}
		comboBoxClass.setSelectedIndex(Cache.sensorClassList.indexOf(c));
		contentPanel.add(comboBoxClass);
		
		textFieldMinValue = new JTextField(String.valueOf(min));
		textFieldMinValue.setText("0");
		textFieldMinValue.setColumns(30);
		textFieldMinValue.setBounds(89, 69, 246, 20);
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
		contentPanel.add(textFieldMinValue);
		
		JLabel lblMinValue = new JLabel("Min Value :");
		lblMinValue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMinValue.setBounds(31, 72, 53, 14);
		contentPanel.add(lblMinValue);
		
		textFieldMaxValue = new JTextField(String.valueOf(max));
		textFieldMaxValue.setText("1");
		textFieldMaxValue.setColumns(30);
		textFieldMaxValue.setBounds(89, 100, 246, 20);
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
		
		textFieldUnit = new JTextField(u);
		textFieldUnit.setBounds(89, 161, 246, 20);
		contentPanel.add(textFieldUnit);
		textFieldUnit.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldUnit.getText();
				if (txt==null || txt.isEmpty()) { 
					lblUnitInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else {
					lblUnitInfo.setText("<html><font color=\"green\">OK!</font></html>");
				}
			}
		});
		textFieldUnit.setColumns(30);
		
		JLabel lblTransFactor = new JLabel("Trans. Factor :");
		lblTransFactor.setBounds(10, 133, 74, 14);
		contentPanel.add(lblTransFactor);
		lblTransFactor.setHorizontalAlignment(SwingConstants.RIGHT);
		
		textFieldTransFactor = new JTextField();
		textFieldTransFactor.setBounds(89, 131, 246, 20);
		contentPanel.add(textFieldTransFactor);
		textFieldTransFactor.setText("1");
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
		textFieldTransFactor.setColumns(30);
		
		lblTransFacInfo = new JLabel("");
		lblTransFacInfo.setBounds(345, 132, 124, 16);
		contentPanel.add(lblTransFacInfo);
		
		JLabel lblUnit = new JLabel("Unit :");
		lblUnit.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUnit.setBounds(10, 163, 74, 14);
		contentPanel.add(lblUnit);
		
		lblUnitInfo = new JLabel("<html><font color=\"green\">OK!</font></html>");
		lblUnitInfo.setBounds(345, 163, 124, 16);
		contentPanel.add(lblUnitInfo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 207, 479, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public boolean flag;
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if (!validateInput()) {
							JOptionPane.showMessageDialog(null,"Invalid information!","Add Sensor",JOptionPane.ERROR_MESSAGE);
						} else {
							WaitUI u=new WaitUI();
							u.setText("Updating sensor");
							Thread t=new Thread() {
								public void run () {
									flag=DatabaseSensor.updateSensor(textFieldName.getText(),(String)comboBoxClass.getSelectedItem(),Double.parseDouble(textFieldMinValue.getText()),Double.parseDouble(textFieldMaxValue.getText()),Double.parseDouble(textFieldTransFactor.getText()), textFieldUnit.getText());
									u.dispose();
								}
							};
							t.start();
							u.setVisible(true);
							
							if (flag) {
								sensorEdited=true;
								dispose();
							} else {
								JOptionPane.showMessageDialog(null,"Database error, please check the console for more information.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
							}
						}
					}
					
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
					
				});
				buttonPane.add(cancelButton);
			}
		}
	}
}
