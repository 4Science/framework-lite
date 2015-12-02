package it.cilea.core.configuration.dao;

import it.cilea.core.configuration.model.ConfigurationLink;
import it.cilea.core.spring.dao.GenericDao;

import java.util.Collection;

public interface ConfigurationLinkDao extends GenericDao<ConfigurationLink, Integer> {
	void deleteConfigurationLink(Collection<Integer> idCollection);
}
