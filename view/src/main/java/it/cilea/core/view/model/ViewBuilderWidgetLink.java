package it.cilea.core.view.model;

import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.widget.model.Widget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ViewBuilderWidgetLink extends IdentifiableObject implements Comparable<ViewBuilderWidgetLink> {

	@Id
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "FK_WIDGET", insertable = false, updatable = false, nullable = false)
	private Widget widget;

	@Column(name = "FK_WIDGET", insertable = true, updatable = true, nullable = true)
	private Integer widgetId;

	@ManyToOne
	@JoinColumn(name = "FK_VIEW_BUILDER", insertable = false, updatable = false, nullable = false)
	private ViewBuilder viewBuilder;

	@Column(name = "FK_VIEW_BUILDER", insertable = true, updatable = true, nullable = true)
	private Integer viewBuilderId;

	@Column(name = "ORDERING", insertable = true, updatable = true, nullable = true)
	private Integer ordering;

	@Column(name = "LAYOUT_MODE", insertable = true, updatable = true, nullable = true)
	private String layoutMode = "div";

	// TODO ADD CONDITION (RULES) FOR EDIT/SHOW

	@Override
	public int compareTo(ViewBuilderWidgetLink o) {
		if (getOrdering() == null)
			return 1;
		if (o.getOrdering() == null)
			return -1;
		if (getOrdering().equals(o.getOrdering()))
			return getId().compareTo(o.getId());
		return getOrdering().compareTo(o.getOrdering());
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

	public ViewBuilder getViewBuilder() {
		return viewBuilder;
	}

	public void setViewBuilder(ViewBuilder viewBuilder) {
		this.viewBuilder = viewBuilder;
	}

	public Integer getViewBuilderId() {
		return viewBuilderId;
	}

	public void setViewBuilderId(Integer viewBuilderId) {
		this.viewBuilderId = viewBuilderId;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLayoutMode() {
		return layoutMode;
	}

	public void setLayoutMode(String layoutMode) {
		this.layoutMode = layoutMode;
	}
}
