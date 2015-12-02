package it.cilea.core.listener;

import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;

public class ContextSettingsStartupListenerWorker implements StartupListenerWorker, BeanFactoryAware {

	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {

		ResourceBundle resources = ResourceBundle.getBundle("settings");
		for (String key : resources.keySet()) {
			servletContext.setAttribute(key, resources.getObject(key));
		}

	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

	}

	public Integer getPriority() {
		return 1;
	}

}
