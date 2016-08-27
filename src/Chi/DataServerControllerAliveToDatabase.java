package Chi;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DataServerControllerAliveToDatabase {
	private static final String TIMESTAMP_FORMAT="yyyy-MM-dd HH:mm:ss"; //format the time
	private static SimpleDateFormat formatter=new SimpleDateFormat(DataServerControllerAliveToDatabase.TIMESTAMP_FORMAT);
	
	private static class Data {
		String cname;
		LocalDateTime timestamp;

		public String toString() {
			return this.cname+"|"+formatter.format(this.timestamp);
		}
	}
	//Thread safe queue.
	private static ConcurrentLinkedQueue<Data> queue=new ConcurrentLinkedQueue<>();

	public static void queueData(String sn) {
		Data d=new Data();
		d.cname=sn;
		d.timestamp=LocalDateTime.now();
		queue.add(d);
		Logger.log("Data Server - Queued write report time to database : "+d.toString());
		
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
			boolean success=DatabaseController.updateControllerReportTime(d.cname,d.timestamp);
			if (!success) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}
		}
	}
}
