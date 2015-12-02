package it.cilea.core.authorization;

import it.cilea.core.authorization.model.impl.Authority;
import it.cilea.core.authorization.model.impl.Resource;

import java.util.Map;

public class AuthorizationConstant {
	public static Map<String, Authority> AUTHORITY_MAP;
	public static Map<Integer, Authority> AUTHORITY_ID_MAP;
	public static Map<String, Resource> RESOURCE_MAP;
	public static Map<Integer, Resource> RESOURCE_ID_MAP;

	public static void loadAuthorityMap(Map<String, Authority> authorityMap) {
		AuthorizationConstant.AUTHORITY_MAP = authorityMap;
	}

	public static void loadAuthorityIdMap(Map<Integer, Authority> authorityIdMap) {
		AuthorizationConstant.AUTHORITY_ID_MAP = authorityIdMap;
	}

	public static void loadResourceMap(Map<String, Resource> resourceMap) {
		AuthorizationConstant.RESOURCE_MAP = resourceMap;
	}

	public static void loadResourceIdMap(Map<Integer, Resource> resourceIdMap) {
		AuthorizationConstant.RESOURCE_ID_MAP = resourceIdMap;
	}
}
