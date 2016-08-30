package Chi;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Cache {
	public static SessionFactory factory;
	public static ActuatorData Actuators;
	public static SiteData Sites;
	public static ControllerData Controllers;
	public static SensorClassData SensorClasses;
	public static SensorData Sensors;
	public static UserData Users;
	public static DayScheduleRuleData DayScheduleRules;
	public static RegularScheduleData RegularSchedules;
	public static SpecialScheduleData SpecialSchedules;
	
	private static String getHSQLAddress () {
		StringBuilder sb=new StringBuilder();
		sb.append("jdbc:hsqldb:hsql://");
		sb.append(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY));
		sb.append(':');
		sb.append(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY));
		sb.append("/Chi");
		return sb.toString();
	}
	
	public static void initialize() {
	    try{
	        Configuration c= new Configuration().configure("/conf/hibernate.cfg.xml");
	        c.setProperty("hibernate.connection.username",Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_USERNAME_KEY));
	        c.setProperty("hibernate.connection.password",Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PASSWORD_KEY));
	        c.setProperty("hibernate.connection.url",getHSQLAddress());
	        File [] files=new File("conf").listFiles();
	        for (File f : files) {
	        	if (f.getPath().endsWith(".hbm.xml")) {
	        		c=c.addResource("/conf/"+f.getName());
	        	}
	        }
	        factory=c.buildSessionFactory();
	     }catch (Throwable ex) { 
	        Logger.log("Cache failed to create factory object - "+ex.getMessage());
	        ex.printStackTrace();
	        throw new ExceptionInInitializerError(ex); 
	     }
	    Actuators=new ActuatorData();
	    Sites=new SiteData();
	    Controllers=new ControllerData();
	    SensorClasses=new SensorClassData();
	    Sensors=new SensorData();
	    Users=new UserData();
	    DayScheduleRules=new DayScheduleRuleData();
	    RegularSchedules=new RegularScheduleData();
	    SpecialSchedules=new SpecialScheduleData();
	    
	    /*
	    Actuators.updateWithWait(); Sites.updateWithWait(); Controllers.updateWithWait();
	    SensorClasses.updateWithWait(); Sensors.updateWithWait(); Users.updateWithWait();
	    DayScheduleRules.updateWithWait(); RegularSchedules.updateWithWait(); SpecialSchedules.updateWithWait();*/
	}
	
	private static abstract class Data<T> {
		protected static boolean updateSuccess=false;
		public HashMap<String,T> map=new HashMap<>();
		public String getType() {return "data";}
		public String getClassName() {return "data";}
		public void onAcquired(@SuppressWarnings("rawtypes") List l) {}
		public void update() {
			Session session=factory.openSession();
			Transaction tx=null;
			try {
				tx=session.beginTransaction();
				onAcquired(session.createQuery("FROM "+getClassName()).getResultList());
				updateSuccess=true;
			} catch (HibernateException e) {
				updateSuccess=false;
		        Logger.log("Cache.update"+getType()+" Error - "+e.getMessage());
		        if (tx!=null) tx.rollback();
		    } finally {
		         session.close(); 
		    }
		}
		public boolean updateWithWait() {
			updateSuccess=false;
			WaitUI u=new WaitUI();
			u.setText("Querying "+getType());
			Thread t=new Thread() {
				public void run () {
					try { Thread.sleep(10); } catch (InterruptedException e) {}
					update();
					u.dispose();
				}
			};
			t.start();
			u.setVisible(true);
			return updateSuccess;
		}
	}
	
	public static class UserData extends Data<User> {
		public String getType() {return "users";}
		public String getClassName() {return "User";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				User u=(User)o;
				map.put(u.getUsername(),u);
			}
		}
	}

	public static class SensorData extends Data<Sensor> {
		public String getType() {return "sensors";}
		public String getClassName() {return "Sensor";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Sensor s=(Sensor)o;
				map.put(s.getSensorname(),s);
			}
		}
	}

	public static class SensorClassData extends Data<Sensorclass> {
		public String getType() {return "sensor class";}
		public String getClassName() {return "Sensorclass";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Sensorclass s=(Sensorclass)o;
				map.put(s.getClassname(),s);
			}
		}
	}
	
	public static class ControllerData extends Data<Controller> {
		public String getType() {return "controllers";}
		public String getClassName() {return "Controller";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Controller c=(Controller)o;
				map.put(c.getControllername(),c);
			}
		}
	}
	
	public static class SiteData extends Data<Site> {
		public String getType() {return "sites";}
		public String getClassName() {return "Site";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Site s=(Site)o;
				map.put(s.getSitename(),s);
			}
		}
	}
	
	public static class ActuatorData extends Data<Actuator> {
		public String getType() {return "actuators";}
		public String getClassName() {return "Actuator";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Actuator a=(Actuator)o;
				map.put(a.getName(),a);
			}
		}
	}
	
	public static class DayScheduleRuleData extends Data<Dayschedulerule> {
		public String getType() {return "day schedule rules";}
		public String getClassName() {return "Dayschedulerule";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Dayschedulerule d=(Dayschedulerule)o;
				map.put(d.getRulename(),d);
			}
		}
	}
	
	public static class RegularScheduleData extends Data<Regularschedule> {
		public String getType() {return "regular schedules";}
		public String getClassName() {return "Regularschedule";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Regularschedule r=(Regularschedule)o;
				map.put(r.getSchedulename(),r);
			}
		}
	}

	public static class SpecialScheduleData extends Data<Specialschedule> {
		public String getType() {return "special schedules";}
		public String getClassName() {return "Specialschedule";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Specialschedule s=(Specialschedule)o;
				map.put(s.getSchedulename(),s);
			}
		}
	}
}
