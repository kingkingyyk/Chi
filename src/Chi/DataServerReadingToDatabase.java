package Chi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataServerReadingToDatabase {
	private static final String TIMESTAMP_FORMAT="yyyy-MM-dd HH:mm:ss"; //format the time
	private static DateTimeFormatter formatter=DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
	
	public static class Data {
		String sname;
		LocalDateTime timestamp;
		double reading;
		
		public String toString() {
			return this.sname+"|"+this.timestamp.format(formatter)+"|"+this.reading;
		}
	}
	//Thread safe queue.
	public static ConcurrentLinkedQueue<Data> queue=new ConcurrentLinkedQueue<>();

	public static void queueData(String sn, double r) {
		Data d=new Data();
		d.sname=sn;
		d.timestamp=LocalDateTime.now();
		d.reading=r;
		queue.add(d);
		Logger.log("Data Server - Queued write reading to database : "+d.toString());
		
		if (queue.size()==1) {
			Thread t=new Thread() {
				public void run() {
					writeToDatabase();
				}
			};
			t.start();
		}
	}
	
	private static void writeToDatabase() {
		while (queue.size()>0) {
			DatabaseReading.storeReading();
			try { Thread.sleep(500);} catch (InterruptedException e) {}
		}
	}
}
