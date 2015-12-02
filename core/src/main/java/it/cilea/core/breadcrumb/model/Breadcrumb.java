package it.cilea.core.breadcrumb.model;

import it.cilea.core.model.IdentifiableObject;

public class Breadcrumb extends IdentifiableObject {

	private Integer id;

	private String url;

	private String queryString;

	private String username;

	private String tag;

	private String tagNoParams;

	private Integer position;

	private String method;

	private String bookmark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getTagNoParams() {
		return tagNoParams;
	}

	public void setTagNoParams(String tagNoParams) {
		this.tagNoParams = tagNoParams;
	}

	public String getBookmark() {
		return bookmark;
	}

	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

}
