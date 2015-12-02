package it.cilea.core.view.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.view.util.ViewUtil;

@Entity
public class ViewBuilder extends IdentifiableObject {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "STATE", length = 4000)
	@Size(min = 0, max = 4000)
	private String state = ViewBuilderState.draft.name();

	public enum ViewBuilderState {
		draft, withdrawn, active, deprecated
	}

	@Column(name = "IDENTIFIER", length = 4000)
	@Size(min = 0, max = 4000)
	private String identifier;

	@Column(name = "ALTERNATIVE_IDENTIFIERS", length = 4000)
	@Size(min = 0, max = 4000)
	private String alternativeIdentifiers;

	@Column(name = "VIEW_NAME", length = 4000)
	@Size(min = 0, max = 4000)
	private String viewName;

	@Column(name = "CUSTOM_TITLE_KEY", length = 4000)
	@Size(min = 0, max = 4000)
	private String customTitleKey;

	@Column(name = "MANAGED_BY", length = 255)
	private String managedBy = ViewBuilderManagedBy.user.name();

	public enum ViewBuilderManagedBy {
		user, system
	}

	@Column(name = "VERSION_DATE")
	private Date versionDate;

	@Column(name = "VERSION_INFO")
	private String versionInfo;

	@OrderBy(value = "ordering")
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "viewBuilder", fetch = FetchType.LAZY)
	protected Set<ViewBuilderWidgetLink> viewBuilderWidgetLinkSet = new LinkedHashSet<ViewBuilderWidgetLink>();

	@OrderBy(value = "ordering")
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "viewBuilder", fetch = FetchType.LAZY)
	protected Set<ViewBuilderValidator> viewBuilderValidatorSet = new LinkedHashSet<ViewBuilderValidator>();

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "viewBuilder", fetch = FetchType.LAZY)
	protected Set<ViewBuilderParameter> viewBuilderParameterSet = new HashSet<ViewBuilderParameter>();

	@Transient
	protected Map<String, String> viewBuilderParameterMap = new HashMap<String, String>();

	public void init() {
		for (ViewBuilderParameter viewBuilderParameter : viewBuilderParameterSet) {
			String configValue = viewBuilderParameter.getValue();
			configValue = ConfigurationUtil.replaceText(configValue);
			viewBuilderParameterMap.put(viewBuilderParameter.getName(), configValue);
		}
		if (viewBuilderParameterMap.containsKey("hideInputForm"))
			addInputForm = Boolean.valueOf(viewBuilderParameterMap.get("hideInputForm"));
	}

	@Transient
	public String getTitleKey() {
		if (customTitleKey != null)
			return customTitleKey;
		return "title" + StringUtils.replace(ViewUtil.correctIdentifier(identifier), "/", ".");
	}

	@Transient
	private Boolean addInputForm = true;

	@Transient
	public List<Object> getValidatorList() {
		List<Object> list = new ArrayList<Object>();
		for (ViewBuilderValidator viewBuilderValidator : viewBuilderValidatorSet) {
			list.add(viewBuilderValidator.getValidator());
		}
		return list;
	}

	public Set<String> getIdentifierSet() {
		Set<String> identifierSet = new LinkedHashSet<String>();
		identifierSet.add(ViewUtil.correctIdentifier(identifier));
		if (StringUtils.isNotBlank(alternativeIdentifiers)) {
			String[] alternativeIdentifierArray = StringUtils.split(alternativeIdentifiers, ",");
			for (String alternativeIdentifier : alternativeIdentifierArray)
				identifierSet.add(ViewUtil.correctIdentifier(alternativeIdentifier));
		}
		return identifierSet;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		if (viewName != null)
			this.viewName = viewName;
	}

	public String getCustomTitleKey() {
		return customTitleKey;
	}

	public void setCustomTitleKey(String customTitleKey) {
		this.customTitleKey = customTitleKey;
	}

	public Set<ViewBuilderWidgetLink> getViewBuilderWidgetLinkSet() {
		return viewBuilderWidgetLinkSet;
	}

	public void setViewBuilderWidgetLinkSet(Set<ViewBuilderWidgetLink> viewBuilderWidgetLinkSet) {
		this.viewBuilderWidgetLinkSet = viewBuilderWidgetLinkSet;
	}

	public Set<ViewBuilderParameter> getViewBuilderParameterSet() {
		return viewBuilderParameterSet;
	}

	public void setViewBuilderParameterSet(Set<ViewBuilderParameter> viewBuilderParameterSet) {
		this.viewBuilderParameterSet = viewBuilderParameterSet;
	}

	public Map<String, String> getViewBuilderParameterMap() {
		return viewBuilderParameterMap;
	}

	public void setViewBuilderParameterMap(Map<String, String> viewBuilderParameterMap) {
		this.viewBuilderParameterMap = viewBuilderParameterMap;
	}

	public Boolean getAddInputForm() {
		return addInputForm;
	}

	public void setAddInputForm(Boolean addInputForm) {
		this.addInputForm = addInputForm;
	}

	public String getManagedBy() {
		return managedBy;
	}

	public void setManagedBy(String managedBy) {
		this.managedBy = managedBy;
	}

	public Date getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Set<ViewBuilderValidator> getViewBuilderValidatorSet() {
		return viewBuilderValidatorSet;
	}

	public void setViewBuilderValidatorSet(Set<ViewBuilderValidator> viewBuilderValidatorSet) {
		this.viewBuilderValidatorSet = viewBuilderValidatorSet;
	}

	public String getAlternativeIdentifiers() {
		return alternativeIdentifiers;
	}

	public void setAlternativeIdentifiers(String alternativeIdentifiers) {
		this.alternativeIdentifiers = alternativeIdentifiers;
	}

}