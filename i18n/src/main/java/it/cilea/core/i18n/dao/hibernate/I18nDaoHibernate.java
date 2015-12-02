package it.cilea.core.i18n.dao.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import it.cilea.core.i18n.dao.I18nDao;
import it.cilea.core.i18n.model.I18n;
import it.cilea.core.i18n.model.I18nData;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

@Repository("i18nDao")
public class I18nDaoHibernate extends GenericDaoHibernate<I18n, Integer> implements I18nDao {

	@Autowired
	@Qualifier("staticMessageSource")
	private MessageSource staticMessageSource;

	public I18nDaoHibernate() {
		super(I18n.class);
	}

	public Map<String, Map<String, String>> getI18nMap(String context, boolean exact) {
		log.debug("loadTexts");
		Map<String, Map<String, String>> m = new HashMap<String, Map<String, String>>();
		if (exact) {
			// Effettuo una ricerca esatta
			if (context == null) {
				// Il contesto è null carico solo quelle di default
				this.getI8nMapDefault(m);
			} else {
				// Carico solo il le chiavi associate al contesto
				this.getI8nMapContext(m, context);
			}
		} else {
			// Prendo l'unione delle 2 mappe
			this.getI8nMapDefault(m);
			this.getI8nMapContext(m, context);
		}

		return m;
	}

	public Map<String, Map<String, String>> getI18nMap(String context) {
		return this.getI18nMap(context, false);
	}

	/**
	 * Carico dal DB SOLO le chiavi di defautl, cioè non associate a nessun
	 * contesto
	 * 
	 * @param m
	 */
	private void getI8nMapDefault(Map<String, Map<String, String>> m) {
		String queryString = "from I18n where context is null";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (I18n i18n : (List<I18n>) query.list()) {
			m.put(i18n.getKey(), new HashMap<String, String>());
			m.get(i18n.getKey()).put("default", i18n.getValue());
		}

		queryString = "select i18n.key,lower(md.discriminator),md.stringValue ";
		queryString += "from I18nData md ";
		queryString += "left join md.i18n i18n ";
		queryString += "where md.discriminator like 'value_%' and ";
		queryString += "i18n.context is null ";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (Object[] string : (List<Object[]>) query.list())
			m.get((String) string[0]).put(StringUtils.remove((String) string[1], "value_"), (String) string[2]);
	}

	/**
	 * Carico dal DB solo le chiavi del contesto passato come argomento
	 * 
	 * @param m
	 * @param context
	 */
	private void getI8nMapContext(Map<String, Map<String, String>> m, String context) {
		String queryString = "from I18n where context='" + context + "'";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		for (I18n i18n : (List<I18n>) query.list()) {
			if (m.get(i18n.getKey()) == null)
				m.put(i18n.getKey(), new HashMap<String, String>());
			m.get(i18n.getKey()).put("default", i18n.getValue());
		}

		queryString = "select i18n.key,lower(md.discriminator),md.stringValue ";
		queryString += "from I18nData md ";
		queryString += "left join md.i18n i18n ";
		queryString += "where md.discriminator like 'value_%' and ";
		queryString += "i18n.context='" + context + "' ";
		query = getSessionFactory().getCurrentSession().createQuery(queryString);
		List<Object[]> list = (List<Object[]>) query.list();
		for (Object[] string : list)
			m.get((String) string[0]).put(StringUtils.remove((String) string[1], "value_"), (String) string[2]);
	}

	public I18n getI18n(String key, String context) {

		// System.out.println("----> public I18n getI18n(String key, String
		// context) {");

		// String queryString = "from I18n where key=:key";
		String queryString = "from I18n a left join fetch a.dataSet where key=:key";

		if (StringUtils.isNotBlank(context)) {
			queryString += " and context = :context";
		}
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query.setParameter("key", key);
		if (StringUtils.isNotBlank(context)) {
			query.setParameter("context", context);
		}

		return (I18n) query.uniqueResult();
	}

	public Object test() {

		String queryString = "select count (i18n) from I18n i18n";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		return query.uniqueResult();
	}

