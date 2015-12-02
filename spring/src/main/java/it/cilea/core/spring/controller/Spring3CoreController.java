package it.cilea.core.spring.controller;

import java.beans.PropertyEditor;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomMapEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import it.cilea.core.dto.MultipleChoice;
import it.cilea.core.model.IdentifiableObject;
import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.Selectable;
import it.cilea.core.serializer.SelectableSerializer;
import it.cilea.core.service.JaxbService;
import it.cilea.core.spring.context.SecurityUserHolder;
import it.cilea.core.spring.factory.ControllerLogicFactory;
import it.cilea.core.spring.model.ControllerLogic;
import it.cilea.core.spring.propertyeditors.CustomCalendarEditor;
import it.cilea.core.spring.propertyeditors.CustomDateTimeEditor;
import it.cilea.core.spring.propertyeditors.CustomStringEditor;
import it.cilea.core.spring.util.MessageUtil;
import it.cilea.core.validator.FieldValidator;
import it.cilea.core.validator.XmlValidator;
import it.cilea.core.validator.XmlValidatorFactory;

public class Spring3CoreController {
	protected static Logger log = LoggerFactory.getLogger(Spring3CoreController.class);

	@Autowired
	protected ApplicationContext context;

	@Autowired
	protected JaxbService jaxbService;

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	@Autowired
	protected MessageUtil messageUtil;

	public void setMessageUtil(MessageUtil messageUtil) {
		this.messageUtil = messageUtil;
	}

	public void saveMessage(HttpServletRequest request, String msg) {
		MessageUtil.saveMessage(request, msg);
	}

	public void saveError(HttpServletRequest request, String msg) {
		MessageUtil.saveError(request, msg);
	}

	public UserDetails getUser() {
		return SecurityUserHolder.getUser();
	}

