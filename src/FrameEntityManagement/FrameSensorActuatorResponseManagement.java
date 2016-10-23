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

import Chi.Config;
import Chi.Theme;
import Chi.Utility;
import Database.Cache;
import Entity.Sensoractuatorresponse;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FrameSensorActuatorResponseManagement extends JFrame {
	private static FrameSensorActuatorResponseManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class SensorActuatorResponseTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,String.class,String.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class SensorActuatorResponseTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","On Trigger","On Not Trigger","Enabled","Timeout"};
		
		public SensorActuatorResponseTableModel (SensorActuatorResponseTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((SensorActuatorResponseTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((SensorActuatorResponseTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((SensorActuatorResponseTableRow)arg0).getSubRowIndex((SensorActuatorResponseTableRow)arg1);
		}

		@Override
	    public String getColumnName(int column) {
	        return COLUMNS[column];
	    }

		@Override
		public Object getValueAt(Object arg0, int arg1) {
			return arg0;
		}

	}
	
	private static class SensorActuatorResponseTableRow {
		private LinkedList<SensorActuatorResponseTableRow> subRow;
		private HashMap<Sensoractuatorresponse,SensorActuatorResponseTableRow> rowObj;
		public String [] renderText;
		private Sensoractuatorresponse obj;
		
		public SensorActuatorResponseTableRow(Sensoractuatorresponse res) {
			if (res!=null) {
				obj=res;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				this.subRow=new LinkedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void updateInfo () {
			renderText=new String [] {obj.getActuator().getName(),obj.getOntriggeraction(),
									  obj.getOnnottriggeraction(),String.valueOf(obj.getEnabled()),""};
			if (obj.getTimeout()>=60) renderText[4]=String.valueOf(obj.getTimeout()/60)+"m";
			else renderText[4]=String.valueOf(obj.getTimeout())+"s";
		}
		public void addRow (SensorActuatorResponseTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void removeRowByResponse (Sensoractuatorresponse res) {
			if (this.rowObj.containsKey(res)) {
				this.subRow.remove(this.rowObj.get(res));
				this.rowObj.remove(res);
			}
		}
		
		public int getSubRowIndex (SensorActuatorResponseTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public SensorActuatorResponseTableRow getSubRow (int index) {
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
	
	private static class ActuatorTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			SensorActuatorResponseTableRow row=(SensorActuatorResponseTableRow)value;
			
			if (row!=null && row.renderText!=null) {
				int index=ArrayUtils.indexOf(SensorActuatorResponseTableModel.COLUMNS,aTable.getColumnName(aColumn));
				if (!aTable.getColumnName(aColumn).equals("Enabled"))
					setText(row.renderText[index].toString());
				else
					if (row.renderText[index].equals("true"))
						setIcon(Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/TICK.png")), 16, 16));
					else
						setIcon(Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/DELETE.png")), 16, 16));
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
	
	public static FrameSensorActuatorResponseManagement getInstance() {
		if (FrameSensorActuatorResponseManagement.currInstance==null) {
			FrameSensorActuatorResponseManagement.currInstance=new FrameSensorActuatorResponseManagement();
			FrameSensorActuatorResponseManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameSensorActuatorResponseManagement.currInstance.toFront();
			FrameSensorActuatorResponseManagement.currInstance.repaint();
		}
		return FrameSensorActuatorResponseManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameSensorActuatorResponseManagement.currInstance!=null) {
			FrameSensorActuatorResponseManagement.currInstance.updateActuatorTable();
			//FrameActuatorManagement.currInstance.table.repaint();
		}
	}
	
	private JPanel contentPane;
	private SensorActuatorResponseTable table;
	private SensorActuatorResponseTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameSensorActuatorResponseManagement() {
		setTitle("Sensor Actuator Response");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameSensorActuatorResponseManagement.currInstance=null;}
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
		table = new SensorActuatorResponseTable();
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("ActuatorIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		FrameSensorActuatorResponseManagementContextMenu popup=new FrameSensorActuatorResponseManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameSensorActuatorResponseManagementActions.edit(FrameSensorActuatorResponseManagement.this);
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
		
		rootRow=new SensorActuatorResponseTableRow(null);
		updateActuatorTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameSensorActuatorResponseManagementActions.add();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameSensorActuatorResponseManagementActions.edit(FrameSensorActuatorResponseManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameSensorActuatorResponseManagementActions.delete(FrameSensorActuatorResponseManagement.this);
						break;
					}
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
			
		});
		
		if (table.getColumnModel().getColumnCount()!=0) {
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(54);
			table.getColumnModel().getColumn(2).setPreferredWidth(54);
			table.getColumnModel().getColumn(1).setPreferredWidth(54);
			table.getColumnModel().getColumn(2).setPreferredWidth(54);
		}
		

	}
	
	public void updateActuatorTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<Sensoractuatorresponse> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.SensorActuatorResponses.map.values());
		for (Sensoractuatorresponse res : existing) {
			rootRow.rowObj.get(res).updateInfo();
		}
		
		boolean flag=false;
		HashSet<Sensoractuatorresponse> removedRes=new HashSet<>();
		removedRes.addAll(rootRow.rowObj.keySet());
		removedRes.removeAll(Cache.SensorActuatorResponses.map.values());
		
		for (Sensoractuatorresponse res : removedRes) {
			flag=true;
			rootRow.removeRowByResponse(res);
		}
		
		HashSet<Sensoractuatorresponse> addedRes=new HashSet<>();
		addedRes.addAll(Cache.SensorActuatorResponses.map.values());
		addedRes.removeAll(rootRow.rowObj.keySet());
		
		for (Sensoractuatorresponse res : addedRes) {
			flag=true;
			rootRow.addRow(new SensorActuatorResponseTableRow(res));
		}
		
		if (flag) {
			table.setTreeTableModel(new SensorActuatorResponseTableModel(rootRow));
			
			table.getColumn(0).setCellRenderer(new ActuatorTableCellRenderer());
			table.getColumn(1).setCellRenderer(new ActuatorTableCellRenderer());
			table.getColumn(2).setCellRenderer(new ActuatorTableCellRenderer());
			table.getColumn(3).setCellRenderer(new ActuatorTableCellRenderer());
			table.getColumn(4).setCellRenderer(new ActuatorTableCellRenderer());
		}
		
		selectedRow=Math.min(selectedRow,rootRow.rowObj.size()-1);
		if (selectedRow!=-1) {
			table.addRowSelectionInterval(table.convertRowIndexToView(selectedRow),table.convertRowIndexToView(selectedRow));
		}
		
		for (int i=0;i<width.length;i++) {
			table.getColumnModel().getColumn(i).setWidth(width[i]);
		}
		
		this.table.repaint();
	}

	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Sensoractuatorresponse getSelectedResponse () {
		return ((SensorActuatorResponseTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public Sensoractuatorresponse[] getSelectedResponses () {
		int [] selected=this.getSelectedRows();
		Sensoractuatorresponse [] sars=new Sensoractuatorresponse[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) sars[i]=((SensorActuatorResponseTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return sars;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
