package Database;

import java.util.ArrayList;
import Entity.Sensoractuatorresponse;

public class CacheRelationshipBind {

	public static class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String n) {
			for (Sensoractuatorresponse s : Cache.SensorActuatorResponses.map.values())
				if (s.getActuator().getName().equals(n))
					Cache.SensorActuatorResponses.map.remove(String.valueOf(s.getId()));
		}
	}
	
	public static class OnSensorUpdate implements DatabaseSensor.OnUpdateAction {
		private static String [] operators={""," ","|","&",">","<","=","(",")"};
		public void run (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con, double minT, double maxT, double px, double py) {
			if (!oldSN.equals(sn)) {
				ArrayList<String> candidates=new ArrayList<>();
				ArrayList<String> replacement=new ArrayList<>();
				
				for (int i=0;i<operators.length;i++) {
					for (int i2=1;i2<operators.length;i2++) {
						candidates.add(operators[i]+oldSN+operators[i2]);
						replacement.add(operators[i]+sn+operators[i2]);
					}
				}
				
				for (Sensoractuatorresponse s : Cache.SensorActuatorResponses.map.values()) {
					for (int i=0;i<candidates.size();i++) {
						String str=candidates.get(i);
						if (s.getExpression().contains(str))
							DatabaseSensorActuatorResponse.updateSensorActuatorResponse(s.getId(),s.getActuator().getName(),
																						s.getOntriggeraction(),s.getOnnottriggeraction(),
																						s.getExpression().replace(str,replacement.get(i)),s.getEnabled(),
																						s.getTimeout());
					}
				}
			}
		}
	}
	public static class OnSensorDelete implements DatabaseSensor.OnDeleteAction {
		private static String [] operators={""," ","|","&",">","<","=","(",")"};
		public void run (String sn) {
			ArrayList<String> candidates=new ArrayList<>();
			
			for (int i=0;i<operators.length;i++) {
				for (int i2=1;i2<operators.length;i2++) {
					candidates.add(operators[i]+sn+operators[i2]);
				}
			}
			
			for (Sensoractuatorresponse s : Cache.SensorActuatorResponses.map.values()) {
				for (int i=0;i<candidates.size();i++) {
					String str=candidates.get(i);
					if (s.getExpression().contains(str))
						DatabaseSensorActuatorResponse.updateSensorActuatorResponse(s.getId(),s.getActuator().getName(),
																					s.getOntriggeraction(),s.getOnnottriggeraction(),
																					s.getExpression(),false,
																					s.getTimeout());
				}
			}
		}
	}
	
	public static void initialize() {
		DatabaseSensor.registerOnUpdateAction(new OnSensorUpdate());
		DatabaseSensor.registerOnDeleteAction(new OnSensorDelete());
		DatabaseActuator.registerOnDeleteAction(new OnActuatorDelete());
	}
}
