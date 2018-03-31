package com.kingkingyyk.chi.data;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="Chi", name = "nodes", 
		readConsistency="ALL", writeConsistency="ALL", 
		caseSensitiveKeyspace=false, caseSensitiveTable=false)

public class Node {
	@PartitionKey
	@Column(name = "id") private UUID id;
	@Column(name = "name") private String name;
	@Column(name = "mapX") private double mapX;
	@Column(name = "mapY") private double mapY;
	
	public Node() {}
}
