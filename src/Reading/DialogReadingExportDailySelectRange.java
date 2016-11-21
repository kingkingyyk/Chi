package Reading;

import java.time.LocalDateTime;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

public class DialogReadingExportDailySelectRange extends JDialog {
	private static final long serialVersionUID = -2984954156093518852L;
	public Sensor s;
	private JComboBox<String> comboBoxAggregation;
	private JXDatePicker dateTo;
	private JXDatePicker dateFrom;
	private static String [] exportColumns={"Name","Year","Month","Day","Value"};
	
	public DialogReadingExportDailySelectRange() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Daily Report");
		setBounds(100, 100, 420, 219);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		
		dateFrom = new JXDatePicker(Utility.localDateTimeToUtilDate(LocalDateTime.now().minusDays(1)));
		dateFrom.setBounds(86, 31, 250, 22);
		
		dateTo = new JXDatePicker(Utility.localDateTimeToUtilDate(LocalDateTime.now()));
		dateTo.setBounds(86, 71, 250, 22);
		
		JLabel lblFrom = new JLabel("From :");
		lblFrom.setBounds(10, 35, 67, 14);
		lblFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblTo = new JLabel("To :");
		lblTo.setBounds(10, 75, 67, 14);
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnExport = new JButton("Export");
		btnExport.setBounds(222, 157, 86, 23);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    final JFileChooser fc = new JFileChooser();
			    fc.setSelectedFile(new File(s.getSensorname()+".csv"));
			    fc.setDialogTitle("Export Daily Report");
			    fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
			    fc.setAcceptAllFileFilterUsed(true);
			    if (fc.showSaveDialog(DialogReadingExportDailySelectRange.this)==JFileChooser.APPROVE_OPTION && (!fc.getSelectedFile().exists() || (fc.getSelectedFile().exists() && JOptionPane.showConfirmDialog(null,"Overwrite the file?","Export Daily Report",JOptionPane.WARNING_MESSAGE)==JOptionPane.OK_OPTION) )) {
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
			    	
			    	DialogReadingExportDailySelectRange.this.dispose();
			    }
			    fc.setVisible(false);

			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(318, 157, 86, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		comboBoxAggregation = new JComboBox<>();
		comboBoxAggregation.setBounds(86, 111, 250, 20);
		comboBoxAggregation.addItem("Sum");
		comboBoxAggregation.addItem("Average");
		comboBoxAggregation.addItem("Culmulative");
		
		JLabel lblAggregation = new JLabel("Aggregation :");
		lblAggregation.setBounds(10, 114, 67, 14);
		lblAggregation.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().setLayout(null);
		getContentPane().add(lblFrom);
		getContentPane().add(dateFrom);
		getContentPane().add(lblTo);
		getContentPane().add(lblAggregation);
		getContentPane().add(comboBoxAggregation);
		getContentPane().add(dateTo);
		getContentPane().add(btnExport);
		getContentPane().add(btnCancel);

	}
}
