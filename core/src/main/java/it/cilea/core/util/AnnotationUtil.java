package it.cilea.core.util;

import it.cilea.core.annotation.OrderByCustom;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationUtil {
	private static final Logger log = LoggerFactory.getLogger(AnnotationUtil.class);

	public static OrderByCustom getOrderByCustom(Class clazz, String property) {
		if (clazz == null || property == null) {
			log.debug("Null class o propertyOriginal");
			return null;
		}
		Field field;
		Method getter;
		if (property.startsWith("get")) {
			getter = findMethod(clazz, property);
			field = findField(clazz, convertGettterMethodToProperty(property));
		} else {
			getter = findMethod(clazz, convertPropertyToGetterMethod(property));
			field = findField(clazz, property);
		}
		if (field != null && field.getAnnotation(OrderByCustom.class) != null)
			return field.getAnnotation(OrderByCustom.class);
		if (getter != null && getter.getAnnotation(OrderByCustom.class) != null)
			return getter.getAnnotation(OrderByCustom.class);
		return null;
	}

	public static Field findField(Class clazz, String property) {
		if (clazz == null || property == null) {
			log.debug("Null class o property");
			return null;
		}
		while (!Object.class.equals(clazz)) {
			try {
				return clazz.getDeclaredField(property);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		log.debug("No such property (" + property + ") in class " + clazz);
		return null;
	}

	public static Method findMethod(Class clazz, String method) {
		if (clazz == null || method == null) {
			log.debug("Null class o method");
			return null;
		}
		while (!Object.class.equals(clazz)) {
			try {
				return clazz.getDeclaredMethod(method);
			} catch (NoSuchMethodException e) {
				clazz = clazz.getSuperclass();
			}
		}
		log.debug("No such method (" + method + ") in class " + clazz);
		return null;
	}

	public static String convertPropertyToGetterMethod(String property) {
		if (property == null) {
			throw new IllegalArgumentException("Property null");
		}
		return "get" + WordUtils.capitalize(property);
	}

	public static String convertGettterMethodToProperty(String getter) {
		if (getter == null) {
			throw new IllegalArgumentException("Getter null");
		}
		return WordUtils.uncapitalize(StringUtils.substring(getter, 3));
	}

}
