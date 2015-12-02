package it.cilea.core.search.validation;

import it.cilea.core.search.model.SearchBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SearchBuilderValidator implements Validator {

	private static Logger log = LoggerFactory.getLogger(SearchBuilderValidator.class);

	@Override
	public boolean supports(Class clazz) {
		return SearchBuilder.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object object, Errors errors) {
		SearchBuilder searchBuilder = (SearchBuilder) object;
	}
}
