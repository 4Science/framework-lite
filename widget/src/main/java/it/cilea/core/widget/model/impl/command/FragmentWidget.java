package it.cilea.core.widget.model.impl.command;

import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

@Entity
@DiscriminatorValue("fragment")
public class FragmentWidget extends Widget {

	@Transient
	private String childFQClassName;
	@Transient
	private String childSetGetterName;
	@Transient
	private String childDiscriminator;
	@Transient
	private String listView;
	@Transient
	private String formView;
	@Transient
	private Boolean useChildDiscriminatorAsMethodParam;
	@Transient
	private String fragmentDivId;
	@Transient
	private String multipleChoiceListBeanName;
	@Transient
	private String comparatorFqClassName;
	@Transient
	private String infoDivMessage;
	@Transient
	private String fieldsetLegend;
	@Transient
	private String invocationType;
	@Transient
	private String parentFQClassName;
	@Transient
	private String parentId;
	@Transient
	private String parentUniqueIdentifier;
	@Transient
	private String parentGetterName;
	@Transient
	private String parentFkName;
	@Transient
	private Integer framedWindowHeight;
	@Transient
	private Integer framedWindowWidth;
	@Transient
	private String validatorListBeanName;
	@Transient
	private String deleteValidatorListBeanName;
	@Transient
	private String modeType;
	@Transient
	private Boolean ajaxSupplied;
	@Transient
	private String fragmentParameterList;
	@Transient
	private String comparatorPropertyList;
	@Transient
	private Integer maxItems;
	@Transient
	private String editTitleKey;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.containsKey("childFQClassName"))
			childFQClassName = parameterMap.get("childFQClassName").iterator().next();
		if (parameterMap.containsKey("childSetGetterName"))
			childSetGetterName = parameterMap.get("childSetGetterName").iterator().next();
		if (parameterMap.containsKey("childDiscriminator"))
			childDiscriminator = parameterMap.get("childDiscriminator").iterator().next();
		if (parameterMap.containsKey("listView"))
			listView = parameterMap.get("listView").iterator().next();
		if (parameterMap.containsKey("formView"))
			formView = parameterMap.get("formView").iterator().next();
		if (parameterMap.containsKey("useChildDiscriminatorAsMethodParam"))
			useChildDiscriminatorAsMethodParam = Boolean.valueOf(parameterMap.get("useChildDiscriminatorAsMethodParam")
					.iterator().next());
		if (parameterMap.containsKey("fragmentDivId"))
			fragmentDivId = parameterMap.get("fragmentDivId").iterator().next();
		if (parameterMap.containsKey("multipleChoiceListBeanName"))
			multipleChoiceListBeanName = parameterMap.get("multipleChoiceListBeanName").iterator().next();
		if (parameterMap.containsKey("comparatorFqClassName"))
			comparatorFqClassName = parameterMap.get("comparatorFqClassName").iterator().next();
		if (parameterMap.containsKey("infoDivMessage"))
			infoDivMessage = parameterMap.get("infoDivMessage").iterator().next();
		if (parameterMap.containsKey("fieldsetLegend"))
			fieldsetLegend = parameterMap.get("fieldsetLegend").iterator().next();
		if (parameterMap.containsKey("invocationType"))
			invocationType = parameterMap.get("invocationType").iterator().next();
		if (parameterMap.containsKey("parentFQClassName"))
			parentFQClassName = parameterMap.get("parentFQClassName").iterator().next();
		if (parameterMap.containsKey("parentId"))
			parentId = parameterMap.get("parentId").iterator().next();
		if (parameterMap.containsKey("parentUniqueIdentifier"))
			parentUniqueIdentifier = parameterMap.get("parentUniqueIdentifier").iterator().next();
		if (parameterMap.containsKey("parentGetterName"))
			parentGetterName = parameterMap.get("parentGetterName").iterator().next();
		if (parameterMap.containsKey("parentFkName"))
			parentFkName = parameterMap.get("parentFkName").iterator().next();
		if (parameterMap.containsKey("framedWindowHeight"))
			framedWindowHeight = Integer.valueOf(parameterMap.get("framedWindowHeight").iterator().next());
		if (parameterMap.containsKey("framedWindowWidth"))
			framedWindowWidth = Integer.valueOf(parameterMap.get("framedWindowWidth").iterator().next());
		if (parameterMap.containsKey("validatorListBeanName"))
			validatorListBeanName = parameterMap.get("validatorListBeanName").iterator().next();
		if (parameterMap.containsKey("deleteValidatorListBeanName"))
			deleteValidatorListBeanName = parameterMap.get("deleteValidatorListBeanName").iterator().next();
		if (parameterMap.containsKey("modeType"))
			modeType = parameterMap.get("modeType").iterator().next();
		if (parameterMap.containsKey("ajaxSupplied"))
			ajaxSupplied = Boolean.valueOf(parameterMap.get("ajaxSupplied").iterator().next());
		if (parameterMap.containsKey("fragmentParameterList"))
			fragmentParameterList = parameterMap.get("fragmentParameterList").iterator().next();
		if (parameterMap.containsKey("comparatorPropertyList"))
			comparatorPropertyList = parameterMap.get("comparatorPropertyList").iterator().next();
		if (parameterMap.containsKey("maxItems"))
			maxItems = Integer.valueOf(parameterMap.get("maxItems").iterator().next());
		if (parameterMap.containsKey("editTitleKey"))
			editTitleKey = parameterMap.get("editTitleKey").iterator().next();
	}

	public String[] getRequestValues(HttpServletRequest request) {
		return null;
	}

	public String getChildFQClassName() {
		return childFQClassName;
	}

	public void setChildFQClassName(String childFQClassName) {
		this.childFQClassName = childFQClassName;
	}

	public String getChildSetGetterName() {
		return childSetGetterName;
	}

	public void setChildSetGetterName(String childSetGetterName) {
		this.childSetGetterName = childSetGetterName;
	}

	public String getChildDiscriminator() {
		return childDiscriminator;
	}

	public void setChildDiscriminator(String childDiscriminator) {
		this.childDiscriminator = childDiscriminator;
	}

	public String getListView() {
		return listView;
	}

	public void setListView(String listView) {
		this.listView = listView;
	}

	public String getFormView() {
		return formView;
	}

	public void setFormView(String formView) {
		this.formView = formView;
	}

	public Boolean getUseChildDiscriminatorAsMethodParam() {
		return useChildDiscriminatorAsMethodParam;
	}

	public void setUseChildDiscriminatorAsMethodParam(Boolean useChildDiscriminatorAsMethodParam) {
		this.useChildDiscriminatorAsMethodParam = useChildDiscriminatorAsMethodParam;
	}

	public String getFragmentDivId() {
		return fragmentDivId;
	}

	public void setFragmentDivId(String fragmentDivId) {
		this.fragmentDivId = fragmentDivId;
	}

	public String getMultipleChoiceListBeanName() {
		return multipleChoiceListBeanName;
	}

	public void setMultipleChoiceListBeanName(String multipleChoiceListBeanName) {
		this.multipleChoiceListBeanName = multipleChoiceListBeanName;
	}

	public String getComparatorFqClassName() {
		return comparatorFqClassName;
	}

	public void setComparatorFqClassName(String comparatorFqClassName) {
		this.comparatorFqClassName = comparatorFqClassName;
	}

	public String getInfoDivMessage() {
		return infoDivMessage;
	}

	public void setInfoDivMessage(String infoDivMessage) {
		this.infoDivMessage = infoDivMessage;
	}

	public String getFieldsetLegend() {
		return fieldsetLegend;
	}

	public void setFieldsetLegend(String fieldsetLegend) {
		this.fieldsetLegend = fieldsetLegend;
	}

	public String getInvocationType() {
		return invocationType;
	}

	public void setInvocationType(String invocationType) {
		this.invocationType = invocationType;
	}

	public String getParentFQClassName() {
		return parentFQClassName;
	}

	public void setParentFQClassName(String parentFQClassName) {
		this.parentFQClassName = parentFQClassName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentUniqueIdentifier() {
		return parentUniqueIdentifier;
	}

	public void setParentUniqueIdentifier(String parentUniqueIdentifier) {
		this.parentUniqueIdentifier = parentUniqueIdentifier;
	}

	public String getParentGetterName() {
		return parentGetterName;
	}

	public void setParentGetterName(String parentGetterName) {
		this.parentGetterName = parentGetterName;
	}

	public String getParentFkName() {
		return parentFkName;
	}

	public void setParentFkName(String parentFkName) {
		this.parentFkName = parentFkName;
	}

	public Integer getFramedWindowHeight() {
		return framedWindowHeight;
	}

	public void setFramedWindowHeight(Integer framedWindowHeight) {
		this.framedWindowHeight = framedWindowHeight;
	}

	public Integer getFramedWindowWidth() {
		return framedWindowWidth;
	}

	public void setFramedWindowWidth(Integer framedWindowWidth) {
		this.framedWindowWidth = framedWindowWidth;
	}

	public String getValidatorListBeanName() {
		return validatorListBeanName;
	}

	public void setValidatorListBeanName(String validatorListBeanName) {
		this.validatorListBeanName = validatorListBeanName;
	}

	public String getDeleteValidatorListBeanName() {
		return deleteValidatorListBeanName;
	}

	public void setDeleteValidatorListBeanName(String deleteValidatorListBeanName) {
		this.deleteValidatorListBeanName = deleteValidatorListBeanName;
	}

	public String getModeType() {
		return modeType;
	}

	public void setModeType(String modeType) {
		this.modeType = modeType;
	}

	public Boolean getAjaxSupplied() {
		return ajaxSupplied;
	}

	public void setAjaxSupplied(Boolean ajaxSupplied) {
		this.ajaxSupplied = ajaxSupplied;
	}

	public String getFragmentParameterList() {
		return fragmentParameterList;
	}

	public void setFragmentParameterList(String fragmentParameterList) {
		this.fragmentParameterList = fragmentParameterList;
	}

	public String getComparatorPropertyList() {
		return comparatorPropertyList;
	}

	public void setComparatorPropertyList(String comparatorPropertyList) {
		this.comparatorPropertyList = comparatorPropertyList;
	}

	public Integer getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(Integer maxItems) {
		this.maxItems = maxItems;
	}

	public String getEditTitleKey() {
		return editTitleKey;
	}

	public void setEditTitleKey(String editTitleKey) {
		this.editTitleKey = editTitleKey;
	}

}
