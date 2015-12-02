package it.cilea.core.configuration.util;

import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cilea.core.configuration.ConfigurationConstant;
import it.cilea.core.configuration.model.Configuration;
import it.cilea.core.configuration.service.ConfigurationService;

public class ConfigurationUtil {
	private static final Logger log = LoggerFactory.getLogger(ConfigurationUtil.class);

	public static void reload(ServletContext servletContext, ConfigurationService configurationService) {

		ConfigurationConstant.configurationMap.clear();
		ResourceBundle resources = ResourceBundle.getBundle("settings");
		for (String key : resources.keySet()) {
			servletContext.setAttribute(key, resources.getObject(key));
			if (resources.getObject(key) == null)
				ConfigurationConstant.configurationMap.put(key, null);
			else
				ConfigurationConstant.configurationMap.put(key, resources.getObject(key).toString());
		}

		List<Configuration> configurationList = configurationService.getConfigurationList("settings", "false", null);

		for (Configuration config : configurationList) {
			servletContext.setAttribute(config.getKey(), config.getValue());
			if (config.getValue() == null)
				ConfigurationConstant.configurationMap.put(config.getKey(), null);
			else
				ConfigurationConstant.configurationMap.put(config.getKey(), config.getValue());
		}

	}

	public static String replaceText(String text) {
		if (ConfigurationConstant.configurationMap == null)
			return text;
		while (StringUtils.contains(text, "${config.")) {
			String key = StringUtils.substringBetween(text, "${config.", "}");
			text = StringUtils.replace(text, "${config." + key + "}",
					ConfigurationConstant.configurationMap.get(key) == null ? "CONFIG_NOT_CONFIGURED:" + key
							: ConfigurationConstant.configurationMap.get(key));
		}
		return text;
	}

	public static String getConfigValue(String key) {
		return ConfigurationConstant.configurationMap.get(key);
	}

}
