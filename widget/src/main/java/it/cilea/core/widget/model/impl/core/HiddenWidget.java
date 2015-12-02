package it.cilea.core.widget.model.impl.core;

import it.cilea.core.widget.model.Widget;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("hidden")
public class HiddenWidget extends Widget {

	@Transient
	protected String populateMethod;
	@Transient
	protected List<?> valueList;

	public String getPopulateMethod() {
		return populateMethod;
	}

	public void setPopulateMethod(String populateMethod) {
		this.populateMethod = populateMethod;
	}

	public List<?> getValueList() {
		return valueList;
	}

	public void setValueList(List<?> valueList) {
		this.valueList = valueList;
	}

}
