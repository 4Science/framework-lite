package it.cilea.core.search.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.model.SearchBuilderParameter;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.util.SearchUtil;
import it.cilea.core.spring.CoreSpringConstant;
import it.cilea.core.spring.controller.Spring3CoreController;
import it.cilea.core.util.HttpUtil;
import it.cilea.core.widget.service.WidgetService;

@Controller
public class SearchController extends Spring3CoreController {

	@Autowired
	protected SearchService searchService;
	@Autowired
	private WidgetService widgetService;

	@RequestMapping(value = { "/search/reload.htm" })
	public ModelAndView Reload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SearchUtil.reload(widgetService, searchService);
		saveMessage(request, messageUtil.findMessage("action.widget.reload"));
		return new ModelAndView("redirect:/");
	}

	@RequestMapping("/search/reload.fragment")
	public void reload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SearchUtil.reload(widgetService, searchService);
		response.getWriter().println("<html>ok</html>");
	}

	@RequestMapping(value = { "**/*/widgetSearch.{outputType:ajax|htm|fragment}" })
	public ModelAndView handleWebRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		SearchBuilder searchBuilder = getSearchBuilder(request);
		SearchStrategy strategy = SearchStrategyFactory.getStrategy(searchBuilder, request);

		Map model = new HashMap();
		try {
			model = strategy.getModel(searchBuilder, request, response, searchService);
			if (model == null)
				return null;

		} catch (ValidationException e) {

			String[] errorsParam = e.getMessage().split("___")[0].split(",");
			String[] paramErrorCause = e.getMessage().split("___")[1].split(",");
			String[] errorMessage = e.getMessage().split("___")[2].split(",");
			String[] paramName = e.getMessage().split("___")[3].split(",");

			boolean skipDetailedMesg = true;

			for (int i = 0; i < errorsParam.length; i++) {
				if (!"notNull".equals(paramErrorCause[i])) {
					skipDetailedMesg = false;
				}
			}

			if (skipDetailedMesg) {
				saveMessage(request, messageUtil.findMessage("error.widget.notnull"));
			} else {

				for (int i = 0; i < errorsParam.length; i++) {

					String[] label = new String[paramName[i].split("-").length];
					String[] labelValue = new String[paramName[i].split("-").length];
					String labelValueString = "";

					for (int j = 0; j < paramName[i].split("-").length; j++) {
						label[j] = "widget" + (request.getRequestURI().replace("/widgetSearch.ajax", "")
								.replace("/widgetSearch.htm", "").replace("/widgetSearch.fragment", "")
								.replace("/", ".")) + "." + paramName[i].split("-")[j] + ".widget.label";
						labelValue[j] = messageUtil.findMessage(label[j]);
						labelValueString += labelValue[j] + ",";
					}
					labelValueString = StringUtils.removeEnd(labelValueString, ",");

					saveError(request, labelValueString + ": " + messageUtil.findMessage(errorMessage[i]));
				}
			}
		}
		return new ModelAndView(searchBuilder.getView(), model);

	}

	@RequestMapping(value = { "**/*/widgetSearch.{outputType:json|xml}" })
	public void handleRestRequest(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String outputType) throws Exception {
		SearchBuilder searchBuilder = getSearchBuilder(request);
		SearchStrategy strategy = SearchStrategyFactory.getStrategy(searchBuilder, request);

		Writer output = strategy.getRestMarkup(searchBuilder, request, response, outputType, searchService);
		output = applyTransformer(output, outputType, searchBuilder, request);
		response.setContentType(HttpUtil.getMimeTypeHeader(outputType));
		response.getWriter().print(output);
		output.close();
	}

	private SearchBuilder getSearchBuilder(HttpServletRequest request) {
		Map<String, SearchBuilder> searchBuilderMap = SearchConstant.searchBuilderMap;
		String currentServletUrl = (String) request.getAttribute(CoreSpringConstant.SPRING_ATTRIBUTE_URL);

		if (currentServletUrl.contains(";jsessionid"))
			currentServletUrl = currentServletUrl.substring(0, currentServletUrl.indexOf(";jsessionid"));

		SearchBuilder searchBuilder = searchBuilderMap.get(currentServletUrl);

		if (searchBuilder == null)
			throw new IllegalStateException("A SearchBuilder Object MUST be bound with this URL: " + currentServletUrl);

		return searchBuilder;
	}

	private Writer applyTransformer(Writer source, String outputType, SearchBuilder searchBuilder,
			HttpServletRequest request) throws Exception {

		String jsonStyle = request.getParameter(SearchConstant.JSON_REQUEST_PARAMETER);
		String jsonJavascript = searchBuilder.getSearchBuilderParameterMap()
				.get(SearchBuilderParameterName.JSON_JAVASCRIPT.toString());

		if (isJson(outputType) && StringUtils.isEmpty(jsonStyle) && StringUtils.isNotEmpty(jsonJavascript)) {
			return jsonTransformer(source, jsonJavascript);
		} else if (isJson(outputType))
			return source;

		String streamXslFilePath = searchBuilder.getSearchBuilderParameterMap()
				.get(SearchBuilderParameterName.XSL_FILE.toString());
		if (StringUtils.isNotEmpty(request.getParameter(SearchConstant.XSL_REQUEST_PARAMETER)))
			streamXslFilePath = request.getParameter(SearchConstant.XSL_REQUEST_PARAMETER);

		if (StringUtils.isNotEmpty(streamXslFilePath)) {
			InputStream xslStream = request.getServletContext().getResourceAsStream(streamXslFilePath);
			Writer transformedXml = xslTransformer(new StringReader(source.toString()), xslStream);
			xslStream.close();
			return transformedXml;
		} else {
			return source;
		}
	}

	private Writer jsonTransformer(Writer source, String convertJson)
			throws ScriptException, NoSuchMethodException, IOException {
		String jsonInput = source.toString();
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");
		engine.put("messageUtil", messageUtil);
		engine.put("jsonInput", jsonInput);
		String convertedJson = (String) engine.eval(SearchConstant.JSON_STRINGIFY_FUNCTION + ";" + convertJson);
		StringReader reader = new StringReader(convertedJson);
		StringWriter out = new StringWriter();
		IOUtils.copy(reader, out);
		return out;
	}

	private Writer xslTransformer(StringReader sourceXml, InputStream xsl) throws Exception {
		if (sourceXml == null || xsl == null)
			throw new IllegalArgumentException("sourceXml and xsl MUST NOT BE null. Check if files exist");
		Writer resultXml = new StringWriter();
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer(new StreamSource(xsl));
		transformer.transform(new StreamSource(sourceXml), new StreamResult(resultXml));
		sourceXml.close();
		xsl.close();
		return resultXml;
	}

	private boolean isJson(String outputType) {
		if ("json".equals(outputType))
			return true;
		else
			return false;
	}

	public void setSearchService(SearchService searchService) {
		this.searchService = searchService;
	}

	public void setWidgetService(WidgetService widgetService) {
		this.widgetService = widgetService;
	}

	@RequestMapping(value = { "/search/init" })
	public ModelAndView init(HttpServletRequest request) throws Exception {

		if (!SearchConstant.searchBuilderMap.containsKey("/item/widgetSearch.htm")) {
			SearchBuilder searchBuilder = new SearchBuilder();
			searchBuilder.setName("/item/widgetSearch.htm");
			searchBuilder.setUrl("/item/widgetSearch.htm");
			searchBuilder.setFilterClause("1=1");
			searchBuilder.setView("item/search");
			searchBuilder.setStrategy(SearchConstant.SearchStrategyType.HQL.name());
			searchService.saveOrUpdate(searchBuilder);
			SearchBuilderParameter rootModelClass = new SearchBuilderParameter();
			rootModelClass.setName(SearchConstant.SearchBuilderParameterName.ROOT_MODEL_CLASS.name());
			rootModelClass.setValue("org.dspace.content.Item");
			rootModelClass.setSearchBuilderId(searchBuilder.getId());
			searchService.saveOrUpdate(rootModelClass);
			SearchBuilderParameter distinctClause = new SearchBuilderParameter();
			distinctClause.setName(SearchConstant.SearchBuilderParameterName.DISTINCT_CLAUSE.name());
			distinctClause.setValue("NO_DISTINCT_CLAUSE");
			distinctClause.setSearchBuilderId(searchBuilder.getId());
			searchService.saveOrUpdate(distinctClause);
			SearchConstant.searchBuilderMap.put(searchBuilder.getUrl(), searchBuilder);

		}

		SearchUtil.reload(widgetService, searchService);
		saveMessage(request, messageUtil.findMessage("action.search.init"));
		return new ModelAndView("redirect:/");
	}

}