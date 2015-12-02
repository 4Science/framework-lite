package it.cilea.core.widget.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;
import it.cilea.core.service.GenericService;
import it.cilea.core.widget.service.WidgetService;

@Entity
public class WidgetLink extends IdentifiableObject implements Comparable<WidgetLink>, Cloneable, Selectable {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "FK_PARENT")
	private Integer parentId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_PARENT", insertable = false, updatable = false)
	private Widget parent;

	@Column(name = "FK_CHILD")
	private Integer childId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CHILD", insertable = false, updatable = false)
	private Widget child;

	@Column(name = "ORDERING")
	private Integer ordering;

	public WidgetLink() {
	}

	public int compareTo(WidgetLink other) {
		if (this.getId() != null && other.getId() != null) {
			return this.getId().compareTo(other.getId());
		} else
			return this.getId().compareTo(other.getId());
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	public void initialize(GenericService genericService) {
		WidgetService widgetService = (WidgetService) genericService.getService("widgetService");
		parent = parentId != null ? widgetService.getWidget(parentId) : null;
		child = childId != null ? widgetService.getWidget(childId) : null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		WidgetLink clone = (WidgetLink) super.clone();
		return clone;
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

	public Widget getParent() {
		return parent;
	}

	public void setParent(Widget parent) {
		this.parent = parent;
	}

	public Widget getChild() {
		return child;
	}

	public void setChild(Widget child) {
		this.child = child;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	@Transient
	public String getIdentifyingValue() {
		if (id == null)
			return null;
		return String.valueOf(id);
	}

	@Transient
	public String getDisplayValue() {
		return "MUST BE IMPLEMENTED";
	}

}
