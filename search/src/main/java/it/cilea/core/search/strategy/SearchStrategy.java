package it.cilea.core.search.strategy;

import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

public interface SearchStrategy {
	public List<?> getResult(SearchStrategyData data) throws Exception;

	public Long getCount(SearchStrategyData data) throws Exception;

	public Map getModel(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			SearchService searchService) throws Exception;

	public void setContext(ApplicationContext context);

	public Writer getRestMarkup(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			String outputType, SearchService searchService) throws Exception;

	public String getSql(SearchStrategyData d) throws Exception;

}
