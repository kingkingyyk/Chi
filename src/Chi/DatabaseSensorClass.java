package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseSensorClass extends DatabaseHSQL {
	
	public static interface OnCreateAction {
		public void run (String name);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldName, String name);
	}
	
	public static interface OnDeleteAction {
		public void run (String name);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseSensorClass - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseSensorClass - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseSensorClass - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static ResultSet getSensorClass () {
		return runSQLFromFileAndGetData("DB Get Sensor Class",Config.getConfig(Config.DATABASE_QUERY_SENSOR_CLASS_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createSensorClass (String name) {
		Logger.log("DB Create Sensor Class : "+Config.getConfig(Config.DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_KEY));
		if (Cache.sensorClassMap.containsKey(name)) {
			Logger.log("DB Create Sensor Class[Cache] - Sensor class already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Create Sensor Class - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Create Sensor Class - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, name);
					Logger.log("DB Create Sensor Class - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Create Sensor Class - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Create Sensor Class - Execute Callbacks");
					for (OnCreateAction a : OnCreateList) {
						a.run(name);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Create Sensor Class - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateSensorClass (String oldN, String newN) {
		Logger.log("DB Update Sensor Class : "+Config.getConfig(Config.DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_KEY));
		if (!Cache.sensorClassMap.containsKey(oldN)) {
			Logger.log("DB Update Sensor Class[Cache] - Sensor class doesn't exist!");
			return false;
		} else {
			Object [] o=Cache.sensorClassMap.get(oldN);
			if (o[0].equals(newN)) {
				Logger.log("DB Update Sensor Class[Cache] - Information is same!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Sensor Class - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Sensor Class - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						Logger.log("DB Update Sensor Class - Execute : "+ps.toString());
						ps.setString(1, newN);
						ps.setString(2, oldN);
						Logger.log("DB Update Sensor Class - Execute");
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Sensor Class - Execute "+ps.toString());
						ps.execute();
						
						Logger.log("DB Update Sensor Class - Execute Callbacks");
						for (OnUpdateAction a : OnUpdateList) {
							a.run(oldN,newN);
						}
					}
					c.close();
					
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Sensor Class - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean deleteSensorClass (String name) {
		Logger.log("DB Delete Sensor Class : "+Config.getConfig(Config.DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_KEY));
		if (!Cache.sensorClassMap.containsKey(name)) {
			Logger.log("DB Create Sensor Class[Cache] - Sensor class already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete Sensor Class - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Update Sensor Class - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, name);
					Logger.log("DB Delete Sensor Class - Execute - "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					ps.setString(1, name);
					Logger.log("DB Delete Sensor Class - Execute - "+ps.toString());
					ps.execute();
	
					ps=c.prepareStatement(sql[3]);
					Logger.log("DB Delete Sensor Class - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Delete Sensor Class - Execute Callbacks");
					for (OnDeleteAction a : OnDeleteList) {
						a.run(name);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete Sensor Class - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}

}
