package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
public class DatabaseSensor extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String sn, String cn, double min, double max, double trans, String unit, String con);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldSN, String sn, String cn, double min, double max, double trans, String unit, String con);
	}
	
	public static interface OnDeleteAction {
		public void run (String sn);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseSensor - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseSensor - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseSensor - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static ResultSet getSensorNameList () {
		return runSQLFromFileAndGetData("DB Get Sensor Name List",Config.getConfig(Config.DATABASE_QUERY_SENSOR_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getSensors () {
		return runSQLFromFileAndGetData("DB Get Sensor",Config.getConfig(Config.DATABASE_QUERY_SENSOR_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createSensor (String sn, String cn, double min, double max, double trans, String unit, String con) {
		Logger.log("DB Create Sensor : "+Config.getConfig(Config.DATABASE_CREATE_SENSOR_SQL_FILE_KEY));
		if (Cache.sensorMap.containsKey(sn)) {
			Logger.log("DB Create Sensor[Cache] - Sensor already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Create Sensor - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_SENSOR_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Create Sensor - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, sn);
					ps.setString(2, cn);
					ps.setDouble(3, min);
					ps.setDouble(4, max);
					ps.setDouble(5, trans);
					ps.setString(6, unit);
					ps.setString(7, con);
					Logger.log("DB Create Sensor - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Create Sensor - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Create Sensor - Execute Callbacks");
					for (OnCreateAction a : OnCreateList) {
						a.run(sn, cn, min, max, trans, unit, con);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Create Sensor - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateSensor (String sn, String cn, double min, double max, double trans, String unit, String con) {
		Logger.log("DB Update Sensor : "+Config.getConfig(Config.DATABASE_UPDATE_SENSOR_SQL_FILE_KEY));
		if (!Cache.sensorMap.containsKey(sn)) {
			Logger.log("DB Upate Sensor[Cache] - Sensor doesn't exist!");
			return false;
		} else {
			Object [] o=Cache.sensorMap.get(sn);
			if (o[0].equals(sn) && o[1].equals(cn) && o[2].equals(min) && o[3].equals(max) && o[4].equals(trans) && o[5].equals(unit) && o[6].equals(con)) {
				Logger.log("DB Update Sensor[Cache] - Information is the same!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Sensor - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_SENSOR_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Sensor - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, cn);
						ps.setDouble(2, min);
						ps.setDouble(3, max);
						ps.setDouble(4, trans);
						ps.setString(5, unit);
						ps.setString(6, con);
						ps.setString(7, sn);
						Logger.log("DB Update Sensor - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Sensor - Execute "+ps.toString());
						ps.execute();
						
						Logger.log("DB Update Sensor - Execute Callbacks");
						for (OnUpdateAction a : OnUpdateList) {
							a.run(sn, sn, cn, min, max, trans, unit, con);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Sensor - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean deleteSensor (String sn) {
		Logger.log("DB Delete Sensor : "+Config.getConfig(Config.DATABASE_DELETE_SENSOR_SQL_FILE_KEY));
		if (!Cache.sensorMap.containsKey(sn)) {
			Logger.log("DB Delete Sensor[Cache] - Sensor doesn't exist!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete Sensor - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_SENSOR_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Delete Sensor - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, sn);
					Logger.log("DB Delete Sensor - Execute - "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Delete Sensor - Execute "+ps.toString());
					ps.execute();
					
					Logger.log("DB Delete Sensor - Execute Callbacks");
					for (OnDeleteAction a : OnDeleteList) {
						a.run(sn);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete Sensor - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}

	}

}
