package it.cilea.core.authorization.model.impl;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import it.cilea.core.authorization.comparator.ResourceComparator;
import it.cilea.core.authorization.comparator.ResourceLinkComparator;
import it.cilea.core.authorization.model.CoreResource;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;

@Entity
@NamedQueries({ @NamedQuery(name = "Resource.findAll", query = "from Resource r order by description asc"),
		@NamedQuery(name = "Resource.findByIdentifier", query = "from Resource resource where resource.identifier=:identifier"),
		@NamedQuery(name = "Resource.findDistinctDiscriminator", query = "select distinct new it.cilea.core.model.SelectBaseStringI18n(resource.discriminator,concat('resourceDiscriminator.',resource.discriminator)) from Resource resource") })
public class Resource extends IdentifiableObject implements CoreResource<String>, Selectable {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DISCRIMINATOR")
	private String discriminator;

	@Column(name = "DESCRIPTION", length = 4000)
	private String description;

	@Column(name = "NOTE", length = 4000)
	private String note;

	@Column(name = "ALLOWED")
	private Boolean allowed;

	@Column(name = "IDENTIFIER", length = 4000)
	private String identifier;

	@Column(name = "DATA_ACCESS_LOGIC")
	private String dataAccessLogicBeanIdentifier;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "resource")
	private Set<Authorities> authorities = new HashSet<Authorities>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "child")
	private Set<ResourceLink> parentResourceLinkSet = new LinkedHashSet<ResourceLink>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "parent")
	private Set<ResourceLink> childResourceLinkSet = new LinkedHashSet<ResourceLink>();

	@Transient
	private Set<Resource> allowedParentResourceSet;

	@Transient
	private Set<Resource> allowedChildResourceSet;

	@Transient
	private Set<Resource> allAllowedParentResourceSet;

	public Resource() {
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
	public String getDiscriminator() {
		return discriminator;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getDataAccessLogicBeanIdentifier() {
		return dataAccessLogicBeanIdentifier;
	}

	@Transient
	public String getIdentifyingValue() {
		if (id == null)
			return null;
		return String.valueOf(id);
	}

	@Transient
	public String getDisplayValue() {
		return getDescription();
	}

	public Set<Resource> getAllowedChildResourceSet() {
		if (allowedChildResourceSet == null) {
			allowedChildResourceSet = new LinkedHashSet<Resource>();
			if (this.getAllowed())
				for (ResourceLink link : getChildResourceLinkSet())
					if (link.getAllowed() && link.getChild().getAllowed())
						allowedChildResourceSet.add(link.getChild());
		}
		return allowedChildResourceSet;
	}

	public Set<Resource> getAllowedParentResourceSet() {
		if (allowedParentResourceSet == null) {
			allowedParentResourceSet = new LinkedHashSet<Resource>();
			if (this.getAllowed())
				for (ResourceLink link : getParentResourceLinkSet())
					if (link.getAllowed() && link.getParent().getAllowed())
						allowedParentResourceSet.add(link.getParent());
		}
		return allowedParentResourceSet;
	}

	@Transient
	public Set<Resource> getAllowedChildResourceSetOrdered() {
		Set<Resource> orderedSet = new TreeSet<Resource>(new ResourceComparator());
		orderedSet.addAll(getAllowedChildResourceSet());
		return orderedSet;
	}

	@Transient
	public Set<Resource> getAllowedParentResourceSetOrdered() {
		Set<Resource> orderedSet = new TreeSet<Resource>(new ResourceComparator());
		orderedSet.addAll(getAllowedParentResourceSet());
		return orderedSet;
	}

	public Set<Authorities> getAuthorities() {
		return authorities;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setAuthorities(Set<Authorities> authorities) {
		this.authorities = authorities;
	}

	public void setDataAccessLogicBeanIdentifier(String dataAccessLogicBeanIdentifier) {
		this.dataAccessLogicBeanIdentifier = dataAccessLogicBeanIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Set<ResourceLink> getParentResourceLinkSet() {
		return parentResourceLinkSet;
	}

	public Set<ResourceLink> getParentResourceLinkSetOrdered() {
		Set<ResourceLink> orderedSet = new TreeSet<ResourceLink>(new ResourceLinkComparator());
		orderedSet.addAll(parentResourceLinkSet);
		return orderedSet;
	}

	public void setParentResourceLinkSet(Set<ResourceLink> parentResourceLinkSet) {
		this.parentResourceLinkSet = parentResourceLinkSet;
	}

	public Set<ResourceLink> getChildResourceLinkSet() {
		return childResourceLinkSet;
	}

	public Set<ResourceLink> getChildResourceLinkSetOrdered() {
		Set<ResourceLink> orderedSet = new TreeSet<ResourceLink>(new ResourceLinkComparator());
		orderedSet.addAll(childResourceLinkSet);
		return orderedSet;
	}

	public void setChildResourceLinkSet(Set<ResourceLink> childResourceLinkSet) {
		this.childResourceLinkSet = childResourceLinkSet;
	}

	public Boolean getAllowed() {
		return (this.allowed == null ? true : this.allowed);
	}

	public void setAllowed(Boolean allowed) {
		this.allowed = allowed;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Set<Resource> getAllAllowedParentResourceSet() {
		if (allAllowedParentResourceSet == null) {
			allAllowedParentResourceSet = new LinkedHashSet<Resource>();
			if (this.getAllowed())
				for (ResourceLink link : getParentResourceLinkSet()) {
					if (link.getAllowed() && link.getParent().getAllowed()) {
						allAllowedParentResourceSet.add(link.getParent());
						allAllowedParentResourceSet.addAll(link.getParent().getAllAllowedParentResourceSet());
					}
				}
		}
		return allAllowedParentResourceSet;
	}

	@Transient
	public Set<Resource> getAllAllowedParentResourceSetOrdered() {
		Set<Resource> orderedSet = new TreeSet<Resource>(new ResourceComparator());
		orderedSet.addAll(getAllAllowedParentResourceSet());
		return orderedSet;
	}
}
