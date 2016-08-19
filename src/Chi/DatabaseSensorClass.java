package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseSensorClass extends DatabaseHSQL {
	
	public static ResultSet getSensorClass () {
		return runSQLFromFileAndGetData("DB Get Sensor Class",Config.getConfig(Config.DATABASE_QUERY_SENSOR_CLASS_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createSensorClass (String name) {
		Logger.log("DB Create Sensor Class : "+Config.getConfig(Config.DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Create Sensor Class - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_SENSOR_CLASS_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, name);
				Logger.log("DB Create Sensor Class - Execute");
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create Sensor Class - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateSensorClass (String oldN, String newN) {
		Logger.log("DB Update Sensor : "+Config.getConfig(Config.DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Update Sensor - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_SENSOR_CLASS_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Update Sensor - SQL : "+ps.toString());
				ps.setString(1, oldN);
				ps.setString(2, newN);
				Logger.log("DB Update Sensor - Execute");
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update Sensor - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteSensorClass (String oldN) {
		Logger.log("DB Delete Sensor Class : "+Config.getConfig(Config.DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Delete Sensor Class - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_SENSOR_CLASS_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, oldN);
				Logger.log("DB Delete Sensor Class - Execute - "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, oldN);
				Logger.log("DB Delete Sensor Class - Execute - "+ps.toString());
				ps.execute();

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
