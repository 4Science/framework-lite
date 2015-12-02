package it.cilea.core.search.dao;

import it.cilea.core.search.strategy.impl.sql.SqlSearchStrategyData;

import java.sql.Connection;
import java.util.List;

public interface SqlStrategyDao {

	Long getResultCount(SqlSearchStrategyData data);

	List<?> getResultList(SqlSearchStrategyData data);

	String getSqlList(SqlSearchStrategyData data);

	Connection getConnection();

}
