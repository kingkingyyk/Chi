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
import Database.DatabaseSite;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class DialogSiteAddEdit extends JDialog {
	private static final long serialVersionUID = 2263148230107556625L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldName;
	private JLabel lblNameInfo;
	public boolean updated=false;
	private JTextField textFieldURL;
	private JLabel lblURLInfo;
	private JButton okButton;
	private JButton cancelButton;
	
	public DialogSiteAddEdit() {
		create();
		uiActionsNormal();
		uiActionsAdd();
	}
	
	public DialogSiteAddEdit(String n, String u) {
		create();
		prefill(n,u);
		uiActionsNormal();
		uiActionsEdit(n);
	}
	
	private void create() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setResizable(false);
		setTitle("Add Site");
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 460, 154);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 11, 454, 114);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			JLabel lblName = new JLabel("Name :");
			lblName.setBounds(10, 11, 52, 14);
			lblName.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPanel.add(lblName);
		}
		{
			textFieldName = new JTextField();
			textFieldName.setBounds(72, 8, 232, 20);
			contentPanel.add(textFieldName);
			textFieldName.setColumns(30);
		}
		{
			lblNameInfo = new JLabel("");
			lblNameInfo.setBounds(314, 9, 132, 16);
			contentPanel.add(lblNameInfo);
		}
		
		textFieldURL = new JTextField();
		textFieldURL.setBounds(72, 39, 232, 20);
		contentPanel.add(textFieldURL);
		textFieldURL.setColumns(30);
		
		JLabel lblURL = new JLabel("URL :");
		lblURL.setHorizontalAlignment(SwingConstants.RIGHT);
		lblURL.setBounds(10, 42, 52, 14);
		contentPanel.add(lblURL);
		
		lblURLInfo = new JLabel();
		lblURLInfo.setBounds(314, 41, 132, 16);
		contentPanel.add(lblURLInfo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 84, 456, 33);
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
	
	private void prefill(String n, String u) {
		textFieldName.setText(n);
		textFieldURL.setText(u);
	}
	
	private void uiActionsNormal() {
		textFieldURL.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldURL.getText();
				if (txt==null || txt.isEmpty()) { 
					lblURLInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else {
					lblURLInfo.setText("<html><font color=\"green\">OK!</font></html>");
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
	
	private void uiActionsAdd() {
		setTitle("Add Site");
		
		String s;
		int count=1;
		do {
			StringBuilder sb=new StringBuilder();
			sb.append("Site ");
			sb.append(count++);
			s=sb.toString();
		} while (Cache.Sites.map.containsKey(s));
		textFieldName.setText(s);
		
		lblURLInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.Sites.map.containsKey(txt)) {
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
				if (txt==null || txt.isEmpty() || Cache.Sites.map.containsKey(txt) || lblURLInfo.getText().isEmpty() || !Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Site",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Creating site");
					Thread t=new Thread() {
						public void run () {
							String url=textFieldURL.getText();
							if (!url.startsWith("http://")) {
								url="http://"+url;
							}
							flag=DatabaseSite.createSite(textFieldName.getText(),url);
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
		setTitle("Edit Site "+n);
		lblURLInfo.setText("<html><font color=\"green\">OK!</font></html>");
		
		textFieldName.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				String txt=textFieldName.getText();
				if (txt==null || txt.isEmpty()) { 
					lblNameInfo.setText("<html><font color=\"red\">Cannot be empty!</font></html>");
				} else if (Cache.Sites.map.containsKey(txt) && !txt.equals(n)) {
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
				if (txt==null || txt.isEmpty() || (Cache.Sites.map.containsKey(txt) && !txt.equals(n)) || lblURLInfo.getText().isEmpty() || Utility.validateName(txt)) {
					JOptionPane.showMessageDialog(null,"Invalid information!","Add Site",JOptionPane.ERROR_MESSAGE);
				} else {
					WaitUI u=new WaitUI();
					u.setText("Updating site");
					Thread t=new Thread() {
						public void run () {
							String url=textFieldURL.getText();
							if (!url.startsWith("http://")) {
								url="http://"+url;
							}
							flag=DatabaseSite.updateSite(n,textFieldName.getText(),url);
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
