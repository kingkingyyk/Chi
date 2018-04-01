package com.kingkingyyk.chi.data.db;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.kingkingyyk.chi.config.Cassandra;

public class Database {
	private static Cluster cluster;
	private static Session session;
	
	public static void initialize(Cassandra cassandra) {
	    cluster = Cluster.builder().withClusterName(cassandra.clusterName).addContactPointsWithPorts(cassandra.endpoints).build();
	    session = cluster.connect();
	}
	
	public static void shutdown() {
		session.close();
		cluster.close();
	}
	
	public static Session getSession() {
		return Database.session;
	}
	
}
