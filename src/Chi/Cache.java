package Chi;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.JOptionPane;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Cache {
	public static volatile SessionFactory factory;
	public static ActuatorData Actuators;
	public static SiteData Sites;
	public static ControllerData Controllers;
	public static SensorClassData SensorClasses;
	public static SensorData Sensors;
	public static UserData Users;
	public static DayScheduleRuleData DayScheduleRules;
	public static RegularScheduleData RegularSchedules;
	public static SpecialScheduleData SpecialSchedules;
	public static SensorActuatorResponseData SensorActuatorResponses;
	
	private static String getHSQLAddress () {
		StringBuilder sb=new StringBuilder();
		sb.append("jdbc:hsqldb:hsql://");
		sb.append(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_IP_KEY));
		sb.append(':');
		sb.append(Config.getConfig(Config.CONFIG_SERVER_DATABASE_HSQL_PORT_KEY));
		sb.append("/Chi");
		return sb.toString();
	}
	
	private static boolean initDone=false;
	public static void initialize(final StartScreen diag) {
		final WaitUI u=new WaitUI();
		if (diag==null) {
			u.setText("Pooling data from database... (20)");
		}
		Thread t=new Thread() {
			public void run () {
				try {
			        Configuration c= new Configuration().configure("/conf/hibernate.cfg.xml");
			        c.setProperty("hibernate.enable_lazy_load_no_trans","true");
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
			        Logger.log(Logger.LEVEL_ERROR,"Cache failed to create factory object - "+ex.getMessage());
			        ex.printStackTrace();
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
			    SensorActuatorResponses=new SensorActuatorResponseData();
			    
			    boolean flag=(factory!=null);
			    if (flag) {
			    	//Topological order
				    flag &=Users.update();
				    flag &=SensorClasses.update();
				    flag &=Sites.update();
				    flag &=Controllers.update();
				    flag &=Sensors.update();
				    flag &=Actuators.update();
				    flag &=DayScheduleRules.update();
				    flag &=RegularSchedules.update();
				    flag &=SpecialSchedules.update();
				    flag &=SensorActuatorResponses.update();
			    }
			    
			    if (!flag) JOptionPane.showMessageDialog(null,"Fail to update data from database!\nMost functionalities will not be working.",Config.APP_NAME,JOptionPane.ERROR_MESSAGE);
			    if (diag==null) u.setVisible(false); else initDone=true;
			}
		};
		t.start();
		Thread t2=new Thread() {
			public void run () {
				try { Thread.sleep(1000); }catch (InterruptedException e) {}
				for (int cd=20;cd>=0 && ((diag==null && u.isVisible()) || !initDone);cd--) {
					if (diag==null) u.setText("Pooling data from database... ("+cd+")");
					else diag.setText("Pooling data from database... ("+cd+")");
					try { Thread.sleep(1000); }catch (InterruptedException e) {}
				}
				initDone=true;
			}
		};
		t2.start();
		if (diag==null) {
			u.setVisible(true);
		} else {
			while (!initDone) {
				try { Thread.sleep(1000); }catch (InterruptedException e) {}
			}
		}
	}
	
	public static void Stop() {
		if (factory!=null) factory.close();
	}
	
	private static abstract class Data<T> {
		public ConcurrentHashMap<String,T> map=new ConcurrentHashMap<>();
		public String getType() {return "data";}
		public String getClassName() {return "data";}
		public void onAcquired(@SuppressWarnings("rawtypes") List l) {}
		
		public boolean update() {
			Session session=factory.openSession();
			Transaction tx=null;
			boolean flag=false;
			try {
				tx=session.beginTransaction();
				onAcquired(session.createQuery("FROM "+getClassName()).getResultList());
				flag=true;
			} catch (HibernateException e) {
		        Logger.log(Logger.LEVEL_ERROR,"Cache.update"+getType()+" - "+e.getMessage());
		        if (tx!=null) tx.rollback();
		    } finally {
		         session.close(); 
		    }
			return flag;
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
	
	public static class SensorActuatorResponseData extends Data<Sensoractuatorresponse> {
		public String getType() {return "sensor actuator responses";}
		public String getClassName() {return "Sensoractuatorresponse";}
		
		public void onAcquired (@SuppressWarnings("rawtypes") List l) {
			map.clear();
			for (Object o : l) {
				Sensoractuatorresponse s=(Sensoractuatorresponse)o;
				map.put(String.valueOf(s.getId()),s);
			}
		}
	}
	
}
