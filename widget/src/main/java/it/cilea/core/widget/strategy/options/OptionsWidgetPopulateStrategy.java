package it.cilea.core.widget.strategy.options;

import it.cilea.core.model.Selectable;
import it.cilea.core.widget.model.OptionsWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public abstract class OptionsWidgetPopulateStrategy {
	protected List<String> parameterValueList = new ArrayList<String>();
	protected OptionsWidget widget;

	public OptionsWidgetPopulateStrategy(OptionsWidget widget, HttpServletRequest request) {
		if (request == null)
			throw new IllegalArgumentException("HttpServletRequest must be non null");
		this.widget=widget;
		Set<String> parameterValueList = widget.getParameterValueSet();
		Set<String> parameterNameList = widget.getParameterNameSet();

		List<String> parameterExtractedValueList = new ArrayList<String>();
		
		for (String name : parameterNameList) {
			String paramValue = request.getParameter(name);
			
			if (StringUtils.isBlank(paramValue)) {
				paramValue = (String) request.getAttribute(name);
			}

			if (StringUtils.isBlank(paramValue)) {
				paramValue = (String) request.getSession().getAttribute(name);
			}

			if (StringUtils.isBlank(paramValue)) {
				paramValue = (String) request.getSession().getServletContext().getAttribute(name);
			}
			parameterExtractedValueList.add(paramValue);
		}

		if (parameterValueList!=null)
			parameterExtractedValueList.addAll(parameterValueList);
		
		this.parameterValueList = parameterExtractedValueList;
	}

	public abstract List<? extends Selectable> getOptions();
	public abstract List<? extends Selectable> getOptions(String[] ids);
	
	

	/**
	 * 
	 * @return the parameter value list associated with the current widget. In
	 *         the list we have FIRST all parameter value retrieved from
	 *         request/session/application scopes and THEN we have static
	 *         parameter values that is parameter values exactly as extracted
	 *         from DB.
	 */
	public Object[] getMethodInvocationParameter() {
		return parameterValueList.toArray();
	}
	
	protected String getRequestValue(HttpServletRequest request, String name){
		String paramValue = request.getParameter(name);
		
		if (StringUtils.isBlank(paramValue)) {
			paramValue = (String) request.getAttribute(name);
		}

		if (StringUtils.isBlank(paramValue)) {
			paramValue = (String) request.getSession().getAttribute(name);
		}

		if (StringUtils.isBlank(paramValue)) {
			paramValue = (String) request.getSession().getServletContext().getAttribute(name);
		}
		
		return paramValue;
	}

}
