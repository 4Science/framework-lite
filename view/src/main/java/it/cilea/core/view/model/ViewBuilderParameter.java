package it.cilea.core.view.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

@Entity
public class ViewBuilderParameter implements Comparable<ViewBuilderParameter> {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "NAME", length = 255)
	@Size(min = 0, max = 255)
	private String name;

	@Column(name = "VALUE", length = 4000)
	@Size(min = 0, max = 4000)
	private String value;

	@ManyToOne
	@JoinColumn(name = "FK_VIEW_BUILDER", referencedColumnName = "ID", insertable = false, updatable = false, nullable = false)
	private ViewBuilder viewBuilder;
	@Column(name = "FK_VIEW_BUILDER", insertable = true, updatable = true, nullable = true)
	private Integer viewBuilderId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public int compareTo(ViewBuilderParameter altroViewBuilderParameter) {
		if (this.getName() != null && altroViewBuilderParameter.getName() != null) {
			if (!this.getName().equals(altroViewBuilderParameter.getName()))
				return this.getName().compareTo(altroViewBuilderParameter.getName());
		}
		return this.getId().compareTo(altroViewBuilderParameter.getId());
	}

}