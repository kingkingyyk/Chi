package Chi;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameSensorManagement extends JFrame {
	private static FrameSensorManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class SensorTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Double.class,Double.class,Double.class,String.class,String.class,Double.class,Double.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class SensorTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Class","Min Value","Max Value","Trans. Fact","Unit","Attached On","Min Threshold","Max Threshold"};
		
		public SensorTableModel (SensorTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((SensorTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((SensorTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((SensorTableRow)arg0).getSubRowIndex((SensorTableRow)arg1);
		}

		@Override
		public Object getValueAt(Object arg0, int arg1) {
			return arg0;
		}
		
		@Override
	    public String getColumnName(int column) {
	        return COLUMNS[column];
	    }

	}
	
	private static class SensorTableRow {
		private ArrayList<SensorTableRow> subRow;
		public String [] renderText;
		
		public SensorTableRow(Sensor s) {
			if (s!=null) {
				renderText=new String[SensorTableModel.COLUMNS.length];
				renderText[0]=s.getSensorname();
				renderText[1]=s.getSensorclass().getClassname();
				renderText[2]=s.getMinvalue().toString();
				renderText[3]=s.getMaxvalue().toString();
				renderText[4]=s.getTransformationfactor().toString();
				renderText[5]=s.getUnit();
				renderText[6]=s.getController().getControllername();
				renderText[7]=s.getMinthreshold().toString();
				renderText[8]=s.getMaxthreshold().toString();
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (SensorTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
		}
		
		public int getSubRowIndex (SensorTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public SensorTableRow getSubRow (int index) {
			return this.subRow.get(index);
		}
		
		public int subRowCount () {
			if (this.subRow==null) return 0;
			else return this.subRow.size();
		}
		
		public String toString() {
			return this.renderText[0];
		}
	}
	
	private static class SensorTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			SensorTableRow row=(SensorTableRow)value;
			if (row!=null && row.renderText!=null) {
				setText(row.renderText[ArrayUtils.indexOf(SensorTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString());
			}

			if (aTable.isRowSelected(aRow)) {
				setBackground(SystemColor.textHighlight);
				setForeground(SystemColor.WHITE);
			} else {
				setBackground(Color.WHITE);
				setForeground(SystemColor.BLACK);
			}
			return this;
		}
	}
	
	public static FrameSensorManagement getInstance() {
		if (FrameSensorManagement.currInstance==null) {
			FrameSensorManagement.currInstance=new FrameSensorManagement();
			FrameSensorManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameSensorManagement.currInstance.toFront();
			FrameSensorManagement.currInstance.repaint();
		}
		return FrameSensorManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameSensorManagement.currInstance!=null) {
			FrameSensorManagement.currInstance.updateSensorTable();
			FrameSensorManagement.currInstance.table.repaint();
		}
	}
	
	private JPanel contentPane;
	private SensorTable table;
	private SensorTableRow rootRow;
	private ArrayList<Sensor> list=new ArrayList<>();
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameSensorManagement() {
		setTitle("Sensor Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameSensorManagement.currInstance=null;}
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
		
		updateSensorTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		table = new SensorTable();
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("SensorIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		table.setComponentPopupMenu(new FrameSensorManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateSensorTable() {
		rootRow=new SensorTableRow(null);
		list.clear(); list.addAll(Cache.Sensors.map.values());
		for (Sensor s : list) {
			rootRow.addRow(new SensorTableRow(s));
		}
		
		int lastSelectedRow=-1;
		if (table!=null) {
			lastSelectedRow=table.getSelectedRow();
		}
		createTable();
		table.setAutoCreateRowSorter(true);
		table.setTreeTableModel(new SensorTableModel(rootRow));
		
		if (lastSelectedRow>=0 && list.size()>0) {
			lastSelectedRow=Math.min(lastSelectedRow,list.size()-1);
			table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
		}
		updateSuccess=true;

		if (updateSuccess) {
			table.getColumn(0).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(1).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(2).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(3).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(4).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(5).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(6).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(7).setCellRenderer(new SensorTableCellRenderer());
			table.getColumn(8).setCellRenderer(new SensorTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(133);
			table.getColumnModel().getColumn(2).setPreferredWidth(54);
			table.getColumnModel().getColumn(3).setPreferredWidth(54);
			table.getColumnModel().getColumn(4).setPreferredWidth(54);
			table.getColumnModel().getColumn(5).setPreferredWidth(54);
			table.getColumnModel().getColumn(6).setPreferredWidth(100);
			table.getColumnModel().getColumn(7).setPreferredWidth(54);
			table.getColumnModel().getColumn(8).setPreferredWidth(54);
		}
	}
	
	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Sensor getSelectedSensor () {
		return this.list.get(this.getSelectedRow());
	}
}
