package it.cilea.core.view.service;

import it.cilea.core.spring.util.MessageUtil;
import it.cilea.core.view.dao.ViewBuilderDao;
import it.cilea.core.view.dao.ViewBuilderWidgetLinkDao;
import it.cilea.core.view.model.ViewBuilder;
import it.cilea.core.view.model.ViewBuilderWidgetLink;
import it.cilea.core.widget.service.WidgetService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ViewService {
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private WidgetService widgetService;
	@Autowired
	private ViewBuilderDao viewBuilderDao;
	@Autowired
	private ViewBuilderWidgetLinkDao viewBuilderWidgetLinkDao;

	// ViewBuilder
	public List<ViewBuilder> initViewBuilderList() {
		return viewBuilderDao.initViewBuilderList();
	}

	public ViewBuilder getViewBuilder(Integer viewBuilderId) {
		return viewBuilderDao.get(viewBuilderId);
	}

	public ViewBuilder saveOrUpdate(ViewBuilder viewBuilder) {
		return viewBuilderDao.save(viewBuilder);
	}

	public void deleteViewBuilder(Integer viewBuilderId) {
		viewBuilderDao.remove(viewBuilderId);
	}

	public List<ViewBuilder> getViewBuilderList() {
		return viewBuilderDao.findByNamedQuery("ViewBuilder.findAll");
	}

	// ViewBuilderWidgetLink
	public void saveOrUpdate(ViewBuilderWidgetLink viewBuilderWidgetLink) {
		viewBuilderWidgetLinkDao.save(viewBuilderWidgetLink);
	}

	public void deleteViewBuilderWidgetLink(Integer viewBuilderWidgetLinkId) {
		viewBuilderWidgetLinkDao.remove(viewBuilderWidgetLinkId);
	}

	public void setViewBuilderDao(ViewBuilderDao viewBuilderDao) {
		this.viewBuilderDao = viewBuilderDao;
	}

	public void setViewBuilderWidgetLinkDao(ViewBuilderWidgetLinkDao viewBuilderWidgetLinkDao) {
		this.viewBuilderWidgetLinkDao = viewBuilderWidgetLinkDao;
	}

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	public void setWidgetService(WidgetService widgetService) {
		this.widgetService = widgetService;
	}

	public WidgetService getWidgetService() {
		return widgetService;
	}
}
