package Chi;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JProgressBar;

public class StartScreen extends JDialog {
	private static final long serialVersionUID = -8104624919051841151L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblInfo;
	private JProgressBar progressBar;


	public StartScreen() {
		setModal(true);
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblVersion = new JLabel(Config.VERSION);
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblVersion.setForeground(Color.WHITE);
		lblVersion.setBounds(292, 167, 108, 14);
		contentPanel.add(lblVersion);
		
		lblInfo = new JLabel("");
		lblInfo.setForeground(Color.WHITE);
		lblInfo.setBounds(10, 264, 430, 14);
		contentPanel.add(lblInfo);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(0, 287, 450, 14);
		contentPanel.add(progressBar);
		{
			JLabel lblBackground = new JLabel("");
			lblBackground.setBounds(0, 0, 450, 300);
			lblBackground.setIcon(Utility.resizeImageIcon(Theme.getIcon("StartScreen"),lblBackground.getWidth(),lblBackground.getHeight()));
			contentPanel.add(lblBackground);
		}
	}
	
	public void setText (String s) {
		lblInfo.setText(s);
	}
	
	public void setProgBarMax (int max) {
		progressBar.setMaximum(max);
	}
	
	public void setProgBarValue (int v) {
		progressBar.setValue(v);
	}
}
