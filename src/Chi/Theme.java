package Chi;

import java.util.HashMap;
import javax.swing.ImageIcon;

public class Theme {
	
	private static HashMap<String,ImageIcon> map=new HashMap<>();
	
	public static void initialize() {
		map.put("Reset-16x16", Utility.resizeImageIcon(new ImageIcon(Theme.class.getResource("/images/reset.png")),16,16));
		map.put("TopTitleBar", new ImageIcon(Chi.class.getResource("/images/TopTitleBar.jpg")));
		map.put("CassandraLogo", new ImageIcon(Theme.class.getResource("/images/cassandra-logo.png")));
		map.put("HSQLLogo", new ImageIcon(Theme.class.getResource("/images/hypersql-logo.png")));
		map.put("GWTLogo", new ImageIcon(Theme.class.getResource("/images/gwt-logo.png")));
		map.put("ChiLogo", new ImageIcon(Theme.class.getResource("/images/chi-logo.png")));
		map.put("NotificationIcon", new ImageIcon(Theme.class.getResource("/images/NOTIFICATION.png")));
		map.put("ControllerIcon", new ImageIcon(Theme.class.getResource("/images/CONTROLLER.png")));
		map.put("SensorIcon", new ImageIcon(Theme.class.getResource("/images/SENSOR.png")));
		map.put("ActuatorIcon", new ImageIcon(Theme.class.getResource("/images/ACTUATOR.png")));
		map.put("AddIcon", new ImageIcon(Theme.class.getResource("/images/ADD.png")));
		map.put("ControllerIcon", new ImageIcon(Theme.class.getResource("/images/CONTROLLER.png")));
		map.put("DayScheduleRuleIcon", new ImageIcon(Theme.class.getResource("/images/DAY_SCHEDULE_RULE.png")));
		map.put("DeleteIcon", new ImageIcon(Theme.class.getResource("/images/DELETE.png")));
		map.put("EditIcon", new ImageIcon(Theme.class.getResource("/images/EDIT.png")));
		map.put("HelloIcon", new ImageIcon(Theme.class.getResource("/images/HELLO.png")));
		map.put("PointIcon", new ImageIcon(Theme.class.getResource("/images/POINT.png")));
		map.put("RegularScheduleIcon", new ImageIcon(Theme.class.getResource("/images/REGULAR_SCHEDULE.png")));
		map.put("ResetIcon", new ImageIcon(Theme.class.getResource("/images/reset.png")));
		map.put("SiteIcon", new ImageIcon(Theme.class.getResource("/images/SITE.png")));
		map.put("SpecialScheduleIcon", new ImageIcon(Theme.class.getResource("/images/SPECIAL_SCHEDULE.png")));
		map.put("TickIcon", new ImageIcon(Theme.class.getResource("/images/TICK.png")));
		map.put("UserIcon", new ImageIcon(Theme.class.getResource("/images/USER.png")));
		map.put("LoadingIcon", new ImageIcon(Theme.class.getResource("/images/loading2.gif")));
		map.put("StartScreen", new ImageIcon(Chi.class.getResource("/images/START_SCREEN.jpg")));
		map.put("GanttChartBackground", new ImageIcon(Chi.class.getResource("/images/GRAPH_BG.jpg")));
		map.put("GraphIcon", new ImageIcon(Chi.class.getResource("/images/STATISTICS.png")));
	}
	
	public static ImageIcon getIcon (String key) {
		return map.get(key);
	}
}
