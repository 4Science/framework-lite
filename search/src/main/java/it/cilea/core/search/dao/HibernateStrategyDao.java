package it.cilea.core.search.dao;

import it.cilea.core.search.strategy.impl.hibernate.HibernateSearchStrategyData;

import java.sql.Connection;
import java.util.List;

public interface HibernateStrategyDao {

	Long getResultCount(HibernateSearchStrategyData data);

	List<?> getResultList(HibernateSearchStrategyData data);

	String getSqlList(HibernateSearchStrategyData data);

	Connection getConnection();

}
