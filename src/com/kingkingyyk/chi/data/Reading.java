package com.kingkingyyk.chi.data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="Chi", name = "readings", 
	readConsistency="ALL", writeConsistency="ALL", 
	caseSensitiveKeyspace=false, caseSensitiveTable=false)

public class Reading {
	@PartitionKey
	@Column(name = "id") private UUID id;
	@Column(name = "probe_id") private UUID probe_id;
	@Column(name = "timestamp") private LocalDateTime timestamp;
	@Column(name = "value") private double rawValue;
	@Column(name = "tvalue") private double transformedValue;
	
	public Reading() {}
}