	public I18n getI18nExactContext(String key, String context) {

		// System.out.println("----> public I18n getI18n(String key, String
		// context) {");

		// String queryString = "from I18n where key=:key";
		String queryString = "from I18n a left join fetch a.dataSet where key=:key";

		if (StringUtils.isNotBlank(context)) {
			queryString += " and context = :context";
		} else {
			queryString += " and ( context = '' or context is null ) ";
		}

		// System.out.println(queryString);

		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query.setParameter("key", key);
		if (StringUtils.isNotBlank(context)) {
			query.setParameter("context", context);
		}

		I18n i18n = (I18n) query.uniqueResult();
		// if (i18n != null) {
		// System.out.println(i18n.getKey() + " " + i18n.getContext() + " " +
		// i18n.getValue());
		// }
		return i18n;
	}

	public I18n searchI18n(String id) {

		String queryString = "from I18n a left join fetch a.dataSet where id=:id";

		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		query.setParameter("id", new Integer(id));

		List<I18n> l = query.list();
		if (l.size() > 0) {
			return (I18n) l.get(0);
		} else {
			return null;
		}

	}

	public String searchI18nId(String key, String context) {

		String queryString = "from I18n where key=:key  ";
		if (StringUtils.isNotBlank(context)) {
			queryString += " and context = :context";
		} else {
			queryString += " and context is null ";
		}

		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);

		query.setParameter("key", key);

		if (StringUtils.isNotBlank(context)) {
			query.setParameter("context", context);
		}

		List<I18n> l = query.list();

		if (l.size() > 0) {
			I18n i18n = l.get(0);
			return i18n.getId().toString();
		} else {
			return null;
		}

	}

	public void deleteObjectI18n(String id) {

		I18n i18n = searchI18n(id);
		i18n.setDataSet(null); // this way if set i18n.Map<String, String> for a
								// single children delete
								// ...something wrong on i18n entity??????????

		if (i18n != null) {
			getSessionFactory().getCurrentSession().delete(i18n);
		}

	}

	public void saveUpdateI18nData(String id, String discriminator, String value) {

		I18n I18n = searchI18n(id);

		if (I18n != null) {

			String queryString = "select md from I18nData md where md.i18n = :I18n and discriminator = :discriminator ";

			Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
			query.setParameter("I18n", I18n);
			query.setParameter("discriminator", discriminator);

			List<I18nData> l = query.list();

			I18nData i18nData;

			if (l.size() > 0) {
				i18nData = l.get(0);
				i18nData.setStringValue(value);
			} else {
				i18nData = new I18nData();
				I18nData.I18nDataId i18nDataId = new I18nData.I18nDataId();
				i18nDataId.setDiscriminator(discriminator);
				i18nDataId.setI18nId(Integer.valueOf(id));
				i18nData.setId(i18nDataId);
				i18nData.setStringValue(value);
				i18nData.setDiscriminator(discriminator);
				I18n.getDataSet().add(i18nData);
			}
			getSessionFactory().getCurrentSession().save(i18nData);
			// getSessionFactory().getCurrentSession().flush();
		}

	}

	public String searchI18nData(String id, String discriminator) {

		I18n I18n = searchI18n(id);

		String out = null;

		if (I18n != null) {

			String queryString = "select md from I18nData md where md.i18n = :I18n and discriminator = :discriminator ";

			Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
			query.setParameter("I18n", I18n);
			query.setParameter("discriminator", discriminator);

			List<I18nData> l = query.list();

			I18nData i18nData;

			if (l.size() > 0) {
				out = l.get(0).getStringValue();
			}

		}

		return out;
	}

	@Override
	public I18n save(I18n i18n) {
		addHistory(i18n);
		return super.save(i18n);
	}

	public Integer saveOrUpdateAndFlush(I18n i18n) {

		getSessionFactory().getCurrentSession().saveOrUpdate(i18n);
		getSessionFactory().getCurrentSession().flush();
		return i18n.getId();
	}

	private void addHistory(I18n i18n) {
		// Aggiungo 'OLD_VALUE

		// System.out.println("----------history");

		// if (!i18n.getStringMap().containsKey("old_custom")) {

		if (i18n.getI18nId() != null) {
			String checkQuerySQL = "SELECT value FROM i18n where id=:id";
			SQLQuery sqlQuery = getSessionFactory().getCurrentSession().createSQLQuery(checkQuerySQL);
			sqlQuery.setParameter("id", i18n.getId());
			String oldValue = (String) sqlQuery.uniqueResult();

			if (oldValue != null) {
				if (!oldValue.equals(i18n.getValue())) {
					i18n.getStringMap().put("old_custom", oldValue);
				}
			}

		}
		// }else {
		//
		// }

	}

	public void setStaticMessageSource(MessageSource staticMessageSource) {
		this.staticMessageSource = staticMessageSource;
	}

}
