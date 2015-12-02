package it.cilea.core.listener;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

public interface StartupListenerWorker {
	void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception;
	Integer getPriority();
}
