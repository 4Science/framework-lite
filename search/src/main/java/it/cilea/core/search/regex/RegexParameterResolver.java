package it.cilea.core.search.regex;

import it.cilea.core.configuration.ConfigurationConstant;
import it.cilea.core.model.Selectable;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.antlr.QueryNormalizer;
import it.cilea.core.search.util.SearchUtil;
import it.cilea.core.spring.CoreSpringConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

public class RegexParameterResolver {

	public static List<String> getParameterNameList(String metaQuery) {
		if (metaQuery == null)
			return new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(metaQuery);
		List<String> paramList = new ArrayList<String>();

		while (matcher.find()) {
			paramList.add(StringUtils.remove(StringUtils.remove(matcher.group(), "${"), "}"));
		}
		return paramList;
	}

	public static List<String> getModelAttributeNameList(String metaQuery) {
		if (metaQuery == null)
			return new ArrayList<String>();
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(metaQuery);
		List<String> paramList = new ArrayList<String>();
		while (matcher.find()) {
			paramList.add(StringUtils.remove(StringUtils.remove(matcher.group(), "{"), "}"));
		}
		return paramList;
	}

	private static String replaceChar(String str) {
		return SearchUtil.escapeBracket(StringEscapeUtils.escapeSql(str));

	}

	public static String[] getParameterValue(String parameterName, HttpServletRequest request,
			RegexParameterProvider regexParameterProvider) {
		String[] returnValue = null;
		Object parameterValue = null;
		if (parameterName.contains("param.")) {

			returnValue = request.getParameterValues(StringUtils.remove(parameterName, "param."));
			if (returnValue != null) {
				for (int i = 0; i < returnValue.length; i++) {
					returnValue[i] = replaceChar(returnValue[i]);
				}
			}
			// if (requestParameterValues != null) {
			// parameterValues = new String();
			// for (String parameterValue : requestParameterValues) {
			// parameterValues+=","+getWrappedParameterValue(parameterName,
			// parameterValue);
			// }
			// parameterValues = parameterValues.substring(1);
			// }
		} else if (parameterName.contains("session.")) {
			parameterValue = request.getSession().getAttribute(StringUtils.remove(parameterName, "session."));

			if (parameterValue != null) {
				returnValue = new String[] { replaceChar(parameterValue.toString()) };
			}
		} else if (parameterName.contains("request.")) {
			parameterValue = request.getAttribute(StringUtils.remove(parameterName, "request."));

			if (parameterValue != null) {
				returnValue = new String[] { replaceChar(parameterValue.toString()) };
			}
		} else if (parameterName.contains("application.")) {
			parameterValue = request.getSession().getServletContext()
					.getAttribute(StringUtils.remove(parameterName, "application."));

			if (parameterValue != null) {
				returnValue = new String[] { replaceChar(parameterValue.toString()) };
			}
		} else if (parameterName.contains("serverSide.")) {
			String searchBuilderIdentifier = (String) request.getAttribute(SearchConstant.SearchBuilderIdentifier.NAME
					.value());
			if (StringUtils.isEmpty(searchBuilderIdentifier)) {
				searchBuilderIdentifier = (String) request.getAttribute(CoreSpringConstant.SPRING_ATTRIBUTE_URL);
			}
			List<? extends Selectable> valueList = regexParameterProvider.getValues(
					StringUtils.remove(parameterName, "serverSide."), searchBuilderIdentifier, request);
			if (valueList != null) {
				returnValue = new String[valueList.size()];
				int index = 0;
				for (Selectable selectable : valueList) {
					if (StringUtils.isNotBlank(selectable.getIdentifyingValue())) {
						returnValue[index++] = replaceChar(selectable.getIdentifyingValue());
					}
				}
			}

		} else if (parameterName.equals("user.LANGUAGE")) {
			parameterValue = LocaleContextHolder.getLocale().getLanguage();
			if (parameterValue != null) {
				returnValue = new String[] { replaceChar(parameterValue.toString()) };
			}

		} else if (parameterName.contains("config.")) {
			parameterValue = ConfigurationConstant.configurationMap.get(StringUtils.remove(parameterName, "config."));
			if (parameterValue != null) {
				returnValue = new String[] { replaceChar(parameterValue.toString()) };
			}

		} else {
			throw new IllegalArgumentException(
					"Parameter name must match param.*, request.*, application.*, serverSide.*, session.*, user.LANGUAGE, config.*");
		}
		return returnValue;
	}

