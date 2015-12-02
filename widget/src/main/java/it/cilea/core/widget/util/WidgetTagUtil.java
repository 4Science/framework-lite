package it.cilea.core.widget.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cilea.core.annotation.EuroOnlyJsp;
import it.cilea.core.annotation.MoneyOnlyJsp;
import it.cilea.core.annotation.MoneyPatternDefaultOnlyJsp;
import it.cilea.core.annotation.NotNullOnlyJsp;
import it.cilea.core.annotation.PercentOnlyJsp;
import it.cilea.core.annotation.TimeOnlyJsp;
import it.cilea.core.annotation.TypeOfCollection;
import it.cilea.core.model.Selectable;
import it.cilea.core.util.MessageUtilConstant;
import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.model.impl.core.HiddenWidget;
import it.cilea.core.widget.model.impl.core.ServerSideWidget;

/**
 * A Utility class. It is a singleton.
 * 
 * @author Ted Bergeron
 * @version $Id: Utilities.java,v 1.5 2008-02-06 17:29:17 suardicvs Exp $
 */

public class WidgetTagUtil {
	private static final Logger log = LoggerFactory.getLogger(WidgetTagUtil.class);

	private static final String PROPERTY_SEPERATOR = ".";

	public static Method findGetterMethod(Object object, String propertyPath) {
		if (object == null) {
			log.debug("Object was null.");
			return null;
		}

		if (propertyPath == null) {
			log.debug("propertyPath was null.");
			return null;
		}

		// RequestContext requestContext = (RequestContext)
		// pageContext.getAttribute(RequestContextAwareTag.
		// REQUEST_CONTEXT_PAGE_ATTRIBUTE);
		// Object target = requestContext.getModelObject(beanName);
		// (BindStatus:132) Converts string path to object instance.

		log.debug("Testing methods.");
		log.debug("Path = " + propertyPath);
		log.debug("Derived object = " + getObjectFromPropertyPath(propertyPath));
		log.debug("Derived property = " + getPropertyFromPropertyPath(propertyPath));
		log.debug("Passed object classname = " + object.getClass().getName());

		String property = getPropertyFromPropertyPath(propertyPath);

		String getterMethodName = convertPropertyNameToGetMethodName(property); // property
		// is
		// "email"
		// convert
		// to
		// getEmail

		try {
			return object.getClass().getMethod(getterMethodName);
		} catch (NoSuchMethodException e) {
			// log.error(e);
			return null; // Method name passed in was invalid.
		}

		// Class classe = object.getClass().getSuperclass();
		//
		// while (!classe.equals(java.lang.Object.class)
		// || !classe.equals(it.cilea.core.model.BaseObject.class)) {
		// try {
		// return classe.getMethod(getterMethodName);
		// } catch (NoSuchMethodException e) {
		// if (classe.getSuperclass().equals(java.lang.Object.class)
		// || classe.getSuperclass().equals(
		// it.cilea.core.model.BaseObject.class))
		// log.error(e);
		// }
		// classe = object.getClass().getSuperclass();
		// }
		// return null;

	}

	public static Integer maxLength(Object object, String propertyPath) {

		Method getMethod = findGetterMethod(object, propertyPath);
		if (getMethod != null) {
			if (getMethod.isAnnotationPresent(Size.class)) {
				Size length = getMethod.getAnnotation(Size.class);
				int max = length.max();
				log.debug("Max length = " + max);
				if (max > 0) {
					return max;
				} else {
					return null; // Length was defined, but max was not
					// provided,
					// or was set to 0.
				}
			}
		}

		Field field = findField(object.getClass(), getPropertyFromPropertyPath(propertyPath));
		if (field != null) {
			if (field.isAnnotationPresent(Size.class)) {
				Size length = field.getAnnotation(Size.class);
				int max = length.max();
				log.debug("Max length = " + max);
				if (max > 0) {
					return max;
				} else {
					return null; // Length was defined, but max was not
					// provided,
					// or was set to 0.
				}
			}
		}

		if (propertyPath.contains("clobMap["))
			return 4000;

		return null;

	}

	public static Boolean required(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		Boolean required = false;
		if (getMethod != null) {
			required = getMethod.isAnnotationPresent(NotNull.class);
			if (!required)
				required = getMethod.isAnnotationPresent(NotNullOnlyJsp.class);
		}
		Field field = findField(object.getClass(), getPropertyFromPropertyPath(propertyPath));
		if (field != null) {
			if (!required)
				required = field.isAnnotationPresent(NotNull.class);
			if (!required)
				required = field.isAnnotationPresent(NotNullOnlyJsp.class);
		}
		return required;
	}

