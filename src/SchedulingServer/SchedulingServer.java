package SchedulingServer;

import Chi.Logger;
import Chi.WaitUI;

public class SchedulingServer {
	private static SchedulingThread scheduleThread;
	public static boolean isStarted=false;
	
	public static boolean start() {
		Logger.log(Logger.LEVEL_INFO,"Attempting to start scheduling server...");
		scheduleThread=new SchedulingThread();
		scheduleThread.start();
		final WaitUI u=new WaitUI();
		u.setText("Starting...");
		Thread t=new Thread() {
			public void run () {
				int trials=1;
				for (;!isStarted && trials<=10;trials++) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {}
				}
				u.setVisible(false);
			}
		};
		t.start();
		u.setVisible(true);

		if (isStarted) {
			Logger.log(Logger.LEVEL_INFO,"Scheduling server started.");
			return true;
		} else {
			Logger.log(Logger.LEVEL_INFO,"Scheduling server failed to start");
			return false;
		}
	}
	
	public static void stop() {
		if (scheduleThread!=null) {
			Logger.log(Logger.LEVEL_INFO,"Attempting to stop scheduling server...");
			scheduleThread.requestStop();
			int trials=1;
			for (;isStarted && trials<=5;trials++) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
			if (isStarted) {
				Logger.log(Logger.LEVEL_ERROR,"Scheduling server failed to stop after 5 trials...");
			} else {
				Logger.log(Logger.LEVEL_INFO,"Scheduling server stopped.");
				scheduleThread=null;
			}
		}
	}
	
	public static SchedulingThread getSchedulingThread () {
		return scheduleThread;
	}
	
	public static boolean started() {
		return SchedulingServer.isStarted;
	}
	
	public static void notifyStop() {
		isStarted=false;
	}
	
	public static boolean isActuatorLocked (String aname) {
		SchedulingData highestPrio=null;
		if (started() && scheduleThread.data!=null) {
			for (SchedulingData dat : scheduleThread.data.values()) if (dat.actuatorName.equals(aname) && (highestPrio==null || dat.priority>highestPrio.priority))
				highestPrio=dat;
		}
		return (highestPrio!=null && highestPrio.lock);
	}
}
