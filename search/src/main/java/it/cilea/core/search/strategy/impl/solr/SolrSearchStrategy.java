package it.cilea.core.search.strategy.impl.solr;

import it.cilea.core.configuration.ConfigurationConstant;
import it.cilea.core.displaytag.dto.DisplayTagData;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.SearchConstant.RequestStandardParameter;
import it.cilea.core.search.SearchConstant.SolrQueryType;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;
import it.cilea.core.search.util.JaxbUtil;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.springframework.context.ApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SolrSearchStrategy implements SearchStrategy {
	protected ApplicationContext context;

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	@Override
	public List<?> getResult(SearchStrategyData d) throws Exception {

		SolrSearchStrategyData data = (SolrSearchStrategyData) d;

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		SolrQuery solrQuery = getSolrQuery(data);

		QueryResponse rsp = data.getServer().query(solrQuery);
		data.setQueryResponse(rsp);
		SolrDocumentList docs = rsp.getResults();

		Iterator<SolrDocument> it = docs.iterator();
		while (it.hasNext()) {
			SolrDocument doc = it.next();
			Collection<String> fieldNameList = doc.getFieldNames();

			Map<String, String> singleRecord = new HashMap<String, String>();

			for (String fieldName : fieldNameList) {
				Object fieldValueRaw = doc.getFieldValue(fieldName);
				if (fieldValueRaw instanceof List) {
					String separator = ConfigurationConstant.configurationMap.get("SOLR_MULTIVALUE_FIELD_SEPARATOR");
					if (StringUtils.isBlank(separator))
						separator = "; ";
					singleRecord.put(fieldName, StringUtils.join(((List) fieldValueRaw).iterator(), separator));

				} else if (fieldValueRaw instanceof String) {
					singleRecord.put(fieldName, (String) doc.getFieldValue(fieldName));
				}
			}
			setSnippet(rsp, doc, data, singleRecord);
			resultList.add(singleRecord);
		}
		return resultList;
	}

	@Override
	public Long getCount(SearchStrategyData d) throws Exception {
		SolrSearchStrategyData data = (SolrSearchStrategyData) d;
		if (data.getQueryResponse() == null) {
			throw new UnsupportedOperationException("getCount MUST BE invoked after getList");
		}
		return data.getQueryResponse().getResults().getNumFound();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map getModel(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			SearchService searchService) throws Exception {
		Map model = new HashMap();
		SolrSearchStrategyData data = (SolrSearchStrategyData) SearchStrategyFactory.getStrategyData(searchBuilder,
				request, searchService);

		if (data.getPost()) {
			List resultList = getResult(data);

			DisplayTagData displayTagData = new DisplayTagData(getCount(data).intValue(), resultList,
					data.getDefaultSort(), data.getSortDirection(), data.getPage(), data.getPageSize());
			model.put(SearchConstant.DISPLAY_TAG_DATA, displayTagData);
			// model.put("searchBuilderName", searchBuilder.getName());

			if (data.getFacetedFieldsJs() != null) {
				Map<String, Collection<String>> valueMap = data.getParameterMap();
				Collection<String> filterValueList = valueMap.get(SearchConstant.RequestStandardParameter.FILTER_QUERY
						.value());
				String queryWithFilter = getQueryWithFilter(valueMap);
				String queryWithoutFilter = getQueryWithoutFilter(valueMap);
				model.put(SearchConstant.FACET_DATA, getFacets(data, queryWithFilter));
				model.put(
						SearchConstant.REMOVE_FACET_DATA,
						getRemoveFacetList(filterValueList,
								getFacetDependencyMap(data.getFacetedDependencyFieldsJson()),
								queryWithoutFilter.toString()));
				model.put(SearchConstant.QUERY_STRING, queryWithFilter);
				model.put(SearchConstant.SolrSearchBuilderParameterName.VISIBLE_FACET_NUMBER.value(),
						data.getVisibileFacetNumber());
				model.put(SearchConstant.CARROT_TOPIC, getCarrotTopic(data.getQueryResponse().getResponse()));
			}

		}
		return model;
	}

	private SolrQuery getSolrQuery(SolrSearchStrategyData data) {
		SolrQuery solrQuery = new SolrQuery();

		solrQuery.setFacetMinCount(1);

		solrQuery.setQueryType(data.getQueryType());

		if (SolrQueryType.MORE_LIKE_THIS.value().equals(data.getQueryType())) {
			solrQuery.set("mlt.qf", data.getQueryFields());
			solrQuery.set("mlt.fl", data.getMoreLikeThisFields());
		} else {
			solrQuery.set("pf", data.getPhraseFields());
			solrQuery.set("qf", data.getQueryFields());
			solrQuery.set("fl", data.getResponseFields());
			if (data.getBoostQuery() != null)
				solrQuery.set("bq", data.getBoostQuery().split(","));
		}

		if (data.getFacetedFieldsJs() != null) {
			ScriptEngineManager mgr = new ScriptEngineManager();
			ScriptEngine engine = mgr.getEngineByName("JavaScript");
			try {
				engine.put("data", data);
				Object csvFacetString = engine.eval(data.getFacetedFieldsJs());
				solrQuery.setFacet(true);
				solrQuery.addFacetField(csvFacetString.toString().split(","));
			} catch (ScriptException ex) {
				ex.printStackTrace();
			}
		}

		if (data.getSnippetSize() != null) {
			solrQuery.setHighlight(true);
			solrQuery.setHighlightFragsize(data.getSnippetSize());
		}

		solrQuery.setFilterQueries(data.getSolrFilterQueryList());

		String filterClause = data.getFilterClause();
		if (StringUtils.isNotBlank(filterClause)) {
			int firstBracket = filterClause.indexOf("(");
			int lastBracket = filterClause.lastIndexOf(")");
			if (firstBracket != -1 && lastBracket != -1) {
				data.setFilterClause(filterClause.substring(firstBracket + 1, lastBracket));
			}
			data.setFilterClause(data.getFilterClause().replace("°", "^"));
		} else
			data.setFilterClause("*:*");
		solrQuery.setQuery(data.getFilterClause());
		solrQuery.setRows(data.getPageSize());
		solrQuery.setStart((data.getPage() - 1) * data.getPageSize());
		solrQuery.setIncludeScore(true);

		if (data.getSortFieldList() != null) {
			boolean sortDirectionForEachField = false;
			if (data.getSortDirectionList() != null
					&& data.getSortDirectionList().length == data.getSortFieldList().length) {
				sortDirectionForEachField = true;
			}

			int counter = 0;
			for (String sort : data.getSortFieldList()) {
				ORDER sortDirection = ORDER.asc;
				String sortDirectionString = data.getSortDirection();
				if (sortDirectionForEachField) {
					sortDirectionString = data.getSortDirectionList()[counter++];
				}

				if ("desc".equalsIgnoreCase(sortDirectionString)) {
					sortDirection = ORDER.desc;
				}

				solrQuery.addSortField(sort, sortDirection);
			}
		} else if (StringUtils.isNotBlank(data.getOrderClause())) {
			// pippo
			String[] sortFieldList = StringUtils.split(data.getOrderClause(), ",");
			for (String sortField : sortFieldList) {
				String[] sortFieldAndDirection = StringUtils.split(sortField.trim(), " ");
				if (sortFieldAndDirection.length == 1) {
					solrQuery.addSortField(sortFieldAndDirection[0], ORDER.asc);
				} else if (sortFieldAndDirection.length == 2) {
					solrQuery.addSortField(sortFieldAndDirection[0],
							("desc".equalsIgnoreCase(sortFieldAndDirection[1]) ? ORDER.desc : ORDER.asc));
				}
			}
		}

		return solrQuery;

	}

	private void setSnippet(QueryResponse rsp, SolrDocument doc, SolrSearchStrategyData data,
			Map<String, String> singleRecord) {
		if (rsp.getHighlighting() != null && rsp.getHighlighting().get(doc.getFieldValue(data.getUid())) != null) {
			for (String highlight : data.getHighlightField()) {
				List<String> highlightSnippets = rsp.getHighlighting().get(doc.getFieldValue(data.getUid()))
						.get(highlight);
				if (!CollectionUtils.isEmpty(highlightSnippets)) {
					String strCurrentValueTotal = "";

					for (String str : highlightSnippets) {
						strCurrentValueTotal += " " + str;
					}

					String prevValue = singleRecord.get(SearchConstant.SNIPPET);
					if (prevValue == null) {
						prevValue = strCurrentValueTotal;
					} else {

						Pattern pattern = Pattern.compile("<em>");
						Matcher matcher = pattern.matcher(prevValue);
						int countPrevValue = 0;
						while (matcher.find()) {
							countPrevValue++;
						}

						matcher = pattern.matcher(strCurrentValueTotal);
						int countCurrentValue = 0;
						while (matcher.find()) {
							countCurrentValue++;
						}

						if (countPrevValue < countCurrentValue) {
							prevValue = strCurrentValueTotal;
						}
					}
					singleRecord.put(SearchConstant.SNIPPET, prevValue);
				}
			}
		}
	}

	private String getQueryWithoutFilter(Map<String, Collection<String>> valueMap) throws Exception {
		Set<String> keySet = valueMap.keySet();
		StringBuilder queryWithoutFilter = new StringBuilder();
		StringBuilder queryWithFilter = new StringBuilder();
		for (String key : keySet) {
			for (String value : valueMap.get(key)) {
				queryWithFilter.append("&" + key).append("=").append(URLEncoder.encode(value, "UTF-8"));
				if (!SearchConstant.RequestStandardParameter.FILTER_QUERY.value().equals(key)) {
					queryWithoutFilter.append("&" + key).append("=").append(URLEncoder.encode(value, "UTF-8"));
				}
			}
		}
		return queryWithoutFilter.toString();
	}

	private String getQueryWithFilter(Map<String, Collection<String>> valueMap) throws Exception {
		Set<String> keySet = valueMap.keySet();
		StringBuilder queryWithoutFilter = new StringBuilder();
		StringBuilder queryWithFilter = new StringBuilder();
		for (String key : keySet) {
			for (String value : valueMap.get(key)) {
				queryWithFilter.append("&" + key).append("=").append(URLEncoder.encode(value, "UTF-8"));
				if (!SearchConstant.RequestStandardParameter.FILTER_QUERY.value().equals(key)) {
					queryWithoutFilter.append("&" + key).append("=").append(URLEncoder.encode(value, "UTF-8"));
				}
			}
		}
		return queryWithFilter.toString();
	}

	private Map<String, List<String>> getCarrotTopic(NamedList namedList) {
		Map<String, List<String>> carrotTopicMap = new LinkedHashMap<String, List<String>>();
		for (int i = 0; i < namedList.size(); i++) {
			if ("clusters".equals(namedList.getName(i))) {
				List<NamedList> obj = (List<NamedList>) namedList.getVal(i);
				for (NamedList list : obj) {
					String label = null;
					List<String> docs = new ArrayList<String>();
					for (int j = 0; j < list.size(); j++) {
						if ("labels".equals(list.getName(j))) {
							List labels = (List) list.getVal(j);
							label = (String) labels.get(0);
						} else if ("docs".equals(list.getName(j))) {
							docs = (List<String>) list.getVal(j);
							if (docs != null) {
								label += "(" + docs.size() + ")";
							}
						}
					}
					carrotTopicMap.put(label, docs);
				}
			}
		}
		return carrotTopicMap;
	}

	private Map<String, Map<String, FacetWrapper>> getFacets(SolrSearchStrategyData data, String queryWithFilter)
			throws Exception {
		Map<String, Map<String, FacetWrapper>> addFilterMap = new LinkedHashMap<String, Map<String, FacetWrapper>>();
		List<FacetField> facetFieldList = data.getQueryResponse().getFacetFields();
		for (FacetField facet : facetFieldList) {
			Map<String, FacetWrapper> map = new LinkedHashMap<String, FacetWrapper>();
			if (facet.getValues() != null) {
				for (Count count : facet.getValues()) {
					int pageParameterStartIndex = queryWithFilter.indexOf("?" + RequestStandardParameter.PAGE.value()
							+ "=");
					boolean firstParameter = true;
					if (pageParameterStartIndex == -1) {
						firstParameter = false;
						pageParameterStartIndex = queryWithFilter.indexOf("&" + RequestStandardParameter.PAGE.value()
								+ "=");
					}

					if (pageParameterStartIndex != -1) {
						if (firstParameter) {
							int pageParameterEndIndex = queryWithFilter.indexOf("&", pageParameterStartIndex + 1);
							if (pageParameterEndIndex == -1)
								queryWithFilter = "";
							else
								queryWithFilter = "?" + queryWithFilter.substring(pageParameterEndIndex + 1);
						} else {
							int pageParameterEndIndex = queryWithFilter.indexOf("&", pageParameterStartIndex + 1);
							if (pageParameterEndIndex == -1)
								queryWithFilter = queryWithFilter.substring(0, pageParameterStartIndex);
							else
								queryWithFilter = queryWithFilter.substring(0, pageParameterStartIndex) + "&"
										+ queryWithFilter.substring(pageParameterEndIndex + 1);
						}

					}

					String url = queryWithFilter + "&" + SearchConstant.RequestStandardParameter.FILTER_QUERY.value()
							+ "=" + URLEncoder.encode(facet.getName() + ":" + "\"" + count.getName() + "\"", "UTF-8");
					map.put(count.getName(), new FacetWrapper(count.getName(), url, count.getCount()));
				}
				addFilterMap.put(facet.getName(), map);
			}
		}

		return addFilterMap;
	}

	private Map<String, String> getRemoveFacetList(Collection<String> filterValueList,
			Map<String, List<String>> dependenciesMap, String queryWithoutFilter) throws Exception {
		Map<String, String> removeFilterMap = new HashMap<String, String>();
		if (filterValueList != null) {
			for (String filterValue : filterValueList) {
				StringBuilder str = new StringBuilder();
				for (String filterValueInner : filterValueList) {
					if (!filterValueInner.equals(filterValue)) {
						if (!dependsOn(filterValueInner, filterValue, dependenciesMap)) {
							str.append("&" + SearchConstant.RequestStandardParameter.FILTER_QUERY.value() + "="
									+ URLEncoder.encode(filterValueInner, "UTF-8"));
						}
					}
				}
				removeFilterMap.put(filterValue, queryWithoutFilter + str.toString());
			}
		}
		return removeFilterMap;
	}

	private Map<String, List<String>> getFacetDependencyMap(String facetDependencyJson) throws Exception {
		Map<String, List<String>> dependenciesMap = new HashMap<String, List<String>>();
		if (facetDependencyJson != null) {
			JsonObject object = (JsonObject) new JsonParser().parse(facetDependencyJson);
			Set<Map.Entry<String, JsonElement>> jsonKeySet = object.entrySet();
			for (Map.Entry<String, JsonElement> entry : jsonKeySet) {
				JsonArray array = object.get(entry.getKey()).getAsJsonArray();
				List<String> dependenciesList = dependenciesMap.get(entry.getKey());
				if (dependenciesList == null) {
					dependenciesList = new ArrayList<String>();
				}
				for (int i = 0; i < array.size(); i++) {
					dependenciesList.add(array.get(i).getAsString());
				}
				dependenciesMap.put(entry.getKey(), dependenciesList);
			}
		}
		return dependenciesMap;
	}

	private Boolean dependsOn(String childFilterValue, String parentFilterValue,
			Map<String, List<String>> dependeciesMap) {
		// ATTENZIONE LA LOGICA E' ERRATA!!!!!!
		// FUNZIONA NEL CASO SPECIFICO DI UNIMI RC
		// MA NON FUNZIONA IN SENSO GENERALE
		// NOTARE LE DUE VERSIONI substringBefore E substringAfter
		// CHE NON DEVONO ESSERCI!!!!!!!
		if (CollectionUtils.isEmpty(dependeciesMap.keySet()))
			return false;
		childFilterValue = StringUtils.substringBefore(childFilterValue, ":").replaceAll("\"", "");
		parentFilterValue = StringUtils.substringAfter(parentFilterValue, ":").replaceAll("\"", "");
		List<String> dependeciesFields = dependeciesMap.get(childFilterValue);
		if (dependeciesFields != null && dependeciesFields.contains(parentFilterValue))
			return true;
		else
			return false;
	}

	@Override
	public Writer getRestMarkup(SearchBuilder searchBuilder, HttpServletRequest request, HttpServletResponse response,
			String outputType, SearchService searchService) throws Exception {
		SolrSearchStrategyData data = (SolrSearchStrategyData) SearchStrategyFactory.getStrategyData(searchBuilder,
				request, searchService);
		if (data.getFacetedFieldsJs() != null) {
			SolrSearchResultWrapper solrSearchResultWrapper = new SolrSearchResultWrapper();
			solrSearchResultWrapper.setResultList((List<Map<String, String>>) getResult(data));
			solrSearchResultWrapper.setFacetMap(getFacets(data, getQueryWithFilter(data.getParameterMap())));
			GsonBuilder gsonBuilder = new GsonBuilder();
			Gson gson = gsonBuilder.create();
			Writer output = new StringWriter();
			output.write(gson.toJson(solrSearchResultWrapper));
			return output;
		} else
			return JaxbUtil.getRestMarkup(this, searchBuilder, request, response, outputType, searchService);
	}

	@Override
	public String getSql(SearchStrategyData d) throws Exception {
		throw new UnsupportedOperationException("getCount MUST BE invoked after getList");
	}
}
