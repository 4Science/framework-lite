package it.cilea.core.spring.service;

import it.cilea.core.CoreConstant;
import it.cilea.core.model.SelectBaseInteger;
import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.Selectable;
import it.cilea.core.spring.dao.CoreDao;
import it.cilea.core.spring.util.MessageUtil;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoreService {
	@Autowired
	private CoreDao coreDao;

	@Autowired
	protected MessageUtil messageUtil;

	public void setCoreDao(CoreDao coreDao) {
		this.coreDao = coreDao;
	}

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	public Timestamp getCurrentTimestamp() {
		return coreDao.getCurrentTimestamp();
	}

	public List<SelectBaseString> getSelectableListFromConcatenated(String values, String i18nPrefix) {
		List<SelectBaseString> list = new ArrayList<SelectBaseString>();
		for (String value : StringUtils.split(values, ","))
			list.add(new SelectBaseString(value, messageUtil.findMessage(i18nPrefix + value)));
		return list;
	}

	public List<SelectBaseInteger> getYearList(Object yearFromParam) {
		Integer yearFrom = null;
		try {
			yearFrom = Integer.valueOf(yearFromParam.toString());
		} catch (Exception e) {
			// YearFrom not set
		}

		return getYearList(yearFrom, null, null);
	}

	public List<SelectBaseInteger> getYearList(Object yearFromParam, String ordering) {
		Integer yearFrom = null;
		try {
			yearFrom = Integer.valueOf(yearFromParam.toString());
		} catch (Exception e) {
			// YearFrom not set
		}

		return getYearList(yearFrom, null, ordering);
	}

	public List<SelectBaseInteger> getYearList(Object yearFromParam, String ordering, String nullable,
			String nullableKey) {
		Integer yearFrom = null;
		try {
			yearFrom = Integer.valueOf(yearFromParam.toString());
		} catch (Exception e) {
			// YearFrom not set
		}

		List<SelectBaseInteger> list = getYearList(yearFrom, null, ordering);
		if (Boolean.parseBoolean(nullable))
			list.add(0, new SelectBaseInteger(null, messageUtil.findMessage(nullableKey)));

		return list;
	}

	public List<SelectBaseInteger> getYearList(Integer yearFrom, Integer yearTo) {
		return getYearList(yearFrom, yearTo, null);
	}

	public List<SelectBaseInteger> getYearList(Integer yearFrom, Integer yearTo, String ordering) {
		List<SelectBaseInteger> yearList = new ArrayList<SelectBaseInteger>();

		if (yearTo == null) {
			Date date = new Date();
			SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy");
			yearTo = Integer.valueOf(simpleDateformat.format(date));
		}

		if (StringUtils.isEmpty(ordering))
			ordering = "asc";

		if ("asc".equals(ordering)) {
			for (; yearFrom <= yearTo; yearFrom++)
				yearList.add(new SelectBaseInteger(yearFrom, yearFrom));
		} else {
			for (; yearTo >= yearFrom; yearTo--)
				yearList.add(new SelectBaseInteger(yearTo, yearTo));
		}

		return yearList;
	}

	/**
	 * Recupera e restituisce la lista dei moduli installati per I18N.
	 * 
	 * @param nullable
	 * @param nullableKey
	 * @return
	 */
	public List<Selectable> getI18nModuleList(String nullable, String nullableKey) {
		List<Selectable> moduleList = new ArrayList<Selectable>();

		String[] moduleName = StringUtils.split(CoreConstant.I18N_MODULE_LIST, ",");

		if (Boolean.parseBoolean(nullable))
			moduleList.add(0, new SelectBaseString("", messageUtil.findMessage(nullableKey)));

		for (String s : moduleName) {
			s = StringUtils.strip(s, "'");
			moduleList.add(new SelectBaseString(s, messageUtil.findMessage("label.module." + s)));
		}

		return moduleList;
	}

	/**
	 * Recupera e restituisce la lista delle lingue ablitate.
	 * 
	 * @param nullable
	 * @param nullableKey
	 * @return
	 */
	public List<Selectable> getLanguageList(String nullable, String nullableKey) {
		List<Selectable> moduleList = new ArrayList<Selectable>();

		String[] moduleName = StringUtils.split(CoreConstant.I18N_LIST, ",");

		if (Boolean.parseBoolean(nullable))
			moduleList.add(0, new SelectBaseString("", messageUtil.findMessage("label.i18n.default")));

		for (String s : moduleName) {
			moduleList.add(new SelectBaseString(s, messageUtil.findMessage("label.i18n." + s)));
		}

		return moduleList;
	}

}