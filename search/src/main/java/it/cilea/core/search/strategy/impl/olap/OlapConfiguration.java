package it.cilea.core.search.strategy.impl.olap;

import javax.servlet.http.HttpServletRequest;

public interface OlapConfiguration {
	String getMdxQuery(String mdxQuery, HttpServletRequest request);
}
