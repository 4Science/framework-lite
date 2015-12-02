package it.cilea.core.widget.model.impl.core;

import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("textarea")
public class TextAreaWidget extends Widget {

	@Transient
	private Integer maxLength;

	@Transient
	private Boolean showCounter;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.containsKey(ParameterType.SHOW_COUNTER.name()))
			showCounter = Boolean.valueOf(parameterMap.get(ParameterType.SHOW_COUNTER.name()).iterator().next());
		if (showCounter == null)
			showCounter = false;
		if (parameterMap.containsKey(ParameterType.MAX_LENGTH.name()))
			maxLength = Integer.valueOf(parameterMap.get(ParameterType.MAX_LENGTH.name()).iterator().next());
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Boolean getShowCounter() {
		return showCounter == null ? false : showCounter;
	}

	public void setShowCounter(Boolean showCounter) {
		this.showCounter = showCounter;
	}

}
