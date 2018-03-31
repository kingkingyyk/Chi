package com.kingkingyyk.chi.data;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace="Chi", name = "users", 
		readConsistency="ALL", writeConsistency="ALL", 
		caseSensitiveKeyspace=false, caseSensitiveTable=false)
public class User {
	@PartitionKey
	@Column(name = "id") private UUID id;
	@Column(name = "name") private String name;
	@Column(name = "password") private String password;
	
	public User() {}
}
