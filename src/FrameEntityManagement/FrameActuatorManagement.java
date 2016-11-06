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
import Entity.Actuator;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameActuatorManagement extends JFrame {
	private static FrameActuatorManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class ActuatorTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,String.class,String.class,String.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class ActuatorTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Attached On","Status","Position","Control Type","Actions"};
		
		public ActuatorTableModel (ActuatorTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((ActuatorTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((ActuatorTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((ActuatorTableRow)arg0).getSubRowIndex((ActuatorTableRow)arg1);
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
	
	private static class ActuatorTableRow {
		private LinkedList<ActuatorTableRow> subRow;
		private HashMap<Actuator,ActuatorTableRow> rowObj;
		public String [] renderText;
		private Actuator obj;
		
		public ActuatorTableRow(Actuator act) {
			if (act!=null) {
				obj=act;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				this.subRow=new LinkedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void updateInfo () {
			renderText=new String [] {obj.getName(), obj.getController().getControllername(), obj.getStatus(),String.format("%.2f,%.2f",obj.getPositionx(),obj.getPositiony()),obj.getControltype(),obj.getStatuslist()};
		}
		public void addRow (ActuatorTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void removeRowByActuator (Actuator act) {
			if (this.rowObj.containsKey(act)) {
				this.subRow.remove(this.rowObj.get(act));
				this.rowObj.remove(act);
			}
		}
		
		public int getSubRowIndex (ActuatorTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public ActuatorTableRow getSubRow (int index) {
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
			ActuatorTableRow row=(ActuatorTableRow)value;
			
			if (row!=null && row.renderText!=null) {
				setText(row.renderText[ArrayUtils.indexOf(ActuatorTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString());
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
	
	public static FrameActuatorManagement getInstance() {
		if (FrameActuatorManagement.currInstance==null) {
			FrameActuatorManagement.currInstance=new FrameActuatorManagement();
			FrameActuatorManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameActuatorManagement.currInstance.toFront();
			FrameActuatorManagement.currInstance.repaint();
		}
		return FrameActuatorManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameActuatorManagement.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					FrameActuatorManagement.currInstance.updateActuatorTable();
				}
			});
		}
	}
	
	private JPanel contentPane;
	private ActuatorTable table;
	private ActuatorTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameActuatorManagement() {
		setTitle("Actuator Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameActuatorManagement.currInstance=null;}
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
		table = new ActuatorTable();
		table.setEditable(false);
		table.setSortable(true);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("ActuatorIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		FrameActuatorManagementContextMenu popup=new FrameActuatorManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameActuatorManagementActions.edit(FrameActuatorManagement.this);
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
		
		rootRow=new ActuatorTableRow(null);
		updateActuatorTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameActuatorManagementActions.add();
						break;
					}
					case (KeyEvent.VK_SPACE) : {
						if (getSelectedCount()==1) FrameActuatorManagementActions.toggle(FrameActuatorManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameActuatorManagementActions.edit(FrameActuatorManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameActuatorManagementActions.delete(FrameActuatorManagement.this);
						break;
					}
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
			
		});
		
		if (table.getColumnModel().getColumnCount()!=0) {
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(80);
			table.getColumnModel().getColumn(3).setPreferredWidth(50);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
		}
	}
	
	public void updateActuatorTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<Actuator> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.Actuators.map.values());
		for (Actuator act : existing) {
			rootRow.rowObj.get(act).updateInfo();
		}
		
		boolean flag=false;
		HashSet<Actuator> removedAct=new HashSet<>();
		removedAct.addAll(rootRow.rowObj.keySet());
		removedAct.removeAll(Cache.Actuators.map.values());
		
		for (Actuator act : removedAct) {
			flag=true;
			rootRow.removeRowByActuator(act);
		}
		
		HashSet<Actuator> addedAct=new HashSet<>();
		addedAct.addAll(Cache.Actuators.map.values());
		addedAct.removeAll(rootRow.rowObj.keySet());
		
		for (Actuator act : addedAct) {
			flag=true;
			rootRow.addRow(new ActuatorTableRow(act));
		}
		
		if (flag) {
			table.setTreeTableModel(new ActuatorTableModel(rootRow));
			
			for (int i=0;i<table.getColumnCount();i++) 	table.getColumn(i).setCellRenderer(new ActuatorTableCellRenderer());
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
	
	public Actuator getSelectedActuator () {
		return ((ActuatorTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public Actuator[] getSelectedActuators () {
		int [] selected=this.getSelectedRows();
		Actuator [] act=new Actuator[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) act[i]=((ActuatorTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return act;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
