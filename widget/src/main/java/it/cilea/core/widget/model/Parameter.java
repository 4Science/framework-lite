package it.cilea.core.widget.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class Parameter implements Comparable<Parameter> {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DISCRIMINATOR", length = 1000)
	@Size(min = 0, max = 255)
	private String discriminator;

	@Column(name = "ORDERING")
	private Integer ordering;

	@Column(name = "VALUE", length = 1000)
	@Size(min = 0, max = 1000)
	private String value;

	@ManyToOne
	@JoinColumn(name = "FK_WIDGET", insertable = false, updatable = false, nullable = false)
	private Widget widget;

	@Column(name = "FK_WIDGET", insertable = true, updatable = true, nullable = true)
	private Integer widgetId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public Integer getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Integer widgetId) {
		this.widgetId = widgetId;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	@Override
	public int compareTo(Parameter o) {
		if (getOrdering() == null)
			return 1;
		if (o.getOrdering() == null)
			return -1;
		if (getOrdering().equals(o.getOrdering()))
			return getId().compareTo(o.getId());
		return getOrdering().compareTo(o.getOrdering());
	}

}
