package it.cilea.core.view.model.validator;

import it.cilea.core.view.model.ViewBuilderValidator;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.context.ApplicationContext;

@Entity
@DiscriminatorValue("bean")
public class ViewBuilderBeanValidator extends ViewBuilderValidator<Object> {

	@Column(name = "BEAN_NAME")
	private String beanName;

	@Override
	public void init(ApplicationContext applicationContext) throws Exception {
		super.init(applicationContext);
		validator = applicationContext.getBean(beanName);
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

}
