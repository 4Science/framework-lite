package it.cilea.core.spring.dao.hibernate;

import it.cilea.core.spring.dao.CoreDao;

import java.sql.Timestamp;
import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("coreDao")
public class CoreDaoHibernate implements CoreDao {
	
    @Qualifier("sessionFactorySur")
    @Autowired
	private SessionFactory sessionFactory;

	public Timestamp getCurrentTimestamp() {
		Dialect dialect = ((SessionFactoryImplementor) sessionFactory).getDialect();

		if (dialect.toString().contains("Postgre")) {
			String checkQuerySQL = "select current_timestamp";
			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(checkQuerySQL);
			return (Timestamp) sqlQuery.uniqueResult();
		}

		if (dialect.isCurrentTimestampSelectStringCallable()) {
			SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(
					dialect.getCurrentTimestampSelectString());
			return (Timestamp) sqlQuery.uniqueResult();
		}
		SQLQuery sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(dialect.getSelectGUIDString());
		Object o = sqlQuery.uniqueResult();
		return new Timestamp((new Date()).getTime());
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
