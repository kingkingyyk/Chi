package Chi;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class GWTServer {
	private static GWTServerThread t;
	
	public static Object processRequest (ArrayList<String> list) {
	    switch (list.get(0)) {
		    case "0" : {
		    	Cache.Users.update();
		    	return Cache.Users.map.containsKey(list.get(1));
		    }
		    case "1" : {
		    	return DatabaseUser.createUserCredential(list.get(1),list.get(2),1,"PENDING APPROVAL");
		    }
		    case "2" : {
		    	Cache.Users.update();
		    	User u=Cache.Users.map.get(list.get(1));
		    	if (u==null || !u.getPassword().equals(list.get(2))) {
		    		return "INVALID";
		    	} else {
		    		return Cache.Users.map.get(list.get(1)).getStatus();
		    	}
		    }
		    case "3" : {
		    	Cache.Sites.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Site s : Cache.Sites.map.values()) result.add(s.toObj());
		    	return result;
		    }
		    case "4" : {
		    	Cache.Sites.update();
		    	Cache.Controllers.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Controller c : Cache.Sites.map.get(list.get(1)).getControllers()) result.add(c.toObj());
		    	return result;
		    }
		    case "5" : {
		    	Cache.Controllers.update();
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Sensor s : Cache.Controllers.map.get(list.get(1)).getSensors()) result.add(s.toObj());
		    	return result;
		    }
		    case "6" : {
		    	Cache.Controllers.update();
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Actuator act : Cache.Controllers.map.get(list.get(1)).getActuators()) result.add(act.toObj());
		    	return result;
		    }
		    case "7" : {
		    	Cache.DayScheduleRules.update();
		    	ArrayList<Object []> result=new ArrayList<>();
			    for (Dayschedulerule r : Cache.DayScheduleRules.map.values()) result.add(r.toObj());
		    	return result;
		    }
		    case "8" : {
		    	Cache.DayScheduleRules.update();
		    	ArrayList<Object> result=new ArrayList<>();
		    	for (Object o : Cache.DayScheduleRules.map.get(list.get(1)).toObj()) result.add(o);
		    	return result;
		    }
		    case "9" : {
		    	Cache.DayScheduleRules.update();
		    	Dayschedulerule r=Cache.DayScheduleRules.map.get(list.get(1));
		    	if (r==null) return "RULE_NOT_EXIST";
		    	else {
		    		int sh=Integer.parseInt(list.get(3)); int sm=Integer.parseInt(list.get(4));
		    		int eh=Integer.parseInt(list.get(5)); int em=Integer.parseInt(list.get(6));
		    		if (sh>eh || (sh==eh && sm>em)) return "INVALID_TIME";
		    		boolean flag=DatabaseDayScheduleRule.updateDayScheduleRule(list.get(1),list.get(2), sh, sm, eh, em);
		    		if (flag) return "OK";
		    		else return "ERROR";
		    	}
		    }
		    case "10" : {
		    	Cache.DayScheduleRules.update();
		    	Dayschedulerule r=Cache.DayScheduleRules.map.get(list.get(1));
		    	if (r==null) return "RULE_ALREADY_EXISTS";
		    	else {
		    		int sh=Integer.parseInt(list.get(2)); int sm=Integer.parseInt(list.get(3));
		    		int eh=Integer.parseInt(list.get(4)); int em=Integer.parseInt(list.get(5));
		    		if (sh>eh || (sh==eh && sm>em)) return "INVALID_TIME";
		    		boolean flag=DatabaseDayScheduleRule.createDayScheduleRule(list.get(1), sh, sm, eh, em);
		    		if (flag) return "OK";
		    		else return "ERROR";
		    	}
		    }
		    case "11" : {
		    	Cache.Actuators.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Regularschedule r : Cache.Actuators.map.get(list.get(1)).getRegularschedules()) {
		    		result.add(r.toObj());
		    	}
		    	return result;
		    }
		    case "12" : {
		    	Cache.Actuators.update();
		    	ArrayList<Object []> result=new ArrayList<>();
		    	for (Specialschedule ss : Cache.Actuators.map.get(list.get(1)).getSpecialschedules()) {
		    		result.add(ss.toObj());
		    	}
		    	return result;
		    }
	    }
	    return null;
	}
	
	private static class GWTServerThread extends Thread {
		public SSLServerSocket serverSocket;
		boolean stopFlag=false;
		
		@SuppressWarnings("unchecked")
		public void run () {
			System.setProperty("javax.net.ssl.keyStore",Config.getConfig(Config.CONFIG_SERVER_GWT_KEY_FILE_KEY));
			System.setProperty("javax.net.ssl.keyStorePassword",Config.getConfig(Config.CONFIG_SERVER_GWT_PASSWORD_KEY));
			while (!stopFlag) {
				try {
					try {
					    SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
					    serverSocket = (SSLServerSocket) socketFactory.createServerSocket(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_GWT_PORT_KEY)));
					    serverSocket.setSoTimeout(5000);
					    SSLSocket sslsocket=(SSLSocket) serverSocket.accept();
					    InputStream is=sslsocket.getInputStream();
					    ObjectInputStream ois=new ObjectInputStream(is);
					    
					    OutputStream os=sslsocket.getOutputStream();
					    ObjectOutputStream oos=new ObjectOutputStream(os);
					    
					    try {
						    Object o=ois.readObject();
						    if (o instanceof ArrayList) {
						    	oos.writeObject(GWTServer.processRequest((ArrayList<String>)o));
						    }
					    } catch (ClassNotFoundException cn) {}
				    	oos.close(); os.close();
				    	sslsocket.close(); serverSocket.close();
					} catch (SocketTimeoutException te) {
						serverSocket.close();
					} 
				} catch (IOException e) {
					Logger.log("GWTServerThread Error - "+e.getMessage());
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
			Logger.log("GWTServer - Starting");
			t=new GWTServerThread();
			t.start();
			return true;
		}
		return false;
	}
	
	public static boolean stop() {
		if (t!=null) {
			Logger.log("GWTServer - Requested to stop");
			t.stopFlag=true;
			t.interrupt();
			t=null;
			return true;
		}
		return false;
	}
}
