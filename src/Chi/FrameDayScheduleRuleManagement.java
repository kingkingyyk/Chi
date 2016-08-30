package Chi;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
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
		private ArrayList<DayScheduleRuleTableRow> subRow;
		public String [] renderText;
		
		public DayScheduleRuleTableRow(Dayschedulerule r) {
			if (r!=null) {
				renderText=new String [3];
				renderText[0]=r.getRulename();
				
				String [] t=Utility.formatTime(r.getStarthour(),r.getStartminute());
				renderText[1]=t[0]+":"+t[1]+" "+t[2];
				t=Utility.formatTime(r.getEndhour(),r.getEndminute());
				renderText[2]=t[0]+":"+t[1]+" "+t[2];
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
	
	public static void refresh() {
		if (FrameDayScheduleRuleManagement.currInstance!=null) {
			FrameDayScheduleRuleManagement.currInstance.updateDayScheduleRuleTable();
			FrameDayScheduleRuleManagement.currInstance.repaint();
		}
	}
	
	private JPanel contentPane;
	private DayScheduleRuleTable table;
	private DayScheduleRuleTableRow rootRow;
	private ArrayList<Dayschedulerule> list=new ArrayList<>();
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
		if (Cache.DayScheduleRules.updateWithWait()) {
			this.list.clear(); this.list.addAll(Cache.DayScheduleRules.map.values());
			
			rootRow=new DayScheduleRuleTableRow(null);
			for (Dayschedulerule r : this.list) {
				rootRow.addRow(new DayScheduleRuleTableRow(r));
			}
			
			int lastSelectedRow=-1;
			if (table!=null) {
				lastSelectedRow=table.getSelectedRow();
			}
			
			createTable();
			table.setTreeTableModel(new DayScheduleRuleTableModel(rootRow));
			
			if (lastSelectedRow>=0 && this.list.size()>0) {
				lastSelectedRow=Math.min(lastSelectedRow,this.list.size()-1);
				table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
			}
			
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
	
	public Dayschedulerule getSelectedRule () {
		return this.list.get(this.table.getSelectedRow());
	}
}
