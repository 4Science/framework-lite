package it.cilea.core.search.strategy.impl.solr;

import java.util.List;
import java.util.Map;

public class SolrSearchResultWrapper {
	private List<Map<String, String>> resultList;
	private Map<String, Map<String, FacetWrapper>> facetMap;
	private Map<String, List<String>> carrotTopicMap;

	public List<Map<String, String>> getResultList() {
		return resultList;
	}

	public void setResultList(List<Map<String, String>> resultList) {
		this.resultList = resultList;
	}

	public Map<String, Map<String, FacetWrapper>> getFacetMap() {
		return facetMap;
	}

	public void setFacetMap(Map<String, Map<String, FacetWrapper>> facetMap) {
		this.facetMap = facetMap;
	}

	public Map<String, List<String>> getCarrotTopicMap() {
		return carrotTopicMap;
	}

	public void setCarrotTopicMap(Map<String, List<String>> carrotTopicMap) {
		this.carrotTopicMap = carrotTopicMap;
	}

}
