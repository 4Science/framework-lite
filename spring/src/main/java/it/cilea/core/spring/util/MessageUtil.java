package it.cilea.core.spring.util;

import it.cilea.core.CoreConstant;
import it.cilea.core.spring.CoreSpringConstant;
import it.cilea.core.util.MessageUtilInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageUtil implements MessageUtilInterface {

	@Autowired
	@Qualifier("messageSource")
	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private static Logger log = LoggerFactory.getLogger(MessageUtil.class);

	public String findMessage(String messageKey, String language) {

		Locale locale = null;
		try {
			LocaleEditor editor = new LocaleEditor();
			editor.setAsText(language);
			locale = (Locale) editor.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return findMessage(messageKey, locale);

	}

	public String findMessage(String messageKey, Locale locale) {

		if (messageKey == null) {
			log.debug("MessageKey nullo");
			return messageKey;
		}
		try {
			return messageSource.getMessage(messageKey, null, locale);
		} catch (NoSuchMessageException e) {

		}
		return messageSource.getMessage(messageKey, null, "??????? " + messageKey + " ???????", null);
	}

	public String findMessage(String messageKey) {

		if (messageKey == null) {
			log.debug("MessageKey nullo");
			return messageKey;
		}
		try {
			return messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {

		}
		return messageSource.getMessage(messageKey, null, "??????? " + messageKey + " ???????", null);

	}

	public Boolean hasMessage(String messageKey) {

		if (messageKey == null) {
			return false;
		}
		try {
			messageSource.getMessage(messageKey, null, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {
			return false;
		}
		return true;

	}

	public String findMessage(String messageKey, Object[] params) {
		if (messageKey == null) {
			log.debug("MessageKey nullo");
			return "";
		}

		try {
			return messageSource.getMessage(messageKey, params, LocaleContextHolder.getLocale());
		} catch (NoSuchMessageException e) {

		}
		return messageSource.getMessage(messageKey, params, "??????? " + messageKey + " ???????", null);
	}

	public String findMessage(String messageKey, Object[] params, Locale locale) {
		if (messageKey == null) {
			log.debug("MessageKey nullo");
			return "";
		}

		try {
			return messageSource.getMessage(messageKey, params, locale);
		} catch (NoSuchMessageException e) {

		}
		return messageSource.getMessage(messageKey, params, "??????? " + messageKey + " ???????", null);
	}

	public static String findLabel(Map<String, String> labelI18nMap, String labelDefault, String prefixDiscriminator) {
		Locale locale = LocaleContextHolder.getLocale();
		if (labelI18nMap != null)
			for (String key : labelI18nMap.keySet())
				if (StringUtils.equals(key, prefixDiscriminator + locale.getLanguage()))
					return labelI18nMap.get(key);

		return labelDefault;
	}

	public static String findAllLabel(Map<String, String> labelI18nMap, String labelDefault, boolean i18nStrict,
			String platformLanguage, String prefixDiscriminator) {
		StringBuilder allI18Label = new StringBuilder();
		if (labelI18nMap != null)
			for (String key : labelI18nMap.keySet()) {
				if (StringUtils.startsWith(key, prefixDiscriminator)
						&& !key.equals(prefixDiscriminator + platformLanguage)) {
					allI18Label.append(" [" + StringUtils.substringAfter(key, prefixDiscriminator).toUpperCase() + "] "
							+ labelI18nMap.get(key));
				}
			}

		if (i18nStrict || labelI18nMap == null || labelI18nMap.isEmpty())
			allI18Label.insert(0, " [" + platformLanguage.toUpperCase() + "] " + labelDefault);

		return allI18Label.toString();
	}

	public static String findPieceOfStringByIndex(String string, int index, String delimitatore) {
		if (string == null)
			return null;
		try {
			return string.split(delimitatore)[index - 1];
		} catch (IndexOutOfBoundsException e) {
			return string.split(delimitatore)[0];
		} catch (Exception e) {
			return string.split(delimitatore)[0];
		}
	}

	public static String findPieceOfStringByIndex(String string, int index) {
		return findPieceOfStringByIndex(string, index, ",");
	}

	public static String findMessage(String messageKey, String language, String boundle) {
		Locale locale = null;
		try {
			if (language != null) {
				LocaleEditor editor = new LocaleEditor();
				editor.setAsText(language);
				locale = (Locale) editor.getValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (locale == null) {
			locale = LocaleContextHolder.getLocale();
		}
		ResourceBundle resources = null;
		if (boundle != null) {
			resources = ResourceBundle.getBundle(boundle, locale);
		} else {
			resources = ResourceBundle.getBundle(CoreConstant.MESSAGE_PROPERTIES, locale);
		}
		log.debug("KEY " + messageKey);
		if (messageKey == null) {
			log.debug("MessageKey nullo");
			return messageKey;
		}
		try {
			return resources.getString(messageKey);
		} catch (MissingResourceException e) {
			log.debug("Message key non trovato: " + messageKey);
		}

		return messageKey;

	}

	public Object getMessageSource() {
		return messageSource;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void saveMessage(HttpServletRequest request, String msg) {
		List messages = (List) request.getSession().getAttribute(CoreSpringConstant.MESSAGES_KEY);
		if (messages == null) {
			messages = new ArrayList();
		}
		messages.add(msg);
		request.getSession().setAttribute(CoreSpringConstant.MESSAGES_KEY, messages);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void saveError(HttpServletRequest request, String msg) {
		List messages = (List) request.getSession().getAttribute(CoreSpringConstant.ERRORS_KEY);
		if (messages == null) {
			messages = new ArrayList();
		}
		messages.add(msg);
		request.getSession().setAttribute(CoreSpringConstant.ERRORS_KEY, messages);
	}
}
