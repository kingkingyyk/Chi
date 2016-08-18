package Chi;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
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
		
		public UserTableRow(Object [] o) {
			if (o!=null) {
				renderText=new String[o.length];
				renderText[0]=(String)o[0];
				renderText[1]=(String)o[1];
				renderText[2]=String.valueOf(o[2]);
				renderText[3]=(String)o[3];;
				renderText[4]=FrameUserManagement.fomatter.format((Date)o[4]);
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
	
	private JPanel contentPane;
	private UserTable table;
	private UserTableRow rootRow;
	private ArrayList<Object []> list=new ArrayList<>();
	public HashSet<String> usernameDB=new HashSet<>();
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
		WaitUI u=new WaitUI();
		u.setText("Populating user database");
		updateSuccess=false;
		usernameDB=new HashSet<>();
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseUser.getUsers(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)));
				if (rs!=null) {
					rootRow=new UserTableRow(null);
					list.clear();
					for (Row r : rs) {
						usernameDB.add(r.getString(0));
						Object [] o={r.getString(0),r.getString(1),r.getInt(2),r.getString(3),r.getTimestamp(4)};
						UserTableRow utr=new UserTableRow(o);
						rootRow.addRow(utr);
						list.add(o);
					}
					createTable();
					table.setTreeTableModel(new UserTableModel(rootRow));
					updateSuccess=true;
				} else {
					Logger.log("FrameUserManagement updateUserTable - Error reading user database");
					JOptionPane.showMessageDialog(null,"Error reading user database!",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		
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
	
	public Object [] getSelectedObj () {
		return this.list.get(this.table.getSelectedRow());
	}
}
