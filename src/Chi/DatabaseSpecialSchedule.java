package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseSpecialSchedule extends DatabaseHSQL {

	public static ResultSet getSpecialScheduleName () {
		return runSQLFromFileAndGetData("DB Get Special Schedule Name",Config.getConfig(Config.DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getSpecialSchedules () {
		return runSQLFromFileAndGetData("DB Get Special Schedules",Config.getConfig(Config.DATABASE_QUERY_SPECIAL_SCHEDULE_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createSpecialSchedule (String sn, String an, int year, int month, int day, String rn, boolean ao, int pr, boolean en) {
		Logger.log("DB Create Special Schedule : "+Config.getConfig(Config.DATABASE_CREATE_SPECIAL_SCHEDULE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Create Special Schedule - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_SPECIAL_SCHEDULE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Create Special Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, sn);
				ps.setString(2, an);
				ps.setInt(3, year);
				ps.setInt(4, month);
				ps.setInt(5, day);
				ps.setString(6, rn);
				ps.setBoolean(7, ao);
				ps.setInt(8, pr);
				ps.setBoolean(9, en);
				
				Logger.log("DB Create Special Schedule - Execute");
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Create Special Schedule - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create Special Schedule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateSpecialSchedule (String oldSN, String sn, String an, int year, int month, int day, String rn, boolean ao, int pr, boolean en) {
		Logger.log("DB Update Special Schedule : "+Config.getConfig(Config.DATABASE_UPDATE_SPECIAL_SCHEDULE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Update Special Schedule - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_SPECIAL_SCHEDULE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Update Special Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, sn);
				ps.setString(2, an);
				ps.setInt(3, year);
				ps.setInt(4, month);
				ps.setInt(5, day);
				ps.setString(6, rn);
				ps.setBoolean(7, ao);
				ps.setInt(8, pr);
				ps.setBoolean(9, en);
				ps.setString(10, oldSN);
				Logger.log("DB Update Special Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Update Special Schedule - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update Special Schedule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteSpecialSchedule (String sn) {
		Logger.log("DB Delete Special Schedule : "+Config.getConfig(Config.DATABASE_DELETE_SPECIAL_SCHEDULE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Delete Special Schedule - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_SPECIAL_SCHEDULE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Delete Special Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, sn);
				Logger.log("DB Delete Special Schedule - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Delete Special Schedule - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Delete Special Schedule - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

}
