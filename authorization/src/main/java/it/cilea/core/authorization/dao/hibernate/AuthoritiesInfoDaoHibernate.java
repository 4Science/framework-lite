package it.cilea.core.authorization.dao.hibernate;

import it.cilea.core.authorization.dao.AuthoritiesInfoDao;
import it.cilea.core.authorization.model.impl.AuthoritiesInfo;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("authoritiesInfoDao")
public class AuthoritiesInfoDaoHibernate extends GenericDaoHibernate<AuthoritiesInfo, Integer> implements
		AuthoritiesInfoDao {

	/**
	 * Constructor that sets the entity to Authority.class.
	 */
	public AuthoritiesInfoDaoHibernate() {
		super(AuthoritiesInfo.class);
	}

	public void deleteAuthoritiesInfo(AuthoritiesInfo authoritiesInfo) {
		if (log.isDebugEnabled()) {
			log.debug("authoritiesInfo's id: " + authoritiesInfo.getId());
		}

		String querySql = "delete from AuthoritiesInfo where authoritiesId  = " + authoritiesInfo.getId();
		Query query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();

		querySql = "delete from AuthoritiesInfo where id  = " + authoritiesInfo.getId();
		query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();

	}

	public void deleteAuthoritiesInfo(Collection<Integer> idCollection) {
		String querySql = "delete from AuthoritiesInfo where id  in (" + StringUtils.join(idCollection.iterator(), ",")
				+ ") ";
		Query query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();
	}

}
