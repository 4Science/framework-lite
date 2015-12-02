package it.cilea.core.configuration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cilea.core.configuration.command.ConfigurationSearchCommand;
import it.cilea.core.configuration.dao.ConfigurationDao;
import it.cilea.core.configuration.dao.ConfigurationLinkDao;
import it.cilea.core.configuration.model.Configuration;
import it.cilea.core.configuration.model.ConfigurationLink;
import it.cilea.core.model.Selectable;
import it.cilea.core.spring.util.MessageUtil;

@Service
public class ConfigurationService {
	@Autowired
	private ConfigurationDao configurationDao;
	@Autowired
	private ConfigurationLinkDao configurationLinkDao;

	public void setConfigurationDao(ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
	}

	public void setConfigurationLinkDao(ConfigurationLinkDao configurationLinkDao) {
		this.configurationLinkDao = configurationLinkDao;
	}

	// Configuration
	public Configuration getConfiguration(Integer configurationId) {
		return configurationDao.get(configurationId);
	}

	public void saveOrUpdate(Configuration configuration) {
		configurationDao.save(configuration);
	}

	public List<Configuration> getConfigurationList(String discriminator, String nullable, String nullableKey) {
		ConfigurationSearchCommand search = new ConfigurationSearchCommand();
		search.setDiscriminator(discriminator);
		return getConfigurationSearchList(search, 0, Boolean.valueOf(nullable), nullableKey);
	}

	public List<Configuration> getConfigurationList(String parentConfigurationId, String discriminator, String nullable,
			String nullableKey) {
		ConfigurationSearchCommand search = new ConfigurationSearchCommand();
		search.setDiscriminator(discriminator);
		search.setParentConfigurationId(Integer.valueOf(parentConfigurationId));
		return getConfigurationSearchList(search, 0, Boolean.valueOf(nullable), nullableKey);
	}

	public List<Selectable> getConfigurationList(String[] ConfigurationIds) {
		List<Selectable> results = new ArrayList<Selectable>();
		for (String id : ConfigurationIds) {
			results.add(configurationDao.get(Integer.valueOf(id)));
		}
		return results;
	}

	public List<Configuration> getConfigurationSearchList(ConfigurationSearchCommand command, Integer pageSize) {
		return configurationDao.find(command, pageSize);
	}

	public List<Configuration> getConfigurationSearchList(ConfigurationSearchCommand command, Integer pageSize,
			Boolean nullable, String nullableKey) {
		List<Configuration> list = getConfigurationSearchList(command, pageSize);
		if (!nullable)
			return list;
		Configuration element = new Configuration();
		element.setDescription(messageUtil.findMessage(nullableKey));
		list.add(0, element);
		return list;
	}

	public List<Configuration> getConfigurationFromKey(String description, String discriminator) {
		ConfigurationSearchCommand search = new ConfigurationSearchCommand();
		search.setDescription(description);
		search.setDiscriminator(discriminator);
		List<Configuration> list = configurationDao.find(search, 1);
		return list;
	}

	public List<Configuration> getConfigurationSearchList(String description, String discriminator, String pageSize,
			String nullable, String nullableKey) {
		ConfigurationSearchCommand search = new ConfigurationSearchCommand();
		search.setDiscriminator(discriminator);
		search.setDescription(description);
		return getConfigurationSearchList(search, Integer.valueOf(0), Boolean.valueOf(nullable), nullableKey);
	}

	public int getConfigurationSearchCount(ConfigurationSearchCommand command) {
		return configurationDao.count(command);
	}

	// ConfigurationLink

	public ConfigurationLink getConfigurationLink(Integer configurationLinkId) {
		return configurationLinkDao.get(configurationLinkId);
	}

	public void saveOrUpdate(ConfigurationLink configurationLink) {
		configurationLinkDao.save(configurationLink);
	}

	public void deleteConfigurationLink(Set<Integer> idSet) {
		configurationLinkDao.deleteConfigurationLink(idSet);
	}

	public List<Configuration> getConfigurationList() {
		return configurationDao.getConfigurationList();
	}

	@Autowired
	protected MessageUtil messageUtil;

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}
}
