package Reading;

import java.time.LocalDateTime;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import Chi.Theme;
import Database.DatabaseReading;
import Entity.Sensor;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class DialogReadingExportYearlySelectRange extends JDialog {
	private static final long serialVersionUID = -2984954156093518852L;
	public Sensor s;
	private JComboBox<String> comboBoxAggregation;
	private static String [] exportColumns={"Name","Year","Value"};
	private JComboBox<String> comboBoxFromYear;
	private JComboBox<String> comboBoxToYear;
	
	public DialogReadingExportYearlySelectRange() {
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Yearly Report");
		setBounds(100, 100, 240, 219);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		
		JLabel lblFrom = new JLabel("From :");
		lblFrom.setBounds(10, 34, 67, 14);
		lblFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblTo = new JLabel("To :");
		lblTo.setBounds(10, 72, 67, 14);
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnExport = new JButton("Export");
		btnExport.setBounds(28, 157, 86, 23);
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    final JFileChooser fc = new JFileChooser();
			    fc.setSelectedFile(new File(s.getSensorname()+".csv"));
			    fc.setDialogTitle("Export Yearly Report");
			    fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
			    fc.setAcceptAllFileFilterUsed(true);
			    if (fc.showSaveDialog(DialogReadingExportYearlySelectRange.this)==JFileChooser.APPROVE_OPTION && (!fc.getSelectedFile().exists() || (fc.getSelectedFile().exists() && JOptionPane.showConfirmDialog(null,"Overwrite the file?","Export Yearly Report",JOptionPane.WARNING_MESSAGE)==JOptionPane.OK_OPTION) )) {
			    	LocalDateTime st=LocalDateTime.of(Integer.parseInt((String)comboBoxFromYear.getSelectedItem()),1,1,0,0);
			    	LocalDateTime et=LocalDateTime.of(Integer.parseInt((String)comboBoxToYear.getSelectedItem()),1,1,0,0).plusYears(1).minusSeconds(1);
			    	switch ((String)comboBoxAggregation.getSelectedItem()) {
				    	case "Sum" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getTotalReadingGroupByYearBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}
				    	case "Average" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getAverageReadingGroupByYearBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}
				    	case "Cumulative" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getCumulativeReadingGroupByYearBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}   	
			    	}
			    	
			    	DialogReadingExportYearlySelectRange.this.dispose();
			    }
			    fc.setVisible(false);

			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(121, 157, 86, 23);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		comboBoxAggregation = new JComboBox<>();
		comboBoxAggregation.setBounds(81, 107, 109, 20);
		comboBoxAggregation.addItem("Sum");
		comboBoxAggregation.addItem("Average");
		comboBoxAggregation.addItem("Cumulative");
		
		JLabel lblAggregation = new JLabel("Aggregation :");
		lblAggregation.setBounds(10, 110, 67, 14);
		lblAggregation.setHorizontalAlignment(SwingConstants.RIGHT);
		
		comboBoxFromYear = new JComboBox<>();
		comboBoxFromYear.setBounds(81, 31, 109, 20);
		comboBoxToYear = new JComboBox<>();
		comboBoxToYear.setBounds(81, 69, 109, 20);
		
		for (int i=1990;i<=LocalDateTime.now().getYear();i++) {
			comboBoxFromYear.addItem(i+"");
			comboBoxToYear.addItem(i+"");
		}
		
		comboBoxFromYear.setSelectedItem(String.valueOf(LocalDateTime.now().getYear()-1));
		comboBoxToYear.setSelectedItem(String.valueOf(LocalDateTime.now().getYear()));
		getContentPane().setLayout(null);
		getContentPane().add(lblFrom);
		getContentPane().add(comboBoxFromYear);
		getContentPane().add(lblTo);
		getContentPane().add(lblAggregation);
		getContentPane().add(comboBoxToYear);
		getContentPane().add(comboBoxAggregation);
		getContentPane().add(btnExport);
		getContentPane().add(btnCancel);

	}
}
