package it.cilea.core.widget.model.impl.core;

import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.model.OptionsWidget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("radio")
public class RadioWidget extends OptionsWidget {

	@Transient
	private String positioning = "horizontal";

	@Override
	public void init() throws Exception {
		super.init();
		if (cssClass == null)
			cssClass = "radio-horizontal";
		if (parameterMap.containsKey(ParameterType.POSITIONING.name()))
			positioning = parameterMap.get(ParameterType.POSITIONING.name()).iterator().next();
	}

	public String getPositioning() {
		return positioning;
	}

	public void setPositioning(String positioning) {
		this.positioning = positioning;
	}

}
