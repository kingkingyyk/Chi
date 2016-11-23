package NotificationServer;

import java.util.HashMap;

import DataServer.DataServer;
import Database.Cache;
import Database.DatabaseEvent;
import Database.DatabaseSensor;
import Entity.Sensor;

public class NotificationServerSensorTracker {
	private static HashMap<String,Integer> lastSensorStatus=new HashMap<>();
	
	private static class OnSensorUpdate implements DatabaseSensor.OnUpdateAction {

		@Override
		public void run(String oldSN, String sn, String cn, double min, double max, double trans, String unit,
				String con, double minT, double maxT, double px, double py) {
			if (lastSensorStatus.containsKey(oldSN)) {
				lastSensorStatus.put(sn, lastSensorStatus.get(oldSN));
				lastSensorStatus.remove(oldSN);
			}
		}
		
	}
	
	private static class OnSensorRemove implements DatabaseSensor.OnDeleteAction {

		@Override
		public void run(String sn) {
			lastSensorStatus.remove(sn);
		}
		
	}
	
	//1 - Lower than min threshold, 2 - Normal, 3 - Higher than max threshold;
	private static class OnSensorReadingReceived implements DataServer.OnReadingReceived {
		public void run (String sensorName, double value) {
			Sensor s=Cache.Sensors.map.get(sensorName);
			if (s!=null) {
				int currState=2;
				if (value<s.getMinthreshold()) currState=1;
				else if (value>s.getMaxthreshold()) currState=3;
				if (currState!=lastSensorStatus.getOrDefault(sensorName,2)) {
					if (currState==1) DatabaseEvent.logSensorEvent(sensorName,"Lower than minimum threshold",String.valueOf(value));
					if (currState==3) DatabaseEvent.logSensorEvent(sensorName,"Higher than maximum threshold",String.valueOf(value));
				}
				lastSensorStatus.put(sensorName,currState);
			}
		}
	}
	
	private static DatabaseSensor.OnUpdateAction sensorUpdated=new OnSensorUpdate();
	private static DatabaseSensor.OnDeleteAction sensorRemoved=new OnSensorRemove();
	private static OnSensorReadingReceived readingReceived=new OnSensorReadingReceived();
	
	public static void start () {
		DatabaseSensor.registerOnUpdateAction(sensorUpdated);
		DatabaseSensor.registerOnDeleteAction(sensorRemoved);
		DataServer.registerOnReadingReceived(readingReceived);
	}
	
	public static void stop() {
		DatabaseSensor.unregisterOnUpdateAction(sensorUpdated);
		DatabaseSensor.unregisterOnDeleteAction(sensorRemoved);
		DataServer.unregisterOnReadingReceived(readingReceived);
		lastSensorStatus.clear();
	}
}
