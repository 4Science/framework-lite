package it.cilea.core.authorization.controller;

import it.cilea.core.authorization.service.AuthorizationService;
import it.cilea.core.spring.controller.Spring3CoreController;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/resourceLink/")
public class ResourceLinkController extends Spring3CoreController {

	@Autowired
	private AuthorizationService authorizationService;

	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@RequestMapping(value = { "onOff.json" })
	public void onOff(@RequestParam String resourceLinkId, HttpServletRequest request, HttpServletResponse response)
			throws BeansException, Exception {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("result", "success");
		map.put("messages", messageUtil.findMessage("action.deleted"));
		if (authorizationService.saveOnOffResourceLink(resourceLinkId))
			map.put("messages", messageUtil.findMessage("resource.enabled"));
		else
			map.put("messages", messageUtil.findMessage("resource.disabled"));

		String json = gson.toJson(map);
		response.getWriter().println(json);
	}

}
