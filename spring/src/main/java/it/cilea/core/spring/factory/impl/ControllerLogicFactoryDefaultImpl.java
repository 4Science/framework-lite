package it.cilea.core.spring.factory.impl;

import it.cilea.core.spring.factory.ControllerLogicFactory;
import it.cilea.core.spring.model.ControllerLogic;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ControllerLogicFactoryDefaultImpl implements ControllerLogicFactory {

	protected static Logger log = LoggerFactory.getLogger(ControllerLogicFactoryDefaultImpl.class);

	@Autowired
	protected ApplicationContext context;

	@Override
	public ControllerLogic getControllerLogic(String url) {
		ControllerLogic controllerLogic;
		if (context.containsBean("controller:" + url)) {
			controllerLogic = context.getBean("controller:" + url, ControllerLogic.class);
			if (StringUtils.isNotBlank(controllerLogic.getValidatorListBeanName())) {
				controllerLogic
						.setValidatorList(context.getBean(controllerLogic.getValidatorListBeanName(), List.class));
			}
			return context.getBean("controller:" + url, ControllerLogic.class);
		} else {
			log.warn("No ControllerLogic named \"controller:" + url
					+ "\" can be found in this context: creating an emtpy one with default viewName");
			controllerLogic = new ControllerLogic();
			controllerLogic.setViewName(StringUtils.substringBeforeLast(url, "."));
			return controllerLogic;
		}
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

}
