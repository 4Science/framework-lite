package it.cilea.core.validator;

import it.cilea.core.CoreConstant;
import it.cilea.core.model.SelectBase;
import it.cilea.core.util.MessageUtilConstant;
import it.cilea.core.util.SubnetUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.validation.Errors;
import org.xml.sax.ContentHandler;

public class XmlValidatorFactory {

	public static final String DEFAULT_ERROR_KEY = "label.errorMessage";

	private XmlValidatorFactory(String rule) {
	}

	public static XmlValidator getValidator(String rule, String fieldName) throws Exception {
		if (rule.startsWith("@Required"))
			return new RequiredValidator(rule, fieldName);
		else if (rule.startsWith("@Length"))
			return new LengthValidator(rule, fieldName);
		else if (rule.startsWith("@ByteLength"))
			return new ByteLengthValidator(rule, fieldName);
		else if (rule.startsWith("@HtmlLength"))
			return new HtmlLengthValidator(rule, fieldName);
		else if (rule.startsWith("@Ipv4"))
			return new Ipv4Validator(rule, fieldName);
		else if (rule.startsWith("@Ipv6"))
			return new Ipv6Validator(rule, fieldName);
		else if (rule.startsWith("@Mail"))
			return new Mail(rule, fieldName);
		else if (rule.startsWith("@Issn"))
			return new IssnValidator(rule, fieldName);
		else if (rule.startsWith("@NotZero"))
			return new NotZeroValidator(rule, fieldName);
		else if (rule.startsWith("@NullOrZero"))
			return new NullOrZeroValidator(rule, fieldName);
		else if (rule.startsWith("@Null"))
			return new NullValidator(rule, fieldName);
		else if (rule.startsWith("@Year"))
			return new YearValidator(rule, fieldName);
		else if (rule.startsWith("@Password"))
			return new Password(rule, fieldName);
		else if (rule.startsWith("@PwdOrNull"))
			return new PwdOrNull(rule, fieldName);
		else if (rule.startsWith("@DateOrNull"))
			return new DateOrNull(rule, fieldName);
		else if (rule.startsWith("@DbIdOrNull"))
			return new DbIdOrNull(rule, fieldName);
		else if (rule.startsWith("@Spel"))
			return new SpelValidator(rule, fieldName);
		else if (rule.startsWith("@ScriptFull"))
			return new ScriptFullValidator(rule, fieldName);
		else if (rule.startsWith("@Script"))
			return new ScriptValidator(rule, fieldName);
		else if (rule.startsWith("@Rhino"))
			return new RhinoValidator(rule, fieldName);
		return null;
	}

	public static String getVariableValue(String rule, String variable) {
		Object obj = getObjectValue(rule, variable);
		if (obj instanceof String) {
			return (String) obj;
		}
		return null;
	}

	public static Boolean getVariableValueBoolean(String rule, String variable) {
		Object obj = getObjectValue(rule, variable);
		if (obj instanceof Boolean)
			return (Boolean) obj;
		if (obj == null)
			return null;
		return BooleanUtils.toBoolean(obj.toString());
	}

