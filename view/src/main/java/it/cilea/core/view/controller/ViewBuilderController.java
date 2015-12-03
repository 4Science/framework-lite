package it.cilea.core.view.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.spring.controller.Spring3CoreController;
import it.cilea.core.view.service.ViewService;
import it.cilea.core.view.util.ViewUtil;

@Controller
public class ViewBuilderController extends Spring3CoreController {

	@Autowired
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Autowired
	private ViewService viewService;

	public void setViewService(ViewService viewService) {
		this.viewService = viewService;
	}

	@RequestMapping(value = { "/view/reload.htm" })
	public ModelAndView Reload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ViewUtil.reload(viewService);
		saveMessage(request, messageUtil.findMessage("action.view.reload"));
		return new ModelAndView("redirect:/?CLEAR");
	}

	@RequestMapping("/view/reload.fragment")
	public void reload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ViewUtil.reload(viewService);
		response.getWriter().println("<html>ok</html>");
	}

}
