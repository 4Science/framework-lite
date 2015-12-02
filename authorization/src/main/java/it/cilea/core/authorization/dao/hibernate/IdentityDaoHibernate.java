package it.cilea.core.authorization.dao.hibernate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import it.cilea.core.authorization.dao.IdentityDao;
import it.cilea.core.authorization.model.impl.Authority;
import it.cilea.core.authorization.model.impl.Identity;
import it.cilea.core.authorization.model.impl.Resource;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

@Repository("identityDao")
public class IdentityDaoHibernate extends GenericDaoHibernate<Identity, Integer> implements IdentityDao {

	/**
	 * Constructor that sets the entity to Identity.class.
	 */
	public IdentityDaoHibernate() {
		super(Identity.class);
	}

	@Override
	public List<Identity> getIdentityList(Set<Resource> resourceSet, Authority authority) {
		Set<Integer> resourceIdSet = new HashSet<Integer>();
		for (Resource resource : resourceSet)
			resourceIdSet.add(resource.getId());
		String queryString = "select authorities.identity ";
		queryString += " from Authorities authorities ";
		queryString += " where authorities.resourceId in (";
		queryString += StringUtils.join(resourceIdSet, ",");
		queryString += " ) ";
		queryString += " and authorities.authorityId = :authorityId";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query.setParameter("authorityId", authority.getId());
		return query.list();
	}
}
