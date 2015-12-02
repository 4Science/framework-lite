package it.cilea.core.i18n.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.dto.MultipleChoice;
import it.cilea.core.i18n.conf.DatabaseReloadableMessageSource;
import it.cilea.core.i18n.model.I18n;
import it.cilea.core.i18n.model.I18nImport;
import it.cilea.core.i18n.service.I18nService;
import it.cilea.core.i18n.util.I18nUtil;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
public class I18nImportController extends Spring3CoreController {
	@Autowired(required = false)
	private I18nService i18nService;
	@Autowired(required = false)
	private DatabaseReloadableMessageSource dynamicMessageSource;
	@Autowired(required = false)
	private MessageSource staticMessageSource;

	@RequestMapping(value = { "/i18n/import" }, method = RequestMethod.GET)
	public ModelAndView loadGet(@ModelAttribute("command") I18nImport command, HttpServletRequest request) {
		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewName(), "command", command);
	}

	private int threshold = 35;

	@RequestMapping(value = { "/i18n/import" }, method = RequestMethod.POST)
	public ModelAndView loadPost(@Valid @ModelAttribute("command") I18nImport command, BindingResult result,
			HttpServletRequest request) throws Exception {

		log.debug("\n==== import i18n =====================================");
		log.debug(" ip: " + request.getRemoteAddr());
		log.debug(" user: " + request.getRemoteUser());

		if (request.getParameter("cancel") != null)
			return new ModelAndView(getControllerLogic(request.getServletPath()).getViewUndo());

		executeXmlValidation(request, command, (Errors) result,
				getControllerLogic(request.getServletPath()).getValidatorList());

		if (result.hasErrors()) {
			genericReferenceDataNoCheckFormSubmission(command, result, request);
			return processGet(command, request);
		}

		// Recupero il file
		MultipartFile mf = command.getMultipartFile();

		log.debug(" mf.getOriginalFilename() " + mf.getOriginalFilename());

		if (mf.getOriginalFilename().contains(".txt") || mf.getOriginalFilename().contains(".properties")) {
		} else {
			saveError(request, messageUtil.findMessage("error.i18n.import.fileExt"));
			genericReferenceDataNoCheckFormSubmission(command, result, request);
			return processGet(command, request);
		}

		log.debug(" mf.getSize() " + mf.getSize());

		boolean updateModel = false;
		try {
			Reader reader = null;
			if ("auto".equals(command.getCharset())) {
				CharsetDetector charsetDetector = new CharsetDetector();
				charsetDetector.setText(I18nUtil.getByteFromMultipartFile(mf));
				CharsetMatch match = charsetDetector.detect();
				if (!(match != null && match.getConfidence() > threshold)) {
					saveError(request, messageUtil.findMessage("error.i18n.import.charSet"));
					genericReferenceDataNoCheckFormSubmission(command, result, request);
					return processGet(command, request);
				}
				reader = match.getReader();
			} else
				reader = new BufferedReader(new InputStreamReader(mf.getInputStream(), command.getCharset()));

			ResourceBundle bundle = new PropertyResourceBundle(reader);
			Properties props = new Properties();
			Enumeration<String> enumeration = bundle.getKeys();
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
				props.put(key, bundle.getString(key));
			}

			// load a properties file
			// props.load(mf.getInputStream());

			// ricava etichette da byte[] data (solo quelle che superano la
			// validazione)

			log.debug("\n\n----- inserimento -------\n\n");

			for (Object key : props.keySet()) {

				String i18nKey = (String) key;
				Object param[] = { i18nKey };

				String value = props.getProperty((String) key);
				I18n i18n = null;
				updateModel = false;

				String loginfo = "\n\n ----- 18nKey : " + i18nKey + " value: " + value;

				log.debug(loginfo + ((value == null) ? "value==null" : "value not null: '" + value + "'"));

				try {
					// Recupero dal service l'i18n

					log.debug("getI18nExactContext(" + i18nKey + ", " + command.getContext() + ")");
					i18n = i18nService.getI18nExactContext(i18nKey, command.getContext());

					if (i18n != null) {

						// Se la lingua non è definita, significa che sto
						// aggiungendo il language di default
						if (StringUtils.isBlank(command.getLanguage())) {

							// Se l'override è abilitato sovrascrivo il valore
							if (command.getOverrideExisting() != null && command.getOverrideExisting()) {

								// esiete la chiave su db - lingua di default -
								// sovrascrivi valore
								log.debug("esiete la chiave su db - lingua di default - sovrascrivi valore ");
								i18n.setValue(value);
								updateModel = true;

							} else {
								// Avviso che la property già esiste
								log.debug("esiete la chiave su db - lingua di default - non sovreascrivere ");
								// saveError(request,
								// messageUtil.findMessage("i18n.propertyExist",
								// param));
								saveError(request, messageUtil.findMessage("i18n.propertyExist").replace("{0}",
										(String) param[0]));

							}
						} else {

							// Sto aggiungendo un language tipo ENG
							// Se l'override è abilitato
							if (command.getOverrideExisting() != null && command.getOverrideExisting()) {

								log.debug("esiete la chiave su db - lingua specificata - sovrascrivi ");

								i18n.getStringMap().put("value_" + command.getLanguage(), value);
								updateModel = true;

							} else {

								// Controllo che la key per il language
								// specificato non esista già
								if (!i18n.getStringMap().containsKey("value_" + command.getLanguage())) {

									log.debug(
											"esiete la chiave su db - lingua specificata - valore per la lingua non presente - non sovrascrivere ");

									i18n.getStringMap().put("value_" + command.getLanguage(), value);
									updateModel = true;

									String new_default_old = i18nService.searchI18nData(i18n.getId().toString(),
											"new_default");
									log.debug("default precedentemente salvato su db: " + new_default_old);
								} else {
									// Se esiste e l'override e disabilitato
									// Avviso che la property già esiste

									log.debug(
											"esiete la chiave su db - lingua specificata - valore per la lingua presente - non sovrascrivere ");

									// saveError(request,
									// messageUtil.findMessage("i18n.propertyExist",
									// param));
									saveError(request, messageUtil.findMessage("i18n.propertyExist").replace("{0}",
											(String) param[0]));
								}
							}
						}
					} else {
						// Sul db non esiste nessuna property per questo
						// contesto, allora ne creo una nuova

						log.debug("non esiete la chiave su db ");

						String languageFileValue = null;

						i18n = new I18n();
						i18n.setKey(i18nKey);

						Locale locale = null;
						if (!StringUtils.isBlank(command.getLanguage())) {
							locale = new Locale(command.getLanguage());
							log.debug("valore in lingua presente su file: " + languageFileValue);
						}

						if (!StringUtils.isBlank(command.getContext())) {
							i18n.setContext(command.getContext());
							log.debug(" setContext: " + command.getContext());
						}

						boolean procedi = true;
						if (value == null) {
							procedi = false;
						} else {
							if (!StringUtils.isBlank(command.getLanguage())) {
								if (value.equals(languageFileValue)) {
									procedi = false;
								}
							} else {
							}
						}

						if (procedi) {

							log.debug(
									"non esiete la chiave su db, ha un valore non nullo e differente da quello di default del file per la lingua specificata ");

							// Controllo se il value è per il language di
							// default
							if (StringUtils.isBlank(command.getLanguage())) {

								log.debug(
										"linguaggio non specificato - non esiete la chiave su db, ha un valore non nullo e differente da quello di default del file per la lingua specificata ");

								i18n.setValue(value);
								updateModel = true;

							} else {
								// Sto inserendo in value per un language tipo:
								// ENG ma manca anche il valore per il language
								// di default
								// prendo il valore dal file di configurazione

								// String defaultFieldValue =
								// messageUtil.findMessage(i18nKey);
								// if (!defaultFieldValue.startsWith("???")) {

								log.debug(
										"linguaggio specificato - non esiete la chiave su db, ha un valore non nullo e differente da quello di default del file per la lingua specificata ");

							}
						}

						else {

							log.debug(
									"non esiete la chiave su db, ha un valore nullo o uguale a quello presente su file per la lingua specificata ");

						}

					}
				} catch (Exception e) {
					// e.printStackTrace();
				}

				if (updateModel) {

					log.debug("updateModel : " + updateModel);

					Errors errors = new BindException(i18n, "command");
					executeXmlValidation(request, i18n, errors,
							getControllerLogic("/i18n/form.htm").getValidatorList());

					if (errors.hasErrors()) {
						for (ObjectError err : errors.getAllErrors()) {
							saveError(request,
									i18n.getKey() + ": " + messageUtil.findMessage(err.getCode(), err.getArguments()));
						}
					} else {
						i18nService.saveOrUpdate(i18n);
					}
				} else {
					log.debug("updateModel : " + updateModel + " non aggiornare ");
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			saveError(request, messageUtil.findMessage("i18n.inputFileFormatError"));
			genericReferenceDataNoCheckFormSubmission(command, result, request);
			return processGet(command, request);
		}

		log.debug("======================================================\n");

		saveMessage(request, messageUtil.findMessage("action.i18n.import.success"));
		return new ModelAndView("redirect:/i18n/import.htm");
	}

	public void setI18nService(I18nService i18nService) {
		this.i18nService = i18nService;
	}

	@ModelAttribute()
	public void genericReferenceData(@Valid I18nImport command, BindingResult result, HttpServletRequest request)
			throws Exception {
		if (!isFormSubmission(request) && StringUtils
				.isNotBlank(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName())) {
			List<MultipleChoice> list = (List<MultipleChoice>) context
					.getBean(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName());
			super.genericReferenceData(command, request, list);
		}
	}

	public void genericReferenceDataNoCheckFormSubmission(@Valid I18nImport command, BindingResult result,
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

	public void setStaticMessageSource(MessageSource staticMessageSource) {
		this.staticMessageSource = staticMessageSource;
	}

	public I18nService getI18nService() {
		return i18nService;
	}

	public DatabaseReloadableMessageSource getDynamicMessageSource() {
		return dynamicMessageSource;
	}

	public MessageSource getStaticMessageSource() {
		return staticMessageSource;
	}

}
