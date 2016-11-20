package Reading;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.lgooddatepicker.components.DateTimePicker;

import Chi.Theme;
import Chi.WaitUI;
import Database.DatabaseReading;
import Entity.Sensor;
import Entity.SensorReading;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;

public class DialogReadingExportInstanceSelectRange extends JDialog {
	private static final long serialVersionUID = -2984954156093518852L;
	public Sensor s;
	private DateTimePicker dateTo;
	private DateTimePicker dateFrom;
	private static String [] exportColumns={"Name","Timestamp","Value"};
	
	public DialogReadingExportInstanceSelectRange() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Instance Report");
		setBounds(100, 100, 420, 198);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		
		dateFrom = new DateTimePicker();
		dateFrom.getDatePicker().getSettings().setAllowEmptyDates(false);
		dateFrom.getDatePicker().getSettings().setVisibleClearButton(false);
		dateFrom.getTimePicker().getSettings().setAllowEmptyTimes(false);
		dateFrom.setDateTimeStrict(LocalDateTime.now().minusHours(1));
		
		dateTo = new DateTimePicker();
		dateTo.getDatePicker().getSettings().setAllowEmptyDates(false);
		dateTo.getDatePicker().getSettings().setVisibleClearButton(false);
		dateTo.getTimePicker().getSettings().setAllowEmptyTimes(false);
		dateTo.setDateTimeStrict(LocalDateTime.now());
		
		JLabel lblFrom = new JLabel("From :");
		lblFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblTo = new JLabel("To :");
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    final JFileChooser fc = new JFileChooser();
			    fc.setSelectedFile(new File(s.getSensorname()+".csv"));
			    fc.setDialogTitle("Export Instance Report");
			    fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
			    fc.setAcceptAllFileFilterUsed(true);
			    if (fc.showSaveDialog(DialogReadingExportInstanceSelectRange.this)==JFileChooser.APPROVE_OPTION && (!fc.getSelectedFile().exists() || (fc.getSelectedFile().exists() && JOptionPane.showConfirmDialog(null,"Overwrite the file?","Export Instance Report",JOptionPane.WARNING_MESSAGE)==JOptionPane.OK_OPTION) )) {
					LocalDateTime st=dateFrom.getDateTimeStrict();
					LocalDateTime et=dateTo.getDateTimeStrict().plusMinutes(1).minusSeconds(1);
			    	
			    	WaitUI u=new WaitUI();
			    	u.setText("Querying data...");
			    	u.setProgressBarMax(3);
			    	Thread t=new Thread() {
			    		private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMMM/YYYY HH:mm:ss");
			    		
			    		public void run () {
					    	LinkedList<SensorReading> list=DatabaseReading.getReadingBetweenTime(s.getSensorname(),st,et);
					    	ArrayList<Object []> readings=new ArrayList<>();
					    	
					    	u.setText("Formatting data...");
					    	u.setProgressBarValue(1);
					    	for (SensorReading sr : list)
					    		readings.add(new Object [] {formatter.format(sr.getTimestamp()),sr.getActualValue()});
					    	
					    	u.setText("Exporting data...");
					    	u.setProgressBarValue(2);
					    	ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,readings);
					    	
					    	u.setText("Done!");
					    	u.setProgressBarValue(3);
					    	u.setVisible(false);
			    		}
			    	};
			    	t.start();

			    	u.setVisible(true);
			    	DialogReadingExportInstanceSelectRange.this.dispose();
			    }
			    fc.setVisible(false);

			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(dateTo, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)))
							.addContainerGap(73, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addGap(8))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFrom))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(dateTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTo))
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExport)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);

	}
}
