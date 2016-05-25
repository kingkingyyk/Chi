package Chi;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerToDatabase {
	private static final String TIMESTAMP_FORMAT="yyyy-MM-dd HH:mm:ss"; //format the time
	private static SimpleDateFormat formatter=new SimpleDateFormat(ServerToDatabase.TIMESTAMP_FORMAT);
	
	private static class Data {
		String cname;
		String sname;
		Date timestamp;
		double reading;
		
		public String toString() {
			return this.cname+"|"+this.sname+"|"+formatter.format(this.timestamp)+"|"+this.reading;
		}
	}
	//Thread safe queue.
	private static ConcurrentLinkedQueue<Data> queue=new ConcurrentLinkedQueue<>();

	public static void queueData(String cn, String sn, double r) {
		Data d=new Data();
		d.cname=cn;
		d.sname=sn;
		d.timestamp=new Date();
		d.reading=r;
		queue.add(d);
		Logger.log("Database Writer - Queued Data : "+d.toString());
		
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
			boolean success=Database.storeReading(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY), Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_PORT_KEY)), d.cname, d.sname, d.timestamp, d.reading);
			if (!success) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
			}
		}
	}
}
