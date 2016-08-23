package Chi;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class DatabaseReading extends DatabaseCassandra {

	public static ResultSet getSensorReading () {
		return runSQLFromFileAndGetData("DB Get Sensor Reading",Config.getConfig(Config.DATABASE_RECORD_GETTING_SQL_FILE_KEY));
	}
	
	public static boolean storeReading (String sn, LocalDateTime time, double v) {
		String ip=Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY);
		int port=Integer.parseInt(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PORT_KEY));
		Logger.log("DB Store Reading : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Store Reading - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_CASSANDRA_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Store Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
			sql[0].setString(0, sn);
			sql[0].setTimestamp(1,Timestamp.valueOf(time));
			sql[0].setInt(2, time.getDayOfWeek().getValue());
			sql[0].setInt(3, time.getDayOfMonth());
			sql[0].setInt(4, time.getMonth().getValue());
			sql[0].setInt(5, time.getYear());
			sql[0].setInt(6, time.getHour());
			sql[0].setInt(7, time.getMinute());
			sql[0].setInt(8, time.getSecond());
			sql[0].setDouble(9, v);
			executeSQL("DB Store Reading", session, sql[0]);
			
			session.close();
			cluster.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("DB Store Reading - Database connection fail!");
		} catch (Exception e) {
			Logger.log("DB Store Reading - Error - "+e.getMessage());
			e.printStackTrace();
		}
		if (cluster!=null) {
			cluster.close();
		}
		return false;
	}
	
}
