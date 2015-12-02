package it.cilea.core.i18n.conf;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import it.cilea.core.spring.context.SecurityUserHolder;

public class CileaMessageSource implements MessageSource {
	final boolean forceAdminView = false;

	@Autowired
	private MessageSource staticMessageSource;

	@Autowired
	private MessageSource dynamicMessageSource;

	public static String NO_ADMINISTRATIVE = "18n-noadministrative.";

	private Boolean administrative = false;

	private Set<String> administraliveUser = new HashSet<String>();

	public void setStaticMessageSource(MessageSource staticMessageSource) {
		this.staticMessageSource = staticMessageSource;
	}

	public void setDynamicMessageSource(MessageSource dynamicMessageSource) {
		this.dynamicMessageSource = dynamicMessageSource;
	}

	public CileaMessageSource() {

	}

	public String getMessage(String s, Object[] aobj, String s1, Locale locale) {

		if (forceAdminView)
			return s;

		if (administrative) {
			String user = SecurityUserHolder.getUsername();
			// Dopo aver valutato la prima condizione se s null voglio che
			// entri nell if senza valuter  lo startsWith
			if (administraliveUser.contains(user) && (s == null || !s.startsWith(NO_ADMINISTRATIVE)))
				return s;
		}
		try {
			// never dynamicMessageSource with default message
			return dynamicMessageSource.getMessage(s, aobj, locale);
		} catch (NoSuchMessageException e) {
		}

		return staticMessageSource.getMessage(s, aobj, s1, locale);
	}

	public String getMessage(String s, Object[] aobj, Locale locale) throws NoSuchMessageException {

		if (forceAdminView)
			return s;

		if (administrative) {
			String user = SecurityUserHolder.getUsername();

			if (administraliveUser.contains(user) && (s == null || !s.startsWith(NO_ADMINISTRATIVE)))
				return s;
		}
		try {
			if (dynamicMessageSource.getMessage(s, aobj, locale) != null)
				return dynamicMessageSource.getMessage(s, aobj, locale);
		} catch (NoSuchMessageException e) {

		}
		return staticMessageSource.getMessage(s, aobj, locale);
	}

	public String getMessage(MessageSourceResolvable messagesourceresolvable, Locale locale)
			throws NoSuchMessageException {

		String key = null;

		// attenzione, exception au:
		// prima chiama il getMessage poi lo mette in un oggetto ObjectError con
		// default message
		// e lo rimanda su getMessage (senza una chiave specificata, solo con un
		// default message)
		if (messagesourceresolvable instanceof org.springframework.validation.ObjectError) {

			org.springframework.validation.ObjectError oe = (org.springframework.validation.ObjectError) messagesourceresolvable;

			key = oe.getDefaultMessage();

		} else {

			String[] s = messagesourceresolvable.getCodes();
			key = s[0];

		}

		if (forceAdminView)
			return key;

		if (administrative) {

			String user = SecurityUserHolder.getUsername();

			if (administraliveUser.contains(user) && (key == null || !((String) key).startsWith(NO_ADMINISTRATIVE)))
				return key;
		}
		try {

			DefaultMessageSourceResolvable newd = new DefaultMessageSourceResolvable(messagesourceresolvable.getCodes(),
					messagesourceresolvable.getArguments());

			if (dynamicMessageSource.getMessage(newd, locale) != null)
				return dynamicMessageSource.getMessage(messagesourceresolvable, locale);
		} catch (NoSuchMessageException e) {

		}
		return staticMessageSource.getMessage(messagesourceresolvable, locale);
	}

	public void setAdministrative(Boolean administrative) {
		this.administrative = administrative;
	}

	public Set<String> getAdministraliveUser() {
		return administraliveUser;
	}

	public MessageSource getDynamicMessageSource() {
		return dynamicMessageSource;
	}

	public boolean isCurrentUserAdministrative() {
		if (administrative) {
			String user = SecurityUserHolder.getUsername();
			if (administraliveUser.contains(user)) {
				return true;
			}

		}
		return false;
	}

}
