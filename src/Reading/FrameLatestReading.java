package Reading;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import Chi.Theme;
import Chi.Utility;
import DataStructures.MinMaxSortedList;
import Database.Cache;
import Database.DatabaseReading;
import Entity.Sensor;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameLatestReading extends JFrame {
	private static FrameLatestReading currInstance;
	private static final long serialVersionUID = 1L;

	private static class SensorTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Double.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class SensorTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Site","Reading"};
		
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
	
	private static class SensorTableRow implements Comparable<SensorTableRow> {
		private MinMaxSortedList<SensorTableRow> subRow;
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
				this.subRow=new MinMaxSortedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void addRow (SensorTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void updateInfo() {
			renderText[0]=obj.getSensorname();
			renderText[1]=obj.getController().getSite().getSitename();
			renderText[2]=obj.getMinthreshold()+";"+obj.getMaxthreshold()+";"+DatabaseReading.SensorLastReading.getOrDefault(obj,0.0)+";"+obj.getUnit();
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
		
		public int compareTo(SensorTableRow r) {
			return this.obj.compareTo(r.obj);
		}
	}
	
	private static class SensorTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			SensorTableRow row=(SensorTableRow)value;
			
			if (row!=null && row.renderText!=null) {
				if (aTable.getColumnName(aColumn).equals("Reading")) {
					String [] v=row.renderText[ArrayUtils.indexOf(SensorTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString().split(";");
					double min=Double.parseDouble(v[0]);
					double max=Double.parseDouble(v[1]);
					double curr=Double.parseDouble(v[2]);
					setText(String.format("%.3f",curr)+" "+v[3]);
					if (curr<min || curr>max) setText("<html><font color=\"red\">"+getText()+"</html>");
				} else {
					setText(row.renderText[ArrayUtils.indexOf(SensorTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString());
				}
			}
			
			if (aTable.isRowSelected(aRow)) {
				setBackground(aTable.getSelectionBackground());
				setForeground(aTable.getSelectionForeground());
			} else {
				setBackground(aTable.getBackground());
				setForeground(aTable.getForeground());
			}
			
			return this;
		}
	}
	
	private static class UpdateThread extends Thread {
		FrameLatestReading f;
		
		public void run () {
			try {Thread.sleep(2000);} catch (InterruptedException e) {}
			while (f.isVisible()) {
				FrameLatestReading.refresh();
				try {Thread.sleep(2000);} catch (InterruptedException e) {}
			}
		}
	}
	
	public static FrameLatestReading getInstance() {
		if (FrameLatestReading.currInstance==null) {
			FrameLatestReading.currInstance=new FrameLatestReading();
			FrameLatestReading.currInstance.setLocationRelativeTo(null);
		} else {
			FrameLatestReading.currInstance.toFront();
			FrameLatestReading.currInstance.repaint();
		}
		return FrameLatestReading.currInstance;
	}
	
	public static void refresh() {
		if (FrameLatestReading.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameLatestReading.currInstance.updateSensorTable();
				}
			});
		}
	}
	
	private JPanel contentPane;
	private SensorTable table;
	private SensorTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;
	private UpdateThread upTh;
	
	public FrameLatestReading() {
		setTitle("Latest Reading");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameLatestReading.currInstance=null; }
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
		
		upTh=new UpdateThread();
		upTh.f=this;
		upTh.start();
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
		FrameLatestReadingContextMenu popup=new FrameLatestReadingContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameLiveReading f=FrameLiveReading.getInstance(FrameLatestReading.this.getSelectedSensor());
						f.setLocationRelativeTo(FrameLatestReading.this);
						f.setVisible(true);
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
					case (KeyEvent.VK_ENTER) : {
						FrameLatestReadingActions.showLive(FrameLatestReading.this);
						break;
					}
					case (KeyEvent.VK_I) : {
						if (getSelectedCount()==1) FrameLatestReadingActions.exportInstance(FrameLatestReading.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_D) : {
						if (getSelectedCount()==1) FrameLatestReadingActions.exportDaily(FrameLatestReading.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_M) : {
						if (getSelectedCount()==1) FrameLatestReadingActions.exportMonthly(FrameLatestReading.this);
						else Toolkit.getDefaultToolkit().beep();
						break;	
					}
					case (KeyEvent.VK_Y) : {
						if (getSelectedCount()==1) FrameLatestReadingActions.exportYearly(FrameLatestReading.this);
						else Toolkit.getDefaultToolkit().beep();
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
