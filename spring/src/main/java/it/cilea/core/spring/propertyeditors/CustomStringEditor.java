package it.cilea.core.spring.propertyeditors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

public class CustomStringEditor extends StringTrimmerEditor {

	public CustomStringEditor(boolean emptyAsNull) {
		super(emptyAsNull);
	}

	public CustomStringEditor(String charsToDelete, boolean emptyAsNull) {
		super(charsToDelete, emptyAsNull);
	}

	@Override
	public void setAsText(String text) {
		StringUtils.replace(text, "&quot;", "\"");
		super.setAsText(text);
	}

	@Override
	public String getAsText() {
		return StringUtils.replace(super.getAsText(), "\"", "&quot;");
	}

}
