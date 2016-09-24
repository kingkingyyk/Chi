package Chi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class DatabaseCassandra {
	
	private static Cluster cluster=null;
	
	public static void initialize() {
		cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY))/*
				*/.withPort(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY)))/*
				*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)).build();
	}
	
	protected static Cluster getCluster() {
		return cluster;
	}
	
	public static boolean testConnection() {
		return runSQLFromFile("DB Test Connection",null);
	}
	
	public static boolean freshStart () {
		return runSQLFromFile("DB Init",Config.getConfig(Config.DATABASE_INIT_SQL_CASSANDRA_FILE_KEY));
	}
	
	public static boolean createTables () {
		return runSQLFromFile("DB Create Tables",Config.getConfig(Config.DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_KEY));
	}
	
	public static boolean reset () {
		return runSQLFromFile("DB Reset",Config.getConfig(Config.DATABASE_RESET_SQL_CASSANDRA_FILE_KEY));
	}

	public static boolean testKeyspace () {
		Cluster cluster=null;
		try {
			Logger.log("DB Test Keyspace - Connecting to database : "+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)+":"+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
			cluster=getCluster();
			Session session=cluster.connect();
			Logger.log("DB Test Keyspace - Database connection OK!");
			KeyspaceMetadata ks=cluster.getMetadata().getKeyspace(Config.APP_NAME);
			session.close();
			return ks!=null;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Test Keyspace - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Test Keyspace - Error - "+e.getMessage());
		}
		return false;
	}
	
	public static boolean runSQL(String cmdName, String sql) {
		Cluster cluster=null;
		try {
			Logger.log(cmdName+" - Connecting to database : "+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)+":"+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
			cluster=getCluster();
			Session session=cluster.connect();
			Logger.log(cmdName+" - Database connection OK!");
			System.out.println("Result : "+executeSQL(cmdName,session,sql).toString());
			session.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log(cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(cmdName+" - Error - "+e.getMessage());
		}
		return false;
	}
	
	public static boolean runSQLFromFile(String cmdName, String filename) {
		Logger.log("Database - Run SQL From File : "+filename);
		Cluster cluster=null;
		try {
			Logger.log(cmdName+" - Connecting to database : "+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)+":"+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
			cluster=getCluster();
			Session session=cluster.connect();
			Logger.log(cmdName+" - Database connection OK!");
			if (filename!=null) {
				String [] sql=getSQLStatementFromFile(filename);
				for (int i=0;i<sql.length;i++) {
					if (!sql[i].equals("")) {
						executeSQL(cmdName,session,sql[i]);
					}
				}
			}
			session.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log(cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(cmdName+" - Error - "+e.getMessage());
		}
		return false;
	}

	protected static ResultSet runSQLFromFileAndGetData(String cmdName, String filename) {
		Logger.log("Database - Run SQL From File : "+filename);
		Cluster cluster=null;
		try {
			Logger.log(cmdName+" - Connecting to database : "+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)+":"+Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
			cluster=getCluster();
			Session session=cluster.connect();
			session=cluster.connect("Chi");
			Logger.log(cmdName+" - Database connection OK!");
			ResultSet rs=null;
			if (filename!=null) {
				String [] sql=getSQLStatementFromFile(filename);
				for (int i=0;i<sql.length;i++) {
					rs=executeSQL(cmdName,session,sql[i]);
				}
			}
			session.close();
			return rs;
		} catch (NoHostAvailableException e) {
			Logger.log(cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(cmdName+" - Error - "+e.getMessage());
		}
		return null;
	}
	
	protected static ResultSet executeSQL(String cmdName, Session session, String statement) {
		Logger.log(cmdName+" - Execute SQL : "+statement);
		ResultSet rs=session.execute(statement);
		Logger.log(cmdName+" - SQL Result : "+rs.toString());
		return rs;
	}
	
	protected static ResultSet executeSQL(String cmdName, Session session, BoundStatement statement) {
		Logger.log(cmdName+" - Execute SQL : "+statement.toString());
		ResultSet rs=session.execute(statement);
		Logger.log(cmdName+" - SQL Result : "+rs.toString());
		return rs;
	}
	
	protected static String [] getSQLStatementFromFile(String path) throws IOException {
		ArrayList<String> statements=new ArrayList<>();
		StringBuilder sqlStatement=new StringBuilder();
		BufferedReader br=new BufferedReader(new FileReader(path));
		String s;
		while ((s=br.readLine())!=null) {
			if (s.equals("@")) {
				statements.add(sqlStatement.toString());
				sqlStatement=new StringBuilder();
			} else {
				sqlStatement.append(s);
				sqlStatement.append(" ");
			}
		}
		statements.add(sqlStatement.toString());
		br.close();
		return statements.toArray(new String[statements.size()]);
	}
	
	protected static BoundStatement [] getBoundSQLStatementFromFile(Session ss, String path) throws IOException {
		String [] sql=getSQLStatementFromFile(path);
		BoundStatement [] psql=new BoundStatement[sql.length];
		for (int i=0;i<sql.length;i++) {
			psql[i]=ss.prepare(sql[i]).bind();
		}
		return psql;
	}
}
