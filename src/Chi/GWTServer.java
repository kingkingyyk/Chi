package Chi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class GWTServer {
	private static GWTServerT t;
	
	public static Object processRequest (ArrayList<Object> list) {
	    switch ((String)list.get(0)) {
		    case "0" : { //UserCheckNameExists
		    	Cache.Users.update();
		    	return Cache.Users.map.containsKey(list.get(1));
		    }
		    case "1" : { //UserRegister
		    	return DatabaseUser.createUserCredential((String)list.get(1),(String)list.get(2),1,"PENDING APPROVAL");
		    }
		    case "2" : { //UserCheckCredentialOK
		    	Cache.Users.update();
		    	User u=Cache.Users.map.get(list.get(1));
		    	if (u==null || !u.getPassword().equals(list.get(2))) {
		    		return "INVALID";
		    	} else {
		    		return Cache.Users.map.get(list.get(1)).getStatus();
		    	}
		    }
		    case "3" : { //SiteGetList
		    	Cache.Sites.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Site s : Cache.Sites.map.values()) result.add(s.toObj());
		    	return result;
		    }
		    case "4" : { //SiteGetControllers
		    	Cache.Sites.update();
		    	Cache.Controllers.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Controller c : Cache.Sites.map.get(list.get(1)).getControllers()) result.add(c.toObj());
		    	return result;
		    }
		    case "5" : { //ControllerGetSensors
		    	Cache.Controllers.update();
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Sensor s : Cache.Controllers.map.get(list.get(1)).getSensors()) result.add(s.toObj());
		    	return result;
		    }
		    case "6" : { //ControllerGetActuators
		    	Cache.Controllers.update();
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Actuator act : Cache.Controllers.map.get(list.get(1)).getActuators()) result.add(act.toObj());
		    	return result;
		    }
		    case "7" : { //DayScheduleRuleGetAll
		    	Cache.DayScheduleRules.update();
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Dayschedulerule r : Cache.DayScheduleRules.map.values()) result.add(r.toObj());
		    	return result;
		    }
		    case "8" : { //DayScheduleRuleGetByName
		    	Cache.DayScheduleRules.update();
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Object o : Cache.DayScheduleRules.map.get(list.get(1)).toObj()) result.add(o);
		    	return result;
		    }
		    case "9" : { //DayScheduleRuleSet
		    	Cache.DayScheduleRules.update();
		    	Dayschedulerule r=Cache.DayScheduleRules.map.get(list.get(1));
		    	if (r==null) return "RULE_NOT_EXIST";
		    	else {
		    		int sh=(int)list.get(3); int sm=(int)list.get(4);
		    		int eh=(int)list.get(5); int em=(int)list.get(6);
		    		if (sh>eh || (sh==eh && sm>em)) return "INVALID_TIME";
		    		boolean flag=DatabaseDayScheduleRule.updateDayScheduleRule((String)list.get(1),(String)list.get(2), sh, sm, eh, em);
		    		if (flag) return "OK";
		    		else return "ERROR";
		    	}
		    }
		    case "10" : { //DayScheduleRuleCreate
		    	Cache.DayScheduleRules.update();
		    	Dayschedulerule r=Cache.DayScheduleRules.map.get(list.get(1));
		    	if (r==null) return "RULE_ALREADY_EXISTS";
		    	else {
		    		int sh=(int)list.get(2); int sm=(int)list.get(3);
		    		int eh=(int)list.get(4); int em=(int)list.get(5);
		    		if (sh>eh || (sh==eh && sm>em)) return "INVALID_TIME";
		    		boolean flag=DatabaseDayScheduleRule.createDayScheduleRule((String)list.get(1), sh, sm, eh, em);
		    		if (flag) return "OK";
		    		else return "ERROR";
		    	}
		    }
		    case "11" : { //ActuatorGetRegularSchedules
		    	Cache.Actuators.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Regularschedule r : Cache.Actuators.map.get(list.get(1)).getRegularschedules()) {
		    		result.add(r.toObj());
		    	}
		    	return result;
		    }
		    case "12" : { //ActuatorGetSpecialSchedules
		    	Cache.Actuators.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Specialschedule ss : Cache.Actuators.map.get(list.get(1)).getSpecialschedules()) {
		    		result.add(ss.toObj());
		    	}
		    	return result;
		    }
		    case "13" : { //RegularScheduleGetAll
		    	Cache.RegularSchedules.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Regularschedule r : Cache.RegularSchedules.map.values()) {
		    		result.add(r.toObj());
		    	}
		    	return result;
		    }
		    case "14" : { //RegularScheduleByName
		    	Cache.RegularSchedules.update();
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Object o : Cache.RegularSchedules.map.get(list.get(1)).toObj()) {
		    		result.add(o);
		    	}
		    	return result;
		    }
		    case "15" : { //RegularScheduleUpdateName
		    	Cache.RegularSchedules.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),(String)list.get(2),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "16" : { //RegularScheduleUpdateActuator
		    	Cache.RegularSchedules.update(); Cache.Actuators.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),Cache.Actuators.map.get(list.get(2)).getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "17" : { //RegularScheduleUpdateDay
		    	Cache.RegularSchedules.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if ((int)list.get(2)>=128) return "INVALID_DAY";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),(int)list.get(2),r.getDayschedulerule().getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "18" : { //RegularScheduleUpdateDayScheduleRule
		    	Cache.RegularSchedules.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(2))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),Cache.DayScheduleRules.map.get(list.get(2)).getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "19" : { //RegularScheduleActuatorOn
		    	Cache.RegularSchedules.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),(boolean)list.get(2),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "20" : { //RegularSchedulePriority
		    	Cache.RegularSchedules.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getActuatoron(),(int)list.get(2),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "21" : { //RegularScheduleEnabled
		    	Cache.RegularSchedules.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.updateRegularSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getDaymask(),r.getDayschedulerule().getRulename(),r.getActuatoron(),r.getPriority(),(boolean)(list.get(2)));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "22" : { //RegularScheduleCreate
		    	Cache.RegularSchedules.update(); Cache.Actuators.update(); Cache.DayScheduleRules.update();
		    	Regularschedule r=Cache.RegularSchedules.map.get(list.get(1));
		    	if (r!=null) return "SCHEDULE_ALREADY_EXISTS";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	else if ((int)(list.get(3))>=128) return "INVALID_DAY";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(4))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseRegularSchedule.createRegularSchedule( (String)list.get(1),Cache.Actuators.map.get(list.get(2)).getName(),
		    																	(int)list.get(3), Cache.DayScheduleRules.map.get(list.get(4)).getRulename(),
		    																	(boolean)list.get(5), (int)list.get(6), (boolean)list.get(7));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "23" : { //SpecialScheduleGetAll
		    	Cache.SpecialSchedules.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Specialschedule r : Cache.SpecialSchedules.map.values()) {
		    		result.add(r.toObj());
		    	}
		    	return result;
		    }
		    case "24" : { //SpecialScheduleByName
		    	Cache.SpecialSchedules.update();
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Object o : Cache.SpecialSchedules.map.get(list.get(1)).toObj()) {
		    		result.add(o);
		    	}
		    	return result;
		    }
		    case "25" : { //SpecialScheduleUpdateName
		    	Cache.SpecialSchedules.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),(String)list.get(2),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "26" : { //SpecialScheduleUpdateActuator
		    	Cache.SpecialSchedules.update(); Cache.Actuators.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),Cache.Actuators.map.get(list.get(2)).getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "27" : { //SpecialScheduleUpdateDay
		    	Cache.SpecialSchedules.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),(int)(list.get(2)),(int)(list.get(3)),(int)(list.get(4)),r.getDayschedulerule().getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "28" : { //SpecialScheduleUpdateDayScheduleRule
		    	Cache.SpecialSchedules.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(2))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),Cache.DayScheduleRules.map.get(list.get(2)).getRulename(),r.getActuatoron(),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "29" : { //SpecialScheduleActuatorOn
		    	Cache.SpecialSchedules.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),(boolean)(list.get(2)),r.getPriority(),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "30" : { //SpecialSchedulePriority
		    	Cache.SpecialSchedules.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),r.getActuatoron(),(int)(list.get(2)),r.getEnabled());
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "31" : { //SpecialScheduleEnabled
		    	Cache.SpecialSchedules.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r==null) return "SCHEDULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.updateSpecialSchedule(r.getSchedulename(),r.getSchedulename(),r.getActuator().getName(),r.getYear(),r.getMonth(),r.getDay(),r.getDayschedulerule().getRulename(),(boolean)(list.get(2)),r.getPriority(),(boolean)(list.get(2)));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "32" : { //SpecialScheduleCreate
		    	Cache.SpecialSchedules.update(); Cache.Actuators.update(); Cache.DayScheduleRules.update();
		    	Specialschedule r=Cache.SpecialSchedules.map.get(list.get(1));
		    	if (r!=null) return "SCHEDULE_ALREADY_EXISTS";
		    	else if (!Cache.Actuators.map.containsKey(list.get(2))) return "ACTUATOR_NOT_EXIST";
		    	else if (!Cache.DayScheduleRules.map.containsKey(list.get(4))) return "RULE_NOT_EXIST";
		    	else {
		    		boolean flag=DatabaseSpecialSchedule.createSpecialSchedule( (String)list.get(1),Cache.Actuators.map.get(list.get(2)).getName(),
		    																	(int)list.get(3), (int)list.get(4), (int)list.get(5), 
		    																	Cache.DayScheduleRules.map.get(list.get(6)).getRulename(),
		    																	(boolean)list.get(7), (int)list.get(8), (boolean)(list.get(9)));
		    		if (flag) return "OK"; else return "ERROR";
		    	}
		    }
		    case "33": { //ScheduleServerIsStarted
		    	return SchedulingServer.isStarted;
		    }
		    case "34" : { //OngoingScheduleGetAll
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (SchedulingData d : SchedulingServer.getSchedulingThread().data.values()) {
		    		result.add(new Object [] {d.getName(),d.getActuatorName(),d.getActuatorFlag(),d.getPriority(),d.getNextStartTime(),d.getNextEndTime()});
		    	}
		    	return result;
		    }
		    case "35" : { //OngoingScheduleGetByActuatorName
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (SchedulingData d : SchedulingServer.getSchedulingThread().data.values())
		    		if (d.getActuatorName().equals(list.get(1)))
		    			result.add(new Object [] {d.getName(),d.getActuatorName(),d.getActuatorFlag(),d.getPriority(),d.getNextStartTime(),d.getNextEndTime()});
		    	return result;
		    }
		    case "36" : { //SensorGetByName
		    	Cache.Sensors.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	if (Cache.Sensors.map.containsKey(list.get(1))) result.add(Cache.Sensors.map.get(list.get(1)).toObj());
		    	return result;
		    }
		    case "37" : { //SensorGetReadingBetweenTime
		    	ArrayList<SensorReading> lsr=DatabaseReading.getReadingBetweenTime((String)list.get(2),(LocalDateTime)list.get(3),(LocalDateTime)list.get(4),5000);
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (SensorReading sr : lsr) result.add(new Object [] {sr.getTimestamp(),sr.getActualValue()});
		    	return result;
		    }
		    case "38" : { //SensorGetReadingBetweenTime
		    	ArrayList<SensorReading> lsr=DatabaseReading.getReadingMonthly((String)list.get(2),(int)list.get(3),(int)list.get(4));
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (SensorReading sr : lsr) result.add(new Object [] {sr.getTimestamp(),sr.getActualValue()});
		    	return result;
		    }
	    }
	    return null;
	}
	
	private static class GWTServerT extends Thread {
		boolean stopFlag=false;
	}
	
	private static class GWTSecureServerThread extends GWTServerT {
		public SSLServerSocket serverSocket;
		
		@SuppressWarnings("unchecked")
		public void run () {
			System.setProperty("javax.net.ssl.keyStore",Config.getConfig(Config.CONFIG_SERVER_GWT_KEY_FILE_KEY));
			System.setProperty("javax.net.ssl.keyStorePassword",Config.getConfig(Config.CONFIG_SERVER_GWT_PASSWORD_KEY));
			while (!stopFlag && !Thread.currentThread().isInterrupted()) {
				try {
					try {
					    SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
					    serverSocket = (SSLServerSocket) socketFactory.createServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
					    serverSocket.setSoTimeout(0);
					    SSLSocket sslsocket=(SSLSocket) serverSocket.accept();
					    InputStream is=sslsocket.getInputStream();
					    ObjectInputStream ois=new ObjectInputStream(is);
					    
					    OutputStream os=sslsocket.getOutputStream();
					    ObjectOutputStream oos=new ObjectOutputStream(os);
					    
					    try {
						    Object o=ois.readObject();
						    if (o instanceof ArrayList) {
						    	oos.writeObject(GWTServer.processRequest((ArrayList<Object>)o));
						    }
					    } catch (ClassNotFoundException cn) {}
				    	oos.close(); os.close();
				    	sslsocket.close(); serverSocket.close();
					} catch (SocketTimeoutException te) {
						serverSocket.close();
					} 
				} catch (IOException e) {
					Logger.log("GWTSecureServerThread Error - "+e.getMessage());
					break;
				}
			}
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
	}
	
	private static class GWTServerThread extends GWTServerT {
		public ServerSocket serverSocket;
		
		@SuppressWarnings("unchecked")
		public void run () {
			while (!stopFlag && !Thread.currentThread().isInterrupted()) {
				try {
					try {
						serverSocket=new ServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
					    serverSocket.setSoTimeout(0);
					    Socket socket=(Socket) serverSocket.accept();
					    InputStream is=socket.getInputStream();
					    ObjectInputStream ois=new ObjectInputStream(is);
					    
					    OutputStream os=socket.getOutputStream();
					    ObjectOutputStream oos=new ObjectOutputStream(os);
					    
					    try {
						    Object o=ois.readObject();
						    if (o instanceof ArrayList) {
						    	oos.writeObject(GWTServer.processRequest((ArrayList<Object>)o));
						    }
					    } catch (ClassNotFoundException cn) {}
				    	oos.close(); os.close();
				    	socket.close(); serverSocket.close();
					} catch (SocketTimeoutException te) {
						serverSocket.close();
					} 
				} catch (IOException e) {
					Logger.log("GWTServerThread Error - "+e.getMessage());
					break;
				}
			}
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			};
		}
	}
	
	public static boolean start() {
		if (t==null) {
			if (Boolean.parseBoolean(Config.getConfig(Config.CONFIG_SERVER_GWT_SSL_KEY))) {
				Logger.log("GWTSecureServer - Starting");
				t=new GWTSecureServerThread();
				t.start();
			} else {
				Logger.log("GWTServer - Starting");
				t=new GWTServerThread();
				t.start();
			}
			return true;
		}
		return false;
	}
	
	public static boolean stop() {
		if (t!=null) {
			Logger.log("GWTSecure/Server - Requested to stop");
			t.stopFlag=true;
			if (t instanceof GWTSecureServerThread) {
				try {
					System.setProperty("javax.net.ssl.keyStore",Config.getConfig(Config.CONFIG_SERVER_GWT_KEY_FILE_KEY));
					System.setProperty("javax.net.ssl.keyStorePassword",Config.getConfig(Config.CONFIG_SERVER_GWT_PASSWORD_KEY));
				    SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				    SSLSocket sc = (SSLSocket) socketFactory.createSocket("127.0.0.1",Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
				    sc.startHandshake();
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
