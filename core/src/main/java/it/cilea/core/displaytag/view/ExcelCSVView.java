package it.cilea.core.displaytag.view;


import org.apache.commons.lang.StringUtils;
import org.displaytag.export.CsvView;

public class ExcelCSVView extends CsvView {	

	public String getRowEnd() {
		return "\r\n";	//Windows end of line
	}

	public String getCellEnd() {
		return ";";	//French Excel deafult separator
	}

	public String getMimeType() {
		return "text/csv; charset=cp1252"; //uses the Windows Latin-1 supersetencoding
	}	

	protected String escapeColumnValue(Object value) {		 
		String stringValue = StringUtils.trim(value.toString());
		if (!StringUtils.containsNone(stringValue, new char[]{'\n', ';' })) {
			//double '"' as escape sequence for included quote symbols
			return "\"" + StringUtils.replace(stringValue, "\"", "\"\"") + "\"";
		}
		return stringValue;
	}
}

