package it.cilea.core.widget.validation;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Errors;

import it.cilea.core.validator.XmlValidator;
import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.service.WidgetService;

public class WidgetValidator implements XmlValidator {

	private WidgetService widgetService;

	public void initialise(String rule, String fieldName) throws Exception {
	}

	public void setWidgetService(WidgetService widgetService) {
		this.widgetService = widgetService;
	}

	public void validate(HttpServletRequest request, Object obj, Errors errors) throws Exception {
		Widget widget = (Widget) obj;

	}
}
