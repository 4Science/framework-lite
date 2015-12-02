package it.cilea.core.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = NotNullOnlyJspValidator.class)
@Documented
public @interface NotNullOnlyJsp {
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String message() default "{validator.notNull}";
}
