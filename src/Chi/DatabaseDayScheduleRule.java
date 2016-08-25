package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

public class DatabaseDayScheduleRule extends DatabaseHSQL {

	public static interface OnCreateAction {
		public void run (String n, int sh, int sm, int eh, int em);
	}
	
	public static interface OnUpdateAction {
		public void run (String oldN, String n, int sh, int sm, int eh, int em);
	}
	
	public static interface OnDeleteAction {
		public void run (String n);
	}
	
	private static LinkedList<OnCreateAction> OnCreateList=new LinkedList<>();
	private static LinkedList<OnUpdateAction> OnUpdateList=new LinkedList<>();
	private static LinkedList<OnDeleteAction> OnDeleteList=new LinkedList<>();
	
	public static void registerOnCreateAction (OnCreateAction a) {
		if (!OnCreateList.contains(a)) {
			Logger.log("DatabaseDayScheduleRule - Registered "+a.toString()+" to OnCreate callback");
			OnCreateList.add(a);
		}
	}
	
	public static void unregisterOnCreateAction (OnCreateAction a) {
		if (OnCreateList.contains(a)) {
			Logger.log("DatabaseDayScheduleRule - Unregistered "+a.toString()+" to OnCreate callback");
			OnCreateList.remove(a);
		}
	}
	
	public static void registerOnUpdateAction (OnUpdateAction a) {
		if (!OnUpdateList.contains(a)) {
			Logger.log("DatabaseDayScheduleRule - Registered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.add(a);
		}
	}
	
	public static void unregisterOnUpdateAction (OnUpdateAction a) {
		if (OnUpdateList.contains(a)) {
			Logger.log("DatabaseDayScheduleRule - Unregistered "+a.toString()+" to OnUpdate callback");
			OnUpdateList.remove(a);
		}
	}
	
	public static void registerOnDeleteAction (OnDeleteAction a) {
		if (!OnDeleteList.contains(a)) {
			Logger.log("DatabaseDayScheduleRule - Registered "+a.toString()+" to OnDelete callback");
			OnDeleteList.add(a);
		}
	}
	
	public static void unregisterOnDeleteAction (OnDeleteAction a) {
		if (OnDeleteList.contains(a)) {
			Logger.log("DatabaseDayScheduleRule - Unregistered "+a.toString()+" to OnDelete callback");
			OnDeleteList.remove(a);
		}
	}
	
	public static ResultSet getDayScheduleRuleNames () {
		return runSQLFromFileAndGetData("DB Get Day Schedule Rule Name List",Config.getConfig(Config.DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getDayScheduleRules () {
		return runSQLFromFileAndGetData("DB Get Day Schedule Rules",Config.getConfig(Config.DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createDayScheduleRule (String n, int sh, int sm, int eh, int em) {
		Logger.log("DB Create DayScheduleRule : "+Config.getConfig(Config.DATABASE_CREATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
		if (Cache.DayScheduleRuleMap.containsKey(n)) {
			Logger.log("DB CreateDayScheduleRule[Cache] : Rule already exists!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Create DayScheduleRule - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Create DayScheduleRule - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					ps.setInt(2, sh);
					ps.setInt(3, sm);
					ps.setInt(4, eh);
					ps.setInt(5, em);
					Logger.log("DB Create DayScheduleRule - Execute");
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Create DayScheduleRule - Execute "+ps.toString());
					ps.execute();
					
					for (OnCreateAction a : OnCreateList) {
						a.run(n,sh,sm,eh,em);
					}
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Create DayScheduleRule - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}
	
	public static boolean updateDayScheduleRule  (String oldN, String n, int sh, int sm, int eh, int em) {
		Logger.log("DB Update DayScheduleRule : "+Config.getConfig(Config.DATABASE_UPDATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
		if (!Cache.DayScheduleRuleMap.containsKey(oldN)) {
			Logger.log("DB CreateDayScheduleRule[Cache] : Rule doesn't exist!");
			return false;
		} else {
			Object [] o=Cache.DayScheduleRuleMap.get(oldN);
			if (o[0].equals(n) && o[1].equals(sh) && o[2].equals(sm) && o[3].equals(eh) && o[4].equals(em)) {
				Logger.log("DB CreateDayScheduleRule[Cache] : Information is the same!");
				return true;
			} else {
				try {
					Connection c = getConnection();
					if (c!=null) {
						Logger.log("DB Update DayScheduleRule - Database connection OK!");
						String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
						PreparedStatement ps=c.prepareStatement(sql[0]);
						Logger.log("DB Update DayScheduleRule - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[1]);
						ps.setString(1, n);
						ps.setInt(2, sh);
						ps.setInt(3, sm);
						ps.setInt(4, eh);
						ps.setInt(5, em);
						ps.setString(6,oldN);
						Logger.log("DB Update DayScheduleRule - Execute "+ps.toString());
						ps.execute();
						
						ps=c.prepareStatement(sql[2]);
						Logger.log("DB Update DayScheduleRule - Execute "+ps.toString());
						ps.execute();
						
						for (OnUpdateAction a : OnUpdateList) {
							a.run(oldN,n,sh,sm,eh,em);
						}
					}
					c.close();
					return true;
				} catch (Exception e) {
					Logger.log("DB Update DayScheduleRule - Error - "+e.getMessage());
					e.printStackTrace();
				}
				return false;	
			}
		}
	}
	
	public static Object [] getDayScheduleRule (String n) {
		Logger.log("DB Get Day Schedule Rule : "+Config.getConfig(Config.DATABASE_QUERY_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			Object [] o=null;
			if (c!=null) {
				Logger.log("DB Get Day Schedule Rule  - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_QUERY_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, n);
				Logger.log("DB Get Day Schedule Rule  - Execute "+ps.toString());
				ResultSet rs=ps.executeQuery(); rs.next();
				o=new Object [] {rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5)};
			}
			c.close();
			return o;
		} catch (Exception e) {
			Logger.log("DB Create DayScheduleRule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean deleteDayScheduleRule (String n) {
		Logger.log("DB Delete DayScheduleRule : "+Config.getConfig(Config.DATABASE_DELETE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
		if (!Cache.DayScheduleRuleMap.containsKey(n)) {
			Logger.log("DB CreateDayScheduleRule[Cache] : Rule doesn't exist!");
			return false;
		} else {
			try {
				Connection c = getConnection();
				if (c!=null) {
					Logger.log("DB Delete DayScheduleRule - Database connection OK!");
					String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
					PreparedStatement ps=c.prepareStatement(sql[0]);
					Logger.log("DB Delete DayScheduleRule - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[1]);
					ps.setString(1, n);
					Logger.log("DB Delete DayScheduleRule - Execute "+ps.toString());
					ps.execute();
					
					ps=c.prepareStatement(sql[2]);
					Logger.log("DB Delete DayScheduleRule - Execute "+ps.toString());
					ps.execute();
				}
				c.close();
				return true;
			} catch (Exception e) {
				Logger.log("DB Delete DayScheduleRule - Error - "+e.getMessage());
				e.printStackTrace();
			}
			return false;
		}
	}

}
