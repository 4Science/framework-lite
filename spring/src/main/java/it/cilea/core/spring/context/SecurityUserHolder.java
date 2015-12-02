package it.cilea.core.spring.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUserHolder {
	public static UserDetails getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails)
			return ((UserDetails) authentication.getPrincipal());
		return null;
	}

	public static String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails)
			username = ((UserDetails) authentication.getPrincipal()).getUsername();
		else if (authentication != null && authentication.getPrincipal() instanceof String)
			username = (String) authentication.getPrincipal();
		return username;
	}
}
