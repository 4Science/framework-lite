package it.cilea.core.widget.model.impl.core;

import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

@Entity
@DiscriminatorValue("infoLine")
public class InfoLineWidget extends Widget {

	@Transient
	String infoKey;

	public String getInfoKey() {
		return infoKey;
	}

	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}

	@Override
	public void init() throws Exception {
		super.init();
		if (widgetDictionaryMap.get(WidgetConstant.WidgetDictionaryType.INFO.name()) != null)
			infoKey = widgetDictionaryMap.get(WidgetConstant.WidgetDictionaryType.INFO.name());
	}

	public String[] getRequestValues(HttpServletRequest request) {
		return null;
	}

}