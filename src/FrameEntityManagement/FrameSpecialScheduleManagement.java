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
import DataStructures.MinMaxSortedList;
import Database.Cache;
import Entity.Specialschedule;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FrameSpecialScheduleManagement extends JFrame {
	private static FrameSpecialScheduleManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class SpecialScheduleTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Integer.class,Integer.class,Integer.class,String.class,String.class,String.class,Boolean.class,Integer.class,Boolean.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class SpecialScheduleTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Actuator","Year","Month","Day","Time Rule","OnStart","OnEnd","Lock","Priority","Enabled"};
		
		public SpecialScheduleTableModel (SpecialScheduleTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((SpecialScheduleTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((SpecialScheduleTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((SpecialScheduleTableRow)arg0).getSubRowIndex((SpecialScheduleTableRow)arg1);
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
	
	private static class SpecialScheduleTableRow implements Comparable<SpecialScheduleTableRow> {
		private MinMaxSortedList<SpecialScheduleTableRow> subRow;
		private HashMap<Specialschedule,SpecialScheduleTableRow> rowObj;
		public String [] renderText;
		private Specialschedule obj;
		
		public SpecialScheduleTableRow(Specialschedule s) {
			if (s!=null) {
				renderText=new String[SpecialScheduleTableModel.COLUMNS.length];
				obj=s;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				this.subRow=new MinMaxSortedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void addRow (SpecialScheduleTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void updateInfo() {
			renderText[0]=obj.getSchedulename();
			renderText[1]=obj.getActuator().getName();
			renderText[2]=obj.getYear().toString();
			renderText[3]=obj.getMonth().toString();
			renderText[4]=obj.getDay().toString();
			renderText[5]=obj.getDayschedulerule().getRulename();
			renderText[6]=obj.getOnstartaction();
			renderText[7]=obj.getOnendaction();
			renderText[8]=String.valueOf(obj.getLockmanual());
			renderText[9]=obj.getPriority().toString();
			renderText[10]=obj.getEnabled().toString();
		}
		
		public void removeRowBySpecialSchedule (Specialschedule ss) {
			if (this.rowObj.containsKey(ss)) {
				this.subRow.remove(this.rowObj.get(ss));
				this.rowObj.remove(ss);
			}
		}
		
		public int getSubRowIndex (SpecialScheduleTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public SpecialScheduleTableRow getSubRow (int index) {
			return this.subRow.get(index);
		}
		
		public int subRowCount () {
			if (this.subRow==null) return 0;
			else return this.subRow.size();
		}
		
		public String toString() {
			return this.renderText[0];
		}
		
		public int compareTo(SpecialScheduleTableRow r) {
			return this.obj.compareTo(r.obj);
		}
	}
	
	private static class SpecialScheduleTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			SpecialScheduleTableRow row=(SpecialScheduleTableRow)value;
			if (row!=null && row.renderText!=null) {
				int index=ArrayUtils.indexOf(SpecialScheduleTableModel.COLUMNS,aTable.getColumnName(aColumn));
				if (!aTable.getColumnName(aColumn).equals("Enabled") && !aTable.getColumnName(aColumn).equals("Lock") )
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
	
	public static FrameSpecialScheduleManagement getInstance() {
		if (FrameSpecialScheduleManagement.currInstance==null) {
			FrameSpecialScheduleManagement.currInstance=new FrameSpecialScheduleManagement();
			FrameSpecialScheduleManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameSpecialScheduleManagement.currInstance.toFront();
			FrameSpecialScheduleManagement.currInstance.repaint();
		}
		return FrameSpecialScheduleManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameSpecialScheduleManagement.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameSpecialScheduleManagement.currInstance.updateSpecialScheduleTable();
				}
			});
		}
	}
	
	private JPanel contentPane;
	private SpecialScheduleTable table;
	private SpecialScheduleTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameSpecialScheduleManagement() {
		setTitle("Special Schedule");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameSpecialScheduleManagement.currInstance=null;}
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
		table = new SpecialScheduleTable();
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("SpecialScheduleIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		FrameSpecialScheduleManagementContextMenu popup=new FrameSpecialScheduleManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameSpecialScheduleManagementActions.edit(FrameSpecialScheduleManagement.this);
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
		
		rootRow=new SpecialScheduleTableRow(null);
		updateSpecialScheduleTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameSpecialScheduleManagementActions.add();
						break;
					}
					case (KeyEvent.VK_SPACE) : {
						FrameSpecialScheduleManagementActions.toggle(FrameSpecialScheduleManagement.this);
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameSpecialScheduleManagementActions.edit(FrameSpecialScheduleManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameSpecialScheduleManagementActions.delete(FrameSpecialScheduleManagement.this);
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
			table.getColumnModel().getColumn(2).setPreferredWidth(30);
			table.getColumnModel().getColumn(3).setPreferredWidth(30);
			table.getColumnModel().getColumn(4).setPreferredWidth(30);
			table.getColumnModel().getColumn(5).setPreferredWidth(54);
			table.getColumnModel().getColumn(6).setPreferredWidth(54);
			table.getColumnModel().getColumn(7).setPreferredWidth(54);
			table.getColumnModel().getColumn(8).setPreferredWidth(54);
			table.getColumnModel().getColumn(9).setPreferredWidth(54);
			table.getColumnModel().getColumn(10).setPreferredWidth(54);
		}
	}
	
	public void updateSpecialScheduleTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<Specialschedule> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.SpecialSchedules.map.values());
		for (Specialschedule act : existing) {
			rootRow.rowObj.get(act).updateInfo();
		}
		
		boolean flag=false;
		HashSet<Specialschedule> removedAct=new HashSet<>();
		removedAct.addAll(rootRow.rowObj.keySet());
		removedAct.removeAll(Cache.SpecialSchedules.map.values());
		
		for (Specialschedule act : removedAct) {
			flag=true;
			rootRow.removeRowBySpecialSchedule(act);
		}
		
		HashSet<Specialschedule> addedAct=new HashSet<>();
		addedAct.addAll(Cache.SpecialSchedules.map.values());
		addedAct.removeAll(rootRow.rowObj.keySet());
		
		for (Specialschedule act : addedAct) {
			flag=true;
			rootRow.addRow(new SpecialScheduleTableRow(act));
		}
		
		if (flag) {
			table.setTreeTableModel(new SpecialScheduleTableModel(rootRow));
			
			for (int i=0;i<table.getColumnCount();i++) 	table.getColumn(i).setCellRenderer(new SpecialScheduleTableCellRenderer());
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
	
	public Specialschedule getSelectedSchedule () {
		return ((SpecialScheduleTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public Specialschedule[] getSelectedSchedules () {
		int [] selected=this.getSelectedRows();
		Specialschedule [] ss=new Specialschedule[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) ss[i]=((SpecialScheduleTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return ss;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
