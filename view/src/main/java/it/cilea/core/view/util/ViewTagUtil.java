package it.cilea.core.view.util;

import it.cilea.core.view.ViewConstant;
import it.cilea.core.view.model.ViewBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewTagUtil {
	private static final Logger log = LoggerFactory.getLogger(ViewTagUtil.class);

	public static ViewBuilder getViewBuilder(String identifier) {
		return ViewConstant.viewBuilderMap.get(ViewUtil.correctIdentifier(identifier));
	}

}
