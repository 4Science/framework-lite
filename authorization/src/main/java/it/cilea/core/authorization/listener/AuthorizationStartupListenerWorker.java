package it.cilea.core.authorization.listener;

import it.cilea.core.authorization.service.AuthorizationService;
import it.cilea.core.authorization.uitl.AuthorizationUtil;
import it.cilea.core.listener.StartupListenerWorker;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

public class AuthorizationStartupListenerWorker implements StartupListenerWorker {

	@Override
	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {
		AuthorizationService authorizationService = (AuthorizationService) applicationContext
				.getBean("authorizationService");
		AuthorizationUtil.reload(servletContext, authorizationService);
	}

	@Override
	public Integer getPriority() {
		return 1;
	}

}
