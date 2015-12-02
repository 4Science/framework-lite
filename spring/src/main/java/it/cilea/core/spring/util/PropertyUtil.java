package it.cilea.core.spring.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class PropertyUtil {

	public static Object getPropertyValue(Object obj, String property) throws Exception {		
		Object result = null;
		// se trovo un [ nel nome della proprietà allora ho di fronte una
		// mappa...
		if (property.contains("[")) {
			property = property.replace("'", "");
			String mapName = StringUtils.substringBefore(property, "[");
			String mapKey = StringUtils.substringBefore(StringUtils.substringAfter(property, "["), "]");
			Map map = (Map) PropertyUtils.getProperty(obj, mapName);
			result = map.get(mapKey);
		}
		// ...altrimenti una proprietà semplice
		else {
			result = PropertyUtils.getProperty(obj, property);
		}
		return result;
	}
	
	public static Object getPropertyPathValue(Object obj, String[] property) throws Exception {
		Object result = obj;
		int i=0;
		int pathLength = property.length;
		while(result!=null && i < pathLength) {
			result = getPropertyValue_mapsupport(result, property[i]);
			i = i + 1;
		}
		return result;
	}
	
	public static void setPropertyPathValue(Object obj, String[] propertyPath, Object value, boolean create) throws Exception {
		if(propertyPath==null || propertyPath.length<1) {
			throw new IllegalArgumentException("property path can't be empty");
		}
			
		Object objNow = obj;
		int pathLastIndex = propertyPath.length -1 ;
		for(int i=0; i< pathLastIndex; i++) {
			Object nextVal = getPropertyValue_mapsupport(objNow, propertyPath[i]);
			if(nextVal==null && create) {
				nextVal = new HashMap<>();
				setPropertyValue(objNow, propertyPath[i], nextVal);
			}
		}
		
		setPropertyValue(objNow, propertyPath[pathLastIndex], value);
	}

	public static void setPropertyValue(Object obj, String property, Object value) throws Exception {
		if(obj instanceof Map) {
			((Map)obj).put(property, value);
		} else {
			PropertyUtils.setProperty(obj, property, value);
		}
	}
	
	
	private static Object getPropertyValue_mapsupport(Object obj, String property) throws Exception {
		if(obj instanceof Map) {
			return ((Map)obj).get(property);
		} else {
			return getPropertyValue(obj, property);
		}
	}
}
