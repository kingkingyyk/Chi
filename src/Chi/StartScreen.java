package Chi;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class StartScreen extends JDialog {
	private static final long serialVersionUID = -8104624919051841151L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblBackground;
	private JLabel lblInfo;
	private JProgressBar progressBar;
	private JLabel lblVersion;

	public StartScreen() {
		setModal(true);
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		lblVersion = new JLabel(Config.VERSION);
		lblVersion.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVersion.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblVersion.setForeground(new Color(255,255,255,0));
		lblVersion.setBounds(172, 167, 109, 14);
		contentPanel.add(lblVersion);
		
		lblInfo = new JLabel("");
		lblInfo.setForeground(Color.WHITE);
		lblInfo.setBounds(10, 264, 430, 14);
		contentPanel.add(lblInfo);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(0, 287, 450, 14);
		contentPanel.add(progressBar);
		{
			lblBackground = new JLabel("");
			lblBackground.setBounds(0, 0, 450, 300);
			lblBackground.setIcon(Utility.resizeImageIcon(Theme.getIcon("StartScreen"),lblBackground.getWidth(),lblBackground.getHeight()));
			contentPanel.add(lblBackground);
		}
		
		Thread t=new Thread() {
			public void run() {
				double start=172;
				double end=250;
				double valuePerTick=(end-start)/60;
				for (int i=1;i<=60;i++) {
					start+=valuePerTick;
					lblVersion.setLocation((int)start, lblVersion.getY());
					lblVersion.setForeground(new Color(255,255,255,(int)(i/60.0*255)));
					try {Thread.sleep(17); } catch (InterruptedException e) {}
				}
			}
		};
		t.start();
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
