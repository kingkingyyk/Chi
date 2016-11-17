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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import Chi.Theme;
import Chi.Utility;
import DataStructures.MinMaxSortedList;
import Database.Cache;
import Entity.User;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameUserManagement extends JFrame {
	private static FrameUserManagement currInstance;
	private static SimpleDateFormat fomatter=new SimpleDateFormat("yyyy MMMM dd KK:mm:ss aa");
	private static final long serialVersionUID = 1L;

	private static class UserTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Integer.class,String.class,Date.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class UserTableModel extends AbstractTreeTableModel {
		private final static String [] COLUMNS= {"Username","Password (Hash)","Level","Status","Time Added"};
		
		public UserTableModel (UserTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((UserTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((UserTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((UserTableRow)arg0).getSubRowIndex((UserTableRow)arg1);
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
	
	private static class UserTableRow implements Comparable<UserTableRow> {
		private MinMaxSortedList<UserTableRow> subRow;
		private HashMap<User,UserTableRow> rowObj;
		public String [] renderText;
		private User obj;
		
		public UserTableRow(User u) {
			if (u!=null) {
				renderText=new String[5];
				obj=u;
				updateInfo();
			} else {
				renderText=new String [] {"root"};
				this.subRow=new MinMaxSortedList<>();
				this.rowObj=new HashMap<>();
			}
		}
		
		public void addRow (UserTableRow r) {
			this.subRow.add(r);
			this.rowObj.put(r.obj,r);
		}
		
		public void updateInfo() {
			renderText[0]=obj.getUsername();
			renderText[1]=obj.getPassword();
			renderText[2]=obj.getLevel().toString();
			renderText[3]=obj.getStatus();
			renderText[4]=FrameUserManagement.fomatter.format(obj.getDateadded());
		}
		
		public void removeRowByUser (User u) {
			if (this.rowObj.containsKey(u)) {
				this.subRow.remove(this.rowObj.get(u));
				this.rowObj.remove(u);
			}
		}
		
		public int getSubRowIndex (UserTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public UserTableRow getSubRow (int index) {
			return this.subRow.get(index);
		}
		
		public int subRowCount () {
			if (this.subRow==null) return 0;
			else return this.subRow.size();
		}
		
		public String toString() {
			return this.renderText[0];
		}
		
		public int compareTo(UserTableRow r) {
			return this.obj.compareTo(r.obj);
		}
	}
	
	private static class UserTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			UserTableRow row=(UserTableRow)value;
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
	
	public static FrameUserManagement getInstance() {
		if (FrameUserManagement.currInstance==null) {
			FrameUserManagement.currInstance=new FrameUserManagement();
			FrameUserManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameUserManagement.currInstance.toFront();
			FrameUserManagement.currInstance.repaint();
		}
		return FrameUserManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameUserManagement.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameUserManagement.currInstance.updateUserTable();
				}
			});
		}
	}
	private JPanel contentPane;
	private UserTable table;
	private UserTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameUserManagement() {
		setTitle("User Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameUserManagement.currInstance=null;}
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
		table = new UserTable();
		//table.setRootVisible(false);
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("UserIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		table.getTableHeader().setReorderingAllowed(false);
		FrameUserManagementContextMenu popup=new FrameUserManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameUserManagementActions.edit(FrameUserManagement.this);
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
		
		rootRow=new UserTableRow(null);
		updateUserTable();
		table.setAutoCreateRowSorter(true);
		
		table.addKeyListener(new KeyListener() {

			@Override public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameUserManagementActions.add();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameUserManagementActions.edit(FrameUserManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameUserManagementActions.delete(FrameUserManagement.this);
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
			table.getColumnModel().getColumn(2).setPreferredWidth(20);
			table.getColumnModel().getColumn(3).setPreferredWidth(54);
			table.getColumnModel().getColumn(4).setPreferredWidth(112);
		}
	}
	
	public void updateUserTable() {
		int selectedRow=this.getSelectedRow();
		int [] width=new int [table.getColumnModel().getColumnCount()];
		for (int i=0;i<width.length;i++) {
			width[i]=table.getColumnModel().getColumn(i).getWidth();
		}
		
		HashSet<User> existing=new HashSet<>();
		existing.addAll(rootRow.rowObj.keySet());
		existing.retainAll(Cache.Users.map.values());
		for (User act : existing) {
			rootRow.rowObj.get(act).updateInfo();
		}
		
		boolean flag=false;
		HashSet<User> removedAct=new HashSet<>();
		removedAct.addAll(rootRow.rowObj.keySet());
		removedAct.removeAll(Cache.Users.map.values());
		
		for (User act : removedAct) {
			flag=true;
			rootRow.removeRowByUser(act);
		}
		
		HashSet<User> addedAct=new HashSet<>();
		addedAct.addAll(Cache.Users.map.values());
		addedAct.removeAll(rootRow.rowObj.keySet());
		
		for (User act : addedAct) {
			flag=true;
			rootRow.addRow(new UserTableRow(act));
		}
		
		if (flag) {
			table.setTreeTableModel(new UserTableModel(rootRow));
			
			for (int i=0;i<table.getColumnCount();i++) 	table.getColumn(i).setCellRenderer(new UserTableCellRenderer());
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
	
	public User getSelectedUser () {
		return ((UserTableRow)table.getTreeTableModel().getRoot()).subRow.get(this.getSelectedRow()).obj;
	}
	
	public int [] getSelectedRows () {
		int [] selected=this.table.getSelectedRows();
		int [] converted=new int [selected.length];
		for (int i=0;i<selected.length;i++) converted[i]=this.table.convertRowIndexToModel(selected[i]);
		return converted;
	}
	
	public User[] getSelectedUsers () {
		int [] selected=this.getSelectedRows();
		User [] u=new User[this.table.getSelectedRowCount()];
		for (int i=0;i<selected.length;i++) u[i]=((UserTableRow)table.getTreeTableModel().getRoot()).subRow.get(selected[i]).obj;
		return u;
	}
	
	public int getSelectedCount() {
		return this.table.getSelectedRowCount();
	}
}
