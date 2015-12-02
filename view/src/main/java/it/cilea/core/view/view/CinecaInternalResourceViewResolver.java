package it.cilea.core.view.view;

import it.cilea.core.view.ViewConstant;
import it.cilea.core.view.util.ViewUtil;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

public class CinecaInternalResourceViewResolver extends InternalResourceViewResolver {
	@Override
	protected AbstractUrlBasedView buildView(String viewName) throws Exception {
		if (ViewConstant.viewBuilderMap.containsKey(ViewUtil.correctIdentifier(viewName)))
			return super.buildView(ViewConstant.viewBuilderMap.get(ViewUtil.correctIdentifier(viewName)).getViewName());
		return super.buildView(viewName);
	}

	// calls buildView
	// @Override
	// protected View loadView(String viewName, Locale locale) throws Exception
	// {
	// if
	// (ViewConstant.viewBuilderMap.containsKey(ViewUtil.correctIdentifier(viewName)))
	// return
	// super.loadView(ViewConstant.viewBuilderMap.get(ViewUtil.correctIdentifier(viewName)).getViewName(),
	// locale);
	// return super.loadView(viewName, locale);
	// }

	@Override
	protected View createView(String viewName, Locale locale) throws Exception {
		if (ViewConstant.viewBuilderMap.containsKey(ViewUtil.correctIdentifier(viewName)))
			return super.createView(
					ViewConstant.viewBuilderMap.get(ViewUtil.correctIdentifier(viewName)).getViewName(), locale);
		return super.createView(viewName, locale);
	}

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		if (ViewConstant.viewBuilderMap.containsKey(ViewUtil.correctIdentifier(viewName)))
			return super.resolveViewName(ViewConstant.viewBuilderMap.get(ViewUtil.correctIdentifier(viewName))
					.getViewName(), locale);
		return super.resolveViewName(viewName, locale);
	}

}
