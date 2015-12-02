package it.cilea.core.authorization.service;

import it.cilea.core.authorization.AuthorizationConstant;
import it.cilea.core.authorization.model.impl.Resource;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.web.FilterInvocation;

public class CileaAccessDecisionManager implements AccessDecisionManager, ApplicationContextAware {
	@Autowired
	private ApplicationContext applicationContext;


	@SuppressWarnings("deprecation")
	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {

		if (authentication.getAuthorities().contains(new GrantedAuthorityImpl("ROLE_ANONYMOUS"))) {
			throw new AccessDeniedException("CileaAccessDecisionManager.accessDenied");
		}
		if (object instanceof FilterInvocation) {
			FilterInvocation filterInvocation = (FilterInvocation) object;
			String requestUrl = null;
			// String fullRequestUrl = null;
			requestUrl = StringUtils.substringBefore(filterInvocation.getRequestUrl(), "?");

			// fullRequestUrl = filterInvocation.getFullRequestUrl();
			if (AuthorizationConstant.RESOURCE_MAP.containsKey(requestUrl)) {

				String parameters = null;
				if (filterInvocation.getRequest().getClass().toString().contains("DummyRequest")) {
					parameters = StringUtils.substringAfter(filterInvocation.getRequestUrl(), "?");
				} else {
					parameters = "";
					for (Object obj : filterInvocation.getHttpRequest().getParameterMap().keySet()) {
						String[] array = filterInvocation.getHttpRequest().getParameterValues(obj.toString());
						for (String key : array) {
							parameters += obj.toString() + "=" + key + "&";
						}
					}
					parameters = StringUtils.removeEnd(parameters, "&");
				}

				Resource resource = AuthorizationConstant.RESOURCE_MAP.get(requestUrl);
				if (!resource.getAllowed())
					throw new AccessDeniedException("CileaAccessDecisionManager.accessDenied");
				DataAccessLogic dataAccessLogic = applicationContext.getBean(
						resource.getDataAccessLogicBeanIdentifier(), DataAccessLogic.class);
				if (!dataAccessLogic.check(requestUrl, parameters)) {
					throw new AccessDeniedException("CileaAccessDecisionManager.accessDenied");
				}
			}
		}
	}

	@Override
	public boolean supports(ConfigAttribute configattribute) {

		return true;
	}

	@Override
	public boolean supports(Class<?> arg0) {

		return true;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}


}
