package it.cilea.core.spring.listener;

import it.cilea.core.listener.StartupListenerWorker;
import it.cilea.core.spring.util.MessageUtil;
import it.cilea.core.util.MessageUtilConstant;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class MessageUtilConstantStartupListenerWorker implements StartupListenerWorker {
	@Autowired
	private MessageUtil messageUtil;

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	public void initialize(ServletContext servletContext, ApplicationContext context) throws Exception {
		MessageUtilConstant.loadMessageUtil(messageUtil);
	}
	
	public Integer getPriority() {
		return 1;
		}
}
