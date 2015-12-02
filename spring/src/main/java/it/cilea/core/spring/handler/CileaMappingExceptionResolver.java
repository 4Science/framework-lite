package it.cilea.core.spring.handler;

import it.cilea.core.CoreConstant;
import it.cilea.core.spring.context.SecurityUserHolder;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class CileaMappingExceptionResolver extends SimpleMappingExceptionResolver {
	@Override
	public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		return super.doResolveException(request, response, handler, ex);
	}

	@Override
	protected String buildLogMessage(Exception ex, HttpServletRequest request) {

		String codice = "ex_" + CoreConstant.MESSAGE_PROPERTIES + "_" + CoreConstant.MODULE_NAME + "_"
				+ Calendar.getInstance().getTimeInMillis();
		UserDetails user = SecurityUserHolder.getUser();
		if (user != null)
			logger.error("Errore " + codice + " generato da " + user.getUsername());
		else
			logger.error("Errore " + codice + " generato da utente sconosciuto");
		ex.printStackTrace();
		request.setAttribute("cileaErrorCode", codice);
		request.setAttribute("cileaErrorStack", ex.toString());
		return super.buildLogMessage(ex, request);
	}

	@Override
	protected void logException(Exception ex, HttpServletRequest request) {

		buildLogMessage(ex, request);

	}

}
