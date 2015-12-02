package it.cilea.core.authorization.model.impl;

import it.cilea.core.authorization.model.CoreAuthoritiesInfo;
import it.cilea.core.model.IdentifiableObject;

import java.util.HashSet;
import java.util.Set;

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
public class AuthoritiesInfo extends IdentifiableObject implements CoreAuthoritiesInfo<Integer> {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "INFO")
	private Integer info;

	@Column(name = "DESCRIPTION", length = 4000)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_AUTHORITIES", insertable = false, updatable = false)
	private Authorities authorities;
	@Column(name = "FK_AUTHORITIES")
	private Integer authoritiesId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_AUTHORITIES_INFO", insertable = false, updatable = false)
	private Authorities authoritiesInfo;
	@Column(name = "FK_AUTHORITIES_INFO")
	private Integer authoritiesInfoId;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "authoritiesInfo")
	private Set<AuthoritiesInfo> infoSet = new HashSet<AuthoritiesInfo>();

	public AuthoritiesInfo() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Authorities getAuthorities() {
		return authorities;
	}

	@Override
	public Integer getInfo() {
		return info;
	}

	@Transient
	public Set<Integer> getInfoIdentifierSet() {
		Set<Integer> infoIntegerSet = new HashSet<Integer>();
		for (AuthoritiesInfo info : infoSet)
			infoIntegerSet.add(Integer.valueOf(info.getInfo()));
		return infoIntegerSet;
	}

	@Transient
	public void getInfoIdentifierStringSet(String parentInfo, Set<String> infoIntegerSet) {
		for (AuthoritiesInfo infoChild : this.getInfoSet()) {
			String newParentInfo = parentInfo + "_" + infoChild.getInfo().toString();
			if (infoChild.getInfoSet().size() == 0)
				infoIntegerSet.add(newParentInfo);
			else
				infoChild.getInfoIdentifierStringSet(newParentInfo, infoIntegerSet);
		}
	}

	public Integer getAuthoritiesId() {
		return authoritiesId;
	}

	public void setAuthorities(Authorities authorities) {
		this.authorities = authorities;
	}

	public void setAuthoritiesId(Integer authoritiesId) {
		this.authoritiesId = authoritiesId;
	}

	public Authorities getAuthoritiesInfo() {
		return authoritiesInfo;
	}

	public void setAuthoritiesInfo(Authorities authoritiesInfo) {
		this.authoritiesInfo = authoritiesInfo;
	}

	public Integer getAuthoritiesInfoId() {
		return authoritiesInfoId;
	}

	public void setAuthoritiesInfoId(Integer authoritiesInfoId) {
		this.authoritiesInfoId = authoritiesInfoId;
	}

	public void setInfo(Integer info) {
		this.info = info;
	}

	public Set<AuthoritiesInfo> getInfoSet() {
		return infoSet;
	}

	public void setInfoSet(Set<AuthoritiesInfo> infoSet) {
		this.infoSet = infoSet;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
