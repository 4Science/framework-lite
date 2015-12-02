package it.cilea.core.search.strategy.impl.solr;

import it.cilea.core.search.SearchConstant.RequestStandardParameter;
import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.SearchConstant.SolrQueryType;
import it.cilea.core.search.SearchConstant.SolrSearchBuilderParameterName;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.regex.RegexParameterProvider;
import it.cilea.core.search.regex.RegexParameterResolver;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.RegexParameterProviderImpl;
import it.cilea.core.search.strategy.SearchStrategyData;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SolrSearchStrategyData extends SearchStrategyData {

	private SolrServer server;
	/**
	 * Query type (dismax, mlt...)
	 */
	private String queryType;

	/**
	 * Used together with queryType=dismax,mlt to specify the list of field to
	 * use with the relative boost
	 */
	private String queryFields;
	private String phraseFields;
	private String moreLikeThisFields;
	private String moreLikeThisCount;
	private String responseFields;
	private String boostQuery;

	private String initialFilterQuery;

	private String facetedFieldsJs;

	private String facetedDependencyFieldsJson;

	private String visibileFacetNumber;

	private String[] solrFilterQueryList;

	private String uid;

	private String[] highlightField;

	private Integer snippetSize;

	private QueryResponse queryResponse;

	public SolrSearchStrategyData(SearchBuilder searchBuilder, HttpServletRequest request, SearchService searchService) {
		super(searchBuilder, request, searchService);
		this.orderClause = searchBuilder.getOrderClause();

		Map<String, String> searchBuilderParameterMap = searchBuilder.getSearchBuilderParameterMap();

		String solrEndpoint = searchBuilderParameterMap.get(SearchBuilderParameterName.SOLR_ENDPOINT.toString());
		if (StringUtils.isBlank(solrEndpoint))
			throw new IllegalStateException("SolrSearchStrategyData needs solrEndpoint parameter");

		try {
			this.server = new CommonsHttpSolrServer(solrEndpoint);
		} catch (Exception e) {
			throw new IllegalStateException("Solr Endpoint specified is illegal: " + solrEndpoint);
		}

		this.queryFields = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.QUERY_FIELDS.toString());
		this.phraseFields = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.PHRASE_FIELDS.toString());
		this.queryType = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.QUERY_TYPE.toString());
		this.boostQuery = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.BOOST_QUERY.toString());

		if (SolrQueryType.MORE_LIKE_THIS.value().equals(this.queryType)) {
			this.moreLikeThisFields = this.queryFields.replaceAll("\\^(\\S)*", ",");
			this.moreLikeThisFields = StringUtils.removeEnd(this.moreLikeThisFields, ",");
		}

		this.uid = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.UID.toString());
		this.highlightField = new String[0];

		String highlightFieldString = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.HIGHLIGHT_FIELD
				.toString());
		if (StringUtils.isNotBlank(highlightFieldString)) {
			this.highlightField = highlightFieldString.split(",");
		}

		if (searchBuilderParameterMap.get(SolrSearchBuilderParameterName.SNIPPET_SIZE.toString()) != null) {
			this.snippetSize = new Integer(searchBuilderParameterMap.get(SolrSearchBuilderParameterName.SNIPPET_SIZE
					.toString()));
		}

		this.initialFilterQuery = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.FILTER_QUERY.toString());

		if (this.initialFilterQuery != null) {
			RegexParameterProvider regexParameterProviderImpl = new RegexParameterProviderImpl();
			this.initialFilterQuery = RegexParameterResolver.getParsedMetaQueryParameterValue(this.initialFilterQuery,
					request, regexParameterProviderImpl);
		}

		String facetedFieldsJs = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.FACETED_FIELDS_JS
				.toString());
		this.facetedFieldsJs = facetedFieldsJs;

		String facetedDependencyFieldsJson = searchBuilderParameterMap
				.get(SolrSearchBuilderParameterName.FACETED_DEPENDENCY_FIELDS_JSON.toString());
		this.facetedDependencyFieldsJson = facetedDependencyFieldsJson;

		this.visibileFacetNumber = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.VISIBLE_FACET_NUMBER
				.toString());

		this.responseFields = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.FIELDS.toString());
		if (StringUtils.isBlank(responseFields)) {
			this.responseFields = "*";
		}

		this.visibileFacetNumber = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.VISIBLE_FACET_NUMBER
				.toString());

		solrFilterQueryList = request.getParameterValues(RequestStandardParameter.FILTER_QUERY.value());
		if (StringUtils.isNotBlank(initialFilterQuery)) {
			solrFilterQueryList = (String[]) ArrayUtils
					.addAll(solrFilterQueryList, new String[] { initialFilterQuery });
		}

		// if (ArrayUtils.isEmpty(solrFilterQueryList) &&
		// StringUtils.isNotBlank(initialFilterQuery)){
		// solrFilterQueryList= new String[]{initialFilterQuery};
		// }

		this.moreLikeThisCount = searchBuilderParameterMap.get(SolrSearchBuilderParameterName.MORE_LIKE_THIS_COUNT
				.toString());

	}

	public String getDefaultSort() {
		String sortField = "";
		if (!ArrayUtils.isEmpty(getSortFieldList())) {
			sortField = getSortFieldList()[0];
			if (StringUtils.isBlank(sortField))
				sortField = "";
		}
		return sortField;
	}

	public SolrServer getServer() {
		return server;
	}

	public void setServer(SolrServer server) {
		this.server = server;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(String queryFields) {
		this.queryFields = queryFields;
	}

	public String[] getSolrFilterQueryList() {
		return solrFilterQueryList;
	}

	public void setSolrFilterQueryList(String[] solrFilterQueryList) {
		this.solrFilterQueryList = solrFilterQueryList;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String[] getHighlightField() {
		return highlightField;
	}

	public void setHighlightField(String[] highlightField) {
		this.highlightField = highlightField;
	}

	public Integer getSnippetSize() {
		return snippetSize;
	}

	public void setSnippetSize(Integer snippetSize) {
		this.snippetSize = snippetSize;
	}

	public String getFacetedFieldsJs() {
		return facetedFieldsJs;
	}

	public void setFacetedFieldsJs(String facetedFieldsJs) {
		this.facetedFieldsJs = facetedFieldsJs;
	}

	public String getVisibileFacetNumber() {
		return visibileFacetNumber;
	}

	public void setVisibileFacetNumber(String visibileFacetNumber) {
		this.visibileFacetNumber = visibileFacetNumber;
	}

	public String getInitialFilterQuery() {
		return initialFilterQuery;
	}

	public void setInitialFilterQuery(String initialFilterQuery) {
		this.initialFilterQuery = initialFilterQuery;
	}

	public String getMoreLikeThisFields() {
		return moreLikeThisFields;
	}

	public void setMoreLikeThisFields(String moreLikeThisFields) {
		this.moreLikeThisFields = moreLikeThisFields;
	}

	public String getResponseFields() {
		return responseFields;
	}

	public void setResponseFields(String responseFields) {
		this.responseFields = responseFields;
	}

	public String getMoreLikeThisCount() {
		return moreLikeThisCount;
	}

	public void setMoreLikeThisCount(String moreLikeThisCount) {
		this.moreLikeThisCount = moreLikeThisCount;
	}

	public String getFacetedDependencyFieldsJson() {
		return facetedDependencyFieldsJson;
	}

	public void setFacetedDependencyFieldsJson(String facetedDependencyFieldsJson) {
		this.facetedDependencyFieldsJson = facetedDependencyFieldsJson;
	}

	public QueryResponse getQueryResponse() {
		return queryResponse;
	}

	public void setQueryResponse(QueryResponse queryResponse) {
		this.queryResponse = queryResponse;
	}

	public String getBoostQuery() {
		return boostQuery;
	}

	public void setBoostQuery(String boostQuery) {
		this.boostQuery = boostQuery;
	}

	public String getPhraseFields() {
		return phraseFields;
	}

	public void setPhraseFields(String phraseFields) {
		this.phraseFields = phraseFields;
	}

}
