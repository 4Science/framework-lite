package it.cilea.core.configuration.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import it.cilea.core.configuration.service.ConfigurationService;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;
import it.cilea.core.service.GenericService;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "FK_PARENT", "FK_CHILD", "DISCRIMINATOR" }) })
public class ConfigurationLink extends IdentifiableObject
		implements Comparable<ConfigurationLink>, Cloneable, Selectable {

	@Id
	@Column(name = "ID")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_PARENT", insertable = false, updatable = false)
	private Configuration parent;

	@Column(name = "FK_PARENT")
	private Integer parentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CHILD", insertable = false, updatable = false)
	private Configuration child;

	@Column(name = "FK_CHILD")
	private Integer childId;

	@Column(name = "DISCRIMINATOR")
	private String discriminator;

	@Column(name = "PRIORITY")
	private Integer priority;

	@Override
	public boolean equals(Object o) {
		if (o instanceof ConfigurationLink) {
			ConfigurationLink that = (ConfigurationLink) o;
			return this.id.equals(that.id);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public ConfigurationLink() {
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public Configuration getParent() {
		return parent;
	}

	public void setParent(Configuration parent) {
		this.parent = parent;
	}

	public Configuration getChild() {
		return child;
	}

	public void setChild(Configuration child) {
		this.child = child;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getChildId() {
		return childId;
	}

	public Integer getParentId() {
		return parentId;
	}

	@Override
	public String getIdentifyingValue() {
		if (id == null)
			return null;
		return String.valueOf(id);
	}

	@Override
	public String getDisplayValue() {
		return "MUST BE IMPLEMENTED";
	}

	public void initialize(GenericService genericService) {
		ConfigurationService gaService = (ConfigurationService) genericService.getService("gaService");

		parent = parentId != null ? gaService.getConfiguration(parentId) : null;
		child = childId != null ? gaService.getConfiguration(childId) : null;

		if (child != null) {

			for (String key : child.getBlobMap().keySet()) {
				child.getBlobMap().get(key);
			}
			for (String key : child.getBooleanMap().keySet()) {
				child.getBooleanMap().get(key);
			}
			for (String key : child.getClobMap().keySet()) {
				child.getClobMap().get(key);
			}
			for (String key : child.getDateMap().keySet()) {
				child.getDateMap().get(key);
			}
			for (String key : child.getConfigurationMap().keySet()) {
				child.getConfigurationMap().get(key);
			}

			for (String key : child.getIntegerMap().keySet()) {
				child.getIntegerMap().get(key);
			}
			for (String key : child.getNumberMap().keySet()) {
				child.getNumberMap().get(key);
			}
			for (String key : child.getStringMap().keySet()) {
				child.getStringMap().get(key);
			}
		}

		if (parent != null) {
			parent.getStartDate();
			parent.getEndDate();
			for (String key : parent.getBlobMap().keySet()) {
				parent.getBlobMap().get(key);
			}
			for (String key : parent.getBooleanMap().keySet()) {
				parent.getBooleanMap().get(key);
			}
			for (String key : parent.getClobMap().keySet()) {
				parent.getClobMap().get(key);
			}
			for (String key : parent.getDateMap().keySet()) {
				parent.getDateMap().get(key);
			}
			for (String key : parent.getConfigurationMap().keySet()) {
				parent.getConfigurationMap().get(key);
			}

			for (String key : parent.getIntegerMap().keySet()) {
				parent.getIntegerMap().get(key);
			}
			for (String key : parent.getNumberMap().keySet()) {
				parent.getNumberMap().get(key);
			}
			for (String key : parent.getStringMap().keySet()) {
				parent.getStringMap().get(key);
			}
		}

	}

	public int compareTo(ConfigurationLink other) {
		if (this.getId() != null && other.getId() != null) {
			return this.getId().compareTo(other.getId());
		} else
			return this.getId().compareTo(other.getId());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ConfigurationLink clone = (ConfigurationLink) super.clone();
		return clone;
	}

}