	public static Integer getVariableValueInteger(String rule, String variable) {
		Object obj = getObjectValue(rule, variable);
		try {
			if (obj != null)
				return Integer.valueOf((String) obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] getVariableValueAsArray(String rule, String variable) {
		Object obj = getObjectValue(rule, variable);
		if (obj instanceof String[]) {
			return (String[]) obj;
		}
		return null;
	}

	private static Object getObjectValue(String rule, String variable) {

		rule = rule.substring(rule.indexOf("(") + 1);
		rule = rule.substring(0, rule.indexOf(")"));

		String[] variables = rule.split(",");
		for (String value : variables) {
			String variableName = value.substring(0, value.indexOf("=")).trim();
			if (variableName.equals(variable)) {
				value = value.substring(value.indexOf("=") + 1);
				value = value.replace("\"", "");

				if (value.indexOf("{") != -1) {
					// trovo una graffa quindi ho un array
					value = value.replace("{", "");
					value = value.replace("}", "");
					String[] arrayElement = value.split(",");

					for (int i = 0; i < arrayElement.length; i++) {
						arrayElement[i] = arrayElement[i].replace("\"", "");
						arrayElement[i] = arrayElement[i].trim();
					}

				} else {
					return value;
				}
			}
		}
		return null;
	}
}

class RequiredValidator implements XmlValidator {

	private String errorKey;
	private String fieldLabelKey;
	private String fieldName;
	private String i18nRequiredType;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected RequiredValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		List<SelectBase> i18nList = (List<SelectBase>) request.getServletContext().getAttribute("i18nList");
		// SelectBase
		// i18nBase=(SelectBase)request.getServletContext().getAttribute("i18nBase");

		if ("ALL".equals(i18nRequiredType)) {
			boolean isValid = isSingleItemValid(request, command, errors, fieldName);

			if (CollectionUtils.isNotEmpty(i18nList)) {
				for (SelectBase i18n : i18nList) {
					isValid &= isSingleItemValid(request, command, errors, getI18nFieldName(fieldName, i18n.getValue()));
				}
			}
			if (!isValid)
				rejectValue(fieldName, errors);
		} else if ("AT_LEAST_ONE".equals(i18nRequiredType)) {
			boolean isValid = isSingleItemValid(request, command, errors, fieldName);

			if (CollectionUtils.isNotEmpty(i18nList)) {
				for (SelectBase i18n : i18nList) {
					isValid |= isSingleItemValid(request, command, errors, getI18nFieldName(fieldName, i18n.getValue()));
				}
			}
			if (!isValid)
				rejectValue(fieldName, errors);
		} else {
			if (!isSingleItemValid(request, command, errors, fieldName))
				rejectValue(fieldName, errors);
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		fieldLabelKey = XmlValidatorFactory.getVariableValue(rule, "fieldLabelKey");
		i18nRequiredType = XmlValidatorFactory.getVariableValue(rule, "i18nRequiredType");
		this.fieldName = fieldName;
	}

	private boolean isSingleItemValid(HttpServletRequest request, Object command, Errors errors, String fieldName)
			throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
			return false;
		} else
			return true;
	}

	private void rejectValue(String fieldName, Errors errors) {
		if (fieldLabelKey == null)
			errors.rejectValue(fieldName, errorKey);
		else
			errors.rejectValue(fieldName, errorKey,
					new Object[] { MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) }, null);
	}

	private String getI18nFieldName(String fieldName, String language) throws Exception {
		String newFieldName;
		if (StringUtils.contains(fieldName, "[")) {
			newFieldName = StringUtils.replace(fieldName, "]", "_" + language + "]");
		} else {
			String lastValue = StringUtils.substringAfterLast(fieldName, ".");
			newFieldName = StringUtils.replace(fieldName, "." + lastValue, ".stringMap[" + lastValue + "_" + language
					+ "]");
		}
		return newFieldName;
	}
}

class LengthValidator implements XmlValidator {
	private String errorKey;
	private String fieldLabelKey;
	private Integer min;
	private Integer max;
	private String fieldName;
	private Boolean stripHtml;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected LengthValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing LengthValidator....");

		boolean error = false;
		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
			if (min != null)
				error = true;
		} else {
			Integer length = 0;
			if (stripHtml == null || !stripHtml)
				length = value.toString().trim().length();
			else {
				InputStream is = new ByteArrayInputStream(value.toString().getBytes());
				ContentHandler contenthandler = new BodyContentHandler();
				Metadata metadata = new Metadata();
				Parser parser = new HtmlParser();
				parser.parse(is, contenthandler, metadata, new ParseContext());
				String htmlStripped = StringUtils.trim(contenthandler.toString());
				if (htmlStripped != null)
					length = htmlStripped.length();
			}
			log.info("length: " + length);
			if (min != null && length < min)
				error = true;
			else if (max != null && length > max)
				error = true;
		}
		if (error)
			if (fieldLabelKey == null)
				errors.rejectValue(fieldName, errorKey, new Object[] { min, max }, null);
			else
				errors.rejectValue(fieldName, errorKey,
						new Object[] { min, max, MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) }, null);

	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		min = XmlValidatorFactory.getVariableValueInteger(rule, "min");
		max = XmlValidatorFactory.getVariableValueInteger(rule, "max");
		fieldLabelKey = XmlValidatorFactory.getVariableValue(rule, "fieldLabelKey");
		stripHtml = XmlValidatorFactory.getVariableValueBoolean(rule, "stripHtml");
		this.fieldName = fieldName;
	}
}



