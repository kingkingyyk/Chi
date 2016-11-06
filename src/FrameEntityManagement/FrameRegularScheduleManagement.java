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
import Entity.Regularschedule;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class FrameRegularScheduleManagement extends JFrame {
	private static FrameRegularScheduleManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class RegularScheduleTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Integer.class,String.class,String.class,String.class,Boolean.class,Integer.class,Boolean.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class RegularScheduleTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Actuator","Days","Time Rule","OnStart","OnEnd","Lock","Priority","Enabled"};
		
		public RegularScheduleTableModel (RegularScheduleTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((RegularScheduleTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((RegularScheduleTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((RegularScheduleTableRow)arg0).getSubRowIndex((RegularScheduleTableRow)arg1);
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
	
	private static class RegularScheduleTableRow {
		private LinkedList<RegularScheduleTableRow> subRow;
		private HashMap<Regularschedule,RegularScheduleTableRow> rowObj;
		public String [] renderText;
		private Regularschedule obj;
		
		public RegularScheduleTableRow(Regularschedule r) {
			if (r!=null) {
				renderText=new String[9];
				obj=r;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				subRow=new LinkedList<>();
				rowObj=new HashMap<>();
			}
		}
		
		public void addRow (RegularScheduleTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void updateInfo() {
			renderText[0]=obj.getSchedulename();
			renderText[1]=obj.getActuator().getName();
			renderText[2]=Utility.dayMaskToStr(obj.getDaymask());
			renderText[3]=obj.getDayschedulerule().getRulename();
			renderText[4]=obj.getOnstartaction();
			renderText[5]=obj.getOnendaction();
			renderText[6]=String.valueOf(obj.getLockmanual());
			renderText[7]=obj.getPriority().toString();
			renderText[8]=obj.getEnabled().toString();
		}
		
		public void removeRowByRegularSchedule (Regularschedule rs) {
			if (this.rowObj.containsKey(rs)) {
				this.subRow.remove(this.rowObj.get(rs));
				this.rowObj.remove(rs);
			}
		}
		
		public int getSubRowIndex (RegularScheduleTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public RegularScheduleTableRow getSubRow (int index) {
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
	
	private static class RegularScheduleTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			RegularScheduleTableRow row=(RegularScheduleTableRow)value;
			if (row!=null && row.renderText!=null) {
				int index=ArrayUtils.indexOf(RegularScheduleTableModel.COLUMNS,aTable.getColumnName(aColumn));
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
	
	public static FrameRegularScheduleManagement getInstance() {
		if (FrameRegularScheduleManagement.currInstance==null) {
			FrameRegularScheduleManagement.currInstance=new FrameRegularScheduleManagement();
			FrameRegularScheduleManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameRegularScheduleManagement.currInstance.toFront();
			FrameRegularScheduleManagement.currInstance.repaint();
		}
		return FrameRegularScheduleManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameRegularScheduleManagement.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameRegularScheduleManagement.currInstance.updateRegularScheduleTable();
				}
			});
		}
	}
	
	private JPanel contentPane;
	private RegularScheduleTable table;
	private RegularScheduleTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameRegularScheduleManagement() {
		setTitle("Regular Schedule");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameRegularScheduleManagement.currInstance=null;}
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
		table = new RegularScheduleTable();
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("RegularScheduleIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		FrameRegularScheduleManagementContextMenu popup=new FrameRegularScheduleManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameRegularScheduleManagementActions.edit(FrameRegularScheduleManagement.this);
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
		
		rootRow=new RegularScheduleTableRow(null);
		updateRegularScheduleTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameRegularScheduleManagementActions.add();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameRegularScheduleManagementActions.edit(FrameRegularScheduleManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameRegularScheduleManagementActions.delete(FrameRegularScheduleManagement.this);
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
			table.getColumnModel().getColumn(2).setPreferredWidth(133);
			table.getColumnModel().getColumn(3).setPreferredWidth(54);
			table.getColumnModel().getColumn(4).setPreferredWidth(54);
			table.getColumnModel().getColumn(5).setPreferredWidth(54);
			table.getColumnModel().getColumn(6).setPreferredWidth(54);
			table.getColumnModel().getColumn(7).setPreferredWidth(54);
			table.getColumnModel().getColumn(8).setPreferredWidth(54);
		}
	}
	
	public void updateRegularScheduleTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<Regularschedule> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.RegularSchedules.map.values());
		for (Regularschedule act : existing) {
			rootRow.rowObj.get(act).updateInfo();
		}
		
		boolean flag=false;
		HashSet<Regularschedule> removedAct=new HashSet<>();
		removedAct.addAll(rootRow.rowObj.keySet());
		removedAct.removeAll(Cache.RegularSchedules.map.values());
		
		for (Regularschedule act : removedAct) {
			flag=true;
			rootRow.removeRowByRegularSchedule(act);
		}
		
		HashSet<Regularschedule> addedAct=new HashSet<>();
		addedAct.addAll(Cache.RegularSchedules.map.values());
		addedAct.removeAll(rootRow.rowObj.keySet());
		
		for (Regularschedule act : addedAct) {
			flag=true;
			rootRow.addRow(new RegularScheduleTableRow(act));
		}
		
		if (flag) {
			table.setTreeTableModel(new RegularScheduleTableModel(rootRow));
			
			for (int i=0;i<table.getColumnCount();i++) 	table.getColumn(i).setCellRenderer(new RegularScheduleTableCellRenderer());
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
	
	public Regularschedule getSelectedSchedule () {
		return ((RegularScheduleTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public Regularschedule[] getSelectedSchedules () {
		int [] selected=this.getSelectedRows();
		Regularschedule [] rs=new Regularschedule[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) rs[i]=((RegularScheduleTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return rs;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
