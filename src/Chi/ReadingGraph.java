package Chi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.datastax.driver.core.Row;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.time.Minute;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;
import java.awt.FlowLayout;

public class ReadingGraph extends JFrame {
	private JPanel contentPane;

	public ReadingGraph(ArrayList<Row> rows) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 665, 434);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		TimeSeriesCollection dataset=new TimeSeriesCollection();
		TimeSeries s1=new TimeSeries("Usage");
		for (Row row : rows) {
			s1.add(new Minute(row.getTimestamp(2)),row.getDouble(3));
		}
		dataset.addSeries(s1);
		
    	JFreeChart barChart = ChartFactory.createTimeSeriesChart("Usage", "Time", "kWh", dataset, true, true, false);
    	barChart.removeLegend();
    	barChart.getTitle().setFont(new Font("Segoe UI",Font.BOLD,20));
    	//barChart.getTitle().setPaint(Color.WHITE);
    	//barChart.setBackgroundImage(new ImageIcon(this.getClass().getResource("/SmallDialogTopPanel.jpg")).getImage());

        final DateAxis axis = (DateAxis) barChart.getXYPlot().getDomainAxis();
    	panel.setLayout(new BorderLayout(0, 0));
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        
    	ChartPanel chartPanel = new ChartPanel( barChart );
    	//chartPanel.setPreferredSize(new Dimension(2, 2));
    	chartPanel.setAutoscrolls(true);
    	//chartPanel.setBorder(new LineBorder(Color.GRAY));
    	panel.add(chartPanel);
	}

}
