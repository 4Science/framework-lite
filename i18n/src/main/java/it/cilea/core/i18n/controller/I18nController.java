package it.cilea.core.i18n.controller;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.i18n.conf.CileaMessageSource;
import it.cilea.core.i18n.conf.DatabaseReloadableMessageSource;
import it.cilea.core.i18n.model.I18n;
import it.cilea.core.i18n.service.I18nService;
import it.cilea.core.i18n.util.I18nUtil;
import it.cilea.core.spring.controller.Spring3CoreController;
import it.cilea.core.util.HttpUtil;
import it.cilea.core.util.MessageUtilConstant;

@Controller
@RequestMapping({ "/i18n/", "/frame/i18n/" })
public class I18nController extends Spring3CoreController {

	private static final Logger log = LoggerFactory.getLogger(I18nController.class);

	@Autowired(required = false)
	private I18nService i18nService;
	@Autowired(required = false)
	private DatabaseReloadableMessageSource dynamicMessageSource;

	public void setDynamicMessageSource(DatabaseReloadableMessageSource dynamicMessageSource) {
		this.dynamicMessageSource = dynamicMessageSource;
	}

	public void setI18nService(I18nService i18nService) {
		this.i18nService = i18nService;
	}

	@RequestMapping(value = { "/i18n/reload" })
	public ModelAndView reload(HttpServletRequest request) throws Exception {
		I18nUtil.reload(dynamicMessageSource);
		saveMessage(request, messageUtil.findMessage("action.i18n.reload"));
		return new ModelAndView("redirect:/?CLEAR");
	}

	@RequestMapping("/i18n/reload.fragment")
	public void reload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		I18nUtil.reload(dynamicMessageSource);
		response.getWriter().println("<html>ok</html>");
	}

	@RequestMapping(value = { "**/detail.{outputType:htm|json|xml}", "/detail.{outputType:htm|json|xml}" })
	public ModelAndView detail(@RequestParam Integer i18nId, HttpServletRequest request, HttpServletResponse response,
			@PathVariable String outputType) throws Exception {

		I18n i18n = i18nService.getI18n(i18nId);

		if ("htm".equals(outputType))
			return new ModelAndView(getControllerLogic(request.getServletPath()).getViewName(), "command", i18n);
		else {
			response.setContentType(HttpUtil.getMimeTypeHeader(outputType));
			Writer output = jaxbService.marshal(i18n, outputType);
			response.getWriter().print(output);
			return null;
		}
	}

	@RequestMapping(value = { "delete.htm" })
	public ModelAndView delete(@RequestParam String i18nId, HttpServletRequest request) {
		// i18nService.deleteI18n(i18nId);
		i18nService.deleteObjectI18n(i18nId);

		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewSuccess());
	}

	@RequestMapping(value = { "administrative/start.fragment" })
	public void administrativeStart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("I18n administrative start " + getUser().getUsername());

		CileaMessageSource source = (CileaMessageSource) MessageUtilConstant.messageUtil.getMessageSource();

		source.setAdministrative(true);
		source.getAdministraliveUser().add(getUser().getUsername());
		String username = "";
		for (String string : source.getAdministraliveUser())
			username += string + ", ";
		username = StringUtils.removeEnd(username, ", ");
		saveMessage(request,
				"administrative view started. Remember to stop it. Users connected in administrative view: "
						+ username);
		response.getWriter().println("<html>ok</html>");
	}

	@RequestMapping(value = { "administrative/stop.fragment" })
	public void administrativeStop(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("I18n administrative stop " + getUser().getUsername());

		CileaMessageSource source = (CileaMessageSource) MessageUtilConstant.messageUtil.getMessageSource();
		source.getAdministraliveUser().remove(getUser().getUsername());
		if (source.getAdministraliveUser().size() == 0)
			source.setAdministrative(false);
		String username = "";
		for (String string : source.getAdministraliveUser())
			username += string + ", ";
		username = StringUtils.removeEnd(username, ", ");
		if (StringUtils.isBlank(username))
			saveMessage(request, "administrative view terminated. No more users connected in administrative view");
		else
			saveMessage(request, "administrative view terminated. Users connected in administrative view: " + username);
		response.getWriter().println("<html>ok</html>");
	}

	public static Logger getLog() {
		return log;
	}

	public I18nService getI18nService() {
		return i18nService;
	}

	public DatabaseReloadableMessageSource getDynamicMessageSource() {
		return dynamicMessageSource;
	}

}
