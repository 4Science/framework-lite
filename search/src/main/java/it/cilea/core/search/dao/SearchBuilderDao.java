package it.cilea.core.search.dao;

import java.util.List;

import it.cilea.core.model.Selectable;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.model.SearchBuilderParameter;
import it.cilea.core.spring.dao.GenericDao;

public interface SearchBuilderDao extends GenericDao<SearchBuilder, Integer> {
	List<SearchBuilder> initSearchBuilderList();

	Object mergeObjects(Object o);

	List<Selectable> getSelectableFromHql(String hqlQuery);

	SearchBuilderParameter saveParameter(SearchBuilderParameter searchBuilderParameter);
}
