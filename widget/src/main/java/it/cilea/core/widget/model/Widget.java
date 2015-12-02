package it.cilea.core.widget.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.internal.util.SerializationHelper;

import it.cilea.core.authorization.context.AuthorizationUserHolder;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.spring.context.SecurityUserHolder;
import it.cilea.core.util.MessageUtilConstant;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.WidgetConstant.WidgetDictionaryType;

@Entity
@DiscriminatorColumn(name = "DISCRIMINATOR")
@DiscriminatorOptions(force = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Widget extends IdentifiableObject implements Cloneable {

	@Id
	@Column(name = "ID")
	protected Integer id;

	@Column(name = "DISCRIMINATOR", insertable = false, updatable = false, nullable = false, length = 255)
	@Size(min = 0, max = 255)
	protected String discriminator;

	@Column(name = "SECTION", insertable = false, updatable = false, nullable = true, length = 255)
	@Size(min = 0, max = 255)
	protected String section;

	@Column(name = "NAME", length = 4000)
	@Size(min = 0, max = 4000)
	protected String name;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "parentWidget", fetch = FetchType.LAZY)
	protected Set<Widget> childWidgetSet = new HashSet<Widget>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_WIDGET", insertable = false, updatable = false, nullable = true)
	private Widget parentWidget;

	@Column(name = "FK_WIDGET", insertable = true, updatable = true, nullable = true)
	private Integer parentWidgetId;

	@Transient
	protected String defaultValue = null;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "widget", fetch = FetchType.LAZY)
	protected Set<WidgetDictionary> widgetDictionarySet = new HashSet<WidgetDictionary>();

	@Transient
	protected Map<String, String> widgetDictionaryMap;

	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "widget", fetch = FetchType.LAZY)
	@OrderBy(value = "ordering")
	protected Set<Parameter> parameterSet = new LinkedHashSet<Parameter>();

	@Transient
	protected Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();

	@Column(name = "MODEL_ATTRIBUTE_NAME", length = 4000)
	@Size(min = 0, max = 4000)
	protected String modelAttributeName;

	@Column(name = "PAGE_ATTRIBUTE_NAME", length = 255)
	@Size(min = 0, max = 255)
	protected String pageAttributeName;

	@Transient
	protected String stringMatchType;

	@Transient
	protected String cssClass;

	@Transient
	protected Boolean renderAsHidden = false;
	@Transient
	private Boolean required = false;

	@Column(name = "ATTACCHED_RESOURCE", length = 4000)
	private String attacchedResource;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "child")
	@org.hibernate.annotations.OrderBy(clause = "ordering asc, FK_CHILD asc")
	private Set<WidgetLink> parentWidgetLinkSet = new LinkedHashSet<WidgetLink>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.OrderBy(clause = "ordering asc, FK_PARENT asc")
	private Set<WidgetLink> childWidgetLinkSet = new LinkedHashSet<WidgetLink>();

	@Transient
	protected Set<Widget> childWidgetSetFromLink = new LinkedHashSet<Widget>();

	@Transient
	private Integer position;

	public void init() throws Exception {
		if (widgetDictionaryMap == null) {
			widgetDictionaryMap = new HashMap<String, String>();
			for (WidgetDictionary widgetDictionary : widgetDictionarySet) {
				widgetDictionaryMap.put(widgetDictionary.getDiscriminator(), widgetDictionary.getI18nIdentifier());
			}
			if (widgetDictionaryMap.get(WidgetDictionaryType.LABEL.toString()) == null)
				widgetDictionaryMap.put(WidgetDictionaryType.LABEL.toString(),
						getI18nIdentifier(WidgetDictionaryType.LABEL));
		}

		for (Parameter parameter : parameterSet) {
			Set<String> parameterSetLoc = parameterMap.get(parameter.getDiscriminator());
			if (parameterSetLoc == null)
				parameterSetLoc = new LinkedHashSet<String>();
			parameterSetLoc.add(parameter.getValue());
			parameterMap.put(parameter.getDiscriminator(), parameterSetLoc);
		}

		for (WidgetLink widgetLink : childWidgetLinkSet)
			childWidgetSetFromLink.add(widgetLink.getChild());

		if (parameterMap.containsKey(ParameterType.RENDER_AS_HIDDEN.name()))
			renderAsHidden = Boolean.valueOf(parameterMap.get(ParameterType.RENDER_AS_HIDDEN.name()).iterator().next());

		if (renderAsHidden == null)
			renderAsHidden = false;

		if (parameterMap.containsKey(ParameterType.STRING_MATCH_TYPE.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.STRING_MATCH_TYPE.name()).iterator().next()))
			stringMatchType = parameterMap.get(ParameterType.STRING_MATCH_TYPE.name()).iterator().next();

		if (parameterMap.containsKey(ParameterType.CSS_CLASS.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.CSS_CLASS.name()).iterator().next()))
			cssClass = parameterMap.get(ParameterType.CSS_CLASS.name()).iterator().next();

		if (parameterMap.containsKey(ParameterType.DEFAULT_VALUE.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.DEFAULT_VALUE.name()).iterator().next()))
			defaultValue = parameterMap.get(ParameterType.DEFAULT_VALUE.name()).iterator().next();

		if (parameterMap.containsKey(ParameterType.REQUIRED.name()))
			required = Boolean.valueOf(parameterMap.get(ParameterType.REQUIRED.name()).iterator().next());

	}

	public String getWidgetType() {
		return getDiscriminator();
	}

	/**
	 * Restituisce un array di String contenente i parametri di request
	 * associati DIRETTAMENTE al widget
	 */
	public String[] getRequestValues(HttpServletRequest request) {

		String[] requestValues = new String[0];
		if (defaultValue != null)
			requestValues = new String[] { defaultValue };

		Object objectRequestValues = request.getParameterValues(this.getRequestAttributeName());

		if (objectRequestValues == null)
			objectRequestValues = request.getAttribute(this.getRequestAttributeName());
		// removed because dead code
		// else if (objectRequestValues == null)
		// objectRequestValues =
		// request.getSession().getAttribute(this.getRequestAttributeName());

		if (objectRequestValues != null) {
			if (objectRequestValues instanceof String) {
				String temp = (String) objectRequestValues;
				requestValues = temp.split(",");
			} else if (objectRequestValues instanceof String[]) {
				requestValues = (String[]) objectRequestValues;
			}
		} else {
			String lowerBound = (String) request.getAttribute(this.getLowerBoundRequestAttributeName());
			String upperBound = (String) request.getAttribute(this.getUpperBoundRequestAttributeName());
			if (lowerBound != null || upperBound != null) {
				requestValues = new String[2];
				requestValues[0] = lowerBound;
				requestValues[1] = upperBound;
			}
		}
		return requestValues;
	}

	/**
	 * Restituisce un array di String contenente i parametri di request
	 * associati DIRETTAMENTE al widget di modelAttributeName passato come
	 * argomento Questo metodo serve per gestire il reperimento dei paramentri
	 * relativi ai widget dipendenti
	 */
	protected String[] getRequestValues(HttpServletRequest request, String pageAttributeName) {

		String[] requestValues = new String[0];
		if (defaultValue != null)
			requestValues = new String[] { defaultValue };

		Object objectRequestValues = request.getParameterValues(pageAttributeName);

		if (objectRequestValues == null)
			objectRequestValues = request.getAttribute(pageAttributeName);
		// removed because dead code
		// else if (objectRequestValues == null)
		// objectRequestValues =
		// request.getSession().getAttribute(pageAttributeName);

		if (objectRequestValues != null) {
			if (objectRequestValues instanceof String) {
				String temp = (String) objectRequestValues;
				requestValues = temp.split(",");
			} else if (objectRequestValues instanceof String[]) {
				requestValues = (String[]) objectRequestValues;
			}
		}

		return requestValues;
	}

	public String getPageAttributeName() {
		if (pageAttributeName == null)
			pageAttributeName = modelAttributeName;
		return pageAttributeName;
	}

	public boolean isWidgetGranted() {
		if (StringUtils.isBlank(this.getAttacchedResource()))
			return true;
		if (SecurityUserHolder.getUser() == null)
			return false;
		return AuthorizationUserHolder.getUser().hasAuthorities(this.getAttacchedResource());
	}

	public Boolean getIsWidgetGranted() {
		return isWidgetGranted();
	}

	public String getRequestAttributeName() {
		return getPageAttributeName();
	}

	public String getLowerBoundRequestAttributeName() {
		return WidgetConstant.FIRST_FIELD_PREFIX + getPageAttributeName();
	}

	public String getUpperBoundRequestAttributeName() {
		return WidgetConstant.SECOND_FIELD_PREFIX + getPageAttributeName();
	}

	public String getLabel() {
		if (!isWidgetGranted())
			return null;
		return getGenericWidgetDictionaryType(WidgetDictionaryType.LABEL);
	}

	public String getLabelKey() {
		if (!isWidgetGranted())
			return null;
		return getGenericWidgetDictionaryTypeKey(WidgetDictionaryType.LABEL);
	}

	public void setLabelKey(String labelKey) {
		widgetDictionaryMap.put(WidgetDictionaryType.LABEL.name(), labelKey);
	}

	public String getInfo() {
		if (!isWidgetGranted())
			return null;
		return getGenericWidgetDictionaryType(WidgetDictionaryType.INFO);
	}

	public String getInfoKey() {
		if (!isWidgetGranted())
			return null;
		return getGenericWidgetDictionaryTypeKey(WidgetDictionaryType.INFO);
	}

	public String getError() {
		if (!isWidgetGranted())
			return null;
		return getGenericWidgetDictionaryType(WidgetDictionaryType.ERROR);
	}

	public String getErrorKey() {
		if (!isWidgetGranted())
			return null;
		return getGenericWidgetDictionaryTypeKey(WidgetDictionaryType.ERROR);
	}

	public Set<String> getParameterNameSet() {
		Set<String> parameterNameList = parameterMap.get(ParameterType.NAME.toString());
		if (parameterNameList != null)
			return parameterNameList;
		else
			return new HashSet<String>();
	}

	public Set<String> getParameterValueSet() {
		Set<String> parameterValueList = parameterMap.get(ParameterType.VALUE.toString());
		if (parameterValueList != null)
			return parameterValueList;
		else
			return new HashSet<String>();
	}

	protected String getGenericWidgetDictionaryType(WidgetDictionaryType widgetDictionaryType) {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = MessageUtilConstant.messageUtil
				.findMessage(widgetDictionaryMap.get(widgetDictionaryType.toString()));
		if (message == null)
			return "";
		return message;
	}

	protected String getGenericWidgetDictionaryTypeKey(WidgetDictionaryType widgetDictionaryType) {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String message = widgetDictionaryMap.get(widgetDictionaryType.toString());
		if (message == null)
			return "";
		return message;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Set<WidgetDictionary> getWidgetDictionarySet() {
		return widgetDictionarySet;
	}

	public void setWidgetDictionarySet(Set<WidgetDictionary> widgetDictionarySet) {
		this.widgetDictionarySet = widgetDictionarySet;
	}

	public Set<Parameter> getParameterSet() {
		return parameterSet;
	}

	public void setParameterSet(Set<Parameter> parameterSet) {
		this.parameterSet = parameterSet;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getModelAttributeName() {
		return modelAttributeName;
	}

	public void setModelAttributeName(String modelAttributeName) {
		this.modelAttributeName = modelAttributeName;
	}

	public String getStringMatchType() {
		return stringMatchType;
	}

	public void setStringMatchType(String stringMatchType) {
		this.stringMatchType = stringMatchType;
	}

	public Boolean getRenderAsHidden() {
		return renderAsHidden;
	}

	public void setRenderAsHidden(Boolean renderAsHidden) {
		this.renderAsHidden = renderAsHidden;
	}

	public void setPageAttributeName(String pageAttributeName) {
		this.pageAttributeName = pageAttributeName;
	}

	public Object clone() throws CloneNotSupportedException {
		Widget clone = (Widget) SerializationHelper.clone(this);
		return clone;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getAttacchedResource() {
		return attacchedResource;
	}

	public void setAttacchedResource(String attacchedResource) {
		this.attacchedResource = attacchedResource;
	}

	@Transient
	public String getIdentifier() {
		return name;
	}

	public String getI18nIdentifier(WidgetDictionaryType widgetDictionaryType) {
		String i18nIdentifier = "widget."
				+ StringUtils.replace(StringUtils.replace(getIdentifier(), "/", "."), " ", "_") + "."
				+ StringUtils.lowerCase(widgetDictionaryType.toString());
		i18nIdentifier = StringUtils.replace(i18nIdentifier, "..", ".");
		return i18nIdentifier;
	}

	public Widget getParentWidget() {
		return parentWidget;
	}

	public void setParentWidget(Widget parentWidget) {
		this.parentWidget = parentWidget;
	}

	public Integer getParentWidgetId() {
		return parentWidgetId;
	}

	public void setParentWidgetId(Integer parentWidgetId) {
		this.parentWidgetId = parentWidgetId;
	}

	public Set<Widget> getChildWidgetSet() {
		return childWidgetSet;
	}

	public void setChildWidgetSet(Set<Widget> childWidgetSet) {
		this.childWidgetSet = childWidgetSet;
	}

	public Set<WidgetLink> getParentWidgetLinkSet() {
		return parentWidgetLinkSet;
	}

	public void setParentWidgetLinkSet(Set<WidgetLink> parentWidgetLinkSet) {
		this.parentWidgetLinkSet = parentWidgetLinkSet;
	}

	public Set<WidgetLink> getChildWidgetLinkSet() {
		return childWidgetLinkSet;
	}

	public void setChildWidgetLinkSet(Set<WidgetLink> childWidgetLinkSet) {
		this.childWidgetLinkSet = childWidgetLinkSet;
	}

	public Set<Widget> getChildWidgetSetFromLink() {
		return childWidgetSetFromLink;
	}

	public void setChildWidgetSetFromLink(Set<Widget> childWidgetSetFromLink) {
		this.childWidgetSetFromLink = childWidgetSetFromLink;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}
}