	public static String getParsedMetaQueryParameterValue(String metaQuery, HttpServletRequest request,
			RegexParameterProvider regexParameterProvider) {
		if (metaQuery == null)
			return null;
		metaQuery = "(" + metaQuery + ")";

		List<String> parameterNameList = RegexParameterResolver.getParameterNameList(metaQuery);

		Map<String, String> replaceToken = new HashMap<String, String>();
		replaceToken.put("&", "<![CDATA[AND]]>");
		replaceToken.put("|", "<![CDATA[OR]]>");
		replaceToken.put("^", "<![CDATA[NOT]]>");
		replaceToken.put("(", "<![CDATA[LPAREN]]>");
		replaceToken.put(")", "<![CDATA[RPAREN]]>");

		for (String parameterName : parameterNameList) {
			String[] parameterValues = getParameterValue(parameterName, request, regexParameterProvider);

			if (!ArrayUtils.isEmpty(parameterValues) && StringUtils.isNotBlank(parameterValues[0])
					&& !"null".equals(parameterValues[0])) {
				String parameterValue = null;

				// lista di valori su singola option di select
				if (parameterValues.length == 1) {
					parameterValue = parameterValues[0];
					if (parameterValue != null) {
						if (parameterValue.startsWith("ARRAY[")) {
							parameterValues = parameterValue.replace("ARRAY[", "").replace("]", "").split(",");
						}
					}
				}

				if (parameterValues.length == 1) {
					parameterValue = parameterValues[0];
				} else {
					String pattern = ".*\\s*'\\s*\\$\\{" + parameterName.replace(".", "\\.") + "\\}\\s*'\\s*.*";
					boolean stringDelimiterDetected = metaQuery.matches(pattern);
					StringBuffer result = new StringBuffer();
					for (String s : parameterValues) {
						if (!"null".equals(s)) {
							if (stringDelimiterDetected) {
								s = "'" + s + "'";
							}
							result.append(s).append(",");
						}
					}
					if (stringDelimiterDetected)
						parameterValue = StringUtils.substringAfter(
								StringUtils.substringBeforeLast(result.toString(), "',"), "'");
					else
						parameterValue = StringUtils.substringAfter(
								StringUtils.substringBeforeLast(result.toString(), ","), "");
				}
				Iterator iterator = replaceToken.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
					String _old = entry.getKey();
					String _new = entry.getValue();
					if (parameterValue.contains(_old)) {
						parameterValue = parameterValue.replace(_old, _new);
					}
				}
				metaQuery = StringUtils.replace(metaQuery, "${" + parameterName + "}", parameterValue);
			}

		}

		String normalizedQuery = QueryNormalizer.getNormalizedQuery(metaQuery);

		Iterator iterator = replaceToken.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
			String _old = entry.getKey();
			String _new = entry.getValue();
			if (normalizedQuery.contains(_new)) {
				normalizedQuery = normalizedQuery.replace(_new, _old);
			}
		}

		// effettuo la sostituzione delle parentesi quadre con le tonde
		// l'utilizzo delle quadre viene fatto solo perché la parentesi tonda
		// viene interpretata nella grammatica in uso come "alteratore" della
		// precedenza degli operatori e non con altri significati.
		// qualsiasi altro utilizzo della parentesi tonda deve sostituito con la
		// quadra
		if (StringUtils.isNotBlank(normalizedQuery))
			normalizedQuery = normalizedQuery.replace("[", "(").replace("]", ")");
		return normalizedQuery;

	}

	// public static String getTokenInQuery(String metaQuery) {
	// if (!metaQuery.startsWith("(")){
	// metaQuery="("+metaQuery+")";
	// }
	//
	// List<String>
	// parameterNameList=RegexParameterResolver.getParameterNameList(metaQuery);
	// for (String parameterName : parameterNameList) {
	// String parameterValue=getParameterValue(parameterName, request);
	// if (StringUtils.isNotBlank(parameterValue) &&
	// !"null".equals(parameterValue))
	// metaQuery = StringUtils.replace(metaQuery, "${" + parameterName + "}",
	// parameterValue);
	// }
	//
	// return QueryNormalizer.getNormalizedQuery(metaQuery);
	//
	// }

	public static void main(String[] args) {
		// String pattern="(.*)\\[(\\s*)'(\\s*)\\$\\{" +
		// "scheda.discriminator".replace(".", "\\.") +
		// "\\}(\\s*)'(\\s*)\\](.*)";
		String pattern = ".*\\[\\s*'\\s*\\$\\{" + "scheda.discriminator".replace(".", "\\.") + "\\}\\s*'\\s*\\].*";
		String sss = "not (['${scheda.discriminator}'] and altra or other)";
	}
}
