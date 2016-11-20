package NotificationServer;

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

import Chi.Theme;
import Chi.Utility;
import Database.DatabaseEvent;
import Entity.Controllerevent;
import Entity.Sensorevent;
import Entity.Actuatorevent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Icon;

public class FrameNotification extends JFrame {
	private static FrameNotification currInstance;
	private static final long serialVersionUID = 1L;

	private static class Notification implements Comparable<Notification> {
		String name;
		String type;
		LocalDateTime dt;
		String eventType;
		String eventDetails;
		
		public Notification (String n, String t, LocalDateTime ldt, String et, String ed) {
			this.name=n;
			this.type=t;
			this.dt=ldt;
			this.eventType=et;
			this.eventDetails=ed;
		}
		
		public int compareTo(Notification n) {
			if (this.dt.equals(n.dt))
				if (this.name.equals(n.name))
					if (this.type.equals(n.type))
						if (this.eventType.equals(n.eventType))
							return this.eventDetails.compareTo(n.eventDetails);
						else
							return this.eventType.compareTo(n.eventType);
					else
						return this.type.compareTo(n.type);
				else
					return this.name.compareTo(n.name);
			else
				return n.dt.compareTo(this.dt);
		}
	}
	
	private static class NotificationTable extends JXTreeTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={LocalDateTime.class,String.class,String.class,String.class,String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	private static class NotificationTableModel extends AbstractTreeTableModel {
		public static final String [] COLUMNS= {"Time","Type","Name","Event Type","Details"};
		
		public NotificationTableModel (NotificationTableRow r) {
			super(r);
		}
		
		@Override
		public int getColumnCount() {
			return COLUMNS.length;
		}

		@Override
		public Object getChild(Object arg0, int arg1) {
			return ((NotificationTableRow)arg0).getSubRow(arg1);
		}

		@Override
		public int getChildCount(Object arg0) {
			return ((NotificationTableRow)arg0).subRowCount();
		}

