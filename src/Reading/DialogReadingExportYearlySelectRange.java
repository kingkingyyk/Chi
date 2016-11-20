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
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Yearly Report");
		setBounds(100, 100, 248, 219);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		
		JLabel lblFrom = new JLabel("From :");
		lblFrom.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JLabel lblTo = new JLabel("To :");
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnExport = new JButton("Export");
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
				    	case "Culmulative" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getCulmulativeReadingGroupByYearBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}   	
			    	}
			    	
			    	DialogReadingExportYearlySelectRange.this.dispose();
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
		
		comboBoxFromYear = new JComboBox<>();
		comboBoxToYear = new JComboBox<>();
		
		for (int i=1990;i<=LocalDateTime.now().getYear();i++) {
			comboBoxFromYear.addItem(i+"");
			comboBoxToYear.addItem(i+"");
		}
		
		comboBoxFromYear.setSelectedItem(String.valueOf(LocalDateTime.now().getYear()-1));
		comboBoxToYear.setSelectedItem(String.valueOf(LocalDateTime.now().getYear()));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBoxFromYear, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblAggregation, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(comboBoxToYear, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
										.addComponent(comboBoxAggregation, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(29)
							.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(197, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(31)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblFrom)
						.addComponent(comboBoxFromYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTo)
						.addComponent(comboBoxToYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAggregation)
						.addComponent(comboBoxAggregation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnExport)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);

	}
}
