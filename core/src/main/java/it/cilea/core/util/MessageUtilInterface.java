package it.cilea.core.util;

import java.util.Locale;

public interface MessageUtilInterface {

	String findMessage(String messageKey, String language);

	String findMessage(String messageKey, Locale locale);

	String findMessage(String messageKey);
	
	Boolean hasMessage(String messageKey);

	String findMessage(String messageKey, Object[] params);

	String findMessage(String messageKey, Object[] params, Locale locale);
	
	Object getMessageSource();
}
