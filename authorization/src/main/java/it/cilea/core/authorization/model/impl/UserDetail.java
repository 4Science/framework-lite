package it.cilea.core.authorization.model.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.cilea.core.authorization.AuthorizationConstant;
import it.cilea.core.authorization.comparator.AuthorityComparator;
import it.cilea.core.authorization.dto.AuthoritiesDto;
import it.cilea.core.authorization.model.CoreAuthorities;
import it.cilea.core.authorization.model.CoreIdentity;
import it.cilea.core.authorization.model.CoreUserDetail;

public class UserDetail implements CoreUserDetail<String, String, Integer> {

	private boolean enabled = true;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private Integer userId;
	private String username;
	private String password;
	private Collection<AuthoritiesDto> globalAuthorities;

	private Collection<Authority> authorityList;
	private Map<Authority, Set<Identity>> authorityMap = new HashMap<Authority, Set<Identity>>();
	private String currentAuthorityIdentifier;

	private Map<String, Map<String, AuthoritiesDto>> globalAuthoritiesByAuthorityAndResourceMap;

	private Map<String, String> stringMap;

	private Set<Identity> identitySet = new HashSet<Identity>();

	public UserDetail() {
	}

	public UserDetail(Integer userId, String username, String password, Collection<Identity> identitySet) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		globalAuthorities = new HashSet<AuthoritiesDto>();
		authorityList = new TreeSet<Authority>(new AuthorityComparator());
		/*
		 * tra le authorities delle identity potrebbero essercene alcune uguali
		 * (magari con info diverse) puo succedere se ho più team con varie
		 * authorities il ciclo successivo crea la globalAuthorities come somma
		 * di tutto inoltre mi inizializza tutte le collection lazy essendoci le
		 * resourceLink devo ciclare su tutti i figli di ogni risorsa collegata.
		 * inizializando globalAuthorities con tutto quanto risolvo molti
		 * problemi dopo
		 */
		for (Identity identity : identitySet) {
			for (Authorities authorities : identity.getAuthorities()) {
				Resource resource = AuthorizationConstant.RESOURCE_ID_MAP.get(authorities.getResourceId());
				if (resource.getAllowed()) {
					addAuthorities(globalAuthorities, authorities, resource);
					if (!authorityList
							.contains(AuthorizationConstant.AUTHORITY_ID_MAP.get(authorities.getAuthorityId())))
						authorityList.add(AuthorizationConstant.AUTHORITY_ID_MAP.get(authorities.getAuthorityId()));
				}
			}
			Identity clonedIdentity = new Identity();
			clonedIdentity.setId(identity.getId());
			clonedIdentity.setTeamId(identity.getTeamId());
			clonedIdentity.setUserId(identity.getUserId());
			this.identitySet.add(clonedIdentity);
		}
		globalAuthoritiesByAuthorityAndResourceMap = new HashMap<String, Map<String, AuthoritiesDto>>();
		for (AuthoritiesDto dto : globalAuthorities) {
			if (!globalAuthoritiesByAuthorityAndResourceMap.containsKey(dto.getAuthorityIdentifier()))
				globalAuthoritiesByAuthorityAndResourceMap.put(dto.getAuthorityIdentifier(),
						new HashMap<String, AuthoritiesDto>());
			globalAuthoritiesByAuthorityAndResourceMap.get(dto.getAuthorityIdentifier())
					.put(dto.getResourceIdentifier(), dto);
		}
		for (AuthoritiesDto dto : globalAuthorities) {
			if (!authorityMap.containsKey(AuthorizationConstant.AUTHORITY_ID_MAP.get(dto.getAuthorityId())))
				authorityMap.put(AuthorizationConstant.AUTHORITY_ID_MAP.get(dto.getAuthorityId()),
						new HashSet<Identity>());
			for (CoreIdentity identity : dto.getIdentitySet()) {
				authorityMap.get(AuthorizationConstant.AUTHORITY_ID_MAP.get(dto.getAuthorityId()))
						.add((Identity) identity);
			}
		}
		swithAuthority(authorityList.size() != 0 ? authorityList.iterator().next().getIdentifier() : null);
	}

	private void addAuthorities(Collection<AuthoritiesDto> set, Authorities authorities, Resource resource) {
		AuthoritiesDto dto = new AuthoritiesDto();
		dto.setAuthorityIdentifier(
				AuthorizationConstant.AUTHORITY_ID_MAP.get(authorities.getAuthorityId()).getIdentifier());
		dto.setAuthorityId(authorities.getAuthorityId());
		dto.setAuthorityUseInfos(
				AuthorizationConstant.AUTHORITY_ID_MAP.get(authorities.getAuthorityId()).getUseInfos());
		dto.setResourceIdentifier(resource.getIdentifier());
		dto.setDataAccessLogicBeanIdentifier(resource.getDataAccessLogicBeanIdentifier());
		dto.getInfoIdentifierSet().addAll(authorities.getInfoIdentifierSet());
		dto.getInfoIdentifierStringSet().addAll(authorities.getInfoIdentifierStringSet());
		Identity clonedIdentity = new Identity();
		clonedIdentity.setId(authorities.getIdentity().getId());
		clonedIdentity.setTeamId(authorities.getIdentity().getTeamId());
		clonedIdentity.setUserId(authorities.getIdentity().getUserId());
		dto.getIdentitySet().add(clonedIdentity);
		if (set.contains(dto)) {
			for (AuthoritiesDto exist : set) {
				if (exist.equals(dto)) {
					exist.getInfoIdentifierSet().addAll(dto.getInfoIdentifierSet());
					exist.getIdentitySet().addAll(dto.getIdentitySet());
				}
			}
		} else
			set.add(dto);
		for (Resource resourceChild : resource.getAllowedChildResourceSet())
			addAuthorities(set, authorities, resourceChild);
	}

	/**
	 * Returns authorityDto in the current authority (granted) with the given
	 * resource
	 * 
	 * @param resourceIdentifier
	 *            resource to be checked
	 */
	@Override
	public CoreAuthorities<String, String, Integer> getAuthorities(String resourceIdentifier) {
		return getAuthorities(resourceIdentifier, currentAuthorityIdentifier);
	}

	/**
	 * Checks if you've got in the current authority (granted) with the given
	 * resource
	 * 
	 * @param resourceIdentifier
	 *            resource to be checked
	 */
	public boolean hasAuthorities(String resourceIdentifier) {
		return hasAuthorities(resourceIdentifier, currentAuthorityIdentifier);
	}

	/**
	 * Returns authorityDto in the current authority (granted) with the given
	 * resource and authority
	 * 
	 * @param resourceIdentifier
	 *            resource to be checked
	 * @param authorityIdentifier
	 *            authority to be checked
	 */
	@Override
	public CoreAuthorities<String, String, Integer> getAuthorities(String resourceIdentifier,
			String authorityIdentifier) {
		if (!hasAuthorities(resourceIdentifier, authorityIdentifier))
			return null;
		return globalAuthoritiesByAuthorityAndResourceMap.get(authorityIdentifier).get(resourceIdentifier);
	}

	/**
	 * Returns ALL CoreAuthorities relatives to the specified resource and
	 * authority REGARDLESS of current authority (granted)
	 * 
	 * 
	 * @param resourceIdentifier
	 *            resource to be checked
	 * @param authorityIdentifier
	 *            authority to be checked
	 */

	public CoreAuthorities<String, String, Integer> getGlobalAuthorities(String resourceIdentifier,
			String authorityIdentifier) {
		return globalAuthoritiesByAuthorityAndResourceMap.get(authorityIdentifier).get(resourceIdentifier);
	}

	/**
	 * Checks if you've got in the current authority (granted) with the given
	 * resource and authority
	 * 
	 * @param resourceIdentifier
	 *            resource to be checked
	 * @param authorityIdentifier
	 *            authority to be checked
	 */
	public boolean hasAuthorities(String resourceIdentifier, String authorityIdentifier) {
		if (currentAuthorityIdentifier == null)
			return false;
		if (!currentAuthorityIdentifier.equals(authorityIdentifier))
			return false;
		if (!globalAuthoritiesByAuthorityAndResourceMap.containsKey(authorityIdentifier))
			return false;
		return globalAuthoritiesByAuthorityAndResourceMap.get(authorityIdentifier).containsKey(resourceIdentifier);
	}

	/**
	 * Checks if you've got in the current authority (granted) with the given
	 * resource and authority and infos
	 * 
	 * @param resourceIdentifier
	 *            resource to be checked
	 * @param authorityIdentifier
	 *            authority to be checked
	 * @param info
	 *            info to be checked
	 */
	public boolean hasAuthorities(String resourceIdentifier, String authorityIdentifier, Integer info) {
		if (!currentAuthorityIdentifier.equals(authorityIdentifier))
			return false;
		if (!globalAuthoritiesByAuthorityAndResourceMap.containsKey(authorityIdentifier))
			return false;
		if (!globalAuthoritiesByAuthorityAndResourceMap.get(authorityIdentifier).containsKey(resourceIdentifier))
			return false;
		return globalAuthoritiesByAuthorityAndResourceMap.get(authorityIdentifier).get(resourceIdentifier)
				.getInfoIdentifierSet().contains(info);
	}

	/**
	 * Checks if you've got in the current authority (granted) with the given
	 * resource and authority and infos
	 * 
	 * @param resourceIdentifier
	 *            resource to be checked
	 * @param authorityIdentifier
	 *            authority to be checked
	 * @param info
	 *            info to be checked
	 */
	public boolean hasAuthoritiesComplex(String resourceIdentifier, String authorityIdentifier, String info) {
		if (!currentAuthorityIdentifier.equals(authorityIdentifier))
			return false;
		if (!globalAuthoritiesByAuthorityAndResourceMap.containsKey(authorityIdentifier))
			return false;
		if (!globalAuthoritiesByAuthorityAndResourceMap.get(authorityIdentifier).containsKey(resourceIdentifier))
			return false;
		return globalAuthoritiesByAuthorityAndResourceMap.get(authorityIdentifier).get(resourceIdentifier)
				.getInfoIdentifierStringSet().contains(info);
	}

	public Collection<Authority> getAuthorityList() {
		return authorityList;
	}

	@Override
	public void swithAuthority(String authorityIdentifier) {
		this.currentAuthorityIdentifier = authorityIdentifier;
	}

	@Override
	public String getCurrentAuthorityIdentifier() {
		return currentAuthorityIdentifier;
	}

	@Override
	@Deprecated
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> set = new HashSet<GrantedAuthority>();
		if (globalAuthoritiesByAuthorityAndResourceMap != null && currentAuthorityIdentifier != null
				&& globalAuthoritiesByAuthorityAndResourceMap.get(currentAuthorityIdentifier) != null)
			if (CollectionUtils
					.isNotEmpty(globalAuthoritiesByAuthorityAndResourceMap.get(currentAuthorityIdentifier).keySet()))
				for (String resourceIdentifier : globalAuthoritiesByAuthorityAndResourceMap
						.get(currentAuthorityIdentifier).keySet())
					set.add(globalAuthoritiesByAuthorityAndResourceMap.get(currentAuthorityIdentifier)
							.get(resourceIdentifier));
		return set;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Integer getUserId() {
		return userId;
	}

	public Map<String, String> getStringMap() {
		return stringMap;
	}

	public Map<Authority, Set<Identity>> getAuthorityMap() {
		return authorityMap;
	}

	public Set<Identity> getCurrentIdentitySet() {
		return authorityMap.get(AuthorizationConstant.AUTHORITY_MAP.get(currentAuthorityIdentifier));
	}

	@Override
	public boolean equals(Object obj) {
		UserDetails other = (UserDetails) obj;
		if (other == null)
			return false;
		return getUsername().equals(other.getUsername());
	}

	@Override
	public int hashCode() {
		return getUsername().hashCode();
	}

	public Set<Identity> getIdentitySet() {
		return identitySet;
	}

}
