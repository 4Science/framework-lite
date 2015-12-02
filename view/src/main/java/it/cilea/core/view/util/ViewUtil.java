package it.cilea.core.view.util;

import it.cilea.core.view.ViewConstant;
import it.cilea.core.view.model.ViewBuilder;
import it.cilea.core.view.model.ViewBuilder.ViewBuilderManagedBy;
import it.cilea.core.view.model.ViewBuilder.ViewBuilderState;
import it.cilea.core.view.service.ViewService;
import it.cilea.core.widget.util.WidgetUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewUtil {

	private static final Logger log = LoggerFactory.getLogger(ViewUtil.class);

	public static void reload(ViewService viewService) throws Exception {
		log.info("View reload");
		WidgetUtil.reload(viewService.getWidgetService());
		ViewConstant.viewBuilderMap = new HashMap<String, ViewBuilder>();
		ViewConstant.allViewBuilderMap = new HashMap<String, Set<ViewBuilder>>();
		List<ViewBuilder> viewBuilderList = viewService.initViewBuilderList();
		for (ViewBuilder viewBuilder : viewBuilderList) {
			viewBuilder.init();
			for (String identifier : viewBuilder.getIdentifierSet()) {
				if (!ViewConstant.allViewBuilderMap.containsKey(identifier))
					ViewConstant.allViewBuilderMap.put(identifier, new HashSet<ViewBuilder>());
				ViewConstant.allViewBuilderMap.get(identifier).add(viewBuilder);
			}
		}

		for (String identifier : ViewConstant.allViewBuilderMap.keySet()) {
			for (ViewBuilder viewBuilderNow : ViewConstant.allViewBuilderMap.get(identifier)) {
				if (ViewBuilderState.active.name().equals(viewBuilderNow.getState())) {
					if (!ViewConstant.viewBuilderMap.containsKey(identifier))
						ViewConstant.viewBuilderMap.put(identifier, viewBuilderNow);
					else {
						ViewBuilder viewBuilderPresent = ViewConstant.viewBuilderMap.get(identifier);
						if (ViewBuilderManagedBy.user.toString().equals(viewBuilderPresent.getManagedBy())) {
							// user already in viewBuilderMap
							// system already in viewBuilderMap
							if (ViewBuilderManagedBy.user.toString().equals(viewBuilderNow.getManagedBy())
									&& viewBuilderNow.getVersionDate().after(viewBuilderPresent.getVersionDate()))
								ViewConstant.viewBuilderMap.put(identifier, viewBuilderNow);
						} else {
							// system already in viewBuilderMap
							if (ViewBuilderManagedBy.user.toString().equals(viewBuilderNow.getManagedBy()))
								ViewConstant.viewBuilderMap.put(identifier, viewBuilderNow);
							else if (viewBuilderNow.getVersionDate().after(viewBuilderPresent.getVersionDate()))
								ViewConstant.viewBuilderMap.put(identifier, viewBuilderNow);
						}
					}
				}

			}
		}
	}

	public static String correctIdentifier(String identifier) {
		if (StringUtils.startsWith(identifier, "/"))
			return identifier;
		return "/" + identifier;
	}

}
