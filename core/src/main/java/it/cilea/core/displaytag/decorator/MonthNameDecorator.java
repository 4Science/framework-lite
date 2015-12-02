package it.cilea.core.displaytag.decorator;

import javax.servlet.jsp.PageContext;
import java.text.DateFormatSymbols;
import java.util.Locale;


import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public class MonthNameDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext,
			MediaTypeEnum media) throws DecoratorException {
		Integer month = (Integer) columnValue;
		if (month>0 && month<13)
			return new DateFormatSymbols(Locale.ITALY).getMonths()[month-1];
		else
			return columnValue;
	}
}
