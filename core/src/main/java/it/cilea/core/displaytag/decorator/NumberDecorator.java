package it.cilea.core.displaytag.decorator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.math.NumberUtils;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public class NumberDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		if (columnValue == null)
			return null;
		if (!NumberUtils.isNumber("" + columnValue))
			throw new DecoratorException(NumberDecorator.class, "error formatting value: " + columnValue);
		if (!(columnValue instanceof Number))
			columnValue = new BigDecimal("" + columnValue);
		Locale locale = Locale.ITALIAN;
		return NumberFormat.getNumberInstance(locale).format(columnValue);
	}
}
