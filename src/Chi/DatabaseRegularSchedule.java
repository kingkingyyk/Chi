package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DatabaseRegularSchedule extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String sn, String an, int day, String rn, boolean ao, int pr, boolean en);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldSN, String sn, String an, int day, String rn, boolean ao, int pr, boolean en);
	}
	
	public static interface OnDeleteAction {
		public void run (String sn);
	}
	
	private static ArrayList<OnCreateAction> OnCreateList=new ArrayList<>();
	private static ArrayList<OnUpdateAction> OnUpdateList=new ArrayList<>();
	private static ArrayList<OnDeleteAction> OnDeleteList=new ArrayList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseRegularSchedule - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static ResultSet getRegularScheduleName () {
		return runSQLFromFileAndGetData("DB Get Regular Schedule Name",Config.getConfig(Config.DATABASE_QUERY_REGULAR_SCHEDULE_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getRegularSchedules () {
		return runSQLFromFileAndGetData("DB Get Regular Schedules",Config.getConfig(Config.DATABASE_QUERY_REGULAR_SCHEDULE_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createRegularSchedule (String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
		Logger.log("DB Create Regular Schedule : "+Config.getConfig(Config.DATABASE_CREATE_REGULAR_SCHEDULE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Create Regular Schedule - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_REGULAR_SCHEDULE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Create Regular Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, sn);
				ps.setString(2, an);
				ps.setInt(3, day);
				ps.setString(4, rn);
				ps.setBoolean(5, ao);
				ps.setInt(6, pr);
				ps.setBoolean(7, en);
				Logger.log("DB Create Regular Schedule - Execute "+ps.toString());
				ps.execute();
				
				Logger.log("DB Create Regular Schedule - Execute Callbacks");
				for (OnCreateAction a : OnCreateList) {
					a.run(sn,an,day,rn,ao,pr,en);
				}
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Create Regular Schedule - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create Regular Schedule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateRegularSchedule (String oldSN, String sn, String an, int day, String rn, boolean ao, int pr, boolean en) {
		Logger.log("DB Update Regular Schedule : "+Config.getConfig(Config.DATABASE_UPDATE_REGULAR_SCHEDULE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Update Regular Schedule - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_REGULAR_SCHEDULE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Update Regular Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, sn);
				ps.setString(2, an);
				ps.setInt(3, day);
				ps.setString(4, rn);
				ps.setBoolean(5, ao);
				ps.setInt(6, pr);
				ps.setBoolean(7, en);
				ps.setString(8, oldSN);
				

				Logger.log("DB Update Regular Schedule - Execute "+ps.toString());
				ps.execute();
				
				Logger.log("DB Update Regular Schedule - Execute Callbacks");
				for (OnUpdateAction a : OnUpdateList) {
					a.run(oldSN,sn,an,day,rn,ao,pr,en);
				}
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Update Regular Schedule - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update Regular Schedule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteRegularSchedule (String sn) {
		Logger.log("DB Delete Regular Schedule : "+Config.getConfig(Config.DATABASE_DELETE_REGULAR_SCHEDULE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Delete Regular Schedule - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_REGULAR_SCHEDULE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Delete Regular Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, sn);
				Logger.log("DB Delete Regular Schedule - Execute "+ps.toString());
				ps.execute();
				
				Logger.log("DB Delete Regular Schedule - Execute Callbacks");
				for (OnDeleteAction a : OnDeleteList) {
					a.run(sn);
				}
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Delete Regular Schedule - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Delete Regular Schedule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

}
