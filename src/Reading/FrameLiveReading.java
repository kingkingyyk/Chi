package Reading;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

import Chi.Theme;
import Chi.Utility;
import Database.Cache;
import Database.DatabaseReading;
import Entity.Sensor;
import Entity.SensorReading;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import java.awt.BorderLayout;

public class FrameLiveReading extends JFrame {
	private static final long serialVersionUID = -2113178474247458689L;
	private static HashMap <Sensor,FrameLiveReading> fMap=new HashMap<>();
	private JPanel contentPane;
	private LocalDateTime lastUpdateTime;
	private TimeSeriesCollection dataset;
	private TimeSeries tSeries;
	private JFreeChart chart;
	private Timer t;
	private DateAxis dAxis;
	private Sensor s;
	private JFreeChart meterChart;
	private DefaultCategoryDataset meterDataset;
	
	private static final String TIMESTAMP_FORMAT="yyyy-MM-dd HH:mm:ss"; //format the time
	private static DateTimeFormatter formatter=DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);

	private TimeSeries tSeriesPredicted;
	
	private static class UpdateTask extends TimerTask {
		FrameLiveReading r;
		
		@Override
		public void run() {
			if (!Cache.Sensors.map.containsKey(r.s.getSensorname())) {
				r.dispose();
				fMap.remove(r.s);
				return;
			}
			if (DataServer.DataServer.started()) r.setTitle(r.s.getSensorname()+"'s Live Reading");
			else r.setTitle(r.s.getSensorname()+"'s Live Reading [Data Server not Started!]");
			if (r.chart.isNotify()) {
				r.chart.setNotify(false);
				
				r.meterChart.getCategoryPlot().getRangeAxis().setRange(r.s.getMinvalue(),r.s.getMaxvalue());

				LocalDateTime now=LocalDateTime.now();
				LinkedList<SensorReading> list=DatabaseReading.getReadingBetweenTime(r.s.getSensorname(),r.lastUpdateTime,now);
				r.lastUpdateTime=now;
				for (SensorReading r : list) {
					this.r.tSeries.addOrUpdate(new Second(Utility.localDateTimeToUtilDate(r.getTimestamp())),r.getActualValue());
					this.r.meterChart.setTitle(formatter.format(r.getTimestamp()));
				}
				
		    	((DateAxis) this.r.chart.getXYPlot().getDomainAxis()).setRange(Utility.localDateTimeToUtilDate(LocalDateTime.now().minusHours(1)), Utility.localDateTimeToUtilDate(LocalDateTime.now().plusMinutes(5)));
		    	
		    	if (this.r.tSeries.getItemCount()>0) {
		    		if (this.r.tSeries.getItemCount()>1) {
			    		SimpleRegression regress=new SimpleRegression();
			    		for (int i=0;i<this.r.tSeries.getItemCount();i++) {
			    			TimeSeriesDataItem dat=this.r.tSeries.getDataItem(i);
			    			regress.addData(dat.getPeriod().getStart().getTime(),dat.getValue().doubleValue());
			    		}
			    		if (r.tSeriesPredicted.getItemCount()>0 && r.tSeriesPredicted.getDataItem(r.tSeriesPredicted.getItemCount()-1).getPeriod().getStart().compareTo(r.tSeries.getDataItem(r.tSeries.getItemCount()-1).getPeriod().getStart())>0) {
			    			r.tSeriesPredicted.delete(r.tSeriesPredicted.getItemCount()-1, r.tSeriesPredicted.getItemCount()-1);
			    		}
			    		r.tSeriesPredicted.addOrUpdate(new Second(Utility.localDateTimeToUtilDate(now)),regress.predict(Utility.localDateTimeToUtilDate(now).getTime()));
		    		}
		    		
		    		Thread t=new Thread() {
		    			public void run () {
		    				double currValue=r.meterDataset.getValue("","").doubleValue();
		    				double targetValue=r.tSeries.getDataItem(r.tSeries.getItemCount()-1).getValue().doubleValue();    		
		    			    double perStep=(targetValue-currValue)/60;
		    				for (int i=0;i<60 && r.contentPane.isVisible();i++) {
		    					currValue+=perStep;
		    					r.meterDataset.setValue(currValue,"","");
		    					try {Thread.sleep(17);} catch (InterruptedException e) {}
		    				}
		    			}
		    		};
		    		t.start();
		    	}
		    	
		    	r.chart.setNotify(true);
				if (!this.r.tSeries.isEmpty()) {
			        this.r.dAxis.setMinimumDate(this.r.tSeries.getDataItem(0).getPeriod().getStart());
			        this.r.dAxis.setMaximumDate(Utility.localDateTimeToUtilDate(now.plusSeconds(5)));
				}
			}
		}
		
	}

	public static FrameLiveReading getInstance (Sensor s) {
		if (fMap.containsKey(s)) {
			FrameLiveReading f=fMap.get(s);
			f.toFront();
			return f;
		} else {
			return new FrameLiveReading(s);
		}
	}
	
	private FrameLiveReading(Sensor s) {
		setTitle(s.getSensorname()+"'s Live Reading");
		setIconImage(Theme.getIcon("ChiLogo").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 400);
        
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		dataset=new TimeSeriesCollection();
		tSeries=new TimeSeries("Real Value");
		tSeries.setMaximumItemCount(30);
		dataset.addSeries(tSeries);
		
		tSeriesPredicted=new TimeSeries("Predicted Value");
		tSeriesPredicted.setMaximumItemCount(30);
		dataset.addSeries(tSeriesPredicted);
		
    	chart = ChartFactory.createTimeSeriesChart("Live Reading", "Time", s.getUnit(), dataset, true, true, false);
    	//chart.removeLegend();
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
		panel.setMaximumDrawWidth(2400);
		panel.setMaximumDrawHeight(1600);
		chart.getTitle().setPaint(Color.WHITE);
    	chart.setBackgroundImage(Theme.getIcon("GanttChartBackground").getImage());
    	chart.setBackgroundPaint(new Color(0,0,0,0));
    	XYSplineRenderer areaRen=new XYSplineRenderer();
    	areaRen.setSeriesPaint(0,new Color(247,220,111,255));
    	areaRen.setSeriesStroke(0,new BasicStroke(3));
    	areaRen.setSeriesPaint(1,new Color(247,220,111,100));
    	areaRen.setSeriesStroke(1,new BasicStroke(3));
    	chart.getXYPlot().setRenderer(0,areaRen);
    	chart.getXYPlot().setBackgroundPaint(new Color(0,0,0,0));
    	chart.getXYPlot().setDomainGridlinesVisible(false);
    	chart.getXYPlot().setRangeGridlinesVisible(false);
    	
    	chart.getXYPlot().getDomainAxis().setAxisLinePaint(Color.WHITE);
    	chart.getXYPlot().getDomainAxis().setLabelPaint(Color.WHITE);
    	chart.getXYPlot().getDomainAxis().setTickLabelPaint(Color.WHITE);
    	chart.getXYPlot().getDomainAxis().setTickMarkPaint(Color.WHITE);
    	chart.getXYPlot().getDomainAxis().setAutoRange(true);
    	
    	chart.getXYPlot().getRangeAxis().setAxisLinePaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setLabelPaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setTickLabelPaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setTickMarkPaint(Color.WHITE);
    	chart.getXYPlot().getRangeAxis().setRange(s.getMinvalue(),s.getMaxvalue());
    	chart.getXYPlot().getRangeAxis().setAutoRange(true);
		scrollPane.setViewportView(panel);

		meterDataset=new DefaultCategoryDataset();
		meterDataset.setValue(0.0,"","");
		meterChart=ChartFactory.createBarChart("Latest", "", s.getUnit(),meterDataset);
		meterChart.setTitle("Latest");
		meterChart.getTitle().setFont(new Font("Segoe UI",Font.BOLD,15));
		meterChart.removeLegend();
		meterChart.setBackgroundPaint(new Color(0,0,0,100));
		meterChart.getTitle().setPaint(Color.WHITE);
		((BarRenderer)meterChart.getCategoryPlot().getRenderer()).setBarPainter(new StandardBarPainter());
		
		((BarRenderer)meterChart.getCategoryPlot().getRenderer()).setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		((BarRenderer)meterChart.getCategoryPlot().getRenderer()).setBaseItemLabelsVisible(true);
		((BarRenderer)meterChart.getCategoryPlot().getRenderer()).setBaseItemLabelPaint(Color.WHITE);
		((BarRenderer)meterChart.getCategoryPlot().getRenderer()).setBaseItemLabelFont(new Font("Segoe UI",Font.BOLD,12));
		
		meterChart.getCategoryPlot().setBackgroundPaint(new Color(0,0,0,0));
		meterChart.getCategoryPlot().setDomainGridlinesVisible(false);
		meterChart.getCategoryPlot().setRangeGridlinesVisible(false);
		meterChart.getCategoryPlot().getDomainAxis().setAxisLinePaint(Color.WHITE);
		meterChart.getCategoryPlot().getDomainAxis().setLabelPaint(Color.WHITE);
		meterChart.getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.WHITE);
		meterChart.getCategoryPlot().getDomainAxis().setTickMarkPaint(Color.WHITE);
    	
		meterChart.getCategoryPlot().getRangeAxis().setAxisLinePaint(Color.WHITE);
		meterChart.getCategoryPlot().getRangeAxis().setLabelPaint(Color.WHITE);
		meterChart.getCategoryPlot().getRangeAxis().setTickLabelPaint(Color.WHITE);
		meterChart.getCategoryPlot().getRangeAxis().setTickMarkPaint(Color.WHITE);
		meterChart.getCategoryPlot().getRangeAxis().setRange(s.getMinvalue(),s.getMaxvalue());
    	
		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		ChartPanel panel_2 = new ChartPanel(meterChart);
		panel_2.setMaximumDrawHeight(1750);
		panel_2.setMaximumDrawWidth(750);
		panel_2.setMinimumDrawHeight(351);
		panel_2.setMinimumDrawWidth(148);
		panel_1.add(panel_2);
		contentPane.setLayout(gl_contentPane);
		
        lastUpdateTime=LocalDateTime.now().minusHours(1);
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
				fMap.remove(s);
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
		
		fMap.put(s,this);
	}
}
