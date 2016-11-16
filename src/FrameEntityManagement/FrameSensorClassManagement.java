package FrameEntityManagement;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Chi.Theme;
import Database.Cache;
import Entity.Sensorclass;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;

public class FrameSensorClassManagement extends JFrame {
	private static FrameSensorClassManagement currInstance;
	private static final long serialVersionUID = 1L;

	private static class SensorClassTable extends JTable {
		private static final long serialVersionUID = 5965017601846585072L;
		
		public boolean isCellEditable(int row, int column){ return false;  }
		
		@SuppressWarnings("rawtypes")
		private Class [] colClass={String.class};
	    @Override
	    public Class<?> getColumnClass(int colNum) {
	    	return colClass[colNum];
	    }
	    
	}
	
	public static FrameSensorClassManagement getInstance() {
		if (FrameSensorClassManagement.currInstance==null) {
			FrameSensorClassManagement.currInstance=new FrameSensorClassManagement();
			FrameSensorClassManagement.currInstance.setLocationRelativeTo(null);
		} else {
			FrameSensorClassManagement.currInstance.toFront();
			FrameSensorClassManagement.currInstance.repaint();
		}
		return FrameSensorClassManagement.currInstance;
	}
	
	public static void refresh() {
		if (FrameSensorClassManagement.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameSensorClassManagement.currInstance.updateSensorClassTable();
					FrameSensorClassManagement.currInstance.table.repaint();
				}
			});
		}
	}
	
	private JPanel contentPane;
	private SensorClassTable table;
	private ArrayList<Sensorclass> list=new ArrayList<>();
	public boolean updateSuccess;
	private JScrollPane scrollPane;

	public FrameSensorClassManagement() {
		setTitle("Sensor Class Management");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameSensorClassManagement.currInstance=null;}
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
		
		updateSensorClassTable();
	}
	
	private void createTable() {
		if (table!=null) {
			table.removeAll();
		}
		Object [][] obj=new Object [list.size()][1];
		for (int i=0;i<list.size();i++) {
			obj[i][0]=list.get(i).getClassname();
		}
		table = new SensorClassTable();
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		table.setShowGrid(false);
		table.setAutoCreateRowSorter(true);
		table.setModel(new DefaultTableModel(obj,new String [] {"Name"}));
		FrameSensorClassManagementContextMenu popup=new FrameSensorClassManagementContextMenu(this);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
	            JTable source = (JTable)e.getSource();
	            int row = source.rowAtPoint( e.getPoint() );

	            if (getSelectedCount()==1) table.clearSelection();
	            if (row!=-1) {
	            	table.addRowSelectionInterval(row,row);
	            
					if (e.getButton()==MouseEvent.BUTTON1 && e.getClickCount()==2) {
						FrameSensorClassManagementActions.edit(FrameSensorClassManagement.this);
					}
				}
	            
	            if (e.getButton()==MouseEvent.BUTTON3 && e.getClickCount()==1) {
	            	popup.show(e.getComponent(), e.getX(), e.getY());
	            }
			}

			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override public void mouseReleased(MouseEvent arg0) {}
		});
		scrollPane.setViewportView(table);
		
		table.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Enter");
	    table.getActionMap().put("Enter", new AbstractAction() {
			private static final long serialVersionUID = 8673697961083541098L;

			@Override
	        public void actionPerformed(ActionEvent ae) {}
	    });
	    
		table.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				switch (arg0.getKeyCode()) {
					case (KeyEvent.VK_INSERT) : {
						FrameSensorClassManagementActions.add();
						break;
					}
					case (KeyEvent.VK_ENTER) : {
						if (getSelectedCount()==1) FrameSensorClassManagementActions.edit(FrameSensorClassManagement.this);
						else Toolkit.getDefaultToolkit().beep();
						break;
					}
					case (KeyEvent.VK_DELETE) : {
						if (getSelectedCount()>0) FrameSensorClassManagementActions.delete(FrameSensorClassManagement.this);
						break;
					}
				}
			}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override public void keyTyped(KeyEvent arg0) {}
			
		});
		
		table.getTableHeader().addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (arg0.getButton()==MouseEvent.BUTTON3) {
					if (!popup.isVisible()) {
						popup.setLocation(arg0.getLocationOnScreen());
						popup.setVisible(true);
						Thread t=new Thread() {
							public void run () {
								try { Thread.sleep(7000); } catch (InterruptedException e) {}
								if (popup.isVisible()) popup.setVisible(false);
							}
						};
						t.start();
					} else {
						popup.setVisible(false);
					}
				}
			}

			@Override public void mouseEntered(MouseEvent arg0) {}
			@Override public void mouseExited(MouseEvent arg0) {}
			@Override public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
	}
	
	public void updateSensorClassTable() {
		if (true) {
			list.clear();
			list.addAll(Cache.SensorClasses.map.values());
			int lastSelectedRow=-1;
			if (table!=null) {
				 lastSelectedRow=table.getSelectedRow();
			}
			createTable();
			if (lastSelectedRow>=0 && list.size()>0) {
				lastSelectedRow=Math.min(lastSelectedRow,list.size()-1);
				table.setRowSelectionInterval(lastSelectedRow,lastSelectedRow);
			}
			updateSuccess=true;
		}
	}
	
	public int getSelectedRow () {
		if (this.table.getSelectedRow()==-1) {
			return -1;
		}
		return this.table.convertRowIndexToModel(this.table.getSelectedRow());
	}
	
	public Sensorclass getSelectedClass () {
		return this.list.get(this.getSelectedRow());
	}
	
	public int getSelectedCount () {
		return this.table.getSelectedRowCount();
	}
	
	public int[] getSelectedRows () {
		if (this.table.getSelectedRow()==-1) {
			return null;
		}
		int [] rows=this.table.getSelectedRows();
		int [] value=new int [getSelectedCount()];
		for (int i=0;i<value.length;i++) value[i]=this.table.convertRowIndexToModel(rows[i]);
		return value;
	}
	
	public Sensorclass [] getSelectedClasses () {
		int [] rows=this.getSelectedRows();
		Sensorclass [] scs=new Sensorclass[rows.length];
		for (int i=0;i<scs.length;i++) scs[i]=this.list.get(rows[i]);
		return scs;
	}
}
