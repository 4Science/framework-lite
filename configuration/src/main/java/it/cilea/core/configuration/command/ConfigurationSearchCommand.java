package it.cilea.core.configuration.command;

import it.cilea.core.displaytag.dto.DisplayTagData;

import org.apache.commons.lang.ArrayUtils;

public class ConfigurationSearchCommand extends DisplayTagData {

	private Integer[] configurationIds;
	private Integer parentConfigurationId;
	private String discriminator;
	private String description;
	private String descriptionExact;
	private String displayValue;
	private Boolean onlyLeaf;
	private Boolean onlyRoot;

	public ConfigurationSearchCommand() {
		super.setSort("configuration.description");
	}

	public Integer[] getConfigurationIds() {
		configurationIds = (Integer[]) ArrayUtils.removeElement(configurationIds, null);
		return configurationIds;
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

	public Boolean getOnlyLeaf() {
		return onlyLeaf;
	}

	public void setOnlyLeaf(Boolean onlyLeaf) {
		this.onlyLeaf = onlyLeaf;
	}

	public Boolean getOnlyRoot() {
		return onlyRoot;
	}

	public void setOnlyRoot(Boolean onlyRoot) {
		this.onlyRoot = onlyRoot;
	}

	public void setConfigurationIds(Integer[] configurationIds) {
		this.configurationIds = configurationIds;
	}

	public Integer getParentConfigurationId() {
		return parentConfigurationId;
	}

	public void setParentConfigurationId(Integer parentConfigurationId) {
		this.parentConfigurationId = parentConfigurationId;
	}

	public String getDescriptionExact() {
		return descriptionExact;
	}

	public void setDescriptionExact(String descriptionExact) {
		this.descriptionExact = descriptionExact;
	}

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}
}
