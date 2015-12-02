package it.cilea.core.search.strategy.impl.solr;

public class FacetWrapper {
	public FacetWrapper(String value, String url, Long count) {
		this.value = value;
		this.url = url;
		this.count = count;
	}

	private String value;
	private String url;
	private Long count;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
