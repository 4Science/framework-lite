package it.cilea.core.widget.dao;

import it.cilea.core.model.Selectable;
import it.cilea.core.spring.dao.GenericDao;
import it.cilea.core.widget.model.Widget;

import java.util.List;

public interface WidgetDao extends GenericDao<Widget, Integer> {
	List<Widget> initWidgetList();

	Object mergeObjects(Object o);

	List<Selectable> getSelectableFromHql(String hqlQuery);
}
