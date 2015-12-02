package it.cilea.core.configuration.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import it.cilea.core.annotation.TypeOfCollection;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;
import it.cilea.core.spring.util.MessageUtil;

@Entity
public class Configuration extends IdentifiableObject implements Selectable {

	@Transient
	private UUID uuid = UUID.randomUUID();

	public String getUniqueIdentifier() {
		return uuid.toString();
	}

	public void setUniqueIdentifier(String uuidString) {
		this.uuid = UUID.fromString(uuidString);
	}

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "DISCRIMINATOR", nullable = false)
	private String discriminator;

	@Column(name = "DESCRIPTION", nullable = false, length = 4000)
	private String description;

	@Column(name = "KEY", nullable = false, length = 4000)
	private String key;
	@Column(name = "VALUE", nullable = false, length = 4000)
	private String value;

	@Column(name = "START_DATE")
	private Date startDate;
	@Column(name = "END_DATE")
	private Date endDate;

	@Column(name = "PRIORITY")
	private Integer priority;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "configuration")
	private Set<ConfigurationData> dataSet = new HashSet<ConfigurationData>();

	@ManyToMany
	@ElementCollection
	@TypeOfCollection(type = Configuration.class)
	private Map<String, Configuration> configurationMap = new HashMap<String, Configuration>();

	@ElementCollection
	private Map<String, String> stringMap = new HashMap<String, String>();

	@ElementCollection
	private Map<String, Boolean> booleanMap = new HashMap<String, Boolean>();

	@ElementCollection
	private Map<String, Integer> integerMap = new HashMap<String, Integer>();

	@ElementCollection
	private Map<String, BigDecimal> numberMap = new HashMap<String, BigDecimal>();

	@ElementCollection
	@TypeOfCollection(type = Date.class)
	private Map<String, Date> dateMap = new HashMap<String, Date>();

	@ElementCollection
	private Map<String, String> clobMap = new HashMap<String, String>();

	@ElementCollection
	private Map<String, String> blobMap = new HashMap<String, String>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "child")
	@org.hibernate.annotations.OrderBy(clause = "priority asc, FK_CHILD asc")
	private Set<ConfigurationLink> parentConfigurationLinkSet = new LinkedHashSet<ConfigurationLink>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "parent")
	@org.hibernate.annotations.OrderBy(clause = "priority asc, FK_PARENT asc")
	private Set<ConfigurationLink> childConfigurationLinkSet = new LinkedHashSet<ConfigurationLink>();

	public Configuration() {
	}

	@Transient
	public String getIdentifyingValue() {
		if (id == null)
			return null;
		return String.valueOf(id);
	}

	@Transient
	public String getDisplayValue() {
		return MessageUtil.findLabel(stringMap, description, "description_");
	}

	public void setDisplayValue(String displayValue) {
		setDescription(displayValue);
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Transient
	public Set<ConfigurationLink> getParentConfigurationLinkSet(String discriminator) throws Exception {
		Set<ConfigurationLink> result = new TreeSet<ConfigurationLink>();
		for (ConfigurationLink configurationLink : parentConfigurationLinkSet) {
			if (discriminator.equals(configurationLink.getDiscriminator())) {
				result.add(configurationLink);
			}
		}
		return result;
	}

	@Transient
	public Set<ConfigurationLink> getChildConfigurationLinkSet(String discriminator) throws Exception {
		Set<ConfigurationLink> result = new TreeSet<ConfigurationLink>();
		for (ConfigurationLink configurationLink : childConfigurationLinkSet) {
			if (discriminator.equals(configurationLink.getDiscriminator())) {
				result.add(configurationLink);
			}
		}
		return result;
	}

	@Transient
	public Map<String, Set<ConfigurationLink>> getParentConfigurationLinkMapByDiscriminator() {
		Map<String, Set<ConfigurationLink>> map = new HashMap<String, Set<ConfigurationLink>>();
		for (ConfigurationLink ro : parentConfigurationLinkSet) {
			if (map.get(ro.getDiscriminator()) == null)
				map.put(ro.getDiscriminator(), new LinkedHashSet<ConfigurationLink>());
			map.get(ro.getDiscriminator()).add(ro);
		}
		return map;
	}

	@Transient
	public Map<String, Set<ConfigurationLink>> getChildConfigurationLinkMapByDiscriminator() {
		Map<String, Set<ConfigurationLink>> map = new HashMap<String, Set<ConfigurationLink>>();
		for (ConfigurationLink ro : childConfigurationLinkSet) {
			if (map.get(ro.getDiscriminator()) == null)
				map.put(ro.getDiscriminator(), new LinkedHashSet<ConfigurationLink>());
			map.get(ro.getDiscriminator()).add(ro);
		}
		return map;
	}

	// TODO: ELIMINARE!!!!!!!!!!!!!
	@Transient
	public Set<ConfigurationLink> getConfigurationLinkSet(String discriminator) throws Exception {
		Set<ConfigurationLink> result = new TreeSet<ConfigurationLink>();
		for (ConfigurationLink configurationLink : childConfigurationLinkSet) {
			if (discriminator.equals(configurationLink.getDiscriminator())) {
				result.add(configurationLink);
			}
		}
		return result;
	}

	@Transient
	public Integer getConfigurationId() {
		return getId();
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Set<ConfigurationData> getDataSet() {
		return dataSet;
	}

	public void setDataSet(Set<ConfigurationData> dataSet) {
		this.dataSet = dataSet;
	}

	public Map<String, Configuration> getConfigurationMap() {
		return configurationMap;
	}

	public void setConfigurationMap(Map<String, Configuration> configurationMap) {
		this.configurationMap = configurationMap;
	}

	public Map<String, String> getStringMap() {
		return stringMap;
	}

	public void setStringMap(Map<String, String> stringMap) {
		this.stringMap = stringMap;
	}

	public Map<String, Boolean> getBooleanMap() {
		return booleanMap;
	}

	public void setBooleanMap(Map<String, Boolean> booleanMap) {
		this.booleanMap = booleanMap;
	}

	public Map<String, Integer> getIntegerMap() {
		return integerMap;
	}

	public void setIntegerMap(Map<String, Integer> integerMap) {
		this.integerMap = integerMap;
	}

	public Map<String, BigDecimal> getNumberMap() {
		return numberMap;
	}

	public void setNumberMap(Map<String, BigDecimal> numberMap) {
		this.numberMap = numberMap;
	}

	public Map<String, Date> getDateMap() {
		return dateMap;
	}

	public void setDateMap(Map<String, Date> dateMap) {
		this.dateMap = dateMap;
	}

	public Map<String, String> getClobMap() {
		return clobMap;
	}

	public void setClobMap(Map<String, String> clobMap) {
		this.clobMap = clobMap;
	}

	public Set<ConfigurationLink> getParentConfigurationLinkSet() {
		return parentConfigurationLinkSet;
	}

	public void setParentConfigurationLinkSet(Set<ConfigurationLink> parentConfigurationLinkSet) {
		this.parentConfigurationLinkSet = parentConfigurationLinkSet;
	}

	public Set<ConfigurationLink> getChildConfigurationLinkSet() {
		return childConfigurationLinkSet;
	}

	public void setChildConfigurationLinkSet(Set<ConfigurationLink> childConfigurationLinkSet) {
		this.childConfigurationLinkSet = childConfigurationLinkSet;
	}

	public Map<String, String> getBlobMap() {
		return blobMap;
	}

	public void setBlobMap(Map<String, String> blobMap) {
		this.blobMap = blobMap;
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
}
