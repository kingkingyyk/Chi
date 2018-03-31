package com.kingkingyyk.chi;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.kingkingyyk.chi.config.Config;
import com.kingkingyyk.chi.connection.REST;

public class Chi {

	public static Cluster cluster;
	public static Session session;
	
	public static void main (String [] args) throws Exception {
		Config.load();
		
	    cluster = Cluster.builder().withClusterName(Config.CASSANDRA_CLUSTER_NAME).addContactPointsWithPorts(Config.CASSANDRA_ENDPOINTS).build();
	    session = cluster.connect();
	    new REST().start();
	}
	
}
