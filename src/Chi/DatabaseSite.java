package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseSite extends DatabaseHSQL {

	public static ResultSet getSiteList () {
		return runSQLFromFileAndGetData("DB Get Site Name List",Config.getConfig(Config.DATABASE_QUERY_SITE_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getSites () {
		return runSQLFromFileAndGetData("DB Get Site",Config.getConfig(Config.DATABASE_QUERY_SITE_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createSite (String n, String u) {
		Logger.log("DB Create Site : "+Config.getConfig(Config.DATABASE_CREATE_SITE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Create Site - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_SITE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Create Site - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, n);
				ps.setString(2, u);
				Logger.log("DB Create Site - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Create Site - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create Site - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateSite (String oldN, String n, String u) {
		Logger.log("DB Update Site : "+Config.getConfig(Config.DATABASE_UPDATE_SITE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Update Site - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_SITE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Update Site - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, n);
				ps.setString(2, u);
				ps.setString(3, oldN);
				Logger.log("DB Update Site - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Update Site - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update Site - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteSite (String n) {
		Logger.log("DB Delete Site : "+Config.getConfig(Config.DATABASE_DELETE_SITE_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Delete Site - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_SITE_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Delete Site - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, n);
				Logger.log("DB Delete Site - Execute - "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Delete Site - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Delete Site - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

}
