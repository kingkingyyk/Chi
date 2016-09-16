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

public class FrameRegularScheduleManagement extends JFrame {
	private static FrameRegularScheduleManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class RegularScheduleTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Integer.class,String.class,Boolean.class,Integer.class,Boolean.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class RegularScheduleTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Actuator","Days","Time Rule","Switch","Priority","Enabled"};
		
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
		private ArrayList<RegularScheduleTableRow> subRow;
		public String [] renderText;
		
		public RegularScheduleTableRow(Regularschedule r) {
			if (r!=null) {
				renderText=new String[7];
				renderText[0]=r.getSchedulename();
				renderText[1]=r.getActuator().getName();
				renderText[2]=Utility.dayMaskToStr(r.getDaymask());
				renderText[3]=r.getDayschedulerule().getRulename();
				if (r.getEnabled()) renderText[4]="ON";
				else renderText[4]="OFF";
				renderText[5]=r.getPriority().toString();
				renderText[6]=r.getEnabled().toString();
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (RegularScheduleTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
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
			FrameRegularScheduleManagement.currInstance.updateRegularScheduleTable();
			FrameRegularScheduleManagement.currInstance.table.repaint();
		}
	}
	
	private JPanel contentPane;
	private RegularScheduleTable table;
	private RegularScheduleTableRow rootRow;
	private ArrayList<Regularschedule> list=new ArrayList<>();
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
		
		updateRegularScheduleTable();
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
		table.setComponentPopupMenu(new FrameRegularScheduleManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateRegularScheduleTable() {
		rootRow=new RegularScheduleTableRow(null);
		list.clear(); list.addAll(Cache.RegularSchedules.map.values());
		for (Regularschedule r : list) {
			rootRow.addRow(new RegularScheduleTableRow(r));
		}
		
		int lastSelectedRow=-1;
		if (table!=null) {
			lastSelectedRow=table.getSelectedRow();
		}
		createTable();
		table.setAutoCreateRowSorter(true);
		table.setTreeTableModel(new RegularScheduleTableModel(rootRow));
		
		if (lastSelectedRow>=0 && list.size()>0) {
			lastSelectedRow=Math.min(lastSelectedRow,list.size()-1);
			table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
		}
		updateSuccess=true;

		if (updateSuccess) {
			table.getColumn(0).setCellRenderer(new RegularScheduleTableCellRenderer());
			table.getColumn(1).setCellRenderer(new RegularScheduleTableCellRenderer());
			table.getColumn(2).setCellRenderer(new RegularScheduleTableCellRenderer());
			table.getColumn(3).setCellRenderer(new RegularScheduleTableCellRenderer());
			table.getColumn(4).setCellRenderer(new RegularScheduleTableCellRenderer());
			table.getColumn(5).setCellRenderer(new RegularScheduleTableCellRenderer());
			table.getColumn(6).setCellRenderer(new RegularScheduleTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(54);
			table.getColumnModel().getColumn(2).setPreferredWidth(133);
			table.getColumnModel().getColumn(3).setPreferredWidth(54);
			table.getColumnModel().getColumn(4).setPreferredWidth(54);
			table.getColumnModel().getColumn(5).setPreferredWidth(54);
			table.getColumnModel().getColumn(6).setPreferredWidth(54);
		}
	}
	
	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Regularschedule getSelectedSchedule () {
		return this.list.get(this.getSelectedRow());
	}
}