class ByteLengthValidator implements XmlValidator {
	private String errorKey;
	private String fieldLabelKey;
	private Integer min;
	private Integer max;
	private String fieldName;
	private Boolean stripHtml;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected ByteLengthValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing ByteLengthValidator....");

		boolean error = false;
		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
			if (min != null)
				error = true;
		} else {
			Integer length = 0;
			if (stripHtml == null || !stripHtml)
				// length = value.toString().trim().length();
				length = value.toString().getBytes().length;
			else {
				InputStream is = new ByteArrayInputStream(value.toString().getBytes());
				ContentHandler contenthandler = new BodyContentHandler();
				Metadata metadata = new Metadata();
				Parser parser = new HtmlParser();
				parser.parse(is, contenthandler, metadata, new ParseContext());
				String htmlStripped = StringUtils.trim(contenthandler.toString());
				if (htmlStripped != null){
					// length = htmlStripped.length();
					length = htmlStripped.getBytes().length;
				}
				
			}
			log.info("Bytes length: " + length);
			if (min != null && length < min)
				error = true;
			else if (max != null && length > max)
				error = true;
		}
		if (error)
			if (fieldLabelKey == null)
				errors.rejectValue(fieldName, errorKey, new Object[] { min, max }, null);
			else
				errors.rejectValue(fieldName, errorKey,
						new Object[] { min, max, MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) }, null);

	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		min = XmlValidatorFactory.getVariableValueInteger(rule, "min");
		max = XmlValidatorFactory.getVariableValueInteger(rule, "max");
		fieldLabelKey = XmlValidatorFactory.getVariableValue(rule, "fieldLabelKey");
		stripHtml = XmlValidatorFactory.getVariableValueBoolean(rule, "stripHtml");
		this.fieldName = fieldName;
	}
}











class HtmlLengthValidator implements XmlValidator {

	private String errorKey;
	private String fieldLabelKey;
	private Integer min;
	private Integer max;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected HtmlLengthValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing LengthValidator....");

		boolean error = false;
		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
			if (min != null)
				error = true;
		} else {
			InputStream is = new ByteArrayInputStream(value.toString().getBytes());
			ContentHandler contenthandler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			Parser parser = new HtmlParser();
			parser.parse(is, contenthandler, metadata, new ParseContext());
			String htmlStripped = StringUtils.trim(contenthandler.toString());
			Integer length = htmlStripped == null ? 0 : htmlStripped.length();
			log.info("length " + length);
			if (min != null && length < min)
				error = true;
			else if (max != null && length > max)
				error = true;
		}
		if (error)
			if (fieldLabelKey == null)
				errors.rejectValue(fieldName, errorKey, new Object[] { min, max }, null);
			else
				errors.rejectValue(fieldName, errorKey,
						new Object[] { min, max, MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) }, null);

	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		min = XmlValidatorFactory.getVariableValueInteger(rule, "min");
		max = XmlValidatorFactory.getVariableValueInteger(rule, "max");
		fieldLabelKey = XmlValidatorFactory.getVariableValue(rule, "fieldLabelKey");
		this.fieldName = fieldName;
	}
}

