package it.cilea.core.dto;

import it.cilea.core.model.Selectable;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoice {

	private String attributeName;

	private String httpScope;

	private String populateMethod;

	private Boolean nullable;

	private List<String> parameterList = new ArrayList<String>();

	private List<Object> noParameterList = new ArrayList<Object>();

	private List<Selectable> optionList;

	private String serviceBeanId;

	private Boolean onFormSubmission = true;

	public MultipleChoice(String attributeName, String populateMethod) {
		super();
		this.attributeName = attributeName;
		this.populateMethod = populateMethod;
		this.nullable = nullable;
		this.httpScope = "request";
	}

	public List<Selectable> getOptionList() {
		return optionList;
	}

	public void setOptionList(List<Selectable> optionList) {
		this.optionList = optionList;
	}

	public MultipleChoice() {
		this.httpScope = "request";
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public void setPopulateMethod(String populateMethod) {
		this.populateMethod = populateMethod;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public String getPopulateMethod() {
		return populateMethod;
	}

	public List<String> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<String> parameterList) {
		this.parameterList = parameterList;
	}

	public Boolean getNullable() {
		return nullable;
	}

	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	public List<Object> getNoParameterList() {
		return noParameterList;
	}

	public void setNoParameterList(List<Object> noParameterList) {
		this.noParameterList = noParameterList;
	}

	public String getHttpScope() {
		return httpScope;
	}

	public void setHttpScope(String httpScope) {
		this.httpScope = httpScope;
	}

	public String getServiceBeanId() {
		return serviceBeanId;
	}

	public void setServiceBeanId(String serviceBeanId) {
		this.serviceBeanId = serviceBeanId;
	}

	public Boolean getOnFormSubmission() {
		return onFormSubmission;
	}

	public void setOnFormSubmission(Boolean onFormSubmission) {
		this.onFormSubmission = onFormSubmission;
	}
}
