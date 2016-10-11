package Chi;

public class CacheRelationshipBind {

	public static class OnActuatorDelete implements DatabaseActuator.OnDeleteAction {
		public void run (String n) {
			for (Sensoractuatorresponse s : Cache.SensorActuatorResponses.map.values())
				if (s.getActuator().getName().equals(n))
					Cache.SensorActuatorResponses.map.remove(String.valueOf(s.getId()));
		}
	}
	
	public static void initialize() {
		DatabaseActuator.registerOnDeleteAction(new OnActuatorDelete());
	}
}
