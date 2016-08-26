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

public class DialogSensorClassAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	private JButton cancelButton;
	private JButton okButton;

	public DialogSensorClassAddEdit() {
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogSensorClassAddEdit(String n) {
		create();
		textFieldName.setText(n);
		uiActionsNormal();
		uiActionsEdit(n);
	}
	
	public void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 448, 118);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 444, 130);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JLabel lblName = new JLabel("Name :");
			lblName.setBounds(10, 11, 55, 14);
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(75, 8, 246, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
		}
		{
			lblNameInfo = new JLabel();
			lblNameInfo.setBounds(331, 10, 89, 16);
			contentPanel.add(lblNameInfo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 47, 444, 33);
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
	
	public void uiActionsNormal() {
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
	}
	
	public void uiActionsAdd() {
		setTitle("Add Sensor Class");
		lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.sensorClassMap.containsKey(txt)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
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
				if (txt==null || txt.isEmpty() || Cache.sensorClassMap.containsKey(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid name!",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating sensor class");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseSensorClass.createSensorClass(txt);
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
	
	public void uiActionsEdit(String fillN) {
		setTitle("Edit "+fillN);
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.sensorClassMap.containsKey(txt) && !txt.equals(fillN)) {
					lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
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
				if (txt==null || txt.isEmpty() || (Cache.sensorClassMap.containsKey(txt) && !txt.equals(fillN))) {
					JOptionPane.showMessageDialog(null,"Invalid name!",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating sensor class");
					Thread t=new Thread() {
						public void run () {
							flag=DatabaseSensorClass.updateSensorClass(fillN,txt);
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
