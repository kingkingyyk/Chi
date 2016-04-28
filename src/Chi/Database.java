package Chi;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class Database {

	public static boolean testConnection(String ip, int port) {
		try {
			Logger.log("Connecting to database : "+ip+":"+port);
			Cluster cluster=Cluster.builder().withPort(port).addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("Database connection OK!");
			session.close();
			return true;
		} catch (NoHostAvailableException e) {
			Logger.log("Database connection fail!");
		}
		return false;
	}
	
}
