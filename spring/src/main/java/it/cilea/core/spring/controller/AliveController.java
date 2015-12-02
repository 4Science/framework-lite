package it.cilea.core.spring.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonObject;

@Controller
public class AliveController extends Spring3CoreController {

	@RequestMapping("/alive.json")
	public void handleJsonDifferences(HttpServletRequest request, HttpServletResponse response) throws Exception {
		JsonObject json = new JsonObject();
		json.addProperty("message", "ok");
		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
