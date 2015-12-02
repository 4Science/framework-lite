package it.cilea.core.spring.model;

import it.cilea.core.model.IdentifiableObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class ControllerLogic extends IdentifiableObject {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "URL")
	private String url;

	@Column(name = "VIEW_NAME")
	private String viewName;

	@Column(name = "VIEW_ERROR")
	private String viewError;
	@Column(name = "VIEW_SUCCESS")
	private String viewSuccess;
	@Column(name = "VIEW_UNDO")
	private String viewUndo;

	@Column(name = "MULTIPLE_CHOICE_LIST")
	private String multipleChoiceListBeanName;

	@Column(name = "VALIDATOR_LIST")
	private String validatorListBeanName;

	@Transient
	private List<Object> validatorList = null;

	private Class modelClass;

	@Transient
	private Map<String, Object> infoMap = new HashMap<String, Object>();

	public ControllerLogic() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getMultipleChoiceListBeanName() {
		return multipleChoiceListBeanName;
	}

	public void setMultipleChoiceListBeanName(String multipleChoiceListBeanName) {
		this.multipleChoiceListBeanName = multipleChoiceListBeanName;
	}

	public String getValidatorListBeanName() {
		return validatorListBeanName;
	}

	public void setValidatorListBeanName(String validatorListBeanName) {
		this.validatorListBeanName = validatorListBeanName;
	}

	public String getViewSuccess() {
		return viewSuccess;
	}

	public void setViewSuccess(String viewSuccess) {
		this.viewSuccess = viewSuccess;
	}

	public String getViewUndo() {
		return viewUndo;
	}

	public void setViewUndo(String viewUndo) {
		this.viewUndo = viewUndo;
	}

	public String getViewError() {
		return viewError;
	}

	public void setViewError(String viewError) {
		this.viewError = viewError;
	}

	public Map<String, Object> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(Map<String, Object> infoMap) {
		this.infoMap = infoMap;
	}

	public Class getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class modelClass) {
		this.modelClass = modelClass;
	}

	public List<Object> getValidatorList() {
		return validatorList;
	}

	public void setValidatorList(List<Object> validatorList) {
		this.validatorList = validatorList;
	}
}
