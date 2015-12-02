package it.cilea.core.widget.strategy.options;

import it.cilea.core.model.Selectable;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.OptionsWidget;
import it.cilea.core.widget.service.WidgetService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class OptionsWidgetHQLPopulateStrategy extends OptionsWidgetPopulateStrategy {

	public OptionsWidgetHQLPopulateStrategy(OptionsWidget widget, HttpServletRequest request) {
		super(widget, request);
	}

	public List<? extends Selectable> getOptions() {
		String populationValue = widget.getPopulationValue();
		if (StringUtils.isBlank(populationValue) || !StringUtils.contains(populationValue, "[HQL_QUERY:")
				|| !StringUtils.contains(populationValue, "[HQL_QUERY_IDS:"))
			throw new IllegalStateException(
					"OptionsWidgetHQLPopulateStrategy expects a populationValue with an [HQL_QUERY:${HQL_QUERY}][HQL_QUERY_IDS:${HQL_QUERY_IDS}]");
		WidgetService widgetService = WidgetConstant.beanFactory.getBean("widgetService", WidgetService.class);
		String[] piece = StringUtils.splitByWholeSeparator(populationValue, "][HQL_QUERY_IDS:");
		String hqlQuery = StringUtils.removeStart(piece[0], "[HQL_QUERY:");

		List<Selectable> globalList = new ArrayList<Selectable>();
		if (widget.getRenderEmptyOption()) {
			globalList.add(widget.getSelectableEmptyOption());
		}

		List<Selectable> resultList = null;
		try {
			resultList = widgetService.getSelectableFromHql(hqlQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
		globalList.addAll(resultList);
		return globalList;
	}

	public List<? extends Selectable> getOptions(String[] ids) {
		List<Selectable> defaultSelectable = new ArrayList<Selectable>();
		if (ArrayUtils.isEmpty(ids) || (ids.length == 1 && StringUtils.isBlank(ids[0])))
			return defaultSelectable;
		String populationValue = widget.getPopulationValue();
		if (StringUtils.isBlank(populationValue) || !StringUtils.contains(populationValue, "[HQL_QUERY:")
				|| !StringUtils.contains(populationValue, "[HQL_QUERY_IDS:"))
			throw new IllegalStateException(
					"OptionsWidgetHQLPopulateStrategy expects a populationValue with an [HQL_QUERY:${HQL_QUERY}][HQL_QUERY_IDS:${HQL_QUERY_IDS}]");
		WidgetService widgetService = WidgetConstant.beanFactory.getBean("widgetService", WidgetService.class);
		String[] piece = StringUtils.splitByWholeSeparator(populationValue, "][HQL_QUERY_IDS:");

		String hqlQueryIds = StringUtils.removeEnd(piece[1], "]");
		if (hqlQueryIds.contains("(?)")) {
			hqlQueryIds = StringUtils.replace(hqlQueryIds, "(?)", StringUtils.join(ids, ","));
		} else if (hqlQueryIds.contains("('?')")) {
			hqlQueryIds = StringUtils.replace(hqlQueryIds, "('?')", "('" + StringUtils.join(ids, "','") + "')");
		} else
			throw new IllegalStateException(
					"OptionsWidgetHQLPopulateStrategy expects a populationValue with an [HQL_QUERY:${HQL_QUERY}][HQL_QUERY_IDS:${HQL_QUERY_IDS}] and HQL_QUERY_IDS muST contain one of (?) or ('?') ");
		try {
			return widgetService.getSelectableFromHql(hqlQueryIds);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return defaultSelectable;

	}

}