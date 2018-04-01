package com.kingkingyyk.chi.config;

public class Config {
	protected static final String CONFIG_FOLDER = "conf";
	protected static final String DEFAULT_CONFIG_NAME = "_default";
	public static Cassandra cassandra;
	public static REST rest;

	public static void load() throws Exception {
		Config.cassandra = Cassandra.load();
		Config.rest = REST.load();
	}
}
