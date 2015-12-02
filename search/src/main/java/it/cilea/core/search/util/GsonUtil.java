package it.cilea.core.search.util;

import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.SearchStrategy;
import it.cilea.core.search.strategy.SearchStrategyData;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@Deprecated
public class GsonUtil {

	public static boolean isGsonEnabled(SearchBuilder searchBuilder) {
		String serializerClassName = searchBuilder.getSearchBuilderParameterMap().get(
				SearchBuilderParameterName.SERIALIZER_CLASS.toString());
		if (StringUtils.isNotBlank(serializerClassName))
			return true;
		else
			return false;
	}

	public static Class getDefaultRootModelClass(SearchBuilder searchBuilder) throws Exception {
		String modelClassName = searchBuilder.getSearchBuilderParameterMap().get(
				SearchBuilderParameterName.ROOT_MODEL_CLASS.toString());
		return Class.forName(modelClassName);
	}

	public static Writer getJson(SearchBuilder searchBuilder, SearchStrategy strategy, HttpServletRequest request,
			Class modelClass, SearchService searchService) throws Exception {
		SearchStrategyData data = SearchStrategyFactory.getStrategyData(searchBuilder, request, searchService);
		List<?> resultList = strategy.getResult(data);
		GsonBuilder gsonBuilder = new GsonBuilder();
		String serializerClassName = searchBuilder.getSearchBuilderParameterMap().get(
				SearchBuilderParameterName.SERIALIZER_CLASS.toString());
		gsonBuilder.registerTypeAdapter(modelClass, Class.forName(serializerClassName).newInstance());
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
		Type listType = new TypeToken<List>() {
		}.getType();
		StringWriter st = new StringWriter();
		st.append(gson.toJson(resultList, listType));
		return st;
	}
}
