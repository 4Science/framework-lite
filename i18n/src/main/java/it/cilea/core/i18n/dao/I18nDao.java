package it.cilea.core.i18n.dao;

import java.util.Map;

import it.cilea.core.i18n.model.I18n;
import it.cilea.core.spring.dao.GenericDao;

public interface I18nDao extends GenericDao<I18n, Integer> {

	Map<String, Map<String, String>> getI18nMap(String context);

	Map<String, Map<String, String>> getI18nMap(String context, boolean exact);

	I18n getI18n(String key, String context);

	public Object test();

	I18n getI18nExactContext(String key, String context);

	I18n searchI18n(String id);

	void deleteObjectI18n(String id);

	void saveUpdateI18nData(String id, String discriminator, String value);

	public String searchI18nData(String id, String discriminator);

	public String searchI18nId(String key, String context);

	public Integer saveOrUpdateAndFlush(I18n i18n);

}
