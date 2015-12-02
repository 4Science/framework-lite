package it.cilea.core.annotation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class CompositeId implements Serializable {

	private static final long serialVersionUID = 2334373603795816728L;

	public CompositeId() {
	}

	public CompositeId(Integer id, String discriminator) {
		this.id = id;
		this.discriminator = discriminator;
	}

	@Id
	@Column(name = "ID")
	private Integer id;

	@Id
	@Column(name = "DISCRIMINATOR")
	private String discriminator;

	public boolean equals(Object obj) {
		if (!(obj instanceof CompositeId)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		CompositeId entitaRcId = (CompositeId) obj;

		return new EqualsBuilder().append(id, entitaRcId.getId()).append(discriminator, entitaRcId.getDiscriminator())
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(17, 37).append(id).append(discriminator).toHashCode();
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
}
