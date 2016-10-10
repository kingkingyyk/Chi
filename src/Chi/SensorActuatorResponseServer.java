package Chi;

public class SensorActuatorResponseServer {
	private static boolean isStarted=false;
	
	public static void start() {
		isStarted=true;
	}
	
	public static void stop() {
		isStarted=false;
	}
	
	public static boolean isStarted() {
		return isStarted;
	}
}
