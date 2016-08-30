package Chi;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class DatabaseController extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String n, String s, double x, double y, int t);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldN, String n, String s, double x, double y, int t);
	}
	
	public static interface OnReportAction {
		public void run (String n);
	}
	
	public static interface OnDeleteAction {
		public void run (String n);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnReportAction> OnReportList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseController - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseController - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnReportAction (OnReportAction a) {
		if (!OnReportList.contains(a)) {
			Logger.log("DatabaseController - Registered "+a.toString()+" to OnReport callback");
			OnReportList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseController - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static boolean createController (String n, String s, double x, double y, int t) {
		Logger.log("DB Create Controller : "+Config.getConfig(Config.DATABASE_CREATE_CONTROLLER_SQL_FILE_KEY));
		if (Cache.Controllers.map.containsKey(n)) {
			Logger.log("DB Create Controller[Cache] - Controller already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Create Controller - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_CONTROLLER_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Create Controller - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					ps.setString(2, s);
					ps.setDouble(3, x);
					ps.setDouble(4, y);
					ps.setInt(5, t);
					ps.setDate(6,new Date(0));
					Logger.log("DB Create Controller - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Create Controller - Execute "+ps.toString());
					ps.execute();
					
					for (OnCreateAction a : OnCreateList) {
						a.run(n, s, x, y, t);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Create Controller - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateController (String n, String s, double x, double y, int t) {
		Logger.log("DB Update Controller : "+Config.getConfig(Config.DATABASE_UPDATE_CONTROLLER_SQL_FILE_KEY));
		if (!Cache.Controllers.map.containsKey(n)) {
			Logger.log("DB Update Controller[Cache] - Controller doesn't exist!");
			return false;
		} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Controller - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_CONTROLLER_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Controller - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, s);
						ps.setDouble(2, x);
						ps.setDouble(3, y);
						ps.setInt(4, t);
						ps.setString(5, n);
						Logger.log("DB Update Controller - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Controller - Execute "+ps.toString());
						ps.execute();
						
						for (OnUpdateAction a : OnUpdateList) {
							a.run(n, n, s, x, y, t);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Controller - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
			}
	}
	
	public static boolean updateControllerReportTime (String n, LocalDateTime dt) {
		Logger.log("DB Update Controller Report Time : "+Config.getConfig(Config.DATABASE_UPDATE_CONTROLLER_REPORT_TIME_SQL_FILE_KEY));
		if (!Cache.Controllers.map.containsKey(n)) {
			Logger.log("DB Update Controller Report Time [Cache] - Controller doesn't exist!");
			return false;
		} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update Controller Report Time - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_CONTROLLER_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update Controller Report Time - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setDate(1, Utility.localDateTimeToDate(dt));
						ps.setString(2, n);
						Logger.log("DB Update Controller Report Time - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update Controller Report Time - Execute "+ps.toString());
						ps.execute();
						
						for (OnReportAction a : OnReportList) {
							a.run(n);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update Controller - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;
		}
	}
	
	public static boolean deleteController (String sn) {
		Logger.log("DB Delete Controller : "+Config.getConfig(Config.DATABASE_DELETE_CONTROLLER_SQL_FILE_KEY));
		if (!Cache.Controllers.map.containsKey(sn)) {
			Logger.log("DB Create Controller[Cache] - Controller doesn't exist!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete Controller - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_CONTROLLER_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Delete Controller - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, sn);
					Logger.log("DB Delete Controller - Execute - "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Delete Controller - Execute "+ps.toString());
					ps.execute();
					
					for (OnDeleteAction a : OnDeleteList) {
						a.run(sn);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete Controller - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}

}
