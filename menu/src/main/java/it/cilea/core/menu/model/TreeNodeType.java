package it.cilea.core.menu.model;

import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.Selectable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({ @NamedQuery(name = "TreeNodeType.findAll", query = "from TreeNodeType treeNodeType order by treeNodeType.description asc") })
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "IDENTIFIER" }) })
public class TreeNodeType extends IdentifiableObject implements Selectable {

	@Id
	@Column(name = "ID")
	private Integer id;

	@Column(name = "IDENTIFIER", nullable = false, unique = true, length = 4000)
	@NotNull
	private String identifier;

	@Column(name = "DESCRIPTION", nullable = false)
	@Size(min = 0, max = 250)
	private String description;

	@Column(name = "VALID_CHILDREN", nullable = false)
	@Size(min = 0, max = 250)
	private String validChildren;

	@Column(name = "ICON", nullable = false)
	@Size(min = 0, max = 250)
	private String icon;

	@Column(name = "MODEL_CLASS", nullable = true)
	@Size(min = 0, max = 4000)
	private String modelClass;

	@Column(name = "BEAN_SERVICE_NAME", nullable = true)
	@Size(min = 0, max = 4000)
	private String beanServiceName;

	@Column(name = "METHOD_NAME", nullable = true)
	@Size(min = 0, max = 4000)
	private String methodName;

	@Column(name = "VIEW_NAME", nullable = true)
	@Size(min = 0, max = 4000)
	private String viewName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValidChildren() {
		return validChildren;
	}

	public void setValidChildren(String validChildren) {
		this.validChildren = validChildren;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Transient
	public String getDisplayValue() {
		return getDescription();
	}

	@Transient
	public String getIdentifyingValue() {
		if (id == null)
			return null;
		return String.valueOf(id);
	}

	public String getModelClass() {
		return modelClass;
	}

	public void setModelClass(String modelClass) {
		this.modelClass = modelClass;
	}

	public String getBeanServiceName() {
		return beanServiceName;
	}

	public void setBeanServiceName(String beanServiceName) {
		this.beanServiceName = beanServiceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
