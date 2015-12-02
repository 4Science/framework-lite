package it.cilea.core.authorization.dto;

import it.cilea.core.authorization.model.CoreAuthorities;
import it.cilea.core.authorization.model.CoreIdentity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;

@Entity
public class AuthoritiesDto implements CoreAuthorities<String, String, Integer> {

	private Integer authorityId;
	private String authorityIdentifier;
	private Boolean authorityUseInfos;
	private String resourceIdentifier;
	private String dataAccessLogicBeanIdentifier;
	private Set<Integer> infoIdentifierSet = new HashSet<Integer>();
	private Set<CoreIdentity> identitySet = new HashSet<CoreIdentity>();
	private Set<String> infoIdentifierStringSet = new HashSet<String>();

	public AuthoritiesDto() {
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof AuthoritiesDto) {
			AuthoritiesDto that = (AuthoritiesDto) o;
			return this.authorityIdentifier.equals(that.authorityIdentifier)
					&& this.resourceIdentifier.equals(that.resourceIdentifier);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return authorityIdentifier.hashCode() + resourceIdentifier.hashCode();
	}

	@Override
	public String getAuthorityIdentifier() {
		return authorityIdentifier;
	}

	@Override
	public String getResourceIdentifier() {
		return resourceIdentifier;
	}

	@Override
	public Set<Integer> getInfoIdentifierSet() {
		return infoIdentifierSet;
	}

	@Override
	public String getAuthority() {
		return authorityIdentifier;
	}

	@Override
	public Set<CoreIdentity> getIdentitySet() {
		return identitySet;
	}

	public void setAuthorityIdentifier(String authorityIdentifier) {
		this.authorityIdentifier = authorityIdentifier;
	}

	public Boolean getAuthorityUseInfos() {
		return authorityUseInfos;
	}

	public void setAuthorityUseInfos(Boolean authorityUseInfos) {
		this.authorityUseInfos = authorityUseInfos;
	}

	public void setResourceIdentifier(String resourceIdentifier) {
		this.resourceIdentifier = resourceIdentifier;
	}

	public String getDataAccessLogicBeanIdentifier() {
		return dataAccessLogicBeanIdentifier;
	}

	public void setDataAccessLogicBeanIdentifier(String dataAccessLogicBeanIdentifier) {
		this.dataAccessLogicBeanIdentifier = dataAccessLogicBeanIdentifier;
	}

	public void setInfoIdentifierSet(Set<Integer> infoIdentifierSet) {
		this.infoIdentifierSet = infoIdentifierSet;
	}

	public Integer getAuthorityId() {
		return authorityId;
	}

	public void setAuthorityId(Integer authorityId) {
		this.authorityId = authorityId;
	}

	public void setIdentitySet(Set<CoreIdentity> identitySet) {
		this.identitySet = identitySet;
	}

	public Set<String> getInfoIdentifierStringSet() {
		return infoIdentifierStringSet;
	}

	public void setInfoIdentifierStringSet(Set<String> infoIdentifierStringSet) {
		this.infoIdentifierStringSet = infoIdentifierStringSet;
	}
}
