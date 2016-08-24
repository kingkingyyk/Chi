package Chi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataServerToDatabase {
	private static final String TIMESTAMP_FORMAT="yyyy-MM-dd HH:mm:ss"; //format the time
	private static SimpleDateFormat formatter=new SimpleDateFormat(DataServerToDatabase.TIMESTAMP_FORMAT);
	
	private static class Data {
		String sname;
		LocalDateTime timestamp;
		double reading;
		
		public String toString() {
			return this.sname+"|"+formatter.format(this.timestamp)+"|"+this.reading;
		}
	}
	//Thread safe queue.
	private static ConcurrentLinkedQueue<Data> queue=new ConcurrentLinkedQueue<>();

	public static void queueData(String sn, double r) {
		Data d=new Data();
		d.sname=sn;
		d.timestamp=LocalDateTime.now();
		d.reading=r;
		queue.add(d);
		Logger.log("Data Server - Queued write to database : "+d.toString());
		
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
			Data d=queue.poll();
			boolean success=DatabaseReading.storeReading(d.sname, d.timestamp, d.reading);
			if (!success) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}
		}
	}
}
