package Chi;

public class DatabaseSensorReadingBind {
	
	public static class OnSensorUpdate implements DatabaseSensor.OnUpdateAction {
		public void run (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
			if (!oldSN.equals(sn)) DatabaseReading.updateSensorName(oldSN,sn);
		}
	}
	
	public static class OnSensorDelete implements DatabaseSensor.OnDeleteAction {
		public void run (String sn) {
			DatabaseReading.clearReading(sn);
		}
	}
	
	public static void initialize() {
		DatabaseSensor.registerOnUpdateAction(new OnSensorUpdate());
		DatabaseSensor.registerOnDeleteAction(new OnSensorDelete());
	}
}
