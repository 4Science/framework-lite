package it.cilea.core.widget.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.Selectable;
import it.cilea.core.spring.util.MessageUtil;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.WidgetConstant.OptionsWidgetPopulationType;
import it.cilea.core.widget.WidgetConstant.WidgetDictionaryType;
import it.cilea.core.widget.dao.WidgetDao;
import it.cilea.core.widget.dao.WidgetDictionaryDao;
import it.cilea.core.widget.model.Parameter;
import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.model.WidgetDictionary;

@Service
public class WidgetService {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private WidgetDao widgetDao;
	@Autowired
	private WidgetDictionaryDao widgetDictionaryDao;

	// Widget
	public List<Widget> initWidgetList() {
		return widgetDao.initWidgetList();
	}

	public Widget getWidget(Integer widgetId) {
		return widgetDao.get(widgetId);
	}

	public Widget saveOrUpdate(Widget widget) {
		return widgetDao.save(widget);
	}

	public Parameter saveOrUpdate(Parameter parameter) {
		return widgetDao.saveParameter(parameter);
	}

	public void deleteWidget(Integer widgetId) {
		widgetDao.remove(widgetId);
	}

	// WidgetDictionary
	public WidgetDictionary getWidgetDictionary(Integer widgetDictionaryId) {
		return widgetDictionaryDao.get(widgetDictionaryId);
	}

	/**
	 * Effettua il salvataggio della WidgetDictionary e di tutte i suoi fragment
	 * 
	 * @param widgetDictionary
	 * @param requestParams
	 * @param applicationService
	 * @return
	 */
	public Integer saveOrUpdateWithFragment(WidgetDictionary widgetDictionary, Map<String, Map> requestParams,
			Object applicationService) {
		this.saveOrUpdate(widgetDictionary);
		Integer id = widgetDictionary.getId();
		try {
			// this.saveFragmentSet(widgetDictionary, requestParams,
			// applicationService);
		} catch (Exception e) {
			log.error("Impossibile salvare i fragment della WidgetDictionary " + id, e);
		}
		return id;
	}

	public void saveOrUpdate(WidgetDictionary widgetDictionary) {
		if (widgetDictionary.getId() == null) {
			widgetDictionaryDao.save(widgetDictionary);
		} else {
			widgetDictionaryDao.save((WidgetDictionary) widgetDao.mergeObjects(widgetDictionary));
		}
	}

	public void deleteWidgetDictionary(Integer widgetDictionaryId) {
		widgetDictionaryDao.remove(widgetDictionaryId);
	}

	public void deleteWidgetDictionary(Set<Integer> widgetDictionaryIdSet) {
		widgetDictionaryDao.deleteWidgetDictionary(widgetDictionaryIdSet);
	}

	// other
	public List<Selectable> getSelectableFromHql(String hqlQuery) {
		return widgetDao.getSelectableFromHql(hqlQuery);
	}

	public List<Selectable> getSelectableFromHql(String hqlQuery, String nullableKey) {
		List<Selectable> globalList = new ArrayList<Selectable>();
		if (StringUtils.isNotBlank(nullableKey))
			globalList.add(new SelectBaseString("", messageUtil.findMessage(nullableKey)));
		globalList.addAll(widgetDao.getSelectableFromHql(hqlQuery));
		return globalList;
	}

	// This method is used through introspection
	public List<Selectable> getSiNoList() {
		List<Selectable> siNoList = new ArrayList<Selectable>();
		siNoList.add(new SelectBaseString("true", messageUtil.findMessage("label.widget.yes")));
		siNoList.add(new SelectBaseString("false", messageUtil.findMessage("label.widget.no")));
		return siNoList;
	}

	// This method is used through introspection
	public List<Selectable> getPositioningList() {
		List<Selectable> positioningList = new ArrayList<Selectable>();
		positioningList.add(new SelectBaseString("horizontal", messageUtil.findMessage("label.widget.horizontal")));
		positioningList.add(new SelectBaseString("vertical", messageUtil.findMessage("label.widget.vertical")));
		return positioningList;
	}

	// This method is used through introspection
	public List<Selectable> getOptionsWidgetPopulationTypeList() {
		// Option Widget Population Type from enum
		OptionsWidgetPopulationType[] optionsWidgetPopulationType = WidgetConstant.OptionsWidgetPopulationType.values();
		List<Selectable> optionsWidgetPopulationTypeList = new ArrayList<Selectable>();
		for (int i = 0; i < optionsWidgetPopulationType.length; i++)
			optionsWidgetPopulationTypeList.add(new SelectBaseString(optionsWidgetPopulationType[i].toString(),
					optionsWidgetPopulationType[i].toString()));
		optionsWidgetPopulationTypeList.add(0,
				new SelectBaseString("", messageUtil.findMessage("label.widget.selezionare")));
		return optionsWidgetPopulationTypeList;
	}

	// This method is used through introspection
	public List<SelectBaseString> getWidgetDictionaryTypeList() {
		WidgetDictionaryType[] widgetDictionaryType = WidgetConstant.WidgetDictionaryType.values();
		List<SelectBaseString> widgetDictionaryTypeList = new ArrayList<SelectBaseString>();
		for (int i = 0; i < widgetDictionaryType.length; i++)
			widgetDictionaryTypeList
					.add(new SelectBaseString(widgetDictionaryType[i].toString(), widgetDictionaryType[i].toString()));
		widgetDictionaryTypeList.add(0, new SelectBaseString("", messageUtil.findMessage("label.widget.selezionare")));
		return widgetDictionaryTypeList;
	}

	// This method is used through introspection
	public List<SelectBaseString> getWidgetDictionaryTypeList(boolean nullable) {
		List<SelectBaseString> widgetDictionaryTypeList = getWidgetDictionaryTypeList();
		if (nullable)
			widgetDictionaryTypeList.add(0, new SelectBaseString("", ""));

		return widgetDictionaryTypeList;
	}

	public List<Selectable> getOptionList(Object... parameter) {
		List<Selectable> list = new LinkedList<Selectable>();
		for (Object key : parameter) {
			SelectBaseString s = new SelectBaseString(key != null ? key.toString() : null,
					key != null ? key.toString() : null);
			list.add(s);
		}
		return list;
	}

	// setter

	public void setWidgetDao(WidgetDao widgetDao) {
		this.widgetDao = widgetDao;
	}

	public void setWidgetDictionaryDao(WidgetDictionaryDao widgetDictionaryDao) {
		this.widgetDictionaryDao = widgetDictionaryDao;
	}

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}
}