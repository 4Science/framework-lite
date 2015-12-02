package it.cilea.core.i18n.model;

import it.cilea.core.model.BaseObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class I18nData extends BaseObject {

	@Embeddable
	public static class I18nDataId extends BaseObject {

		@Column(name = "DISCRIMINATOR")
		private String discriminator;

		@Column(name = "FK_I18N")
		private Integer i18nId;

		public I18nDataId() {
		}

		public I18nDataId(String discriminator, Integer i18nId) {
			super();
			this.discriminator = discriminator;
			this.i18nId = i18nId;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof I18nDataId) {
				I18nDataId that = (I18nDataId) o;
				return this.i18nId.equals(that.i18nId) && this.discriminator.equals(that.discriminator);
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return discriminator.hashCode() + i18nId.hashCode();
		}

		public String getDiscriminator() {
			return discriminator;
		}

		public void setDiscriminator(String discriminator) {
			this.discriminator = discriminator;
		}

		public Integer getI18nId() {
			return i18nId;
		}

		public void setI18nId(Integer i18nId) {
			this.i18nId = i18nId;
		}

	}

	@EmbeddedId
	private I18nDataId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FK_I18N", insertable = false, updatable = false)
	private I18n i18n;

	@Column(name = "DISCRIMINATOR", insertable = false, updatable = false)
	private String discriminator;

	@Column(name = "STRING_VALUE", length = 4000)
	@Size(max = 4000)
	private String stringValue;

	public I18nData() {
	}

	public I18nDataId getId() {
		return id;
	}

	public void setId(I18nDataId id) {
		this.id = id;
	}

	public I18n getI18n() {
		return i18n;
	}

	public void setI18n(I18n i18n) {
		this.i18n = i18n;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

}
