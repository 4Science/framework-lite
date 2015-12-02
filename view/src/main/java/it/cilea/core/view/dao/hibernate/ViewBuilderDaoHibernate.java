package it.cilea.core.view.dao.hibernate;

import it.cilea.core.model.Selectable;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;
import it.cilea.core.view.dao.ViewBuilderDao;
import it.cilea.core.view.model.ViewBuilder;
import it.cilea.core.view.model.ViewBuilderParameter;
import it.cilea.core.view.model.ViewBuilderValidator;
import it.cilea.core.view.model.ViewBuilderWidgetLink;
import it.cilea.core.widget.WidgetConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

@Repository("viewBuilderDao")
public class ViewBuilderDaoHibernate extends GenericDaoHibernate<ViewBuilder, Integer> implements ViewBuilderDao {
	@Autowired
	private ApplicationContext applicationContext;

	public ViewBuilderDaoHibernate() {
		super(ViewBuilder.class);
	}

	public List<ViewBuilder> initViewBuilderList() {
		String queryString;
		Query query;
		queryString = "from ViewBuilder";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		Map<Integer, ViewBuilder> viewBuilderMap = new HashMap<Integer, ViewBuilder>();
		for (ViewBuilder viewBuilder : (List<ViewBuilder>) query.list()) {
			viewBuilder.setViewBuilderParameterSet(new HashSet<ViewBuilderParameter>());
			viewBuilder.setViewBuilderWidgetLinkSet(new TreeSet<ViewBuilderWidgetLink>());
			viewBuilder.setViewBuilderValidatorSet(new TreeSet<ViewBuilderValidator>());
			viewBuilderMap.put(viewBuilder.getId(), viewBuilder);
		}
		queryString = "from ViewBuilderParameter";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (ViewBuilderParameter viewBuilderParameter : (List<ViewBuilderParameter>) query.list()) {
			if (viewBuilderMap.containsKey(viewBuilderParameter.getViewBuilderId()))
				viewBuilderMap.get(viewBuilderParameter.getViewBuilderId()).getViewBuilderParameterSet()
						.add(viewBuilderParameter);
		}
		queryString = "from ViewBuilderValidator";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (ViewBuilderValidator viewBuilderValidator : (List<ViewBuilderValidator>) query.list()) {
			try {
				viewBuilderValidator.init(applicationContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (viewBuilderMap.containsKey(viewBuilderValidator.getViewBuilderId()))
				viewBuilderMap.get(viewBuilderValidator.getViewBuilderId()).getViewBuilderValidatorSet()
						.add(viewBuilderValidator);
		}
		queryString = "from ViewBuilderWidgetLink";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (ViewBuilderWidgetLink viewBuilderWidgetLink : (List<ViewBuilderWidgetLink>) query.list()) {
			viewBuilderWidgetLink.setWidget(WidgetConstant.widgetMap.get(viewBuilderWidgetLink.getWidgetId()));
			if (viewBuilderMap.containsKey(viewBuilderWidgetLink.getViewBuilderId()))
				viewBuilderMap.get(viewBuilderWidgetLink.getViewBuilderId()).getViewBuilderWidgetLinkSet()
						.add(viewBuilderWidgetLink);
		}
		for (Integer widgetId : WidgetConstant.widgetMap.keySet()) {
			try {
				WidgetConstant.widgetMap.get(widgetId).init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (Integer viewBuilderId : viewBuilderMap.keySet()) {
			viewBuilderMap.get(viewBuilderId).init();
		}
		List<ViewBuilder> list = new ArrayList<ViewBuilder>();
		for (Integer viewBuilderId : viewBuilderMap.keySet())
			list.add(viewBuilderMap.get(viewBuilderId));
		return list;

	}

	public Object mergeObjects(Object o) {
		return getSessionFactory().getCurrentSession().merge(o);

	}

	public List<Selectable> getSelectableFromHql(String hqlQuery) {
		Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
		return query.list();
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
