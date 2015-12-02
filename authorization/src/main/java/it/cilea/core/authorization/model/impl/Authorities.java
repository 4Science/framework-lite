package it.cilea.core.authorization.model.impl;

import it.cilea.core.authorization.model.CoreAuthorities;
import it.cilea.core.authorization.model.CoreIdentity;
import it.cilea.core.model.IdentifiableObject;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Authorities extends IdentifiableObject implements CoreAuthorities<String, String, Integer> {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Transient
	protected UUID uuid = UUID.randomUUID();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_AUTHORITY", insertable = false, updatable = false)
	private Authority authority;
	@Column(name = "FK_AUTHORITY")
	private Integer authorityId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_IDENTITY", insertable = false, updatable = false)
	private Identity identity;
	@Column(name = "FK_IDENTITY")
	private Integer identityId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_RESOURCE", insertable = false, updatable = false)
	private Resource resource;
	@Column(name = "FK_RESOURCE")
	private Integer resourceId;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "authorities")
	private Set<AuthoritiesInfo> infoSet = new HashSet<AuthoritiesInfo>();

	public Authorities() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getAuthority() {
		return authority.getDescription();
	}

	@Override
	@Transient
	public Set<Integer> getInfoIdentifierSet() {
		Set<Integer> infoIntegerSet = new HashSet<Integer>();
		for (AuthoritiesInfo info : infoSet)
			infoIntegerSet.add(Integer.valueOf(info.getInfo()));
		return infoIntegerSet;
	}

	@Override
	@Transient
	public Set<String> getInfoIdentifierStringSet() {
		// TODO rendere recursive

		// Set<String> infoIntegerSet = new HashSet<String>();
		// for (AuthoritiesInfo info1 : infoSet) {
		// String parentInfo1 = info1.getInfo().toString();
		// if (info1.getInfoSet().size() == 0)
		// infoIntegerSet.add(parentInfo1);
		// else
		// for (AuthoritiesInfo info2 : info1.getInfoSet()) {
		// String parentInfo2 = parentInfo1 + "_" + info2.getInfo().toString();
		// if (info2.getInfoSet().size() == 0)
		// infoIntegerSet.add(parentInfo2);
		// else
		// for (AuthoritiesInfo info3 : info2.getInfoSet()) {
		// infoIntegerSet.add(parentInfo2 + "_" + info3.getInfo().toString());
		// }
		// }
		// }
		// return infoIntegerSet;

		Set<String> infoIntegerSet = new HashSet<String>();
		for (AuthoritiesInfo info : infoSet) {
			info.getInfoIdentifierStringSet(info.getInfo().toString(), infoIntegerSet);
		}
		return infoIntegerSet;
	}

	@Override
	public String getAuthorityIdentifier() {
		return authority.getIdentifier();
	}

	@Override
	public String getResourceIdentifier() {
		return resource.getIdentifier();
	}

	public Authority getAuthorityObject() {
		return authority;
	}

	public void setAuthorityObject(Authority authority) {
		this.authority = authority;
	}

	public Identity getIdentity() {
		return identity;
	}

	public Resource getResource() {
		return resource;
	}

	public Set<AuthoritiesInfo> getInfoSet() {
		return infoSet;
	}

	public Integer getAuthorityId() {
		return authorityId;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	public Integer getIdentityId() {
		return identityId;
	}

	public void setAuthorityId(Integer authorityId) {
		this.authorityId = authorityId;
	}

	public void setIdentity(Identity identity) {
		this.identity = identity;
	}

	public void setIdentityId(Integer identityId) {
		this.identityId = identityId;
	}

	public void setInfoSet(Set<AuthoritiesInfo> infoSet) {
		this.infoSet = infoSet;
	}

	public Integer getResourceId() {
		return resourceId;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}

	@Override
	@Deprecated
	public Set<CoreIdentity> getIdentitySet() {
		return null;
	}

	public String getUniqueIdentifier() {
		return uuid.toString();
	}

}
