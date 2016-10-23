package Chi;

import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import Database.DatabaseHSQL;

import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;

public class DialogMenuUIRunSQL extends JDialog {
	private static final long serialVersionUID = -6356587059394517899L;
	private static DialogMenuUIRunSQL activeWindow=null;
	private JTextArea textAreaSQL;
	
	public static DialogMenuUIRunSQL getInstance() {
		if (activeWindow==null) {
			activeWindow=new DialogMenuUIRunSQL();
			activeWindow.setLocationRelativeTo(null);
			activeWindow.setVisible(true);
		}
		else activeWindow.toFront();
		return activeWindow;
	}
	
	public DialogMenuUIRunSQL() {
		setTitle("Run SQL");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 360);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { DialogMenuUIRunSQL.activeWindow=null;}
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
		});
		
		JLabel lblNewLabel = new JLabel("SQL (Use @ as line delimiter) :");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitUI u=new WaitUI();
				u.setText("Running SQL...");
				Thread t=new Thread() {
					public void run () {
						DatabaseHSQL.runSQL("Run SQL", textAreaSQL.getText());
						u.setVisible(false);
					}
				};
				t.start();
				u.setVisible(true);
				u.dispose();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblNewLabel)
							.addPreferredGap(ComponentPlacement.RELATED, 477, Short.MAX_VALUE)
							.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNewLabel)
						.addComponent(btnRun))
					.addGap(8)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		textAreaSQL = new JTextArea();
		scrollPane.setViewportView(textAreaSQL);
		getContentPane().setLayout(groupLayout);

	}
}
