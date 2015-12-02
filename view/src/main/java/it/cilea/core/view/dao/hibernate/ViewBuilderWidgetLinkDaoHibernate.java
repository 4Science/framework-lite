package it.cilea.core.view.dao.hibernate;

import it.cilea.core.view.dao.ViewBuilderWidgetLinkDao;
import it.cilea.core.view.model.ViewBuilderWidgetLink;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

import org.springframework.stereotype.Repository;

@Repository("viewBuilderWidgetLinkDao")
public class ViewBuilderWidgetLinkDaoHibernate extends GenericDaoHibernate<ViewBuilderWidgetLink, Integer>
		implements ViewBuilderWidgetLinkDao {
	public ViewBuilderWidgetLinkDaoHibernate() {
		super(ViewBuilderWidgetLink.class);
	}

}
