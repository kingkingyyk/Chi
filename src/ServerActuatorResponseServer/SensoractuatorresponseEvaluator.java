package ServerActuatorResponseServer;

import java.util.HashMap;
import java.util.Map;
import org.mvel.MVEL;
import Database.Cache;
import Database.DatabaseReading;
import Entity.Sensor;

public class SensoractuatorresponseEvaluator {
	private static String [] operators={""," ","|","&",">","<","=","(",")"};
	
	public static boolean containsSensor (String s) {
		boolean found=false;
		
		for (Sensor sn : Cache.Sensors.map.values()) {
			for (int i=0;i<operators.length && !found;i++) {
				for (int i2=1;i2<operators.length && !found;i2++) {
					found=s.contains(operators[i]+sn.getSensorname()+operators[i2]);
				}
			}
			if (found) return true;
		}

		return found;
	}
	
	public static boolean evaluateStatement (String s) {
		if (!containsSensor(s)) return false;
		Map<String,Double> context = new HashMap<>();
		for (String name : DatabaseReading.SensorLastReading.keySet()) context.put(name,DatabaseReading.SensorLastReading.get(name));
		return (boolean) MVEL.eval(s,context);
	}
	
}
