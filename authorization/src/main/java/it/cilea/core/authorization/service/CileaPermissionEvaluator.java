package it.cilea.core.authorization.service;

import it.cilea.core.authorization.model.impl.UserDetail;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

public class CileaPermissionEvaluator implements PermissionEvaluator {

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		UserDetail userDetail = (UserDetail) authentication.getPrincipal();
		// return userDetail.hasAuthorities(targetDomainObject.toString(),
		// permission.toString());
		if ("currentRole".equals(permission.toString())) {
			if (userDetail.hasAuthorities(targetDomainObject.toString()))
				return true;
			else
				return false;
		}
		for (String role : StringUtils.split(permission.toString(), ","))
			if (userDetail.hasAuthorities(targetDomainObject.toString(), role))
				return true;
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
			Object permission) {
		return false;
	}

}
