package it.cilea.core.widget.listener;

import it.cilea.core.listener.StartupListenerWorker;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.service.WidgetService;
import it.cilea.core.widget.util.WidgetUtil;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;

public class WidgetStartupListenerWorker implements StartupListenerWorker, BeanFactoryAware {

	@Override
	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {
		WidgetService widgetService = (WidgetService) applicationContext.getBean("widgetService");
		WidgetUtil.reload(widgetService);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		WidgetConstant.beanFactory = beanFactory;
	}

	public Integer getPriority() {
		return 1;
	}
}