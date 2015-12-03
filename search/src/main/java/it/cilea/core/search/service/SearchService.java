package it.cilea.core.search.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cilea.core.model.SelectBase;
import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.Selectable;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.SearchConstant.SearchStrategyType;
import it.cilea.core.search.SearchConstant.StringMatchType;
import it.cilea.core.search.dao.SearchBuilderDao;
import it.cilea.core.search.dao.SearchBuilderWidgetLinkDao;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.model.SearchBuilderParameter;
import it.cilea.core.search.model.SearchBuilderWidgetLink;
import it.cilea.core.spring.util.MessageUtil;

@Service
public class SearchService {
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private SearchBuilderDao searchBuilderDao;
	@Autowired
	private SearchBuilderWidgetLinkDao searchBuilderWidgetLinkDao;

	// SearchBuilder
	public List<SearchBuilder> initSearchBuilderList() {
		return searchBuilderDao.initSearchBuilderList();
	}

	public SearchBuilder getSearchBuilder(Integer searchBuilderId) {
		return searchBuilderDao.get(searchBuilderId);
	}

	public SearchBuilder saveOrUpdate(SearchBuilder searchBuilder) {
		return searchBuilderDao.save(searchBuilder);
	}

	public SearchBuilderParameter saveOrUpdate(SearchBuilderParameter searchBuilderParameter) {
		return searchBuilderDao.saveParameter(searchBuilderParameter);
	}

	public void deleteSearchBuilder(Integer searchBuilderId) {
		searchBuilderDao.remove(searchBuilderId);
	}

	public List<SearchBuilder> getSearchBuilderList() {
		return searchBuilderDao.findByNamedQuery("SearchBuilder.findAll");
	}

	// SearchBuilderWidgetLink
	public void saveOrUpdate(SearchBuilderWidgetLink searchBuilderWidgetLink) {
		searchBuilderWidgetLinkDao.save(searchBuilderWidgetLink);
	}

	public void deleteSearchBuilderWidgetLink(Integer searchBuilderWidgetLinkId) {
		searchBuilderWidgetLinkDao.remove(searchBuilderWidgetLinkId);
	}

	// This method is used through introspection
	public List<SelectBaseString> getSearchBuilderParameterList() {
		SearchBuilderParameterName[] searchBuilderParameterName = SearchConstant.SearchBuilderParameterName.values();
		List<SelectBaseString> searchBuilderParameterNameList = new ArrayList<SelectBaseString>();
		for (int i = 0; i < searchBuilderParameterName.length; i++) {
			searchBuilderParameterNameList.add(new SelectBaseString(searchBuilderParameterName[i].toString(),
					searchBuilderParameterName[i].toString()));
		}
		return searchBuilderParameterNameList;
	}

	// This method is used through introspection
	public List<SelectBaseString> getSearchBuilderParameterList(boolean nullable) {
		List<SelectBaseString> searchBuilderParameterList = getSearchBuilderParameterList();
		if (nullable)
			searchBuilderParameterList.add(0, new SelectBaseString("", ""));
		return searchBuilderParameterList;
	}

	// This method is used through introspection
	public List<SelectBase> getSearchStrategyTypeList() {
		SearchStrategyType[] searchStrategyType = SearchConstant.SearchStrategyType.values();
		List<SelectBase> strategyTypeList = new ArrayList<SelectBase>();
		strategyTypeList.add(new SelectBase("", ""));
		for (int i = 0; i < searchStrategyType.length; i++) {
			strategyTypeList.add(new SelectBase(searchStrategyType[i].toString(), searchStrategyType[i].toString()));
		}
		return strategyTypeList;
	}

	// This method is used through introspection
	public List<Selectable> getSectionList() {
		List<Selectable> sectionList = new ArrayList<Selectable>();
		sectionList.add(new SelectBaseString("filter", messageUtil.findMessage("label.widget.section.filter")));
		sectionList.add(new SelectBaseString("select", messageUtil.findMessage("label.widget.section.select")));
		sectionList.add(new SelectBaseString("group", messageUtil.findMessage("label.widget.section.group")));
		return sectionList;
	}

	// This method is used through introspection
	public List<Selectable> getStringMatchTypeList() {
		StringMatchType[] StringMatchType = SearchConstant.StringMatchType.values();
		List<Selectable> stringMatchTypeList = new ArrayList<Selectable>();
		for (int i = 0; i < StringMatchType.length; i++)
			stringMatchTypeList.add(new SelectBaseString(StringMatchType[i].toString(), StringMatchType[i].toString()));
		stringMatchTypeList.add(0, new SelectBaseString("", messageUtil.findMessage("label.widget.selezionare")));
		return stringMatchTypeList;
	}

	public void setSearchBuilderDao(SearchBuilderDao searchBuilderDao) {
		this.searchBuilderDao = searchBuilderDao;
	}

	public void setSearchBuilderWidgetLinkDao(SearchBuilderWidgetLinkDao searchBuilderWidgetLinkDao) {
		this.searchBuilderWidgetLinkDao = searchBuilderWidgetLinkDao;
	}

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

}
