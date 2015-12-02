package it.cilea.core.widget.model.impl.command;

import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

@Entity
@DiscriminatorValue("displayTag:column")
public class DisplayTagColumnWidget extends Widget {

	@Transient
	String script;
	@Transient
	String decoratorClass;
	@Transient
	String style;
	@Transient
	Boolean sortable = false;
	@Transient
	String sortProperty;
	@Transient
	String media;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.get(WidgetConstant.ParameterType.SCRIPT.name()) != null)
			script = parameterMap.get(WidgetConstant.ParameterType.SCRIPT.name()).iterator().next();
		if (parameterMap.get(WidgetConstant.ParameterType.DECORATOR_CLASS.name()) != null)
			decoratorClass = parameterMap.get(WidgetConstant.ParameterType.DECORATOR_CLASS.name()).iterator().next();
		if (parameterMap.get(WidgetConstant.ParameterType.STYLE.name()) != null)
			style = parameterMap.get(WidgetConstant.ParameterType.STYLE.name()).iterator().next();
		if (parameterMap.get(WidgetConstant.ParameterType.SORTABLE.name()) != null)
			sortable = Boolean
					.valueOf(parameterMap.get(WidgetConstant.ParameterType.SORTABLE.name()).iterator().next());
		if (parameterMap.get(WidgetConstant.ParameterType.SORT_PROPERTY.name()) != null)
			sortProperty = parameterMap.get(WidgetConstant.ParameterType.SORT_PROPERTY.name()).iterator().next();
		if (parameterMap.get(WidgetConstant.ParameterType.MEDIA.name()) != null)
			media = parameterMap.get(WidgetConstant.ParameterType.MEDIA.name()).iterator().next();
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

	public String getDecoratorClass() {
		return decoratorClass;
	}

	public void setDecoratorClass(String decoratorClass) {
		this.decoratorClass = decoratorClass;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Boolean getSortable() {
		return sortable;
	}

	public void setSortable(Boolean sortable) {
		this.sortable = sortable;
	}

	public String getSortProperty() {
		return sortProperty;
	}

	public void setSortProperty(String sortProperty) {
		this.sortProperty = sortProperty;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}
}
