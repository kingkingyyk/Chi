package NotificationServer;

public class NotificationServer {

	private static boolean started=false;
	
	public static void start () {
		if (!started) {
			NotificationServerControllerTracker.start();
			NotificationServerSensorTracker.start();
			started=true;
		}
	}
	
	public static void stop() {
		if (started) {
			NotificationServerControllerTracker.stop();
			NotificationServerSensorTracker.stop();
			started=false;
		}
	}
	
	public static boolean isStarted() {
		return started;
	}
}
