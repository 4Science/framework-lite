package it.cilea.core.search.util;

import it.cilea.core.search.SearchConstant;
import it.cilea.core.search.model.SearchBuilder;
import it.cilea.core.search.service.SearchService;
import it.cilea.core.widget.model.ParameterDTO;
import it.cilea.core.widget.model.ParameterMultipleValuesDto;
import it.cilea.core.widget.model.ParameterValueDTO;
import it.cilea.core.widget.model.Widget;
import it.cilea.core.widget.model.impl.core.HiddenWidget;
import it.cilea.core.widget.service.WidgetService;
import it.cilea.core.widget.util.WidgetTagUtil;
import it.cilea.core.widget.util.WidgetUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchUtil {

	private static final Logger log = LoggerFactory.getLogger(SearchUtil.class);

	// TODO: ELIMINARE L'UTULIZZO DEL SESSION REPOSITORY
	public static Map<String, String[]> getRequestAttributeMap(HttpServletRequest request, List<Widget> widgetList) {

		Map<String, String[]> requestParameterMap = new HashMap<String, String[]>();

		if (widgetList != null) {
			for (Widget widget : widgetList) {
				String pageAttributeName = widget.getPageAttributeName();
				Object objectParameterValues = request.getAttribute(pageAttributeName);
				if (objectParameterValues == null) {
					String newPageAttributeName;

					newPageAttributeName = WidgetUtil.getLowerBoundRequestAttributeName(pageAttributeName);
					objectParameterValues = request.getAttribute(newPageAttributeName);
					popolaMappaConValoriDaRequest(newPageAttributeName, objectParameterValues, requestParameterMap);

					newPageAttributeName = WidgetUtil.getUpperBoundRequestAttributeName(pageAttributeName);
					objectParameterValues = request.getAttribute(newPageAttributeName);
					popolaMappaConValoriDaRequest(newPageAttributeName, objectParameterValues, requestParameterMap);

				} else {
					popolaMappaConValoriDaRequest(pageAttributeName, objectParameterValues, requestParameterMap);
					// popolo anche l'eventuale checkbox per l'utilizzo della
					// logica fuzzy
					String fuzzyCheckbox = WidgetUtil.getFuzzinessCheckboxRequestAttributeName((pageAttributeName));
					popolaMappaConValoriDaRequest(fuzzyCheckbox, request.getAttribute(fuzzyCheckbox),
							requestParameterMap);
				}
			}
		}
		// else {
		// SessionRepository sessionRepository = null;
		// //(SessionRepository)
		// request.getAttribute(SessionFilter.REPOSITORY_NAME);
		// List<String> searchParameterList =
		// sessionRepository.getSearchParameterList();
		//
		// for (String searchParameter : searchParameterList) {
		// Object objectParameterValues = request.getAttribute(searchParameter);
		// if (objectParameterValues == null){
		// String newPageAttributeName;
		//
		// newPageAttributeName=Utilities.getLowerBoundRequestAttributeName(searchParameter);
		// objectParameterValues = request.getAttribute(newPageAttributeName);
		// popolaMappaConValoriDaRequest(newPageAttributeName,
		// objectParameterValues,requestParameterMap);
		//
		// newPageAttributeName=Utilities.getUpperBoundRequestAttributeName(searchParameter);
		// objectParameterValues = request.getAttribute(newPageAttributeName);
		// popolaMappaConValoriDaRequest(newPageAttributeName,
		// objectParameterValues,requestParameterMap);
		//
		// } else {
		// popolaMappaConValoriDaRequest(searchParameter,
		// objectParameterValues,requestParameterMap);
		// String
		// fuzzyCheckbox=Utilities.getFuzzinessCheckboxRequestAttributeName((searchParameter));
		// popolaMappaConValoriDaRequest(fuzzyCheckbox,
		// request.getAttribute(fuzzyCheckbox),requestParameterMap);
		// }
		// }
		// }
		return requestParameterMap;
	}

	// TODO: ELIMINARE L'UTULIZZO DEL SESSION REPOSITORY
	// TODO: METTERE IN UNA CONSTANT progressiveRelaxation
	public static boolean isProgressiveRelaxationSearch(HttpServletRequest request, List<Widget> widgetList) {
		Map<String, String[]> requestParameterMap = new HashMap<String, String[]>();

		if (widgetList != null) {
			for (Widget widget : widgetList) {
				if ("progressiveRelaxation".equals(widget.getStringMatchType())) {
					String pageAttributeName = widget.getPageAttributeName();
					Object attributeValue = request.getAttribute(pageAttributeName);
					if (attributeValue != null && (attributeValue instanceof String && !"".equals(attributeValue)))
						return true;
				}
			}
		}
		return false;
	}

	public static void popolaMappaConValoriDaRequest(String attributeName, Object attributeValue,
			Map<String, String[]> requestParameterMap) {
		String[] parameterValues = null;
		if (attributeValue == null)
			return;
		if (attributeValue instanceof String) {
			parameterValues = new String[1];
			parameterValues[0] = (String) attributeValue;
		} else if (attributeValue instanceof String[]) {
			parameterValues = (String[]) attributeValue;
		}
		requestParameterMap.put(attributeName, parameterValues);
	}

	public static boolean isExport(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<String> keySet = parameterMap.keySet();

		for (String key : keySet) {
			if (key.startsWith("d-") && key.endsWith("-e")) {
				return true;
			}
		}

		return false;
	}

	public static Class getFieldType(Class modelClass, String parameterName) throws Exception {

		Class fieldType = null;

		if (modelClass != null) {

			if (parameterName.indexOf(".") != -1) {
				Class rootFieldType = modelClass;
				String parameterNameBit = parameterName;

				while (parameterNameBit.indexOf(".") != -1) {
					// parameterName in questo caso indica il nome di un
					// attributo di un'altro oggetto
					String rootParameterName = parameterNameBit.substring(0, parameterNameBit.indexOf("."));

					Field rootField = WidgetTagUtil.findField(rootFieldType, rootParameterName);

					// Field
					// rootField=rootFieldType.getDeclaredField(rootParameterName
					// );
					rootFieldType = rootField.getType();
					// By Roberto!
					if (rootFieldType.equals(java.util.List.class) || rootFieldType.equals(java.util.Set.class)) {
						// sono in un list devo recuperare il type generics del
						// field.
						String nomeClasse = rootField
								.getGenericType()
								.toString()
								.substring(rootField.getGenericType().toString().indexOf("<") + 1,
										rootField.getGenericType().toString().indexOf(">"));
						rootFieldType = Class.forName(nomeClasse);
					}
					parameterNameBit = parameterNameBit.substring(parameterNameBit.indexOf(".") + 1);
				}
				String rootParameterName = parameterNameBit;
				Field rootField = WidgetTagUtil.findField(rootFieldType, rootParameterName);
				// rootFieldType.getDeclaredField(rootParameterName);
				// Field
				// rootField=rootFieldType.getDeclaredField(rootParameterName);
				rootFieldType = rootField.getType();
				parameterNameBit = parameterNameBit.substring(parameterNameBit.indexOf(".") + 1);

				fieldType = rootFieldType;
			} else {
				Field field = WidgetTagUtil.findField(modelClass, parameterName);
				if (field != null)
					fieldType = field.getType();
			}
		}

		return fieldType;

	}

	public static ParameterMultipleValuesDto getParameterMultipleValuesDto(Map<String, String[]> requestParameterMap,
			String pageAttributeName) {

		String[] parameterValues = requestParameterMap.get(WidgetUtil.getRequestAttributeName(pageAttributeName));

		boolean rangeSearch = false;
		boolean isLowerBound = false;

		if (parameterValues == null) {
			// se nullo è probabile presenza di ricerca intervallare per
			// l'attributo in questione
			String lowerBound = null;
			String upperBound = null;

			String[] transf = requestParameterMap.get(WidgetUtil.getLowerBoundRequestAttributeName(pageAttributeName));
			if (transf != null && transf.length != 0)
				lowerBound = transf[0];

			transf = requestParameterMap.get(WidgetUtil.getUpperBoundRequestAttributeName(pageAttributeName));
			if (transf != null && transf.length != 0)
				upperBound = transf[0];

			if (StringUtils.isNotEmpty(lowerBound) || StringUtils.isNotEmpty(upperBound)) {
				rangeSearch = true;
				if (StringUtils.isNotEmpty(lowerBound) && StringUtils.isNotEmpty(upperBound)) {
					parameterValues = new String[2];
					parameterValues[0] = lowerBound;
					parameterValues[1] = upperBound;
				} else {
					parameterValues = new String[1];
					if (StringUtils.isNotEmpty(lowerBound)) {
						parameterValues[0] = lowerBound;
						isLowerBound = true;
					} else {
						parameterValues[0] = upperBound;
						isLowerBound = false;
					}
				}
			}
		} else {
			// se ho una sola stringa e questa contiene virgole...
			if (parameterValues.length == 1 && parameterValues[0].contains(","))
				parameterValues = parameterValues[0].split(",");
		}

		String[] checkboxFuzziness = (String[]) requestParameterMap.get(WidgetUtil
				.getFuzzinessCheckboxRequestAttributeName(pageAttributeName));

		return new ParameterMultipleValuesDto(parameterValues, rangeSearch, isLowerBound, ((checkboxFuzziness != null
				&& checkboxFuzziness.length == 1 && checkboxFuzziness[0].equals("1"))) ? true : false);
	}

	public static String getSign(ParameterMultipleValuesDto parameterMultipleValuesDto, int nIteration) {
		String sign = "=";

		if (parameterMultipleValuesDto.isRangeSearch()) {
			// ricerca intervallare
			if (parameterMultipleValuesDto.getParameterValues().length == 2) {
				// se l'utente ha inserito entrambi gli estremi...
				if (nIteration == 0) {
					// se è l'estremo inferiore
					sign = ">=";
				} else {
					// se è l'estremo superiore
					sign = "<=";
				}
			} else {
				// utente ha inserito un solo estremo...
				if (parameterMultipleValuesDto.isLowerBound()) {
					// se ha inserito solo l'estremo inferiore
					sign = ">=";
				} else {
					// se ha inserito solo l'estremo superiore
					sign = "<=";
				}
			}
		}

		return sign;
	}

	public static void createDbSearchParameter(Class modelClass, Map<String, String[]> requestParameterMap,
			Map<String, Object> model, List<String> errorList,
			Map<ParameterDTO, List<ParameterValueDTO>> dbSearchParameter, List<Widget> widgetList) throws Exception {

		if (widgetList != null) {
			Iterator<Widget> it = widgetList.iterator();

			while (it.hasNext()) {

				Widget widget = it.next();
				List<ParameterValueDTO> parameterList = new ArrayList<ParameterValueDTO>();

				// if (modelClass == null)
				// modelClass = control.getModelClass();

				Class objectClass = null;
				String parameterName = null;

				// if (control.getCollectionElementClass() == null ||
				// control.getCollectionElementAttributeName() == null) {
				// objectClass = modelClass;
				// parameterName = control.getModelAttributeName();
				// } else {
				// objectClass = control.getCollectionElementClass();
				// parameterName = control.getCollectionElementAttributeName();
				// }

				Class fieldType = SearchUtil.getFieldType(objectClass, parameterName);

				// carico i valori associati all'attributo
				ParameterMultipleValuesDto parameterMultipleValuesDto = SearchUtil.getParameterMultipleValuesDto(
						requestParameterMap, widget.getPageAttributeName());

				ParameterDTO parameterDto = new ParameterDTO();
				parameterDto.setName(widget.getModelAttributeName());
				// parameterDto.setRangeSearch(control.getRangeSearch());
				// parameterDto.setJoinAlias(control.getJoinAlias());
				// parameterDto.setJoinWithClause(control.getJoinWithClause());
				parameterDto.setStringMatchType(widget.getStringMatchType());
				// parameterDto.setMultipleValuesDisjuncted(control.getMultipleValuesDisjuncted());
				parameterDto.setUseFuzziness(parameterMultipleValuesDto.isUseFuzziness());

				String[] parameterValues = parameterMultipleValuesDto.getParameterValues();

				// Se non ci sono valori per questo parametro...
				if ((parameterValues == null || parameterValues.length == 0 || StringUtils.isEmpty(parameterValues[0]))) {
					if (widget instanceof HiddenWidget && StringUtils.isNotBlank(widget.getDefaultValue())) {
						parameterValues = new String[] { widget.getDefaultValue() };
					} else
						continue;
				}

				for (int j = 0; j < parameterValues.length; j++) {
					ParameterValueDTO parameterValueDTO;

					// se il fieldType non è nullo il parametro in questione è
					// da
					// prendere dalla classe modello
					// if (fieldType != null) {
					if (WidgetUtil.isString(fieldType)) {

						// se il tipo di confronto è exact
						if ("exact".equals(widget.getStringMatchType())) {
							if (!"null".equals(parameterValues[j])) {
								String sign = SearchUtil.getSign(parameterMultipleValuesDto, j);
								if (!"=".equals(sign) && !"<>".equals(sign))
									sign = "=";
								parameterValueDTO = new ParameterValueDTO(parameterValues[j], sign);
								parameterList.add(parameterValueDTO);
							}
						} else {
							// se è una stringa la splitto sullo spazio e carico
							// l'array risultante nella lista di items
							// che verrà utilizzata per il like

							String[] stringValues = parameterValues[j].split(" ");
							for (int i = 0; i < stringValues.length; i++) {
								if (!"null".equals(stringValues[i].trim())) {
									parameterValueDTO = new ParameterValueDTO(stringValues[i].trim(), " like ");
									parameterList.add(parameterValueDTO);
								}
							}
						}

						// se è una stringa la splitto sullo spazio e carico
						// l'array risultante nella lista di items
						// che verrà utilizzata per il like

					} else {
						Object parameter = null;

						// converto il parametro al tipo che ho ricavato
						// tramite reflection
						try {
							if (!"null".equals(parameterValues[j]))
								parameter = ConvertUtils.convert(parameterValues[j], fieldType);
						} catch (ConversionException ce) {
							if (!errorList.contains(widget.getPageAttributeName()))
								errorList.add(widget.getPageAttributeName());
						}

						if (parameter != null) {
							// Ricavo il segno per il confronto: i valori
							// possibili sono =, <=, >=
							String sign = SearchUtil.getSign(parameterMultipleValuesDto, j);
							parameterValueDTO = new ParameterValueDTO(parameter, sign);
							parameterList.add(parameterValueDTO);
						}
					}
				}

				if (parameterList.size() != 0)
					dbSearchParameter.put(parameterDto, parameterList);
			}
		}
	}

	public static String getAliasedSelectValue(String propertyPath) {
		String pre = StringUtils.substringBeforeLast(propertyPath, ".");
		if (StringUtils.contains(pre, "."))
			pre = StringUtils.substringAfterLast(pre, ".");
		if (StringUtils.contains(pre, "_"))
			pre = StringUtils.substringAfterLast(pre, "_");
		String post = StringUtils.substringAfterLast(propertyPath, ".");
		return pre + "." + post;

	}

	public static String getAliasedSelectValueSql(String propertyPath) {
		String pre = StringUtils.substringBeforeLast(propertyPath, ".");
		if (StringUtils.contains(pre, "."))
			pre = StringUtils.substringAfterLast(pre, ".");
		if (StringUtils.contains(pre, ":"))
			pre = StringUtils.substringAfterLast(pre, ":");
		if (StringUtils.contains(pre, "@"))
			pre = StringUtils.substringBefore(pre, "@");
		String post = StringUtils.substringAfterLast(propertyPath, ".");
		return pre + "." + post;

	}

	public static String escapeBracket(String text) {
		return StringUtils.replace(StringUtils.replace(text, ")", "@CLOSE_BRACKET@"), "(", "@OPEN_BRACKET@");
	}

	public static String unescapeBracket(String text) {
		return StringUtils.replace(StringUtils.replace(text, "@CLOSE_BRACKET@", ")"), "@OPEN_BRACKET@", "(");
	}

	public static String getClassNameOnly(Class clazz) {
		String fqClassName = clazz.getName();
		return fqClassName.substring(fqClassName.lastIndexOf(".") + 1);
	}

	public static void reload(WidgetService widgetService, SearchService searchService) throws Exception {
		log.info("Search reload");
		WidgetUtil.reload(widgetService);
		SearchConstant.searchBuilderMap = new HashMap<String, SearchBuilder>();
		List<SearchBuilder> searchBuilderList = searchService.initSearchBuilderList();
		for (SearchBuilder searchBuilder : searchBuilderList) {
			searchBuilder.init();
			// if the url is defined then put this value in the map
			// otherwise use the name
			if (StringUtils.isNotBlank(searchBuilder.getUrl())) {
				SearchConstant.searchBuilderMap.put(searchBuilder.getUrl(), searchBuilder);
			} else if (StringUtils.isNotBlank(searchBuilder.getName())) {
				SearchConstant.searchBuilderMap.put(searchBuilder.getName(), searchBuilder);
			}
		}
	}
}
