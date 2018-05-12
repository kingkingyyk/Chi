package com.kingkingyyk.chi.data.obj;

import java.time.LocalDateTime;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="Chi", name = "readings", 
	readConsistency="ANY", writeConsistency="ANY", 
	caseSensitiveKeyspace=false, caseSensitiveTable=false)
public class Reading {
	public static final String CREATION_SQL = "CREATE TABLE IF NOT EXISTS Chi.readings(id UUID, probe_id UUID, received_time timestamp, value double, tvalue double, PRIMARY KEY(id,received_time));";
	@PartitionKey(0) @Column(name = "id") private UUID id;
	@Column(name = "probe_id") private UUID probe_id;
	@PartitionKey(1) @ClusteringColumn @Column(name = "received_time") private LocalDateTime receivedTime;
	@Column(name = "value") private double rawValue;
	@Column(name = "tvalue") private double transformedValue;
	
	public Reading() {}
}
