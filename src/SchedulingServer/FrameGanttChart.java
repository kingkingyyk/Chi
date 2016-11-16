package SchedulingServer;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
import org.jfree.chart.axis.DateAxis;

import Chi.Theme;
import Chi.Utility;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class FrameGanttChart extends JFrame {
	private static final long serialVersionUID = -2113178474247458689L;
	private static FrameGanttChart currInstance;
	private JPanel contentPane;
	private Timer t;
	private JFreeChart chart;
	private ChartPanel cPanel;
	private TaskSeriesCollection collection;
	private ArrayList<String> barDescription=new ArrayList<>();
	private int actuatorCount=0;
	
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
			
			((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setLowerMargin(0);
			((DateAxis)(chart.getCategoryPlot().getRangeAxis())).setUpperMargin(0);
			
			TaskSeries dataSet=new TaskSeries("0");
			dataSet.add(new Task("Today",Utility.localDateTimeToUtilDate(minBound),Utility.localDateTimeToUtilDate(maxBound)));
			barDescription.add("Ongoing...");
			
			HashMap<String,Integer> count=new HashMap<>();
			HashMap<Integer,TaskSeries> int2Task=new HashMap<>();
			int2Task.put(0,dataSet);
			
			for (SchedulingData dat : list) {
				if (dat.getNextStartTime().compareTo(maxBound)<0) {
					int index=count.getOrDefault(dat.getActuatorName(),0);
					if (!int2Task.containsKey(index)) {
						TaskSeries ts=new TaskSeries(String.valueOf(index));
						int2Task.put(index,ts);
					}
					count.put(dat.getActuatorName(),index+1);
					Task t=new Task(dat.getActuatorName(),Utility.localDateTimeToUtilDate(maxDate(dat.getNextStartTime(),minBound)),Utility.localDateTimeToUtilDate(minDate(dat.getNextEndTime(),maxBound)));
					int2Task.get(index).add(t);
					barDescription.add(dat.getName()+" : "+dat.getStartAction()+" & "+dat.getEndAction());
				}		
			}
			
			for (SchedulingData dat : list) {
				if (dat.getNextStartTime().compareTo(maxBound)<0) {
					if (dat instanceof SchedulingDataRegular && ((((SchedulingDataRegular) dat).getDay() & 1 << dat.getNextStartTime().plusDays(1).getDayOfWeek().getValue()) !=0) && dat.getNextStartTime().plusDays(1).compareTo(maxBound)<0) {
						int index=count.getOrDefault(dat.getActuatorName(),0);
						if (!int2Task.containsKey(index)) {
							TaskSeries ts=new TaskSeries(String.valueOf(index));
							int2Task.put(index,ts);
						}
						count.put(dat.getActuatorName(),index+1);
						Task t=new Task(dat.getActuatorName(),Utility.localDateTimeToUtilDate(maxDate(dat.getNextStartTime().plusDays(1),minBound)),Utility.localDateTimeToUtilDate(minDate(dat.getNextEndTime().plusDays(1),maxBound)));
						int2Task.get(index).add(t);
						barDescription.add(dat.getName()+" : "+dat.getStartAction()+" & "+dat.getEndAction());
					}
				}		
			}
			
			for (TaskSeries ts : int2Task.values()) collection.add(ts);
			
			actuatorCount=count.keySet().size();
		}
	}
	
	public FrameGanttChart() {
		setTitle("Gantt Chart");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 520);
		setIconImage(Theme.getIcon("ChiLogo").getImage());
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
		setContentPane(contentPane);
		
		collection=new TaskSeriesCollection();
		
    	chart = ChartFactory.createGanttChart("Upcoming Schedule in 24 Hours", "Actuator", "Time", (IntervalCategoryDataset) collection, true, true, false);
    	chart.removeLegend();
    	chart.getTitle().setFont(new Font("Segoe UI",Font.BOLD,20));
    	chart.getTitle().setPaint(Color.WHITE);
    	chart.fireChartChanged();
    	chart.setBackgroundImage(Theme.getIcon("GanttChartBackground").getImage());
    	chart.setBackgroundPaint(new Color(0,0,0,0));
    	((BarRenderer) chart.getCategoryPlot().getRenderer()).setBarPainter(new StandardBarPainter());
    	chart.getCategoryPlot().setBackgroundPaint(new Color(0,0,0,0));
    	chart.getCategoryPlot().getDomainAxis().setAxisLinePaint(Color.WHITE);
    	chart.getCategoryPlot().getDomainAxis().setLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getDomainAxis().setTickMarkPaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setAxisLinePaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setTickLabelPaint(Color.WHITE);
    	chart.getCategoryPlot().getRangeAxis().setTickMarkPaint(Color.WHITE);
    	
    	Font font3 = new Font("Dialog", Font.PLAIN, 11);
    	
    	CategoryItemRenderer renderer = chart.getCategoryPlot().getRenderer();
    	renderer.setBaseItemLabelGenerator( new IntervalCategoryItemLabelGenerator());
    	renderer.setBaseItemLabelsVisible(true);
    	renderer.setBaseItemLabelPaint(Color.BLACK);
    	renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.INSIDE9, TextAnchor.CENTER_LEFT));
    	renderer.setBaseItemLabelFont(font3);
    	renderer.setBaseItemLabelGenerator( new CategoryItemLabelGenerator(){

    	    @Override
    	    public String generateRowLabel(CategoryDataset dataset, int row) { return "ROW"; }

    	    @Override
    	    public String generateColumnLabel(CategoryDataset dataset, int column) { return "COLUMN"; }

    	    @Override
    	    public String generateLabel(CategoryDataset dataset, int row, int column) {
    	        return barDescription.get(row*actuatorCount+column);
    	    }


    	});
		
    	BarRenderer bren=(BarRenderer)chart.getCategoryPlot().getRenderer();
    	bren.setSeriesFillPaint(0,Color.ORANGE);
    	
		JScrollPane scrollPane = new JScrollPane();
    	
		cPanel = new ChartPanel(chart,true);
		cPanel.setZoomAroundAnchor(true);
		cPanel.setFillZoomRectangle(false);
		cPanel.setRefreshBuffer(true);
		cPanel.setMouseZoomable(true);
		cPanel.setMouseWheelEnabled(true);
		//cPanel.setMouseWheelEnabled(true);
		cPanel.setInitialDelay(300);
		scrollPane.setViewportView(cPanel);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE)
		);
		contentPane.setLayout(gl_contentPane);
		
		t=new Timer();
		UpdateTask ut=new UpdateTask(); ut.f=this;
		t.scheduleAtFixedRate(ut,0,10000);
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
		
		addComponentListener(new ComponentListener() {
		    public void componentResized(ComponentEvent e) {
		        cPanel.setMinimumDrawHeight(scrollPane.getHeight());
		        cPanel.setMinimumDrawWidth(scrollPane.getWidth());
		    }

			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {}

			@Override
			public void componentShown(ComponentEvent arg0) {}
		});
	}

}
