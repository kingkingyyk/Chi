package DataServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

import Chi.Logger;
import Database.Cache;
import Database.DatabaseReading;

public class DataServerReadingToDatabase {
	private static final String TIMESTAMP_FORMAT="yyyy-MM-dd HH:mm:ss"; //format the time
	private static DateTimeFormatter formatter=DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT);
	//Thread safe queue.
	public static ConcurrentLinkedQueue<Data> queue=new ConcurrentLinkedQueue<>();
	
	private static class WriteThread extends Thread {
		public void run() {
			while (true) {
				if (queue.peek()!=null) {
					DatabaseReading.storeReading();
				}
				try { Thread.sleep(500);} catch (InterruptedException e) {}
			}
		}
	}

	public static class Data {
		public String sname;
		public LocalDateTime timestamp;
		public double reading;
		
		public String toString() {
			return this.sname+"|"+this.timestamp.format(formatter)+"|"+this.reading;
		}
	}


	public static void queueData(String sn, double r) {
		Data d=new Data();
		d.sname=sn;
		d.timestamp=LocalDateTime.now();
		d.reading=r;
		queue.offer(d);
		Logger.log(Logger.LEVEL_INFO,"Data Server - Queued write reading to database : "+d.toString());
		
		DataServer.fireOnReadingReceived(sn,Cache.Sensors.map.get(sn).denormalizeValue(r));
	}
	
	public static void initialize() {
		new WriteThread().start();
	}
}
