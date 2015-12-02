package it.cilea.core.spring.propertyeditors;

import java.text.DateFormat;

import org.springframework.beans.propertyeditors.CustomDateEditor;

public class CustomDateTimeEditor extends CustomDateEditor {

	public CustomDateTimeEditor(DateFormat dateFormat, boolean allowEmpty,
			int exactDateLength) {
		super(dateFormat, allowEmpty, exactDateLength);
	}

	@Override
	public void setAsText(String text)  {
		if (text.length() == 10) {
			text += " 00:00";
		}
		super.setAsText(text);
	}

}
