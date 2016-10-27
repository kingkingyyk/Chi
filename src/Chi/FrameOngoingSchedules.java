package Chi;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import SchedulingServer.SchedulingData;
import SchedulingServer.SchedulingDataRegular;
import SchedulingServer.SchedulingDataSpecial;
import SchedulingServer.SchedulingServer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameOngoingSchedules extends JFrame {
	private static FrameOngoingSchedules currInstance;
	private static final long serialVersionUID = 1L;

	private static class OnGoingSchedulesTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,String.class,String.class,String.class,Boolean.class,Integer.class,LocalDateTime.class,LocalDateTime.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class OnGoingSchedulesTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Type","Actuator","OnStart","OnEnd","Lock","Priority","Start At","End At"};
		
		public OnGoingSchedulesTableModel (OnGoingSchedulesTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((OnGoingSchedulesTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((OnGoingSchedulesTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((OnGoingSchedulesTableRow)arg0).getSubRowIndex((OnGoingSchedulesTableRow)arg1);
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
	
	private static class OnGoingSchedulesTableRow {
		private ArrayList<OnGoingSchedulesTableRow> subRow;
		private DateTimeFormatter dt=DateTimeFormatter.ofPattern("yyyy/LLL/dd KK:mm a");
		public String [] renderText;
		public SchedulingData data;
		
		public OnGoingSchedulesTableRow(SchedulingData d) {
			if (d!=null) {
				renderText=new String[9];
				renderText[0]=d.getName();
				renderText[1]="";
				renderText[2]=d.getActuatorName();
				renderText[3]=d.getStartAction();
				renderText[4]=d.getEndAction();
				renderText[5]=String.valueOf(d.getLock());
				renderText[6]=String.valueOf(d.getPriority());
				renderText[7]=dt.format(d.getNextStartTime());
				renderText[8]=dt.format(d.getNextEndTime());
				
				this.data=d;
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (OnGoingSchedulesTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
		}
		
		public int getSubRowIndex (OnGoingSchedulesTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public OnGoingSchedulesTableRow getSubRow (int index) {
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
	
	private static class OnGoingSchedulesTableCellRenderer extends DefaultTableCellRenderer {
		private static Icon regularSchIcon=Utility.resizeImageIcon(Theme.getIcon("RegularScheduleIcon"), 16, 16);
		private static Icon specialSchIcon=Utility.resizeImageIcon(Theme.getIcon("SpecialScheduleIcon"), 16, 16);
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			OnGoingSchedulesTableRow row=(OnGoingSchedulesTableRow)value;
			if (row!=null && row.renderText!=null) {
				setText(row.renderText[ArrayUtils.indexOf(OnGoingSchedulesTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString());
				if (aTable.getColumnName(aColumn).equals(OnGoingSchedulesTableModel.COLUMNS[1])) {
					if (row.data instanceof SchedulingDataRegular) {
						setIcon(regularSchIcon);
					} else if (row.data instanceof SchedulingDataSpecial) {
						setIcon(specialSchIcon);
					}
				}

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
	
	public static FrameOngoingSchedules getInstance() {
		if (FrameOngoingSchedules.currInstance==null) {
			FrameOngoingSchedules.currInstance=new FrameOngoingSchedules();
			FrameOngoingSchedules.currInstance.setLocationRelativeTo(null);
		} else {
			FrameOngoingSchedules.currInstance.toFront();
			FrameOngoingSchedules.currInstance.repaint();
		}
		return FrameOngoingSchedules.currInstance;
	}
	
	public static void refresh() {
		if (FrameOngoingSchedules.currInstance!=null) {
			FrameOngoingSchedules.currInstance.updateOnGoingSchedulesTable();
			FrameOngoingSchedules.currInstance.table.repaint();
		}
	}
	
	private JPanel contentPane;
	private OnGoingSchedulesTable table;
	private OnGoingSchedulesTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;
	private ArrayList<SchedulingData> list=new ArrayList<>();

	public FrameOngoingSchedules() {
		setTitle("Ongoing Schedule");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameOngoingSchedules.currInstance=null;}
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
		
		updateOnGoingSchedulesTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		table = new OnGoingSchedulesTable();
		table.setEditable(false);
		table.setLeafIcon(null);
		table.setOpenIcon(null);
		table.setClosedIcon(null);
		scrollPane.setViewportView(table);
	}
	
	public void updateOnGoingSchedulesTable() {
		updateSuccess=true;
		if (updateSuccess) {
			list.clear();
			rootRow=new OnGoingSchedulesTableRow(null);
			list.addAll(SchedulingServer.getSchedulingThread().data.values());
			Collections.sort(list);
			
			for (SchedulingData d : list) {
				rootRow.addRow(new OnGoingSchedulesTableRow(d));
			}
			
			int lastSelectedRow=-1;
			if (table!=null) {
				lastSelectedRow=table.getSelectedRow();
			}
			createTable();
			table.setAutoCreateRowSorter(true);
			table.setTreeTableModel(new OnGoingSchedulesTableModel(rootRow));
			
			if (lastSelectedRow>=0 && list.size()>0) {
				lastSelectedRow=Math.min(lastSelectedRow,list.size()-1);
				table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
			}
			updateSuccess=true;
			
			table.getColumn(0).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(1).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(2).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(3).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(4).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(5).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(6).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(7).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			table.getColumn(8).setCellRenderer(new OnGoingSchedulesTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(20);
			table.getColumnModel().getColumn(2).setPreferredWidth(67);
			table.getColumnModel().getColumn(3).setPreferredWidth(30);
			table.getColumnModel().getColumn(4).setPreferredWidth(30);
			table.getColumnModel().getColumn(5).setPreferredWidth(30);
			table.getColumnModel().getColumn(6).setPreferredWidth(30);
			table.getColumnModel().getColumn(7).setPreferredWidth(133);
			table.getColumnModel().getColumn(8).setPreferredWidth(133);
		}
	}
	
	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	/*
	public Object [] getSelectedObj () {
		return this.ongoingSchduleList.get(this.getSelectedRow());
	}*/
}
