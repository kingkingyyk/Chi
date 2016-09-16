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
import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
		private Class [] colClass={String.class,String.class,Integer.class,Integer.class,Integer.class,String.class,Boolean.class,Integer.class,Boolean.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class SpecialScheduleTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Actuator","Year","Month","Day","Time Rule","Switch","Priority","Enabled"};
		
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
	
	private static class SpecialScheduleTableRow {
		private ArrayList<SpecialScheduleTableRow> subRow;
		public String [] renderText;
		
		public SpecialScheduleTableRow(Specialschedule s) {
			if (s!=null) {
				renderText=new String[SpecialScheduleTableModel.COLUMNS.length];
				renderText[0]=s.getSchedulename();
				renderText[1]=s.getActuator().getName();
				renderText[2]=s.getYear().toString();
				renderText[3]=s.getMonth().toString();
				renderText[4]=s.getDay().toString();
				renderText[5]=s.getDayschedulerule().getRulename();
				if (s.getActuatoron()) renderText[6]="ON";
				else renderText[6]="OFF";
				renderText[7]=s.getPriority().toString();
				renderText[8]=s.getEnabled().toString();
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (SpecialScheduleTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
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
	}
	
	private static class SpecialScheduleTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			SpecialScheduleTableRow row=(SpecialScheduleTableRow)value;
			if (row!=null && row.renderText!=null) {
				int index=ArrayUtils.indexOf(SpecialScheduleTableModel.COLUMNS,aTable.getColumnName(aColumn));
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
			FrameSpecialScheduleManagement.currInstance.updateSpecialScheduleTable();
			FrameSpecialScheduleManagement.currInstance.table.repaint();
		}
	}
	
	private JPanel contentPane;
	private SpecialScheduleTable table;
	private SpecialScheduleTableRow rootRow;
	private ArrayList<Specialschedule> list=new ArrayList<>();
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
		
		updateSpecialScheduleTable();
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
		table.setComponentPopupMenu(new FrameSpecialScheduleManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateSpecialScheduleTable() {
		rootRow=new SpecialScheduleTableRow(null);
		list.clear(); list.addAll(Cache.SpecialSchedules.map.values());
		for (Specialschedule s : list) {
			rootRow.addRow(new SpecialScheduleTableRow(s));
		}
		
		int lastSelectedRow=-1;
		if (table!=null) {
			lastSelectedRow=table.getSelectedRow();
		}
		createTable();
		table.setAutoCreateRowSorter(true);
		table.setTreeTableModel(new SpecialScheduleTableModel(rootRow));
		
		if (lastSelectedRow>=0 && list.size()>0) {
			lastSelectedRow=Math.min(lastSelectedRow,list.size()-1);
			table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
		}
		updateSuccess=true;

		if (updateSuccess) {
			table.getColumn(0).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(1).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(2).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(3).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(4).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(5).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(6).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(7).setCellRenderer(new SpecialScheduleTableCellRenderer());
			table.getColumn(8).setCellRenderer(new SpecialScheduleTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(54);
			table.getColumnModel().getColumn(2).setPreferredWidth(30);
			table.getColumnModel().getColumn(3).setPreferredWidth(30);
			table.getColumnModel().getColumn(4).setPreferredWidth(30);
			table.getColumnModel().getColumn(5).setPreferredWidth(54);
			table.getColumnModel().getColumn(6).setPreferredWidth(54);
			table.getColumnModel().getColumn(7).setPreferredWidth(54);
			table.getColumnModel().getColumn(8).setPreferredWidth(54);
		}
	}
	
	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Specialschedule getSelectedSchedule () {
		return this.list.get(this.getSelectedRow());
	}
}
