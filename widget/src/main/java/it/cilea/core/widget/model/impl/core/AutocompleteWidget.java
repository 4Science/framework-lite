package it.cilea.core.widget.model.impl.core;

import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.model.Selectable;
import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.factory.OptionsWidgetPopulateStrategyFactory;
import it.cilea.core.widget.model.OptionsWidget;
import it.cilea.core.widget.strategy.options.OptionsWidgetPopulateStrategy;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

@Entity
@DiscriminatorValue("autocomplete")
public class AutocompleteWidget extends OptionsWidget {
	@Transient
	private String autocompleteUrl;
	@Transient
	private String autocompleteInputName;
	@Transient
	private String autocompleteInputValue;
	@Transient
	private String autocompleteData;
	@Transient
	private String autocompleteGetUrl;
	@Transient
	private String autocompleteGetData;

	// Per utilizzare questo widget è necessario specificare il nome del metodo
	// da invocare per
	// l'autocomplete (che dovrà essere disponibile in uno dei service elencati
	// nel genericService)
	// e il modulo in cui l'autocomplete è utilizzato. Quest'ultima info serve
	// per creare correttamente
	// l'url per l'ajax che ovviamente cambia da applicativo ad applicativo
	@Override
	public void init() throws Exception {
		super.init();

		if (parameterMap.containsKey(ParameterType.AUTOCOMPLETE_URL.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.AUTOCOMPLETE_URL.name()).iterator().next()))
			autocompleteUrl = ConfigurationUtil.replaceText(parameterMap.get(ParameterType.AUTOCOMPLETE_URL.name())
					.iterator().next());
		if (parameterMap.containsKey(ParameterType.AUTOCOMPLETE_DATA.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.AUTOCOMPLETE_DATA.name()).iterator().next()))
			autocompleteData = ConfigurationUtil.replaceText(parameterMap.get(ParameterType.AUTOCOMPLETE_DATA.name())
					.iterator().next());
		if (parameterMap.containsKey(ParameterType.AUTOCOMPLETE_GET_URL.name())
				&& StringUtils
						.isNotBlank(parameterMap.get(ParameterType.AUTOCOMPLETE_GET_URL.name()).iterator().next()))
			autocompleteGetUrl = ConfigurationUtil.replaceText(parameterMap
					.get(ParameterType.AUTOCOMPLETE_GET_URL.name()).iterator().next());
		if (parameterMap.containsKey(ParameterType.AUTOCOMPLETE_GET_DATA.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.AUTOCOMPLETE_GET_DATA.name()).iterator()
						.next()))
			autocompleteGetData = ConfigurationUtil.replaceText(parameterMap
					.get(ParameterType.AUTOCOMPLETE_GET_DATA.name()).iterator().next());
		if (parameterMap.containsKey(ParameterType.AUTOCOMPLETE_INPUT_NAME.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.AUTOCOMPLETE_INPUT_NAME.name()).iterator()
						.next()))
			autocompleteInputName = ConfigurationUtil.replaceText(parameterMap
					.get(ParameterType.AUTOCOMPLETE_INPUT_NAME.name()).iterator().next());
		if (parameterMap.containsKey(ParameterType.AUTOCOMPLETE_INPUT_VALUE.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.AUTOCOMPLETE_INPUT_VALUE.name()).iterator()
						.next()))
			autocompleteInputValue = ConfigurationUtil.replaceText(parameterMap
					.get(ParameterType.AUTOCOMPLETE_INPUT_VALUE.name()).iterator().next());

		renderEmptyOption = false;
	}

	@Override
	public void setRenderAsHidden(Boolean renderAsHidden) {
		throw new UnsupportedOperationException("Attributo RenderAsHidden non supportata");
	}

	public List<Selectable> getCollection(HttpServletRequest request) {
		String[] requestValues = getRequestValues(request);
		String[] optionIds = (requestValues != null && requestValues.length != 0) ? requestValues : null;
		try {
			OptionsWidgetPopulateStrategy provider = OptionsWidgetPopulateStrategyFactory.getCommand(this, request);
			List<Selectable> selectableList = (List<Selectable>) provider.getOptions(optionIds);
			return selectableList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Selectable>();
	}

	public String getAutocompleteData() {
		return autocompleteData;
	}

	public String getAutocompleteGetData() {
		return autocompleteGetData;
	}

	public String getAutocompleteGetUrl() {
		return ConfigurationUtil.replaceText(autocompleteGetUrl);
	}

	public String getAutocompleteInputName() {
		return autocompleteInputName;
	}

	public String getAutocompleteInputValue() {
		return autocompleteInputValue;
	}

	public String getAutocompleteUrl() {
		return ConfigurationUtil.replaceText(autocompleteUrl);
	}

}
