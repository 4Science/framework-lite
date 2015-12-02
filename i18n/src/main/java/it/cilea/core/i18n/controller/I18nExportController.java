package it.cilea.core.i18n.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.CoreConstant;
import it.cilea.core.dto.MultipleChoice;
import it.cilea.core.i18n.conf.CileaMessageSource;
import it.cilea.core.i18n.conf.DatabaseReloadableMessageSource;
import it.cilea.core.i18n.conf.StaticMessageSource;
import it.cilea.core.i18n.model.I18nExport;
import it.cilea.core.i18n.service.I18nService;
import it.cilea.core.i18n.util.I18nUtil;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
public class I18nExportController extends Spring3CoreController {
	@Autowired(required = false)
	private I18nService i18nService;
	@Autowired(required = false)
	private DatabaseReloadableMessageSource dynamicMessageSource;
	@Autowired(required = false)
	private StaticMessageSource staticMessageSource;

	@Autowired()
	private org.springframework.context.MessageSource messageSource;

	public void setMessageSource(CileaMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private static String KEY_VALUE_SEPARATOR = "=";

	@RequestMapping(value = { "/i18n/export" }, method = RequestMethod.GET)
	public ModelAndView loadGet(@ModelAttribute("command") I18nExport command, HttpServletRequest request) {
		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewName(), "command", command);
	}

	@RequestMapping(value = { "/i18n/export" }, method = RequestMethod.POST)
	public void loadPost(@Valid @ModelAttribute("command") I18nExport command, BindingResult result,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Locale locale = null;
		if (StringUtils.isNotBlank(command.getLanguage())) {
			locale = new Locale(command.getLanguage());

		} else {

		}

		String applContext = command.getContext();

		if (applContext != null) {

			if ("".equals(applContext.trim())) {
				applContext = null;
			}
		}

		String language = null;
		// System.out.println("Export applContext: " + applContext);
		if (locale != null) {
			// System.out.println("Export locale.getLanguage(): " +
			// locale.getLanguage());
			language = locale.getLanguage();
		} else {
			// System.out.println("Export default Language()");
			language = CoreConstant.I18N_BASE;
		}

		StringBuffer buffer = new StringBuffer();

		Map<String, String> mergeProperties = new HashMap<String, String>();

		switch (command.getTipoExport()) {
		case 0:// ALL
				//
			mergeProperties.putAll(getStaticKeys(applContext, locale));

			// le chiavi esatte per contesto/comuni con il locale specificato o
			// quello di default se nessuno e' specificato (tendina indica
			// default,'')
			mergeProperties.putAll(getDynamicKeys(applContext, locale, true));

			break;
		case 1:// SOLO DATABASE
			mergeProperties.putAll(getDynamicKeys(applContext, locale, true));
			// allKey = Arrays.asList(mergedKeys.toArray());
			break;
		case 2:// SOLO FILE
			mergeProperties.putAll(getStaticKeys(applContext, locale));
			// allKey = Arrays.asList(mergedKeys.toArray());
			break;
		default: // TIPO NON VALIDO!
			buffer.append("Tipo export non valido!");
			break;
		}

		Properties prop = new Properties();

		prop.putAll(mergeProperties);

		String fileName = null;
		String fileNameApplContext = applContext;
		if (fileNameApplContext == null) {
			fileNameApplContext = "all";
		}
		if ("".equals(fileNameApplContext)) {
			fileNameApplContext = "all";
		}

		fileName = "Export_i18n_" + language + "_" + fileNameApplContext + "_type" + command.getTipoExport() + ".txt";

		I18nUtil.writeStreamContext(prop, response, fileName,
				command.getSortExport() != null && command.getSortExport(), command.getCharset());

	}

	/**
	 * Restituisce la mappa chiava/valore statica per il locale ed il contesto
	 * di riferimento
	 * 
	 * @param applContext
	 * @param locale
	 * @return
	 */
	private Map<String, String> getStaticKeys(String applContext, Locale locale) {

		// le chiavi esatte per contesto/comuni con il locale specificato o
		// quello di default se nessuno e' specificato (tendina indica
		// default,'')

		String localeString = null;

		if (locale == null) {
			// System.out.println(" utilizza CoreConstant.I18N_BASE " +
			// CoreConstant.I18N_BASE);
			localeString = CoreConstant.I18N_BASE;
		} else {
			// System.out.println(" utilizza locale.getLanguage " +
			// locale.getLanguage());
			localeString = locale.getLanguage();
		}

		Map<String, String> I18N_BASEstaticProperties = null;

		Map<String, String> staticProperties = null;

		// ok leggo direttamente dallo staticMS
		staticProperties = staticMessageSource.getCommonKeyValues(locale);

		if (localeString.equals(CoreConstant.I18N_BASE)) {
			// ok
		} else {

			I18N_BASEstaticProperties = staticMessageSource
					.getCommonKeyValues(Locale.forLanguageTag(CoreConstant.I18N_BASE));

			if (staticProperties != null) {
				if (I18N_BASEstaticProperties != null) {
					staticProperties = minus(staticProperties, I18N_BASEstaticProperties);
				}

			}
		}

		// ---

		return staticProperties;
	}

