package it.cilea.core.spring.dao;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public interface NamedQueryExecutor<T> {

	List<T> executeFinder(Method method, Object[] queryArgs);

	List<T> executePaginator(Method method, Object[] queryArgs, String sort, boolean inverse, int firstResult,
			int maxResults);

	Number executeCounter(Method method, Object[] queryArgs);

	Date executeDater(Method method, Object[] queryArgs);

	Number executeMaxerNumber(Method method, Object[] queryArgs);

	Integer executeIdFinder(Method method, Object[] queryArgs);

}
