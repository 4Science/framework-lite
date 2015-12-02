package it.cilea.core.widget.dao;

import it.cilea.core.spring.dao.GenericDao;
import it.cilea.core.widget.model.WidgetDictionary;

import java.util.Collection;

public interface WidgetDictionaryDao extends GenericDao<WidgetDictionary, Integer> {
	void deleteWidgetDictionary(Collection<Integer> ids);

}
