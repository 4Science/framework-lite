package it.cilea.core.search.strategy.impl.olap;

import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategyData;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class OlapSearchStrategyData extends SearchStrategyData {

	public OlapSearchStrategyData(SearchBuilder searchBuilder, HttpServletRequest request, SearchService searchService) {
		super(searchBuilder, request, searchService);

		Map<String, String> searchBuilderParameterMap = searchBuilder.getSearchBuilderParameterMap();

	}

}
