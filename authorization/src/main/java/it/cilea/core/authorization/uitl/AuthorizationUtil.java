package it.cilea.core.authorization.uitl;

import it.cilea.core.authorization.AuthorizationConstant;
import it.cilea.core.authorization.model.impl.Authority;
import it.cilea.core.authorization.model.impl.Resource;
import it.cilea.core.authorization.model.impl.ResourceLink;
import it.cilea.core.authorization.service.AuthorizationService;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationUtil {
	private static final Logger log = LoggerFactory.getLogger(AuthorizationUtil.class);

	public static void reload(ServletContext servletContext, AuthorizationService authorizationService) {
		log.info("Resource reload");
		Map<String, Authority> authorityMap = new HashMap<String, Authority>();
		for (Authority authority : authorizationService.getAuthorityEagerList()) {
			authorityMap.put(authority.getIdentifier(), authority);
		}
		AuthorizationConstant.loadAuthorityMap(authorityMap);
		Map<Integer, Authority> authorityIdMap = new HashMap<Integer, Authority>();
		for (Entry<String, Authority> entry : authorityMap.entrySet()) {
			authorityIdMap.put(entry.getValue().getId(), entry.getValue());
		}
		AuthorizationConstant.loadAuthorityIdMap(authorityIdMap);

		Map<String, Resource> resourceMap = new HashMap<String, Resource>();
		for (Resource resource : authorizationService.getResourceList()) {
			resourceMap.put(resource.getIdentifier(), resource);
			resource.setChildResourceLinkSet(new LinkedHashSet<ResourceLink>());
			resource.setParentResourceLinkSet(new LinkedHashSet<ResourceLink>());
		}
		Map<Integer, Resource> resourceIdMap = new HashMap<Integer, Resource>();
		for (Entry<String, Resource> entry : resourceMap.entrySet()) {
			resourceIdMap.put(entry.getValue().getId(), entry.getValue());
		}
		for (ResourceLink resourceLink : authorizationService.getResourceLinkList()) {
			Resource parent = resourceIdMap.get(resourceLink.getParentId());
			Resource child = resourceIdMap.get(resourceLink.getChildId());
			resourceLink.setChild(child);
			resourceLink.setParent(parent);
			parent.getChildResourceLinkSet().add(resourceLink);
			child.getParentResourceLinkSet().add(resourceLink);
		}
		AuthorizationConstant.loadResourceMap(resourceMap);
		AuthorizationConstant.loadResourceIdMap(resourceIdMap);
		servletContext.setAttribute("resourceMap", resourceMap);
	}

}
