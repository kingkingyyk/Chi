package Chi;

public class NotificationServer {

	public static void start () {
		NotificationServerControllerTracker.start();
		NotificationServerSensorTracker.start();
	}
	
	public static void stop() {
		NotificationServerControllerTracker.stop();
		NotificationServerSensorTracker.stop();
	}
	
}
