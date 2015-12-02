package it.cilea.core.search.factory;

import it.cilea.core.search.SearchConstant.SearchStrategyType;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;
import it.cilea.core.search.strategy.impl.hibernate.HibernateSearchStrategy;
import it.cilea.core.search.strategy.impl.hibernate.HibernateSearchStrategyData;
import it.cilea.core.search.strategy.impl.olap.OlapSearchStrategy;
import it.cilea.core.search.strategy.impl.olap.OlapSearchStrategyData;
import it.cilea.core.search.strategy.impl.solr.SolrSearchStrategy;
import it.cilea.core.search.strategy.impl.solr.SolrSearchStrategyData;
import it.cilea.core.search.strategy.impl.sql.SqlSearchStrategy;
import it.cilea.core.search.strategy.impl.sql.SqlSearchStrategyData;

import javax.servlet.http.HttpServletRequest;

public class SearchStrategyFactory {
	private static SolrSearchStrategy solrStrategy;
	private static HibernateSearchStrategy hibernateStrategy;
	private static OlapSearchStrategy olapStrategy;
	private static SqlSearchStrategy sqlStrategy;

	public static SearchStrategy getStrategy(SearchBuilder searchBuilder, HttpServletRequest request) {
		if (SearchStrategyType.SOLR.toString().equals(searchBuilder.getStrategy())) {
			return solrStrategy;
		} else if (SearchStrategyType.HQL.toString().equals(searchBuilder.getStrategy())) {
			return hibernateStrategy;
		} else if (SearchStrategyType.OLAP.toString().equals(searchBuilder.getStrategy())) {
			return olapStrategy;
		} else if (SearchStrategyType.SQL.toString().equals(searchBuilder.getStrategy())) {
			return sqlStrategy;
		}
		throw new UnsupportedOperationException("No SearchStrategy is available for the given Search Strategy Type: "
				+ searchBuilder.getStrategy());
	}

	public static SearchStrategyData getStrategyData(SearchBuilder searchBuilder, HttpServletRequest request,
			SearchService searchService) {
		SearchStrategyData strategy = null;
		if (SearchStrategyType.SOLR.toString().equals(searchBuilder.getStrategy())) {
			strategy = new SolrSearchStrategyData(searchBuilder, request, searchService);
			strategy.init(request);
			return strategy;
		} else if (SearchStrategyType.HQL.toString().equals(searchBuilder.getStrategy())) {
			strategy = new HibernateSearchStrategyData(searchBuilder, request, searchService);
			strategy.init(request);
			return strategy;
		} else if (SearchStrategyType.OLAP.toString().equals(searchBuilder.getStrategy())) {
			strategy = new OlapSearchStrategyData(searchBuilder, request, searchService);
			strategy.init(request);
			return strategy;
		} else if (SearchStrategyType.SQL.toString().equals(searchBuilder.getStrategy())) {
			strategy = new SqlSearchStrategyData(searchBuilder, request, searchService);
			strategy.init(request);
			return strategy;
		}
		throw new UnsupportedOperationException(
				"No SearchStrategyData is available for the given Search Strategy Type: " + searchBuilder.getStrategy());
	}

	public static void setHibernateStrategy(HibernateSearchStrategy hibernateStrategy) {
		if (SearchStrategyFactory.hibernateStrategy == null)
			SearchStrategyFactory.hibernateStrategy = hibernateStrategy;
		else
			throw new RuntimeException("hibernateStrategy MUST BE initialized only once by context listener start");
	}

	public static void setSolrStrategy(SolrSearchStrategy solrStrategy) {
		if (SearchStrategyFactory.solrStrategy == null)
			SearchStrategyFactory.solrStrategy = solrStrategy;
		else
			throw new RuntimeException("solrStrategy MUST BE initialized only once by context listener start");
	}

	public static void setOlapStrategy(OlapSearchStrategy olapStrategy) {
		if (SearchStrategyFactory.olapStrategy == null)
			SearchStrategyFactory.olapStrategy = olapStrategy;
		else
			throw new RuntimeException("olapStrategy MUST BE initialized only once by context listener start");
	}

	public static void setSqlStrategy(SqlSearchStrategy sqlStrategy) {
		if (SearchStrategyFactory.sqlStrategy == null)
			SearchStrategyFactory.sqlStrategy = sqlStrategy;
		else
			throw new RuntimeException("sqlStrategy MUST BE initialized only once by context listener start");
	}

}
