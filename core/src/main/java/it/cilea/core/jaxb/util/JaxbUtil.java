package it.cilea.core.jaxb.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class JaxbUtil {
	
	public static Class getJaxbBindingClass(Object object){
		Class clazz = null;
		if (object instanceof List){
			Object realObject=((List) object).get(0);
			clazz=realObject.getClass();
		} else {
			clazz=object.getClass();
		}
		return clazz;
	}	
	
	public static String getJaxbBindingFile(Class clazz, boolean jaxbBindingAsSelectable){
		String fullyQualifiedClassName=clazz.getCanonicalName();
		String packageName=StringUtils.substringBeforeLast(fullyQualifiedClassName, ".");
		String className=StringUtils.substringAfterLast(fullyQualifiedClassName, ".");
		packageName+=".jaxb.";
		String bindingFileNamePart=null;
		if (jaxbBindingAsSelectable){
			bindingFileNamePart="selectable";
		} else {
			bindingFileNamePart=className.substring(0, 1).toLowerCase()+className.substring(1);
		}
		return packageName.replaceAll("\\.", "/") + bindingFileNamePart+"-binding.xml";
	}
}
