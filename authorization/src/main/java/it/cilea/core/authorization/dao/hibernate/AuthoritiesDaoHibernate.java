package it.cilea.core.authorization.dao.hibernate;

import it.cilea.core.authorization.dao.AuthoritiesDao;
import it.cilea.core.authorization.model.impl.Authorities;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("authoritiesDao")
public class AuthoritiesDaoHibernate extends GenericDaoHibernate<Authorities, Integer> implements AuthoritiesDao {

	/**
	 * Constructor that sets the entity to Authority.class.
	 */
	public AuthoritiesDaoHibernate() {
		super(Authorities.class);
	}

	public void deleteAuthorities(Authorities authorities) {
		if (log.isDebugEnabled()) {
			log.debug("authorities's id: " + authorities.getId());
		}

		String querySql = "delete from AuthoritiesInfo where authoritiesId  = " + authorities.getId();
		Query query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();

		querySql = "delete from Authorities where id  = " + authorities.getId();
		query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();

	}

	public void deleteAuthorities(Collection<Integer> idCollection) {
		String querySql = "delete from Authorities where id  in (" + StringUtils.join(idCollection.iterator(), ",")
				+ ") ";
		Query query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();
	}

	@Override
	public int count(Integer identityId, Integer authorityId, Integer resourceId, Integer infoId) {
		String queryString = "select count(authorities.id) from Authorities authorities";
		if (infoId != null)
			queryString += " join authorities.infoSet infoSet";
		queryString += " where";
		if (identityId != null)
			queryString += " authorities.identityId=:identityId and";
		if (authorityId != null)
			queryString += " authorities.authorityId=:authorityId and";
		if (resourceId != null)
			queryString += " authorities.resourceId=:resourceId and";
		if (infoId != null)
			queryString += " infoSet.info=:infoId and";
		queryString = StringUtils.removeEnd(queryString, " and");
		queryString = StringUtils.removeEnd(queryString, " where ");
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);

		if (identityId != null)
			query.setParameter("identityId", identityId);
		if (authorityId != null)
			query.setParameter("authorityId", authorityId);
		if (resourceId != null)
			query.setParameter("resourceId", resourceId);
		if (infoId != null)
			query.setParameter("infoId", infoId);
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	@Override
	public Authorities get(Integer identityId, Integer authorityId, Integer resourceId, Integer infoId) {
		String queryString = "select authorities from Authorities authorities";
		if (infoId != null)
			queryString += " join authorities.infoSet infoSet";
		queryString += " where";
		if (identityId != null)
			queryString += " authorities.identityId=:identityId and";
		if (authorityId != null)
			queryString += " authorities.authorityId=:authorityId and";
		if (resourceId != null)
			queryString += " authorities.resourceId=:resourceId and";
		if (infoId != null)
			queryString += " infoSet.info=:infoId and";
		queryString = StringUtils.removeEnd(queryString, " and");
		queryString = StringUtils.removeEnd(queryString, " where ");
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);

		if (identityId != null)
			query.setParameter("identityId", identityId);
		if (authorityId != null)
			query.setParameter("authorityId", authorityId);
		if (resourceId != null)
			query.setParameter("resourceId", resourceId);
		if (infoId != null)
			query.setParameter("infoId", infoId);
		return (Authorities) query.uniqueResult();
	}

}
