package com.kingkingyyk.chi.data.obj;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="Chi", name = "sites", 
		readConsistency="ANY", writeConsistency="ANY", 
		caseSensitiveKeyspace=false, caseSensitiveTable=false)
public class Site {
	public static final String CREATION_SQL = "CREATE TABLE IF NOT EXISTS Chi.sites(id UUID PRIMARY KEY, name text, mapURL text);";
	@PartitionKey
	@Column(name = "id") private UUID id;
	@Column(name = "name") private String name;
	@Column(name = "mapURL") private String mapURL;
	
	public Site() {}
}
