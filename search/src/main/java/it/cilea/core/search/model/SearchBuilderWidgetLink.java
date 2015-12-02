package it.cilea.core.search.model;

import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.widget.model.Widget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class SearchBuilderWidgetLink extends IdentifiableObject implements Comparable<SearchBuilderWidgetLink> {

	@Id
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "FK_WIDGET", insertable = false, updatable = false, nullable = false)
	private Widget widget;

	@Column(name = "FK_WIDGET", insertable = true, updatable = true, nullable = true)
	private Integer widgetId;

	@ManyToOne
	@JoinColumn(name = "FK_SEARCH_BUILDER", insertable = false, updatable = false, nullable = false)
	private SearchBuilder searchBuilder;

	@Column(name = "FK_SEARCH_BUILDER", insertable = true, updatable = true, nullable = true)
	private Integer searchBuilderId;

	@Column(name = "ORDERING", insertable = true, updatable = true, nullable = true)
	private Integer ordering;

	@Override
	public int compareTo(SearchBuilderWidgetLink o) {
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

	public SearchBuilder getSearchBuilder() {
		return searchBuilder;
	}

	public void setSearchBuilder(SearchBuilder searchBuilder) {
		this.searchBuilder = searchBuilder;
	}

	public Integer getSearchBuilderId() {
		return searchBuilderId;
	}

	public void setSearchBuilderId(Integer searchBuilderId) {
		this.searchBuilderId = searchBuilderId;
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

}
