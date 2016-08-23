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
import java.util.HashSet;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class DialogSensorClassEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	public HashSet<String> classSet;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	public boolean classEdited=false;

	public DialogSensorClassEdit(String fillN) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Edit Sensor Class "+fillN);
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
			textFieldName = new JTextField(fillN);
			textFieldName.setBounds(75, 8, 246, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
			textFieldName.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e) {
					String txt=textFieldName.getText();
					if (txt==null || txt.isEmpty()) { 
						lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
					} else if (classSet.contains(txt) && !txt.equals(fillN)) {
						lblNameInfo.setText("<html><font color=\"red\">Already in use!</font></html>");
					} else {
						lblNameInfo.setText("<html><font color=\"green\">OK!</font></html>");
					}
				}
			});
		}
		{
			lblNameInfo = new JLabel("<html><font color=\"red\">Cannot be empty!</font></html>");
			lblNameInfo.setBounds(331, 10, 89, 16);
			contentPanel.add(lblNameInfo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 47, 444, 33);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {
					public boolean flag;
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						String txt=textFieldName.getText();
						if (txt==null || txt.isEmpty() || (classSet.contains(txt) && !txt.equals(fillN))) {
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
								classEdited=true;
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
