package it.cilea.core.view.model.validator;

import it.cilea.core.validator.FieldValidator;
import it.cilea.core.view.model.ViewBuilderValidator;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;
import org.springframework.context.ApplicationContext;

@Entity
@DiscriminatorValue("field")
public class ViewBuilderFieldValidator extends ViewBuilderValidator<FieldValidator> {

	@Column(name = "FIELD_NAME")
	private String fieldName;

	@Column(name = "VALIDATION_RULES")
	@Lob
	@Type(type = "org.hibernate.type.StringClobType")
	private String validationRules;

	@Override
	public void init(ApplicationContext applicationContext) throws Exception {
		super.init(applicationContext);
		validator = new FieldValidator(fieldName, validationRules);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getValidationRules() {
		return validationRules;
	}

	public void setValidationRules(String validationRules) {
		this.validationRules = validationRules;
	}

}
