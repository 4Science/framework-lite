package it.cilea.core.authorization.dao.hibernate;

import it.cilea.core.authorization.dao.ResourceDao;
import it.cilea.core.authorization.model.impl.Resource;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

@Repository("resourceDao")
public class ResourceDaoHibernate extends GenericDaoHibernate<Resource, Integer> implements ResourceDao {

	/**
	 * Constructor that sets the entity to Resource.class.
	 */
	public ResourceDaoHibernate() {
		super(Resource.class);
	}


}
