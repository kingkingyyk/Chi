package Reading;

import java.time.LocalDateTime;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdesktop.swingx.JXDatePicker;

import Chi.Theme;
import Chi.Utility;
import Database.DatabaseReading;
import Entity.Sensor;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class DialogReadingExportSelectDayRange extends JDialog {
	private static final long serialVersionUID = -2984954156093518852L;
	public Sensor s;
	private JComboBox<String> comboBoxAggregation;
	private JXDatePicker dateTo;
	private JXDatePicker dateFrom;
	private static String [] exportColumns={"Name","Year","Month","Day","Value"};
	
	public DialogReadingExportSelectDayRange() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Daily Report");
		setBounds(100, 100, 420, 219);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		
		dateFrom = new JXDatePicker(Utility.localDateTimeToUtilDate(LocalDateTime.now().minusDays(1)));
		
		dateTo = new JXDatePicker(Utility.localDateTimeToUtilDate(LocalDateTime.now()));
		
		JLabel lblFrom = new JLabel("From :");
		lblFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblTo = new JLabel("To :");
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    final JFileChooser fc = new JFileChooser();
			    fc.setSelectedFile(new File(s.getSensorname()+".csv"));
			    fc.setDialogTitle("Export Daily Report");
			    fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
			    fc.setAcceptAllFileFilterUsed(true);
			    if (fc.showSaveDialog(DialogReadingExportSelectDayRange.this)==JFileChooser.APPROVE_OPTION && (!fc.getSelectedFile().exists() || (fc.getSelectedFile().exists() && JOptionPane.showConfirmDialog(null,"Overwrite the file?","Export Daily Report",JOptionPane.WARNING_MESSAGE)==JOptionPane.OK_OPTION) )) {
			    	LocalDateTime st=Utility.dateToLocalDateTime(dateFrom.getDate());
			    	LocalDateTime et=Utility.dateToLocalDateTime(dateFrom.getDate()).plusDays(1).minusSeconds(1);
			    	switch ((String)comboBoxAggregation.getSelectedItem()) {
				    	case "Sum" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getTotalReadingGroupByDayBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}
				    	case "Average" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getAverageReadingGroupByDayBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}
				    	case "Culmulative" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getCulmulativeReadingGroupByDayBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}   	
			    	}
			    	
				    DialogReadingExportSelectDayRange.this.dispose();
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
		
		comboBoxAggregation = new JComboBox<>();
		comboBoxAggregation.addItem("Sum");
		comboBoxAggregation.addItem("Average");
		comboBoxAggregation.addItem("Culmulative");
		
		JLabel lblAggregation = new JLabel("Aggregation :");
		lblAggregation.setHorizontalAlignment(SwingConstants.RIGHT);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblAggregation, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(comboBoxAggregation, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(dateTo, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))))
							.addContainerGap(73, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
									.addGap(94))
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE))
							.addContainerGap())))
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
						.addComponent(comboBoxAggregation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblAggregation))
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnExport)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);

	}
}