	/**
	 * Restituisce la mappa chiava/valore dinamica per il locale ed il contesto
	 * di riferimento
	 * 
	 * @param applContext
	 * @param locale
	 * @param exactly
	 * @return
	 */
	private Map<String, String> getDynamicKeys(String applContext, Locale locale, boolean exactly) {
		Map<String, String> dynamicProperties = new HashMap<String, String>();

		if (exactly) {

			// qui getDynamicKeys restituisce:
			// 1) <key,<default,value>>
			// 2) <key,<en,value>> <key,<es,value>> ...
			//

			Map<String, Map<String, String>> contextProperties = i18nService.getI18nMap(applContext, true);

			// Map<String, Map<String, String>> defaultProperties =
			// i18nService.getI18nMap(null, true);

			String language = "default";
			if (locale != null) {
				language = locale.getLanguage();
			}

			// Restituisco solo le chiavi per cui esiste effettivamente un
			// valore per il locale passato
			for (String key : contextProperties.keySet()) {

				String value = contextProperties.get(key).get(language);

				if (value != null) {
					dynamicProperties.put(key, value);
				}
			}

			// non serve lo fa gia getI18nMap
			// Se il contesto e' null prendo anche quelle di default
			// if (applContext == null) {
			// for (String key : defaultProperties.keySet()) {
			// String value = defaultProperties.get(key).get(language);
			// if (locale == null || value != null) {
			// dynamicProperties.put(key, value);
			// }
			// }
			// }

		} else {
			for (String key : dynamicMessageSource.getProperties().keySet()) {
				dynamicProperties.put(key, dynamicMessageSource.getMessage(key, null, locale));
			}
		}

		return dynamicProperties;
	}

	public void setI18nService(I18nService i18nService) {
		this.i18nService = i18nService;
	}

	@ModelAttribute()
	public void genericReferenceData(@Valid I18nExport command, BindingResult result, HttpServletRequest request)
			throws Exception {
		if (!isFormSubmission(request) && StringUtils
				.isNotBlank(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName())) {
			List<MultipleChoice> list = (List<MultipleChoice>) context
					.getBean(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName());
			super.genericReferenceData(command, request, list);
		}
	}

	public void genericReferenceDataNoCheckFormSubmission(@Valid I18nExport command, BindingResult result,
			HttpServletRequest request) throws Exception {
		if (StringUtils.isNotBlank(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName())) {
			List<MultipleChoice> list = (List<MultipleChoice>) context
					.getBean(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName());
			super.genericReferenceData(command, request, list);
		}
	}

	public void setDynamicMessageSource(DatabaseReloadableMessageSource dynamicMessageSource) {
		this.dynamicMessageSource = dynamicMessageSource;
	}

	public void setStaticMessageSource(StaticMessageSource staticMessageSource) {
		this.staticMessageSource = staticMessageSource;
	}

	public org.springframework.context.MessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(org.springframework.context.MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public static String getKEY_VALUE_SEPARATOR() {
		return KEY_VALUE_SEPARATOR;
	}

	public static void setKEY_VALUE_SEPARATOR(String kEY_VALUE_SEPARATOR) {
		KEY_VALUE_SEPARATOR = kEY_VALUE_SEPARATOR;
	}

	public I18nService getI18nService() {
		return i18nService;
	}

	public DatabaseReloadableMessageSource getDynamicMessageSource() {
		return dynamicMessageSource;
	}

	public StaticMessageSource getStaticMessageSource() {
		return staticMessageSource;
	}

	public static Map<String, String> minus(Map<String, String> m1, Map<String, String> m2) {

		if (m1 != null) {
			if (m2 != null) {

				Map<String, String> m3 = new HashMap<String, String>();

				for (Map.Entry<String, String> staticPropertiesEntry : m1.entrySet()) {
					// System.out.println(staticPropertiesEntry.getKey() + "/" +
					// staticPropertiesEntry.getValue());

					String baseValue = m2.get(staticPropertiesEntry.getKey());
					if (baseValue != null) {

						if (baseValue.equals(staticPropertiesEntry.getValue())) {
							// do not consider
							// System.out.println("trovato uguale " +
							// staticPropertiesEntry.getKey());
						} else {
							// System.out.println("aggiungi: " +
							// staticPropertiesEntry.getKey());
							m3.put(staticPropertiesEntry.getKey(), staticPropertiesEntry.getValue());
						}

					}
				}

				m1 = null;
				m1 = m3;

			}
		}

		return m1;

	}

}
