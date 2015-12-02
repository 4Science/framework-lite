package it.cilea.core.widget.factory;

import it.cilea.core.widget.WidgetConstant.OptionsWidgetPopulationType;
import it.cilea.core.widget.model.OptionsWidget;
import it.cilea.core.widget.strategy.options.OptionsWidgetDictionaryMinePopulateStrategy;
import it.cilea.core.widget.strategy.options.OptionsWidgetHQLPopulateStrategy;
import it.cilea.core.widget.strategy.options.OptionsWidgetJavaMethodPopulateStrategy;
import it.cilea.core.widget.strategy.options.OptionsWidgetParametersPopulateStrategy;
import it.cilea.core.widget.strategy.options.OptionsWidgetPopulateStrategy;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

public class OptionsWidgetPopulateStrategyFactory {
	public static OptionsWidgetPopulateStrategy getCommand(OptionsWidget widget) {
		return getCommand(widget, null);
	}

	public static OptionsWidgetPopulateStrategy getCommand(OptionsWidget widget, HttpServletRequest request) {

		String clazz = "";
		if (OptionsWidgetPopulationType.DICTIONARY_MINE.toString().equals(widget.getPopulationType())) {
			clazz = OptionsWidgetDictionaryMinePopulateStrategy.class.getCanonicalName();
		} else if (OptionsWidgetPopulationType.JAVA_METHOD.toString().equals(widget.getPopulationType())) {
			clazz = OptionsWidgetJavaMethodPopulateStrategy.class.getCanonicalName();
		} else if (OptionsWidgetPopulationType.HQL.toString().equals(widget.getPopulationType())) {
			clazz = OptionsWidgetHQLPopulateStrategy.class.getCanonicalName();
		} else if (OptionsWidgetPopulationType.SOLR.toString().equals(widget.getPopulationType())) {
			clazz = "it.cilea.core.widget.strategy.options.OptionsWidgetSOLRPopulateStrategy";
		} else if (OptionsWidgetPopulationType.WIDGET_OBJECT.toString().equals(widget.getPopulationType())) {
			clazz = "it.cilea.core.widget.strategy.options.OptionsWidgetWidgetObjectPopulateStrategy";
		} else if (OptionsWidgetPopulationType.PARAMETERS.toString().equals(widget.getPopulationType())) {
			clazz = OptionsWidgetParametersPopulateStrategy.class.getCanonicalName();
		} else if (!widget.getClass().getCanonicalName()
				.equals("it.cilea.core.widget.model.impl.command.CommandAutocompleteWidget")) {
			throw new UnsupportedOperationException(
					"No OptionsWidgetCommand is available for the given Population Type: " + widget.getPopulationType());
		}

		if (!widget.getClass().getCanonicalName()
				.equals("it.cilea.core.widget.model.impl.command.CommandAutocompleteWidget")) {
			try {
				return (OptionsWidgetPopulateStrategy) Class.forName(clazz)
						.getDeclaredConstructor(OptionsWidget.class, HttpServletRequest.class)
						.newInstance(widget, request);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new UnsupportedOperationException("Error initializing population of: "
						+ widget.getPopulationType());
			}
		} else {
			return null;
		}


	}
}
