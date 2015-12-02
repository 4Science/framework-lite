package it.cilea.core.authorization.dao;

import it.cilea.core.authorization.model.impl.ResourceLink;
import it.cilea.core.authorization.model.impl.ResourceLink.ResourceLinkId;
import it.cilea.core.spring.dao.GenericDao;

import java.util.List;

public interface ResourceLinkDao extends GenericDao<ResourceLink, ResourceLinkId> {
	public List<Object[]> getAllowedResourceLinkIdentifier();
}

