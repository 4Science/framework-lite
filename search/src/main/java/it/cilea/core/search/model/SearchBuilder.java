package it.cilea.core.search.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.widget.model.Widget;

@Entity
public class SearchBuilder extends IdentifiableObject {

	@Id
	@Column(name = "ID")
	protected Integer id;

	@Column(name = "NAME", length = 4000)
	@Size(min = 0, max = 4000)
	protected String name;

	@Column(name = "URL", length = 1000)
	@Size(min = 0, max = 1000)
	protected String url;

	@Column(name = "PAGE_SIZE")
	protected Integer pageSize;

	@Column(name = "WEB_VIEW")
	protected String view;

	@Column(name = "STRATEGY", length = 255)
	@Size(min = 0, max = 255)
	protected String strategy;

	@Column(name = "SELECT_CLAUSE", length = 4000)
	@Size(min = 0, max = 4000)
	protected String selectClause;

	@Column(name = "FILTER_CLAUSE", length = 4000)
	@Size(min = 0, max = 4000)
	protected String filterClause;

	@Column(name = "ORDER_CLAUSE", length = 4000)
	@Size(min = 0, max = 4000)
	protected String orderClause;

	@Column(name = "GROUP_CLAUSE", length = 4000)
	@Size(min = 0, max = 4000)
	protected String groupClause;

	@Column(name = "WITH_CLAUSE", length = 4000)
	@Size(min = 0, max = 4000)
	protected String withClause;

	@Column(name = "VALIDATION_CLAUSE", length = 4000)
	@Size(min = 0, max = 4000)
	protected String validationClause;

	@OrderBy(value = "ordering")
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "searchBuilder", fetch = FetchType.LAZY)
	protected Set<SearchBuilderWidgetLink> searchBuilderWidgetLinkSet = new LinkedHashSet<SearchBuilderWidgetLink>();

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "searchBuilder", fetch = FetchType.LAZY)
	protected Set<SearchBuilderParameter> searchBuilderParameterSet = new HashSet<SearchBuilderParameter>();

	@Transient
	protected Map<String, String> searchBuilderParameterMap = new HashMap<String, String>();

	@Transient
	protected Map<String, Widget> widgetMap = new HashMap<String, Widget>();

	@Transient
	protected String[] mediaTypes;

	public void init() {
		for (SearchBuilderParameter searchBuilderParameter : searchBuilderParameterSet) {
			String configValue = searchBuilderParameter.getValue();
			configValue = ConfigurationUtil.replaceText(configValue);
			searchBuilderParameterMap.put(searchBuilderParameter.getName(), configValue);
		}

		for (SearchBuilderWidgetLink searchBuilderWidgetLink : searchBuilderWidgetLinkSet) {
			if (searchBuilderWidgetLink.getWidget() != null)
				if (searchBuilderWidgetLink.getWidget().getPageAttributeName() != null)
					widgetMap.put(searchBuilderWidgetLink.getWidget().getPageAttributeName(),
							searchBuilderWidgetLink.getWidget());
				else
					widgetMap.put(searchBuilderWidgetLink.getWidget().getModelAttributeName(),
							searchBuilderWidgetLink.getWidget());
		}
		if (getSearchBuilderParameterMap().containsKey(SearchConstant.MEDIA_TYPE))
			mediaTypes = StringUtils.split(getSearchBuilderParameterMap().get(SearchConstant.MEDIA_TYPE), ",");
	}

	@Transient
	public Map<String, Set<SearchBuilderWidgetLink>> getSearchBuilderWidgetLinkSetBySectionMap() {
		Map<String, Set<SearchBuilderWidgetLink>> map = new HashMap<String, Set<SearchBuilderWidgetLink>>();
		map.put(SearchConstant.SECTION_SELECT, new LinkedHashSet<SearchBuilderWidgetLink>());
		map.put(SearchConstant.SECTION_FILTER, new LinkedHashSet<SearchBuilderWidgetLink>());
		map.put(SearchConstant.SECTION_GROUP, new LinkedHashSet<SearchBuilderWidgetLink>());
		for (SearchBuilderWidgetLink link : searchBuilderWidgetLinkSet) {
			if (link.getWidget().getSection() == null)
				map.get(SearchConstant.SECTION_FILTER).add(link);
			else
				map.get(link.getWidget().getSection()).add(link);
		}
		return map;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<SearchBuilderWidgetLink> getSearchBuilderWidgetLinkSet() {
		return searchBuilderWidgetLinkSet;
	}

	public void setSearchBuilderWidgetLinkSet(Set<SearchBuilderWidgetLink> searchBuilderWidgetLinkSet) {
		this.searchBuilderWidgetLinkSet = searchBuilderWidgetLinkSet;
	}

	public String getFilterClause() {
		return filterClause;
	}

	public void setFilterClause(String query) {
		this.filterClause = query;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public Set<SearchBuilderParameter> getSearchBuilderParameterSet() {
		return searchBuilderParameterSet;
	}

	public void setSearchBuilderParameterSet(Set<SearchBuilderParameter> searchBuilderParameterSet) {
		this.searchBuilderParameterSet = searchBuilderParameterSet;
	}

	public Map<String, String> getSearchBuilderParameterMap() {
		return searchBuilderParameterMap;
	}

	public void setSearchBuilderParameterMap(Map<String, String> searchBuilderParameterMap) {
		this.searchBuilderParameterMap = searchBuilderParameterMap;
	}

	public String getOrderClause() {
		return orderClause;
	}

	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}

	public String getGroupClause() {
		return groupClause;
	}

	public void setGroupClause(String groupClause) {
		this.groupClause = groupClause;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Widget> getWidgetMap() {
		return widgetMap;
	}

	public void setWidgetMap(Map<String, Widget> widgetMap) {
		this.widgetMap = widgetMap;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	public String getSelectClause() {
		return selectClause;
	}

	public void setSelectClause(String selectClause) {
		this.selectClause = selectClause;
	}

	public String getWithClause() {
		return withClause;
	}

	public void setWithClause(String withClause) {
		this.withClause = withClause;
	}

	public String getValidationClause() {
		return validationClause;
	}

	public void setValidationClause(String validationClause) {
		this.validationClause = validationClause;
	}

	public String[] getMediaTypes() {
		return mediaTypes;
	}

	public void setMediaTypes(String[] mediaTypes) {
		this.mediaTypes = mediaTypes;
	}
}