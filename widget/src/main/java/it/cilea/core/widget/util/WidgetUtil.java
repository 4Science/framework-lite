package it.cilea.core.widget.util;

import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.model.WidgetDictionary;
import it.cilea.core.widget.service.WidgetService;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WidgetUtil {

	private static final Logger log = LoggerFactory.getLogger(WidgetUtil.class);

	public static boolean isString(Class clazz) {
		if (clazz == null)
			return false;
		return "java.lang.String".equals(clazz.getName());
	}

	public static boolean isDate(Class clazz) {
		if (clazz == null)
			return false;
		return "java.util.Date".equals(clazz.getName());
	}

	public static boolean areObjectEquals(Object str1, Object str2) {
		if ((str1 == null && str2 != null) || (str1 != null && str2 == null)
				|| (str1 != null && str2 != null && (!str1.equals(str2))))
			return false;

		else {

			if ((str1 == null && str2 == null) || str1.equals(str2))
				return true;
			else
				return false;
		}
	}

	public static String getRequestAttributeName(String pageAttributeName) {
		return pageAttributeName;
	}

	public static String getLowerBoundRequestAttributeName(String pageAttributeName) {
		return WidgetConstant.FIRST_FIELD_PREFIX + pageAttributeName;
	}

	public static String getUpperBoundRequestAttributeName(String pageAttributeName) {
		return WidgetConstant.SECOND_FIELD_PREFIX + pageAttributeName;
	}

	public static String getFuzzinessCheckboxRequestAttributeName(String pageAttributeName) {
		return pageAttributeName + WidgetConstant.FUZZINESS_CHECKBOX_SUFFIX_ATTRIBUTE_NAME;
	}

	private static String replaceAllOccourence(String source, String regex, String replace) {
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(source).replaceAll(replace);
	}

	public static String replaceFullTextSearchCharacters(String value) {

		// Pattern.compile("\"",
		// Pattern.CASE_INSENSITIVE).matcher(value).replaceAll("");

		value = replaceAllOccourence(value, "'", "''");
		value = replaceAllOccourence(value, "‘", "''");
		value = replaceAllOccourence(value, "’", "''");
		value = replaceAllOccourence(value, "“", "");
		value = replaceAllOccourence(value, "”", "");
		value = replaceAllOccourence(value, "\"", "");
		value = replaceAllOccourence(value, "\\+", " ");
		value = replaceAllOccourence(value, "\\-", "");
		value = replaceAllOccourence(value, "\\$", "");
		value = replaceAllOccourence(value, "\\{", "");
		value = replaceAllOccourence(value, "\\}", "");
		value = replaceAllOccourence(value, "\\[", "");
		value = replaceAllOccourence(value, "\\]", "");
		value = replaceAllOccourence(value, "\\^", "");
		value = replaceAllOccourence(value, "\\?", "");

		value = replaceAllOccourence(value, " ACCUM ", "");
		value = replaceAllOccourence(value, " NEAR ", "");
		value = replaceAllOccourence(value, " AND ", "");
		value = replaceAllOccourence(value, " OR ", "");

		value = replaceAllOccourence(value, "FUZZY", "");
		value = value.trim();

		return value;
	}

	public static void reload(WidgetService widgetService) throws Exception {
		log.info("Widget reload");

		WidgetConstant.widgetMap = new HashMap<Integer, Widget>();
		WidgetConstant.widgetDictionaryMap = new HashMap<Integer, WidgetDictionary>();
		List<Widget> widgetList = widgetService.initWidgetList();
		for (Widget widget : widgetList) {
			widget.init();
			WidgetConstant.widgetMap.put(widget.getId(), widget);
			for (WidgetDictionary dic : widget.getWidgetDictionarySet())
				WidgetConstant.widgetDictionaryMap.put(dic.getId(), dic);
		}
	}

}
