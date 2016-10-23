package DialogEntityManagement;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Chi.Config;
import Chi.Theme;
import Chi.WaitUI;
import Database.Cache;
import Database.DatabaseSensorActuatorResponse;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DialogSensorActuatorResponseAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBoxActuator;
	private JComboBox<String> comboBoxOnHappen;
	private JComboBox<String> comboBoxOnNotHappen;
	private JCheckBox chckbxEnabled;
	private JScrollPane scrollPaneStatement;
	private JTextArea textAreaStatement;
	private int responseId;
	private JComboBox<String> comboBoxTimeout;
	private JComboBox<String> comboBoxTimeoutUnit;

	private int getTimeout () {
		if (comboBoxTimeoutUnit.getSelectedIndex()==0) return Integer.parseInt((String)comboBoxTimeout.getSelectedItem());
		return Integer.parseInt((String)comboBoxTimeout.getSelectedItem())*60;
	}
	private static boolean validateStatement (String s) {
		return true;
	}
	
	public DialogSensorActuatorResponseAddEdit() {
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogSensorActuatorResponseAddEdit(int id, String n, String trigAct,
												String nTrigAct, String e, boolean en,
												int timeout) {
		create();
		uiActionsNormal();
		prefill(n,trigAct,nTrigAct,e,en,timeout);
		uiActionsEdit(n);
		responseId=id;
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 409, 366);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 405, 293);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblActuator = new JLabel("Actuator :");
		lblActuator.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActuator.setBounds(20, 36, 66, 14);
		contentPanel.add(lblActuator);
		
		comboBoxActuator = new JComboBox<String>();
		comboBoxActuator.setBounds(96, 33, 167, 20);
		contentPanel.add(comboBoxActuator);
		
		JLabel lblOnHappen = new JLabel("On happen, does :");
		lblOnHappen.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOnHappen.setBounds(63, 67, 107, 14);
		contentPanel.add(lblOnHappen);
		
		comboBoxOnHappen = new JComboBox<>();
		comboBoxOnHappen.setBounds(179, 64, 111, 20);
		contentPanel.add(comboBoxOnHappen);
		
		JLabel lblOnNotHappen = new JLabel("On not happen, does :");
		lblOnNotHappen.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOnNotHappen.setBounds(53, 98, 117, 14);
		contentPanel.add(lblOnNotHappen);
		
		comboBoxOnNotHappen = new JComboBox<>();
		comboBoxOnNotHappen.setBounds(179, 95, 111, 20);
		contentPanel.add(comboBoxOnNotHappen);
		
		JLabel lblStatement = new JLabel("Statement");
		lblStatement.setBounds(10, 158, 66, 14);
		contentPanel.add(lblStatement);
		
		chckbxEnabled = new JCheckBox("Enabled");
		chckbxEnabled.setBounds(10, 0, 97, 23);
		contentPanel.add(chckbxEnabled);
		
		scrollPaneStatement = new JScrollPane();
		scrollPaneStatement.setBounds(8, 183, 383, 99);
		contentPanel.add(scrollPaneStatement);
		
		textAreaStatement = new JTextArea();
		scrollPaneStatement.setViewportView(textAreaStatement);
		
		JLabel lblNewLabel = new JLabel("Timeout :");
		lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel.setBounds(20, 133, 66, 14);
		contentPanel.add(lblNewLabel);
		
		comboBoxTimeout = new JComboBox<>();
		comboBoxTimeout.setBounds(96, 130, 74, 20);
		contentPanel.add(comboBoxTimeout);
		
		comboBoxTimeoutUnit = new JComboBox<>();
		comboBoxTimeoutUnit.setBounds(179, 130, 74, 20);
		contentPanel.add(comboBoxTimeoutUnit);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 304, 405, 33);
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
	
	private void prefill(String name, String onTrigAct, String onNotTrigAct, String exp, boolean en, int timeout) {
		comboBoxActuator.setSelectedItem(name);
		comboBoxOnHappen.setSelectedItem(onTrigAct);
		comboBoxOnNotHappen.setSelectedItem(onNotTrigAct);
		textAreaStatement.setText(exp);
		chckbxEnabled.setSelected(en);
		
		if (timeout>=60) {
			comboBoxTimeout.setSelectedItem(String.valueOf(timeout/60));
			comboBoxTimeoutUnit.setSelectedItem("minutes");
		} else {
			comboBoxTimeout.setSelectedItem(String.valueOf(timeout));
			comboBoxTimeoutUnit.setSelectedItem("seconds");
		}
	}
	
	private void uiActionsNormal() {
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		for (String c : Cache.Actuators.map.keySet()) {
			comboBoxActuator.addItem(c);
		}
		
		comboBoxOnHappen.addItem("ON");
		comboBoxOnHappen.addItem("OFF");
		comboBoxOnHappen.addItem("NOTHING");
		
		comboBoxOnNotHappen.addItem("ON");
		comboBoxOnNotHappen.addItem("OFF");
		comboBoxOnNotHappen.addItem("NOTHING");
		comboBoxOnNotHappen.addItem("RESTORE");
		
		comboBoxTimeout.addItem("5");
		comboBoxTimeout.addItem("10");
		comboBoxTimeout.addItem("15");
		comboBoxTimeout.addItem("30");
		comboBoxTimeout.addItem("45");
		
		comboBoxTimeoutUnit.addItem("seconds");
		comboBoxTimeoutUnit.addItem("minutes");
		chckbxEnabled.setSelected(true);
	}
	
	private void uiActionsAdd() {
		setTitle("Add Sensor Actuator Response");
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Actuator ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.Actuators.map.containsKey(s));
		
		okButton.addActionListener(new ActionListener() {
			public boolean flag;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String txt=textAreaStatement.getText();
				if (txt==null || txt.isEmpty() || txt.contains(Config.PACKET_FIELD_DELIMITER) || !validateStatement(txt) ||
					comboBoxActuator.getItemCount()==0) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Sensor Actuator Response",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating response rule");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseSensorActuatorResponse.createSensorActuatorResponse((String)comboBoxActuator.getSelectedItem(), (String)comboBoxOnHappen.getSelectedItem(),
																							  (String)comboBoxOnNotHappen.getSelectedItem(), textAreaStatement.getText(),
																							  chckbxEnabled.isSelected(),getTimeout());
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
		setTitle("Edit "+n);
		
		okButton.addActionListener(new ActionListener() {
			public boolean flag;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String txt=textAreaStatement.getText();
				if (txt==null || txt.isEmpty() || txt.contains(Config.PACKET_FIELD_DELIMITER) || !validateStatement(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Sensor Actuator Response",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating response rule");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseSensorActuatorResponse.updateSensorActuatorResponse(responseId,(String)comboBoxActuator.getSelectedItem(), (String)comboBoxOnHappen.getSelectedItem(),
																							  (String)comboBoxOnNotHappen.getSelectedItem(), textAreaStatement.getText(),
																							  chckbxEnabled.isSelected(), getTimeout());
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
