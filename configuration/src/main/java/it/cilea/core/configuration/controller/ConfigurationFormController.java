package it.cilea.core.configuration.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.configuration.model.Configuration;
import it.cilea.core.configuration.service.ConfigurationService;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
@RequestMapping("/configuration/")
public class ConfigurationFormController extends Spring3CoreController {

	public @ModelAttribute("command") Configuration formBacking(HttpServletRequest request) throws Exception {
		return (Configuration) super.formBacking(Configuration.class, configurationService, request);
	}

	@RequestMapping(value = { "**/form", "**/new", "**/detail" }, method = RequestMethod.GET)
	public ModelAndView processGet(@ModelAttribute("command") Configuration configuration, HttpServletRequest request) {
		return super.processGet(configuration, request);
	}

	@RequestMapping(value = { "**/form", "**/new" }, method = RequestMethod.POST)
	public ModelAndView processPost(@Valid @ModelAttribute("command") Configuration command, BindingResult result,
			SessionStatus status, HttpServletRequest request) throws Exception {
		ModelAndView mav = super.processPostCheckCancelAndValidation(command, result, request);
		return (mav != null) ? mav : processPostSuccess(command, configurationService, request);
	}

	@Autowired(required = false)
	private ConfigurationService configurationService;

	public void setGaService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}
