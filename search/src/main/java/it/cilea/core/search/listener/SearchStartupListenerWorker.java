package it.cilea.core.search.listener;

import it.cilea.core.listener.StartupListenerWorker;
import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.impl.hibernate.HibernateSearchStrategy;
import it.cilea.core.search.strategy.impl.olap.OlapSearchStrategy;
import it.cilea.core.search.strategy.impl.solr.SolrSearchStrategy;
import it.cilea.core.search.strategy.impl.sql.SqlSearchStrategy;
import it.cilea.core.search.util.SearchUtil;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.service.WidgetService;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContext;

public class SearchStartupListenerWorker implements StartupListenerWorker, BeanFactoryAware {

	@Override
	public void initialize(ServletContext servletContext, ApplicationContext applicationContext) throws Exception {

		SearchService searchService = (SearchService) applicationContext.getBean("searchService");
		WidgetService widgetService = (WidgetService) applicationContext.getBean("widgetService");
		SearchUtil.reload(widgetService, searchService);
		HibernateSearchStrategy hibernateStrategy = (HibernateSearchStrategy) applicationContext
				.getBean("hibernateSearchStrategy");

		SearchStrategyFactory.setHibernateStrategy(hibernateStrategy);
		SearchStrategyFactory.setSolrStrategy(new SolrSearchStrategy());
		OlapSearchStrategy olap = new OlapSearchStrategy();
		olap.setContext(applicationContext);
		SearchStrategyFactory.setOlapStrategy(olap);

		SqlSearchStrategy sqlStrategy = (SqlSearchStrategy) applicationContext.getBean("sqlSearchStrategy");

		SearchStrategyFactory.setSqlStrategy(sqlStrategy);

	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		WidgetConstant.beanFactory = beanFactory;
	}

	public Integer getPriority() {
		return 1;
	}
}