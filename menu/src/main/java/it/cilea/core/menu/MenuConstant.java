package it.cilea.core.menu;

import it.cilea.core.CoreConstant;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class MenuConstant {

	static {
		try {
			Properties props = new Properties();
			ClassLoader cl = CoreConstant.class.getClassLoader();
			InputStream is = cl.getResourceAsStream("settings.properties");
			props.load(is);

			String menuReplaceRules = props.getProperty("MENU_REPLACE_RULES");
			Map<String, String> menuReplaceRulesMap = new HashMap<String, String>();
			for (String string : StringUtils.split(menuReplaceRules, ","))
				menuReplaceRulesMap.put(StringUtils.substringBefore(string, "="),
						StringUtils.substringAfter(string, "="));
			MENU_REPLACE_RULES = menuReplaceRulesMap;

		} catch (IOException e) {
			throw new ExceptionInInitializerError();
		}
	}

	public static final Map<String, String> MENU_REPLACE_RULES;

	public static final String DEFAULT_JS_RULES_CLASS = "it.cilea.core.menu.rhino.iterfaces.implementation.AddJsInfo";

	public static enum TreeNodeDictionaryType {
		LABEL, DESCRIPTION, NOTE;
	}

}
