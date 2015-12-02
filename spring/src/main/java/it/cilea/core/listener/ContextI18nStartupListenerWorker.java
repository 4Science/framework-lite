package it.cilea.core.listener;

import it.cilea.core.model.SelectBase;
import it.cilea.core.model.Selectable;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;

/**
 * 
 * this listener must be initialized when you plan to use widget tags with
 * showI18n=true
 * 
 * @author suardi
 */
public class ContextI18nStartupListenerWorker implements StartupListenerWorker, BeanFactoryAware {

	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {

		String i18nList = null;
		String i18nBase = null;
		ResourceBundle resources = ResourceBundle.getBundle("settings");
		if (resources.containsKey("I18N_LIST"))
			i18nList = resources.getString("I18N_LIST");
		if (resources.containsKey("I18N_BASE"))
			i18nBase = resources.getString("I18N_BASE");
		resources = ResourceBundle.getBundle("settings");
		if (resources.containsKey("I18N_LIST") && StringUtils.isNotBlank(resources.getString("I18N_LIST")))
			i18nList = resources.getString("I18N_LIST");
		if (resources.containsKey("I18N_BASE") && StringUtils.isNotBlank(resources.getString("I18N_BASE")))
			i18nBase = resources.getString("I18N_BASE");
		if (StringUtils.isNotBlank(i18nList)) {
			List<Selectable> list = new ArrayList<Selectable>();
			for (String code : StringUtils.split(i18nList, ",")) {
				SelectBase select = new SelectBase();
				select.setValue(code);
				select.setLabelKey("label.i18n." + code);
				list.add(select);
			}
			servletContext.setAttribute("i18nList", list);
		}
		if (StringUtils.isNotBlank(i18nBase)) {
			SelectBase selectableI18nBase = new SelectBase();
			selectableI18nBase.setValue(i18nBase);
			selectableI18nBase.setLabelKey("label.i18n." + i18nBase);
			servletContext.setAttribute("i18nBase", selectableI18nBase);
		}

	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

	}

	public Integer getPriority() {
		return 1;
	}

}
