package it.cilea.core.displaytag.decorator;

import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public class EuroDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext,
			MediaTypeEnum media) throws DecoratorException {
		if (columnValue == null)
			return null;
		//Double number = (Double) columnValue;
		Locale locale = Locale.ITALIAN;
		return columnValue != null ? NumberFormat.getNumberInstance(locale).format(
				columnValue) : null;
	}

}
