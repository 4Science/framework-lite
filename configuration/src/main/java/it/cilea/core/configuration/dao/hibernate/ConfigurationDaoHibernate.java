package it.cilea.core.configuration.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import it.cilea.core.configuration.command.ConfigurationSearchCommand;
import it.cilea.core.configuration.dao.ConfigurationDao;
import it.cilea.core.configuration.model.Configuration;
import it.cilea.core.spring.dao.hibernate.GenericDaoHibernate;

@Repository("configurationDao")
public class ConfigurationDaoHibernate extends GenericDaoHibernate<Configuration, Integer> implements ConfigurationDao {

	/**
	 * Constructor that sets the entity to Configuration.class.
	 */
	public ConfigurationDaoHibernate() {
		super(Configuration.class);
	}

	public List<Configuration> find(ConfigurationSearchCommand command, Integer pageSize) {
		String queryString = "select distinct (configuration)";
		queryString += this.constructConfigurationSearchQuery(command);
		if ("configuration.displayValue".equals(command.getSort()) || "displayValue".equals(command.getSort())) {
			queryString = StringUtils.replace(queryString, " distinct (configuration)", " configuration ");
			queryString += " order by case when language.stringValue is not null then language.stringValue else configuration.description end";
		} else
			queryString += " order by " + command.getSort();
		queryString += ("desc".equals(command.getDir())) ? " desc" : " asc";

		log.debug(queryString);

		Query query = createConfigurationSearchQuery(command, queryString);
		if (pageSize != 0) {
			query.setFirstResult((command.getPage() - 1) * pageSize);
			query.setMaxResults(pageSize);
		}
		return query.list();
	}

	public int test() throws Exception {
		final String queryString = "select count(distinct configuration.id) from Configuration configuration";
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	public int count(ConfigurationSearchCommand command) {
		String queryString = "select count(distinct configuration.id)";
		queryString += this.constructConfigurationSearchQuery(command);
		log.debug(queryString);
		Query query = createConfigurationSearchQuery(command, queryString);
		Long count = (Long) query.uniqueResult();
		return count.intValue();
	}

	private String constructConfigurationSearchQuery(ConfigurationSearchCommand command) {
		String queryString = " from Configuration configuration";
		if (command.getOnlyLeaf() != null && command.getOnlyLeaf())
			queryString += " left join configuration.childConfigurationLinkSet childConfigurationLinkSet";
		if ((command.getOnlyRoot() != null && command.getOnlyRoot()) || command.getParentConfigurationId() != null)
			queryString += " left join configuration.parentConfigurationLinkSet parentConfigurationLinkSet";
		if ("configuration.displayValue".equals(command.getSort()) || "displayValue".equals(command.getSort())
				|| StringUtils.isNotBlank(command.getDisplayValue()))
			queryString += " left join configuration.dataSet language with language.discriminator='description_"
					+ LocaleContextHolder.getLocale().getLanguage() + "'";
		queryString += " where ";
		if (StringUtils.isNotBlank(command.getDiscriminator()))
			queryString += " configuration.discriminator = :discriminator and";
		if (StringUtils.isNotBlank(command.getDescription()))
			queryString += " lower(configuration.description) like :description and";
		if (StringUtils.isNotBlank(command.getDescriptionExact()))
			queryString += " lower(configuration.description) = :description and";
		if (StringUtils.isNotBlank(command.getDisplayValue()))
			queryString += " lower(case when language.stringValue is not null then language.stringValue else configuration.description end) like :displayValue and";
		if (command.getConfigurationIds() != null && command.getConfigurationIds().length > 0)
			queryString += " configuration.id in (" + StringUtils.join(command.getConfigurationIds(), ",") + ") and";
		if (command.getParentConfigurationId() != null)
			queryString += " parentConfigurationLinkSet.parentId = :parentConfigurationId and";
		if (command.getOnlyRoot() != null && command.getOnlyRoot())
			queryString += " parentConfigurationLinkSet.parentId is null and";
		if (command.getOnlyLeaf() != null && command.getOnlyLeaf())
			queryString += " childConfigurationLinkSet.childId is null";
		queryString = StringUtils.removeEnd(queryString, " and");
		queryString = StringUtils.removeEnd(queryString, " where ");
		return queryString;
	}

	private Query createConfigurationSearchQuery(ConfigurationSearchCommand command, String queryString) {
		Query query = getSessionFactory().getCurrentSession().createQuery(queryString);
		if (StringUtils.isNotBlank(command.getDiscriminator()))
			query.setParameter("discriminator", command.getDiscriminator());
		if (StringUtils.isNotBlank(command.getDescription()))
			query.setParameter("description", "%" + StringUtils.lowerCase(command.getDescription()) + "%");
		if (StringUtils.isNotBlank(command.getDescriptionExact()))
			query.setParameter("description", StringUtils.lowerCase(command.getDescription()));
		if (command.getParentConfigurationId() != null)
			query.setParameter("parentConfigurationId", command.getParentConfigurationId());
		if (StringUtils.isNotBlank(command.getDisplayValue()))
			query.setParameter("displayValue", "%" + StringUtils.lowerCase(command.getDisplayValue()) + "%");

		return query;
	}

	public List<Configuration> getConfigurationList() {
		return null;
	}

}
