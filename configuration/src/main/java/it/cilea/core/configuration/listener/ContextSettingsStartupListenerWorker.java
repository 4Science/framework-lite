package it.cilea.core.configuration.listener;

import it.cilea.core.configuration.service.ConfigurationService;
import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.listener.StartupListenerWorker;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ContextSettingsStartupListenerWorker implements StartupListenerWorker, BeanFactoryAware {

	@Autowired
	private ConfigurationService configurationService;

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {
		ConfigurationUtil.reload(servletContext, configurationService);
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

	}

	@Override
	public Integer getPriority() {
		return 0;
	}
}
