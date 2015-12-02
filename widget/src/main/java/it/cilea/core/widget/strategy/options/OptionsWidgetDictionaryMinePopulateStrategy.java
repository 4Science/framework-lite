package it.cilea.core.widget.strategy.options;

import it.cilea.core.authorization.context.AuthorizationUserHolder;
import it.cilea.core.authorization.model.impl.UserDetail;
import it.cilea.core.model.Selectable;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.OptionsWidget;
import it.cilea.core.widget.model.WidgetDictionary;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OptionsWidgetDictionaryMinePopulateStrategy extends OptionsWidgetPopulateStrategy {

	private OptionsWidget widget;

	private final Logger log = LoggerFactory.getLogger(OptionsWidgetDictionaryMinePopulateStrategy.class);

	public OptionsWidgetDictionaryMinePopulateStrategy(OptionsWidget widget, HttpServletRequest request) {
		super(widget, request);
		this.widget = widget;
	}

	public List<? extends Selectable> getOptions() {

		List<Selectable> globalList = new ArrayList<Selectable>();
		if (widget.getRenderEmptyOption()) {
			globalList.add(widget.getSelectableEmptyOption());
		}
		UserDetail userDetail = AuthorizationUserHolder.getUser();
		Object[] params = getMethodInvocationParameter();
		if (params.length > 0)
			for (WidgetDictionary dic : widget.getWidgetDictionarySet()) {
				if (StringUtils.equals(dic.getDiscriminator(), (String) params[0]))
					if (userDetail == null || dic.getAuthorizationResource() == null
							|| userDetail.hasAuthorities(dic.getAuthorizationResource().getIdentifier()))
						globalList.add(dic);
			}
		else
			log.warn("Add parameters in widget!");
		return globalList;
	}

	public List<? extends Selectable> getOptions(String[] ids) {
		List<Selectable> result = new ArrayList<Selectable>();
		for (String id : ids)
			result.add(WidgetConstant.widgetDictionaryMap.get(Integer.valueOf(id)));
		return result;
	}

}
