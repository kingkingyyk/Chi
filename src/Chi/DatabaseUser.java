package Chi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DatabaseUser extends DatabaseHSQL {

	public static ResultSet getUsernameList () {
		return runSQLFromFileAndGetData("DB Get Username List",Config.getConfig(Config.DATABASE_QUERY_USERNAME_ALL_SQL_FILE_KEY));
	}
	
	public static ResultSet getUsers () {
		return runSQLFromFileAndGetData("DB Get Users",Config.getConfig(Config.DATABASE_QUERY_USER_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createUserCredential (String user, String pw, int lvl, String status) {
		Logger.log("DB Create User : "+Config.getConfig(Config.DATABASE_CREATE_USER_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Create User - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_CREATE_USER_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Create User - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, user);
				ps.setString(2, pw);
				ps.setInt(3, lvl);
				ps.setString(4, status);
				ps.setTimestamp(5,Timestamp.valueOf(LocalDateTime.now()));
				Logger.log("DB Create User - Execute");
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Create User - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Create User - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateUserCredentialPassword (String user, String pw, int lvl, String status) {
		Logger.log("DB Update User Credential : "+Config.getConfig(Config.DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Update User Credential - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Update User Credential - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, pw);
				ps.setInt(2, lvl);
				ps.setString(3, status);
				ps.setString(4, user);
				Logger.log("DB Update User Credential - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Update User Credential - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Update User Credential - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateUserCredentialNoPassword (String user, int lvl, String status) {
		Logger.log("DB Update User Credential/2 : "+Config.getConfig(Config.DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Update User Credential/2 - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Update User Credential/2 - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setInt(1, lvl);
				ps.setString(2, status);
				ps.setString(3, user);
				Logger.log("DB Update User Credential/2 - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Update User Credential/2 - Execute "+ps.toString());
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
	
	public static boolean deleteUser (String user) {
		Logger.log("DB Delete User : "+Config.getConfig(Config.DATABASE_DELETE_USER_SQL_FILE_KEY));
		try {
			Connection c = getConnection();
			if (c!=null) {
				Logger.log("DB Delete User - Database connection OK!");
				String [] sql=getSQLStatementFromFile(Config.getConfig(Config.DATABASE_DELETE_USER_SQL_FILE_KEY));
				PreparedStatement ps=c.prepareStatement(sql[0]);
				Logger.log("DB Delete User - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[1]);
				ps.setString(1, user);
				Logger.log("DB Delete User - Execute "+ps.toString());
				ps.execute();
				
				ps=c.prepareStatement(sql[2]);
				Logger.log("DB Delete User - Execute "+ps.toString());
				ps.execute();
			}
			c.close();
			return true;
		} catch (Exception e) {
			Logger.log("DB Delete User - Error - "+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

}
