package it.cilea.core.configuration.dao.hibernate;

import it.cilea.core.configuration.dao.ConfigurationLinkDao;
import it.cilea.core.configuration.model.ConfigurationLink;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("configurationLinkDao")
public class ConfigurationLinkDaoHibernate extends GenericDaoHibernate<ConfigurationLink, Integer> implements
		ConfigurationLinkDao {

	public ConfigurationLinkDaoHibernate() {
		super(ConfigurationLink.class);
	}

	@Override
	public void deleteConfigurationLink(Collection<Integer> idCollection) {
		String querySql = "delete from ConfigurationLink where id  in ("
				+ StringUtils.join(idCollection.iterator(), ",") + ") ";
		Query query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();
	}
}
