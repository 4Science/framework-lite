package it.cilea.core.authorization.dao.hibernate;

import it.cilea.core.authorization.dao.ResourceLinkDao;
import it.cilea.core.authorization.model.impl.ResourceLink;
import it.cilea.core.authorization.model.impl.ResourceLink.ResourceLinkId;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("resourceLinkDao")
public class ResourceLinkDaoHibernate extends GenericDaoHibernate<ResourceLink, ResourceLinkId> implements
		ResourceLinkDao {

	/**
	 * Constructor that sets the entity to ResourceLink.class.
	 */
	public ResourceLinkDaoHibernate() {
		super(ResourceLink.class);
	}

	public List<Object[]> getAllowedResourceLinkIdentifier() {
		String querySql = " select distinct parent.identifier as parent,child.identifier as child ";
		querySql += "from ResourceLink resourceLink ";
		querySql += "join resourceLink.child child ";
		querySql += "join resourceLink.parent parent ";
		querySql += "where coalesce(resourceLink.allowed,true) is true ";
		querySql += "and coalesce(child.allowed,true) is true ";
		querySql += "and coalesce(arent.allowed,true) is true ";
		Query query = getSessionFactory().getCurrentSession().createQuery(querySql);
		return query.list();
	}
}