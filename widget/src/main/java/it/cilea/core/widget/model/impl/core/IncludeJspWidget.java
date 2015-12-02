package it.cilea.core.widget.model.impl.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.Widget;

@Entity
@DiscriminatorValue("include-jsp")
public class IncludeJspWidget extends Widget {

	@Transient
	String page;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.get(WidgetConstant.ParameterType.PAGE.name()) != null)
			page = parameterMap.get(WidgetConstant.ParameterType.PAGE.name()).iterator().next();
	}

	public String[] getRequestValues(HttpServletRequest request) {
		return null;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}
}
