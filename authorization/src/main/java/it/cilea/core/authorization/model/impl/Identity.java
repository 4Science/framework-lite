package it.cilea.core.authorization.model.impl;

import it.cilea.core.authorization.model.CoreIdentity;
import it.cilea.core.model.IdentifiableObject;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({ @NamedQuery(name = "Identity.findByUser", query = "from Identity where userId = :userId"),
		@NamedQuery(name = "Identity.findByTeam", query = "from Identity where teamId = :teamId")
})
public class Identity extends IdentifiableObject implements CoreIdentity {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "FK_USER")
	private Integer userId;

	@Column(name = "FK_TEAM")
	private Integer teamId;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "identity")
	private Set<Authorities> authorities = new HashSet<Authorities>();

	public Identity() {
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
	public Boolean hasAuthorities() {
		return authorities.size() > 0;
	}

	@Override
	public Set<Authorities> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authorities> authorities) {
		this.authorities = authorities;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTeamId() {
		return teamId;
	}

	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
}
