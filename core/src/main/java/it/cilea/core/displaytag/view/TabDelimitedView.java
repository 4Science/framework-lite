package it.cilea.core.displaytag.view;

import org.apache.commons.lang.StringUtils;
import org.displaytag.export.BaseExportView;
import org.displaytag.model.TableModel;

public class TabDelimitedView extends BaseExportView {

	public TabDelimitedView() {
	}

	public void setParameters(TableModel tableModel, boolean exportFullList, boolean includeHeader,
			boolean decorateValues) {
		super.setParameters(tableModel, exportFullList, includeHeader, decorateValues);
	}

	protected String getRowEnd() {
		return "\n";
	}

	protected String getCellEnd() {
		return "	";
	}

	protected boolean getAlwaysAppendCellEnd() {
		return false;
	}

	protected boolean getAlwaysAppendRowEnd() {
		return true;
	}

	public String getMimeType() {
		return "text/csv; charset=cp1252"; 
	}

	protected String escapeColumnValue(Object value) {
		String stringValue = StringUtils.trim(value.toString());

		stringValue = StringUtils.replace(stringValue, "\r", "");
		stringValue = StringUtils.replace(stringValue, "\n", "");
		stringValue = StringUtils.replace(stringValue, "\f", "");
		stringValue = StringUtils.replace(stringValue, "\r\n", "");
		return stringValue;
	}
}
