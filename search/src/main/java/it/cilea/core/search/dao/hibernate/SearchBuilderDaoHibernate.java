package it.cilea.core.search.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import it.cilea.core.model.Selectable;
import it.cilea.core.search.dao.SearchBuilderDao;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.model.SearchBuilderParameter;
import it.cilea.core.search.model.SearchBuilderWidgetLink;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;
import it.cilea.core.widget.WidgetConstant;

@Repository("searchBuilderDao")
public class SearchBuilderDaoHibernate extends GenericDaoHibernate<SearchBuilder, Integer> implements SearchBuilderDao {
	public SearchBuilderDaoHibernate() {
		super(SearchBuilder.class);
	}

	public List<SearchBuilder> initSearchBuilderList() {
		String queryString;
		Query query;
		queryString = "from SearchBuilder";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		Map<Integer, SearchBuilder> searchBuilderMap = new HashMap<Integer, SearchBuilder>();
		for (SearchBuilder searchBuilder : (List<SearchBuilder>) query.list()) {
			searchBuilder.setSearchBuilderParameterSet(new HashSet<SearchBuilderParameter>());
			searchBuilder.setSearchBuilderWidgetLinkSet(new TreeSet<SearchBuilderWidgetLink>());
			searchBuilderMap.put(searchBuilder.getId(), searchBuilder);
		}
		queryString = "from SearchBuilderParameter";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (SearchBuilderParameter searchBuilderParameter : (List<SearchBuilderParameter>) query.list()) {
			if (searchBuilderMap.containsKey(searchBuilderParameter.getSearchBuilderId()))
				searchBuilderMap.get(searchBuilderParameter.getSearchBuilderId()).getSearchBuilderParameterSet()
						.add(searchBuilderParameter);
		}
		queryString = "from SearchBuilderWidgetLink";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (SearchBuilderWidgetLink searchBuilderWidgetLink : (List<SearchBuilderWidgetLink>) query.list()) {
			searchBuilderWidgetLink.setWidget(WidgetConstant.widgetMap.get(searchBuilderWidgetLink.getWidgetId()));
			if (searchBuilderMap.containsKey(searchBuilderWidgetLink.getSearchBuilderId()))
				searchBuilderMap.get(searchBuilderWidgetLink.getSearchBuilderId()).getSearchBuilderWidgetLinkSet()
						.add(searchBuilderWidgetLink);
		}
		for (Integer widgetId : WidgetConstant.widgetMap.keySet()) {
			try {
				WidgetConstant.widgetMap.get(widgetId).init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (Integer searchBuilderId : searchBuilderMap.keySet()) {
			searchBuilderMap.get(searchBuilderId).init();
		}
		List<SearchBuilder> list = new ArrayList<SearchBuilder>();
		for (Integer searchBuilderId : searchBuilderMap.keySet())
			list.add(searchBuilderMap.get(searchBuilderId));
		return list;

	}

	public Object mergeObjects(Object o) {
		return getSessionFactory().getCurrentSession().merge(o);

	}

	public List<Selectable> getSelectableFromHql(String hqlQuery) {
		Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
		return query.list();
	}

	@Override
	public SearchBuilderParameter saveParameter(SearchBuilderParameter searchBuilderParameter) {
		if (searchBuilderParameter.getId() == null) {
			getSessionFactory().getCurrentSession().save(searchBuilderParameter);
			return searchBuilderParameter;
		} else
			return (SearchBuilderParameter) getSessionFactory().getCurrentSession().merge(searchBuilderParameter);
	}
}
