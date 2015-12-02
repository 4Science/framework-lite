package it.cilea.core.i18n.conf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import it.cilea.core.CoreConstant;

public class StaticMessageSource extends ResourceBundleMessageSource {

	private List<String> basenames;

	private List<String> moduleBasenames;

	public StaticMessageSource() {
		super();
	}

	/**
	 * 
	 * Restituisce la lista di tutte le keywords per tutti i basename/contesto
	 * specificato.
	 * 
	 * 
	 * @param baseName
	 *            : se null vengono recuperate le chiavi per tutti i
	 *            basename/contesti
	 * @param locale
	 *            : se null viene recuperato il locale di default
	 * @return
	 */
	private Map<String, String> getKeyValues(List<String> baseName, Locale locale) {
		Map<String, String> staticProperties = new HashMap<String, String>();

		Set<String> keys = new HashSet<String>();

		if (locale == null) {
			locale = LocaleContextHolder.getLocale();
		}

		for (String base : baseName) {
			ResourceBundle bundle = getResourceBundle(base, locale);
			if (bundle != null) {
				for (String key : bundle.keySet()) {
					staticProperties.put(key, bundle.getString(key));
				}
			}

		}

		return staticProperties;
	}

	/**
	 * Restituisce la lista di tutte le keyword per tutti i basename/contesti
	 * 
	 * @param locale
	 *            : se null viene recuperato il locale di default
	 * @return
	 */
	public Map<String, String> getKeyValues(Locale locale) {
		return this.getKeyValues(this.basenames, locale);
	}

	/**
	 * Restituisce la lista delle chiavi specifiche del modulo senza considerare
	 * quelle refinite nei basename comuni
	 * 
	 * @param locale
	 * @return
	 */
	public Map<String, String> getModuleKeyValues(Locale locale) {
		return this.getKeyValues(getModuleBase(), locale);
	}

	/**
	 * Restituisce la lista delle chiavi definite nei basename senza considerare
	 * quelle specifiche del modulo
	 * 
	 * @param locale
	 * @return
	 */
	public Map<String, String> getCommonKeyValues(Locale locale) {
		List<String> commonBasename = new ArrayList<String>();
		if (commonBasename != null) {
			for (String basename : this.basenames) {
				if (!getModuleBase().contains(basename)) {
					commonBasename.add(basename);
				}
			}

		}

		return this.getKeyValues(commonBasename, locale);
	}

	public List getBaseNames() {
		return this.basenames;
	}

	public void setBasenames(String[] basenames) {
		if (this.basenames == null) {
			this.basenames = new ArrayList<String>();
		}
		this.basenames.addAll(Arrays.asList(basenames));
		super.setBasenames(basenames);
	}

	public void setModuleBasenames(String[] moduleBasenames) {
		getModuleBase();
		this.moduleBasenames.addAll(Arrays.asList(moduleBasenames));
	}

	public List<String> getModuleBase() {
		if (this.moduleBasenames == null) {
			this.moduleBasenames = new ArrayList<String>();
		}
		return this.moduleBasenames;
	}

	/*
	 * restituisce una map con i valori common accessibili per locale/key
	 */
	public Map<Locale, Map<String, String>> getAllLocaleCommonKeyValues() {

		Map<Locale, Map<String, String>> allLocaleCommonKeyValues = new HashMap<Locale, Map<String, String>>();

		String[] languageArray = CoreConstant.I18N_LIST.split(",");

		String[] allLanguageArray = new String[languageArray.length + 1];

		for (int j = 0; j < languageArray.length; j++) {
			allLanguageArray[j] = languageArray[j];
		}
		allLanguageArray[languageArray.length] = CoreConstant.I18N_BASE;

		List<String> commonBasename = new ArrayList<String>();
		for (String basename : this.basenames) {
			if (!getModuleBase().contains(basename)) {
				commonBasename.add(basename);
			}

		}

		for (int i = 0; i < allLanguageArray.length; i++) {

			Locale l = Locale.forLanguageTag(allLanguageArray[i]);

			Map<String, String> m = null;
			if (allLocaleCommonKeyValues.containsKey(l)) {
				m = allLocaleCommonKeyValues.get(l);
			} else {
				m = new HashMap<String, String>();
			}

			m = this.getKeyValues(commonBasename, l);

			allLocaleCommonKeyValues.put(l, m);
		}

		return allLocaleCommonKeyValues;
	}

	// get common key value only if locale value exists
	public String getCommonKey(String key, Locale locale) {

		List<String> commonBasename = new ArrayList<String>();
		for (String basename : this.basenames) {
			if (!getModuleBase().contains(basename)) {
				commonBasename.add(basename);
			}

		}
		String staticProp = null;

		Set<String> keys = new HashSet<String>();

		Locale localeDefault = LocaleContextHolder.getLocale();

		if (localeDefault.equals(locale)) {

			for (String base : commonBasename) {
				ResourceBundle bundle = getResourceBundle(base, locale);

				if (bundle != null) {
					if (bundle.containsKey(key)) {
						staticProp = bundle.getString(key);
					}
				}

			}

		} else {

			for (String base : commonBasename) {
				ResourceBundle bundle = getResourceBundle(base, locale);
				ResourceBundle bundleDefault = getResourceBundle(base, localeDefault);

				if (bundle != null) {
					if (bundle.containsKey(key)) {
						staticProp = bundle.getString(key);
						String staticPropDefault = bundleDefault.getString(key);
						if (staticProp.equals(staticPropDefault)) {
							staticProp = null;
						}
					}
				}

			}
		}

		return staticProp;

	}

}
