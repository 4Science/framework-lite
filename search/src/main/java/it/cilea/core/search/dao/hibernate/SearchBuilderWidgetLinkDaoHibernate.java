package it.cilea.core.search.dao.hibernate;

import it.cilea.core.search.dao.SearchBuilderWidgetLinkDao;
import it.cilea.core.search.model.SearchBuilderWidgetLink;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

@Repository("searchBuilderWidgetLinkDao")
public class SearchBuilderWidgetLinkDaoHibernate extends GenericDaoHibernate<SearchBuilderWidgetLink, Integer>
		implements SearchBuilderWidgetLinkDao {
	public SearchBuilderWidgetLinkDaoHibernate() {
		super(SearchBuilderWidgetLink.class);
	}

}
