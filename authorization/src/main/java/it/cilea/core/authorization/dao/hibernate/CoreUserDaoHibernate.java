package it.cilea.core.authorization.dao.hibernate;

import org.hibernate.SessionFactory;

import it.cilea.core.authorization.dao.CoreUserDao;

public class CoreUserDaoHibernate implements CoreUserDao {
	private SessionFactory sessionFactory;

	@Override
	public String getPasswordHashByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer getUserIdByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
