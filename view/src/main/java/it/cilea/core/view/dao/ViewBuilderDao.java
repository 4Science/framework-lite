package it.cilea.core.view.dao;

import it.cilea.core.model.Selectable;
import it.cilea.core.view.model.ViewBuilder;
import it.cilea.core.spring.dao.GenericDao;

import java.util.List;

public interface ViewBuilderDao extends GenericDao<ViewBuilder, Integer> {
	List<ViewBuilder> initViewBuilderList();

	Object mergeObjects(Object o);

	List<Selectable> getSelectableFromHql(String hqlQuery);
}