class Ipv4Validator implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected Ipv4Validator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing Ipv4Validator....");
		if (StringUtils.isNotBlank((String) value)) {
			String[] ips = value.toString().split(",");
			for (String ip : ips) {
				ip = ip.trim();
				String[] ipAndMask = {};
				String mask = null;
				if (ip.contains("/")) {
					ipAndMask = ip.split("/");
					if (ipAndMask.length == 2) {
						ip = ipAndMask[0];
						mask = ipAndMask[1];
					} else {
						errors.rejectValue(fieldName, errorKey);
					}
				}

				String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
				Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");
				Matcher matcher = pattern.matcher(ip);
				if (!matcher.matches()) {
					errors.rejectValue(fieldName, errorKey);
				} else {
					if (ipAndMask.length == 2) {
						SubnetUtils subnetUtils = new SubnetUtils(ip, mask);
						subnetUtils.setInclusiveHostCount(true);
						if (!subnetUtils.getInfo().isInRange(ip)) {
							errors.rejectValue(fieldName, errorKey);
						}
					}
				}
			}
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class Ipv6Validator implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected Ipv6Validator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing Ipv6Validator....");

		if (StringUtils.isNotBlank((String) value)) {
			String[] REGEXIPV6 = {
					"\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?) ::((?:[0-9A-Fa-f]{1,4}:)*)(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z",
					"\\A((?:[0-9A-Fa-f]{1,4}:){6,6})(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z",
					"\\A((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)\\z",
					"\\A(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}\\z" };
			for (String i : REGEXIPV6) {
				Pattern pattern = Pattern.compile(i);
				Matcher matcher = pattern.matcher((String) value);
				if (!matcher.matches()) {
					errors.rejectValue(fieldName, errorKey);
					break;
				}
			}
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class Mail implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected Mail(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing Mail Validator....");

		if (StringUtils.isNotBlank((String) value)) {
			Pattern pattern = Pattern
					.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[\\-A-Za-z0-9]+(\\.[\\-A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			Matcher matcher = pattern.matcher((String) value);
			if (!matcher.matches()) {
				errors.rejectValue(fieldName, errorKey);
			}
		}// else {
			// errors.rejectValue(fieldName, errorKey);
			// }
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class PwdOrNull implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected PwdOrNull(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "[")) {
			value = PropertyUtils.getProperty(command, fieldName);
		} else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing PwdOrNull Validator....");

		if (StringUtils.isNotBlank((String) value)) {
			Pattern pattern0 = Pattern.compile("^.*[a-z]{1,1}.*$");
			Pattern pattern1 = Pattern.compile("^.*[A-Z]{1,1}.*$");
			Pattern pattern2 = Pattern.compile("^.*[0-9]{1,1}.*$");
			Matcher matcher0 = pattern0.matcher((String) value);
			Matcher matcher1 = pattern1.matcher((String) value);
			Matcher matcher2 = pattern2.matcher((String) value);

			if ((((String) value).length() < 8) || (((String) value).length() > 255)) {
				errors.rejectValue(fieldName, errorKey);
			} else {

				if (!matcher0.matches()) {
					errors.rejectValue(fieldName, errorKey);
				} else {
					if (!matcher1.matches()) {
						errors.rejectValue(fieldName, errorKey);
					} else {
						if (!matcher2.matches()) {
							errors.rejectValue(fieldName, errorKey);
						}
					}

				}

			}
		} else {
			// ok null
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class Password implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected Password(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "[")) {
			value = PropertyUtils.getProperty(command, fieldName);
		} else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing Password Validator....");

		if (StringUtils.isNotBlank((String) value)) {
			Pattern pattern0 = Pattern.compile("^.*[a-z]{1,1}.*$");
			Pattern pattern1 = Pattern.compile("^.*[A-Z]{1,1}.*$");
			Pattern pattern2 = Pattern.compile("^.*[0-9]{1,1}.*$");
			Matcher matcher0 = pattern0.matcher((String) value);
			Matcher matcher1 = pattern1.matcher((String) value);
			Matcher matcher2 = pattern2.matcher((String) value);

			if ((((String) value).length() < 8) || (((String) value).length() > 255)) {
				errors.rejectValue(fieldName, errorKey);
			} else {

				if (!matcher0.matches()) {
					errors.rejectValue(fieldName, errorKey);
				} else {
					if (!matcher1.matches()) {
						errors.rejectValue(fieldName, errorKey);
					} else {
						if (!matcher2.matches()) {
							errors.rejectValue(fieldName, errorKey);
						}
					}

				}

			}
		} else {
			errors.rejectValue(fieldName, errorKey);
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class IssnValidator implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected IssnValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing Issn Validator....");

		if (StringUtils.isNotBlank((String) value)) {
			Pattern pattern = Pattern.compile("^[0-9]{4}-[0-9]{3}[0-9X]$");
			Matcher matcher = pattern.matcher((String) value);
			if (!matcher.matches()) {
				errors.rejectValue(fieldName, errorKey);
			}
		} // else {
			// errors.rejectValue(fieldName, errorKey);
		// }
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class Url implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected Url(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing Url Validator....");

		if (StringUtils.isNotBlank((String) value)) {
			Pattern pattern = Pattern.compile("(https?|ftp)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
			Matcher matcher = pattern.matcher((String) value);
			if (!matcher.matches()) {
				errors.rejectValue(fieldName, errorKey);
			}
		} else {
			errors.rejectValue(fieldName, errorKey);
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class NotZeroValidator implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected NotZeroValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
			errors.rejectValue(fieldName, errorKey);
		} else if (NumberUtils.isNumber(value.toString())
				&& NumberUtils.createBigDecimal(value.toString()).equals(BigDecimal.ZERO)) {
			errors.rejectValue(fieldName, errorKey);
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class NullValidator implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected NullValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
		} else {
			errors.rejectValue(fieldName, errorKey);
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class NullOrZeroValidator implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected NullOrZeroValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		if (value == null
				|| StringUtils.isEmpty(value.toString().trim())
				|| (NumberUtils.isNumber(value.toString()) && NumberUtils.createBigDecimal(value.toString()).equals(
						BigDecimal.ZERO))) {
		} else
			errors.rejectValue(fieldName, errorKey);
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class YearValidator implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected YearValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		if (!(value == null || StringUtils.isEmpty(value.toString().trim()))) {
			if (((Integer) value).compareTo(1000) <= 0 || ((Integer) value).compareTo(3000) >= 0) {

				errors.rejectValue(fieldName, errorKey);

			}
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}

}

class DbIdOrNull implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected DbIdOrNull(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;

		if (!StringUtils.contains(fieldName, "[")) {
			value = PropertyUtils.getProperty(command, fieldName);
		} else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing DbIdOrNull Validator....");

		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
			// ok null
		} else {

			if (!CoreConstant.dbIdPattern.matcher((String) value).matches()) {
				errors.rejectValue(fieldName, errorKey);
			}

		}

	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class DateOrNull implements XmlValidator {

	private String errorKey;
	private String fieldName;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected DateOrNull(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;

		if (!StringUtils.contains(fieldName, "[")) {
			value = PropertyUtils.getProperty(command, fieldName);
		} else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing DateOrNull Validator....");

		if (value == null || StringUtils.isEmpty(value.toString().trim())) {
			// ok null
		} else {

			if (!CoreConstant.dataPattern.matcher((String) value).matches()) {
				errors.rejectValue(fieldName, errorKey);
			}

		}

	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		this.fieldName = fieldName;
	}
}

class SpelValidator implements XmlValidator {

	private String errorKey;
	private String fieldLabelKey;
	private String fieldName;
	private String spel;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected SpelValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		if (!isValid(request, command, errors, fieldName))
			rejectValue(fieldName, errors);
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		fieldLabelKey = XmlValidatorFactory.getVariableValue(rule, "fieldLabelKey");
		spel = XmlValidatorFactory.getVariableValue(rule, "spel");
		this.fieldName = fieldName;
	}

	private boolean isValid(HttpServletRequest request, Object command, Errors errors, String fieldName)
			throws Exception {
		Object value = null;
		if (fieldName == null || "*".equals(fieldName))
			value = command;
		else if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");
		ExpressionParser parser = new SpelExpressionParser();
		boolean returnValue = (Boolean) parser.parseExpression(spel).getValue(value);
		return returnValue;

	}

	private void rejectValue(String fieldName, Errors errors) {
		if (fieldName == null || "*".equals(fieldName)) {
			if (fieldLabelKey == null)
				errors.reject(errorKey);
			else
				errors.reject(errorKey, new Object[] { MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) },
						null);
		} else {
			if (fieldLabelKey == null)
				errors.rejectValue(fieldName, errorKey);
			else
				errors.rejectValue(fieldName, errorKey,
						new Object[] { MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) }, null);
		}
	}

}

class ScriptValidator implements XmlValidator {

	private String errorKey;
	private String fieldLabelKey;
	private String fieldName;
	private String script;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected ScriptValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		if (!isValid(request, command, errors, fieldName))
			rejectValue(fieldName, errors);
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		fieldLabelKey = XmlValidatorFactory.getVariableValue(rule, "fieldLabelKey");
		script = XmlValidatorFactory.getVariableValue(rule, "script");
		this.fieldName = fieldName;
	}

	private boolean isValid(HttpServletRequest request, Object command, Errors errors, String fieldName)
			throws Exception {
		Object value = null;
		if (fieldName == null || "*".equals(fieldName))
			value = command;
		else if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		engine.put("object", value);
		engine.put("messageUtil", MessageUtilConstant.messageUtil);
		Boolean returnValue = (Boolean) engine.eval(script);
		return returnValue;
	}

	private void rejectValue(String fieldName, Errors errors) {
		if (fieldName == null || "*".equals(fieldName)) {
			if (fieldLabelKey == null)
				errors.reject(errorKey);
			else
				errors.reject(errorKey, new Object[] { MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) },
						null);
		} else {
			if (fieldLabelKey == null)
				errors.rejectValue(fieldName, errorKey);
			else
				errors.rejectValue(fieldName, errorKey,
						new Object[] { MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) }, null);
		}
	}

}

class ScriptFullValidator implements XmlValidator {

	private String fieldName;
	private String script;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected ScriptFullValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Object value = null;
		if (fieldName == null || "*".equals(fieldName))
			value = command;
		else if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine engine = mgr.getEngineByName("JavaScript");
		try {
			engine.put("object", value);
			engine.put("messageUtil", MessageUtilConstant.messageUtil);
			engine.put("request", request);
			engine.put("errors", errors);
			engine.put("command", command);
			engine.eval(script);
		} catch (ScriptException ex) {
			ex.printStackTrace();
			errors.reject("error.validation");
		}
	}

	public void initialise(String rule, String fieldName) throws Exception {
		script = StringUtils.substringAfter(rule, "(");
		script = StringUtils.substringBeforeLast(script, ")");
		this.fieldName = fieldName;
	}
}

