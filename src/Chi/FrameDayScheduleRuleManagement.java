package Chi;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
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
		private static String [] Hours={"12","01","02","03","04","05","06","07","08","09","10","11"};
		private static String [] Minutes; //generated dynamically, too long :P
		private static String [] AMPM={"AM","PM"};
		private ArrayList<DayScheduleRuleTableRow> subRow;
		public String [] renderText;
		
		public DayScheduleRuleTableRow(Object [] o) {
			if (Minutes==null) {
				Minutes=new String [60];
				for (int i=0;i<60;i++) {
					if (i<10) {
						StringBuilder sb=new StringBuilder();
						sb.append("0");
						sb.append(i);
						Minutes[i]=sb.toString();
					} else {
						Minutes[i]=String.valueOf(i);
					}
				}
			}
			
			if (o!=null) {
				renderText=new String [3];
				renderText[0]=(String)o[0];
				renderText[1]=Hours[(Integer)o[1]%Hours.length]+":"+Minutes[(Integer)o[2]]+" "+AMPM[(Integer)o[1]/Hours.length];
				renderText[2]=Hours[(Integer)o[3]%Hours.length]+":"+Minutes[(Integer)o[4]]+" "+AMPM[(Integer)o[3]/Hours.length];
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (DayScheduleRuleTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
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
	
	private JPanel contentPane;
	private DayScheduleRuleTable table;
	private DayScheduleRuleTableRow rootRow;
	private ArrayList<Object []> list=new ArrayList<>();
	public HashSet<String> DayScheduleRulenameDB=new HashSet<>();
	public boolean updateSuccess;
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
		
		updateDayScheduleRuleTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		table = new DayScheduleRuleTable();
		table.setRootVisible(false);
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/DAY_SCHEDULE_RULE.png")), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		table.getTableHeader().setReorderingAllowed(false);
		table.setComponentPopupMenu(new FrameDayScheduleRuleManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateDayScheduleRuleTable() {
		WaitUI u=new WaitUI();
		u.setText("Populating DayScheduleRule list");
		updateSuccess=false;
		DayScheduleRulenameDB=new HashSet<>();
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseDayScheduleRule.getDayScheduleRules();
				if (rs!=null) {
					rootRow=new DayScheduleRuleTableRow(null);
					list.clear();
					try {
						while (rs.next()) {
							DayScheduleRulenameDB.add(rs.getString(1));
							Object [] o={rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5)};
							DayScheduleRuleTableRow utr=new DayScheduleRuleTableRow(o);
							rootRow.addRow(utr);
							list.add(o);
						}
					} catch (SQLException e) {e.printStackTrace();}
					int lastSelectedRow=-1;
					if (table!=null) {
						lastSelectedRow=table.getSelectedRow();
					}
					
					createTable();
					table.setTreeTableModel(new DayScheduleRuleTableModel(rootRow));
					
					if (lastSelectedRow>=0 && DayScheduleRulenameDB.size()>0) {
						lastSelectedRow=Math.min(lastSelectedRow,DayScheduleRulenameDB.size()-1);
						table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
					}
					updateSuccess=true;
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		
		if (updateSuccess) {
			table.getColumn(0).setCellRenderer(new DayScheduleRuleTableCellRenderer());
			table.getColumn(1).setCellRenderer(new DayScheduleRuleTableCellRenderer());
			table.getColumn(2).setCellRenderer(new DayScheduleRuleTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(30);
			table.getColumnModel().getColumn(2).setPreferredWidth(30);
		}
	}
	
	public int getSelectedRow () {
		return this.table.getSelectedRow();
	}
	
	public Object [] getSelectedObj () {
		return this.list.get(this.table.getSelectedRow());
	}
}
