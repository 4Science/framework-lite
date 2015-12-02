package it.cilea.core.search.dao;

import it.cilea.core.model.Selectable;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.spring.dao.GenericDao;

import java.util.List;

public interface SearchBuilderDao extends GenericDao<SearchBuilder, Integer> {
	List<SearchBuilder> initSearchBuilderList();

	Object mergeObjects(Object o);

	List<Selectable> getSelectableFromHql(String hqlQuery);
}
