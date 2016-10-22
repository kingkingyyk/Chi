package Chi;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.JXDatePicker;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class DialogSpecialScheduleAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	public boolean updated=false;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBoxActuator;
	private JTextField textFieldPriority;
	private JComboBox<String> comboBoxOnStart;
	private JLabel lblPriorityInfo;
	private JCheckBox chckbxEnabled;
	private JLabel lblTimeRule;
	private JComboBox<String> comboBoxTimeRule;
	private JXDatePicker datePickerDate;
	private JComboBox<String> comboBoxOnEnd;
	private JLabel lblOnEndDo;
	private JCheckBox chckbxLock;
	
	public DialogSpecialScheduleAddEdit() {
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogSpecialScheduleAddEdit(String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		create();
		uiActionsNormal();
		prefill(sn,an,year,month,day,rn,startAct,endAct,lock,pr,en);
		uiActionsEdit(sn);
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 463, 321);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 457, 283);
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
			lblNameInfo.setBounds(314, 9, 133, 16);
			contentPanel.add(lblNameInfo);
		}
		
		JLabel lblActuator = new JLabel("Actuator :");
		lblActuator.setHorizontalAlignment(SwingConstants.RIGHT);
		lblActuator.setBounds(10, 42, 78, 14);
		contentPanel.add(lblActuator);
		
		comboBoxActuator = new JComboBox<>();
		comboBoxActuator.setBounds(98, 39, 206, 20);
		contentPanel.add(comboBoxActuator);
		
		JLabel lblDate = new JLabel("Date :");
		lblDate.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDate.setBounds(10, 73, 78, 14);
		contentPanel.add(lblDate);
		
		JLabel lblOnStart = new JLabel("On Start, Do :");
		lblOnStart.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOnStart.setBounds(10, 137, 78, 14);
		contentPanel.add(lblOnStart);
		
		comboBoxOnStart = new JComboBox<String>();
		comboBoxOnStart.addItem("ON");
		comboBoxOnStart.addItem("OFF");
		comboBoxOnStart.addItem("NOTHING");
		comboBoxOnStart.setBounds(98, 134, 206, 20);
		contentPanel.add(comboBoxOnStart);
		
		textFieldPriority = new JTextField();
		textFieldPriority.setBounds(98, 193, 206, 20);
		contentPanel.add(textFieldPriority);
		textFieldPriority.setColumns(10);
		
		JLabel lblPriority = new JLabel("Priority :");
		lblPriority.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPriority.setBounds(10, 196, 78, 14);
		contentPanel.add(lblPriority);
		
		chckbxEnabled = new JCheckBox("Enabled");
		chckbxEnabled.setBounds(179, 220, 97, 23);
		contentPanel.add(chckbxEnabled);
		
		lblPriorityInfo = new JLabel("");
		lblPriorityInfo.setBounds(314, 197, 133, 16);
		contentPanel.add(lblPriorityInfo);
		
		lblTimeRule = new JLabel("Time Rule :");
		lblTimeRule.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTimeRule.setBounds(10, 105, 78, 14);
		contentPanel.add(lblTimeRule);
		
		comboBoxTimeRule = new JComboBox<String>();
		comboBoxTimeRule.setBounds(98, 102, 206, 20);
		contentPanel.add(comboBoxTimeRule);
		
		datePickerDate = new JXDatePicker(new Date());
		datePickerDate.setBounds(98, 70, 206, 20);
		contentPanel.add(datePickerDate);
		
		comboBoxOnEnd = new JComboBox<String>();
		comboBoxOnEnd.setBounds(98, 162, 206, 20);
		comboBoxOnEnd.addItem("ON");
		comboBoxOnEnd.addItem("OFF");
		comboBoxOnEnd.addItem("NOTHING");
		contentPanel.add(comboBoxOnEnd);
		
		lblOnEndDo = new JLabel("On End, Do :");
		lblOnEndDo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOnEndDo.setBounds(10, 165, 78, 14);
		contentPanel.add(lblOnEndDo);
		
		chckbxLock = new JCheckBox("Manual Trigger Lock");
		chckbxLock.setEnabled(true);
		chckbxLock.setBounds(10, 220, 167, 23);
		contentPanel.add(chckbxLock);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 250, 457, 33);
			contentPanel.add(buttonPane);
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
	
	private void prefill(String sn, String an, int year, int month, int day, String rn, String startAct, String endAct, boolean lock, int pr, boolean en) {
		textFieldName.setText(sn);
		comboBoxActuator.setSelectedItem(an);
		
		datePickerDate.setDate(new Date(LocalDateTime.of(year,month,day,0,0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
		comboBoxTimeRule.setSelectedItem(rn);
		
		comboBoxOnStart.setSelectedItem(startAct);
		comboBoxOnEnd.setSelectedItem(endAct);
		chckbxLock.setSelected(lock);
		
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
		
		for (String c : Cache.Actuators.map.keySet()) {
			comboBoxActuator.addItem(c);
		}
		
		for (String c : Cache.DayScheduleRules.map.keySet()) {
			comboBoxTimeRule.addItem(c);
		}
		
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
		setTitle("Add Special Schedule");
		
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Special Schedule ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.SpecialSchedules.map.containsKey(s) || Cache.RegularSchedules.map.containsKey(s));
		textFieldName.setText(s);
		
		textFieldPriority.setText("1");
		chckbxEnabled.setEnabled(true);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.SpecialSchedules.map.containsKey(txt) || Cache.RegularSchedules.map.containsKey(txt)) {
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
				boolean priorityOK=false;
				try {
					int i=Integer.parseInt(textFieldPriority.getText());
					priorityOK=(i>0);
				} catch (NumberFormatException e) {};
				
				if (txt==null || txt.isEmpty() || Cache.SpecialSchedules.map.containsKey(txt) || Cache.RegularSchedules.map.containsKey(txt) ||
					!priorityOK || !Utility.validateName(txt) || comboBoxActuator.getItemCount()==0 || comboBoxTimeRule.getItemCount()==0) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Special Schedule",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating schedule");
					Thread t=new Thread() {
						public void run () {
							LocalDate date=Instant.ofEpochMilli(datePickerDate.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
							flag=DatabaseSpecialSchedule.createSpecialSchedule(textFieldName.getText(),(String)comboBoxActuator.getSelectedItem(),
																				date.getYear(),date.getMonth().getValue(),date.getDayOfMonth(),
																				(String)comboBoxTimeRule.getSelectedItem(),(String)comboBoxOnStart.getSelectedItem(),
																				(String)comboBoxOnEnd.getSelectedItem(),chckbxLock.isSelected(),
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
		setTitle("Edit Special Schedule "+n);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if ((Cache.SpecialSchedules.map.containsKey(txt) || Cache.RegularSchedules.map.containsKey(txt)) && !txt.equals(n)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
				} else if (!Utility.validateName(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Contains invald character</font></html>");
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
				if (txt==null || txt.isEmpty() || ((Cache.SpecialSchedules.map.containsKey(txt) || Cache.RegularSchedules.map.containsKey(txt)) && !txt.equals(n)) || !priorityOK || !Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Edit Special Schedule",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating schedule");
					Thread t=new Thread() {
						public void run () {
							LocalDate date=Instant.ofEpochMilli(datePickerDate.getDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
							flag=DatabaseSpecialSchedule.updateSpecialSchedule(n,textFieldName.getText(),(String)comboBoxActuator.getSelectedItem(),
																				date.getYear(),date.getMonth().getValue(),date.getDayOfMonth(),
																				(String)comboBoxTimeRule.getSelectedItem(),(String)comboBoxOnStart.getSelectedItem(),
																				(String)comboBoxOnEnd.getSelectedItem(),chckbxLock.isSelected(),
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
