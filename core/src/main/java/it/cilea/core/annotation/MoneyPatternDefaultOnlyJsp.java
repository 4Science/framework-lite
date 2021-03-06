package it.cilea.core.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;


@Documented
@Target( { METHOD, FIELD })
@Retention(RUNTIME)
public @interface MoneyPatternDefaultOnlyJsp {

}
