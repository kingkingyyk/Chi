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

public class DialogActuatorAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	private JButton okButton;
	private JButton cancelButton;
	private JComboBox<String> comboBoxController;
	
	public DialogActuatorAddEdit() {
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogActuatorAddEdit(String n, String u) {
		create();
		uiActionsNormal();
		prefill(n,u);
		uiActionsEdit(n);
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 445, 154);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 439, 114);
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
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 84, 439, 33);
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
		
		comboBoxController = new JComboBox<>();
		comboBoxController.setBounds(98, 39, 206, 20);
		contentPanel.add(comboBoxController);
	}
	
	private void prefill(String n, String c) {
		textFieldName.setText(n);
		comboBoxController.setSelectedItem(c);
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
				if (txt==null || txt.isEmpty() || Cache.Actuators.map.containsKey(txt) || txt.contains(Config.PACKET_FIELD_DELIMITER)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Actuator",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating actuator");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseActuator.createActuator(textFieldName.getText(),(String) comboBoxController.getSelectedItem());
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
				if (txt==null || txt.isEmpty() || (Cache.Actuators.map.containsKey(txt) && !txt.equals(n)) || txt.contains(Config.PACKET_FIELD_DELIMITER)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Edit Actuator",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating actuator");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseActuator.updateActuator(n, textFieldName.getText(),(String)comboBoxController.getSelectedItem());
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
