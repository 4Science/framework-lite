package it.cilea.core.configuration.dao;

import java.util.List;

import it.cilea.core.configuration.command.ConfigurationSearchCommand;
import it.cilea.core.configuration.model.Configuration;
import it.cilea.core.spring.dao.GenericDao;

public interface ConfigurationDao extends GenericDao<Configuration, Integer> {

	List<Configuration> find(ConfigurationSearchCommand command, Integer pageSize);

	int count(ConfigurationSearchCommand command);

	List<Configuration> getConfigurationList();

	}
