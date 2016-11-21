package Reading;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

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
import Chi.Utility;
import Chi.WaitUI;
import Database.DatabaseReading;
import Entity.Sensor;
import Entity.SensorReading;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class DialogReadingExportInstanceSelectRange extends JDialog {
	private static final long serialVersionUID = -2984954156093518852L;
	public Sensor s;
	private DateTimePicker dateTo;
	private DateTimePicker dateFrom;
	private static String [] exportColumns={"Name","Timestamp","Value"};
	private JComboBox<String> comboBoxGroupBy;
	private JComboBox<String> comboBoxValue;
	private static int [] groupByValue={1,5,15,30};
	
	public DialogReadingExportInstanceSelectRange() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Instance Report");
		setBounds(100, 100, 420, 260);
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
					    	if (comboBoxGroupBy.getSelectedItem().equals("None")) {
					    		readings.addAll(list.stream()
						    		 .map(sr -> new Object [] {formatter.format(sr.getTimestamp()),sr.getActualValue()})
						    		.collect(Collectors.toList()));
					    	} else {
					    		HashMap<Long,Double> valueMap=new HashMap<>();
					    		long factor=60*groupByValue[comboBoxGroupBy.getSelectedIndex()]*1000;
					    		
					    		if (comboBoxValue.getSelectedItem().equals("Average")) {
					    			HashMap<Long,Integer> itemsCountMap=new HashMap<>();
							    	for (SensorReading sr : list) {
							    		long key=Utility.localDateTimeToLong(sr.getTimestamp())/factor*factor;
							    		valueMap.put(key,valueMap.getOrDefault(key,0.0)+sr.getActualValue());
							    		itemsCountMap.put(key,itemsCountMap.getOrDefault(key,0)+1);
							    	}
							    	
							    	for (Long key : valueMap.keySet()) valueMap.put(key,valueMap.get(key)/itemsCountMap.get(key));
							    	
					    		} else if (comboBoxValue.getSelectedItem().equals("Minimum")) {
							    	for (SensorReading sr : list) {
							    		long key=Utility.localDateTimeToLong(sr.getTimestamp())/factor*factor;
							    		valueMap.put(key,Math.min(valueMap.getOrDefault(key,Double.MAX_VALUE),sr.getActualValue()));
							    	}
					    		} else if (comboBoxValue.getSelectedItem().equals("Maximum")) {
							    	for (SensorReading sr : list) {
							    		long key=Utility.localDateTimeToLong(sr.getTimestamp())/factor*factor;
							    		valueMap.put(key,Math.min(valueMap.getOrDefault(key,Double.MIN_VALUE),sr.getActualValue()));
							    	}
					    		}
					    		
						    	readings.addAll(valueMap.keySet().stream()
						    			.map(s -> Utility.dateToLocalDateTime(new Date(s)))
						    			.sorted()
						    			.map(dt -> new Object[]{formatter.format(dt),valueMap.get(Utility.localDateTimeToLong(dt))})
						    			.collect(Collectors.toList()));
					    	}
					    	
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
		
		JLabel lbl = new JLabel("Group By :");
		lbl.setHorizontalAlignment(SwingConstants.RIGHT);
		
		comboBoxGroupBy = new JComboBox<>();
		comboBoxGroupBy.addItem("None");
		comboBoxGroupBy.addItem("5 minutes");
		comboBoxGroupBy.addItem("15 minutes");
		comboBoxGroupBy.addItem("30 minutes");
		comboBoxGroupBy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				comboBoxValue.setEnabled(comboBoxGroupBy.getSelectedIndex()!=0);
			}
		});
		
		JLabel lblValue = new JLabel("Value :");
		lblValue.setHorizontalAlignment(SwingConstants.RIGHT);
		
		comboBoxValue = new JComboBox<>();
		comboBoxValue.setEnabled(false);
		comboBoxValue.addItem("Average");
		comboBoxValue.addItem("Minimum");
		comboBoxValue.addItem("Maximum");
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
									.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(lblTo, GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(dateTo, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
										.addComponent(comboBoxGroupBy, 0, 249, Short.MAX_VALUE))))
							.addGap(78))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addGap(8))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblValue, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBoxValue, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
							.addContainerGap(78, Short.MAX_VALUE))))
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
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lbl)
						.addComponent(comboBoxGroupBy, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblValue)
						.addComponent(comboBoxValue, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExport)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);

	}
}
