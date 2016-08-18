package Chi;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class DatabaseReading extends Database{

	public static ResultSet getSensorReading (String ip, int port) {
		return runSQLFromFileAndGetData("DB Get Sensor Reading",ip,port,Config.getConfig(Config.DATABASE_RECORD_GETTING_SQL_FILE_KEY));
	}
	
	public static boolean storeReading (String ip, int port, String cn, String sn, LocalDateTime time, double v) {
		Logger.log("DB Store Reading : "+Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
		Cluster cluster=null;
		try {
			Logger.log("DB Store Reading - Connecting to database : "+ip+":"+port);
			cluster=Cluster.builder().withCredentials(Config.getConfig(Config.CONFIG_SERVER_DATABASE_USERNAME_KEY),Config.getConfig(Config.CONFIG_SERVER_DATABASE_PASSWORD_KEY))/*
			*/.withPort(port)/*
			*/.addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("DB Store Reading - Database connection OK!");
			
			BoundStatement [] sql=getBoundSQLStatementFromFile(session,Config.getConfig(Config.DATABASE_RECORD_READING_SQL_FILE_KEY));
			sql[0].setString(0, cn);
			sql[0].setString(1, sn);
			sql[0].setTimestamp(2,Timestamp.valueOf(time));
			sql[0].setInt(3, time.getDayOfWeek().getValue());
			sql[0].setInt(4, time.getDayOfMonth());
			sql[0].setInt(5, time.getMonth().getValue());
			sql[0].setInt(6, time.getYear());
			sql[0].setInt(7, time.getHour());
			sql[0].setInt(8, time.getMinute());
			sql[0].setInt(9, time.getSecond());
			sql[0].setDouble(10, v);
			Database.executeSQL("DB Store Reading", session, sql[0]);
			
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
