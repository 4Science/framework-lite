package it.cilea.core.search.strategy.impl.olap;

import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

public class OlapSearchStrategy implements SearchStrategy {

	protected ApplicationContext context;

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public List<?> getResult(SearchStrategyData d) throws Exception {

		throw new UnsupportedOperationException();
	}

	@Override
	public Long getCount(SearchStrategyData d) throws Exception {

		throw new UnsupportedOperationException();
	}

	public Map getModel(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			SearchService searchService) throws Exception {
		Map model = new HashMap();
		model.put("start", "1");

		request.getSession().setAttribute("olapActionPath",
				request.getAttribute("org.springframework.web.servlet.HandlerMapping.pathWithinHandlerMapping"));

		request.getSession().removeAttribute("toolbar01");
		request.getSession().removeAttribute("jndi");

		Map<String, String> defaultOlapConfigurationMap = new HashMap<String, String>();
		defaultOlapConfigurationMap.put("cube", "true");
		defaultOlapConfigurationMap.put("mdx", "true");
		defaultOlapConfigurationMap.put("sort", "true");
		defaultOlapConfigurationMap.put("showParent", "true");
		defaultOlapConfigurationMap.put("hideSpan", "true");
		defaultOlapConfigurationMap.put("property", "true");
		defaultOlapConfigurationMap.put("nonEmpty", "true");
		defaultOlapConfigurationMap.put("swapAxes", "true");
		defaultOlapConfigurationMap.put("drillMember", "true");
		defaultOlapConfigurationMap.put("drillPosition", "true");
		defaultOlapConfigurationMap.put("drillReplace", "true");
		defaultOlapConfigurationMap.put("drillThrough", "true");
		defaultOlapConfigurationMap.put("chartButton", "true");
		defaultOlapConfigurationMap.put("chartProperties", "true");
		defaultOlapConfigurationMap.put("printProperties", "true");
		defaultOlapConfigurationMap.put("printpdf", "true");
		defaultOlapConfigurationMap.put("printxls", "true");
		defaultOlapConfigurationMap.put("saveButton", "true");

		defaultOlapConfigurationMap.putAll(searchBuilder.getSearchBuilderParameterMap());
		searchBuilder.getSearchBuilderParameterMap().putAll(defaultOlapConfigurationMap);

		request.getSession().setAttribute("searchBuilder", searchBuilder);
		OlapConfiguration olapConfiguration = (OlapConfiguration) context.getBean(searchBuilder.getSearchBuilderParameterMap()
				.get("beanConfiguration"));
		String mdxQuery = searchBuilder.getSearchBuilderParameterMap().get("mdxQuery");
		mdxQuery = olapConfiguration.getMdxQuery(mdxQuery, request);
		request.getSession().setAttribute("mdxQuery", mdxQuery);

		return model;
	}

	@Override
	public Writer getRestMarkup(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			String outputType, SearchService searchService) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSql(SearchStrategyData d) throws Exception {
		throw new UnsupportedOperationException("getCount MUST BE invoked after getList");
	}

}
