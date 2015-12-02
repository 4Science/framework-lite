package it.cilea.core.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import it.cilea.core.model.IdentifiableObject;

@Entity
public class SearchBuilderParameter extends IdentifiableObject implements Comparable<SearchBuilderParameter> {

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
	@JoinColumn(name = "FK_SEARCH_BUILDER", referencedColumnName = "ID", insertable = false, updatable = false, nullable = false)
	private SearchBuilder searchBuilder;
	@Column(name = "FK_SEARCH_BUILDER", insertable = true, updatable = true, nullable = true)
	private Integer searchBuilderId;

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

	public SearchBuilder getSearchBuilder() {
		return searchBuilder;
	}

	public void setSearchBuilder(SearchBuilder searchBuilder) {
		this.searchBuilder = searchBuilder;
	}

	public Integer getSearchBuilderId() {
		return searchBuilderId;
	}

	public void setSearchBuilderId(Integer searchBuilderId) {
		this.searchBuilderId = searchBuilderId;
	}

	@Override
	public int compareTo(SearchBuilderParameter other) {
		if (this.getName() != null && other.getName() != null) {
			if (!this.getName().equals(other.getName()))
				return this.getName().compareTo(other.getName());
		}
		return this.getId().compareTo(other.getId());
	}

}