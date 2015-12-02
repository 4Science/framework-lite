package it.cilea.core.util;

import it.cilea.core.CoreConstant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.Converter;

public class DateConverter implements Converter {

	public Object convert(Class clazz, Object value) {

		if (value == null)
			throw new ConversionException("No value specified");

		if (value instanceof Date)
			return value;

		DateFormat dateFormat = new SimpleDateFormat(CoreConstant.DATE_PATTERN);
		Date date = null;

		try {
			date = dateFormat.parse(value.toString());
		} catch (ParseException pe) {
			throw new ConversionException("Could not perform conversion");
		}

		return date;

	}

}
