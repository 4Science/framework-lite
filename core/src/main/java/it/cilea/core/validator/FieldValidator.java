package it.cilea.core.validator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class FieldValidator {
	public FieldValidator() {

	}

	public FieldValidator(String fieldName, String validationRules) {
		this.fieldName = fieldName;
		String[] validationRulesArray = StringUtils.splitByWholeSeparator(validationRules, "§§");
		List<String> validationRuleList = new ArrayList<String>();
		CollectionUtils.addAll(validationRuleList, validationRulesArray);
		setValidationRuleList(validationRuleList);
	}

	private String fieldName;
	private List<String> validationRuleList;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public List<String> getValidationRuleList() {
		return validationRuleList;
	}

	public void setValidationRuleList(List<String> validationRuleList) {
		this.validationRuleList = validationRuleList;
	}
}
