package it.cilea.core.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class GenericService {
	private static final Logger log = LoggerFactory.getLogger(GenericService.class);

	private Map genericServiceMap = new HashMap();

	private Collection genericServiceSet = null;

	private String defaultServiceKey = null;

	private Object uniqueService = null;

	public Object getService(String serviceName) {
		return genericServiceMap.get(serviceName);
	}

	public Object getDefaultService(String serviceName) {
		if (defaultServiceKey != null)
			return genericServiceMap.get(defaultServiceKey);
		else
			return null;
	}

	public Map getApplicationServiceMap() {
		return genericServiceMap;
	}

	public void setApplicationServiceMap(Map genericServiceMap) {
		this.genericServiceMap = genericServiceMap;
		genericServiceSet = genericServiceMap.values();
		if (genericServiceSet.size() == 1) {
			Iterator it = genericServiceSet.iterator();
			while (it.hasNext()) {
				uniqueService = it.next();
				break;
			}
		}
	}

	public Object invokeMethod(String methodName, Object[] params) throws Exception {
		Object service = null;
		Object returnValue = null;
		if (uniqueService != null) {
			service = uniqueService;
			try {			
				returnValue = MethodUtils.invokeMethod(service, methodName, params);
			}  catch (Exception e) {
				log.info("Fallita invocazione su " + service.getClass() + " Messaggio: " + e.getMessage());
			}
		} else {
			Iterator it = genericServiceSet.iterator();
			while (it.hasNext()) {
				service = it.next();
				try {
					returnValue = MethodUtils.invokeMethod(service, methodName, params);
					log.info("Successo invocazione su " + service.getClass());
					break;
				} catch (NoSuchMethodException e) {
					log.info("Fallita invocazione su " + service.getClass() + " Messaggio: " + e.getMessage());
				} catch (InvocationTargetException e) {
					log.info("Fallita invocazione su " + service.getClass() + " Messaggio: " + e.getMessage());
				} catch (IllegalAccessException e) {
					log.info("Fallita invocazione su " + service.getClass() + " Messaggio: " + e.getMessage());
				}
			}
		}
		if (returnValue == null){
			String message="Metodo " + methodName + "(";
			
			int counter=0;
			for (Object obj : params){
				if (counter!=0)
					message+=", ";
				message+=obj.getClass();
				counter++;
			}
			message+=") non trovato";
			throw new Exception(message);
		}
		else
			return returnValue;

	}

	public void setDefaultServiceKey(String defaultServiceKey) {
		this.defaultServiceKey = defaultServiceKey;
	}

}
