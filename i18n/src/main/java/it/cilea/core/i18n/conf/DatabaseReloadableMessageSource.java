package it.cilea.core.i18n.conf;
import it.cilea.core.i18n.service.I18nService;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.AbstractMessageSource;

public class DatabaseReloadableMessageSource extends AbstractMessageSource {

	private String context;
	
	private boolean init = false;

	public DatabaseReloadableMessageSource(String context) {
		this.context = context;
	}

	protected static Logger log = LoggerFactory.getLogger(DatabaseReloadableMessageSource.class);

	@Qualifier("i18nService")
	@Autowired
	private I18nService i18nService;

	public DatabaseReloadableMessageSource() {
		super();
		// System.out.println("-------> " + CoreConstant.MODULE_NAME +
		// " DatabaseReloadableMessageSource()");
	}

	public void setI18nService(I18nService i18nService) {
		this.i18nService = i18nService;
	}

	private final Map<String, Map<String, String>> properties = new HashMap<String, Map<String, String>>();

	/*
	 * public String getMessage(String code, Object[] args, String
	 * defaultMessage, Locale locale) {
	 * 
	 * return null; }
	 * 
	 * public String getMessage(String code, Object[] args, Locale locale)
	 * throws NoSuchMessageException {
	 * 
	 * return null; }
	 * 
	 * public String getMessage(MessageSourceResolvable resolvable, Locale
	 * locale) throws NoSuchMessageException { return null; }
	 */

	public void reload() {
		// System.out.println("-------> " + CoreConstant.MODULE_NAME +
		// " DatabaseReloadableMessageSource() reload");	    
		properties.clear();
		properties.putAll(i18nService.getI18nMap(context));
		/*
		 * // log System.out.println("DatabaseReloadableMessageSource context'"
		 * + context + "'"); Map<String, Map<String, String>> m1 =
		 * i18nService.getI18nMap(context); for (String k1 : m1.keySet()) {
		 * String key = k1.toString(); Map<String, String> value1 = m1.get(k1);
		 * for (String k2 : value1.keySet()) { String key2 = k2.toString();
		 * String value2 = value1.get(key2); System.out.println("key: '" + key +
		 * "' key2 '" + key2 + "' value: '" + value2 + "'"); } }
		 * System.out.println
		 * ("-----------------------------------------------------"); // end log
		 */
	}
	
	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		String msg = getText(code, locale);
		MessageFormat result = null;
		if(msg!=null) { 
		    result = createMessageFormat(msg, locale);
		}
		return result;
	}

	@Override
	protected String resolveCodeWithoutArguments(String code, Locale locale) {
		return getText(code, locale);
	}

	private String getText(String code, Locale locale) {
		Map<String, String> localized = getProperties().get(code);

		String textForCurrentLanguage = null;
		if (localized != null) {
			textForCurrentLanguage = localized.get(locale.getLanguage());
			if (textForCurrentLanguage == null) {
				textForCurrentLanguage = localized.get("default");
			}
		}
		return textForCurrentLanguage;
	}

	public Map<String, Map<String, String>> getProperties() {
        if (init == false)
        {
            reload();
            init = true;
        }
		return properties;
	}
}
