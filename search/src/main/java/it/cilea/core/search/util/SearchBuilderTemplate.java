package it.cilea.core.search.util;

import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.SearchConstant.RequestStandardParameter;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * This class mimics the behaviour of Spring Templates (JdbcTemplate,
 * RestTemplate, ...) for SearchBuilders. There are two overloaded methods that
 * allow to make a query by specifing a search builder name or URL. Keep in mind
 * - search builder are extracted from SearchConstant.searchBuilderMap so you
 * cannot access search builders of other modules - parameter in the map are
 * placed in the request context
 * 
 * @author palena
 * 
 */
@Component
public class SearchBuilderTemplate {
	@Autowired
	protected SearchService searchService;

	public List<?> getNativeList(String searchBuilderIdentifier, HashMap<String, Object> parameterMap, String jndi) {
		List<Object[]> result = new ArrayList<Object[]>();

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;

		try {
			DataSource ds = (DataSource) new InitialContext().lookup(jndi);
			connection = ds.getConnection();
			statement = connection.createStatement();
			String sqlQuery = sql(searchBuilderIdentifier, parameterMap);
			rs = statement.executeQuery(sqlQuery);

			while (rs.next()) {
				Object[] row = new Object[rs.getMetaData().getColumnCount()];
				for (int i = 1; i < rs.getMetaData().getColumnCount() + 1; i++) {
					row[i - 1] = rs.getObject(i);
				}
				result.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
		return result;
	}


	/**
	 * This method treats the searchBuilderIdentifier as a name
	 * 
	 * @param searchBuilderIdentifier
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<?> query(String searchBuilderIdentifier, Map<String, Object> paramMap) throws Exception {
		return query(searchBuilderIdentifier, SearchConstant.SearchBuilderIdentifier.NAME, paramMap);
	}

	/**
	 * This method treats the searchBuilderIdentifier as a name
	 * 
	 * @param searchBuilderIdentifier
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public List<?> query(String searchBuilderIdentifier, Map<String, Object> paramMap, HttpServletRequest request)
			throws Exception {
		return query(searchBuilderIdentifier, SearchConstant.SearchBuilderIdentifier.NAME, paramMap, request);
	}

	/**
	 * This method treats the searchBuilderIdentifier as a name
	 * 
	 * @param searchBuilderIdentifier
	 * @param paramMap
	 * @param request
	 * @return the SQL query generated
	 * @throws Exception
	 */
	public String sql(String searchBuilderIdentifier, Map<String, Object> paramMap) throws Exception {
		return sql(searchBuilderIdentifier, SearchConstant.SearchBuilderIdentifier.NAME, paramMap);
	}

	public Object getSingleObject(String searchBuilderIdentifier, Map<String, Object> paramMap) throws Exception {
		List<?> resultList = query(searchBuilderIdentifier, SearchConstant.SearchBuilderIdentifier.NAME, paramMap);
		if (CollectionUtils.isNotEmpty(resultList))
			return resultList.get(0);
		else
			return null;
	}

	public Object getSingleObject(String searchBuilderIdentifier, Map<String, Object> paramMap, String module)
			throws Exception {
		throw new UnsupportedOperationException("Unsupported Operation - TODO");
	}

	public List<?> query(String searchBuilderIdentifier, Map<String, Object> paramMap, String module) throws Exception {
		throw new UnsupportedOperationException("Unsupported Operation - TODO");
	}

	/**
	 * This method allow to specify how to treat searchBuilderIdentifier: as a
	 * name or as a URL
	 * 
	 * @param searchBuilderIdentifier
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */

	public List<?> query(String searchBuilderIdentifier,
			SearchConstant.SearchBuilderIdentifier searchBuilderIdentifierType, Map<String, Object> paramMap)
			throws Exception {
		HttpServletRequest request;
		if (RequestContextHolder.getRequestAttributes() != null) {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			request = attr.getRequest();
		} else
			throw new IllegalStateException("No HttpRequest is bound");

		request.setAttribute(RequestStandardParameter.POSTING.value(), "1");
		Map<String, SearchBuilder> searchBuilderMap = SearchConstant.searchBuilderMap;
		SearchBuilder searchBuilder = searchBuilderMap.get(searchBuilderIdentifier);
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet())
				request.setAttribute(key, paramMap.get(key));
		}

		if (searchBuilderIdentifierType.equals(SearchConstant.SearchBuilderIdentifier.NAME)) {
			request.setAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value(), searchBuilderIdentifier);
		}