		@Override
		public int getIndexOfChild(Object arg0, Object arg1) {
			return ((NotificationTableRow)arg0).getSubRowIndex((NotificationTableRow)arg1);
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
	
	private static class NotificationTableRow {
		private static final String TIMESTAMP_FORMAT="yyyy-MM-dd HH:mm:ss.SSS"; //format the time
		private static DateTimeFormatter formatter=DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
		private ArrayList<NotificationTableRow> subRow;
		public String [] renderText;
		
		public NotificationTableRow(Notification n) {
			if (n!=null) {
				renderText=new String [] {n.dt.format(formatter),n.type,n.name,n.eventType,n.eventDetails};
			} else {
				renderText=new String [] {"root"};
			}
		}
		
		public void addRow (NotificationTableRow r) {
			if (this.subRow==null) {
				this.subRow=new ArrayList<>();
			}
			this.subRow.add(r);
		}
		
		public int getSubRowIndex (NotificationTableRow r) {
			return this.subRow.indexOf(r);
		}
		
		public NotificationTableRow getSubRow (int index) {
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
	
	private static class NotificationTableCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent (JTable aTable, Object value, boolean aIsSelected, boolean aHasFocus, int aRow, int aColumn) {
			NotificationTableRow row=(NotificationTableRow)value;
			if (row!=null && row.renderText!=null) {
				String text=row.renderText[ArrayUtils.indexOf(NotificationTableModel.COLUMNS,aTable.getColumnName(aColumn))].toString();
				if (ArrayUtils.indexOf(NotificationTableModel.COLUMNS,"Type")==aColumn) {
					if (text.equals("CONTROLLER")) setIcon(Utility.resizeImageIcon(Theme.getIcon("ControllerIcon"), 16, 16));
					else if (text.equals("SENSOR")) setIcon(Utility.resizeImageIcon(Theme.getIcon("SensorIcon"), 16, 16));
					else if (text.equals("ACTUATOR")) setIcon(Utility.resizeImageIcon(Theme.getIcon("ActuatorIcon"), 16, 16));
				} else {
					setText(text);
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
	
	public static FrameNotification getInstance() {
		if (FrameNotification.currInstance==null) {
			FrameNotification.currInstance=new FrameNotification();
			FrameNotification.currInstance.setLocationRelativeTo(null);
		} else {
			FrameNotification.currInstance.toFront();
			FrameNotification.currInstance.repaint();
		}
		return FrameNotification.currInstance;
	}
	
	public static void refresh() {
		if (FrameNotification.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameNotification.currInstance.updateNotificationTable();
				}
			});
		}
	}
	
	private JPanel contentPane;
	private NotificationTable table;
	private NotificationTableRow rootRow;
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameNotification() {
		setTitle("Notification Center");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameNotification.currInstance=null;}
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
		
		updateNotificationTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		table = new NotificationTable();
		table.setEditable(false);
		Icon img=Utility.resizeImageIcon(Theme.getIcon("NotificationIcon"), 16, 16);
		table.setLeafIcon(img);
		table.setOpenIcon(img);
		table.setClosedIcon(img);
		scrollPane.setViewportView(table);
	}
	
	public void updateNotificationTable() {
		rootRow=new NotificationTableRow(null);
		
		ArrayList<Sensorevent> seL=DatabaseEvent.getSensorEventBetweenTime(LocalDateTime.of(1970,1,1,0,0),LocalDateTime.now());
		ArrayList<Controllerevent> ceL=DatabaseEvent.getControllerEventBetweenTime(LocalDateTime.of(1970,1,1,0,0),LocalDateTime.now());
		ArrayList<Actuatorevent> aeL=DatabaseEvent.getActuatorEventBetweenTime(LocalDateTime.of(1970,1,1,0,0),LocalDateTime.now());
		
		ArrayList<Notification> nL=new ArrayList<>();
		for (Sensorevent se : seL) {
			nL.add(new Notification(se.getSensor().getSensorname(),"SENSOR",Utility.dateToLocalDateTime(se.getTimestp()),se.getEventtype(),se.getEventvalue()));
		}
		for (Controllerevent ce : ceL) {
			nL.add(new Notification(ce.getController().getControllername(),"CONTROLLER",Utility.dateToLocalDateTime(ce.getTimestp()),ce.getEventtype(),ce.getEventvalue()));
		}
		for (Actuatorevent ae : aeL) {
			nL.add(new Notification(ae.getActuator().getName(),"ACTUATOR",Utility.dateToLocalDateTime(ae.getTimestp()),ae.getEventtype(),ae.getEventvalue()));
		}
		Collections.sort(nL);
		
		for (Notification n : nL) {
			rootRow.addRow(new NotificationTableRow(n));
		}
		
		int lastSelectedRow=-1;
		if (table!=null) {
			lastSelectedRow=table.getSelectedRow();
		}
		createTable();
		table.setAutoCreateRowSorter(true);
		table.setTreeTableModel(new NotificationTableModel(rootRow));
		
		if (lastSelectedRow>=0 && nL.size()>0) {
			lastSelectedRow=Math.min(lastSelectedRow,nL.size()-1);
			table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
		}
		
		table.getColumn(0).setCellRenderer(new NotificationTableCellRenderer());
		table.getColumn(1).setCellRenderer(new NotificationTableCellRenderer());
		table.getColumn(2).setCellRenderer(new NotificationTableCellRenderer());
		table.getColumn(3).setCellRenderer(new NotificationTableCellRenderer());
		table.getColumn(4).setCellRenderer(new NotificationTableCellRenderer());
		
		table.getColumnModel().getColumn(0).setPreferredWidth(54);
		table.getColumnModel().getColumn(1).setPreferredWidth(5);
		table.getColumnModel().getColumn(2).setPreferredWidth(54);
		table.getColumnModel().getColumn(3).setPreferredWidth(54);
		table.getColumnModel().getColumn(4).setPreferredWidth(133);
	}
	
}
