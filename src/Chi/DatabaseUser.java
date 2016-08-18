package Chi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DatabaseUser extends DatabaseHSQL {

	public static ResultSet getUsernameList (String ip, int port) {
		return runSQLFromFileAndGetData("DB Get Username List",ip,port,Config.getConfig(Config.DATABASE_QUERY_USERNAME_ALL_SQL_FILE_KEY));
	}
	
	public static ResultSet getUsers (String ip, int port) {
		return runSQLFromFileAndGetData("DB Get Users",ip,port,Config.getConfig(Config.DATABASE_QUERY_USER_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createUserCredential (String ip, int port, String user, String pw, int lvl, String status) {
		Logger.log("DB Create User Credential : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		try {
			Connection c = DriverManager.getConnection(getAddress(), Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY), Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY));
			if (c!=null) {
				Logger.log("DB Create User Credential - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_USER_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, user);
				ps.setString(2, pw);
				ps.setInt(3, lvl);
				ps.setString(4, status);
				ps.setTimestamp(5,Timestamp.valueOf(LocalDateTime.now()));
				Logger.log("DB Create User Credential - Execute");
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create User Credential - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateUserCredentialPassword (String ip, int port, String user, String pw, int lvl, String status) {
		Logger.log("DB Update User Credential : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		try {
			Connection c = DriverManager.getConnection(getAddress(), Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY), Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY));
			if (c!=null) {
				Logger.log("DB Update User Credential - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, pw);
				ps.setInt(2, lvl);
				ps.setString(3, status);
				ps.setString(4, user);
				Logger.log("DB Update User Credential - Execute");
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create User Credential - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateUserCredentialNoPassword (String ip, int port, String user, int lvl, String status) {
		Logger.log("DB Update User Credential/2 : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		try {
			Connection c = DriverManager.getConnection(getAddress(), Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY), Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY));
			if (c!=null) {
				Logger.log("DB Update User Credential/2 - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setInt(1, lvl);
				ps.setString(2, status);
				ps.setString(3, user);
				Logger.log("DB Update User Credential/2 - Execute");
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create User Credential/2 - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteUser (String ip, int port, String user) {
		Logger.log("DB Update User Credential/2 : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		try {
			Connection c = DriverManager.getConnection(getAddress(), Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY), Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY));
			if (c!=null) {
				Logger.log("DB Update User Credential/2 - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_USER_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				ps.setString(1, user);
				Logger.log("DB Update User Credential/2 - Execute");
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create User Credential/2 - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static void initialize() {
		
	}
}