	public void genericReferenceData(Object command, HttpServletRequest request, List<MultipleChoice> list)
			throws Exception {
		if (list != null) {
			boolean usableMultipleChoice = true;
			for (MultipleChoice multipleChoiceObject : list) {
				List<Object> objectList = new ArrayList<Object>();
				if (multipleChoiceObject.getParameterList() != null)
					for (String parameterName : multipleChoiceObject.getParameterList()) {
						String parameterValue = request.getParameter(parameterName);
						if (parameterValue == null) {
							parameterValue = request.getAttribute(parameterName) != null
									? request.getAttribute(parameterName).toString() : null;
						}
						if (parameterValue == null)
							parameterValue = request.getSession().getAttribute(parameterName) != null
									? request.getSession().getAttribute(parameterName).toString() : null;
						if (parameterValue == null) {
							log.error("The parameter " + parameterName + " cannot be found in request or session");
							log.error("The multipleChoice named " + multipleChoiceObject.getAttributeName()
									+ " is skipped ");
							// This multiplechoice cannot be invoked and is
							// marked as unusable
							usableMultipleChoice = false;
							break;
						}
						objectList.add(parameterValue);
					}
				// If unusable skip this multipleChoice
				if (!usableMultipleChoice)
					continue;
				if (multipleChoiceObject.getNoParameterList() != null)
					for (Object string : multipleChoiceObject.getNoParameterList()) {
						objectList.add(string);
					}
				Object[] objectArray = new Object[objectList.size()];
				objectList.toArray(objectArray);
				Object ob = null;
				if ("self".equals(multipleChoiceObject.getServiceBeanId())) {
					ob = MethodUtils.invokeMethod(command, multipleChoiceObject.getPopulateMethod(), objectArray);
				} else if ("optionList".equals(multipleChoiceObject.getServiceBeanId())) {
					ob = multipleChoiceObject.getOptionList();
				} else {
					ob = MethodUtils.invokeMethod(context.getBean(multipleChoiceObject.getServiceBeanId()),
							multipleChoiceObject.getPopulateMethod(), objectArray);
				}
				if (multipleChoiceObject.getHttpScope() == null
						|| multipleChoiceObject.getHttpScope().equals("request"))
					request.setAttribute(multipleChoiceObject.getAttributeName(), ob);
				else if ("session".equals(multipleChoiceObject.getHttpScope())) {
					if (multipleChoiceObject.getOnFormSubmission())
						request.getSession().setAttribute(multipleChoiceObject.getAttributeName(), ob);
				}
			}
		}
		request.setAttribute("actionUrl", request.getServletPath());
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
		binder.registerCustomEditor(BigDecimal.class, new CustomNumberEditor(BigDecimal.class, nf, true));
		binder.registerCustomEditor(String.class, new CustomStringEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Boolean.class, new CustomBooleanEditor(true));
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		// this must be true to avoid problems on date with DST
		dateFormat.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateTimeEditor(dateFormat, true, 16));
		binder.registerCustomEditor(Calendar.class, new CustomCalendarEditor());
		binder.registerCustomEditor(Map.class, new CustomMapEditor(LinkedHashMap.class));
		binder.registerCustomEditor(Map.class, new CustomMapEditor(HashMap.class));
		try {

			Iterator it = ((List) context.getBean("customEditorList")).iterator();
			while (it.hasNext()) {
				String valore = (String) it.next();
				String[] pieces = valore.split("\\|");
				if (pieces.length != 2)
					throw new Exception("Numero di parametri errato");
				binder.registerCustomEditor(Class.forName(pieces[0]),
						(PropertyEditor) Class.forName(pieces[1]).newInstance());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected boolean isFormSubmission(HttpServletRequest request) {
		return "POST".equals(request.getMethod());
	}

	protected void executeXmlValidation(HttpServletRequest request, Object command, Errors errors,
			Collection validationList) throws Exception {
		if (validationList == null)
			return;
		Iterator it = validationList.iterator();

		while (it.hasNext()) {
			Object validatorObject = it.next();
			if (validatorObject instanceof XmlValidator) {
				XmlValidator xmlValidator = (XmlValidator) validatorObject;
				xmlValidator.validate(request, command, errors);
			} else if (validatorObject instanceof org.springframework.validation.Validator) {
				org.springframework.validation.Validator validator = (org.springframework.validation.Validator) validatorObject;
				validator.validate(command, errors);
			} else {
				FieldValidator fieldValidator = (FieldValidator) validatorObject;
				String fieldName = fieldValidator.getFieldName();
				List<String> validationRuleList = fieldValidator.getValidationRuleList();
				for (String rule : validationRuleList) {
					XmlValidator validator = XmlValidatorFactory.getValidator(rule, fieldName);
					validator.validate(request, command, errors);
				}
			}
		}
	}

	@Autowired
	private ControllerLogicFactory controllerLogicFactory;

	protected ControllerLogic getControllerLogic(String url) {
		return controllerLogicFactory.getControllerLogic(url);
	}

	protected Object formBacking(Class clazz, Object service, HttpServletRequest request) throws Exception {
		String parameterId = request.getParameter(getClassId(clazz));
		if (StringUtils.isNotBlank(parameterId))
			return MethodUtils.invokeExactMethod(service, getClassGetter(clazz),
					new Object[] { Integer.valueOf(parameterId) });
		return clazz.newInstance();
	}

	protected Object formBacking(Class clazz, Object service, HttpServletRequest request, String paramName,
			String getterMethod) throws Exception {
		String parameterId = request.getParameter(paramName);
		if (StringUtils.isNotBlank(parameterId))
			return MethodUtils.invokeExactMethod(service, getterMethod, new Object[] { Integer.valueOf(parameterId) });
		return clazz.newInstance();
	}

	@SuppressWarnings("unchecked")
	public void referenceData(Object command, HttpServletRequest request) {
		if (StringUtils.isNotBlank(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName())) {
			List<MultipleChoice> list = (List<MultipleChoice>) context
					.getBean(getControllerLogic(request.getServletPath()).getMultipleChoiceListBeanName());
			try {
				genericReferenceData(command, request, list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("actionUrl", request.getServletPath());
	}

	protected ModelAndView processGet(Object command, HttpServletRequest request) {
		referenceData(command, request);
		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewName(), "command", command);
	}

	protected ModelAndView processPostCheckCancelAndValidation(Object command, BindingResult result,
			HttpServletRequest request) throws Exception {
		if (request.getParameter("cancel") != null)
			return new ModelAndView(getControllerLogic(request.getServletPath()).getViewUndo());
		executeXmlValidation(request, command, (Errors) result,
				getControllerLogic(request.getServletPath()).getValidatorList());
		if (result.hasErrors())
			return processGet(command, request);

		return null;
	}

	protected void processJsonEdit(Object command, BindingResult result, Object service, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		executeXmlValidation(request, command, (Errors) result,
				getControllerLogic(request.getServletPath()).getValidatorList());

		SelectBaseString select = new SelectBaseString();
		if (result.hasErrors()) {
			select.setValue("error");
			String message = "";
			for (ObjectError error : result.getAllErrors()) {
				message += messageUtil.findMessage(error.getCode(), error.getArguments()) + ",";
			}
			select.setDisplayValue(StringUtils.removeEnd(message, ","));
		} else {
			MethodUtils.invokeExactMethod(service, "saveOrUpdate", new Object[] { command });
			select.setValue("ok");
			select.setDisplayValue(messageUtil.findMessage("action.updated"));
		}
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Selectable.class, new SelectableSerializer());
		Gson gson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();
		Type sessionType = new TypeToken<Selectable>() {
		}.getType();
		String json = gson.toJson(select, sessionType);
		response.getWriter().println(json);
	}

	protected ModelAndView processPostSuccess(IdentifiableObject command, Object service, HttpServletRequest request)
			throws Exception {
		boolean firstInsert = command.getId() == null;
		MethodUtils.invokeExactMethod(service, "saveOrUpdate", new Object[] { command });
		saveMessage(request, messageUtil.findMessage("action." + (firstInsert ? "created" : "updated")));
		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewSuccess(),
				getClassId(command.getClass()), command.getId());
	}

	protected ModelAndView processPostSuccess(IdentifiableObject command, Object service, HttpServletRequest request,
			String paramName) throws Exception {
		boolean firstInsert = command.getId() == null;
		MethodUtils.invokeExactMethod(service, "saveOrUpdate", new Object[] { command });
		saveMessage(request, messageUtil.findMessage("action." + (firstInsert ? "created" : "updated")));
		return new ModelAndView(getControllerLogic(request.getServletPath()).getViewSuccess(), paramName,
				command.getId());
	}

	protected String getClassId(Class clazz) {
		return WordUtils.uncapitalize(StringUtils.substringAfterLast(clazz.toString(), ".")) + "Id";
	}

	protected String getClassGetter(Class clazz) {
		return "get" + StringUtils.substringAfterLast(clazz.toString(), ".");
	}

	public void setJaxbService(JaxbService jaxbService) {
		this.jaxbService = jaxbService;
	}

	public void setControllerLogicFactory(ControllerLogicFactory controllerLogicFactory) {
		this.controllerLogicFactory = controllerLogicFactory;
	}
}
