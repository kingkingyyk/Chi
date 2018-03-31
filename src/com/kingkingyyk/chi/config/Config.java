package com.kingkingyyk.chi.config;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Config {
	
	public static final int REST_PORT = 5000;
	public static String CASSANDRA_CLUSTER_NAME = "Test Cluster";
	public static List<InetSocketAddress> CASSANDRA_ENDPOINTS=new ArrayList<>();
	
	public static void load() throws Exception {
		InetSocketAddress e=InetSocketAddress.createUnresolved("192.168.0.48",9042);
		CASSANDRA_ENDPOINTS.add(e);
	}
}
