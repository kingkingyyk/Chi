package Chi;

import java.util.HashMap;
import java.util.Map;

import org.mvel.MVEL;

import Database.DatabaseReading;
import Entity.Sensor;

public class SensoractuatorresponseEvaluator {
	
	public static boolean evaluateStatement (String s) {
		Map<String,Double> context = new HashMap<>();
		for (Sensor sr : DatabaseReading.SensorLastReading.keySet()) {
			context.put(sr.getSensorname(),DatabaseReading.SensorLastReading.get(sr));
		}
		return (boolean) MVEL.eval(s,context);
	}
	
}
