package Reading;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import Chi.Theme;
import Chi.Utility;
import Database.DatabaseReading;
import Entity.Sensor;
import Entity.SensorReading;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class FrameReading extends JFrame {
	private static final long serialVersionUID = -2113178474247458689L;
	private JPanel contentPane;
	private LocalDateTime lastUpdateTime;
	private TimeSeriesCollection dataset;
	private TimeSeries tSeries;
	private JFreeChart chart;
	private Timer t;
	private DateAxis dAxis;
	private Sensor s;
	private JTable table;
	
	private static class UpdateTask extends TimerTask {
		FrameReading r;
		
		@Override
		public void run() {
			if (r.chart.isNotify()) {
				r.chart.setNotify(false);
				LocalDateTime now=LocalDateTime.now();
				LinkedList<SensorReading> list=DatabaseReading.getReadingBetweenTime(r.s.getSensorname(),r.lastUpdateTime,now);
				r.lastUpdateTime=now;
				for (SensorReading r : list) {
					this.r.tSeries.addOrUpdate(new Second(Utility.localDateTimeToUtilDate(r.getTimestamp())),r.getActualValue());
					((DefaultTableModel) this.r.table.getModel()).addRow(new Object [] {r.getTimestamp(),r.getActualValue()});
				}
				r.chart.setNotify(true);
				if (!this.r.tSeries.isEmpty()) {
			        this.r.dAxis.setMinimumDate(this.r.tSeries.getDataItem(0).getPeriod().getStart());
			        this.r.dAxis.setMaximumDate(Utility.localDateTimeToUtilDate(now.plusSeconds(5)));
				}
			}
		}
		
	}
	
	private static class TableModel extends DefaultTableModel {
		private static final long serialVersionUID = -869414171351487961L;
		@SuppressWarnings("rawtypes")
		Class[] columnTypes = new Class[] {
				LocalDateTime.class, Double.class
			};
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		
		
	}
	
	
	private static class DateTimeRenderer implements TableCellRenderer {
		private static DateTimeFormatter form=DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
		      boolean hasFocus, int row, int column) {
		    return new JLabel(form.format((LocalDateTime)value));
		  }
	}
	
	public FrameReading(Sensor s) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 400);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		dataset=new TimeSeriesCollection();
		tSeries=new TimeSeries("Current");
		dataset.addSeries(tSeries);
		
    	chart = ChartFactory.createTimeSeriesChart(s.getSensorname(), "Time", s.getUnit(), dataset, true, true, false);
    	chart.removeLegend();
    	chart.getTitle().setFont(new Font("Segoe UI",Font.BOLD,20));
    	chart.fireChartChanged();
    	
        dAxis = (DateAxis) chart.getXYPlot().getDomainAxis();
        dAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss aa"));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.8);
		splitPane.setPreferredSize(new Dimension(191, 25));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
		);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		ChartPanel panel = new ChartPanel(chart);
		panel.setMouseZoomable(true);
		panel.setMouseWheelEnabled(true);
		panel.setMinimumDrawWidth(600);
		panel.setMinimumDrawHeight(400);
		panel.setPreferredSize(new Dimension(400, 300));
		panel.setMaximumDrawWidth(600);
		panel.setMaximumDrawHeight(400);
		chart.getTitle().setPaint(Color.WHITE);
    	chart.setBackgroundImage(Theme.getIcon("GanttChartBackground").getImage());
    	chart.setBackgroundPaint(new Color(0,0,0,0));
    	chart.getXYPlot().setRenderer(new XYAreaRenderer());
    	((XYAreaRenderer)chart.getXYPlot().getRenderer()).setBaseFillPaint(Color.WHITE);
    	chart.getXYPlot().setBackgroundPaint(new Color(0,0,0,0));
    	chart.getXYPlot().getDomainAxis().setAxisLinePaint(Color.WHITE);
    	chart.getXYPlot().getDomainAxis().setLabelPaint(Color.WHITE);
    	chart.getXYPlot().getDomainAxis().setTickLabelPaint(Color.WHITE);
    	chart.getXYPlot().getDomainAxis().setTickMarkPaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setAxisLinePaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setLabelPaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setTickLabelPaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setTickMarkPaint(Color.WHITE);
		scrollPane.setViewportView(panel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		
		table = new JTable(new TableModel());
		TableColumn tc=new TableColumn(); tc.setHeaderValue("Timestamp");
		table.addColumn(tc);
		tc=new TableColumn(); tc.setHeaderValue("Value");
		table.addColumn(tc);
		table.setDefaultRenderer(LocalDateTime.class,new DateTimeRenderer());
		table.setAutoCreateRowSorter(true);
		
		scrollPane_1.setViewportView(table);
		contentPane.setLayout(gl_contentPane);
		
        lastUpdateTime=LocalDateTime.of(1970,1,1,0,0);
		t=new Timer();
		UpdateTask ut=new UpdateTask(); ut.r=this;
		t.schedule(ut,10,2000);
		this.s=s;
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {
				t.cancel();
			}

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
	}
}
