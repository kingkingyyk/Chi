package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseSensor extends DatabaseHSQL {

	public static ResultSet getSensorNameList () {
		return runSQLFromFileAndGetData("DB Get Sensor Name List",Config.getConfig(Config.DATABASE_QUERY_SENSOR_ALL_NAME_SQL_FILE_KEY));
	}
	
	public static ResultSet getSensors () {
		return runSQLFromFileAndGetData("DB Get Sensor",Config.getConfig(Config.DATABASE_QUERY_SENSOR_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createSensor (String sn, String cn, double min, double max, double trans, String unit) {
		Logger.log("DB Create Sensor : "+Config.getConfig(Config.DATABASE_CREATE_SENSOR_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Create Sensor - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_SENSOR_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, sn);
				ps.setString(2, cn);
				ps.setDouble(3, min);
				ps.setDouble(4, max);
				ps.setDouble(5, trans);
				ps.setString(6, unit);
				Logger.log("DB Create Sensor - Execute");
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create Sensor - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateSensor (String sn, String cn, double min, double max, double trans, String unit) {
		Logger.log("DB Update Sensor : "+Config.getConfig(Config.DATABASE_UPDATE_SENSOR_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Update Sensor - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_SENSOR_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, cn);
				ps.setDouble(2, min);
				ps.setDouble(3, max);
				ps.setDouble(4, trans);
				ps.setString(5, unit);
				ps.setString(6, sn);
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
	
	public static boolean deleteSensor (String sn) {
		Logger.log("DB Delete Sensor : "+Config.getConfig(Config.DATABASE_DELETE_SENSOR_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Delete Sensor - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_SENSOR_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, sn);
				Logger.log("DB Delete Sensor - Execute - "+ps.toString());
				ps.execute();
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
