package Chi;

public class SchedulingServer {
	private static SchedulingThread scheduleThread;
	public static boolean isStarted=false;
	
	public static boolean start() {
		Logger.log("Attempting to start scheduling server...");
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
			Logger.log("Scheduling server started.");
			return true;
		} else {
			Logger.log("Scheduling server failed to start");
			return false;
		}
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
