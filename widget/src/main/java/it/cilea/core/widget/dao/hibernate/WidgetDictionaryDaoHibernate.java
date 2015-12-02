package it.cilea.core.widget.dao.hibernate;

import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;
import it.cilea.core.widget.dao.WidgetDictionaryDao;
import it.cilea.core.widget.model.WidgetDictionary;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("widgetDictionaryDao")
public class WidgetDictionaryDaoHibernate extends GenericDaoHibernate<WidgetDictionary, Integer> implements
		WidgetDictionaryDao {
	public WidgetDictionaryDaoHibernate() {
		super(WidgetDictionary.class);
	}

	@Override
	public void deleteWidgetDictionary(Collection<Integer> idCollection) {
		String querySql = "delete from WidgetDictionary where id  in ("
				+ StringUtils.join(idCollection.iterator(), ",") + ") ";
		Query query = getSessionFactory().getCurrentSession().createQuery(querySql);
		query.executeUpdate();
	}

}
