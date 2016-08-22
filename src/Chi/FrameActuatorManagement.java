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

public class FrameActuatorManagement extends JFrame {
	private static FrameActuatorManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class ActuatorTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class,String.class,Double.class,Double.class,String.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class ActuatorTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Name","Attached On"};
		
		public ActuatorTableModel (ActuatorTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((ActuatorTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((ActuatorTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((ActuatorTableRow)arg0).getSubRowIndex((ActuatorTableRow)arg1);
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
	
	private static class ActuatorTableRow {
		private ArrayList<ActuatorTableRow> subRow;
		public String [] renderText;
		
		public ActuatorTableRow(Object [] o) {
			if (o!=null) {
				renderText=new String[o.length];
				for (int i=0;i<o.length;i++) {
					renderText[i]=o[i].toString();
				}
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (ActuatorTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
		}
		
		public int getSubRowIndex (ActuatorTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public ActuatorTableRow getSubRow (int index) {
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
	
	private static class SiteTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			ActuatorTableRow row=(ActuatorTableRow)value;
			if (row!=null && row.renderText!=null) {
				setText(row.renderText[ArrayUtils.indexOf(ActuatorTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString());
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
	
	public static FrameActuatorManagement getInstance() {
		if (FrameActuatorManagement.currInstance==null) {
			FrameActuatorManagement.currInstance=new FrameActuatorManagement();
			FrameActuatorManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameActuatorManagement.currInstance.toFront();
			FrameActuatorManagement.currInstance.repaint();
		}
		return FrameActuatorManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameActuatorManagement.currInstance!=null) {
			FrameActuatorManagement.currInstance.updateActuatorTable();
			FrameActuatorManagement.currInstance.repaint();
		}
	}
	
	private JPanel contentPane;
	private ActuatorTable table;
	private ActuatorTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;
	private ArrayList<Object []> actuatorList=new ArrayList<>();

	public FrameActuatorManagement() {
		setTitle("Actuator Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameActuatorManagement.currInstance=null;}
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
		
		updateActuatorTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		table = new ActuatorTable();
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(new ImageIcon(getClass().getResource(Config.ICON_TEXTURE_PATH+"/ACTUATOR.png")), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		table.setComponentPopupMenu(new FrameActuatorManagementContextMenu(this));
		scrollPane.setViewportView(table);
	}
	
	public void updateActuatorTable() {
		updateSuccess=Cache.updateActuator();
		if (updateSuccess) {
			actuatorList.clear();
			actuatorList.addAll(Cache.actuatorObj);
			rootRow=new ActuatorTableRow(null);
			
			for (Object [] o : Cache.actuatorObj) {
				rootRow.addRow(new ActuatorTableRow(o));
			}
			
			int lastSelectedRow=-1;
			if (table!=null) {
				lastSelectedRow=table.getSelectedRow();
			}
			createTable();
			table.setAutoCreateRowSorter(true);
			table.setTreeTableModel(new ActuatorTableModel(rootRow));
			
			if (lastSelectedRow>=0 && actuatorList.size()>0) {
				lastSelectedRow=Math.min(lastSelectedRow,actuatorList.size()-1);
				table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
			}
			updateSuccess=true;
			
			table.getColumn(0).setCellRenderer(new SiteTableCellRenderer());
			table.getColumn(1).setCellRenderer(new SiteTableCellRenderer());
			
			table.getColumnModel().getColumn(0).setPreferredWidth(133);
			table.getColumnModel().getColumn(1).setPreferredWidth(266);

		}
	}
	
	public int getSelectedRow () {
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Object [] getSelectedObj () {
		return this.actuatorList.get(this.getSelectedRow());
	}
}
