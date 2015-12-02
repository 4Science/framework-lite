package it.cilea.core.i18n.validator;

import it.cilea.core.i18n.model.I18nImport;
import it.cilea.core.spring.util.MessageUtil;
import it.cilea.core.validator.XmlValidator;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

public class I18nImportValidator implements XmlValidator {

	private static final Logger log = LoggerFactory.getLogger(I18nImportValidator.class.getName());

	
	@Autowired
	protected MessageUtil messageUtil;

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	

	public void initialise(String rule, String fieldName) throws Exception {
	}

	public void validate(HttpServletRequest request, Object object, Errors errors) throws Exception {
		if (errors.hasErrors())
			return;
		
		I18nImport i18nImport = (I18nImport) object;
		
		if (i18nImport.getMultipartFile().getSize() == 0) {
			errors.reject("i18n.inputFileNotDefined");
		} 
	}
}