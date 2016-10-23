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
import java.text.SimpleDateFormat;
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
import Entity.Controller;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameControllerManagement extends JFrame {
	private static FrameControllerManagement currInstance;
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy MMMM dd KK:mm:ss aa");
	
	private static class ControllerTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,String.class,String.class,String.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class ControllerTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Site","IP","Position","Report Period","Last Report Time"};
		
		public ControllerTableModel (ControllerTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((ControllerTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((ControllerTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((ControllerTableRow)arg0).getSubRowIndex((ControllerTableRow)arg1);
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
	
	private static class ControllerTableRow {
		private LinkedList<ControllerTableRow> subRow;
		private HashMap<Controller,ControllerTableRow> rowObj;
		public String [] renderText;
		private Controller obj;
		
		public ControllerTableRow(Controller ctrl) {
			if (ctrl!=null) {
				renderText=new String[6];
				obj=ctrl;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				this.subRow=new LinkedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void addRow (ControllerTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void updateInfo () {
			renderText[0]=obj.getControllername();
			renderText[1]=obj.getSite().getSitename();
			renderText[2]=obj.getIpaddress();
			renderText[3]=String.format("%.2f",obj.getPositionx())+","+String.format("%.2f",obj.getPositiony()); //position Y
			
			int period=obj.getReporttimeout();
			StringBuilder sb=new StringBuilder();
			if (period>=3600) {
				sb.append(period/3600);
				sb.append("h ");
				period%=3600;
			}
			if (period>=60) {
				sb.append(period/60);
				sb.append("m ");
				period%=60;
			}
			if (period>0) {
				sb.append(period);
				sb.append("s");
			}
			renderText[4]=sb.toString(); // report period.
			renderText[5]=dateFormatter.format(obj.getLastreporttime()); //last report time
		}
		
		public void removeRowByController (Controller ctrl) {
			if (this.rowObj.containsKey(ctrl)) {
				this.subRow.remove(this.rowObj.get(ctrl));
				this.rowObj.remove(ctrl);
			}
		}
		
		public int getSubRowIndex (ControllerTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public ControllerTableRow getSubRow (int index) {
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
	
	private static class ControllerTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			ControllerTableRow row=(ControllerTableRow)value;
			if (row!=null && row.renderText!=null) {
				setText(row.renderText[ArrayUtils.indexOf(ControllerTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString());
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
	
	public static FrameControllerManagement getInstance() {
		if (FrameControllerManagement.currInstance==null) {
			FrameControllerManagement.currInstance=new FrameControllerManagement();
			FrameControllerManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameControllerManagement.currInstance.repaint();
		}
		FrameControllerManagement.currInstance.toFront();
		return FrameControllerManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameControllerManagement.currInstance!=null) {
			FrameControllerManagement.currInstance.updateControllerTable();
			FrameControllerManagement.currInstance.table.repaint();
		}
	}
	
	private JPanel contentPane;
	private ControllerTable table;
	private ControllerTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameControllerManagement() {
		setTitle("Controller Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameControllerManagement.currInstance=null;}
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
		table = new ControllerTable();
		table.setEditable(false);
		table.setSortable(true);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("ControllerIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		FrameControllerManagementContextMenu popup=new FrameControllerManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameControllerManagementActions.edit(FrameControllerManagement.this);
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
		
		rootRow=new ControllerTableRow(null);
		updateControllerTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameControllerManagementActions.add();
						break;
					}
					case (KeyEvent.VK_SPACE) : {
						if (getSelectedCount()==1) FrameControllerManagementActions.forceReport(FrameControllerManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameControllerManagementActions.edit(FrameControllerManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameControllerManagementActions.delete(FrameControllerManagement.this);
						break;
					}
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
			
		});
		
		if (table.getColumnModel().getColumnCount()!=0) {
			table.getColumnModel().getColumn(0).setPreferredWidth(160);
			table.getColumnModel().getColumn(1).setPreferredWidth(70);
			table.getColumnModel().getColumn(2).setPreferredWidth(70);
			table.getColumnModel().getColumn(3).setPreferredWidth(30);
			table.getColumnModel().getColumn(4).setPreferredWidth(60);
			table.getColumnModel().getColumn(5).setPreferredWidth(133);
		}
	}
	
	public void updateControllerTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<Controller> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.Controllers.map.values());
		for (Controller act : existing) {
			rootRow.rowObj.get(act).updateInfo();
		}
		
		boolean flag=false;
		HashSet<Controller> removedAct=new HashSet<>();
		removedAct.addAll(rootRow.rowObj.keySet());
		removedAct.removeAll(Cache.Controllers.map.values());
		
		for (Controller act : removedAct) {
			flag=true;
			rootRow.removeRowByController(act);
		}
		
		HashSet<Controller> addedAct=new HashSet<>();
		addedAct.addAll(Cache.Controllers.map.values());
		addedAct.removeAll(rootRow.rowObj.keySet());
		
		for (Controller act : addedAct) {
			flag=true;
			rootRow.addRow(new ControllerTableRow(act));
		}
		
		if (flag) {
			table.setTreeTableModel(new ControllerTableModel(rootRow));
			
			for (int i=0;i<table.getColumnCount();i++) 	table.getColumn(i).setCellRenderer(new ControllerTableCellRenderer());
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
	
	public Controller getSelectedController () {
		return ((ControllerTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public Controller[] getSelectedControllers () {
		int [] selected=this.getSelectedRows();
		Controller [] ctrl=new Controller[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) ctrl[i]=((ControllerTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return ctrl;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
