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
import javax.swing.JCheckBox;

public class DialogRegularScheduleAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	public boolean updated=false;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBoxActuator;
	private JTextField textFieldPriority;
	private JCheckBox chckbxMonday;
	private JCheckBox chckbxTuesday;
	private JCheckBox chckbxWednesday;
	private JCheckBox chckbxThursday;
	private JCheckBox chckbxFriday;
	private JCheckBox chckbxSaturday;
	private JCheckBox chckbxSunday;
	private JCheckBox chckbxWeekends;
	private JCheckBox chckbxWeekdays ;
	private JCheckBox [] chckbxDays;
	private JComboBox<String> comboBoxSwitch;
	private JLabel lblPriorityInfo;
	private JCheckBox chckbxEnabled;
	private JLabel lblTimeRule;
	private JComboBox<String> comboBoxTimeRule;
	
	public DialogRegularScheduleAddEdit() {
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogRegularScheduleAddEdit(String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
		create();
		uiActionsNormal();
		prefill(sn,an,day,rn,ao,pr,en);
		uiActionsEdit(sn);
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 475, 353);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 469, 280);
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
			lblNameInfo.setBounds(314, 9, 145, 16);
			contentPanel.add(lblNameInfo);
		}
		
		JLabel lblActuator = new JLabel("Actuator :");
		lblActuator.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActuator.setBounds(10, 42, 78, 14);
		contentPanel.add(lblActuator);
		
		comboBoxActuator = new JComboBox<>();
		comboBoxActuator.setBounds(98, 39, 206, 20);
		contentPanel.add(comboBoxActuator);
		
		JLabel lblDays = new JLabel("Days :");
		lblDays.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDays.setBounds(10, 70, 78, 14);
		contentPanel.add(lblDays);
		
		chckbxMonday = new JCheckBox("Monday");
		chckbxMonday.setBounds(98, 66, 78, 23);
		contentPanel.add(chckbxMonday);
		
		chckbxTuesday = new JCheckBox("Tuesday");
		chckbxTuesday.setBounds(178, 66, 78, 23);
		contentPanel.add(chckbxTuesday);
		
		chckbxWednesday = new JCheckBox("Wednesday");
		chckbxWednesday.setBounds(258, 66, 88, 23);
		contentPanel.add(chckbxWednesday);
		
		chckbxThursday = new JCheckBox("Thursday");
		chckbxThursday.setBounds(98, 92, 78, 23);
		contentPanel.add(chckbxThursday);
		
		chckbxFriday = new JCheckBox("Friday");
		chckbxFriday.setBounds(178, 92, 78, 23);
		contentPanel.add(chckbxFriday);
		
		chckbxSaturday = new JCheckBox("Saturday");
		chckbxSaturday.setBounds(258, 92, 78, 23);
		contentPanel.add(chckbxSaturday);
		
		chckbxSunday = new JCheckBox("Sunday");
		chckbxSunday.setBounds(98, 120, 78, 23);
		contentPanel.add(chckbxSunday);
		
		chckbxWeekdays = new JCheckBox("Weekdays");
		chckbxWeekdays.setBounds(178, 120, 78, 23);
		contentPanel.add(chckbxWeekdays);
		
		chckbxWeekends = new JCheckBox("Weekends");
		chckbxWeekends.setBounds(258, 120, 78, 23);
		contentPanel.add(chckbxWeekends);
		
		JLabel lblSwitch = new JLabel("Switch :");
		lblSwitch.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSwitch.setBounds(10, 182, 78, 14);
		contentPanel.add(lblSwitch);
		
		comboBoxSwitch = new JComboBox<String>();
		comboBoxSwitch.addItem("ON");
		comboBoxSwitch.addItem("OFF");
		comboBoxSwitch.setBounds(98, 179, 206, 20);
		contentPanel.add(comboBoxSwitch);
		
		textFieldPriority = new JTextField();
		textFieldPriority.setBounds(98, 213, 206, 20);
		contentPanel.add(textFieldPriority);
		textFieldPriority.setColumns(10);
		
		JLabel lblPriority = new JLabel("Priority :");
		lblPriority.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPriority.setBounds(10, 216, 78, 14);
		contentPanel.add(lblPriority);
		
		JLabel lblEnabled = new JLabel("Enabled :");
		lblEnabled.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEnabled.setBounds(10, 244, 78, 14);
		contentPanel.add(lblEnabled);
		
		chckbxEnabled = new JCheckBox("");
		chckbxEnabled.setBounds(96, 240, 97, 23);
		contentPanel.add(chckbxEnabled);
		
		lblPriorityInfo = new JLabel("");
		lblPriorityInfo.setBounds(314, 213, 145, 16);
		contentPanel.add(lblPriorityInfo);
		
		lblTimeRule = new JLabel("Time Rule :");
		lblTimeRule.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimeRule.setBounds(10, 151, 78, 14);
		contentPanel.add(lblTimeRule);
		
		comboBoxTimeRule = new JComboBox<String>();
		comboBoxTimeRule.setBounds(98, 148, 206, 20);
		contentPanel.add(comboBoxTimeRule);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 291, 469, 33);
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
	
	private void prefill(String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
		textFieldName.setText(sn);
		comboBoxActuator.setSelectedItem(an);
		
		for (int i=1;i<=7;i++) {
			chckbxDays[i].setSelected((day & (1 << i))!=0);
		}
		
		comboBoxTimeRule.setSelectedItem(rn);
		
		if (ao) comboBoxSwitch.setSelectedIndex(0);
		else comboBoxSwitch.setSelectedIndex(1);
		
		textFieldPriority.setText(String.valueOf(pr));
		chckbxEnabled.setSelected(en);
	}
	
	private void uiActionsNormal() {
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		for (String c : Cache.actuatorList) {
			comboBoxActuator.addItem(c);
		}
		
		for (String c : Cache.DayScheduleRuleList) {
			comboBoxTimeRule.addItem(c);
		}
		
		chckbxDays=new JCheckBox[8];
		chckbxDays[1]=chckbxMonday;
		chckbxDays[2]=chckbxTuesday;
		chckbxDays[3]=chckbxWednesday;
		chckbxDays[4]=chckbxThursday;
		chckbxDays[5]=chckbxFriday;
		chckbxDays[6]=chckbxSaturday;
		chckbxDays[7]=chckbxSunday;
		
		chckbxWeekdays.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chckbxWeekdays.setSelected(false);
				for (int i=1;i<=5;i++) {
					chckbxDays[i].setSelected(true);
				}
			}
		});
		
		chckbxWeekends.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chckbxWeekends.setSelected(false);
				for (int i=6;i<=7;i++) {
					chckbxDays[i].setSelected(true);
				}
			}
		});
		
		textFieldPriority.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldPriority.getText();
				if (txt==null || txt.isEmpty()) { 
					lblPriorityInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else {
					try {
						int i=Integer.parseInt(txt);
						if (i>0) {
							lblPriorityInfo.setText("<html><font color=\"green\">OK!</font></html>");
						} else {
							lblPriorityInfo.setText("<html><font color=\"red\">Must be more than 0.</font></html>");
						}
					} catch (NumberFormatException ex) {
						lblPriorityInfo.setText("<html><font color=\"red\">Must be a number!</font></html>");
					}
				}
			}
		});
	}
	
	private void uiActionsAdd() {
		setTitle("Add Regular Schedule");
		
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Regular Schedule ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.RegularScheduleMap.containsKey(s) || Cache.SpecialScheduleMap.containsKey(s));
		textFieldName.setText(s);
		
		textFieldPriority.setText("1");
		chckbxEnabled.setEnabled(true);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.RegularScheduleMap.containsKey(txt) || Cache.SpecialScheduleMap.containsKey(txt)) {
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
				String txt=textFieldName.getText();
				boolean priorityOK=false;
				try {
					int i=Integer.parseInt(textFieldPriority.getText());
					priorityOK=(i>0);
				} catch (NumberFormatException e) {};
				
				if (txt==null || txt.isEmpty() || Cache.RegularScheduleMap.containsKey(txt) || Cache.SpecialScheduleMap.containsKey(txt) || !priorityOK || txt.contains(Config.PACKET_FIELD_DELIMITER)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Regular Schedule",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating schedule");
					Thread t=new Thread() {
						public void run () {
							int day=0;
							for (int i=1;i<=7;i++) {
								if (chckbxDays[i].isSelected()) {
									day=(day | (1 << i));
								}
							}
							flag=DatabaseRegularSchedule.createRegularSchedule(textFieldName.getText(),(String)comboBoxActuator.getSelectedItem(),
																				day,(String)comboBoxTimeRule.getSelectedItem(),
																				comboBoxSwitch.getSelectedItem().equals("ON"),
																				Integer.parseInt(textFieldPriority.getText()),chckbxEnabled.isSelected());
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
	
	private void uiActionsEdit(String n) {
		setTitle("Edit Regular Schedule "+n);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if ((Cache.RegularScheduleMap.containsKey(txt) || Cache.SpecialScheduleMap.containsKey(txt)) && !txt.equals(n)) {
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
				String txt=textFieldName.getText();
				boolean priorityOK=false;
				try {
					int i=Integer.parseInt(textFieldPriority.getText());
					priorityOK=(i>0);
				} catch (NumberFormatException e) {};
				if (txt==null || txt.isEmpty() || (Cache.RegularScheduleMap.containsKey(txt) || Cache.SpecialScheduleMap.containsKey(txt) && !txt.equals(n)) || !priorityOK || txt.contains(Config.PACKET_FIELD_DELIMITER)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Edit Regular Schedule",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating schedule");
					Thread t=new Thread() {
						public void run () {
							int day=0;
							for (int i=1;i<=7;i++) {
								if (chckbxDays[i].isSelected()) {
									day=(day | (1 << i));
								}
							}
							flag=DatabaseRegularSchedule.updateRegularSchedule(n,textFieldName.getText(),(String)comboBoxActuator.getSelectedItem(),
																				day,(String)comboBoxTimeRule.getSelectedItem(),
																				comboBoxSwitch.getSelectedItem().equals("ON"),
																				Integer.parseInt(textFieldPriority.getText()),chckbxEnabled.isSelected());
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
