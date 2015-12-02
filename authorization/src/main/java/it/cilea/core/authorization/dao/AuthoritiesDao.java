package it.cilea.core.authorization.dao;

import it.cilea.core.authorization.model.impl.Authorities;
import it.cilea.core.spring.dao.GenericDao;

import java.util.Collection;

public interface AuthoritiesDao extends GenericDao<Authorities, Integer> {

	void deleteAuthorities(Authorities authorities);

	void deleteAuthorities(Collection<Integer> idCollection);

	int count(Integer identityId, Integer authorityId, Integer resourceId, Integer infoId);

	Authorities get(Integer identityId, Integer authorityId, Integer resourceId, Integer infoId);
}
