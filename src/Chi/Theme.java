package Chi;

import java.util.HashMap;
import javax.swing.ImageIcon;

public class Theme {
	
	private static HashMap<String,ImageIcon> map=new HashMap<>();
	
	public static void initialize() {
		map.put("Reset-16x16", Utility.resizeImageIcon(new ImageIcon(Theme.class.getResource("/images/reset.png")),16,16));
		map.put("TopTitleBar", new ImageIcon(Theme.class.getResource("/images/TopTitleBar.jpg")));
		map.put("CassandraLogo", new ImageIcon(Theme.class.getResource("/images/cassandra-logo.png")));
		map.put("ChiLogo", new ImageIcon(Theme.class.getResource("/images/chi-logo.png")));
	}
	
	public static ImageIcon getIcon (String key) {
		return map.get(key);
	}
}
