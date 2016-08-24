package Chi;

public class SchedulingServer {
	private static SchedulingThread scheduleThread;
	private static boolean isStarted=false;
	
	public static void start() {
		Logger.log("Attempting to start scheduling server...");
		scheduleThread=new SchedulingThread();
		scheduleThread.start();
		isStarted=true;
		Logger.log("Scheduling server started.");
	}
	
	public static void stop() {
		Logger.log("Attempting to stop scheduling server...");
		scheduleThread.requestStop();
		int trials=1;
		for (;isStarted && trials<=5;trials++) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {}
		}
		if (isStarted) {
			Logger.log("Scheduling server failed to stop after 5 trials...");
		} else {
			Logger.log("Scheduling server stopped.");
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
}
