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

public class DialogReadingExportSelectMonthRange extends JDialog {
	private static final long serialVersionUID = -2984954156093518852L;
	public Sensor s;
	private JComboBox<String> comboBoxAggregation;
	private static String [] exportColumns={"Name","Year","Month","Value"};
	private JComboBox<String> comboBoxFromMonth;
	private JComboBox<String> comboBoxFromYear;
	private JComboBox<String> comboBoxToMonth;
	private JComboBox<String> comboBoxToYear;
	
	public DialogReadingExportSelectMonthRange() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle("Export Monthly Report");
		setBounds(100, 100, 420, 219);
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
			    fc.setDialogTitle("Export Monthly Report");
			    fc.addChoosableFileFilter(new FileNameExtensionFilter("CSV", "csv"));
			    fc.setAcceptAllFileFilterUsed(true);
			    if (fc.showSaveDialog(DialogReadingExportSelectMonthRange.this)==JFileChooser.APPROVE_OPTION && (!fc.getSelectedFile().exists() || (fc.getSelectedFile().exists() && JOptionPane.showConfirmDialog(null,"Overwrite the file?","Export Monthly Report",JOptionPane.WARNING_MESSAGE)==JOptionPane.OK_OPTION) )) {
			    	LocalDateTime st=LocalDateTime.of(Integer.parseInt((String)comboBoxFromYear.getSelectedItem()),Integer.parseInt((String)comboBoxFromMonth.getSelectedItem()),1,0,0);
			    	LocalDateTime et=LocalDateTime.of(Integer.parseInt((String)comboBoxToYear.getSelectedItem()),Integer.parseInt((String)comboBoxToMonth.getSelectedItem()),1,0,0).plusMonths(1).minusSeconds(1);
			    	switch ((String)comboBoxAggregation.getSelectedItem()) {
				    	case "Sum" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getTotalReadingGroupByMonthBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}
				    	case "Average" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getAverageReadingGroupByMonthBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}
				    	case "Culmulative" : {
				    		ReadingExport.export(fc.getSelectedFile(),s.getSensorname(),exportColumns,DatabaseReading.getCulmulativeReadingGroupByMonthBetweenTime(s.getSensorname(), st, et));
				    		break;
				    	}   	
			    	}
			    	
				    DialogReadingExportSelectMonthRange.this.dispose();
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
		comboBoxFromMonth = new JComboBox<>();
		comboBoxToYear = new JComboBox<>();
		comboBoxToMonth = new JComboBox<>();
		
		for (int i=1990;i<=LocalDateTime.now().getYear();i++) {
			comboBoxFromYear.addItem(i+"");
			comboBoxToYear.addItem(i+"");
		}
		
		for (int i=1;i<=12;i++) {
			if (i<10) {
				comboBoxFromMonth.addItem("0"+i);
				comboBoxToMonth.addItem("0"+i);
			} else {
				comboBoxFromMonth.addItem(""+i);
				comboBoxToMonth.addItem(""+i);
			}
		}
		
		comboBoxFromYear.setSelectedItem(String.valueOf(LocalDateTime.now().minusMonths(1).getYear()));
		if (LocalDateTime.now().minusMonths(1).getMonthValue()<10) comboBoxFromMonth.setSelectedItem("0"+LocalDateTime.now().minusMonths(1).getMonthValue());
		comboBoxFromMonth.setSelectedItem(String.valueOf(LocalDateTime.now().minusMonths(1).getMonthValue()));
		
		comboBoxToYear.setSelectedItem(String.valueOf(LocalDateTime.now().getYear()));
		if (LocalDateTime.now().getMonthValue()<10) comboBoxToMonth.setSelectedItem("0"+LocalDateTime.now().getMonthValue());
		comboBoxToMonth.setSelectedItem(String.valueOf(LocalDateTime.now().getMonthValue()));
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
									.addComponent(lblFrom, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBoxFromYear, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(comboBoxFromMonth, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblAggregation, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(comboBoxToYear, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(comboBoxToMonth, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
										.addComponent(comboBoxAggregation, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
							.addContainerGap(73, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
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
						.addComponent(lblFrom)
						.addComponent(comboBoxFromYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxFromMonth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTo)
						.addComponent(comboBoxToYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboBoxToMonth, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAggregation)
						.addComponent(comboBoxAggregation, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(15)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnExport)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);

	}
}
