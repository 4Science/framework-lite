package it.cilea.core.search;

import it.cilea.core.model.Selectable;
import it.cilea.core.search.model.SearchBuilder;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SearchConstant {
	public static Map<String, SearchBuilder> searchBuilderMap = new HashMap<String, SearchBuilder>();

	public static enum SearchBuilderIdentifier {
		NAME("it.cilea.core.search.SearchBuilderIdentifier.name"), URL(
				"it.cilea.core.search.SearchBuilderIdentifier.url");

		private String value;

		SearchBuilderIdentifier(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}

	}

	/**
	 * @author Davide Palena This enum lists the form types:
	 *         <ul>
	 *         <li>SOLR: this search form has as data source backend a SOLR
	 *         server.
	 *         <li>HQL: this search form has as data source backend a DB server
	 *         using HQL (Hibernate).
	 *         </ul>
	 * 
	 */
	public static enum SearchStrategyType {
		SOLR, HQL, OLAP, SQL, JIRA;
		// public String toString() { return this.name().toLowerCase(); }
	}

	/**
	 * @author Luca Fronterotta This enum lists the string match types:
	 *         <ul>
	 *         <li>exact: the compared strings must match exactly.
	 *         <li>like_left: the compared string must begin with...
	 *         <li>like_right: the compared string must finish with...
	 *         <li>like_both: the compared string must contain the substring
	 *         <li>relaxation: execute the most restrictive matching version of
	 *         the query first, progressively relaxing the query until the
	 *         required number of hits are obtained
	 *         </ul>
	 * 
	 */
	public static enum StringMatchType {
		exact, relaxation, like_left, like_right, like_both;
	}

	public static enum SearchBuilderParameterName {
		SERIALIZER_CLASS, ROOT_MODEL_CLASS, DISTINCT_CLAUSE, DISTINCT_ATTRIBUTE_NAME, SOLR_ENDPOINT, DTO_SELECT_CLAUSE, FULL_SELECT_CLAUSE, FULL_GROUP_CLAUSE, JIRA_ENDPOINT, JAXB_MODEL_BINDING, JAXB_SEARCH_RESULT_BINDING, XSL_FILE, JSON_PROPERTY, JSON_JAVASCRIPT, MEDIA_TYPE
	}

	public static enum SolrQueryType {
		EDISMAX("edismax"), MORE_LIKE_THIS("mlt");

		private String value;

		SolrQueryType(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}
	}

	public static enum SolrSearchBuilderParameterName {
		/**
		 * URL SOLR SERVER
		 */
		SOLR_ENDPOINT("solrEndpoint"),
		/**
		 * ACCEPTABLE VALUES FOR QUERY TYPE ARE: - edismax - mlt
		 */
		QUERY_TYPE("queryType"),

		/**
		 * This attribute specifies the fields and the boost to be used in
		 * searching Ex. displayValue^2.4 text^0.4 If qt=edismax then the name
		 * of the attribute to be used is qf If qt=mlt then the name of the
		 * attribute to be used is mlt.qf
		 */
		QUERY_FIELDS("queryFields"), PHRASE_FIELDS("phraseFields"),
		/**
		 * This attribute specifies the csv list of the fields to be returned in
		 * response Ex. displayValue,text
		 */
		FIELDS("fields"),

		FACETED_FIELDS_JS("facetedFieldsJs"), FACETED_DEPENDENCY_FIELDS_JSON("facetedDependencyFieldsJson"), VISIBLE_FACET_NUMBER(
				"visibleFacetNumber"), UID("uid"), HIGHLIGHT_FIELD("highlightField"), SNIPPET_SIZE("snippeSize"), FILTER_QUERY(
				"filterQuery"), MORE_LIKE_THIS_COUNT("moreLikeThisCount"), BOOST_QUERY("boostQuery");

		private String value;

		SolrSearchBuilderParameterName(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}
	}

	public static enum RequestStandardParameter {
		POSTING("posting"), PAGE("page"), SORT("sort"), SORT_DIRECTION("dir"), PAGE_SIZE("pageSize"), FILTER_QUERY(
				"filter");

		private String value;

		RequestStandardParameter(String value) {
			this.value = value;
		}

		public String value() {
			return value;
		}
	}

	public static final Integer DEFAULT_PAGE_SIZE = 30;

	public static final Integer MAX_PAGE_SIZE_FROM_QUERY_STRING = 500;
	public static final Integer MAX_PAGE_SIZE_EXPORT = 20000;

	public static final String JSON_STRINGIFY_FUNCTION_FILE = "it/cilea/core/search/util/json-stringify.js";
	public static final String JSON_STRINGIFY_FUNCTION;
	public static final String JSON_REQUEST_PARAMETER = "json";
	public static final String XSL_REQUEST_PARAMETER = "xsl";
	public static final String SEARCH_FORM_NAME = "searchFormName";
	public static final String DISPLAY_TAG_DATA = "displayTagData";
	public static final String FACET_DATA = "facetData";
	public static final String CARROT_TOPIC = "carrotTopic";
	public static final String SNIPPET = "snippet";
	public static final String REMOVE_FACET_DATA = "removeFacetData";
	public static final String QUERY_STRING = "queryString";
	public static final String MEDIA_TYPE = "MEDIA_TYPE";

	public static final String DEST_WIDGET_ID_PARAM_NAME = "destWidgetId";
	public static final String DEST_WIDGET_SELECTED_VALUE = "destWidgetSelectedValue";
	public static final String DEST_WIDGET_NAME_PARAM_NAME = "destWidgetId";
	public static final String SOURCE_WIDGET_SELECTED_ITEM = "parentId";
	public static GsonBuilder gsonBuilder = new GsonBuilder();

	// Register custom serializer for Selectable
	// TODO: TO REMOVE SERIALIZATION PROVIDED WITH JAXB
	static {
		gsonBuilder.registerTypeAdapter(Selectable.class, new JsonSerializer<Selectable>() {
			public JsonElement serialize(Selectable selectable, Type typeOfSrc, JsonSerializationContext context) {
				JsonObject obj = new JsonObject();
				obj.addProperty("identifyingValue", selectable.getIdentifyingValue());
				obj.addProperty("displayValue", selectable.getDisplayValue());
				return obj;
			}
		});

		try {
			InputStream stream = SearchConstant.class.getClassLoader().getResourceAsStream(
					SearchConstant.JSON_STRINGIFY_FUNCTION_FILE);
			Writer str = new StringWriter();
			IOUtils.copy(stream, str, "UTF-8");
			JSON_STRINGIFY_FUNCTION = str.toString();
		} catch (Exception e) {
			throw new ExceptionInInitializerError("Cannot load JSON_STRINGIFY_FUNCTION from classpath: "
					+ SearchConstant.JSON_STRINGIFY_FUNCTION_FILE);
		}
	}

	public static final String DEFAULT_LOCALE = "it";

	public static final String SECTION_SELECT = "select";
	public static final String SECTION_FILTER = "filter";
	public static final String SECTION_GROUP = "group";
	public static final String SELECTABLE = "selectable";
}
