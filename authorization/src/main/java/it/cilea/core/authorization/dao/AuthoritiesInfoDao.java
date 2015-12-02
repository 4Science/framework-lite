package it.cilea.core.authorization.dao;

import it.cilea.core.authorization.model.impl.AuthoritiesInfo;
import it.cilea.core.spring.dao.GenericDao;

import java.util.Collection;

public interface AuthoritiesInfoDao extends GenericDao<AuthoritiesInfo, Integer> {

	void deleteAuthoritiesInfo(AuthoritiesInfo authoritiesInfo);

	void deleteAuthoritiesInfo(Collection<Integer> idCollection);

}
