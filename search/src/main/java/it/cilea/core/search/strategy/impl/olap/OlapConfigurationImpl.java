package it.cilea.core.search.strategy.impl.olap;

import javax.servlet.http.HttpServletRequest;

public class OlapConfigurationImpl implements OlapConfiguration {

	public String getMdxQuery(String mdxQuery, HttpServletRequest request) {
		return mdxQuery;

	}
}
