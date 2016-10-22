package Chi;

import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.ActionEvent;

public class DialogMenuUIRunCQL extends JDialog {
	private static final long serialVersionUID = -6356587059394517899L;
	private static DialogMenuUIRunCQL activeWindow=null;
	private JTextArea textAreaCQL;
	
	public static DialogMenuUIRunCQL getInstance() {
		if (activeWindow==null) {
			activeWindow=new DialogMenuUIRunCQL();
			activeWindow.setLocationRelativeTo(null);
			activeWindow.setVisible(true);
		}
		else activeWindow.toFront();
		return activeWindow;
	}
	
	public DialogMenuUIRunCQL() {
		setTitle("Run CQL");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 360);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { DialogMenuUIRunCQL.activeWindow=null;}
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
		
		JLabel lblNewLabel = new JLabel("CQL (Use @ as line delimiter) :");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WaitUI u=new WaitUI();
				u.setText("Running CQL...");
				Thread t=new Thread() {
					public void run () {
						DatabaseCassandra.runSQL("Run CQL", textAreaCQL.getText());
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
		
		textAreaCQL = new JTextArea();
		scrollPane.setViewportView(textAreaCQL);
		getContentPane().setLayout(groupLayout);

	}
}
