package Reading;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Chi.WaitUI;

public class ReadingExport {

	public static void export (File f, String name, String [] columns, ArrayList<Object []> data) {
		if (f.exists() && !f.delete()) {
			JOptionPane.showMessageDialog(null, "Fail to overwrite file!", "Export", JOptionPane.ERROR_MESSAGE);
			return;
		}
		WaitUI u=new WaitUI();
		u.setText("Exporting data...");
		u.setLocationRelativeTo(null);
		u.setProgressBarMax(data.size());
		Thread t=new Thread() {
			public void run () {
				try {
					PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f)));
					for (int i=0;i<columns.length;i++) {
						pw.print(columns[i]);
						if (i<columns.length-1) pw.print(',');
						else pw.println();
					}
					for (int i=0;i<data.size();i++) {
						Object [] o=data.get(i);
						pw.print(name);
						pw.print(',');
						for (int i2=0;i2<o.length;i2++) {
							pw.print(o[i2].toString());
							if (i2<o.length-1) pw.print(',');
							else pw.println();
						}
						u.setProgressBarValue(i+1);
					}
					pw.close();
					if (JOptionPane.showConfirmDialog(null, "Export successful! Do you want to open the file now?", "Export", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
						Desktop.getDesktop().open(f);
					}
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, "Error during export : "+e.getMessage(), "Export", JOptionPane.ERROR_MESSAGE);
				}
				u.setVisible(false);
			}
		};
		t.start();
		u.setVisible(true);
	}
	
}
