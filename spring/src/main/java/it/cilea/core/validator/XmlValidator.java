package it.cilea.core.validator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.Errors;

public interface XmlValidator {
	public void validate(HttpServletRequest request, Object command, Errors errors) throws Exception;

	public void initialise(String rule, String fieldName) throws Exception;
}
