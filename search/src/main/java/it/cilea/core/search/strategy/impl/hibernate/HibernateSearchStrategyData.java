package it.cilea.core.search.strategy.impl.hibernate;

import it.cilea.core.annotation.OrderByCustom;
import it.cilea.core.search.HibernateModelAttributeNameWrapper;
import it.cilea.core.search.SearchConstant.SearchBuilderParameterName;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.regex.RegexParameterResolver;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.RegexParameterProviderImpl;
import it.cilea.core.search.strategy.SearchStrategyData;
import it.cilea.core.search.util.SearchUtil;
import it.cilea.core.util.AnnotationUtil;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;

public class HibernateSearchStrategyData extends SearchStrategyData {

	public enum DistinctType {
		NO_DISTINCT_CLAUSE, DISTINCT_CLAUSE, DISTINCT_CLAUSE_BLOB
	}

	private Class clazz;
	private String className;
	private String fullyQualifiedClassName;
	private String uncapitalizedClassName;
	private String dtoSelectClause;
	private List idList;
	private boolean orderByCustomDetected = false;
	private String filterClauseForOrderClauseWithOrderByCustom;

	// private boolean distinct;
	private DistinctType distinctType;
	/**
	 * This field contains the attributeName to apply the distinct clause on.
	 * This field must be used when having composite attribute (aka left join)
	 * If not defined the default is used: "id"
	 * 
	 */
	private String distinctAttributeName;

	@Override
	public void init(HttpServletRequest request) {
		// gestisco la sostituzione all'interno della query di ordinamento dei
		// valori
		// associati a OrderByCustom annotation

		if (this.filterClause != null) {
			List<String> sortAttributeNameList = RegexParameterResolver.getModelAttributeNameList(orderClause);
			for (String sortAttributeName : sortAttributeNameList) {
				// se il parametro corrente è fatto da 2 pezzi...
				if (sortAttributeName.split("\\.").length == 2) {
					// sortAttributeName è fully qualified
					// getOrderByCustom ragiona utilizzando solo il nome
					// dell'attributo effettivo

					// e se trovo definita un OrderByCustom annotation...
					OrderByCustom orderByCustom = AnnotationUtil.getOrderByCustom(clazz,
							sortAttributeName.split("\\.")[1]);
					if (orderByCustom != null) {
						orderByCustomDetected = true;
						String direction = null;
						// recupero il verso dell'ordinamento ASC/DESC presente
						// nell'ordinamento che utilizza
						// l'attributo sul quale è stato utilizzato
						// OrderByCustom
						direction = StringUtils.substringBefore(
								StringUtils.substringAfter(orderClause, "{" + sortAttributeName + "}"), ",");
						// elimino dalla clausola di ordinamento l'eventuale
						// verso di ordinamento
						// perché questo deve essere applicato su OGNI clausola
						// di ordinamento trovata
						// nell'OrderByCustom annotation
						this.orderClause = orderClause.replace("{" + sortAttributeName + "}" + direction, "{"
								+ sortAttributeName + "}");

						if (StringUtils.isBlank(direction))
							direction = " ASC ";
						// recupero dall'annotation l'ordinamento da applicare
						String orderByCustomAnnotationValue = orderByCustom.orderBy();
						// sostituisco il segnaposto ${DIRECTION} con quello
						// estratto in precedenza
						orderByCustomAnnotationValue = orderByCustomAnnotationValue.replace("${DIRECTION}", direction);
						this.orderClause = orderClause.replace("{" + sortAttributeName + "}",
								orderByCustomAnnotationValue);

						// aggiungo al filterClause gli eventuali parametri per
						// il where
						if (StringUtils.isNotBlank(orderByCustom.leftJoin())) {
							// non mettere nessuno spazio dopo & altrimenti la
							// grammatica attualmente
							// in uso non effettua la costruzione dell'AST in
							// modo corretto
							this.filterClauseForOrderClauseWithOrderByCustom = "(" + orderByCustom.leftJoin() + ")";
							this.filterClauseForOrderClauseWithOrderByCustom = RegexParameterResolver
									.getParsedMetaQueryParameterValue(this.filterClauseForOrderClauseWithOrderByCustom,
											request, new RegexParameterProviderImpl());
						}
					}
				}
			}
		}
		super.init(request);
	}

