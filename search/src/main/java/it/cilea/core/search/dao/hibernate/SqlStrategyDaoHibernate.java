package it.cilea.core.search.dao.hibernate;

import it.cilea.core.search.SqlModelAttributeNameWrapper;
import it.cilea.core.search.dao.SqlStrategyDao;
import it.cilea.core.search.regex.RegexParameterResolver;
import it.cilea.core.search.strategy.impl.sql.SqlSearchStrategyData;
import it.cilea.core.search.strategy.impl.sql.SqlSearchStrategyData.DistinctType;
import it.cilea.core.search.util.SearchUtil;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("sqlStrategyDao")
public class SqlStrategyDaoHibernate implements SqlStrategyDao {
    
    @Qualifier("sessionFactorySur")
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static final Logger log = LoggerFactory.getLogger(SqlStrategyDao.class);

	private enum QueryType {
		COUNT_QUERY, DISTINCT_COUNT_QUERY, DISTINCT_COUNT_ID_QUERY, LIST_QUERY, DISTINCT_LIST_QUERY, DISTINCT_ID_LIST_QUERY, IDS_BASED_DISTINCT_LIST_QUERY, COUNT_QUERY_OBJECTS, LIST_QUERY_OBJECTS
	}

	public Long getResultCount(SqlSearchStrategyData data) {
		String queryString = null;

		if (data.isObjectsList()) {
			// query senza model class (con select o group widget)
			queryString = getQueryString(data, QueryType.LIST_QUERY_OBJECTS);
		} else if (DistinctType.NO_DISTINCT_CLAUSE.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.LIST_QUERY);
		} else if (DistinctType.DISTINCT_CLAUSE.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.DISTINCT_LIST_QUERY);
		} else if (DistinctType.DISTINCT_CLAUSE_BLOB.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.DISTINCT_ID_LIST_QUERY);

		} else
			throw new IllegalStateException(
					"When using SqlStrategyData MUST BE specified the DistinctType to use: NO_DISTINCT_CLAUSE, DISTINCT_CLAUSE, DISTINCT_CLAUSE_BLOB");

		queryString = "select count(*) from (" + queryString + ")a";
		queryString = SearchUtil.unescapeBracket(queryString);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(queryString);
		return ((Number) query.uniqueResult()).longValue();

	}

	public List<?> getResultList(SqlSearchStrategyData data) {

		List resultList = new ArrayList();
		String queryString = null;

		if (data.isObjectsList()) {
			// query senza model class (con select o group widget)
			queryString = getQueryString(data, QueryType.LIST_QUERY_OBJECTS);
		} else if (DistinctType.NO_DISTINCT_CLAUSE.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.LIST_QUERY);
		} else if (DistinctType.DISTINCT_CLAUSE.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.DISTINCT_LIST_QUERY);
		} else if (DistinctType.DISTINCT_CLAUSE_BLOB.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.DISTINCT_ID_LIST_QUERY);

		} else
			throw new IllegalStateException(
					"When using SqlStrategyData MUST BE specified the DistinctType to use: NO_DISTINCT_CLAUSE, DISTINCT_CLAUSE, DISTINCT_CLAUSE_BLOB");
		queryString = SearchUtil.unescapeBracket(queryString);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(queryString);

		if (data.getPageSize() != 0) {
			query.setFirstResult(data.getStartingFrom());
			query.setMaxResults(data.getPageSize());
		}

		resultList = query.list();

		// sto utilizzando l'approccio con doppia query
		// (estrazione degli id distinti + seconda queryper recuperare oggetti
		// veri)
		// in questo caso resultList conterrà la lista degli id estratti.
		if (DistinctType.DISTINCT_CLAUSE_BLOB.equals(data.getDistinctType())) {
			// se la dimensione della lista degli id estratti (resultList) è
			// <1000 allora eseguo
			// un'unica query specificando la lista totale degli id nell'oggetto
			// SqlSearchStrategyData
			// passato come parametro.
			// Questa limitazione è dovuta al fatto che gli id della lista
			// vengono implosi in una stringa
			// separata da virgole e utilizzata come contenuto della clausola
			// "IN" HQL/SQL che ha le note
			// limitazioni

			if (resultList.size() == 0) {
				return new ArrayList();
			}
			if (resultList.size() < 1000) {
				data.setIdList(resultList);
				queryString = getQueryString(data, QueryType.IDS_BASED_DISTINCT_LIST_QUERY);
				queryString = SearchUtil.unescapeBracket(queryString);
				query = sessionFactory.getCurrentSession().createQuery(queryString);
				// non mi serve gestire la paginazione a questo livello perché
				// l'ho già fatto con
				// la query precedente
				query.setFirstResult(0);
				query.setMaxResults(Integer.MAX_VALUE);
				resultList = query.list();
			} else {
				// creo una nuova lista di id a partire da quella estratta in
				// precedenza
				ArrayList idList = new ArrayList(resultList);
				// resetto resultList che alla fine dell'esecuzione conterrà il
				// risultato
				// effettivo della ricerca (oggetti modello)
				resultList = new ArrayList();

				// eseguo nIteration di query dove n al primo intero maggiore di
				// idList.size()/1000
				// quindi ogni query avrà al max 1000 elementi nella clausola IN
				int size = resultList.size();

				int nIteration = new Double(Math.ceil(size / 1000)).intValue();
				for (int i = 0; i < nIteration; i++) {
					int startIndex = 1000 * i;
					int upperBoundIndex = 1000 * (i + 1);
					int endIndex = (upperBoundIndex < size) ? upperBoundIndex : size;

					// metto nell'oggetto SqlSearchStrategyData la
					// porzione di lista
					// di id che mi servono per costruire la query attuale

					data.setIdList(idList.subList(startIndex, endIndex));

					queryString = getQueryString(data, QueryType.IDS_BASED_DISTINCT_LIST_QUERY);
					queryString = SearchUtil.unescapeBracket(queryString);
					query = sessionFactory.getCurrentSession().createQuery(queryString);
					query.setFirstResult(0);
					query.setMaxResults(Integer.MAX_VALUE);
					resultList.addAll(query.list());
				}
			}
		}
		return resultList;
	}

	public String getSqlList(SqlSearchStrategyData data) {

		String queryString = null;

		if (data.isObjectsList()) {
			// query senza model class (con select o group widget)
			queryString = getQueryString(data, QueryType.LIST_QUERY_OBJECTS);
		} else if (DistinctType.NO_DISTINCT_CLAUSE.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.LIST_QUERY);
		} else if (DistinctType.DISTINCT_CLAUSE.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.DISTINCT_LIST_QUERY);
		} else if (DistinctType.DISTINCT_CLAUSE_BLOB.equals(data.getDistinctType())) {
			queryString = getQueryString(data, QueryType.DISTINCT_ID_LIST_QUERY);

		} else
			throw new IllegalStateException(
					"When using SqlStrategyData MUST BE specified the DistinctType to use: NO_DISTINCT_CLAUSE, DISTINCT_CLAUSE, DISTINCT_CLAUSE_BLOB");

		return queryString;
	}

	private String getLeftJoinClause(SqlSearchStrategyData data, QueryType queryType) {

		Set<String> joinSet = new LinkedHashSet<String>();

		// recupero la lista di tutti i nomi degli attributi fully qualified
		// presenti nella query
		// circondati dai marcatori "{}"

		// se sto lanciando la query con la clausola in con gli id allora NON
		// considero
		// la clausola where perché questo filtro è stato già applicato e ho già
		// gli id che
		// rispettano le condizioni
		// devo però inserire nella clausola where le eventuali condizioni
		// dovute all'utilizzo di
		// attributi di ordinamento sui quali sono state utilizzate le
		// orderbycustom annotation
		// il metodo getFilterClauseForOrderClauseWithOrderByCustom mi
		// restituisce la query già parsata
		// da utilizzare
		List<String> modelAttributeNameList = null;
		if (QueryType.IDS_BASED_DISTINCT_LIST_QUERY.equals(queryType)) {
			modelAttributeNameList = RegexParameterResolver.getModelAttributeNameList(data
					.getFilterClauseForOrderClauseWithOrderByCustom());
		}
		// in tutti gli altri casi devo eseguire la clausola di where standart +
		// le condizioni aggiuntive (eventuali)
		// che scaturiscono dall'uso di ordinamenti su attributi con
		// orderbycustom
		// metto AND e non & perché la query è stata già parsata in fase di
		// creazione dell'oggetto data
		else {
			String parsedCompositeQuery = data.getFilterClause();
			if (StringUtils.isNotBlank(data.getFilterClauseForOrderClauseWithOrderByCustom()))
				parsedCompositeQuery += " AND " + data.getFilterClauseForOrderClauseWithOrderByCustom();

			modelAttributeNameList = RegexParameterResolver.getModelAttributeNameList(parsedCompositeQuery);

			modelAttributeNameList.addAll(RegexParameterResolver.getModelAttributeNameList(data.getGroupClause()));
			modelAttributeNameList.addAll(RegexParameterResolver.getModelAttributeNameList(data.getSelectClause()));
		}

		// per ogni nome di attributo fully qualified recupero il set completo
		// di join
		for (String modelAttributeName : modelAttributeNameList) {
			SqlModelAttributeNameWrapper wrapper = new SqlModelAttributeNameWrapper(modelAttributeName);
			joinSet.addAll(wrapper.getJoinSet());
		}

		List<String> orderByList = RegexParameterResolver.getModelAttributeNameList(data.getOrderClause());

		for (String sortAttributeName : orderByList) {
			SqlModelAttributeNameWrapper wrapper = new SqlModelAttributeNameWrapper(sortAttributeName);
			joinSet.addAll(wrapper.getJoinSet());
		}
		// verifico la withClause
		if (StringUtils.isNotBlank(data.getWithClause()))
			for (String with : StringUtils.splitByWholeSeparator(data.getWithClause(), "___")) {
				String prefix = StringUtils.trim(StringUtils.substringBefore(with, ":"));
				String suffix = StringUtils.trim(StringUtils.substringAfter(with, ":"));
				Set<String> joinSetNew = new LinkedHashSet<String>();
				for (String join : joinSet) {
					if (join.endsWith(prefix))
						joinSetNew.add(join + " on " + suffix);
					else
						joinSetNew.add(join);
				}
				joinSet = new LinkedHashSet<String>();
				joinSet.addAll(joinSetNew);
			}

		// trasformo il set in una stringa
		StringBuffer joinString = new StringBuffer();
		for (String join : joinSet) {
			joinString.append(join);
		}
		return joinString.toString();
	}

	private String getWhereClause(SqlSearchStrategyData data, QueryType queryType) {

		String whereClause = data.getFilterClause();
		if (QueryType.IDS_BASED_DISTINCT_LIST_QUERY.equals(queryType)) {
			// se sto eseguendo la query basata su id allora recupero gli id
			// dalla lista e creo la
			// clausola SQL in
			if (CollectionUtils.isEmpty(data.getIdList())) {
				throw new IllegalStateException(
						"With queryType QueryType.IDS_BASED_DISTINCT_LIST_QUERY data.getIdList() cannot be null");
			}

			StringBuffer ids = new StringBuffer();
			// Dato che nella clausola di select posso avere altri field oltre
			// all'id (a causa della necessità di utilizzo di item per l'order
			// by)
			// allora considero i casi in cui mi arriva un'array di oggetti o un
			// oggetto solo
			Iterator it = data.getIdList().iterator();
			while (it.hasNext()) {
				Object element = it.next();
				if (element instanceof Object[]) {
					ids.append(((Object[]) element)[0]);
				} else {
					ids.append(element);
				}
				ids.append(",");
			}

			String idsString = StringUtils.substringBeforeLast(ids.toString(), ",");

			whereClause = data.getUncapitalizedClassName() + "." + data.getDistinctAttributeName() + " in ("
					+ idsString + ")";

		}

		// aggiungo anche le condizioni di where eventuali (già parsate)
		// dovute all'utilizzo di ordinamento su campi che prevedono
		// orderbycustom
		// metto AND e non & perché la query è stata già parsata in fase di
		// creazione dell'oggetto data
		if (StringUtils.isNotBlank(data.getFilterClauseForOrderClauseWithOrderByCustom())) {
			whereClause += " AND " + data.getFilterClauseForOrderClauseWithOrderByCustom();
		}

		List<String> modelAttributeNameList = RegexParameterResolver.getModelAttributeNameList(whereClause);
		for (String modelAttributeName : modelAttributeNameList) {
			SqlModelAttributeNameWrapper wrapper = new SqlModelAttributeNameWrapper(modelAttributeName);
			whereClause = whereClause.replace("{" + modelAttributeName + "}", wrapper.getAttributeName());
		}
		whereClause = StringUtils.replace(whereClause, "..", ".");
		return whereClause;
	}

	private String getOrderClause(SqlSearchStrategyData data) {
		String orderClause = data.getOrderClause();
		List<String> orderByList = RegexParameterResolver.getModelAttributeNameList(orderClause);

		for (String sortAttributeName : orderByList) {
			SqlModelAttributeNameWrapper wrapper = new SqlModelAttributeNameWrapper(sortAttributeName);
			orderClause = orderClause.replace("{" + sortAttributeName + "}", wrapper.getAttributeName());
		}
		return orderClause;
	}

	private String getGroupingClause(SqlSearchStrategyData data) {

		List<String> groupParameterSet = RegexParameterResolver.getModelAttributeNameList(data.getGroupClause());

		String groupFinale = data.getGroupClause();
		for (String string : groupParameterSet) {
			groupFinale = StringUtils.replace(groupFinale, "{" + string + "}",
					SearchUtil.getAliasedSelectValueSql(string));
		}

		return groupFinale;

	}

	private String getQueryString(SqlSearchStrategyData data, QueryType queryType) {
		StringBuffer queryString = new StringBuffer();
		queryString.append(" select ");

		if (QueryType.COUNT_QUERY.equals(queryType) || QueryType.DISTINCT_COUNT_QUERY.equals(queryType)
				|| QueryType.DISTINCT_COUNT_ID_QUERY.equals(queryType))
			queryString.append(data.getCountClause());
		else if (QueryType.LIST_QUERY.equals(queryType) || QueryType.DISTINCT_LIST_QUERY.equals(queryType))
			queryString.append(data.getStandardSelectClause());
		else if (QueryType.IDS_BASED_DISTINCT_LIST_QUERY.equals(queryType))
			queryString.append(data.getNonDistinctSelectClause());
		else if (QueryType.DISTINCT_ID_LIST_QUERY.equals(queryType))
			queryString.append(data.getDistinctIdSelectClause());
		else if (QueryType.COUNT_QUERY_OBJECTS.equals(queryType) || QueryType.LIST_QUERY_OBJECTS.equals(queryType))
			queryString.append(data.getSelectAndGroupObject());
		else
			throw new IllegalStateException("QueryType not supported");

		queryString.append(" from ");
		queryString.append(data.getClassName() + " " + data.getUncapitalizedClassName());
		queryString.append(getLeftJoinClause(data, queryType));

		String whereClause = getWhereClause(data, queryType);
		if (StringUtils.isNotBlank(whereClause)) {
			queryString.append(" where ");
			queryString.append(whereClause);
		}

		String groupingClause = getGroupingClause(data);
		if (StringUtils.isNotBlank(groupingClause)) {
			queryString.append(" group by ");
			queryString.append(groupingClause);
		}

		String orderClause = getOrderClause(data);
		if (StringUtils.isNotBlank(orderClause)) {
			queryString.append(" order by ");
			queryString.append(orderClause);
		}

		return queryString.toString();
	}

	@Override
	public Connection getConnection() {
		Connection conn = null;
		try {
			DataSource ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/bi");
			conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
