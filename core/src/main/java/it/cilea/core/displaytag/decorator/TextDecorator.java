package it.cilea.core.displaytag.decorator;

import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

public class TextDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object columnValue, PageContext pageContext, MediaTypeEnum media) throws DecoratorException {
		String testo = (String) columnValue;
		if (!StringUtils.isEmpty(testo))
			testo = StringEscapeUtils.escapeHtml(testo.trim());
		return testo;
	}

}
