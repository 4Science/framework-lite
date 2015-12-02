package it.cilea.core.widget.model.impl.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.Widget;

@Entity
@DiscriminatorValue("html")
public class HtmlWidget extends Widget {
	@Transient
	String script;
	@Transient
	String expression;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.get(WidgetConstant.ParameterType.SCRIPT.name()) != null)
			script = parameterMap.get(WidgetConstant.ParameterType.SCRIPT.name()).iterator().next();
		if (parameterMap.get(WidgetConstant.ParameterType.EXPRESSION.name()) != null)
			expression = parameterMap.get(WidgetConstant.ParameterType.EXPRESSION.name()).iterator().next();
	}

	public String[] getRequestValues(HttpServletRequest request) {
		return null;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
}
