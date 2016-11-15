package Reading;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;

import Chi.Theme;
import Database.Cache;
import Entity.Controller;
import Entity.Sensor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;

public class DialogReadingSelectSensor extends JDialog {
	private static final long serialVersionUID = 4449101659845512529L;
	private JComboBox<String> comboBoxSensor;
	private JComboBox<String> comboBoxController;
	private JButton btnOK;
	private JButton btnCancel;
	public boolean OKPressed;
	public String selectedSensor;

	public DialogReadingSelectSensor() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Reading");
		setResizable(false);
		setModal(true);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 308, 190);
		getContentPane().setLayout(null);
		
		JLabel lblController = new JLabel("Controller :");
		lblController.setHorizontalAlignment(SwingConstants.RIGHT);
		lblController.setBounds(29, 47, 59, 14);
		getContentPane().add(lblController);
		
		comboBoxController = new JComboBox<>();
		comboBoxController.setBounds(98, 44, 145, 20);
		ArrayList<String> list=new ArrayList<>();
		for (Controller c : Cache.Controllers.map.values()) list.add(c.getControllername());
		Collections.sort(list);
		for (String s : list) comboBoxController.addItem(s);
		getContentPane().add(comboBoxController);
		
		comboBoxSensor = new JComboBox<>();
		comboBoxSensor.setBounds(98, 75, 145, 20);
		getContentPane().add(comboBoxSensor);
		
		JLabel lblSensor = new JLabel("Sensor :");
		lblSensor.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSensor.setBounds(29, 78, 59, 14);
		getContentPane().add(lblSensor);
		
		JLabel lblNewLabel = new JLabel("Select the sensor");
		lblNewLabel.setBounds(10, 11, 105, 14);
		getContentPane().add(lblNewLabel);
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboBoxSensor.getSelectedIndex()==-1 || comboBoxController.getSelectedIndex()==-1) {
					JOptionPane.showMessageDialog(null,"Please select a sensor.","View Reading",JOptionPane.INFORMATION_MESSAGE);
				} else {
					OKPressed=true;
					selectedSensor=(String)comboBoxSensor.getSelectedItem();
					dispose();
				}
			}
		});
		btnOK.setBounds(60, 127, 89, 23);
		getContentPane().add(btnOK);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(154, 127, 89, 23);
		getContentPane().add(btnCancel);
		
		comboBoxController.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				comboBoxSensor.removeAllItems();
				
				ArrayList<String> list=new ArrayList<>();
				for (Sensor s : Cache.Sensors.map.values())
					if (s.getController().getControllername().equals((String)comboBoxController.getSelectedItem()))
						list.add(s.getSensorname());

				Collections.sort(list);
				for (String s : list) comboBoxSensor.addItem(s);
			}
			
		});
		if (comboBoxController.getItemCount()>0) comboBoxController.setSelectedIndex(0);
	}
}
