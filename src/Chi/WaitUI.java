package Chi;

import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.BorderLayout;
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
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(0, 0, 400, 108);
		getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		lblText = new JLabel("Wait");
		panel.add(lblText, BorderLayout.CENTER);
		lblText.setHorizontalAlignment(SwingConstants.CENTER);
		
		progressBar = new JProgressBar();
		panel.add(progressBar, BorderLayout.SOUTH);

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
