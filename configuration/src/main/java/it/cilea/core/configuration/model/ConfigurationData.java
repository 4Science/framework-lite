package it.cilea.core.configuration.model;

import it.cilea.core.model.BaseObject;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class ConfigurationData extends BaseObject {

	@Embeddable
	public static class ConfigurationDataId extends BaseObject {

		@Column(name = "DISCRIMINATOR")
		private String discriminator;

		@Column(name = "FK_CONFIGURATION")
		private Integer configurationId;

		public ConfigurationDataId() {
		}

		public ConfigurationDataId(String discriminator, Integer configurationId) {
			super();
			this.discriminator = discriminator;
			this.configurationId = configurationId;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof ConfigurationDataId) {
				ConfigurationDataId that = (ConfigurationDataId) o;
				return this.configurationId.equals(that.configurationId)
						&& this.discriminator.equals(that.discriminator);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return discriminator.hashCode() + configurationId.hashCode();
		}

		public String getDiscriminator() {
			return discriminator;
		}

		public void setDiscriminator(String discriminator) {
			this.discriminator = discriminator;
		}

		public Integer getConfigurationId() {
			return configurationId;
		}

		public void setConfigurationId(Integer configurationId) {
			this.configurationId = configurationId;
		}

	}

	@EmbeddedId
	private ConfigurationDataId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CONFIGURATION", insertable = false, updatable = false)
	private Configuration configuration;

	@Column(name = "DISCRIMINATOR", insertable = false, updatable = false)
	private String discriminator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_CONFIGURATION_VALUE", insertable = false, updatable = false)
	private Configuration configurationValue;

	@Column(name = "FK_CONFIGURATION_VALUE")
	private Integer configurationValueId;

	@Column(name = "STRING_VALUE", length = 4000)
	private String stringValue;

	@Column(name = "BOOLEAN_VALUE")
	private Boolean booleanValue;

	@Column(name = "INTEGER_VALUE")
	private Integer integerValue;

	@Column(name = "NUMBER_VALUE", precision = 18, scale = 3)
	private BigDecimal numberValue;

	@Column(name = "DATE_VALUE")
	private Date dateValue;

	@Column(name = "CLOB_VALUE")
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String clobValue;

	@Column(name = "FK_BLOB_VALUE", length = 255)
	private String blobValueId;

	public ConfigurationData() {
	}

	public ConfigurationDataId getId() {
		return id;
	}

	public void setId(ConfigurationDataId id) {
		this.id = id;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public Configuration getConfigurationValue() {
		return configurationValue;
	}

	public void setConfigurationValue(Configuration configurationValue) {
		this.configurationValue = configurationValue;
	}

	public Integer getConfigurationValueId() {
		return configurationValueId;
	}

	public void setConfigurationValueId(Integer configurationValueId) {
		this.configurationValueId = configurationValueId;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public BigDecimal getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(BigDecimal numberValue) {
		this.numberValue = numberValue;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public String getClobValue() {
		return clobValue;
	}

	public void setClobValue(String clobValue) {
		this.clobValue = clobValue;
	}

	public String getBlobValueId() {
		return blobValueId;
	}

	public void setBlobValueId(String blobValueId) {
		this.blobValueId = blobValueId;
	}

}
