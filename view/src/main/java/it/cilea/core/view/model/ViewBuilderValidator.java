package it.cilea.core.view.model;

import it.cilea.core.model.IdentifiableObject;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.DiscriminatorOptions;
import org.springframework.context.ApplicationContext;

@Entity
@DiscriminatorColumn(name = "VALIDATOR_TYPE")
@DiscriminatorOptions(force = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ViewBuilderValidator<V> extends IdentifiableObject implements Comparable<ViewBuilderValidator> {

	@Id
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "FK_VIEW_BUILDER", insertable = false, updatable = false, nullable = false)
	private ViewBuilder viewBuilder;

	@Column(name = "FK_VIEW_BUILDER", insertable = true, updatable = true, nullable = true)
	private Integer viewBuilderId;

	@Column(name = "ORDERING")
	private Integer ordering;

	@Column(name = "VALIDATOR_TYPE", insertable = false, updatable = false)
	private String validatorType;

	@Transient
	protected V validator;

	@Override
	public int compareTo(ViewBuilderValidator o) {
		if (getOrdering() == null)
			return 1;
		if (o.getOrdering() == null)
			return -1;
		if (getOrdering().equals(o.getOrdering()))
			return getId().compareTo(o.getId());
		return getOrdering().compareTo(o.getOrdering());
	}

	public void init(ApplicationContext applicationContext) throws Exception {
	}

	public ViewBuilder getViewBuilder() {
		return viewBuilder;
	}

	public void setViewBuilder(ViewBuilder viewBuilder) {
		this.viewBuilder = viewBuilder;
	}

	public Integer getViewBuilderId() {
		return viewBuilderId;
	}

	public void setViewBuilderId(Integer viewBuilderId) {
		this.viewBuilderId = viewBuilderId;
	}

	public Integer getOrdering() {
		return ordering;
	}

	public void setOrdering(Integer ordering) {
		this.ordering = ordering;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public V getValidator() {
		return validator;
	}

	public void setValidator(V validator) {
		this.validator = validator;
	}

	public String getValidatorType() {
		return validatorType;
	}

	public void setValidatorType(String validatorType) {
		this.validatorType = validatorType;
	}
}
