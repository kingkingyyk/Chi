package DialogEntityManagement;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Chi.Config;
import Chi.Theme;
import Chi.Utility;
import Chi.WaitUI;
import Database.Cache;
import Database.DatabaseDayScheduleRule;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class DialogDayScheduleRuleAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBoxStartHour;
	private JComboBox<String> comboBoxStartMinute;
	private JComboBox<String> comboBoxEndHour;
	private JComboBox<String> comboBoxEndMinute;
	private JComboBox<String> comboBoxStartTimeAMPM;
	private JComboBox<String> comboBoxEndTimeAMPM;
	
	public boolean isTimeSame() {
		ArrayList<JComboBox<String>> startInfo=new ArrayList<>();
		startInfo.add(comboBoxStartHour);
		startInfo.add(comboBoxStartMinute);
		startInfo.add(comboBoxStartTimeAMPM);
		
		ArrayList<JComboBox<String>> endInfo=new ArrayList<>();
		endInfo.add(comboBoxEndHour);
		endInfo.add(comboBoxEndMinute);
		endInfo.add(comboBoxEndTimeAMPM);
		
		for (int i=0;i<startInfo.size();i++) if (!startInfo.get(i).getSelectedItem().equals(endInfo.get(i).getSelectedItem())) return false;
		
		startInfo.clear(); endInfo.clear();
		return true;
	}
	
	public DialogDayScheduleRuleAddEdit() {
		create();
		uiActionsNormal();
		
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Time Rule ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.DayScheduleRules.map.containsKey(s));
		textFieldName.setText(s);
		
		prefill(s,0,0,0,1);
		uiActionsAdd();
	}
	
	public DialogDayScheduleRuleAddEdit(String n, int sh, int sm, int eh, int em) {
		create();
		uiActionsNormal();
		prefill(n,sh,sm,eh,em);
		uiActionsEdit(n);
	}
	
	private int [][] getTime() {
		int [][] t=new int [2][2];
		t[0][0]=comboBoxStartHour.getSelectedIndex()+12*comboBoxStartTimeAMPM.getSelectedIndex();
		t[0][1]=comboBoxStartMinute.getSelectedIndex();
		t[1][0]=comboBoxEndHour.getSelectedIndex()+12*comboBoxEndTimeAMPM.getSelectedIndex();
		t[1][1]=comboBoxEndMinute.getSelectedIndex();
		return t;
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 440, 175);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 433, 102);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JLabel lblName = new JLabel("Name :");
			lblName.setBounds(10, 11, 64, 14);
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(86, 8, 197, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
		}
		{
			lblNameInfo = new JLabel("");
			lblNameInfo.setBounds(292, 9, 131, 16);
			contentPanel.add(lblNameInfo);
		}
		
		JLabel lblStartTime = new JLabel("Start Time :");
		lblStartTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStartTime.setBounds(20, 43, 56, 14);
		contentPanel.add(lblStartTime);
		
		JLabel lblStartTimeSep = new JLabel(":");
		lblStartTimeSep.setBounds(152, 43, 11, 14);
		contentPanel.add(lblStartTimeSep);
		
		JLabel lblEndTime = new JLabel("End Time :");
		lblEndTime.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEndTime.setBounds(20, 74, 56, 14);
		contentPanel.add(lblEndTime);
		
		JLabel lblEndTimeSep = new JLabel(":");
		lblEndTimeSep.setBounds(152, 74, 11, 14);
		contentPanel.add(lblEndTimeSep);
		
		comboBoxStartHour = new JComboBox<>();
		comboBoxStartHour.setBounds(86, 40, 56, 20);
		contentPanel.add(comboBoxStartHour);
		
		comboBoxEndHour = new JComboBox<>();
		comboBoxEndHour.setBounds(86, 71, 56, 20);
		contentPanel.add(comboBoxEndHour);
		
		comboBoxStartMinute = new JComboBox<>();
		comboBoxStartMinute.setBounds(162, 39, 56, 20);
		contentPanel.add(comboBoxStartMinute);
		
		comboBoxEndMinute = new JComboBox<>();
		comboBoxEndMinute.setBounds(162, 71, 56, 20);
		contentPanel.add(comboBoxEndMinute);
		
		comboBoxStartTimeAMPM = new JComboBox<>();
		comboBoxStartTimeAMPM.setBounds(227, 39, 56, 20);
		contentPanel.add(comboBoxStartTimeAMPM);
		
		comboBoxEndTimeAMPM = new JComboBox<>();
		comboBoxEndTimeAMPM.setBounds(228, 71, 55, 20);
		contentPanel.add(comboBoxEndTimeAMPM);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 115, 433, 33);
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
	
	private void prefill(String n, int sh, int sm, int eh, int em) {
		textFieldName.setText(n);
		comboBoxStartHour.setSelectedIndex(sh%12);
		comboBoxStartMinute.setSelectedIndex(sm);
		comboBoxStartTimeAMPM.setSelectedIndex(sh/12);
		comboBoxEndHour.setSelectedIndex(eh%12);
		comboBoxEndMinute.setSelectedIndex(em);
		comboBoxEndTimeAMPM.setSelectedIndex(eh/12);
	}
	
	private void uiActionsNormal() {
		for (String s : Utility.Hours) {
			comboBoxStartHour.addItem(s);
			comboBoxEndHour.addItem(s);
		}
		for (String s : Utility.Minutes) {
			comboBoxStartMinute.addItem(s);
			comboBoxEndMinute.addItem(s);
		}
		for (String s : Utility.AMPM) {
			comboBoxStartTimeAMPM.addItem(s);
			comboBoxEndTimeAMPM.addItem(s);
		}

		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
	
	private void uiActionsAdd() {
		setTitle("Add Day Schedule Rule");
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (txt.length()>100) {
					lblNameInfo.setText("<html><font color=\"red\">Too long!</font></html>");
				} else if (Cache.DayScheduleRules.map.containsKey(txt)) {
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
				if (txt==null || txt.isEmpty() || txt.length()>100 || Cache.DayScheduleRules.map.containsKey(txt) ||
						!Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Day Schedule Rule",JOptionPane.ERROR_MESSAGE);
				} else if (isTimeSame()){
					JOptionPane.showMessageDialog(null,"Start & End Time cannot be the same!","Add Day Schedule Rule",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating rule");
					Thread t=new Thread() {
						public void run () {
							int [][] t=getTime();
							flag=DatabaseDayScheduleRule.createDayScheduleRule(textFieldName.getText(),t[0][0],t[0][1],t[1][0],t[1][1]);
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
		setTitle("Edit Day Schedule Rule "+n);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (txt.length()>100) {
					lblNameInfo.setText("<html><font color=\"red\">Too long!</font></html>");
				} else if (Cache.DayScheduleRules.map.containsKey(txt) && !txt.equals(n)) {
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
				if (txt==null || txt.isEmpty() || txt.length()>100 ||
						(Cache.DayScheduleRules.map.containsKey(txt) &&!txt.equals(n)) || !Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Edit Day Schedule Rule",JOptionPane.ERROR_MESSAGE);
				} else if (isTimeSame()){
					JOptionPane.showMessageDialog(null,"Start & End Time cannot be the same!","Edit Day Schedule Rule",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating actuator");
					Thread t=new Thread() {
						public void run () {
							int [][] t=getTime();
							flag=DatabaseDayScheduleRule.updateDayScheduleRule(n,textFieldName.getText(),t[0][0],t[0][1],t[1][0],t[1][1]);
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
