package it.cilea.core.widget.model.impl.core;

import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

@Entity
@DiscriminatorValue("if")
public class IfWidget extends Widget {

	// TODO
	@Transient
	String script;
	@Transient
	String test;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.get(WidgetConstant.ParameterType.SCRIPT.name()) != null)
			script = parameterMap.get(WidgetConstant.ParameterType.SCRIPT.name()).iterator().next();
		if (parameterMap.get(WidgetConstant.ParameterType.TEST.name()) != null)
			test = parameterMap.get(WidgetConstant.ParameterType.TEST.name()).iterator().next();
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

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

}
