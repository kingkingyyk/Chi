package com.kingkingyyk.chi.data;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="Chi", name = "nodes", 
		readConsistency="ANY", writeConsistency="ANY", 
		caseSensitiveKeyspace=false, caseSensitiveTable=false)
//CREATE TABLE Chi.nodes(id UUID PRIMARY KEY, name text, site_id UUID, mapX double, mapY double);
public class Node {
	@PartitionKey
	@Column(name = "id") private UUID id;
	@Column(name = "name") private String name;
	@Column(name = "site_id") private UUID siteId;
	@Column(name = "mapX") private double mapx;
	@Column(name = "mapY") private double mapy;
	
	public Node() {}
}
