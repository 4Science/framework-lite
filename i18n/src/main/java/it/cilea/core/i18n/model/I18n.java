package it.cilea.core.i18n.model;

import it.cilea.core.model.IdentifiableObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

@Entity
public class I18n extends IdentifiableObject {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "CONTEXT")
	@Size(max = 255)
	private String context;

	@Column(name = "KEY", length = 1000)
	@Size(max = 1000)
	private String key;

	@Column(name = "VALUE", length = 4000)
	@Size(max = 4000)
	private String value;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "i18n")
	private Set<I18nData> dataSet = new HashSet<I18nData>();

	@ElementCollection
	private Set<String> discriminatorSet = new HashSet<String>();

	@ElementCollection
	private Map<String, String> stringMap = new HashMap<String, String>();

	public I18n() {
	}

	@Transient
	public String getDiscriminatorValue(String discriminator) {

		Iterator<I18nData> iter = dataSet.iterator();
		while (iter.hasNext()) {
			I18nData I18nData = iter.next();
			if (discriminator.equals(I18nData.getDiscriminator())) {
				return I18nData.getStringValue();
			}
		}
		return null;
	}

	@Transient
	public Integer getI18nId() {
		return getId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Set<I18nData> getDataSet() {
		return dataSet;
	}

	public void setDataSet(Set<I18nData> dataSet) {
		this.dataSet = dataSet;
	}

	public Set<String> getDiscriminatorSet() {
		return discriminatorSet;
	}

	public void setDiscriminatorSet(Set<String> discriminatorSet) {
		this.discriminatorSet = discriminatorSet;
	}

	public Map<String, String> getStringMap() {
		return stringMap;
	}

	public void setStringMap(Map<String, String> stringMap) {
		this.stringMap = stringMap;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

}
