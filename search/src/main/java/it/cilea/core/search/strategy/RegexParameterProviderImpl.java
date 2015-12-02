package it.cilea.core.search.strategy;

import it.cilea.core.model.Selectable;
import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.model.SearchBuilderWidgetLink;
import it.cilea.core.search.regex.RegexParameterProvider;
import it.cilea.core.widget.factory.OptionsWidgetPopulateStrategyFactory;
import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.model.impl.core.ServerSideWidget;
import it.cilea.core.widget.strategy.options.OptionsWidgetPopulateStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class RegexParameterProviderImpl implements RegexParameterProvider {

	@Override
	public List<? extends Selectable> getValues(String parameterName, String searchBuilderIdentifier,
			HttpServletRequest request) {
		List<Selectable> result = new ArrayList<Selectable>();
		SearchBuilder searchBuilder = SearchConstant.searchBuilderMap.get(searchBuilderIdentifier);
		for (SearchBuilderWidgetLink sbwr : searchBuilder.getSearchBuilderWidgetLinkSet()) {
			Widget widget = sbwr.getWidget();
			if (widget instanceof ServerSideWidget
					&& (parameterName.equals(widget.getPageAttributeName()) || parameterName.equals(widget
							.getModelAttributeName())) && widget.isWidgetGranted()) {
				ServerSideWidget ssWidget = (ServerSideWidget) sbwr.getWidget();
				OptionsWidgetPopulateStrategy provider = OptionsWidgetPopulateStrategyFactory.getCommand(ssWidget,
						request);
				return provider.getOptions();
			}
		}
		return result;
	}
}
