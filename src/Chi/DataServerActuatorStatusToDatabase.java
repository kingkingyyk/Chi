package Chi;

import java.util.concurrent.ConcurrentLinkedQueue;

import Database.DatabaseActuator;

public class DataServerActuatorStatusToDatabase {
	
	private static class Data {
		String cname;
		String st;

		public String toString() {
			return this.cname+"|"+this.st;
		}
	}
	//Thread safe queue.
	private static ConcurrentLinkedQueue<Data> queue=new ConcurrentLinkedQueue<>();

	public static void queueData(String sn, String st) {
		Data d=new Data();
		d.cname=sn;
		d.st=st;
		queue.add(d);
		Logger.log(Logger.LEVEL_INFO,"Data Server - Queued write actuator status to database : "+d.toString());
		
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
			boolean success=DatabaseActuator.updateActuatorStatus(d.cname,d.st);
			if (!success) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {}
			}
		}
	}
}
