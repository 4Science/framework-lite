package it.cilea.core.view.model.validator;

import it.cilea.core.validator.XmlValidator;
import it.cilea.core.view.model.ViewBuilderValidator;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;

@Entity
@DiscriminatorValue("xml")
public class ViewBuilderXmlValidator extends ViewBuilderValidator<XmlValidator> {

	@Column(name = "CLASS_NAME")
	private String className;

	@Override
	public void init(ApplicationContext applicationContext) throws Exception {
		super.init(applicationContext);
		validator = (XmlValidator) applicationContext.getAutowireCapableBeanFactory().autowire(
				Class.forName(className), AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
