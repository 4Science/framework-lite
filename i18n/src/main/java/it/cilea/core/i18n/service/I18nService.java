package it.cilea.core.i18n.service;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import it.cilea.core.i18n.dao.I18nDao;
import it.cilea.core.i18n.model.I18n;
import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.SelectBaseStringI18n;
import it.cilea.core.model.Selectable;

@Service
public class I18nService {

	private static final Logger log = LoggerFactory.getLogger(I18nService.class);

	@Qualifier("i18nDAO")
	@Autowired
	private I18nDao i18nDao;

	public I18nDao getI18nDao() {
		return i18nDao;
	}

	// I18n
	public I18n getI18n(Integer i18nId) {
		return i18nDao.get(i18nId);
	}

	public Object test() {
		return i18nDao.test();
	}

	public I18n searchI18n(String id) {
		return i18nDao.searchI18n(id);
	}

	public void deleteObjectI18n(String id) {
		i18nDao.deleteObjectI18n(id);
	}

	public I18n getI18n(String key, String context) {
		return i18nDao.getI18n(key, context);
	}

	public I18n getI18nExactContext(String key, String context) {
		return i18nDao.getI18nExactContext(key, context);
	}

	public Map<String, Map<String, String>> getI18nMap(String context) {
		return i18nDao.getI18nMap(context);
	}

	public Map<String, Map<String, String>> getI18nMap(String context, boolean exact) {
		return i18nDao.getI18nMap(context, exact);
	}

	public Integer saveOrUpdate(I18n i18n) {
		return i18nDao.save(i18n).getId();
	}

	public Integer saveOrUpdateAndFlush(I18n i18n) {
		return i18nDao.saveOrUpdateAndFlush(i18n);
	}

	public void deleteI18n(Integer id) {
		i18nDao.remove(id);
	}

	public void setI18nDao(I18nDao i18nDao) {
		this.i18nDao = i18nDao;
	}

	public void saveUpdateI18nData(String id, String discriminator, String value) {
		i18nDao.saveUpdateI18nData(id, discriminator, value);
	}

	public String searchI18nData(String id, String discriminator) {
		return i18nDao.searchI18nData(id, discriminator);
	}

	public String searchI18nId(String key, String context) {
		return i18nDao.searchI18nId(key, context);
	}

	public static List<Selectable> getCharsetList(boolean addAuto, boolean nullable, String nullableKey) {
		List<Selectable> list = new ArrayList<Selectable>();
		if (addAuto)
			list.add(new SelectBaseStringI18n("auto", "charset.auto"));
		Map<String, Charset> charsets = Charset.availableCharsets();
		Iterator<Charset> iterator = charsets.values().iterator();
		while (iterator.hasNext()) {
			Charset cs = (Charset) iterator.next();
			list.add(new SelectBaseString(cs.name(), cs.displayName(LocaleContextHolder.getLocale())));
		}
		return list;
	}

}
