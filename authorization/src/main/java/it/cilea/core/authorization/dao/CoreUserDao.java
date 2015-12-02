package it.cilea.core.authorization.dao;

public interface CoreUserDao {

	Integer getUserIdByUsername(String username);

	// unsafe
	// String getPasswordByUsername(String username);

	String getPasswordHashByUsername(String username);
}
