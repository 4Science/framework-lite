package it.cilea.core.widget.model.impl.command;

import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

@Entity
@DiscriminatorValue("displayTag:table")
public class DisplayTagTableWidget extends Widget {
	@Transient
	private String displayTagName = "displayTagData";
	@Transient
	private Integer cellspacing = 0;
	@Transient
	private Integer cellpadding = 0;
	@Transient
	private String requestURI = "";
	@Transient
	private String rowId = "element";
	@Transient
	private Boolean isFragmentTable = true;

	@Override
	public void init() throws Exception {
		super.init();
		cssClass = "jtable";
		if (parameterMap.get(WidgetConstant.ParameterType.IS_FRAGMENT_TABLE.name()) != null)
			isFragmentTable = Boolean.valueOf(parameterMap.get(WidgetConstant.ParameterType.IS_FRAGMENT_TABLE.name())
					.iterator().next());
	}

	public String[] getRequestValues(HttpServletRequest request) {
		return null;
	}

	public String getDisplayTagName() {
		return displayTagName;
	}

	public void setDisplayTagName(String displayTagName) {
		this.displayTagName = displayTagName;
	}

	public Integer getCellspacing() {
		return cellspacing;
	}

	public void setCellspacing(Integer cellspacing) {
		this.cellspacing = cellspacing;
	}

	public Integer getCellpadding() {
		return cellpadding;
	}

	public void setCellpadding(Integer cellpadding) {
		this.cellpadding = cellpadding;
	}

	public String getRequestURI() {
		return requestURI;
	}

	public void setRequestURI(String requestURI) {
		this.requestURI = requestURI;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public Boolean getIsFragmentTable() {
		return isFragmentTable;
	}

	public void setIsFragmentTable(Boolean isFragmentTable) {
		this.isFragmentTable = isFragmentTable;
	}
}
