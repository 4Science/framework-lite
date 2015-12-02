package it.cilea.core.spring.dao.hibernate;

import java.util.Collections;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory;
import org.hibernate.hql.spi.QueryTranslator;
import org.hibernate.hql.spi.QueryTranslatorFactory;

public class HqlToSqlTranslator {
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public String toSql(String hqlQueryText) {
		if (hqlQueryText != null && hqlQueryText.trim().length() > 0) {
			final QueryTranslatorFactory translatorFactory = new ASTQueryTranslatorFactory();
			final SessionFactoryImplementor factory = (SessionFactoryImplementor) sessionFactory;
			final QueryTranslator translator = translatorFactory.createQueryTranslator(hqlQueryText, hqlQueryText,
					Collections.EMPTY_MAP, factory);
			translator.compile(Collections.EMPTY_MAP, false);
			return translator.getSQLString();
		}
		return null;
	}

	// oppure
	// ISessionFactory sessionFactory = ...
	// var sf = (SessionFactoryImpl) sessionFactory;
	// var hql = "from Person";
	// var qt = sf.Settings.QueryTranslatorFactory.CreateQueryTranslator("",
	// hql, new Dictionary<string, IFilter>(), (ISessionFactoryImplementor)
	// sessionFactory);
	// qt.Compile(new Dictionary<string, string>(), true);
	// var sql = qt.SQLString;
	// Console.WriteLine(sql);
}