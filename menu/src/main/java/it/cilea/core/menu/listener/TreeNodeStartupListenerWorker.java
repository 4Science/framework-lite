package it.cilea.core.menu.listener;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import it.cilea.core.listener.StartupListenerWorker;
import it.cilea.core.menu.service.TreeNodeService;
import it.cilea.core.menu.utils.MenuUtil;

public class TreeNodeStartupListenerWorker implements StartupListenerWorker {

	@Override
	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {
		TreeNodeService treeNodeService = (TreeNodeService) applicationContext.getBean("treeNodeService");
		MenuUtil.reload(servletContext, treeNodeService);
	}

	public Integer getPriority() {
		return 1;
	}
}