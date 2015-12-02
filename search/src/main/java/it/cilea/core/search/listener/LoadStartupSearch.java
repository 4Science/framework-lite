package it.cilea.core.search.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import it.cilea.core.search.factory.SearchStrategyFactory;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.search.strategy.impl.hibernate.HibernateSearchStrategy;
import it.cilea.core.search.strategy.impl.olap.OlapSearchStrategy;
import it.cilea.core.search.strategy.impl.solr.SolrSearchStrategy;
import it.cilea.core.search.strategy.impl.sql.SqlSearchStrategy;
import it.cilea.core.search.util.SearchUtil;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.service.WidgetService;

public class LoadStartupSearch extends ContextLoaderListener implements ServletContextListener {

	private Logger log = LoggerFactory.getLogger(LoadStartupSearch.class);

	public void contextInitialized(ServletContextEvent event) {

		ServletContext context = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

		SearchService searchService = (SearchService) ctx.getBean("searchService");
		WidgetService widgetService = (WidgetService) ctx.getBean("widgetService");
		try {
			SearchUtil.reload(widgetService, searchService);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HibernateSearchStrategy hibernateStrategy = (HibernateSearchStrategy) ctx.getBean("hibernateSearchStrategy");

		SearchStrategyFactory.setHibernateStrategy(hibernateStrategy);
		SearchStrategyFactory.setSolrStrategy(new SolrSearchStrategy());
		OlapSearchStrategy olap = new OlapSearchStrategy();
		olap.setContext(ctx);
		SearchStrategyFactory.setOlapStrategy(olap);

		SqlSearchStrategy sqlStrategy = (SqlSearchStrategy) ctx.getBean("sqlSearchStrategy");

		SearchStrategyFactory.setSqlStrategy(sqlStrategy);

		WidgetConstant.beanFactory = (BeanFactory) ctx;
		System.out.println(WidgetConstant.beanFactory);
	}
}
