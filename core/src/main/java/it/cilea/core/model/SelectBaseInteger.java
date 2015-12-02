package it.cilea.core.model;

import it.cilea.core.util.MessageUtilConstant;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SelectBaseInteger implements java.io.Serializable, Selectable {

	private Integer identifyingValue;
	private String displayValue;
	private String displayValueKey;

	public SelectBaseInteger() {
	}

	public SelectBaseInteger(Integer identifyingValue, String displayValue) {
		this.displayValue = displayValue;
		this.identifyingValue = identifyingValue;
	}

	public SelectBaseInteger(Integer identifyingValue, Integer displayValue) {
		this.displayValue = "" + displayValue;
		this.identifyingValue = identifyingValue;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(this.getDisplayValue()).toString();
	}

	@Transient
	public String getIdentifyingValue() {
		if (identifyingValue == null)
			return null;
		return String.valueOf(identifyingValue);
	}

	@Transient
	public String getDisplayValue() {
		if (StringUtils.isNotBlank(displayValueKey))
			return MessageUtilConstant.messageUtil.findMessage(displayValueKey);
		return displayValue;

	}

	public void setLabelKey(String labelKey) {
		setDisplayValueKey(labelKey);
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public void setIdentifyingValue(Integer identifyingValue) {
		this.identifyingValue = identifyingValue;
	}

	public String getDisplayValueKey() {
		return displayValueKey;
	}

	public void setDisplayValueKey(String displayValueKey) {
		this.displayValueKey = displayValueKey;
	}

}
