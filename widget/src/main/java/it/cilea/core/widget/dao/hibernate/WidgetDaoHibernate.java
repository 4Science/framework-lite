package it.cilea.core.widget.dao.hibernate;

import it.cilea.core.model.Selectable;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;
import it.cilea.core.widget.dao.WidgetDao;
import it.cilea.core.widget.model.Parameter;
import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.model.WidgetDictionary;
import it.cilea.core.widget.model.WidgetLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("widgetDao")
public class WidgetDaoHibernate extends GenericDaoHibernate<Widget, Integer> implements WidgetDao {
	public WidgetDaoHibernate() {
		super(Widget.class);
	}

	public Object mergeObjects(Object o) {
		return getSessionFactory().getCurrentSession().merge(o);

	}

	public List<Selectable> getSelectableFromHql(String hqlQuery) {
		Query query = getSessionFactory().getCurrentSession().createQuery(hqlQuery);
		return query.list();
	}

	public List<Widget> initWidgetList() {
		String queryString = "from Widget";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		Map<Integer, Widget> widgetMap = new HashMap<Integer, Widget>();
		for (Widget widget : (List<Widget>) query.list()) {
			widget.setWidgetDictionarySet(new TreeSet<WidgetDictionary>());
			widget.setParameterSet(new TreeSet<Parameter>());
			widget.setChildWidgetLinkSet(new TreeSet<WidgetLink>());
			widget.setParentWidgetLinkSet(new TreeSet<WidgetLink>());
			widget.setChildWidgetSet(new HashSet<Widget>());
			widgetMap.put(widget.getId(), widget);
		}
		for (Integer widgetId : widgetMap.keySet()) {
			if (widgetMap.get(widgetId).getParentWidget() != null)
				widgetMap.get(widgetId).getParentWidget().getChildWidgetSet().add(widgetMap.get(widgetId));
		}
		queryString = "from Parameter";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (Parameter parameter : (List<Parameter>) query.list()) {
			if (widgetMap.containsKey(parameter.getWidgetId()))
				widgetMap.get(parameter.getWidgetId()).getParameterSet().add(parameter);
		}
		queryString = "select distinct widgetDictionary from WidgetDictionary widgetDictionary";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (WidgetDictionary widgetDictionary : (List<WidgetDictionary>) query.list()) {
			if (widgetMap.containsKey(widgetDictionary.getWidgetId()))
				widgetMap.get(widgetDictionary.getWidgetId()).getWidgetDictionarySet().add(widgetDictionary);
		}
		queryString = "from WidgetLink";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (WidgetLink widgetLink : (List<WidgetLink>) query.list()) {
			Widget parent = widgetMap.get(widgetLink.getParentId());
			Widget child = widgetMap.get(widgetLink.getChildId());
			widgetLink.setChild(child);
			widgetLink.setParent(parent);
			parent.getChildWidgetLinkSet().add(widgetLink);
			child.getParentWidgetLinkSet().add(widgetLink);
		}
		for (Integer widgetId : widgetMap.keySet()) {
			try {
				widgetMap.get(widgetId).init();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		List<Widget> list = new ArrayList<Widget>();
		for (Integer widgetId : widgetMap.keySet())
			list.add(widgetMap.get(widgetId));
		return list;

	}

}
