package it.cilea.core.authorization.model.impl;

import it.cilea.core.authorization.model.impl.ResourceLink.ResourceLinkId;
import it.cilea.core.model.BaseObject;
import it.cilea.core.model.Identifiable;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.apache.commons.lang.StringUtils;

@Entity
@NamedQueries({ @NamedQuery(name = "ResourceLink.findAll", query = "from ResourceLink") })
public class ResourceLink extends BaseObject implements Identifiable<ResourceLinkId>, Serializable {

	@Embeddable
	public static class ResourceLinkId extends BaseObject {

		@Column(name = "FK_PARENT")
		private Integer parentId;

		@Column(name = "FK_CHILD")
		private Integer childId;

		public ResourceLinkId() {
		}

		public ResourceLinkId(Integer parentId, Integer childId) {
			super();
			this.parentId = parentId;
			this.childId = childId;
		}

		public ResourceLinkId(String resourceLinkId) {
			super();
			String[] ids = StringUtils.split(resourceLinkId, "_");
			this.parentId = Integer.valueOf(ids[0]);
			this.childId = Integer.valueOf(ids[1]);
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof ResourceLinkId) {
				ResourceLinkId that = (ResourceLinkId) o;
				return this.parentId.equals(that.parentId) && this.childId.equals(that.childId);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return parentId.hashCode() + childId.hashCode();
		}

		@Override
		public String toString() {
			return parentId + "_" + childId;
		}

		public Integer getParentId() {
			return parentId;
		}

		public void setParentId(Integer parentId) {
			this.parentId = parentId;
		}

		public Integer getChildId() {
			return childId;
		}

		public void setChildId(Integer childId) {
			this.childId = childId;
		}
	}

	@EmbeddedId
	private ResourceLinkId id = new ResourceLinkId();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_PARENT", insertable = false, updatable = false)
	private Resource parent;

	@Column(name = "FK_PARENT", insertable = false, updatable = false)
	private Integer parentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CHILD", insertable = false, updatable = false)
	private Resource child;

	@Column(name = "FK_CHILD", insertable = false, updatable = false)
	private Integer childId;

	@Column(name = "ALLOWED")
	private Boolean allowed;

	@Override
	public boolean equals(Object o) {
		if (o instanceof ResourceLink) {
			ResourceLink that = (ResourceLink) o;
			return this.id.equals(that.id);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public ResourceLink() {
	}

	public ResourceLinkId getId() {
		return id;
	}

	public void setId(ResourceLinkId id) {
		this.id = id;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}

	public Resource getChild() {
		return child;
	}

	public void setChild(Resource child) {
		this.child = child;
	}

	public Boolean getAllowed() {
		return (this.allowed == null ? true : this.allowed);
	}

	public void setAllowed(Boolean allowed) {
		this.allowed = allowed;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

}
