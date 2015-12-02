package it.cilea.core.spring.controller;

import it.cilea.core.spring.util.PropertyUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Controller
public class DifferencesController extends Spring3CoreController {

	@Autowired
	private ApplicationContext applicationContext;

	@RequestMapping("/differences.json")
	public void handleJsonDifferences(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String objId1 = request.getParameter("objId1");
		String objId2 = request.getParameter("objId2");
		String className = request.getParameter("className");
		String serviceBeanName = request.getParameter("serviceBeanName");
		String[] fieldNameList = request.getParameterValues("fieldName");
		Object service = applicationContext.getBean(serviceBeanName);
		Object obj1 = MethodUtils.invokeMethod(service, "get" + className, new Object[] { new Integer(objId1) });
		Object obj2 = MethodUtils.invokeMethod(service, "get" + className, new Object[] { new Integer(objId2) });
		JsonObject json = new JsonObject();
		getDifferences(obj1, obj2, fieldNameList, json);

		String[] elementNameList = request.getParameterValues("elementName");
		getDifferencesElement(obj1, obj2, elementNameList, json);

		try {
			response.getWriter().println(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getDifferences(Object obj1, Object obj2, String[] fieldNameList, JsonObject json) throws Exception {
		if (ArrayUtils.isNotEmpty(fieldNameList)) {
			JsonArray itemList = new JsonArray();
			for (String fieldName : fieldNameList) {
				JsonObject item = new JsonObject();
				boolean equalityFlag = false;
				Object innerObj1 = PropertyUtil.getPropertyValue(obj1, fieldName);
				Object innerObj2 = PropertyUtil.getPropertyValue(obj2, fieldName);

				if (innerObj1 == null && innerObj2 == null) {
					equalityFlag = true;
				} else if (innerObj1 != null && innerObj1.equals(innerObj2)) {
					equalityFlag = true;
				}

				item.addProperty("fieldName", fieldName.replace("[", "__").replace("]", "__"));
				item.addProperty("equalityFlag", equalityFlag);
				itemList.add(item);

			}
			json.add("items", itemList);
		}
	}

	private void getDifferencesElement(Object obj1, Object obj2, String[] elementNameList, JsonObject json)
			throws Exception {
		if (ArrayUtils.isNotEmpty(elementNameList)) {
			JsonArray itemList = new JsonArray();
			for (String fieldName : elementNameList) {
				JsonObject item = new JsonObject();
				boolean equalityFlag = true;
				;
				String elementName = StringUtils.substringBefore(fieldName, "___");
				fieldName = StringUtils.substringAfter(fieldName, "___");
				String elementNameSpan = StringUtils.substringAfterLast(fieldName, "___");
				fieldName = StringUtils.substringBeforeLast(fieldName, "___");

				Object innerObj1 = PropertyUtil.getPropertyValue(obj1, elementName);
				Object innerObj2 = PropertyUtil.getPropertyValue(obj2, elementName);
				if (innerObj1 == null && innerObj2 == null) {
					equalityFlag = true;
				}
				else if ((innerObj1 == null && innerObj2 != null) || (innerObj2 == null && innerObj1 != null)) {
					equalityFlag = false;
				} else {
					String[] fieldNameArray = StringUtils.split(fieldName, "___");
					Set set1 = (Set) innerObj1;
					Set set2 = (Set) innerObj2;
					Iterator iterator1 = set1.iterator();

					while (iterator1.hasNext()) {
						Object elementObject1 = iterator1.next();
						Iterator iterator2 = set2.iterator();
						Boolean foundAtLeastOne = false;
						while (iterator2.hasNext()) {
							Object elementObject2 = iterator2.next();
							if (checkDifferences(elementObject1, elementObject2, fieldNameArray)) {
								foundAtLeastOne = true;
								break;
							}
						}

						if (foundAtLeastOne)
							continue;
						else {
							equalityFlag = false;
							break;
						}
					}

					Iterator iterator2 = set2.iterator();

					while (iterator2.hasNext()) {
						Object elementObject1 = iterator2.next();
						Iterator iterator3 = set1.iterator();
						Boolean foundAtLeastOne = false;
						while (iterator3.hasNext()) {
							Object elementObject2 = iterator3.next();
							if (checkDifferences(elementObject1, elementObject2, fieldNameArray)) {
								foundAtLeastOne = true;
								break;
							}
						}

						if (foundAtLeastOne)
							continue;
						else {
							equalityFlag = false;
							break;
						}
					}

				}

				item.addProperty("elementName", elementNameSpan);
				item.addProperty("equalityFlag", equalityFlag);
				itemList.add(item);

			}
			json.add("elementItems", itemList);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private Boolean checkDifferences(Object obj1, Object obj2, String[] fieldNameList) throws Exception {
		if (ArrayUtils.isNotEmpty(fieldNameList)) {
			for (String fieldName : fieldNameList) {
				Object innerObj1 = PropertyUtil.getPropertyValue(obj1, fieldName);
				Object innerObj2 = PropertyUtil.getPropertyValue(obj2, fieldName);
				if (innerObj1 == null && innerObj2 == null) {
					continue;
				} else if (innerObj1 != null && innerObj1.equals(innerObj2)) {
					continue;
				}
				return false;
			}
		}
		return true;
	}
}
