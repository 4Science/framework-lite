package it.cilea.core.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NotNullOnlyJspValidator implements ConstraintValidator<NotNullOnlyJsp, String> {

	public void initialize(NotNullOnlyJsp parameters) {
	}

	public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
		if (object == null)
			return true;
		return object != null;
	}

}