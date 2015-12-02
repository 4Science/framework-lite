package it.cilea.core.view;

import it.cilea.core.view.model.ViewBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ViewConstant {
	public static Map<String, ViewBuilder> viewBuilderMap = new HashMap<String, ViewBuilder>();
	public static Map<String, Set<ViewBuilder>> allViewBuilderMap = new HashMap<String, Set<ViewBuilder>>();
}
