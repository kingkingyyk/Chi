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
	private static boolean EnableLogToFile=false;
	private static ConcurrentLinkedQueue<String> eventQueue=new ConcurrentLinkedQueue<>();
	
	public static void log (String event) {
		eventQueue.offer(formatter.format(new Date())+" "+event);
		if (eventQueue.size()==1) {
			Thread t=new Thread() {
				public void run () {
					try {
						PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(LogFile,true)));
						while (eventQueue.size()>0) {
							String s=eventQueue.poll();
							System.out.println(s);
							if (EnableLogToFile) pw.println(s);
						}
						pw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
		}
	}
	
	public static void initialize () {
		File fol=new File(LOG_FOLDER_PATH);
		if (!fol.exists()) {
			fol.mkdir();
		}
		LogFile=new File(LOG_FOLDER_PATH+"/Server-"+logFileNameFormatter.format(new Date())+".log");
	}
	
}
