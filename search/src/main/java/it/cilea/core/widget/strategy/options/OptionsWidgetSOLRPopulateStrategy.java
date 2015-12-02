package it.cilea.core.widget.strategy.options;

import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.Selectable;
import it.cilea.core.search.regex.RegexParameterResolver;
import it.cilea.core.widget.model.OptionsWidget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

public class OptionsWidgetSOLRPopulateStrategy extends OptionsWidgetPopulateStrategy {

	private OptionsWidget widget;
	private String url;
	private String listQuery;
	private String singleQuery;
	private String identifyingValueName;
	private String displayValueName;
	private SolrServer server;

	public OptionsWidgetSOLRPopulateStrategy(OptionsWidget widget, HttpServletRequest request) {
		super(widget, request);
		this.widget = widget;
		String populationValue = widget.getPopulationValue();
		String[] piece = populationValue.split("\\|");

		if (StringUtils.isBlank(populationValue) || !populationValue.contains("|") || piece.length != 5)
			throw new IllegalStateException(
					"OptionsWidgetSOLRProvider expects a populationValue with this pattern SOLR_SERVER_URL|LIST_QUERY|SINGLE_QUERY_ID_FIELD_NAME|VALUE_FIELD_NAME");

		this.url = piece[0];
		this.listQuery = piece[1];
		this.singleQuery = piece[2];
		this.identifyingValueName = piece[3];
		this.displayValueName = piece[4];
		this.singleQuery = RegexParameterResolver.getParsedMetaQueryParameterValue(singleQuery, request, null);
		this.listQuery = RegexParameterResolver.getParsedMetaQueryParameterValue(listQuery, request, null);
		try {
			this.server = new CommonsHttpSolrServer(url);
		} catch (Exception e) {
			this.server = null;
		}
	}

	public List<? extends Selectable> getOptions() {
		List<Selectable> globalList = new ArrayList<Selectable>();

		if (widget.getRenderEmptyOption()) {
			globalList.add(widget.getSelectableEmptyOption());
		}

		List<Selectable> resultList = null;
		try {

			resultList = getSelectableList(listQuery, identifyingValueName, displayValueName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (resultList != null)
			globalList.addAll(resultList);

		return globalList;
	}

	public Selectable getOption(Object id) {
		Selectable defaultSelectable = new SelectBaseString("", "");
		if (id == null)
			return defaultSelectable;

		List<Selectable> resultList = null;
		try {
			resultList = getSelectableList(singleQuery, identifyingValueName, displayValueName);
			return resultList.get(0);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultSelectable;
		}
	}

	private List<Selectable> getSelectableList(String query, String identifyingValueFieldName,
			String displayValueFieldName) throws Exception {
		// TODO: must be tested
		List<Selectable> selectableList = new ArrayList<Selectable>();

		if (StringUtils.isBlank(query))
			return selectableList;

		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery(query);
		// query.addSortField( "price", SolrQuery.ORDER.asc );
		solrQuery.setRows(300);
		QueryResponse rsp = server.query(solrQuery);
		SolrDocumentList docs = rsp.getResults();
		Iterator<SolrDocument> it = docs.iterator();

		while (it.hasNext()) {
			SolrDocument doc = it.next();
			Object identifyingValueRaw = doc.getFieldValue(identifyingValueFieldName);
			Object displayValueRaw = doc.getFieldValue(displayValueFieldName);
			String identifyingValue = null;
			String displayValue = null;
			if (identifyingValueRaw instanceof List) {
				identifyingValue = (String) ((List) identifyingValueRaw).get(0);
				displayValue = (String) ((List) displayValueRaw).get(0);
			} else {
				identifyingValue = (String) identifyingValueRaw;
				displayValue = (String) displayValueRaw;
			}

			SelectBaseString selectable = new SelectBaseString(identifyingValue, displayValue);
			selectableList.add(selectable);
		}
		return selectableList;
	}

	public List<? extends Selectable> getOptions(String[] ids) {
		// TODO: must be tested
		// Selectable defaultSelectable = new SelectBaseString("", "");
		List<Selectable> defaultSelectable = new ArrayList<Selectable>();
		if (ArrayUtils.isEmpty(ids))
			return defaultSelectable;

		try {
			return getSelectableList(singleQuery, identifyingValueName, displayValueName);

		} catch (Exception e) {
			e.printStackTrace();
			return defaultSelectable;
		}

	}
}