	public static Boolean isTime(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		Boolean required = false;
		if (getMethod != null) {
			required = getMethod.isAnnotationPresent(TimeOnlyJsp.class);
		}
		Field field = findField(object.getClass(), getPropertyFromPropertyPath(propertyPath));
		if (field != null) {
			if (!required)
				required = field.isAnnotationPresent(TimeOnlyJsp.class);
		}
		return required;
	}

	public static Boolean isEuro(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		Boolean required = false;
		if (getMethod != null) {
			required = getMethod.isAnnotationPresent(EuroOnlyJsp.class);
		}
		Field field = findField(object.getClass(), getPropertyFromPropertyPath(propertyPath));
		if (field != null) {
			if (!required)
				required = field.isAnnotationPresent(EuroOnlyJsp.class);
		}
		return required;
	}

	public static Boolean isPercent(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		Boolean required = false;
		if (getMethod != null) {
			required = getMethod.isAnnotationPresent(PercentOnlyJsp.class);
		}
		Field field = findField(object.getClass(), getPropertyFromPropertyPath(propertyPath));
		if (field != null) {
			if (!required)
				required = field.isAnnotationPresent(PercentOnlyJsp.class);
		}
		return required;
	}

	public static Boolean isMoney(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		Boolean required = false;
		if (getMethod != null) {
			required = getMethod.isAnnotationPresent(MoneyOnlyJsp.class);
		}
		Field field = findField(object.getClass(), getPropertyFromPropertyPathPrototype(propertyPath));
		if (field != null) {
			if (!required)
				required = field.isAnnotationPresent(MoneyOnlyJsp.class);
		}
		return required;
	}

	public static Boolean isMoneyPatternDefault(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		Boolean required = false;
		if (getMethod != null) {
			required = getMethod.isAnnotationPresent(MoneyPatternDefaultOnlyJsp.class);
		}
		Field field = findField(object.getClass(), getPropertyFromPropertyPath(propertyPath));
		if (field != null) {
			if (!required)
				required = field.isAnnotationPresent(MoneyPatternDefaultOnlyJsp.class);
		}
		return required;
	}

	public static Boolean isDate(Object object, String propertyPath) {
		return java.util.Date.class.equals(getReturnTypePrototype(object, propertyPath))
				|| Calendar.class.equals(getReturnTypePrototype(object, propertyPath));
	}

	public static String formatMoney(Object value) {
		if (value != null) {
			try {
				Number valore = (Number) value;
				Locale locale = Locale.ITALIAN;
				return NumberFormat.getNumberInstance(locale).format(valore);
			} catch (Exception e) {
				// e.printStackTrace();
				return value.toString();
			}
		}
		return "";
	}

	private static final Class[] genericCollectionClass = { Map.class, List.class, Set.class };

	public static Class getReturnTypePrototype(Object object, String propertyPath) {
		propertyPath = StringUtils.substringBefore(propertyPath, "[");
		Field field = findFieldPrototype(object, getNameFromPropertyPathPrototype(propertyPath));
		if (field == null)
			return null;
		if (ArrayUtils.contains(genericCollectionClass, field.getType())
				&& field.isAnnotationPresent(TypeOfCollection.class))
			return field.getAnnotation(TypeOfCollection.class).type();
		return field.getType();

	}

