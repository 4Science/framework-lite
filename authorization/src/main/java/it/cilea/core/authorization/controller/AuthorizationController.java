package it.cilea.core.authorization.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.authorization.AuthorizationConstant;
import it.cilea.core.authorization.context.AuthorizationUserHolder;
import it.cilea.core.authorization.model.impl.UserDetail;
import it.cilea.core.authorization.service.AuthorizationService;
import it.cilea.core.authorization.uitl.AuthorizationUtil;
import it.cilea.core.spring.controller.Spring3CoreController;

@Controller
@RequestMapping("/authorization")
public class AuthorizationController extends Spring3CoreController {

	@Autowired
	private AuthorizationService authorizationService;

	public void setAuthorizationService(AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	@Autowired(required = false)
	@Qualifier("userService")
	private UserDetailsService userService;

	public void setUserService(UserDetailsService userService) {
		this.userService = userService;
	}

	@RequestMapping("/reload.fragment")
	public void reload(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AuthorizationUtil.reload(request.getServletContext(), authorizationService);
		response.getWriter().println("<html>ok</html>");
	}

	@RequestMapping("/reload")
	public ModelAndView reload(HttpServletRequest request) throws IOException {
		AuthorizationUtil.reload(request.getServletContext(), authorizationService);
		saveMessage(request, messageUtil.findMessage("action.configuration.reload"));
		return new ModelAndView("redirect:/");
	}

	@RequestMapping("/getRole/module.json")
	public void getRoleModule(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserDetail userDetail = AuthorizationUserHolder.getUser();
		response.getWriter()
				.println("{\"authorityIdentifier\":\"" + userDetail.getCurrentAuthorityIdentifier() + "\"}");
	}

	@RequestMapping("/changeRole/module.fragment")
	public void changeRoleModule(@RequestParam String authorityIdentifier, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		UserDetail userDetail = AuthorizationUserHolder.getUser();
		userDetail.swithAuthority(authorityIdentifier);
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetail,
				userDetail.getPassword(), userDetail.getAuthorities()));
		response.getWriter().println("<html>ok</html>");
	}

	@RequestMapping("/loginAs/module.fragment")
	public void loginAsModule(@RequestParam String username, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if (!AuthorizationConstant.RESOURCE_MAP.containsKey("/authorization/loginAs/module.fragment")) {
			// non servirebbe, ma così se non si definisce questa risorsa
			// (resourceGa) nessuno può tentare di effettuare il loginas
			response.getWriter().println("<html>no</html>");
			return;
		}
		try {
			String oldUsername = getUser().getUsername();
			UserDetail userDetail = (UserDetail) userService.loadUserByUsername(username);
			// can't process if you are not an ADMIN and try to became ADMIN
			if (userDetail.hasAuthorities("/ADMIN.profile", "ROLE_ADMIN")
					&& !AuthorizationUserHolder.getUser().hasAuthorities("/ADMIN.profile", "ROLE_ADMIN")) {
				response.getWriter().println("<html>no</html>");
				return;
			} else {
				SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetail,
						userDetail.getPassword(), userDetail.getAuthorities()));
				response.getWriter().println("<html>ok</html>");

				request.getSession().setAttribute("login_as_activated", true);
				request.getSession().setAttribute("login_as_old_username", oldUsername);
			}
		} catch (UsernameNotFoundException e) {
			response.getWriter().println("<html>no</html>");
		}
	}

	@RequestMapping("/loginAsEnd/module.fragment")
	public void loginAsEndModule(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!AuthorizationConstant.RESOURCE_MAP.containsKey("/authorization/loginAs/module.fragment")) {
			// non servirebbe, ma così se non si definisce questa risorsa
			// (resourceGa) nessuno può tentare di effettuare il loginas
			response.getWriter().println("<html>no</html>");
			return;
		}
		if (request.getSession().getAttribute("login_as_activated") == null
				|| !(Boolean) request.getSession().getAttribute("login_as_activated")) {
			response.getWriter().println("<html>no</html>");
			return;
		}
		try {
			UserDetail userDetail = (UserDetail) userService
					.loadUserByUsername((String) request.getSession().getAttribute("login_as_old_username"));
			SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetail,
					userDetail.getPassword(), userDetail.getAuthorities()));
			response.getWriter().println("<html>ok</html>");

			request.getSession().removeAttribute("login_as_activated");
			request.getSession().removeAttribute("login_as_old_username");

		} catch (UsernameNotFoundException e) {
			response.getWriter().println("<html>no</html>");
		}
	}

	public AuthorizationService getAuthorizationService() {
		return authorizationService;
	}

	public UserDetailsService getUserService() {
		return userService;
	}

}
