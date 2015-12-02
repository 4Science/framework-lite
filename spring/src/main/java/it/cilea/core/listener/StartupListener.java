package it.cilea.core.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class StartupListener extends ContextLoaderListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {

		ServletContext servletContext = event.getServletContext();
		ApplicationContext applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(servletContext);

		String[] beanNames = applicationContext.getBeanNamesForType(StartupListenerWorker.class);

		List<StartupListenerWorker> list = new ArrayList<StartupListenerWorker>();
		for (String beanName : beanNames)
			list.add((StartupListenerWorker) applicationContext.getBean(beanName));

		Collections.sort(list, new StartupListenerWorkerComparator());

		for (StartupListenerWorker worker : list)
			try {
				worker.initialize(servletContext, applicationContext);
			} catch (Exception e) {
				e.printStackTrace();
			}

	}
}
