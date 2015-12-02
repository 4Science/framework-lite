package it.cilea.core.authorization.dao.hibernate;

import it.cilea.core.authorization.dao.AuthorityDao;
import it.cilea.core.authorization.model.impl.Authority;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import org.springframework.stereotype.Repository;


@Repository("authorityDao")
public class AuthorityDaoHibernate extends GenericDaoHibernate<Authority, Integer> implements AuthorityDao {

	/**
	 * Constructor that sets the entity to Authority.class.
	 */
	public AuthorityDaoHibernate() {
		super(Authority.class);
	}

}
