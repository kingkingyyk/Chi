package Chi;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

public class Database {

	public static void initialize() {
		try {
			Logger.log("Connecting to database : "+Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY));
			Cluster cluster=Cluster.builder().addContactPoint(Config.getConfig(Config.CONFIG_SERVER_DATABASE_IP_KEY)).build();
			Session session=cluster.connect();
			Logger.log("Database connection OK!");
		} catch (NoHostAvailableException e) {
			e.printStackTrace();
			Logger.log("Database connection fail!");
		}
		
		
	}
	
}
