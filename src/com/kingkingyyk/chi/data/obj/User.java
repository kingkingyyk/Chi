package com.kingkingyyk.chi.data.obj;

import java.util.UUID;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;


@Table(keyspace="Chi", name = "users", 
		readConsistency="ANY", writeConsistency="ANY", 
		caseSensitiveKeyspace=false, caseSensitiveTable=false)

public class User {
	public static final String CREATION_SQL = "CREATE TABLE IF NOT EXISTS Chi.users(id UUID PRIMARY KEY, name text, password text);";
	@PartitionKey
	@Column(name = "id") private UUID id;
	@Column(name = "name") private String name;
	@Column(name = "password") private String password;
	
	public User(UUID id, String name, String password) {
		this.id = id;
		this.name = name;
		this.password = password;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
