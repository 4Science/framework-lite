package it.cilea.core.i18n.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.dto.MultipleChoice;
import it.cilea.core.i18n.model.I18n;
import it.cilea.core.i18n.service.I18nService;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
@RequestMapping("/i18n/")
public class I18nFormController extends Spring3CoreController {

	@Autowired(required = false)
	private I18nService i18nService;

	public void setI18nService(I18nService i18nService) {
		this.i18nService = i18nService;
	}

	@ModelAttribute("command")
	public I18n formBacking(HttpServletRequest request) {
		if (StringUtils.isNotBlank(request.getParameter("i18nId")))
			return i18nService.getI18n(Integer.valueOf(request.getParameter("i18nId")));
		return new I18n();
	}

	@RequestMapping(value = { "**/form", "/form", "**/new", "/new" }, method = RequestMethod.GET)
	public ModelAndView processGet(@ModelAttribute("command") I18n i18n, HttpServletRequest request) {

		if ("y".equals(request.getParameter("new"))) {
			i18n.setKey(request.getParameter("i18n.key"));
			i18n.setContext(request.getParameter("i18n.context"));
		}

		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewName(), "command", i18n);
	}

	@RequestMapping(value = { "**/form", "/form", "**/new", "/new" }, method = RequestMethod.POST)
	public ModelAndView processPost(@Valid @ModelAttribute("command") I18n command, BindingResult result,
			HttpServletRequest request) throws Exception {

		/*
		 * The null i18n dinamic value is a nonsense. it's ignored like the key
		 * doesnt exists in messagesource A null / black value must be replaced
		 * by a '' in the database key value by default. Its not user choice
		 */

		/*
		 * nota: il modello @ElementCollection ha qualche problema di
		 * sincronizzazione ES: ... in questo punto se stampo il valore della
		 * etichetta di default i18n vedo il nuovo valore di command, se stampo
		 * il valore inglese vedo quello sul db preesistente ( con
		 * command.getDataSet(); ) mentre vedo quello nuovo con
		 * command.getStringMap esistono comportamenti non limpidi anche sul
		 * delete a cascata.
		 */

		if (request.getParameter("cancel") != null)
			return new ModelAndView(getControllerLogic(request.getServletPath()).getViewUndo());

		executeXmlValidation(request, command, (Errors) result,
				getControllerLogic(request.getServletPath()).getValidatorList());

		if (result.hasErrors()) {
			genericReferenceDataNoCheckFormSubmission(command, result, request);
			return processGet(command, request);
		}

		boolean firstInsert = command.getId() == null;

		i18nService.saveOrUpdate(command);
		if (command.getValue() == null) {
			command.setValue(" ");
		}
		Map<String, String> map = command.getStringMap();
		for (Map.Entry<String, String> entry : map.entrySet()) {

			if (entry.getKey().startsWith("value_")) {
				if (entry.getValue() == null) {
					command.getStringMap().put(entry.getKey(), " ");
				}
			}
		}
		i18nService.saveOrUpdate(command);

		saveMessage(request, messageUtil.findMessage("action." + (firstInsert ? "created" : "updated")));
		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewSuccess(), "i18nId",
				command.getId());
	}

	@ModelAttribute()
	public void genericReferenceData(@Valid I18n command, BindingResult result, HttpServletRequest request)
			throws Exception {
		if (!isFormSubmission(request) && StringUtils
				.isNotBlank(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName())) {
			List<MultipleChoice> list = (List<MultipleChoice>) context
					.getBean(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName());
			super.genericReferenceData(command, request, list);
		}
	}

	public void genericReferenceDataNoCheckFormSubmission(@Valid I18n command, BindingResult result,
			HttpServletRequest request) throws Exception {
		if (StringUtils.isNotBlank(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName())) {
			List<MultipleChoice> list = (List<MultipleChoice>) context
					.getBean(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName());
			super.genericReferenceData(command, request, list);
		}
	}

	public I18nService getI18nService() {
		return i18nService;
	}

}
