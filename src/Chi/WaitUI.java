package Chi;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class WaitUI extends JDialog {
	private static final long serialVersionUID = 7841588077624335701L;
	private JProgressBar progressBar;
	private JLabel lblText;

	public WaitUI() {
		setUndecorated(true);
		setModal(true);
		setResizable(false);
		setBounds(100, 100, 400, 108);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(38,38,38,255));
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 400, 108);
		getContentPane().add(panel);
		
		lblText = new JLabel("Wait");
		lblText.setForeground(Color.WHITE);
		lblText.setBounds(66, 36, 324, 23);
		lblText.setHorizontalAlignment(SwingConstants.LEFT);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(1, 93, 398, 14);
		
		JLabel lblBG = new JLabel();
		lblBG.setBounds(20, 30, 36, 36);
		lblBG.setIcon(Theme.getIcon("LoadingIcon"));
		panel.setLayout(null);
		panel.add(progressBar);
		panel.add(lblText);
		panel.add(lblBG);

	}

	public void setProgressBarMin (int min) {
		this.progressBar.setMinimum(min);
	}
	
	public void setProgressBarMax (int max) {
		this.progressBar.setMaximum(max);
	}
	
	public void setProgressBarValue (int v) {
		this.progressBar.setValue(v);
	}
	
	public void setText (String s) {
		this.lblText.setText(s);
	}
}
