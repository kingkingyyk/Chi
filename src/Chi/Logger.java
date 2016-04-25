package Chi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private static final String LOG_FOLDER_PATH="log";
	private static final String LOG_TIME_FORMAT="yyyy/MM/dd HH:mm:ss.SSS";
	private static SimpleDateFormat formatter=new SimpleDateFormat(LOG_TIME_FORMAT);
	
	public static void log (String event) {
		System.out.println(formatter.format(new Date())+" "+event);
	}
	
}
