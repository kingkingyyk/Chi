package Chi;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
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

public class FrameControllerManagement extends JFrame {
	private static FrameControllerManagement currInstance;
	private static final long serialVersionUID = 1L;
	private static SimpleDateFormat dateFormatter=new SimpleDateFormat("yyyy MMMM dd KK:mm:ss aa");
	
	private static class ControllerTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Double.class,Double.class,String.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class ControllerTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Site","Position X","Position Y","Report Period","Last Report Time"};
		
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
		private ArrayList<ControllerTableRow> subRow;
		public String [] renderText;
		
		public ControllerTableRow(Controller ctrl) {
			if (ctrl!=null) {
				renderText=new String[6];
				renderText[0]=ctrl.getControllername();
				renderText[1]=ctrl.getSite().getSitename();
				renderText[2]=String.format("%.2f",ctrl.getPositionx()); //position X
				renderText[3]=String.format("%.2f",ctrl.getPositiony()); //position Y
				
				int period=ctrl.getReporttimeout();
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
				renderText[5]=dateFormatter.format(ctrl.getLastreporttime()); //last report time
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (ControllerTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
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
			FrameControllerManagement.currInstance.toFront();
			FrameControllerManagement.currInstance.repaint();
		}
	}
	
	private JPanel contentPane;
	private ControllerTable table;
	private ControllerTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;
	private ArrayList<Controller> controllerList=new ArrayList<>();

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
		
		updateControllerTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		table = new ControllerTable();
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("ControllerIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		table.setComponentPopupMenu(new FrameControllerManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateControllerTable() {
		updateSuccess=true;
		if (updateSuccess) {
			controllerList.clear();
			controllerList.addAll(Cache.Controllers.map.values());
			rootRow=new ControllerTableRow(null);
			
			for (Controller ctlr : controllerList) {
				rootRow.addRow(new ControllerTableRow(ctlr));
			}
			
			int lastSelectedRow=-1;
			if (table!=null) {
				lastSelectedRow=table.getSelectedRow();
			}
			createTable();
			table.setAutoCreateRowSorter(true);
			table.setTreeTableModel(new ControllerTableModel(rootRow));
			
			if (lastSelectedRow>=0 && controllerList.size()>0) {
				lastSelectedRow=Math.min(lastSelectedRow,Cache.Controllers.map.values().size()-1);
				table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
			}
			updateSuccess=true;
			
			table.getColumn(0).setCellRenderer(new ControllerTableCellRenderer());
			table.getColumn(1).setCellRenderer(new ControllerTableCellRenderer());
			table.getColumn(2).setCellRenderer(new ControllerTableCellRenderer());
			table.getColumn(3).setCellRenderer(new ControllerTableCellRenderer());
			table.getColumn(4).setCellRenderer(new ControllerTableCellRenderer());
			table.getColumn(5).setCellRenderer(new ControllerTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(30);
			table.getColumnModel().getColumn(3).setPreferredWidth(30);
			table.getColumnModel().getColumn(4).setPreferredWidth(60);
			table.getColumnModel().getColumn(5).setPreferredWidth(133);

		}
	}
	
	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Controller getSelectedController () {
		return this.controllerList.get(this.getSelectedRow());
	}
}
