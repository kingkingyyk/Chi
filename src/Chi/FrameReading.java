package Chi;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import Database.DatabaseReading;
import Entity.Sensor;
import Entity.SensorReading;

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
	
	private static class UpdateTask extends TimerTask {
		FrameReading r;
		
		@Override
		public void run() {
			if (r.chart.isNotify()) {
				r.chart.setNotify(false);
				LocalDateTime now=LocalDateTime.now();
				LinkedList<SensorReading> list=DatabaseReading.getReadingBetweenTime(r.s.getSensorname(),r.lastUpdateTime,now);
				r.lastUpdateTime=now;
				for (SensorReading r : list) this.r.tSeries.addOrUpdate(new Second(Utility.localDateTimeToUtilDate(r.getTimestamp())),r.getActualValue());
				r.chart.setNotify(true);
				if (!this.r.tSeries.isEmpty()) {
			        this.r.dAxis.setMinimumDate(this.r.tSeries.getDataItem(0).getPeriod().getStart());
			        this.r.dAxis.setMaximumDate(Utility.localDateTimeToUtilDate(now.plusSeconds(5)));
				}
			}
		}
		
	}
	
	public FrameReading(Sensor s) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 400);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
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
		ChartPanel panel = new ChartPanel(chart);
		contentPane.add(panel, BorderLayout.CENTER);
		
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
