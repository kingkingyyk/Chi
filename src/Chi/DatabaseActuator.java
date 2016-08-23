package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseActuator extends DatabaseHSQL {

	public static ResultSet getActuatorList () {
		return runSQLFromFileAndGetData("DB Get Site Name List",Config.getConfig(Config.DATABASE_QUERY_ACTUATOR_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getActuators () {
		return runSQLFromFileAndGetData("DB Get Site",Config.getConfig(Config.DATABASE_QUERY_ACTUATOR_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createActuator (String n, String u) {
		Logger.log("DB Create Actuator : "+Config.getConfig(Config.DATABASE_CREATE_ACTUATOR_SQL_FILE_KEY));
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
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create Actuator - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateActuator (String oldN, String n, String u) {
		Logger.log("DB Update Actuator : "+Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_SQL_FILE_KEY));
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
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update Actuator - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateActuatorStatus (String n, String st) {
		Logger.log("DB Update Actuator Status : "+Config.getConfig(Config.DATABASE_UPDATE_ACTUATOR_STATUS_SQL_FILE_KEY));
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
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update Actuator Status - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteActuator (String n) {
		Logger.log("DB Delete Actuator : "+Config.getConfig(Config.DATABASE_DELETE_ACTUATOR_SQL_FILE_KEY));
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
