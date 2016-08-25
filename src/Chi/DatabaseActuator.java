package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseActuator extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String n, String u);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldN, String n, String u);
	}
	
	public static interface OnDeleteAction {
		public void run (String n);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseActuator - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseActuator - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseActuator - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static ResultSet getActuatorList () {
		return runSQLFromFileAndGetData("DB Get Site Name List",Config.getConfig(Config.DATABASE_QUERY_ACTUATOR_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getActuators () {
		return runSQLFromFileAndGetData("DB Get Site",Config.getConfig(Config.DATABASE_QUERY_ACTUATOR_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createActuator (String n, String u) {
		Logger.log("DB Create Actuator : "+Config.getConfig(Config.DATABASE_CREATE_ACTUATOR_SQL_FILE_KEY));
		if (Cache.actuatorMap.containsKey(n)) {
			Logger.log("DB Create Actuator - Actuator already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Create Actuator - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_ACTUATOR_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Create Actuator - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					ps.setString(2, u);
					ps.setString(3, "Pending Update");
					Logger.log("DB Create Actuator - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Create Actuator - Execute "+ps.toString());
					ps.execute();
					
					for (OnCreateAction a : OnCreateList) {
						a.run(n,u);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Create Actuator - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateActuator (String oldN, String n, String u) {
		Logger.log("DB Update Actuator : "+Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_SQL_FILE_KEY));
		if (!Cache.actuatorMap.containsKey(oldN)) {
			Logger.log("DB Update Actuator - Actuator doesn't exists!");
			return false;
		} else {
			Object [] o=Cache.actuatorMap.get(oldN);
			if (o[0].equals(n) && o[1].equals(u)) {
				Logger.log("DB Update Actuator - Information is the same!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Actuator - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Actuator - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, n);
						ps.setString(2, u);
						ps.setString(3, oldN);
						Logger.log("DB Update Actuator - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Actuator - Execute "+ps.toString());
						ps.execute();
						
						for (OnUpdateAction a : OnUpdateList) {
							a.run(oldN,n,u);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Actuator - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean updateActuatorStatus (String n, String st) {
		Logger.log("DB Update Actuator Status : "+Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_KEY));
		if (!Cache.actuatorMap.containsKey(n)) {
			Logger.log("DB Update Actuator Status - Actuator doesn't exists!");
			return false;
		} else {
			Object [] o=Cache.actuatorMap.get(n);
			if (o[0].equals(n) && o[2].equals(st)) {
				Logger.log("DB Update Actuator Status - Information is the same!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Actuator Status - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Actuator Status - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, st);
						ps.setString(2, n);
						Logger.log("DB Update Actuator Status - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Actuator Status - Execute "+ps.toString());
						ps.execute();
						
						for (OnUpdateAction a : OnUpdateList) {
							a.run(n,n,(String)o[1]);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Actuator Status - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
		}
	}
	
	public static boolean deleteActuator (String n) {
		Logger.log("DB Delete Actuator : "+Config.getConfig(Config.DATABASE_DELETE_ACTUATOR_SQL_FILE_KEY));
		if (!Cache.actuatorMap.containsKey(n)) {
			Logger.log("DB Update Actuator - Actuator doesn't exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete Actuator - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_ACTUATOR_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Update Actuator - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					Logger.log("DB Delete Actuator - Execute - "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Delete Actuator - Execute "+ps.toString());
					ps.execute();
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete Actuator - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}

}