	public static Field findFieldPrototype(Object object, String property) {
		if (object == null || property == null) {
			log.debug("object or property null " + object + " " + property);
			return null;
		}
		Class clazz = object.getClass();
		while (!Object.class.equals(clazz)) {
			try {
				return clazz.getDeclaredField(property);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}

	public static String getNameFromPropertyPathPrototype(String propertyPath) {
		log.debug("propertyPath = " + propertyPath);
		return StringUtils.substringAfterLast(propertyPath, ".");
	}

	public static Class getReturnType(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		if (getMethod == null) {
			return null;
		} else {
			return getMethod.getReturnType();
		}
	}

	public static Object getReferencedObject(Object object, String propertyPath) {
		Method getMethod = findGetterMethod(object, propertyPath);
		if (getMethod == null) {
			return null;
		}
		try {
			return getMethod.invoke(object);
		} catch (Exception e) {
			log.debug("Unable to get object via reflection.");
			return null;
		}
	}

	public static String identifyingValue(Object object, String propertyPath) {
		Object referencedObject = getReferencedObject(object, propertyPath);
		if (referencedObject != null && referencedObject instanceof Selectable) {
			Selectable selectable = (Selectable) referencedObject;
			return selectable.getIdentifyingValue();
		} else {
			return null;
		}
	}

	public static String convertPropertyNameToGetMethodName(String property) {
		if (property == null) {
			throw new IllegalArgumentException("Property name was null.");
		}
		StringBuilder builder = new StringBuilder(property.length() + 3);
		String firstLetter = property.substring(0, 1);
		String wordRemainder = property.substring(1);
		firstLetter = firstLetter.toUpperCase();
		builder.append("get").append(firstLetter).append(wordRemainder);
		return builder.toString();
	}

	public static String getObjectFromPropertyPath(String propertyPath) {
		// Path is "customer.contact.fax.number", need to return
		// "customer.contact.fax"
		log.debug("propertyPath = " + propertyPath);
		int lastSeperatorPosition = propertyPath.lastIndexOf(PROPERTY_SEPERATOR);
		String objectString = propertyPath.substring(0, lastSeperatorPosition);
		log.debug("objectString = " + objectString);
		return objectString;
	}

	public static String getPropertyFromPropertyPath(String propertyPath) {
		// Path is "customer.contact.fax.number", need to return "number"
		log.debug("propertyPath = " + propertyPath);
		int lastSeperatorPosition = propertyPath.lastIndexOf(PROPERTY_SEPERATOR);
		String propertyString = propertyPath.substring(lastSeperatorPosition + 1);
		log.debug("propertyString = " + propertyString);

		// se termina con Id lo rimuovo
		if (propertyString.length() > 2) {
			if (StringUtils.endsWith(propertyString, "Id")) {
				propertyString = StringUtils.removeEnd(propertyString, "Id");
			}
		}
		return propertyString;
	}

	public static String getPropertyFromPropertyPathPrototype(String propertyPath) {
		// Path is "customer.contact.fax.number", need to return "number"
		log.debug("propertyPath = " + propertyPath);
		int lastSeperatorPosition = propertyPath.lastIndexOf(PROPERTY_SEPERATOR);
		String propertyString = propertyPath.substring(lastSeperatorPosition + 1);
		log.debug("propertyString = " + propertyString);

		// se termina con Id lo rimuovo
		if (propertyString.length() > 2) {
			if (propertyString.charAt(propertyString.length() - 1) == 'd'
					&& propertyString.charAt(propertyString.length() - 2) == 'I') {
				propertyString = propertyString.substring(0, propertyString.length() - 2);
			}
		}
		return StringUtils.substringBefore(propertyString, "[");
	}

	public static String convertDatabaseNameToJavaName(String val) {
		if (val == null) {
			return null;
		}

		String value = val.toLowerCase();
		StringBuffer buf = null;

		StringTokenizer st = new StringTokenizer(value, "_");
		if (st.hasMoreTokens()) {
			buf = new StringBuffer(st.nextToken()); // Take first piece as is
			while (st.hasMoreTokens()) {
				String fragment = st.nextToken();
				buf.append(initCap(fragment));
			}
		}

		if (buf == null) {
			return null;
		} else {
			return buf.toString();
		}
	}

	public static String convertGet(String value) {
		StringBuilder buf = new StringBuilder("get");
		buf.append(initCap(convertDatabaseNameToJavaName(value)));
		return buf.toString();
	}

	public static String convertSet(String value) {
		StringBuilder buf = new StringBuilder("set");
		buf.append(initCap(convertDatabaseNameToJavaName(value)));
		return buf.toString();
	}

	public static String initCap(String value) {
		if (value == null) {
			return null;
		}

		String initCap = null;
		String suffix = null;

		try { // Need to convert first letter to caps
			initCap = value.substring(0, 1).toUpperCase();
			suffix = value.substring(1, value.length());
		} catch (IndexOutOfBoundsException e) {
			log.error(e.getMessage());
		}
		return initCap + suffix;
	}

	public static String formatText(String text) {
		String testo = StringUtils.trimToNull(text);
		if (testo != null)
			return StringUtils.replace(testo, "\n", "<br>");
		else
			return null;
	}

	public static Field findField(Class clazz, String property) {
		if (clazz == null || property == null) {
			return null;
		}

		while (!Object.class.equals(clazz)) {
			try {
				return clazz.getDeclaredField(property);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}

		return null;
	}

	public static String getPropertyPathNoIdFromPropertyPathId(String propertyPathId) {
		// Path is "customer.contact.fax.number", need to return "number"
		log.debug("propertyPath = " + propertyPathId);
		// controllo se la property finisce con Id
		if (propertyPathId.endsWith("Id")) {
			propertyPathId = StringUtils.removeEnd(propertyPathId, "Id");
			propertyPathId = propertyPathId + ".displayValue";
			return propertyPathId;
		}
		if (propertyPathId.endsWith("]")) {
			if (!propertyPathId.contains("integerMap") && !propertyPathId.contains("booleanMap")
					&& !propertyPathId.contains("clobMap") && !propertyPathId.contains("dateMap")
					&& !propertyPathId.contains("numberMap") && !propertyPathId.contains("stringMap")) {
				propertyPathId = propertyPathId + ".displayValue";
			}

			return propertyPathId;
		}
		return propertyPathId;
	}

	public static boolean isBoolean(Object object, String propertyPath) {

		Field field = findField(object.getClass(), getPropertyFromPropertyPath(propertyPath));
		if (Boolean.class.equals(getReturnType(object, propertyPath))) {
			return true;
		} else {
			return false;
		}
	}

	public static String findEndLabel(String propertyPath) {

		if (StringUtils.contains(propertyPath, "[")) {
			return StringUtils.substringBetween(propertyPath, "[", "]");
		}
		return StringUtils.substringAfterLast(propertyPath, ".");
	}

	public static String escapeForJavascriptString(String _string) {
		String temp = StringUtils.replace(_string, "'", "&#39;");
		return temp;
	}

	public static String urlEncode(String _string) throws Exception {
		return URLEncoder.encode(_string, "UTF-8");
	}

	public static String escapeForJQ(String id) {
		String temp = StringUtils.replace(id, ".", "\\\\.");
		return StringUtils.replace(temp, "/", "-");
	}

	public static String removeEnd(String str, String remove) {
		return StringUtils.removeEnd(str, remove);
	}

	public static String substringForJavascript(String string, Integer start, Integer end) {
		String temp = StringEscapeUtils.escapeJavaScript(string);
		temp = StringEscapeUtils.escapeHtml(temp);
		return StringUtils.substring(temp, start, end);
	}

	public static Boolean isInvisibleWidget(Widget widget) {
		return widget instanceof HiddenWidget || widget instanceof ServerSideWidget;
	}

	public static String getI18nPropertyPath(String propertyPath) {
		if (StringUtils.contains(propertyPath, "[")) {
			return StringUtils.replace(propertyPath, "]", "_{I18N}]");
		}
		String lastValue = StringUtils.substringAfterLast(propertyPath, ".");
		return StringUtils.replace(propertyPath, "." + lastValue, ".stringMap[" + lastValue + "_{I18N}]");

	}

	public static Integer monthsBetween(Date startDate, Date endDate) {
		if (startDate == null || endDate == null)
			return null;
		Integer monthsBetween = Months.monthsBetween(new DateTime(startDate), new DateTime(endDate)).getMonths();
		return monthsBetween;
	}

	public static Integer monthsBetweenRoundUp(Date startDate, Date endDate) {
		if (startDate == null || endDate == null)
			return null;
		LocalDate date1 = new LocalDate(startDate.getTime());
		LocalDate date2 = new LocalDate(endDate.getTime());
		PeriodType monthDay = PeriodType.yearMonthDay().withYearsRemoved();
		Period difference = new Period(date1, date2, monthDay);
		int months = difference.getMonths();
		if (difference.getDays() >= 15)
			return months + 1;
		return months;
	}

	public static Boolean isDateBetween(Date dateToCheck, Date startDate, Date endDate) {
		if (dateToCheck == null)
			return true;
		if (startDate != null) {
			if (startDate.after(dateToCheck))
				return false;
		}
		if (endDate != null) {
			if (endDate.before(dateToCheck))
				return false;
		}
		return true;
	}

	public static String removeDoubleSpaces(String text) {
		return StringUtils.replace(text, "  ", " ", -1);
	}

	public static Collection orderCollection(Collection collection, String comparatorFqClassName)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (collection == null)
			return null;
		Comparator childComparator = (Comparator) Class.forName(comparatorFqClassName).newInstance();
		TreeSet orderedSet = new TreeSet(childComparator);
		orderedSet.addAll(collection);
		return orderedSet;
	}

	public static String evalScript(String script, Object object) {
		if (StringUtils.isBlank(script))
			return null;
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			engine.put("object", object);
			engine.put("messageUtil", MessageUtilConstant.messageUtil);
			return engine.eval(script).toString();
		} catch (ScriptException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static String[] substringsBetween(String str, String open, String close) {
		return StringUtils.substringsBetween(str, open, close);
	}

	public static String[] findPath(String str) {
		return StringUtils.substringsBetween(str, "${", "}");
	}

	public static Object evaluateExpression(String test, PageContext pageContext) throws JspException {
		if (StringUtils.isBlank(test))
			return null;
		log.debug("evaluateExpression " + test);
		Object result = ExpressionEvaluatorManager.evaluate("evaluate", test, Object.class, pageContext);
		log.debug("result " + result);
		return result;
	}

	public Map getMapForMultiple(Map<String, Object> map, String discriminator) throws Exception {
		Map newMap = map.getClass().newInstance();
		for (String string : map.keySet()) {
			if (StringUtils.startsWith(string, discriminator + "_")
					&& StringUtils.containsNone(StringUtils.substringAfter(string, discriminator + "_"), "_")) {
				newMap.put(string, map.get(string));
			}
		}
		return newMap;
	}
}
