package SchedulingServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.IntervalCategoryItemLabelGenerator;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.ui.TextAnchor;

import Chi.Utility;
import javax.swing.JScrollPane;

public class FrameGanttChart extends JFrame {
	private static final long serialVersionUID = -2113178474247458689L;
	private static FrameGanttChart currInstance;
	private JPanel contentPane;
	private Timer t;
	private JFreeChart chart;
	private ChartPanel cPanel;
	private TaskSeriesCollection collection;
	private ArrayList<String> barDescription=new ArrayList<>();
	
	private static class UpdateTask extends TimerTask {
		FrameGanttChart f;
		@Override
		public void run() {
			f.update();
		}
		
	}
	
	public static FrameGanttChart getInstance() {
		if (FrameGanttChart.currInstance==null) {
			FrameGanttChart.currInstance=new FrameGanttChart();
			FrameGanttChart.currInstance.setLocationRelativeTo(null);
		} else {
			FrameGanttChart.currInstance.toFront();
			FrameGanttChart.currInstance.repaint();
		}
		return FrameGanttChart.currInstance;
	}
	
	public static void refresh() {
		if (FrameGanttChart.currInstance!=null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run () {
					FrameGanttChart.currInstance.update();
				}
			});
		}
	}
	
	private static LocalDateTime minDate (LocalDateTime d1, LocalDateTime d2) {
		if (d1.compareTo(d2)>=0) return d2;
		else return d1;
	}
	
	private static LocalDateTime maxDate (LocalDateTime d1, LocalDateTime d2) {
		if (d1.compareTo(d2)>=0) return d1;
		else return d2;
	}
	
	private void update () {
		if (SchedulingServer.started()) {
			collection.removeAll(); barDescription.clear();
			ArrayList<SchedulingData> list = new ArrayList<>();
			list.addAll(SchedulingServer.getSchedulingThread().data.values());
			Collections.sort(list);
			
			LocalDateTime minBound=LocalDateTime.now();
			LocalDateTime maxBound=minBound.plusDays(1);
			TaskSeries todayBar=new TaskSeries("Today");
			todayBar.add(new Task("Today",Utility.localDateTimeToUtilDate(minBound),Utility.localDateTimeToUtilDate(maxBound)));
			barDescription.add("Ongoing...");
			collection.add(todayBar);
			
			for (SchedulingData dat : list) {
				if (dat.getNextStartTime().compareTo(maxBound)<0) {
					TaskSeries dataSet=new TaskSeries(dat.getName());
					Task t=new Task(dat.actuatorName,Utility.localDateTimeToUtilDate(maxDate(dat.getNextStartTime(),minBound)),Utility.localDateTimeToUtilDate(minDate(dat.getNextEndTime(),maxBound)));
					dataSet.add(t);
					barDescription.add(dat.getName());
					collection.add(dataSet);
				}		
			}
			
			for (SchedulingData dat : list) {
				if (dat.getNextStartTime().compareTo(maxBound)<0) {
					if (dat instanceof SchedulingDataRegular && ((((SchedulingDataRegular) dat).getDay() & 1 << dat.getNextStartTime().plusDays(1).getDayOfWeek().getValue()) !=0) && dat.getNextStartTime().plusDays(1).compareTo(maxBound)<0) {
						TaskSeries dataSet=new TaskSeries(dat.getName());
						dataSet=new TaskSeries(dat.getName()+'.');
						Task t=new Task(dat.actuatorName,Utility.localDateTimeToUtilDate(maxDate(dat.getNextStartTime().plusDays(1),minBound)),Utility.localDateTimeToUtilDate(minDate(dat.getNextEndTime().plusDays(1),maxBound)));
						dataSet.add(t);
						barDescription.add(dat.getName());
						collection.add(dataSet);
					}
				}		
			}
			

		}
	}
	
	public FrameGanttChart() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 400);
		addWindowListener(new WindowListener() {
			@Override
			public void windowClosed(WindowEvent arg0) { FrameGanttChart.currInstance=null;}
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
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		collection=new TaskSeriesCollection();
		
    	chart = ChartFactory.createGanttChart("Scheduling", "Actuator", "Time", (IntervalCategoryDataset) collection, true, true, false);
    	chart.removeLegend();
    	chart.getTitle().setFont(new Font("Segoe UI",Font.BOLD,20));
    	chart.getTitle().setPaint(Color.WHITE);
    	chart.fireChartChanged();
    	chart.setBackgroundPaint(Color.BLACK);
    	((BarRenderer) chart.getCategoryPlot().getRenderer()).setBarPainter(new StandardBarPainter());
    	chart.getCategoryPlot().setBackgroundPaint(Color.BLACK);
    	chart.getCategoryPlot().getDomainAxis().setAxisLinePaint(Color.WHITE);
    	chart.getCategoryPlot().getDomainAxis().setLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getDomainAxis().setTickMarkPaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setAxisLinePaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setTickMarkPaint(Color.WHITE);
    	
    	Font font3 = new Font("Dialog", Font.PLAIN, 9);
    	
    	CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
    	renderer.setBaseItemLabelGenerator( new IntervalCategoryItemLabelGenerator());
    	renderer.setBaseItemLabelsVisible(true);
    	renderer.setBaseItemLabelPaint(Color.BLACK);
    	renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
    	ItemLabelAnchor.INSIDE6, TextAnchor.BOTTOM_CENTER));
    	renderer.setBaseItemLabelFont(font3);
    	renderer.setBaseItemLabelGenerator( new CategoryItemLabelGenerator(){

    	    @Override
    	    public String generateRowLabel(CategoryDataset dataset, int row) { return "ROW"; }

    	    @Override
    	    public String generateColumnLabel(CategoryDataset dataset, int column) { return "COLUMN"; }

    	    @Override
    	    public String generateLabel(CategoryDataset dataset, int row, int column) {
    	        return barDescription.get(row);
    	    }


    	});
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
    	
		cPanel = new ChartPanel(chart);
		scrollPane.setViewportView(cPanel);
		
		t=new Timer();
		UpdateTask ut=new UpdateTask(); ut.f=this;
		t.scheduleAtFixedRate(ut,0,2000);
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {}

			@Override
			public void windowClosed(WindowEvent arg0) {t.cancel();cPanel.removeAll();}

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
