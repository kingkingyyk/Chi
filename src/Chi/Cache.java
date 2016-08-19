package Chi;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;

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
						Object [] o={rs.getString(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getDouble(5),rs.getString(6)};
						sensorList.add(rs.getString(1));
						sensorSet.add(rs.getString(1));
						sensorObj.add(o);
					}
					sensorUpdateSuccess=true;
				} catch (Exception e) {
					Logger.log("Cache.updateSensor - Error - "+e.getMessage());
				}
				u.dispose();
			}
		};
		t.start();
		u.setVisible(true);
		return sensorUpdateSuccess;
	}
	
}
