package it.cilea.core.menu.rhino.iterfaces.implementation;

import it.cilea.core.menu.rhino.iterfaces.AddJsInfoInterface;
import it.cilea.core.spring.context.SecurityUserHolder;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;

// questa classe va iniettata nel dashboardcontroller

public class AddJsInfo implements AddJsInfoInterface {

	public String getJavascriptCode(HttpServletRequest request) {
		UserDetails user = null;
		user= getUser();

		Enumeration en = request.getParameterNames();

		String js = "";
		js = "var parametri=new Object;\n";

		String parametro = "", valore = "";
		while (en.hasMoreElements()) {
			parametro = en.nextElement().toString();

			valore = getParamValue(parametro, request);
			if (!valore.equals("''") && !valore.equals(""))
				js += "parametri." + parametro + "=" + valore + ";\n";
		}

		// crea un oggetto javascript con l'utente
		if (user != null) {
			js += "var user=new Object;";
			js += "\n user.username='" + user.getUsername() + "';\n";

		}

		return js;
	}

	private static String getParamValue(String stringa,
			HttpServletRequest request) {

		String values = "";

		String[] paramValues = null;

		paramValues = request.getParameterValues(stringa);
		if (paramValues != null) {
			for (String value : paramValues) {
				try {
					Integer.valueOf(value);
					values += "," + value;
				} catch (NumberFormatException e) {
					values += ",'" + value + "'";
				}
				values = values.substring(1);
			}
		}

		return values;

	}
	
	public UserDetails getUser() {
		UserDetails user = SecurityUserHolder.getUser();
		return user;
	}

}
