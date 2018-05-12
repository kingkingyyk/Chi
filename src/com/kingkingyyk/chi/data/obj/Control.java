package com.kingkingyyk.chi.data.obj;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="Chi", name = "controls", 
		readConsistency="ANY", writeConsistency="ANY", 
		caseSensitiveKeyspace=false, caseSensitiveTable=false)
public class Control {
	public static final String CREATION_SQL = "CREATE TABLE IF NOT EXISTS Chi.controls(id UUID PRIMARY KEY, name text, node_id UUID, site_id UUID, mapX double, mapY double);";
	
	@PartitionKey
	@Column(name = "id") private UUID id;
	@Column(name = "name") private String name;
	@Column(name = "node_id") private UUID nodeId;
	@Column(name = "site_id") private UUID siteId;
	@Column(name = "mapX") private double mapX;
	@Column(name = "mapY") private double mapY;
	
	public Control() {}
}
