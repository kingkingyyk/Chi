package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseDayScheduleRule extends DatabaseHSQL {

	public static ResultSet getDayScheduleRuleNames () {
		return runSQLFromFileAndGetData("DB Get Day Schedule Rule Name List",Config.getConfig(Config.DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getDayScheduleRules () {
		return runSQLFromFileAndGetData("DB Get Day Schedule Rules",Config.getConfig(Config.DATABASE_QUERY_DAY_SCHEDULE_RULE_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createDayScheduleRule (String n, int sh, int sm, int eh, int em) {
		Logger.log("DB Create DayScheduleRule : "+Config.getConfig(Config.DATABASE_CREATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
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
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create DayScheduleRule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateDayScheduleRule  (String oldN, String n, int sh, int sm, int eh, int em) {
		Logger.log("DB Update DayScheduleRule : "+Config.getConfig(Config.DATABASE_UPDATE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
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
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update DayScheduleRule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteDayScheduleRule (String n) {
		Logger.log("DB Delete DayScheduleRule : "+Config.getConfig(Config.DATABASE_DELETE_DAY_SCHEDULE_RULE_SQL_FILE_KEY));
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
