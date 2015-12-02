package it.cilea.core.authorization.service;

import it.cilea.core.authorization.context.AuthorizationUserHolder;
import it.cilea.core.authorization.dao.AuthorityDao;
import it.cilea.core.authorization.dao.CoreUserDao;
import it.cilea.core.authorization.dao.IdentityDao;
import it.cilea.core.authorization.dao.ResourceDao;
import it.cilea.core.authorization.dao.ResourceLinkDao;
import it.cilea.core.authorization.model.impl.Authority;
import it.cilea.core.authorization.model.impl.Identity;
import it.cilea.core.authorization.model.impl.Resource;
import it.cilea.core.authorization.model.impl.ResourceLink;
import it.cilea.core.authorization.model.impl.ResourceLink.ResourceLinkId;
import it.cilea.core.authorization.model.impl.UserDetail;
import it.cilea.core.model.SelectBaseInteger;
import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.SelectBaseStringI18n;
import it.cilea.core.model.Selectable;
import it.cilea.core.spring.util.MessageUtil;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

	@Autowired
	private AuthorityDao authorityDao;
	@Autowired
	private ResourceDao resourceDao;
	@Autowired
	private ResourceLinkDao resourceLinkDao;
	@Autowired
	private CoreUserDao coreUserDao;
	@Autowired
	private IdentityDao identityDao;
	@Autowired
	protected MessageUtil messageUtil;

	/*
	 * This method will be replaced by dictionary_mine population or another
	 * population strategy
	 */
	public List<Selectable> getResourceChoiceList(String nullable, String nullableKey) {
		List<Selectable> resultList = new LinkedList<Selectable>();
		Selectable a = new SelectBaseInteger(1, "Abilitato");
		Selectable b = new SelectBaseInteger(0, "Disabilitato");
		resultList.add(a);
		resultList.add(b);

		if (!Boolean.valueOf(nullable))
			return resultList;

		SelectBaseString element = new SelectBaseString();
		element.setDisplayValue(messageUtil.findMessage(nullableKey));
		resultList.add(0, element);
		return resultList;
	}

	public List<Authority> getAuthorityEagerList() {
		return authorityDao.findByNamedQuery("Authority.findAllEager");
	}

	public List<Resource> getResourceAllowedList() {
		return resourceDao.findByNamedQuery("Resource.findAllowed");
	}

	public List<Selectable> getResourceDiscriminatorSelectableList(String nullable, String nullableKey) {
		List<Selectable> list = new LinkedList<Selectable>();
		list.addAll(resourceDao.findByNamedQuery("Resource.findDistinctDiscriminator"));
		if (!Boolean.valueOf(nullable))
			return list;
		SelectBaseString element = new SelectBaseString();
		element.setDisplayValue(messageUtil.findMessage(nullableKey));
		list.add(0, element);
		return list;
	}

	public List<Selectable> getResourceDiscriminatorServerSideList() {
		List<Selectable> list = new LinkedList<Selectable>();
		if (!AuthorizationUserHolder.getUser().hasAuthorities("/ADMIN.profile"))
			list.add(new SelectBaseStringI18n("profile", "resourceDiscriminator.profile"));
		else
			list.addAll(resourceDao.findByNamedQuery("Resource.findDistinctDiscriminator"));
		return list;
	}

	public List<Object[]> getAllowedResourceLink() {
		return resourceLinkDao.getAllowedResourceLinkIdentifier();
	}

	public List<Resource> getResourceList() {
		return resourceDao.findByNamedQuery("Resource.findAll");
	}

	public List<ResourceLink> getResourceLinkList() {
		return resourceLinkDao.findByNamedQuery("ResourceLink.findAll");
	}

	public Boolean saveOnOffResourceLink(String resourceLinkId) {
		ResourceLink resourceLink = resourceLinkDao.get(new ResourceLinkId(resourceLinkId));
		resourceLink.setAllowed(!resourceLink.getAllowed());
		resourceLinkDao.save(resourceLink);
		return resourceLink.getAllowed();
	}

	public UserDetail loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		try {
			Integer userId = coreUserDao.getUserIdByUsername(username);
			String password = coreUserDao.getPasswordHashByUsername(username);
			List<Identity> identityList = identityDao.findByNamedQueryAndNamedParam("Identity.findByUser", "userId",
					userId);
			UserDetail userDetail = new UserDetail(userId, username, password, (Collection<Identity>) identityList);
			return userDetail;
		} catch (UsernameNotFoundException e) {
			throw e;
		} catch (DataAccessException e) {
			throw e;
		}
	}

	public Identity getIdentity(Integer identityId) {
		return identityDao.get(identityId);
	}

	public void setAuthorityDao(AuthorityDao authorityDao) {
		this.authorityDao = authorityDao;
	}

	public void setCoreUserDao(CoreUserDao coreUserDao) {
		this.coreUserDao = coreUserDao;
	}

	public void setIdentityDao(IdentityDao identityDao) {
		this.identityDao = identityDao;
	}

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	public void setResourceDao(ResourceDao resourceDao) {
		this.resourceDao = resourceDao;
	}

	public void setResourceLinkDao(ResourceLinkDao resourceLinkDao) {
		this.resourceLinkDao = resourceLinkDao;
	}
}