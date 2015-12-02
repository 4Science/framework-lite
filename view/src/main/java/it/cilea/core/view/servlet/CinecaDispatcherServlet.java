package it.cilea.core.view.servlet;

import it.cilea.core.view.ViewConstant;
import it.cilea.core.view.util.ViewUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

public class CinecaDispatcherServlet extends DispatcherServlet {
	@Override
	protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.doDispatch(request, response);
	}

	@Override
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (ViewConstant.viewBuilderMap.containsKey(ViewUtil.correctIdentifier(mv.getViewName())))
			request.setAttribute("cinecaRealViewName", mv.getViewName());
		super.render(mv, request, response);
	}
}
