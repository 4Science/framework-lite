package it.cilea.core.model;

import it.cilea.core.util.MessageUtilConstant;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SelectBase implements java.io.Serializable, Selectable {

	private String value;
	private String label;
	private String labelKey;

	public SelectBase() {
	}

	public SelectBase(String value, String label) {
		this.value = value;
		this.label = label;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(this.getDisplayValue()).toString();
	}

	@Transient
	public String getIdentifyingValue() {
		if (value == null)
			return null;
		return String.valueOf(value);
	}

	@Transient
	public String getDisplayValue() {
		if (StringUtils.isNotBlank(labelKey))
			return MessageUtilConstant.messageUtil.findMessage(labelKey);
		return label;

	}

	public void setDisplayValue(String displayValue) {
		label = displayValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLabelKey() {
		return labelKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
	}

}
