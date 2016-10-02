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
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class FrameReading extends JFrame {
	private static final long serialVersionUID = -2113178474247458689L;
	private JPanel contentPane;
	private LocalDateTime lastUpdateTime;
	private TimeSeriesCollection dataset;
	private TimeSeries tSeries;
	private JFreeChart chart;
	private Timer t;
	private DateAxis dAxis;
	
	private static class UpdateTask extends TimerTask {
		FrameReading r;
		
		@Override
		public void run() {
			if (r.chart.isNotify()) {
				r.chart.setNotify(false);
				LocalDateTime now=LocalDateTime.now();
				LinkedList<SensorReading> list=DatabaseReading.getReadingBetweenTime("Current",r.lastUpdateTime,now);
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
	
	public FrameReading() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 400);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		dataset=new TimeSeriesCollection();
		tSeries=new TimeSeries("Current");
		//tSeries.setMaximumItemCount(60);
		dataset.addSeries(tSeries);
		
    	chart = ChartFactory.createTimeSeriesChart("Current", "Time", "A", dataset, true, true, false);
    	chart.removeLegend();
    	chart.getTitle().setFont(new Font("Segoe UI",Font.BOLD,20));
    	chart.fireChartChanged();
    	
        dAxis = (DateAxis) chart.getXYPlot().getDomainAxis();
        dAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss aa"));
		ChartPanel panel = new ChartPanel(chart);
		contentPane.add(panel, BorderLayout.CENTER);
		
		chart.getXYPlot().getRangeAxis().setRangeWithMargins(0,30);
		
        lastUpdateTime=LocalDateTime.of(1970,1,1,0,0);
		t=new Timer();
		UpdateTask ut=new UpdateTask(); ut.r=this;
		t.schedule(ut,10,2000);
		
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
