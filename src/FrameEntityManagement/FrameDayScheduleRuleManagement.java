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
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import Chi.Theme;
import Chi.Utility;
import Database.Cache;
import Entity.Dayschedulerule;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameDayScheduleRuleManagement extends JFrame {
	private static FrameDayScheduleRuleManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class DayScheduleRuleTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class DayScheduleRuleTableModel extends AbstractTreeTableModel {
		private final static String [] COLUMNS= {"Name","Start Time","End Time"};
		
		public DayScheduleRuleTableModel (DayScheduleRuleTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((DayScheduleRuleTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((DayScheduleRuleTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((DayScheduleRuleTableRow)arg0).getSubRowIndex((DayScheduleRuleTableRow)arg1);
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
	
	private static class DayScheduleRuleTableRow {
		private LinkedList<DayScheduleRuleTableRow> subRow;
		private HashMap<Dayschedulerule,DayScheduleRuleTableRow> rowObj;
		public String [] renderText;
		private Dayschedulerule obj;
		
		public DayScheduleRuleTableRow(Dayschedulerule r) {
			if (r!=null) {
				renderText=new String[3];
				obj=r;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				this.subRow=new LinkedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void addRow (DayScheduleRuleTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void updateInfo () {
			renderText[0]=obj.getRulename();

			String [] t=Utility.formatTime(obj.getStarthour(),obj.getStartminute());
			renderText[1]=t[0]+":"+t[1]+" "+t[2];
			t=Utility.formatTime(obj.getEndhour(),obj.getEndminute());
			renderText[2]=t[0]+":"+t[1]+" "+t[2];
		}
		
		public void removeRowByDayScheduleRule (Dayschedulerule rule) {
			if (this.rowObj.containsKey(rule)) {
				this.subRow.remove(this.rowObj.get(rule));
				this.rowObj.remove(rule);
			}
		}
		
		public int getSubRowIndex (DayScheduleRuleTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public DayScheduleRuleTableRow getSubRow (int index) {
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
	
	private static class DayScheduleRuleTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			DayScheduleRuleTableRow row=(DayScheduleRuleTableRow)value;
			if (row!=null && row.renderText!=null) {
				setText(row.renderText[aColumn].toString());
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
	
	public static FrameDayScheduleRuleManagement getInstance() {
		if (FrameDayScheduleRuleManagement.currInstance==null) {
			FrameDayScheduleRuleManagement.currInstance=new FrameDayScheduleRuleManagement();
			FrameDayScheduleRuleManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameDayScheduleRuleManagement.currInstance.toFront();
			FrameDayScheduleRuleManagement.currInstance.repaint();
		}
		return FrameDayScheduleRuleManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameDayScheduleRuleManagement.currInstance!=null) {
			FrameDayScheduleRuleManagement.currInstance.updateDayScheduleRuleTable();
			FrameDayScheduleRuleManagement.currInstance.table.repaint();
		}
	}
	
	private JPanel contentPane;
	private DayScheduleRuleTable table;
	private DayScheduleRuleTableRow rootRow;
	private JScrollPane scrollPane;

	public FrameDayScheduleRuleManagement() {
		setTitle("Day Schedule Rule Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameDayScheduleRuleManagement.currInstance=null;}
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
		table = new DayScheduleRuleTable();
		table.setRootVisible(false);
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("DayScheduleRuleIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		table.getTableHeader().setReorderingAllowed(false);
		FrameDayScheduleRuleManagementContextMenu popup=new FrameDayScheduleRuleManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameDayScheduleRuleManagementActions.edit(FrameDayScheduleRuleManagement.this);
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
		
		rootRow=new DayScheduleRuleTableRow(null);
		updateDayScheduleRuleTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameDayScheduleRuleManagementActions.add();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameDayScheduleRuleManagementActions.edit(FrameDayScheduleRuleManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameDayScheduleRuleManagementActions.delete(FrameDayScheduleRuleManagement.this);
						break;
					}
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
			
		});
		
		if (table.getColumnModel().getColumnCount()!=0) {
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(30);
			table.getColumnModel().getColumn(2).setPreferredWidth(30);
		}
	}
	
	public void updateDayScheduleRuleTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<Dayschedulerule> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.DayScheduleRules.map.values());
		for (Dayschedulerule act : existing) {
			rootRow.rowObj.get(act).updateInfo();
		}
		
		boolean flag=false;
		HashSet<Dayschedulerule> removedAct=new HashSet<>();
		removedAct.addAll(rootRow.rowObj.keySet());
		removedAct.removeAll(Cache.DayScheduleRules.map.values());
		
		for (Dayschedulerule act : removedAct) {
			flag=true;
			rootRow.removeRowByDayScheduleRule(act);
		}
		
		HashSet<Dayschedulerule> addedAct=new HashSet<>();
		addedAct.addAll(Cache.DayScheduleRules.map.values());
		addedAct.removeAll(rootRow.rowObj.keySet());
		
		for (Dayschedulerule act : addedAct) {
			flag=true;
			rootRow.addRow(new DayScheduleRuleTableRow(act));
		}
		
		if (flag) {
			table.setTreeTableModel(new DayScheduleRuleTableModel(rootRow));
			
			for (int i=0;i<table.getColumnCount();i++) 	table.getColumn(i).setCellRenderer(new DayScheduleRuleTableCellRenderer());
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
	
	public Dayschedulerule getSelectedRule () {
		return ((DayScheduleRuleTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public Dayschedulerule[] getSelectedRules () {
		int [] selected=this.getSelectedRows();
		Dayschedulerule [] dsr=new Dayschedulerule[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) dsr[i]=((DayScheduleRuleTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return dsr;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
