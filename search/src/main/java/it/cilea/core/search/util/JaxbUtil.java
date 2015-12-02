package it.cilea.core.search.util;

import it.cilea.core.dto.SearchResult;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.SearchConstant.RequestStandardParameter;
import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;
import it.cilea.core.spring.CoreSpringConstant;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.jaxb.JAXBContextProperties;

public class JaxbUtil {
	public static final Writer getRestMarkup(SearchStrategy strategy, SearchBuilder searchBuilder,
			HttpServletRequest request, HttpServletResponse response, String outputType, SearchService searchService)
			throws Exception {

		String currentServletUrl = (String) request.getAttribute(CoreSpringConstant.SPRING_ATTRIBUTE_URL);
		if (currentServletUrl.contains(";jsessionid"))
			currentServletUrl = currentServletUrl.substring(0, currentServletUrl.indexOf(";jsessionid"));

		List<InputStream> bindingList = new ArrayList<InputStream>();
		loadStreamXml(
				searchBuilder,
				bindingList,
				searchBuilder.getSearchBuilderParameterMap().get(
						SearchBuilderParameterName.JAXB_SEARCH_RESULT_BINDING.toString()));

		String jaxbModelBindingCsv = searchBuilder.getSearchBuilderParameterMap().get(
				SearchBuilderParameterName.JAXB_MODEL_BINDING.toString());
		if (StringUtils.isNotBlank(jaxbModelBindingCsv)) {
			String[] jaxbModelBindingList = jaxbModelBindingCsv.split(",");
			for (String jaxbModelBinding : jaxbModelBindingList) {
				loadStreamXml(searchBuilder, bindingList, jaxbModelBinding.trim());
			}
		}

		Map<String, Object> properties = getJaxbProperties(currentServletUrl, outputType, bindingList, searchBuilder);
		JAXBContext ctx = JAXBContext.newInstance(new Class[] { SearchResult.class }, properties);

		Marshaller marshaller = ctx.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		Writer output = new StringWriter();

		SearchStrategyData strategyData = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
		marshaller
				.marshal(
						buildDto(strategy, searchBuilder, searchService, isSelectable(currentServletUrl), strategyData),
						output);
		closeStreamXml(bindingList);
		return output;
	}

	private static Object buildDto(SearchStrategy strategy, SearchBuilder searchBuilder, SearchService searchService,
			boolean selectable, SearchStrategyData strategyData) {

		List<?> results = null;
		Long count = new Long(0);
		try {
			results = strategy.getResult(strategyData);
			count = strategy.getCount(strategyData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer pageSize = strategyData.getPageSize();
		Integer page = strategyData.getPage();
		Map<String, Collection<String>> requestParameters = strategyData.getParameterMap();
		Collection<String> sort = strategyData.getParameterValues(RequestStandardParameter.SORT.value());
		if (sort == null)
			sort = new ArrayList<String>();
		String sortDir = strategyData.getSortDirection();
		if (!selectable) {
			return new SearchResult(count, results, requestParameters, pageSize, page, sort, sortDir);
		} else
			return results;
	}

	private static void loadStreamXml(SearchBuilder searchBuilder, List<InputStream> multipleBindings,
			String bindingPath) {
		ClassLoader classLoader = JaxbUtil.class.getClassLoader();
		if (StringUtils.isNotEmpty(bindingPath)) {
			InputStream stream = classLoader.getResourceAsStream(bindingPath);
			if (stream != null)
				multipleBindings.add(stream);
			else
				throw new RuntimeException("It's not possible retrieve the widget: " + bindingPath + " stream ");
		}
	}

	private static Map<String, Object> getJaxbProperties(String currentServletUrl, String outputType,
			List<InputStream> bindingList, SearchBuilder searchBuilder) throws Exception {
		Map<String, Object> properties = new HashMap<String, Object>();
		if (CollectionUtils.isEmpty(bindingList)) {
			String className = searchBuilder.getSearchBuilderParameterMap().get(
					SearchBuilderParameterName.ROOT_MODEL_CLASS.toString());
			if (StringUtils.isNotBlank(className)) {
				loadStreamXml(searchBuilder, bindingList, it.cilea.core.jaxb.util.JaxbUtil.getJaxbBindingFile(
						Class.forName(className), isSelectable(currentServletUrl)));
			}
		}
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, bindingList);
		if (isJson(outputType)) {
			properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
			properties.put(JAXBContextProperties.JSON_WRAPPER_AS_ARRAY_NAME, true);
			if (isSelectable(currentServletUrl)) {
				properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
			}
		}
		return properties;
	}

	private static boolean isJson(String outputType) {
		if ("json".equals(outputType))
			return true;
		else
			return false;
	}

	private static boolean isSelectable(String servletUrl) {
		if (servletUrl.contains(SearchConstant.SELECTABLE))
			return true;
		else
			return false;
	}

	private static void closeStreamXml(List<InputStream> multipleBindings) {
		for (InputStream is : multipleBindings) {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
