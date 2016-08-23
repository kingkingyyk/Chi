package Chi;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;

public class Cache {
	
	public static ArrayList<String> usernameList=new ArrayList<>();
	public static HashSet<String> usernameSet=new HashSet<>();
	public static ArrayList<Object []> user=new ArrayList<>();
	
	public static void updateUser() {
		
	}
	
	public static ArrayList<String> sensorClassList=new ArrayList<>();
	public static HashSet<String> sensorClassSet=new HashSet<>();
	public static ArrayList<Object []> sensorClassObj=new ArrayList<>();
	private static boolean sensorClassUpdateSuccess=false;
	
	public static boolean updateSensorClass() {
		sensorUpdateSuccess=false;
		WaitUI u=new WaitUI();
		u.setText("Querying sensor class");
		Thread t=new Thread() {
			public void run () {
				ResultSet rs=DatabaseSensorClass.getSensorClass();
				sensorClassList.clear();
				sensorClassSet.clear();
				sensorClassObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1)};
						sensorClassList.add(rs.getString(1));
						sensorClassSet.add(rs.getString(1));
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
	public static HashSet<String> sensorSet=new HashSet<>();
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
				sensorSet.clear();
				sensorObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6),rs.getString(6)};
						sensorList.add(rs.getString(1));
						sensorSet.add(rs.getString(1));
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
	public static HashSet<String> controllerSet=new HashSet<>();
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
				controllerSet.clear();
				controllerObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getInt(5),rs.getDate(6)};
						controllerList.add(rs.getString(1));
						controllerSet.add(rs.getString(1));
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
	public static HashSet<String> siteSet=new HashSet<>();
	public static HashMap<String,String> siteToImgSet=new HashMap<>();
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
				siteSet.clear();
				siteObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2)};
						siteList.add(rs.getString(1));
						siteSet.add(rs.getString(1));
						siteToImgSet.put(rs.getString(1), rs.getString(2));
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
	public static HashSet<String> actuatorSet=new HashSet<>();
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
				actuatorSet.clear();
				actuatorObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getString(2),rs.getString(3)};
						actuatorList.add(rs.getString(1));
						actuatorSet.add(rs.getString(1));
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
	public static HashSet<String> DayScheduleRuleSet=new HashSet<>();
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
				DayScheduleRuleSet.clear();
				DayScheduleRuleObj.clear();
				
				try {
					while (rs.next()) {
						Object [] o={rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5)};
						DayScheduleRuleList.add(rs.getString(1));
						DayScheduleRuleSet.add(rs.getString(1));
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
	
}
