package Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.KeyspaceMetadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

import Chi.Config;
import Chi.Logger;

public class DatabaseCassandra {
	
	private static Cluster cluster=null;
	private static Session session=null;
	
	public static void initialize() {
		Thread t=new Thread() {
			public void run () {
				cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY))/*
						*/.withPort(Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY)))/*
						*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)).build();
				try {
					session=cluster.connect("Chi");
					DatabaseReading.initialize();
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Error when connecting to Cassandra server.\n"+e.getMessage(), "Chi", JOptionPane.ERROR_MESSAGE);
					session=cluster.connect();
				}
			}
		};
		t.start();
	}
	
	public static void Stop() {
		if (session!=null) session.close();
		if (cluster!=null) cluster.close();
	}
	
	protected static Cluster getCluster() {
		return cluster;
	}
	
	protected static Session getSession() {
		return session;
	}
	
	public static boolean testConnection() {
		return runSQLFromFile("DB Test Connection",null);
	}
	
	public static boolean freshStart () {
		boolean flag=runSQLFromFile("DB Init",Config.getConfig(Config.DATABASE_INIT_SQL_CASSANDRA_FILE_KEY));
		if (flag) session=cluster.connect("Chi");
		return flag;
	}
	
	public static boolean createTables () {
		return runSQLFromFile("DB Create Tables",Config.getConfig(Config.DATABASE_CREATE_TABLES_SQL_CASSANDRA_FILE_KEY));
	}
	
	public static boolean reset () {
		return runSQLFromFile("DB Reset",Config.getConfig(Config.DATABASE_RESET_SQL_CASSANDRA_FILE_KEY));
	}

	public static boolean testKeyspace () {
		Logger.log(Logger.LEVEL_INFO,"DatabaseCassandra - Test keyspace");
		try {
			KeyspaceMetadata ks=getCluster().getMetadata().getKeyspace(Config.APP_NAME);
			return ks!=null;
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_WARNING,"DB Test Keyspace - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,"DB Test Keyspace - "+e.getMessage());
		}
		return false;
	}
	
	public static boolean runSQL(String cmdName, String sql) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseCassandra - Run SQL");
		try {
			System.out.println("Result : "+executeSQL(cmdName,getSession(),sql).toString());
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_WARNING,cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,cmdName+" - "+e.getMessage());
		}
		return false;
	}
	
	public static boolean runSQLFromFile(String cmdName, String filename) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseCassandra - Run SQL From File : "+filename);
		try {
			if (filename!=null) {
				String [] sql=getSQLStatementFromFile(filename);
				for (int i=0;i<sql.length;i++) {
					if (!sql[i].equals("")) {
						executeSQL(cmdName,getSession(),sql[i]);
					}
				}
			}
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_WARNING,cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,cmdName+" - "+e.getMessage());
		}
		return false;
	}

	protected static ResultSet runSQLFromFileAndGetData(String cmdName, String filename) {
		Logger.log(Logger.LEVEL_INFO,"DatabaseCassandra - Run SQL From File : "+filename);
		try {
			ResultSet rs=null;
			if (filename!=null) {
				String [] sql=getSQLStatementFromFile(filename);
				for (int i=0;i<sql.length;i++) {
					rs=executeSQL(cmdName,getSession(),sql[i]);
				}
			}
			return rs;
		} catch (NoHostAvailableException e) {
			Logger.log(Logger.LEVEL_WARNING,cmdName+" - Database connection fail!");
		} catch (Exception e) {
			Logger.log(Logger.LEVEL_ERROR,cmdName+" - "+e.getMessage());
		}
		return null;
	}
	
	protected static ResultSet executeSQL(String cmdName, Session session, String statement) {
		Logger.log(Logger.LEVEL_INFO,cmdName+" - Execute SQL");
		ResultSet rs=session.execute(statement);
		Logger.log(Logger.LEVEL_INFO,cmdName+" - SQL Result : "+rs.toString());
		return rs;
	}
	
	protected static ResultSet executeSQL(String cmdName, Session session, BoundStatement statement) {
		Logger.log(Logger.LEVEL_INFO,cmdName+" - Execute SQL");
		ResultSet rs=session.execute(statement);
		Logger.log(Logger.LEVEL_INFO,cmdName+" - SQL Result : "+rs.toString());
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
