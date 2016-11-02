package Chi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.LinkedList;
import java.time.LocalDateTime;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import ControllerPacket.ControllerPacketActuatorTrigger;
import Database.Cache;
import Database.DatabaseDayScheduleRule;
import Database.DatabaseEvent;
import Database.DatabaseReading;
import Database.DatabaseRegularSchedule;
import Database.DatabaseSensorActuatorResponse;
import Database.DatabaseSpecialSchedule;
import Database.DatabaseUser;
import Database.DatabaseUserActuatorNotification;
import Database.DatabaseUserControllerNotification;
import Database.DatabaseUserSensorNotification;
import Entity.Actuator;
import Entity.Actuatorevent;
import Entity.Controller;
import Entity.Controllerevent;
import Entity.Dayschedulerule;
import Entity.Regularschedule;
import Entity.Sensor;
import Entity.SensorReading;
import Entity.Sensoractuatorresponse;
import Entity.Sensorevent;
import Entity.Site;
import Entity.Specialschedule;
import Entity.User;
import SchedulingServer.SchedulingData;
import SchedulingServer.SchedulingServer;
import ServerActuatorResponseServer.SensoractuatorresponseEvaluator;

public class GWTServer {
	private static GWTServerT t;
	
	public static Serializable processRequest (ArrayList<Object> list) {
		for (Object o : list) if (o==null) return null;
	    switch ((String)list.get(0)) {
		    case "0" : { //UserCheckNameExists
		    	return Cache.Users.map.containsKey(list.get(1));
		    }
		    case "1" : { //UserRegister
		    	return DatabaseUser.createUserCredential((String)list.get(1),(String)list.get(2),1,"PENDING APPROVAL");
		    }
		    case "2" : { //UserCheckCredentialOK
		    	User u=Cache.Users.map.get(list.get(1));
		    	if (u==null || !u.getPassword().equals(list.get(2))) {
		    		return "INVALID";
		    	} else {
		    		return Cache.Users.map.get(list.get(1)).getStatus();
		    	}
		    }
		    case "3" : { //SiteGetList
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Site s : Cache.Sites.map.values()) result.add(s.toObj());
		    	return result;
		    }
		    case "4" : { //SiteGetControllers
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Controller c : Cache.Controllers.map.values()) if (c.getSite().getSitename().equals(list.get(1))) result.add(c.toObj());
		    	return result;
		    }
		    case "5" : { //ControllerGetSensors
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Sensor s : Cache.Controllers.map.get(list.get(1)).getSensors()) result.add(s.toObj());
		    	return result;
		    }
		    case "6" : { //ControllerGetActuators
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Actuator act : Cache.Controllers.map.get(list.get(1)).getActuators()) result.add(Cache.Actuators.map.get(act.getName()).toObj());
			    return result;
		    }
		    case "7" : { //DayScheduleRuleGetAll
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Dayschedulerule r : Cache.DayScheduleRules.map.values()) result.add(r.toObj());	
		    	return result;
		    }
		    case "8" : { //DayScheduleRuleGetByName
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Object o : Cache.DayScheduleRules.map.get(list.get(1)).toObj()) result.add(o);
		    	return result;
		    }
		    case "9a" : { //DayScheduleRuleSet
		    	Dayschedulerule r=Cache.DayScheduleRules.map.get(list.get(1));
		    	if (r==null) return "RULE_NOT_EXIST";
		    	else if (!list.get(1).equals(list.get(2)) && Cache.DayScheduleRules.map.get(list.get(2))!=null) return "RULE_ALREADY_EXISTS";
		    	else {
		    		int sh=(int)list.get(3); int sm=(int)list.get(4);
		    		int eh=(int)list.get(5); int em=(int)list.get(6);
		    		boolean flag=DatabaseDayScheduleRule.updateDayScheduleRule((String)list.get(1),(String)list.get(2), sh, sm, eh, em);
		    		if (flag) return "OK";
		    		else return "ERROR";
		    	}
		    }
		    case "9b" : { //DayScheduleRuleDelete
		    	Dayschedulerule r=Cache.DayScheduleRules.map.get(list.get(1));
		    	if (r==null) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseDayScheduleRule.deleteDayScheduleRule((String)list.get(1));
		    		if (flag) return "OK";
		    		else return "ERROR";
		    	}
		    }
		    case "10" : { //DayScheduleRuleCreate
		    	Dayschedulerule r=Cache.DayScheduleRules.map.get(list.get(1));
		    	if (r!=null) return "RULE_ALREADY_EXISTS";
		    	else {
		    		int sh=(int)list.get(2); int sm=(int)list.get(3);
		    		int eh=(int)list.get(4); int em=(int)list.get(5);
		    		boolean flag=DatabaseDayScheduleRule.createDayScheduleRule((String)list.get(1), sh, sm, eh, em);
		    		if (flag) return "OK";
		    		else return "ERROR";
		    	}
		    }
		    case "11" : { //ActuatorGetRegularSchedules
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Regularschedule r : Cache.RegularSchedules.map.values()) {
		    		if (r.getActuator().getName().equals(list.get(1)))
		    			result.add(r.toObj());
		    	}
		    	return result;
		    }
		    case "12" : { //ActuatorGetSpecialSchedules
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Specialschedule ss : Cache.SpecialSchedules.map.values()) {
		    		if (ss.getActuator().getName().equals(list.get(1)))
		    			result.add(ss.toObj());
		    	}
		    	return result;
		    }
		    case "13" : { //RegularScheduleGetAll
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Regularschedule r : Cache.RegularSchedules.map.values()) {
		    		result.add(r.toObj());
		    	}
		    	return result;
		    }
		    case "14" : { //RegularScheduleByName
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Object o : Cache.RegularSchedules.map.get(list.get(1)).toObj()) {
		    		result.add(o);
		    	}
		    	return result;
		    }
		    case "15" : { //RegularScheduleUpdateName
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),(String)list.get(2),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "16" : { //RegularScheduleUpdateActuator
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),Cache.Actuators.map.get(list.get(2)).getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "17" : { //RegularScheduleUpdateDay
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if ((int)list.get(2)>=128) return "INVALID_DAY";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),(int)list.get(2),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "18" : { //RegularScheduleUpdateDayScheduleRule
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(2))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),Cache.DayScheduleRules.map.get(list.get(2)).getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "19a" : { //RegularScheduleSetOnStartAction
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),(String)list.get(2),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "19b" : { //RegularScheduleSetOnEndAction
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),(String)list.get(2),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "19c" : { //RegularScheduleSetLock
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),(boolean)list.get(2),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "20" : { //RegularSchedulePriority
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getOnendaction(),r.getOnendaction(),r.getLockmanual(),(int)list.get(2),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "21" : { //RegularScheduleEnabled
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),(boolean)(list.get(2)));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "22a" : { //RegularScheduleCreate
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r!=null) return "SCHEDULE_ALREADY_EXISTS";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	//else if ((int)(list.get(3))>=128) return "INVALID_DAY";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(4))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.createRegularSchedule( (String)list.get(1),Cache.Actuators.map.get(list.get(2)).getName(),
		    																	(int)list.get(3), Cache.DayScheduleRules.map.get(list.get(4)).getRulename(),
		    																	(String)list.get(5), (String)list.get(6), (boolean)list.get(7),
		    																	(int)list.get(8), (boolean)list.get(9));

		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "22b" : { //RegularScheduleDelete
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.deleteRegularSchedule((String)list.get(1));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "22c" : { //RegularScheduleUpdateFields
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!list.get(1).equals(list.get(2)) && Cache.RegularSchedules.map.containsKey(list.get(2))) return "SCHEDULE_ALREADY_EXISTS";
		    	else if (!Cache.Actuators.map.containsKey(list.get(3))) return "ACTUATOR_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(5))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule( (String)list.get(1),(String)list.get(2),Cache.Actuators.map.get(list.get(3)).getName(),
		    																	(int)list.get(4), Cache.DayScheduleRules.map.get(list.get(5)).getRulename(),
		    																	(String)list.get(6), (String)list.get(7), (boolean)list.get(8),
		    																	(int)list.get(9), (boolean)list.get(10));

		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "23" : { //SpecialScheduleGetAll
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Specialschedule r : Cache.SpecialSchedules.map.values()) {
		    		result.add(r.toObj());
		    	}
		    	return result;
		    }
		    case "24" : { //SpecialScheduleByName
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Object o : Cache.SpecialSchedules.map.get(list.get(1)).toObj()) {
		    		result.add(o);
		    	}
		    	return result;
		    }
		    case "25" : { //SpecialScheduleUpdateName
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),(String)list.get(2),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "26" : { //SpecialScheduleUpdateActuator
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),Cache.Actuators.map.get(list.get(2)).getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "27a" : { //SpecialScheduleUpdateDay
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),(int)(list.get(2)),(int)(list.get(3)),(int)(list.get(4)),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "28" : { //SpecialScheduleUpdateDayScheduleRule
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(2))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),Cache.DayScheduleRules.map.get(list.get(2)).getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "29a" : { //SpecialScheduleStartAction
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),(String)(list.get(2)),r.getOnendaction(),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "29b" : { //SpecialScheduleEndAction
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),(String)(list.get(2)),r.getLockmanual(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "29c" : { //SpecialScheduleLock
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),(boolean)(list.get(2)),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "30" : { //SpecialSchedulePriority
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),(int)(list.get(2)),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "31" : { //SpecialScheduleEnabled
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getOnstartaction(),r.getOnendaction(),r.getLockmanual(),r.getPriority(),(boolean)(list.get(2)));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "32a" : { //SpecialScheduleCreate
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r!=null) return "SCHEDULE_ALREADY_EXISTS";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(4))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.createSpecialSchedule( (String)list.get(1),Cache.Actuators.map.get(list.get(2)).getName(),
		    																	(int)list.get(3), (int)list.get(4), (int)list.get(5), 
		    																	Cache.DayScheduleRules.map.get(list.get(6)).getRulename(),
		    																	(String)list.get(7),(String)list.get(8),(boolean)list.get(9),
		    																	(int)list.get(10), (boolean)(list.get(11)));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "32b" : { //SpecialScheduleDelete
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.deleteSpecialSchedule((String)list.get(1));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "32c" : { //SpecialScheduleUpdateFields
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!list.get(1).equals(list.get(2)) && Cache.SpecialSchedules.map.containsKey(list.get(2))) return "SCHEDULE_ALREADY_EXISTS";
		    	else if (!Cache.Actuators.map.containsKey(list.get(3))) return "ACTUATOR_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(7))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule( (String)list.get(1),(String)list.get(2),Cache.Actuators.map.get(list.get(3)).getName(),
		    																	(int)list.get(4), (int)list.get(5), (int)list.get(6), 
		    																	Cache.DayScheduleRules.map.get(list.get(7)).getRulename(),
		    																	(String)list.get(8),(String)list.get(9),(boolean)list.get(10),
		    																	(int)list.get(11), (boolean)(list.get(12)));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "33": { //ScheduleServerIsStarted
		    	return SchedulingServer.isStarted;
		    }
		    case "34" : { //OngoingScheduleGetAll
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (SchedulingData d : SchedulingServer.getSchedulingThread().data.values()) {
		    		result.add(new Object [] {d.getName(),d.getActuatorName(),d.getStartAction(),d.getEndAction(),d.getLock(),d.getPriority(),d.getNextStartTime(),d.getNextEndTime()});
		    	}
		    	return result;
		    }
		    case "35" : { //OngoingScheduleGetByActuatorName
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (SchedulingData d : SchedulingServer.getSchedulingThread().data.values())
		    		if (d.getActuatorName().equals(list.get(1)))
		    			result.add(new Object [] {d.getName(),d.getActuatorName(),d.getStartAction(),d.getEndAction(),d.getLock(),d.getPriority(),d.getNextStartTime(),d.getNextEndTime()});
		    	return result;
		    }
		    case "36" : { //SensorGetByName
		    	ArrayList<Object []> result=new ArrayList<>();
		    	if (Cache.Sensors.map.containsKey(list.get(1))) result.add(Cache.Sensors.map.get(list.get(1)).toObj());
		    	return result;
		    }
		    case "37" : { //SensorGetReadingBetweenTime
		    	LinkedList<SensorReading> lsr=DatabaseReading.getReadingBetweenTime((String)list.get(1),(LocalDateTime)list.get(2),(LocalDateTime)list.get(3));
		    	ArrayList<Object []> result=new ArrayList<>(lsr.size());
		    	for (SensorReading sr : lsr) {
		    		result.add(new Object [] {sr.getTimestamp(),sr.getActualValue()});
		    	}
		    	return result;
		    }
		    case "38" : { //SensorGetReadingMonthly
		    	LinkedList<SensorReading> lsr=DatabaseReading.getReadingMonthly((String)list.get(1),(int)list.get(2),(int)list.get(3));
		    	ArrayList<Object []> result=new ArrayList<>(lsr.size());
		    	for (SensorReading sr : lsr) result.add(new Object [] {sr.getTimestamp(),sr.getActualValue()});
		    	return result;
		    }
		    case "39a" : { //ControllerEventGetByName
		    	ArrayList<Controllerevent> ceL=DatabaseEvent.getControllerEventByName((String)list.get(1));
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Controllerevent ce : ceL) result.add(new Object [] {ce.getController().getControllername(),Utility.dateToLocalDateTime(ce.getTimestp()),ce.getEventtype(),ce.getEventvalue()});
		    	return result;
		    }
		    case "39b" : { //ControllerEventGetBetweenTime
		    	ArrayList<Controllerevent> ceL=DatabaseEvent.getControllerEventBetweenTime((LocalDateTime)list.get(1),(LocalDateTime)list.get(2));
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Controllerevent ce : ceL) result.add(new Object [] {ce.getController().getControllername(),Utility.dateToLocalDateTime(ce.getTimestp()),ce.getEventtype(),ce.getEventvalue()});
		    	return result;
		    }
		    case "40a" : { //SensorEventGetByName
		    	ArrayList<Sensorevent> seL=DatabaseEvent.getSensorEventByName((String)list.get(1));
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Sensorevent se : seL) result.add(new Object [] {se.getSensor().getSensorname(),Utility.dateToLocalDateTime(se.getTimestp()),se.getEventtype(),se.getEventvalue()});
		    	return result;
		    }
		    case "40b" : { //SensorEventGetBetweenTime
		    	ArrayList<Sensorevent> seL=DatabaseEvent.getSensorEventBetweenTime((LocalDateTime)list.get(1),(LocalDateTime)list.get(2));
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Sensorevent se : seL) result.add(new Object [] {se.getSensor().getSensorname(),Utility.dateToLocalDateTime(se.getTimestp()),se.getEventtype(),se.getEventvalue()});
		    	return result;
		    }
		    case "41a" : { //ActuatorEventGetByName
		    	ArrayList<Actuatorevent> seL=DatabaseEvent.getActuatorEventByName((String)list.get(1));
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Actuatorevent se : seL) result.add(new Object [] {se.getActuator().getName(),Utility.dateToLocalDateTime(se.getTimestp()),se.getEventtype(),se.getEventvalue()});
		    	return result;
		    }
		    case "41b" : { //ActuatorEventGetBetweenTime
		    	ArrayList<Actuatorevent> seL=DatabaseEvent.getActuatorEventBetweenTime((LocalDateTime)list.get(1),(LocalDateTime)list.get(2));
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Actuatorevent se : seL) result.add(new Object [] {se.getActuator().getName(),Utility.dateToLocalDateTime(se.getTimestp()),se.getEventtype(),se.getEventvalue()});
		    	return result;
		    }
		    case "43" : { //ActuatorGetAll
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Actuator act : Cache.Actuators.map.values()) {
		    		result.add(act.toObj());
		    	}
		    	return result;
		    }
		    case "44": { //ActuatorGetByName
		    	Actuator act=Cache.Actuators.map.get(list.get(1));
		    	ArrayList<Object> result=new ArrayList<>();
		    	result.add(act.getName());
		    	result.add(act.getController().getControllername());
		    	result.add(act.getStatus());
		    	return result;
		    }
		    case "45" : { //ActuatorSetStatus;
		    	Actuator act=Cache.Actuators.map.getOrDefault(list.get(1),null);
		    	if (act!=null) {
		    		if (SchedulingServer.isActuatorLocked(act.getName())) return "ACTUATOR_LOCKED_BY_SCHEDULE";
		    		else {
		    			ControllerPacketActuatorTrigger p=new ControllerPacketActuatorTrigger(act.getController().getControllername(),act.getName(),(String)list.get(2));
		    			p.run();
			    		if (act.getStatus().equals(list.get(2))) return "OK";
			    		else return "FAIL";
		    		}
		    	} else return "ACTUATOR_NOT_EXIST";
		    }
		    case "46" : { //SensorActuatorResponseGetAll
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Sensoractuatorresponse sar : Cache.SensorActuatorResponses.map.values())
		    		result.add(sar.toObj());
		    	return result;
		    }
		    case "47" : { //SensorActuatorResponseGetByActuatorName
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Sensoractuatorresponse sar : Cache.SensorActuatorResponses.map.values()) if (sar.getActuator().getName().equals(list.get(1)))
		    		for (Object o : sar.toObj()) result.add(o);
		    	return result;
		    }
		    case "48" : { //SensorActuatorResponseCreate
		    	boolean expOK=true;
		    	try { SensoractuatorresponseEvaluator.evaluateStatement((String)list.get(4)); } catch (Exception e) {expOK=false;}
		    	if (!expOK) return "EXPRESSION_ERROR";
		    	if (Cache.Actuators.map.get(list.get(1)).getSensoractuatorresponses().size()>0) return "ACTUATOR_ALREADY_USED";
		    	boolean flag=DatabaseSensorActuatorResponse.createSensorActuatorResponse((String)list.get(1),(String)list.get(2),(String)list.get(3),
		    																			 (String)list.get(4),(Boolean)list.get(5),(Integer)list.get(6));
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "49" : { //SensorActuatorResponseSetOnTriggerAction
		    	Sensoractuatorresponse sar=Cache.SensorActuatorResponses.map.get(String.valueOf((int)list.get(1)));
		    	if (sar==null) return "RESPONSE_NOT_EXIST";
		    	boolean flag=DatabaseSensorActuatorResponse.updateSensorActuatorResponse(sar.getId(),sar.getActuator().getName(),(String)list.get(2),
		    																			 sar.getOnnottriggeraction(),sar.getExpression(),sar.getEnabled(),
		    																			 sar.getTimeout());
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "50" : { //SensorActuatorResponseSetOnNotTriggerAction
		    	Sensoractuatorresponse sar=Cache.SensorActuatorResponses.map.get(String.valueOf((int)list.get(1)));
		    	if (sar==null) return "RESPONSE_NOT_EXIST";
		    	boolean flag=DatabaseSensorActuatorResponse.updateSensorActuatorResponse(sar.getId(),sar.getActuator().getName(),sar.getOntriggeraction(),
		    																			(String)list.get(2),sar.getExpression(),sar.getEnabled(),
		    																			 sar.getTimeout());
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "51" : { //SensorActuatorResponseSetExpression
		    	boolean expOK=true;
		    	try { SensoractuatorresponseEvaluator.evaluateStatement((String)list.get(2)); } catch (Exception e) {expOK=false;}
		    	if (!expOK) return "EXPRESSION_ERROR";
		    	Sensoractuatorresponse sar=Cache.SensorActuatorResponses.map.get(String.valueOf((int)list.get(1)));
		    	if (sar==null) return "RESPONSE_NOT_EXIST";
		    	boolean flag=DatabaseSensorActuatorResponse.updateSensorActuatorResponse(sar.getId(),sar.getActuator().getName(),sar.getOntriggeraction(),
		    																			 sar.getOnnottriggeraction(),(String)list.get(2),sar.getEnabled(),
		    																			 sar.getTimeout());
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "52" : { //SensorActuatorResponseSetEnabled
		    	Sensoractuatorresponse sar=Cache.SensorActuatorResponses.map.get(String.valueOf((int)list.get(1)));
		    	if (sar==null) return "RESPONSE_NOT_EXIST";
		    	boolean flag=DatabaseSensorActuatorResponse.updateSensorActuatorResponse(sar.getId(),sar.getActuator().getName(),sar.getOntriggeraction(),
		    			 																 sar.getOnnottriggeraction(),sar.getExpression(),(boolean)list.get(2),
		    																			 sar.getTimeout());
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "53" : { //SensorActuatorResponseSetTimeout
		    	Sensoractuatorresponse sar=Cache.SensorActuatorResponses.map.get(String.valueOf((int)list.get(1)));
		    	if (sar==null) return "RESPONSE_NOT_EXIST";
		    	int value=(int)list.get(2); if (value<5) return "INVALID_TIME";
		    	boolean flag=DatabaseSensorActuatorResponse.updateSensorActuatorResponse(sar.getId(),sar.getActuator().getName(),sar.getOntriggeraction(),
		    																			 sar.getOnnottriggeraction(),sar.getExpression(),sar.getEnabled(),
		    																			 value);
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "54" : { //User subscribe controller notification
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Controllers.map.containsKey(sname)) return "CONTROLLER_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserControllerNotification.subscribeControllerNotification(uname,sname);
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "55" : { //User controller notification update last read time
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Controllers.map.containsKey(sname)) return "CONTROLLER_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserControllerNotification.updateControllerNotification(uname,sname,Utility.localDateTimeToUtilDate((LocalDateTime)list.get(3)));
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "56" : { //User unsubscribe controller notification
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Controllers.map.containsKey(sname)) return "SENSOR_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserControllerNotification.unsubscribeControllerNotification(uname,sname);
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "57" : { //User controller notification get last read time
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	return DatabaseUserControllerNotification.getControllerNotificationTime(uname,sname);
		    }
		    
		    case "58" : { //User subscribe sensor notification
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Sensors.map.containsKey(sname)) return "SENSOR_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserSensorNotification.subscribeSensorNotification(uname,sname);
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "59" : { //User sensor notification update last read time
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Sensors.map.containsKey(sname)) return "SENSOR_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserSensorNotification.updateSensorNotification(uname,sname,Utility.localDateTimeToUtilDate((LocalDateTime)list.get(3)));
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "60" : { //User unsubscribe sensor notification
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Sensors.map.containsKey(sname)) return "SENSOR_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserSensorNotification.unsubscribeSensorNotification(uname,sname);
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "61" : { //User sensor notification get last read time
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	return DatabaseUserSensorNotification.getSensorNotificationTime(uname,sname);
		    }
		    case "62" : { //User subscribe actuator notification
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Actuators.map.containsKey(sname)) return "ACTUATOR_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserActuatorNotification.subscribeActuatorNotification(uname,sname);
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "63" : { //User actuator notification update last read time
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Actuators.map.containsKey(sname)) return "ACTUATOR_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserActuatorNotification.updateActuatorNotification(uname,sname,Utility.localDateTimeToUtilDate((LocalDateTime)list.get(3)));
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "64" : { //User unsubscribe actuator notification
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	if (Cache.Users.map.containsKey(uname)) return "USER_NOT_EXIST";
		    	else if (Cache.Actuators.map.containsKey(sname)) return "ACTUATOR_NOT_EXIST";
		    	
		    	boolean flag=DatabaseUserActuatorNotification.unsubscribeActuatorNotification(uname,sname);
		    	if (flag) return "OK"; else return "ERROR";
		    }
		    case "65" : { //User actuator notification get last read time
		    	String uname=(String)list.get(1); String sname=(String)list.get(2);
		    	return DatabaseUserActuatorNotification.getActuatorNotificationTime(uname,sname);
		    }
	    }
	    return null;
	}
	
	private static class GWTServerT extends Thread {
		boolean stopFlag=false;
	}

	private static class GWTSecureServerThread extends GWTServerT {
		public ServerSocket serverSocket;
		public void run () {
			try {
		        final byte[] salt = "chichilol".getBytes();
		        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		        KeySpec spec = new PBEKeySpec(Config.getConfig(Config.CONFIG_SERVER_GWT_PASSWORD_KEY).toCharArray(), salt, 1024, 128);
		        SecretKey secret=new SecretKeySpec(factory.generateSecret(spec).getEncoded(),"AES");
		        Cipher encrypter=Cipher.getInstance("AES");
		        encrypter.init(Cipher.ENCRYPT_MODE,secret);
		        Cipher decrypter=Cipher.getInstance("AES");
		        decrypter.init(Cipher.DECRYPT_MODE,secret);
		        
				serverSocket=new ServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
			    serverSocket.setSoTimeout(0);
				while (!stopFlag && !Thread.currentThread().isInterrupted()) {
				    final Socket socket=(Socket) serverSocket.accept();
					Thread t=new Thread() {
						@SuppressWarnings("unchecked")
						public void run () {
							try {
							    InputStream is=socket.getInputStream();
							    ObjectInputStream ois=new ObjectInputStream(is);
							    
							    OutputStream os=socket.getOutputStream();
							    ObjectOutputStream oos=new ObjectOutputStream(os);
							    
								Object o=ois.readObject();
								if (o instanceof SealedObject) {
									Object decrypted=(((SealedObject) o).getObject(decrypter));
									if (decrypted instanceof ArrayList) {
										oos.writeObject(new SealedObject(GWTServer.processRequest((ArrayList<Object>)decrypted),encrypter));
									}
								}
						    	oos.close(); os.close();
						    	is.close(); ois.close();
							    socket.close(); 
							} catch (Exception e) {};
						}
					};
					t.start();
				}
				serverSocket.close();
			} catch (Exception e) {
				if (serverSocket!=null) try {serverSocket.close();} catch (IOException ioe) {}
				e.printStackTrace();
			}
		}
	}
	
	private static class GWTServerThread extends GWTServerT {
		public ServerSocket serverSocket;
		
		@SuppressWarnings("unchecked")
		public void run () {
			try {
				serverSocket=new ServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
				serverSocket.setSoTimeout(0);
				while (!stopFlag && !Thread.currentThread().isInterrupted()) {
					final Socket socket=(Socket) serverSocket.accept();
					Thread t=new Thread() {
						public void run() {
							try {
								InputStream is=socket.getInputStream();
								ObjectInputStream ois=new ObjectInputStream(is);
								    
								OutputStream os=socket.getOutputStream();
								ObjectOutputStream oos=new ObjectOutputStream(os);
								    
								Object o=ois.readObject();
								if (o instanceof ArrayList) {
									oos.writeObject(GWTServer.processRequest((ArrayList<Object>)o));
								}
						    	oos.close(); os.close();
						    	is.close(); ois.close();
							    socket.close(); 
							} catch (Exception e) {}
						}
					};
					t.run();
				}
				serverSocket.close();
			} catch (Exception exp) {};
		}
	}
	
	public static boolean start() {
		if (t==null) {
			if (Boolean.parseBoolean(Config.getConfig(Config.CONFIG_SERVER_GWT_ENCRYPTION_KEY))) {
				Logger.log(Logger.LEVEL_INFO,"GWTSecureServer - Starting");
				t=new GWTSecureServerThread();
				t.start();
			} else {
				Logger.log(Logger.LEVEL_INFO,"GWTServer - Starting");
				t=new GWTServerThread();
				t.start();
			}
			return true;
		}
		return false;
	}
	
	public static boolean stop() {
		if (t!=null) {
			Logger.log(Logger.LEVEL_INFO,"GWTSecureServer - Requested to stop");
			t.stopFlag=true;
			if (t instanceof GWTSecureServerThread) {
				try {
					Socket sc = new Socket("127.0.0.1",Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
			    	sc.getOutputStream().write(new byte [] {1});
			    	sc.getOutputStream().close();
			    	sc.close();
				} catch (Exception e) {e.printStackTrace();}
			} else {
				try {
					Socket sc = new Socket("127.0.0.1",Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
			    	sc.getOutputStream().write(new byte [] {1});
			    	sc.getOutputStream().close();
			    	sc.close();
				} catch (Exception e) {e.printStackTrace();}
			}
			t=null;
			return true;
		}
		return false;
	}
}
