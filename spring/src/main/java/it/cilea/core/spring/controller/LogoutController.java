package it.cilea.core.spring.controller;

import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

public class LogoutController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		request.getSession().invalidate();
		Cookie terminate = new Cookie(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null);
		terminate.setMaxAge(0);
		response.addCookie(terminate);
		SecurityContextHolder.clearContext();

		Properties props = new Properties();
		ClassLoader cl = this.getClass().getClassLoader();
		InputStream is = cl.getResourceAsStream("cas.properties");
		props.load(is);
		String casUrl = props.getProperty("cas_url");

		return new ModelAndView(new RedirectView(casUrl + "logout", true), null);

	}

}
