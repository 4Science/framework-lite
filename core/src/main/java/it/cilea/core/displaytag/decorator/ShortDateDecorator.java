package it.cilea.core.displaytag.decorator;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.time.FastDateFormat;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public class ShortDateDecorator implements DisplaytagColumnDecorator {

	private FastDateFormat dateFormat = FastDateFormat.getInstance("dd/MM/yyyy");

	public Object decorate(Object columnValue, PageContext pageContext,
			MediaTypeEnum media) throws DecoratorException {
		if (columnValue instanceof GregorianCalendar) {
			GregorianCalendar gc = (GregorianCalendar) columnValue;
			return gc != null ? dateFormat.format(gc.getTime()) : null;
		} else {
			Date date = (Date) columnValue;
			return date != null ? dateFormat.format(date) : null;
		}
	}

}
