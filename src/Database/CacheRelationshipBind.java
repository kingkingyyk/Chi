package Database;

import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;

import Entity.Regularschedule;
import Entity.Sensoractuatorresponse;
import Entity.Specialschedule;

public class CacheRelationshipBind {

	public static class OnActuatorUpdate implements DatabaseActuator.OnUpdateAction {
		@Override
		public void run(String oldN, String n, String u, String slist, double px, double py, String ctrlType) {
			//Disable schedules if OnStart/OnEnd action not match
			String [] statuses=slist.split(";");
			for (Regularschedule rs : Cache.RegularSchedules.map.values())
				if (rs.getActuator().getName().equals(n) && rs.getEnabled())
					if ((!rs.getOnstartaction().equals("NOTHING") && !ArrayUtils.contains(statuses,rs.getOnstartaction())) || (!rs.getOnendaction().equals("NOTHING") && !ArrayUtils.contains(statuses,rs.getOnendaction())))
						DatabaseRegularSchedule.updateRegularSchedule(rs.getSchedulename(),rs.getSchedulename(),rs.getActuator().getName(),
																	  rs.getDaymask(),rs.getDayschedulerule().getRulename(),rs.getOnstartaction(),
																	  rs.getOnendaction(),rs.getLockmanual(),rs.getPriority(),false);

			for (Specialschedule ss : Cache.SpecialSchedules.map.values())
					if (ss.getActuator().getName().equals(n) && ss.getEnabled())
						if ((!ss.getOnstartaction().equals("NOTHING") && !ArrayUtils.contains(statuses,ss.getOnstartaction())) || (!ss.getOnendaction().equals("NOTHING") && !ArrayUtils.contains(statuses,ss.getOnendaction())))
							DatabaseSpecialSchedule.updateSpecialSchedule(ss.getSchedulename(),ss.getSchedulename(),ss.getActuator().getName(),
																			ss.getYear(), ss.getMonth(), ss.getDay(), ss.getDayschedulerule().getRulename(),
																			ss.getOnstartaction(), ss.getOnendaction(), ss.getLockmanual(), ss.getPriority(), false);
		}
	}
	
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
		DatabaseActuator.registerOnUpdateAction(new OnActuatorUpdate());
		DatabaseActuator.registerOnDeleteAction(new OnActuatorDelete());
	}
}
