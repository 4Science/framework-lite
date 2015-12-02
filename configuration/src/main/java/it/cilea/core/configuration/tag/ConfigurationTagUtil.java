package it.cilea.core.configuration.tag;

import it.cilea.core.configuration.ConfigurationConstant;
import it.cilea.core.configuration.util.ConfigurationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationTagUtil {
	private static final Logger log = LoggerFactory.getLogger(ConfigurationTagUtil.class);

	/**
	 * Used only in jsp with custom taglib
	 * 
	 * @param key
	 * @return
	 */
	public static String getValue(String key) {
		String property = ConfigurationConstant.configurationMap.get(key);

		if (property == null) {
			log.warn("Property: " + key + " not found");
			return "";

		} else {
			return property;
		}

	}

	public static String replace(String string) {
		return ConfigurationUtil.replaceText(string);
	}

}
