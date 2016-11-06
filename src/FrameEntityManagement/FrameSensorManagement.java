package FrameEntityManagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import Chi.Theme;
import Chi.Utility;
import Database.Cache;
import Entity.Sensor;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
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
		private Class [] colClass={String.class,String.class,Double.class,Double.class,Double.class,String.class,String.class,Double.class,Double.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class SensorTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Class","Min Value","Max Value","Trans. Fact","Unit","Attached On","Min Threshold","Max Threshold","Position"};
		
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
		private LinkedList<SensorTableRow> subRow;
		private HashMap<Sensor,SensorTableRow> rowObj;
		public String [] renderText;
		private Sensor obj;
		
		public SensorTableRow(Sensor s) {
			if (s!=null) {
				renderText=new String[SensorTableModel.COLUMNS.length];
				obj=s;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				this.subRow=new LinkedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void addRow (SensorTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void updateInfo() {
			renderText[0]=obj.getSensorname();
			renderText[1]=obj.getSensorclass().getClassname();
			renderText[2]=obj.getMinvalue().toString();
			renderText[3]=obj.getMaxvalue().toString();
			renderText[4]=obj.getTransformationfactor().toString();
			renderText[5]=obj.getUnit();
			renderText[6]=obj.getController().getControllername();
			renderText[7]=obj.getMinthreshold().toString();
			renderText[8]=obj.getMaxthreshold().toString();
			renderText[9]=String.format("%.2f,%.2f",obj.getPositionx(),obj.getPositiony());
		}
		
		public void removeRowBySensor (Sensor s) {
			if (this.rowObj.containsKey(s)) {
				this.subRow.remove(this.rowObj.get(s));
				this.rowObj.remove(s);
			}
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
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameSensorManagement.currInstance.updateSensorTable();
				}
			});
		}
	}
	
	private JPanel contentPane;
	private SensorTable table;
	private SensorTableRow rootRow;
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
		
		createTable();
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
		FrameSensorManagementContextMenu popup=new FrameSensorManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameSensorManagementActions.edit(FrameSensorManagement.this);
					}
				}
	            
	            if (e.getButton()==MouseEvent.BUTTON3 && e.getClickCount()==1) {
	            	popup.show(e.getComponent(), e.getX(), e.getY());
	            }
			}

			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
		});
		scrollPane.setViewportView(table);
		
		rootRow=new SensorTableRow(null);
		updateSensorTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameSensorManagementActions.add();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameSensorManagementActions.edit(FrameSensorManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameSensorManagementActions.delete(FrameSensorManagement.this);
						break;
					}
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
			
		});
		
		if (table.getColumnModel().getColumnCount()!=0) {
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(133);
			table.getColumnModel().getColumn(2).setPreferredWidth(54);
			table.getColumnModel().getColumn(3).setPreferredWidth(54);
			table.getColumnModel().getColumn(4).setPreferredWidth(54);
			table.getColumnModel().getColumn(5).setPreferredWidth(54);
			table.getColumnModel().getColumn(6).setPreferredWidth(100);
			table.getColumnModel().getColumn(7).setPreferredWidth(54);
			table.getColumnModel().getColumn(8).setPreferredWidth(54);
			table.getColumnModel().getColumn(9).setPreferredWidth(54);
		}
	}
	
	public void updateSensorTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<Sensor> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.Sensors.map.values());
		for (Sensor act : existing) {
			rootRow.rowObj.get(act).updateInfo();
		}
		
		boolean flag=false;
		HashSet<Sensor> removedAct=new HashSet<>();
		removedAct.addAll(rootRow.rowObj.keySet());
		removedAct.removeAll(Cache.Sensors.map.values());
		
		for (Sensor act : removedAct) {
			flag=true;
			rootRow.removeRowBySensor(act);
		}
		
		HashSet<Sensor> addedAct=new HashSet<>();
		addedAct.addAll(Cache.Sensors.map.values());
		addedAct.removeAll(rootRow.rowObj.keySet());
		
		for (Sensor act : addedAct) {
			flag=true;
			rootRow.addRow(new SensorTableRow(act));
		}
		
		if (flag) {
			table.setTreeTableModel(new SensorTableModel(rootRow));
			
			for (int i=0;i<table.getColumnCount();i++) 	table.getColumn(i).setCellRenderer(new SensorTableCellRenderer());
		}
		
		selectedRow=Math.min(selectedRow,rootRow.rowObj.size()-1);
		if (selectedRow!=-1) {
			table.addRowSelectionInterval(table.convertRowIndexToView(selectedRow),table.convertRowIndexToView(selectedRow));
		}
		
		if (table.getColumnCount()>0) {
			for (int i=0;i<width.length;i++) {
				table.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
			}
		}
		
		this.table.repaint();
	}
	
	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Sensor getSelectedSensor () {
		return ((SensorTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public Sensor[] getSelectedSensors () {
		int [] selected=this.getSelectedRows();
		Sensor [] s=new Sensor[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) s[i]=((SensorTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return s;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
