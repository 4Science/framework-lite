package it.cilea.core.model;

import it.cilea.core.util.MessageUtilConstant;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class SelectBaseString implements java.io.Serializable, Selectable {

	private String value;
	private String label;
	private String labelKey;

	public SelectBaseString() {
	}

	public SelectBaseString(String value, String label) {
		this.value = value;
		this.label = label;
	}

	// questo costruttore viene utilizzato per permettere
	// l'utilizzo del distinct in queries hql (cfr applicationdao di wdiget)
	public SelectBaseString(String value, String label, Integer order) {
		this.value = value;
		this.label = label;
	}

	public SelectBaseString(Integer value, Integer label) {
		this.value = value == null ? null : value.toString();
		this.label = label.toString();
	}

	public SelectBaseString(Integer value, String label) {

		this.value = value == null ? null : value.toString();
		this.label = label.toString();
	}

	// questo costruttore viene utilizzato per permettere
	// l'utilizzo del distinct in queries hql (cfr applicationdao di wdiget)
	public SelectBaseString(Integer value, String label, Integer order) {
		this.value = value.toString();
		this.label = label.toString();
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

	@Override
	public boolean equals(Object obj) {
		return getIdentifyingValue().equals(((Selectable) obj).getIdentifyingValue());
	}

	@Override
	public int hashCode() {
		return getIdentifyingValue().hashCode();
	}

	public void setDisplayValue(String displayValue) {
		setLabel(displayValue);
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
