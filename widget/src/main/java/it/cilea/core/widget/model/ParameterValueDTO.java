package it.cilea.core.widget.model;

import it.cilea.core.widget.util.WidgetUtil;

public class ParameterValueDTO {

	private Object value;
	private String sign;

	public ParameterValueDTO(Object value, String sign) {
		this.value = value;
		this.sign = sign;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		ParameterValueDTO parameterDTO = (ParameterValueDTO) obj;

		if (WidgetUtil.areObjectEquals(value, parameterDTO.getValue())
				&& WidgetUtil.areObjectEquals(sign, parameterDTO.getSign()))
			return true;
		else
			return false;

	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