		request.setAttribute(RequestStandardParameter.PAGE_SIZE.value(), 0);
		SearchStrategy strategy = SearchStrategyFactory.getStrategy(searchBuilder, request);
		SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
		List<?> resultList = strategy.getResult(data);

		if (searchBuilderIdentifierType.equals(SearchConstant.SearchBuilderIdentifier.NAME)) {
			request.removeAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value());
		}
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet())
				request.removeAttribute(key);
		}
		return resultList;
	}

	/**
	 * This method allow to specify how to treat searchBuilderIdentifier: as a
	 * name or as a URL
	 * 
	 * @param searchBuilderIdentifier
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */

	public String sql(String searchBuilderIdentifier,
			SearchConstant.SearchBuilderIdentifier searchBuilderIdentifierType, Map<String, Object> paramMap)
			throws Exception {
		HttpServletRequest request;
		if (RequestContextHolder.getRequestAttributes() != null) {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			request = attr.getRequest();
		} else
			throw new IllegalStateException("No HttpRequest is bound");

		request.setAttribute(RequestStandardParameter.POSTING.value(), "1");
		Map<String, SearchBuilder> searchBuilderMap = SearchConstant.searchBuilderMap;
		SearchBuilder searchBuilder = searchBuilderMap.get(searchBuilderIdentifier);
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet())
				request.setAttribute(key, paramMap.get(key));
		}

		if (searchBuilderIdentifierType.equals(SearchConstant.SearchBuilderIdentifier.NAME)) {
			request.setAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value(), searchBuilderIdentifier);
		}

		request.setAttribute(RequestStandardParameter.PAGE_SIZE.value(), 0);
		SearchStrategy strategy = SearchStrategyFactory.getStrategy(searchBuilder, request);
		SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
		String sql = strategy.getSql(data);

		if (searchBuilderIdentifierType.equals(SearchConstant.SearchBuilderIdentifier.NAME)) {
			request.removeAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value());
		}
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet())
				request.removeAttribute(key);
		}
		return sql;
	}

	/**
	 * This method allow to specify how to treat searchBuilderIdentifier: as a
	 * name or as a URL
	 * 
	 * @param searchBuilderIdentifier
	 * @param paramMap
	 * @param request
	 * @return
	 * @throws Exception
	 */

	public List<?> query(String searchBuilderIdentifier,
			SearchConstant.SearchBuilderIdentifier searchBuilderIdentifierType, Map<String, Object> paramMap,
			HttpServletRequest request) throws Exception {
		request.setAttribute(RequestStandardParameter.POSTING.value(), "1");
		Map<String, SearchBuilder> searchBuilderMap = SearchConstant.searchBuilderMap;
		SearchBuilder searchBuilder = searchBuilderMap.get(searchBuilderIdentifier);
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet())
				request.setAttribute(key, paramMap.get(key));
		}

		if (searchBuilderIdentifierType.equals(SearchConstant.SearchBuilderIdentifier.NAME)) {
			request.setAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value(), searchBuilderIdentifier);
		}

		request.setAttribute(RequestStandardParameter.PAGE_SIZE.value(), 0);
		SearchStrategy strategy = SearchStrategyFactory.getStrategy(searchBuilder, request);
		SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
		List<?> resultList = strategy.getResult(data);

		if (searchBuilderIdentifierType.equals(SearchConstant.SearchBuilderIdentifier.NAME)) {
			request.removeAttribute(SearchConstant.SearchBuilderIdentifier.NAME.value());
		}
		if (paramMap != null && paramMap.size() > 0) {
			for (String key : paramMap.keySet())
				request.removeAttribute(key);
		}
		return resultList;
	}
}
