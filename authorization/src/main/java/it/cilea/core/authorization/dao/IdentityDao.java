package it.cilea.core.authorization.dao;

import java.util.List;
import java.util.Set;

import it.cilea.core.authorization.model.impl.Authority;
import it.cilea.core.authorization.model.impl.Identity;
import it.cilea.core.authorization.model.impl.Resource;
import it.cilea.core.spring.dao.GenericDao;

public interface IdentityDao extends GenericDao<Identity, Integer> {
	List<Identity> getIdentityList(Set<Resource> resourceSet, Authority authority);
}
