package NotificationServer;

import java.util.HashMap;

import DataServer.DataServer;
import Database.Cache;
import Database.DatabaseEvent;
import Entity.Sensor;

public class NotificationServerSensorTracker {
	private static HashMap<Sensor,Integer> lastSensorStatus=new HashMap<>();
	//1 - Lower than min threshold, 2 - Normal, 3 - Higher than max threshold;
	
	private static class OnSensorReadingReceived implements DataServer.OnReadingReceived {
		public void run (String sensorName, double value) {
			Sensor s=Cache.Sensors.map.get(sensorName);
			if (s!=null) {
				int currState=2;
				if (value<s.getMinthreshold()) currState=1;
				else if (value>s.getMaxthreshold()) currState=3;
				if (currState!=lastSensorStatus.getOrDefault(s,2)) {
					if (currState==1) DatabaseEvent.logSensorEvent(sensorName,"Lower than minimum threshold",String.valueOf(value));
					if (currState==3) DatabaseEvent.logSensorEvent(sensorName,"Higher than maximum threshold",String.valueOf(value));
					lastSensorStatus.put(s,currState);
				}
			}
		}
	}
	
	private static OnSensorReadingReceived readingReceived=new OnSensorReadingReceived();
	
	public static void start () {
		DataServer.registerOnReadingReceived(readingReceived);
	}
	
	public static void stop() {
		DataServer.unregisterOnReadingReceived(readingReceived);
		lastSensorStatus.clear();
	}
}
