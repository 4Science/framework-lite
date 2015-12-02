package it.cilea.core.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Base class for objects that can be uniquely identified via an Integer
 * property, id.
 */
@MappedSuperclass
public abstract class IdentifiableObject extends BaseObject implements Identifiable<Integer>, Serializable {

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}

		if (!(object instanceof IdentifiableObject)) {
			return false;
		}

		IdentifiableObject obj = (IdentifiableObject) object;

		return new EqualsBuilder().append(this.getId(), obj.getId()).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(MAGICNUM1, MAGICNUM2).append(this.getId()).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(this.getId()).toString();
	}

}
