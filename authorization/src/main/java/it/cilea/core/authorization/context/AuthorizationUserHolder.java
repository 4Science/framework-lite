package it.cilea.core.authorization.context;

import it.cilea.core.authorization.model.impl.UserDetail;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthorizationUserHolder {
	public static UserDetail getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails)
			return ((UserDetail) authentication.getPrincipal());
		return null;
	}
}
