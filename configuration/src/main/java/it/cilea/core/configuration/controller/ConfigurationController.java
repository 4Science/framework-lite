package it.cilea.core.configuration.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.configuration.service.ConfigurationService;
import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
public class ConfigurationController extends Spring3CoreController {

	@Autowired(required = false)
	private ConfigurationService configurationService;

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	@RequestMapping(value = { "/configuration/reload" })
	public ModelAndView reload(HttpServletRequest request)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ConfigurationUtil.reload(request.getServletContext(), configurationService);
		saveMessage(request, messageUtil.findMessage("action.configuration.reload"));
		return new ModelAndView("redirect:/?CLEAR");
	}

	@RequestMapping("/configuration/reload.fragment")
	public void reload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ConfigurationUtil.reload(request.getServletContext(), configurationService);
		response.getWriter().println("<html>ok</html>");
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

}
