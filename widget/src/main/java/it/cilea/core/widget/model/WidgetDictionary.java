package it.cilea.core.widget.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import it.cilea.core.authorization.AuthorizationConstant;
import it.cilea.core.authorization.model.AuthorizationResource;
import it.cilea.core.authorization.model.impl.Resource;
import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;
import it.cilea.core.util.MessageUtilConstant;
import it.cilea.core.widget.WidgetConstant.WidgetDictionaryType;

@Entity
public class WidgetDictionary extends IdentifiableObject
		implements Selectable, Comparable<WidgetDictionary>, AuthorizationResource {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DISCRIMINATOR")
	private String discriminator;

	@Column(name = "ORDERING")
	private Integer ordering;

	@Column(name = "ACTIVATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date activationDate;

	@Column(name = "DEACTIVATION_DATE")
	@Temporal(TemporalType.DATE)
	private Date deactivationDate;

	@Column(name = "HIDDEN_VALUE", length = 4000)
	private String hiddenValue;

	@Column(name = "STYLE_VALUE")
	private String styleValue;

	@Column(name = "WITH_CLAUSE")
	private String withClause;

	@Column(name = "DECORATOR_CLASS")
	private String decoratorClass;

	// @Column(name = "HARDCODED_VALUE")
	// private String hardCodedValue;

	@Column(name = "I18N_CUSTOM_IDENTIFIER", length = 4000)
	private String i18nCustomIdentifier;

	// TODO DA ELIMINARE NON CREDO CHE SERVANO
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_DICTIONARY", insertable = false, updatable = false, nullable = true)
	private WidgetDictionary parent;

	@Column(name = "FK_DICTIONARY", insertable = true, updatable = true, nullable = true)
	private Integer parentId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_WIDGET", insertable = true, updatable = true, nullable = true)
	private Widget widget;

	@Column(name = "FK_WIDGET", insertable = false, updatable = false, nullable = false)
	private Integer widgetId;

	public WidgetDictionary() {
	}

	public WidgetDictionary(String discriminator) {
		this.discriminator = discriminator;
	}

	@Transient
	public Resource getAuthorizationResource() {
		String key = getWidget().getName() + "/dictionary/" + discriminator;
		if (StringUtils.isNotBlank(this.hiddenValue))
			key += "/" + this.hiddenValue;
		key = StringUtils.replace(key, "//", "/");
		key += ".widgetDictionary";
		return AuthorizationConstant.RESOURCE_MAP.get(key);
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

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	public Date getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(Date activationDate) {
		this.activationDate = activationDate;
	}

	public Date getDeactivationDate() {
		return deactivationDate;
	}

	public void setDeactivationDate(Date deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	public WidgetDictionary getParent() {
		return parent;
	}

	public void setParent(WidgetDictionary parent) {
		this.parent = parent;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public Integer getWidgetId() {
		return widgetId;
	}

	public void setWidgetId(Integer widgetId) {
		this.widgetId = widgetId;
	}

	@Override
	public String getDisplayValue() {
		return MessageUtilConstant.messageUtil.findMessage(getI18nIdentifier());
	}

	@Override
	public String getIdentifyingValue() {
		return id.toString();
	}

	public int compareTo(WidgetDictionary altroWidgetDictionary) {
		if (this.getDiscriminator() != null && altroWidgetDictionary.getDiscriminator() != null) {
			if (this.getDiscriminator().equals(altroWidgetDictionary.getDiscriminator())) {
				if (this.getOrdering() == null || altroWidgetDictionary.getOrdering() == null
						|| this.getOrdering().equals(altroWidgetDictionary.getOrdering()))
					return this.getId().compareTo(altroWidgetDictionary.getId());
				return this.getOrdering().compareTo(altroWidgetDictionary.getOrdering());
			}
			return this.getDiscriminator().compareTo(altroWidgetDictionary.getDiscriminator());
		} else
			return this.getId().compareTo(altroWidgetDictionary.getId());
	}

	@Override
	public boolean equals(Object obj) {
		WidgetDictionary widgetDictionary = (WidgetDictionary) obj;
		if (widgetDictionary == null)
			return false;

		if (id != null)
			return id.equals(widgetDictionary.getId());
		else
			return getId().equals(widgetDictionary.getId());
	}

	public String getHiddenValue() {
		return hiddenValue;
	}

	public String getHiddenValueConfigurationReplaced() {
		return ConfigurationUtil.replaceText(hiddenValue);
	}

	public void setHiddenValue(String hiddenValue) {
		this.hiddenValue = hiddenValue;
	}

	public String getDecoratorClass() {
		return decoratorClass;
	}

	public void setDecoratorClass(String decoratorClass) {
		this.decoratorClass = decoratorClass;
	}

	public String getStyleValue() {
		return styleValue;
	}

	public void setStyleValue(String styleValue) {
		this.styleValue = styleValue;
	}

	public String getWithClause() {
		return withClause;
	}

	public void setWithClause(String withClause) {
		this.withClause = withClause;
	}

	public String getI18nCustomIdentifier() {
		return i18nCustomIdentifier;
	}

	public void setI18nCustomIdentifier(String i18nCustomIdentifier) {
		this.i18nCustomIdentifier = i18nCustomIdentifier;
	}

	public String getI18nIdentifier() {
		if (StringUtils.isNotBlank(i18nCustomIdentifier))
			return i18nCustomIdentifier;
		String i18nIdentifier = getWidget().getI18nIdentifier(WidgetDictionaryType.valueOf(discriminator));
		if (StringUtils.isNotBlank(this.hiddenValue))
			i18nIdentifier += "." + this.hiddenValue;
		i18nIdentifier = StringUtils.replace(i18nIdentifier, "..", ".");
		return i18nIdentifier;
	}
}
