package Chi;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;
import javax.swing.ImageIcon;

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
	
	private static class UserTableRow {
		private ArrayList<UserTableRow> subRow;
		public String [] renderText;
		
		public UserTableRow(User u) {
			if (u!=null) {
				renderText=new String[5];
				renderText[0]=u.getUsername();
				renderText[1]=u.getPassword();
				renderText[2]=u.getLevel().toString();
				renderText[3]=u.getStatus();
				renderText[4]=FrameUserManagement.fomatter.format(u.getDateadded());
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (UserTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
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
			FrameUserManagement.currInstance.updateUserTable();
			FrameUserManagement.currInstance.repaint();
		}
	}
	private JPanel contentPane;
	private UserTable table;
	private UserTableRow rootRow;
	private ArrayList<User> list=new ArrayList<>();
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
		
		updateUserTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		table = new UserTable();
		//table.setRootVisible(false);
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/USER.png")), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		table.getTableHeader().setReorderingAllowed(false);
		table.setComponentPopupMenu(new FrameUserManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateUserTable() {
		Cache.updateUser();
		rootRow=new UserTableRow(null);
		this.list.clear();
		this.list.addAll(Cache.userObj);
		
		for (User u : this.list) {
			UserTableRow utr=new UserTableRow(u);
			rootRow.addRow(utr);
		}
		
		int lastSelectedRow=-1;
		if (table!=null) {
			lastSelectedRow=table.getSelectedRow();
		}
		createTable();
		table.setAutoCreateRowSorter(true);
		table.setTreeTableModel(new UserTableModel(rootRow));
		
		if (lastSelectedRow>=0 && Cache.RegularScheduleList.size()>0) {
			lastSelectedRow=Math.min(lastSelectedRow,Cache.RegularScheduleList.size()-1);
			table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
		}
		updateSuccess=true;
		
		if (updateSuccess) {
			table.getColumn(0).setCellRenderer(new UserTableCellRenderer());
			table.getColumn(1).setCellRenderer(new UserTableCellRenderer());
			table.getColumn(2).setCellRenderer(new UserTableCellRenderer());
			table.getColumn(3).setCellRenderer(new UserTableCellRenderer());
			table.getColumn(4).setCellRenderer(new UserTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(133);
			table.getColumnModel().getColumn(2).setPreferredWidth(20);
			table.getColumnModel().getColumn(3).setPreferredWidth(54);
			table.getColumnModel().getColumn(4).setPreferredWidth(112);
		}
	}
	
	public int getSelectedRow () {
		return this.table.getSelectedRow();
	}
	
	public User getSelectedUser () {
		return this.list.get(this.table.getSelectedRow());
	}
}
