package Chi;

import java.util.Date;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class DatabaseUser extends Database {

	public static ResultSet getUsernameList (String ip, int port) {
		return runSQLFromFileAndGetData("DB Get Username List",ip,port,Config.getConfig(Config.DATABASE_QUERY_USERNAME_ALL_SQL_FILE_KEY));
	}
	
	public static ResultSet getUsers (String ip, int port) {
		return runSQLFromFileAndGetData("DB Get Users",ip,port,Config.getConfig(Config.DATABASE_QUERY_USER_ALL_SQL_FILE_KEY));
	}
	
	public static boolean createUserCredential (String ip, int port, String user, String pw, int lvl, String status) {
		Logger.log("DB Create User Credential : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Create User Credential - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Create User Credential - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_CREATE_USER_SQL_FILE_KEY));
			sql[0].setString(0, user);
			sql[0].setString(1, pw);
			sql[0].setInt(2, lvl);
			sql[0].setString(3, status);
			sql[0].setTimestamp(4, new Date());
			Database.executeSQL("DB Create User Credential", session, sql[0]);
			
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Create User Credential - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Create User Credential - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}
	
	public static boolean updateUserCredentialPassword (String ip, int port, String user, String pw, int lvl, String status) {
		Logger.log("DB Update User Credential : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Update User Credential - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Update User Credential - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_UPDATE_USER_W_PASSWORD_SQL_FILE_KEY));
			sql[0].setString(0, pw);
			sql[0].setInt(1, lvl);
			sql[0].setString(2, status);
			sql[0].setString(3, user);
			Database.executeSQL("DB Update User Credential", session, sql[0]);
			
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Update User Credential - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Update User Credential - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}
	
	public static boolean updateUserCredentialNoPassword (String ip, int port, String user, int lvl, String status) {
		Logger.log("DB Update User Credential/2 : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Update User Credential/2 - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Update User Credential/2 - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_UPDATE_USER_WO_PASSWORD_SQL_FILE_KEY));
			sql[0].setInt(0, lvl);
			sql[0].setString(1, status);
			sql[0].setString(2, user);
			Database.executeSQL("DB Update User Credential/2", session, sql[0]);
			
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Update User Credential/2 - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Update User Credential/2 - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}
	
	public static boolean deleteUser (String ip, int port, String user) {
		Logger.log("DB Delete User : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Delete User - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Delete User - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_DELETE_USER_SQL_FILE_KEY));
			sql[0].setString(0, user);
			Database.executeSQL("DB Delete User", session, sql[0]);
			
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Delete User - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Delete User - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}
}