class RhinoValidator implements XmlValidator {

	private String errorKey;
	private String fieldLabelKey;
	private String fieldName;
	private String rhino;
	private Logger log = LoggerFactory.getLogger(this.getClass());

	protected RhinoValidator(String rule, String fieldName) throws Exception {
		initialise(rule, fieldName);
	}

	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception {
		if (!isValid(request, command, errors, fieldName))
			rejectValue(fieldName, errors);
	}

	public void initialise(String rule, String fieldName) throws Exception {
		errorKey = XmlValidatorFactory.getVariableValue(rule, "errorKey");
		if (StringUtils.isEmpty(errorKey))
			errorKey = XmlValidatorFactory.DEFAULT_ERROR_KEY;
		fieldLabelKey = XmlValidatorFactory.getVariableValue(rule, "fieldLabelKey");
		rhino = XmlValidatorFactory.getVariableValue(rule, "rhino");
		this.fieldName = fieldName;
	}

	private boolean isValid(HttpServletRequest request, Object command, Errors errors, String fieldName)
			throws Exception {
		Object value = null;
		if (fieldName == null || "*".equals(fieldName))
			value = command;
		else if (!StringUtils.contains(fieldName, "["))
			value = PropertyUtils.getProperty(command, fieldName);
		else {
			Map map = (Map) PropertyUtils.getProperty(command, StringUtils.substringBefore(fieldName, "["));
			String key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "['"), "']");
			if (StringUtils.isBlank(key)) {
				key = StringUtils.substringBefore(StringUtils.substringAfter(fieldName, "["), "]");
			}
			value = map.get(key);
		}
		log.info("Executing RequiredValidator....");

		String resultString = (String) ContextFactory.getGlobal().call(new ContextAction() {
			public Object run(Context cx) {
				Scriptable scope = cx.initStandardObjects();
				String script = "function f(){\n" + rhino + "\n} f();";
				Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);
				String resultString = Context.toString(result);
				return resultString;
			}
		});

		return Boolean.valueOf(resultString);
	}

	private void rejectValue(String fieldName, Errors errors) {
		if (fieldName == null || "*".equals(fieldName)) {
			if (fieldLabelKey == null)
				errors.reject(errorKey);
			else
				errors.reject(errorKey, new Object[] { MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) },
						null);
		} else {
			if (fieldLabelKey == null)
				errors.rejectValue(fieldName, errorKey);
			else
				errors.rejectValue(fieldName, errorKey,
						new Object[] { MessageUtilConstant.messageUtil.findMessage(fieldLabelKey) }, null);
		}
	}

}
