package it.cilea.core.widget.strategy.options;

import it.cilea.core.model.Selectable;
import it.cilea.core.widget.WidgetConstant;
import it.cilea.core.widget.model.OptionsWidget;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

@SuppressWarnings("unchecked")
public class OptionsWidgetJavaMethodPopulateStrategy extends OptionsWidgetPopulateStrategy {

	public OptionsWidgetJavaMethodPopulateStrategy(OptionsWidget widget, HttpServletRequest request) {
		super(widget, request);
	}

	public List<? extends Selectable> getOptions() {
		String populationValue = widget.getPopulationValue();
		if (StringUtils.isBlank(populationValue) || !populationValue.contains("|"))
			throw new IllegalStateException(
					"OptionsWidgetJavaMethodProvider expects a populationValue with this pattern SPRING_BEAN_ID|METHOD_NAME");
		String[] piece = populationValue.split("\\|");
		String beanName = piece[0];
		String methodName = piece[1];
		Object obj = WidgetConstant.beanFactory.getBean(beanName);

		List<Selectable> globalList = new ArrayList<Selectable>();
		if (widget.getRenderEmptyOption()) {
			globalList.add(widget.getSelectableEmptyOption());
		}

		List<Selectable> resultList = null;
		try {
			resultList = (List<Selectable>) MethodUtils.invokeMethod(obj, methodName, getMethodInvocationParameter());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (resultList == null) {
			String message = "Metodo " + methodName + "(";

			int counter = 0;
			for (Object o : getMethodInvocationParameter()) {
				if (counter != 0)
					message += ", ";
				message += o.getClass();
				counter++;
			}
			message += ") non trovato";
		} else
			globalList.addAll(resultList);

		return globalList;
	}

	public List<? extends Selectable> getOptions(String[] ids) {
		List<Selectable> defaultSelectable = new ArrayList<Selectable>();
		// defaultSelectable.add(new SelectBaseString("", ""));

		if (ArrayUtils.isEmpty(ids) || (ids.length == 1 && StringUtils.isBlank(ids[0])))
			return defaultSelectable;
		String populationValue = widget.getPopulationValue();
		if (StringUtils.isBlank(populationValue) || !populationValue.contains("|"))
			throw new IllegalStateException(
					"OptionsWidgetJavaMethodProvider expects a populationValue with this pattern SPRING_BEAN_ID|METHOD_NAME");
		String[] piece = populationValue.split("\\|");

		if (piece.length < 3)
			throw new IllegalStateException(
					"OptionsWidgetJavaMethodProvider expects a populationValue with this pattern SPRING_BEAN_ID|METHOD_NAME|METHOD_NAME");

		// Quando viene invocato questo metodo vuol dire che ho la necessità di
		// recuperare il valore
		// visualizzato per autocomplete
		String beanName = piece[0];
		String methodName = piece[2];

		Object obj = WidgetConstant.beanFactory.getBean(beanName);
		try {
			return (List<Selectable>) MethodUtils.invokeMethod(obj, methodName, new Object[] { ids });
		} catch (Exception e) {
			e.printStackTrace();
		}

		return defaultSelectable;

	}
}