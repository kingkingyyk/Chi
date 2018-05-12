package com.kingkingyyk.chi.data.db;

import java.lang.reflect.Field;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.kingkingyyk.chi.config.Cassandra;

public class Database {
	private static final String DATA_OBJECT_PACKAGE = "com.kingkingyyk.chi.data.obj";
	private static final String KEYSPACE_NAME = "Chi";
	private static Cluster cluster;
	private static Session session;
	
	public static void initialize(Cassandra cassandra) throws Exception {
	    cluster = Cluster.builder().withClusterName(cassandra.clusterName).addContactPointsWithPorts(cassandra.endpoints).build();
	    session = cluster.connect();
	    session.execute("CREATE KEYSPACE IF NOT EXISTS "+KEYSPACE_NAME+" WITH REPLICATION = {"+cassandra.replication+"}");
	    
	    Reflections reflections = new Reflections(new ConfigurationBuilder()
					    	     .setUrls(ClasspathHelper.forPackage(DATA_OBJECT_PACKAGE))
					    	     .setScanners(new SubTypesScanner(false))
					    	     );
	    Set<Class<? extends Object>> dataObjs = reflections.getSubTypesOf(Object.class);
	    for (Class<? extends Object> dataObj : dataObjs) if (dataObj.getName().startsWith("com.kingkingyyk.chi.data.obj")) for (Field f : dataObj.getFields()) session.execute(dataObj.getField("CREATION_SQL").get(String.class).toString());
	}
	
	public static void shutdown() {
		session.close();
		cluster.close();
	}
	
	public static Session getSession() {
		return Database.session;
	}
	
}