	public HibernateSearchStrategyData(SearchBuilder searchBuilder, HttpServletRequest request,
			SearchService searchService) {
		super(searchBuilder, request, searchService);

		Map<String, String> searchBuilderParameterMap = searchBuilder.getSearchBuilderParameterMap();
		String rootModelClass = searchBuilderParameterMap.get(SearchBuilderParameterName.ROOT_MODEL_CLASS.toString());
		String distinctType = searchBuilderParameterMap.get(SearchBuilderParameterName.DISTINCT_CLAUSE.toString());
		String distinctAttributeName = searchBuilderParameterMap.get(SearchBuilderParameterName.DISTINCT_ATTRIBUTE_NAME
				.toString());
		this.dtoSelectClause = searchBuilderParameterMap.get(SearchBuilderParameterName.DTO_SELECT_CLAUSE.toString());

		if (StringUtils.isBlank(rootModelClass)) {
			throw new IllegalStateException("The rootModelClass must be specified");
		}

		try {
			clazz = Class.forName(rootModelClass);
		} catch (ClassNotFoundException cnfe) {
			throw new IllegalStateException("The rootModelClass specified (" + rootModelClass
					+ ") was not found in classpath");
		}

		if (StringUtils.isNotBlank(distinctType)) {
			for (DistinctType value : DistinctType.values()) {
				if (value.toString().equals(distinctType)) {
					this.distinctType = value;
				}
			}
			if (this.distinctType == null)
				throw new IllegalStateException("The distinctType specified " + distinctType + " is not supported.");
		} else {
			this.distinctType = DistinctType.NO_DISTINCT_CLAUSE;
		}

		if (StringUtils.isNotBlank(distinctAttributeName))
			this.distinctAttributeName = distinctAttributeName;
		else
			this.distinctAttributeName = "id";

		if (StringUtils.isBlank(this.sortDirection)) {
			this.sortDirection = " ASC ";
		} else
			this.sortDirection = " " + this.sortDirection;

		if (ArrayUtils.isEmpty(this.sortFieldList)) {
			this.orderClause = searchBuilder.getOrderClause();
		} else {
			if (this.sortFieldList.length == 1)
				this.orderClause = this.sortFieldList[0] + this.sortDirection;
			else {
				this.orderClause = StringUtils.join(this.sortFieldList, this.sortDirection + ",");
				this.orderClause += this.sortDirection;
			}
		}

	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public String getClassName() {
		if (className == null)
			className = SearchUtil.getClassNameOnly(clazz);
		return className;
	}

	public String getFullyQualifiedClassName() {
		if (fullyQualifiedClassName == null)
			fullyQualifiedClassName = clazz.getCanonicalName();
		return fullyQualifiedClassName;
	}

	public String getUncapitalizedClassName() {
		if (uncapitalizedClassName == null)
			uncapitalizedClassName = WordUtils.uncapitalize(getClassName());
		return uncapitalizedClassName;
	}

	public String getDistinctIdCountSelectClause() {
		if (StringUtils.isBlank(distinctAttributeName))
			throw new IllegalStateException("distinctAttributeName MUST BE defined");

		StringBuffer selectClause = new StringBuffer();
		selectClause.append(" distinct ");
		String idAttributeName = getUncapitalizedClassName() + "." + distinctAttributeName;
		selectClause.append(idAttributeName);
		return selectClause.toString();
	}

	public String getDistinctIdSelectClause() {
		if (StringUtils.isBlank(distinctAttributeName))
			throw new IllegalStateException("distinctAttributeName MUST BE defined");

		StringBuffer selectClause = new StringBuffer();
		selectClause.append(" distinct ");

		String idAttributeName = getUncapitalizedClassName() + "." + distinctAttributeName;

		selectClause.append(idAttributeName);

		// devo aggiungere tra gli attributi di select distinct anche gli
		// attributi dell'ordinamento
		// altrimenti viene generata una query non corretta (se si hanno left
		// join
		// gli attr di ordinamento devono essere inclusi nella select pena non
		// esecuzione query)

		String orderClause = getOrderClause();
		List<String> orderClauseAttribute = RegexParameterResolver.getModelAttributeNameList(orderClause);
		for (String sortAttributeName : orderClauseAttribute) {
			// se l'attributo di ordinamento è l'id allora non lo aggiungo nella
			// select perché c'è già
			if (!idAttributeName.equals(sortAttributeName)) {
				HibernateModelAttributeNameWrapper wrapper = new HibernateModelAttributeNameWrapper(sortAttributeName);
				selectClause.append("," + wrapper.getAttributeName());
			}
		}

		return selectClause.toString();
	}

	private String getDistinctClause() {
		DistinctType distinctType = getDistinctType();
		if (DistinctType.DISTINCT_CLAUSE.equals(distinctType) || DistinctType.DISTINCT_CLAUSE_BLOB.equals(distinctType)) {
			return " distinct ";
		} else
			return " ";
	}

	public String getSelectAndGroupObject() {
		StringBuffer queryString = new StringBuffer();
		queryString.append(getDistinctClause());
		if (StringUtils.isNotBlank(getGroupClause())) {
			List<String> groupParameterSet = RegexParameterResolver.getModelAttributeNameList(getGroupClause());

			String groupFinale = getGroupClause();
			for (String string : groupParameterSet) {
				groupFinale = StringUtils.replace(groupFinale, "{" + string + "}",
						SearchUtil.getAliasedSelectValue(string));
			}

			queryString.append(groupFinale);

		}
		if (StringUtils.isNotBlank(getSelectClause())) {
			if (StringUtils.isNotBlank(getGroupClause()))
				queryString.append(",");

			List<String> selectParameterSet = RegexParameterResolver.getModelAttributeNameList(getSelectClause());

			String selectFinale = getSelectClause();
			for (String string : selectParameterSet) {
				selectFinale = StringUtils.replace(selectFinale, "{" + string + "}",
						SearchUtil.getAliasedSelectValue(string));
			}

			// List<String>selectParameterSet=
			// RegexParameterResolver.getModelAttributeNameList(data.getSelectClause());
			//
			//
			// String select =
			// StringUtils.replace(StringUtils.replace(data.getSelectClause(),
			// ")", ""), "(", "");
			// Set<String> selectSet = new LinkedHashSet<String>();
			// for (String string : StringUtils.split(select, ",")) {
			// selectSet.add(SearchUtil.getAliasedSelectValue(string));
			// }
			// Set<String> selectSet = new LinkedHashSet<String>();
			// for (String string : selectParameterSet) {
			// selectSet.add(SearchUtil.getAliasedSelectValue(string));
			// }
			//

			queryString.append(selectFinale);

		}
		return queryString.toString();
	}

	public String getStandardSelectClause() {
		StringBuffer selectClause = new StringBuffer();
		selectClause.append(getDistinctClause());
		if (StringUtils.isNotBlank(dtoSelectClause))
			selectClause.append(dtoSelectClause);
		else
			selectClause.append(getUncapitalizedClassName());
		return selectClause.toString();
	}

	public String getNonDistinctSelectClause() {
		StringBuffer selectClause = new StringBuffer();
		if (StringUtils.isNotBlank(dtoSelectClause))
			selectClause.append(dtoSelectClause);
		else
			selectClause.append(getUncapitalizedClassName());
		return selectClause.toString();
	}

	public String getCountClause() {
		StringBuffer countClause = new StringBuffer(" count (");
		if (DistinctType.DISTINCT_CLAUSE.equals(distinctType) || DistinctType.NO_DISTINCT_CLAUSE.equals(distinctType))
			countClause.append(getStandardSelectClause());
		else
			countClause.append(getDistinctIdCountSelectClause());
		countClause.append(")");
		return countClause.toString();
	}

	public String getDistinctAttributeName() {
		return distinctAttributeName;
	}

	public DistinctType getDistinctType() {
		return distinctType;
	}

	public List getIdList() {
		return idList;
	}

	public void setIdList(List idList) {
		this.idList = idList;
	}

	public String getDtoSelectClause() {
		return dtoSelectClause;
	}

	public String getFilterClauseForOrderClauseWithOrderByCustom() {
		return filterClauseForOrderClauseWithOrderByCustom;
	}

	public void setFilterClauseForOrderClauseWithOrderByCustom(String filterClauseForOrderClauseWithOrderByCustom) {
		this.filterClauseForOrderClauseWithOrderByCustom = filterClauseForOrderClauseWithOrderByCustom;
	}

	public Boolean isObjectsList() {
		if (StringUtils.isNotBlank(getGroupClause()) || StringUtils.isNotBlank(getSelectClause()))
			return true;
		return false;
	}
}
