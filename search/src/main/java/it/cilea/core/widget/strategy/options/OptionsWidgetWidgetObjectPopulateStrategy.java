package it.cilea.core.widget.strategy.options;

import it.cilea.core.model.Selectable;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;
import it.cilea.core.widget.model.OptionsWidget;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SuppressWarnings("unchecked")
public class OptionsWidgetWidgetObjectPopulateStrategy extends OptionsWidgetPopulateStrategy {
	/*
	 * this strategy calls a widget search builder for the retrieval of the list
	 */
	public OptionsWidgetWidgetObjectPopulateStrategy(OptionsWidget widget, HttpServletRequest request) {
		super(widget, request);
	}

	public List<? extends Selectable> getOptions() {
		String populationValue = widget.getPopulationValue();
		if (StringUtils.isBlank(populationValue))
			throw new IllegalStateException(
					"OptionsWidgetWidgetObjectPopulateStrategy expects a populationValue with this pattern WIDGET_URL");
		SearchBuilder searchBuilder = SearchConstant.searchBuilderMap.get(populationValue);
		// TODO IF ANY PARAMETERS NAME ARE SPECIFIED THEN YOU MUST SET IN
		// REQUEST
		// request.setAttribute("wfItemYear", year);
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		SearchStrategy strategy = SearchStrategyFactory.getStrategy(searchBuilder, request);
		SearchService searchService = (SearchService) request.getServletContext().getAttribute("searchService");

		Object searchBuilderIdentifierNAMEBefore = request.getAttribute(SearchConstant.SearchBuilderIdentifier.NAME
				.value());
		request.setAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value(), populationValue);
		SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
		if (searchBuilderIdentifierNAMEBefore != null)
			request.setAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value(), searchBuilderIdentifierNAMEBefore);
		else
			request.removeAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value());
		List<Selectable> selectableList = new ArrayList<Selectable>();
		try {
			selectableList = (List<Selectable>) strategy.getResult(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectableList;
	}

	public List<? extends Selectable> getOptions(String[] ids) {
		List<Selectable> defaultSelectable = new ArrayList<Selectable>();
		// TODO
		return defaultSelectable;

	}
}