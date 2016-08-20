package Chi;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class FrameSensorClassManagement extends JFrame {
	private static FrameSensorClassManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class SensorClassTable extends JTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	public static FrameSensorClassManagement getInstance() {
		if (FrameSensorClassManagement.currInstance==null) {
			FrameSensorClassManagement.currInstance=new FrameSensorClassManagement();
			FrameSensorClassManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameSensorClassManagement.currInstance.toFront();
			FrameSensorClassManagement.currInstance.repaint();
		}
		return FrameSensorClassManagement.currInstance;
	}
	
	private JPanel contentPane;
	private SensorClassTable table;
	private ArrayList<Object []> list=new ArrayList<>();
	public HashSet<String> classDB=new HashSet<>();
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameSensorClassManagement() {
		setTitle("Sensor Class Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameSensorClassManagement.currInstance=null;}
			@Override
			public void windowActivated(WindowEvent arg0) {}
			@Override
			public void windowClosing(WindowEvent arg0) {}
			@Override
			public void windowDeactivated(WindowEvent arg0) {}
			@Override
			public void windowDeiconified(WindowEvent arg0) {}
			@Override
			public void windowIconified(WindowEvent arg0) {}
			@Override
			public void windowOpened(WindowEvent arg0) {}
			
		});
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setBounds(100, 100, 700, 273);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
					.addGap(0))
		);
		
		contentPane.setLayout(gl_contentPane);
		
		createTable();
		updateSensorClassTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		Object [][] obj=this.list.toArray(new Object [this.list.size()][]);
		table = new SensorClassTable();
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(new DefaultTableModel(obj,new String [] {"Name"}));
		table.setComponentPopupMenu(new FrameSensorClassManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateSensorClassTable() {
		WaitUI u=new WaitUI();
		u.setText("Populating sensor class");
		updateSuccess=false;
		Thread t=new Thread() {
			public void run () {
				classDB=new HashSet<>();
				ResultSet rs=DatabaseSensorClass.getSensorClass();
				if (rs!=null) {
					list.clear();
					try {
						while (rs.next()) {
							list.add(new Object [] {rs.getString(1)});
							classDB.add(rs.getString(1));
						}
					} catch (SQLException e) {e.printStackTrace();}
					
					int lastSelectedRow=-1;
					if (table!=null) {
						 lastSelectedRow=table.getSelectedRow();
					}
					createTable();
					if (lastSelectedRow>=0 && list.size()>0) {
						lastSelectedRow=Math.min(lastSelectedRow,list.size()-1);
						table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
					}
					
					updateSuccess=true;
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		
		if (updateSuccess) {
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
		}
	}
	
	public int getSelectedRow () {
		if (this.table.getSelectedRow()==-1) {
			return -1;
		}
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Object [] getSelectedObj () {
		return this.list.get(this.getSelectedRow());
	}
}
