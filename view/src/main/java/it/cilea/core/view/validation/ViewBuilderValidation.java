package it.cilea.core.view.validation;

import it.cilea.core.view.model.ViewBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ViewBuilderValidation implements Validator {

	private static Logger log = LoggerFactory.getLogger(ViewBuilderValidation.class);

	@Override
	public boolean supports(Class clazz) {
		return ViewBuilder.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		ViewBuilder viewBuilder = (ViewBuilder) object;
	}
}
