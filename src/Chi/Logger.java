package Chi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {

	private static final String LOG_FOLDER_PATH="log";
	private static SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	private static SimpleDateFormat logFileNameFormatter=new SimpleDateFormat("yyyy.MM.dd-HH.mm.ss");
	private static File LogFile=null;
	public static boolean EnableLogToFile=false;
	private static ConcurrentLinkedQueue<Log> eventQueue=new ConcurrentLinkedQueue<>();
	private static String [] LEVEL_TEXT={"INFO","WARNING","ERROR"};
	public static int LEVEL_ERROR=2;
	public static int LEVEL_WARNING=1;
	public static int LEVEL_INFO=0;
	public static int LOG_LEVEL=LEVEL_INFO;
	
	private static class Log {
		@SuppressWarnings("unused")
		int level;
		String s;
		
		public Log (int l, String str) {
			this.level=l;
			this.s=str;
		}
	}
	
	public static void log (int logLevel, String event) {
		if (logLevel>=LOG_LEVEL) {
			StringBuilder sb=new StringBuilder();
			sb.append(LEVEL_TEXT[logLevel]);
			sb.append(" | ");
			sb.append(formatter.format(new Date()));
			sb.append(" | ");
			sb.append(event);
			Log l=new Log (logLevel,sb.toString());
			eventQueue.offer(l);
			if (eventQueue.size()==1) {
				Thread t=new Thread() {
					public void run () {
						try {
							while (eventQueue.size()>0) {
								Log l=eventQueue.poll();
								System.out.println(l.s);
								if (MenuUI.getCurrInstance()!=null) {
									MenuUI.getCurrInstance().appendLog(l.s);
								}
								if (EnableLogToFile) {
									PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile,true)));
									pw.println(l.s);
									pw.close();
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				t.start();
			}
		}
	}
	
	public static void initialize () {
		File fol=new File(LOG_FOLDER_PATH);
		if (!fol.exists()) {
			fol.mkdir();
		}
		LogFile=new File(LOG_FOLDER_PATH+"/Server-"+logFileNameFormatter.format(new Date())+".log");
	}
	
	public static void refreshLogLevel () {
		LOG_LEVEL=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_LOGGING_LEVEL_KEY));
	}
}
