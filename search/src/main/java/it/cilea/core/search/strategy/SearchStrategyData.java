package it.cilea.core.search.strategy;

import it.cilea.core.CoreConstant;
import it.cilea.core.authorization.context.AuthorizationUserHolder;
import it.cilea.core.authorization.model.impl.UserDetail;
import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.SearchConstant.RequestStandardParameter;
import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.regex.RegexParameterProvider;
import it.cilea.core.search.regex.RegexParameterResolver;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.WidgetDictionary;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class SearchStrategyData {
	protected String selectClause;
	protected String selectClauseOriginal;
	protected String filterClause;
	protected String withClause;
	protected String validationClause;
	protected String orderClause;
	protected String groupClause;
	protected String groupClauseOriginal;
	private boolean fullSelectClause;
	private boolean fullGroupClause;
	protected Map<String, Collection<String>> parameterMap = new HashMap<String, Collection<String>>();
	// protected String normalizedOrderClause;
	// protected String groupingClause;

	// protected String[] sort;
	// protected boolean inverse;
	protected Integer startingFrom;
	protected Integer pageSize;
	protected Integer page;
	protected String[] sortFieldList;

	protected String[] sortDirectionList;
	protected String sortDirection;
	protected Boolean post = false;

	protected SearchService searchService;

	public void init(HttpServletRequest request) {
		Enumeration en = request.getParameterNames();

		while (en.hasMoreElements()) {
			String paramName = (String) en.nextElement();
			Collection<String> collection = new ArrayList<String>();
			CollectionUtils.addAll(collection, request.getParameterValues(paramName));
			this.parameterMap.put(paramName, collection);
		}
		RegexParameterProvider regexParameterProviderImpl = new RegexParameterProviderImpl();
		// sse filterClause !=null allora faccio la risoluzione
		this.filterClause = RegexParameterResolver.getParsedMetaQueryParameterValue(this.filterClause, request,
				regexParameterProviderImpl);

		if (!post)
			return;

		if (StringUtils.isNotBlank(this.selectClause)) {

			this.selectClause = RegexParameterResolver.getParsedMetaQueryParameterValue(this.selectClause, request,
					regexParameterProviderImpl);

			this.selectClause = this.selectClause.trim();
			this.selectClause = StringUtils.removeStart(this.selectClause, "(");
			this.selectClause = StringUtils.removeEnd(this.selectClause, ")");

			if (!isFullSelectClause()) {
				String clauseTmp = "";
				UserDetail userDetail = AuthorizationUserHolder.getUser();
				for (String id : StringUtils.split(
						StringUtils.replace(StringUtils.replace(this.selectClause, "(", ""), ")", ""), ",")) {

					WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer.valueOf(id));
					if (userDetail == null || widgetDictionary.getAuthorizationResource() == null
							|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource().getIdentifier()))
						clauseTmp += widgetDictionary.getHiddenValueConfigurationReplaced() + ",";
				}
				this.selectClause = StringUtils.removeEnd(clauseTmp, ",");
			}
		}
		if (StringUtils.isNotBlank(this.groupClause)) {
			UserDetail userDetail = AuthorizationUserHolder.getUser();
			this.groupClause = RegexParameterResolver.getParsedMetaQueryParameterValue(this.groupClause, request,
					regexParameterProviderImpl);

			this.groupClause = this.groupClause.trim();
			this.groupClause = StringUtils.removeStart(this.groupClause, "(");
			this.groupClause = StringUtils.removeEnd(this.groupClause, ")");

			if (!isFullGroupClause()) {
				String clauseTmp = "";
				for (String id : StringUtils.split(
						StringUtils.replace(StringUtils.replace(this.groupClause, "(", ""), ")", ""), ",")) {
					WidgetDictionary widgetDictionary = WidgetConstant.widgetDictionaryMap.get(Integer.valueOf(id));
					if (userDetail == null || widgetDictionary.getAuthorizationResource() == null
							|| userDetail.hasAuthorities(widgetDictionary.getAuthorizationResource().getIdentifier()))
						clauseTmp += widgetDictionary.getHiddenValueConfigurationReplaced() + ",";
				}
				this.groupClause = StringUtils.removeEnd(clauseTmp, ",");
			}

		}
		if (StringUtils.isNotBlank(this.withClause)) {

			String withClauseParsed = "";
			for (String with : StringUtils.splitByWholeSeparator(this.withClause, "___")) {
				String prefix = StringUtils.trim(StringUtils.substringBefore(with, ":"));
				String suffix = StringUtils.substringAfter(with, ":");
				suffix = RegexParameterResolver.getParsedMetaQueryParameterValue(suffix, request,
						regexParameterProviderImpl);
				withClauseParsed += prefix + ":" + suffix + "___";
			}
			this.withClause = StringUtils.removeEnd(withClauseParsed, "___");
		}
		if (StringUtils.isNotBlank(this.validationClause)) {

			String paramError = "";
			String paramErrorCause = "";
			String errorMessage = "";
			String paramName = "";

			for (String string : StringUtils.splitByWholeSeparator(this.validationClause, "___")) {
				String prefix = StringUtils.trim(StringUtils.substringBefore(string, ":"));
				String suffix = StringUtils.substringAfter(string, ":");

				boolean supported = false;

				if (prefix.equals("notNull")) {
					supported = true;
					if (suffix.startsWith("param.")) {
						if (StringUtils.isBlank(request.getParameter(StringUtils.substringAfter(suffix, "param.")))
								|| "null".equals(request.getParameter(StringUtils.substringAfter(suffix, "param.")))) {
							paramError += string + ",";
							paramErrorCause += "notNull" + ",";
							errorMessage += "error.widget.notnull2" + ",";
							paramName += suffix.replace("param.", "") + ",";
						}
					} else
						throw new IllegalArgumentException("Validation clause invalid: param's supports only");
				}

				if (prefix.equals("isDate")) {
					supported = true;
					if (suffix.startsWith("param.")) {

						if (StringUtils.isBlank(request.getParameter(StringUtils.substringAfter(suffix, "param.")))
								|| "null".equals(request.getParameter(StringUtils.substringAfter(suffix, "param.")))) {
						} else {
							if (!CoreConstant.dataPattern.matcher(
									request.getParameter(StringUtils.substringAfter(suffix, "param."))).matches()) {
								paramError += string + ",";
								paramErrorCause += "isDate" + ",";
								errorMessage += "error.widget.isdate" + ",";
								paramName += suffix.replace("param.", "") + ",";
							}
						}

					} else
						throw new IllegalArgumentException("Validation clause invalid: param's supports only");
				}

				if (prefix.equals("isDbId")) {
					supported = true;
					if (suffix.startsWith("param.")) {

						if (StringUtils.isBlank(request.getParameter(StringUtils.substringAfter(suffix, "param.")))
								|| "null".equals(request.getParameter(StringUtils.substringAfter(suffix, "param.")))) {
						} else {

							if (!CoreConstant.dbIdPattern.matcher(
									request.getParameter(StringUtils.substringAfter(suffix, "param."))).matches()) {
								paramError += string + ",";
								paramErrorCause += "isDbId" + ",";
								errorMessage += "error.widget.isDbId" + ",";
								paramName += suffix.replace("param.", "") + ",";
							}
						}

					} else
						throw new IllegalArgumentException("Validation clause invalid: param's supports only");
				}

				if (prefix.equals("isInteger")) {
					supported = true;
					if (suffix.startsWith("param.")) {

						if (StringUtils.isBlank(request.getParameter(StringUtils.substringAfter(suffix, "param.")))
								|| "null".equals(request.getParameter(StringUtils.substringAfter(suffix, "param.")))) {
						} else {

							if (!CoreConstant.integerPattern.matcher(
									request.getParameter(StringUtils.substringAfter(suffix, "param."))).matches()) {
								paramError += string + ",";
								paramErrorCause += "isInteger" + ",";
								errorMessage += "error.widget.isInteger" + ",";
								paramName += suffix.replace("param.", "") + ",";
							}
						}

					} else
						throw new IllegalArgumentException("Validation clause invalid: param's supports only");
				}

				// isDateInterval:param.insertDate-usageDate

				if (prefix.equals("isDateInterval")) {
					supported = true;
					if (suffix.startsWith("param.")) {

						String interval = StringUtils.substringAfter(suffix, "param.");
						String data1 = interval.split("-")[0];
						String data2 = interval.split("-")[1];
						String data1Value = request.getParameter(data1);
						String data2Value = request.getParameter(data2);

						if (StringUtils.isBlank(data1Value) || "null".equals(data1Value)
								|| StringUtils.isBlank(data2Value) || "null".equals(data2Value)) {
						} else {

							if ((!CoreConstant.dataPattern.matcher(data1Value).matches())) {
							} else {
								if ((!CoreConstant.dataPattern.matcher(data2Value).matches())) {
								} else {
									try {
										Date d1 = (Date) CoreConstant.dateFormat.parseObject(data1Value);
										Date d2 = (Date) CoreConstant.dateFormat.parseObject(data2Value);
										if (d1.after(d2)) {
											paramError += string + ",";
											paramErrorCause += "isDateInterval" + ",";
											errorMessage += "error.widget.isDateInterval" + ",";
											paramName += data1 + "-" + data2 + ",";
										}
									} catch (ParseException e) {
									}

								}

							}

						}

					} else
						throw new IllegalArgumentException("Validation clause invalid: param's supports only");
				}

				if (!supported) {
					throw new IllegalArgumentException("Validation clause invalid: prefix: '" + prefix
							+ "' not supported");
				}

			}
			if (StringUtils.isNotBlank(paramError)) {
				paramError = StringUtils.removeEnd(paramError, ",");
				paramErrorCause = StringUtils.removeEnd(paramErrorCause, ",");
				errorMessage = StringUtils.removeEnd(errorMessage, ",");
				paramName = StringUtils.removeEnd(paramName, ",");
				throw new ValidationException(paramError + "___" + paramErrorCause + "___" + errorMessage + "___"
						+ paramName);
			}
		}

		// this.orderClause=RegexParameterResolver.getParsedMetaQueryParameterValue(this.orderClause,
		// request, regexParameterProviderImpl);
	}

	public SearchStrategyData(SearchBuilder searchBuilder, HttpServletRequest request, SearchService searchService) {

		if (request == null)
			throw new IllegalArgumentException("HttpServletRequest must be non null");

		this.filterClause = ConfigurationUtil.replaceText(StringUtils.replaceChars(
				StringUtils.replaceChars(searchBuilder.getFilterClause(), '\n', ' '), '\t', ' '));
		this.selectClause = searchBuilder.getSelectClause();
		this.selectClauseOriginal = searchBuilder.getSelectClause();
		this.groupClause = searchBuilder.getGroupClause();
		this.groupClauseOriginal = searchBuilder.getGroupClause();
		this.withClause = ConfigurationUtil.replaceText(StringUtils.replaceChars(
				StringUtils.replaceChars(searchBuilder.getWithClause(), '\n', ' '), '\t', ' '));
		this.validationClause = searchBuilder.getValidationClause();
		this.searchService = searchService;

		Map<String, String> searchBuilderParameterMap = searchBuilder.getSearchBuilderParameterMap();

		String fullSelectClauseParam = searchBuilderParameterMap.get(SearchBuilderParameterName.FULL_SELECT_CLAUSE
				.toString());
		if (StringUtils.isNotBlank(fullSelectClauseParam) && "true".equalsIgnoreCase(fullSelectClauseParam)) {
			setFullSelectClause(true);
		}

		String fullGroupClauseParam = searchBuilderParameterMap.get(SearchBuilderParameterName.FULL_GROUP_CLAUSE
				.toString());
		if (StringUtils.isNotBlank(fullGroupClauseParam) && "true".equalsIgnoreCase(fullGroupClauseParam)) {
			setFullGroupClause(true);
		}

		String posting = request.getParameter(RequestStandardParameter.POSTING.value());
		if (StringUtils.isBlank(posting)) {
			posting = (String) request.getAttribute(RequestStandardParameter.POSTING.value());
		}

		if (StringUtils.isNotBlank(posting)) {
			post = true;
		}

		String page = request.getParameter(RequestStandardParameter.PAGE.value());
		try {
			this.page = Integer.valueOf(page);
		} catch (NumberFormatException nfe) {
			this.page = 1;
		}

		this.sortFieldList = request.getParameterValues(RequestStandardParameter.SORT.value());
		if (sortFieldList != null) {
			List<String> sortFieldList2 = new ArrayList<String>();
			for (String string : sortFieldList) {
				String[] stringArray = StringUtils.split(string, ",");
				for (String string2 : stringArray)
					sortFieldList2.add(string2);
			}
			Set<String> sortFieldCheckedSet = new LinkedHashSet<String>();
			String sortNow = "";
			for (String string : sortFieldList2) {
				sortNow += string;
				if (checkSortString(sortNow)) {
					sortFieldCheckedSet.add(sortNow);
					sortNow = "";
				} else
					sortNow += ",";
			}

			sortFieldList = new String[sortFieldCheckedSet.size()];
			sortFieldCheckedSet.toArray(sortFieldList);
		}
		/*
		 * USED ONLY FOR SOLR STRATEGY TO BE EXTENDED TO OTHER STRATEGIES
		 */
		this.sortDirectionList = request.getParameterValues(RequestStandardParameter.SORT_DIRECTION.value());
		if (sortDirectionList != null) {
			List<String> sortDirectionFieldList2 = new ArrayList<String>();
			for (String string : sortDirectionList) {
				String[] stringArray = StringUtils.split(string, ",");
				for (String string2 : stringArray)
					sortDirectionFieldList2.add(string2.trim());
			}

			sortDirectionList = new String[sortDirectionFieldList2.size()];
			sortDirectionFieldList2.toArray(sortDirectionList);
		}

		if (sortDirectionList != null && sortDirectionList.length == 1)
			this.sortDirection = request.getParameter(RequestStandardParameter.SORT_DIRECTION.value());

		String pageSize = request.getParameter(RequestStandardParameter.PAGE_SIZE.value());
		// if pageSize is retrieved from query string
		// the max allowed value is MAX_PAGE_SIZE_FROM_QUERY_STRING elements
		if (StringUtils.isNotEmpty(pageSize)) {
			try {
				this.pageSize = new Integer(pageSize);
				if (this.pageSize > SearchConstant.MAX_PAGE_SIZE_FROM_QUERY_STRING)
					throw new IllegalArgumentException("Parameter pageSize MUST BE  less then or equals to "
							+ SearchConstant.MAX_PAGE_SIZE_FROM_QUERY_STRING);
			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("Parameter pageSize MUST BE  a integer");
			}
		}
		if (this.pageSize == null) {
			if (isExport(request)) {
				this.pageSize = SearchConstant.MAX_PAGE_SIZE_EXPORT;
				this.page = 1;
			} else {
				this.pageSize = searchBuilder.getPageSize();
				if (this.pageSize == null)
					this.pageSize = SearchConstant.DEFAULT_PAGE_SIZE;
			}
		}

		startingFrom = this.pageSize * (this.page - 1);

	}

	private boolean checkSortString(String sort) {
		if (StringUtils.countMatches(sort, "(") != StringUtils.countMatches(sort, ")"))
			return false;
		if (StringUtils.countMatches(sort, "[") != StringUtils.countMatches(sort, "]"))
			return false;
		if (StringUtils.countMatches(sort, "{") != StringUtils.countMatches(sort, "}"))
			return false;
		return true;
	}

	public boolean isExport(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<String> keySet = parameterMap.keySet();

		for (String key : keySet) {
			if (key.startsWith("d-") && key.endsWith("-e")) {
				return true;
			}
		}
		return false;
	}

	public Collection<String> getParameterValues(String parameterName) {
		return parameterMap.get(parameterName);
	}

	public String getParameter(String parameterName) {
		if (parameterMap.get(parameterName) != null) {
			return StringUtils.join(parameterMap.get(parameterName), ",");
		}
		return null;
	}

	public String getFilterClause() {
		return filterClause;
	}

	public void setFilterClause(String query) {
		this.filterClause = query;
	}

	public int getStartingFrom() {
		return startingFrom;
	}

	public void setStartingFrom(int startingFrom) {
		this.startingFrom = startingFrom;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Boolean getPost() {
		return post;
	}

	public void setPost(Boolean post) {
		this.post = post;
	}

	public String getOrderClause() {
		return orderClause;
	}

	public void setOrderClause(String orderClause) {
		this.orderClause = orderClause;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public String[] getSortFieldList() {
		return sortFieldList;
	}

	public String getSelectClause() {
		return selectClause;
	}

	public void setSelectClause(String selectClause) {
		this.selectClause = selectClause;
	}

	public String getGroupClause() {
		return groupClause;
	}

	public void setGroupClause(String groupClause) {
		this.groupClause = groupClause;
	}

	public String getWithClause() {
		return withClause;
	}

	public void setWithClause(String withClause) {
		this.withClause = withClause;
	}

	public String getValidationClause() {
		return validationClause;
	}

	public void setValidationClause(String validationClause) {
		this.validationClause = validationClause;
	}

	public String getSelectClauseOriginal() {
		return selectClauseOriginal;
	}

	public String getGroupClauseOriginal() {
		return groupClauseOriginal;
	}

	public Map<String, Collection<String>> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(Map<String, Collection<String>> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public String[] getSortDirectionList() {
		return sortDirectionList;
	}

	public void setSortDirectionList(String[] sortDirectionList) {
		this.sortDirectionList = sortDirectionList;
	}

	public boolean isFullSelectClause() {
		return fullSelectClause;
	}

	public void setFullSelectClause(boolean fullSelectClause) {
		this.fullSelectClause = fullSelectClause;
	}

	public boolean isFullGroupClause() {
		return fullGroupClause;
	}

	public void setFullGroupClause(boolean fullGroupClause) {
		this.fullGroupClause = fullGroupClause;
	}
}
