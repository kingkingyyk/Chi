package Chi;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Cache {
	
	public static SessionFactory factory;

	public static ArrayList<String> usernameList=new ArrayList<>();
	public static ArrayList<User> userObj=new ArrayList<>();
	public static HashMap<String,User> userMap=new HashMap<>();
	private static boolean userUpdateSuccess=false;
	
	public static void initialize() {
	    try{
	        factory = new Configuration().configure("/conf/hibernate.cfg.xml").buildSessionFactory();
	     }catch (Throwable ex) { 
	        System.err.println("Failed to create sessionFactory object." + ex);
	        throw new ExceptionInInitializerError(ex); 
	     }
	}
	
	public static void updateUser () {
		usernameList.clear(); userObj.clear(); userMap.clear();
		Session session=factory.openSession();
		Transaction tx=null;
		try {
			tx=session.beginTransaction();
			@SuppressWarnings({"rawtypes" })
			List users=session.createQuery("FROM User").getResultList();
			for (Object o : users) {
				User u=(User)o;
				usernameList.add(u.getUsername());
				userObj.add(u);
				userMap.put(u.getUsername(),u);
			}
			userUpdateSuccess=true;
		} catch (HibernateException e) {
			userUpdateSuccess=false;
	        Logger.log("Cache.updateUserSilent Error - "+e.getMessage());
	        if (tx!=null) tx.rollback();
	    } finally {
	         session.close(); 
	    }
	}
	
	public static boolean updateUserWithWait() {
		userUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying user");
		Thread t=new Thread() {
			public void run () {
				updateUser();
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return userUpdateSuccess;
	}
	
	public static ArrayList<String> sensorClassList=new ArrayList<>();
	public static HashMap<String,Object[]> sensorClassMap=new HashMap<>();
	public static ArrayList<Object []> sensorClassObj=new ArrayList<>();
	private static boolean sensorClassUpdateSuccess=false;
	
	public static boolean updateSensorClass() {
		sensorClassUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying sensor class");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseSensorClass.getSensorClass();
				sensorClassList.clear();
				sensorClassMap.clear();
				sensorClassObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1)};
						sensorClassList.add(rs.getString(1));
						sensorClassMap.put(rs.getString(1),o);
						sensorClassObj.add(o);
					}
					sensorClassUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateSensorClass - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query Sensor Class",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return sensorClassUpdateSuccess;
	}
	
	public static ArrayList<String> sensorList=new ArrayList<>();
	public static HashMap<String,Object []> sensorMap=new HashMap<>();
	public static ArrayList<Object []> sensorObj=new ArrayList<>();
	private static boolean sensorUpdateSuccess=false;
	
	public static boolean updateSensor() {
		sensorUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying sensor");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseSensor.getSensors();
				sensorList.clear();
				sensorMap.clear();
				sensorObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6),rs.getString(7)};
						sensorList.add(rs.getString(1));
						sensorMap.put(rs.getString(1),o);
						sensorObj.add(o);
					}
					sensorUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateSensor - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query Sensor",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return sensorUpdateSuccess;
	}
	
	public static ArrayList<String> controllerList=new ArrayList<>();
	public static HashMap<String, Object[]> controllerMap=new HashMap<>();
	public static ArrayList<Object []> controllerObj=new ArrayList<>();
	private static boolean controllerUpdateSuccess=false;
	
	public static boolean updateController() {
		controllerUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying controller");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseController.getControllers();
				controllerList.clear();
				controllerMap.clear();
				controllerObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getInt(5),rs.getDate(6)};
						controllerList.add(rs.getString(1));
						controllerMap.put(rs.getString(1),o);
						controllerObj.add(o);
					}
					controllerUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateController - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query Controller",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return controllerUpdateSuccess;
	}
	
	public static ArrayList<String> siteList=new ArrayList<>();
	public static HashMap<String,Object []> siteMap=new HashMap<>();
	public static ArrayList<Object []> siteObj=new ArrayList<>();
	private static boolean siteUpdateSuccess=false;
	
	public static boolean updateSite() {
		siteUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying site");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseSite.getSites();
				siteList.clear();
				siteMap.clear();
				siteObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2)};
						siteList.add(rs.getString(1));
						siteMap.put(rs.getString(1),o);
						siteObj.add(o);
					}
					siteUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateSite - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query Site",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return siteUpdateSuccess;
	}
	
	public static ArrayList<String> actuatorList=new ArrayList<>();
	public static HashMap<String,Object []> actuatorMap=new HashMap<>();
	public static ArrayList<Object []> actuatorObj=new ArrayList<>();
	private static boolean actuatorUpdateSuccess=false;
	
	public static boolean updateActuator() {
		actuatorUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying actuator");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseActuator.getActuators();
				actuatorList.clear();
				actuatorMap.clear();
				actuatorObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getString(3)};
						actuatorList.add(rs.getString(1));
						actuatorMap.put(rs.getString(1),o);
						actuatorObj.add(o);
					}
					actuatorUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateActuator - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query actuator",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return actuatorUpdateSuccess;
	}
	
	public static ArrayList<String> DayScheduleRuleList=new ArrayList<>();
	public static HashMap<String,Object[]> DayScheduleRuleMap=new HashMap<>();
	public static ArrayList<Object []> DayScheduleRuleObj=new ArrayList<>();
	private static boolean DayScheduleRuleUpdateSuccess=false;
	
	public static boolean updateDayScheduleRule() {
		DayScheduleRuleUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying DayScheduleRule");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseDayScheduleRule.getDayScheduleRules();
				DayScheduleRuleList.clear();
				DayScheduleRuleMap.clear();
				DayScheduleRuleObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5)};
						DayScheduleRuleList.add(rs.getString(1));
						DayScheduleRuleMap.put(rs.getString(1),o);
						DayScheduleRuleObj.add(o);
					}
					DayScheduleRuleUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateDayScheduleRule - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query DayScheduleRule",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return DayScheduleRuleUpdateSuccess;
	}
	
	public static ArrayList<String> RegularScheduleList=new ArrayList<>();
	public static HashMap<String, Object []> RegularScheduleMap=new HashMap<>();
	public static ArrayList<Object []> RegularScheduleObj=new ArrayList<>();
	private static boolean RegularScheduleUpdateSuccess=false;
	
	public static boolean updateRegularSchedule() {
		RegularScheduleUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying schedule");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseRegularSchedule.getRegularSchedules();
				RegularScheduleList.clear();
				RegularScheduleMap.clear();
				RegularScheduleObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getBoolean(5),rs.getInt(6),rs.getBoolean(7)};
						RegularScheduleList.add(rs.getString(1));
						RegularScheduleMap.put(rs.getString(1),o);
						RegularScheduleObj.add(o);
					}
					RegularScheduleUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateRegularSchedule - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query Regular Schedule",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return RegularScheduleUpdateSuccess;
	}
	
	public static ArrayList<String> SpecialScheduleList=new ArrayList<>();
	public static HashMap<String, Object[]> SpecialScheduleMap=new HashMap<>();
	public static ArrayList<Object []> SpecialScheduleObj=new ArrayList<>();
	private static boolean SpecialScheduleUpdateSuccess=false;
	
	public static boolean updateSpecialSchedule() {
		SpecialScheduleUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying schedule");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseSpecialSchedule.getSpecialSchedules();
				SpecialScheduleList.clear();
				SpecialScheduleMap.clear();
				SpecialScheduleObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getString(6),rs.getBoolean(7),rs.getInt(8),rs.getBoolean(9)};
						SpecialScheduleList.add(rs.getString(1));
						SpecialScheduleMap.put(rs.getString(1),o);
						SpecialScheduleObj.add(o);
					}
					SpecialScheduleUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateSpecialSchedule - Error - "+e.getMessage());
					JOptionPane.showMessageDialog(null,"Fail to retrieve data from database.\nPlease refer to the console for more information.","Query Special Schedule",JOptionPane.ERROR_MESSAGE);
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return SpecialScheduleUpdateSuccess;
	}
}
