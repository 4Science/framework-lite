package it.cilea.core.view.listener;

import it.cilea.core.listener.StartupListenerWorker;
import it.cilea.core.view.service.ViewService;
import it.cilea.core.view.util.ViewUtil;
import it.cilea.core.widget.WidgetConstant;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;

public class ViewStartupListenerWorker implements StartupListenerWorker, BeanFactoryAware {

	@Override
	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {
		ViewService viewService = (ViewService) applicationContext.getBean("viewService");
		ViewUtil.reload(viewService);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		WidgetConstant.beanFactory = beanFactory;
	}

	public Integer getPriority() {
		return 1;
	}
}