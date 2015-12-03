package it.cilea.core.widget.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import org.apache.commons.lang.StringUtils;

import it.cilea.core.configuration.util.ConfigurationUtil;
import it.cilea.core.model.SelectBaseString;
import it.cilea.core.model.Selectable;
import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.WidgetConstant.WidgetDictionaryType;
import it.cilea.core.widget.factory.OptionsWidgetPopulateStrategyFactory;
import it.cilea.core.widget.strategy.options.OptionsWidgetPopulateStrategy;

@Entity
public abstract class OptionsWidget extends Widget {

	@Column(name = "POPULATION_TYPE", insertable = true, updatable = true, nullable = true, length = 255)
	@Size(min = 0, max = 255)
	protected String populationType;

	@Column(name = "POPULATION_VALUE", insertable = true, updatable = true, nullable = true, length = 4000)
	@Size(min = 0, max = 4000)
	protected String populationValue;

	@Transient
	protected String ajaxUrl;

	@Transient
	protected Boolean autoDisplay;

	@Transient
	protected Boolean renderEmptyOption;

	@Transient
	protected Boolean multipleSelection;

	@Transient
	protected List<Selectable> options;

	@Override
	public void init() throws Exception {
		super.init();

		if (parameterMap.containsKey(ParameterType.AUTO_DISPLAY.name()))
			autoDisplay = Boolean.valueOf(parameterMap.get(ParameterType.AUTO_DISPLAY.name()).iterator().next());

		if (parameterMap.containsKey(ParameterType.RENDER_EMPTY_OPTION.name()))
			renderEmptyOption = Boolean
					.valueOf(parameterMap.get(ParameterType.RENDER_EMPTY_OPTION.name()).iterator().next());

		if (renderEmptyOption == null)
			renderEmptyOption = true;

		if (parameterMap.containsKey(ParameterType.MULTIPLE_SELECTION.name()))
			multipleSelection = Boolean
					.valueOf(parameterMap.get(ParameterType.MULTIPLE_SELECTION.name()).iterator().next());

		if (multipleSelection == null)
			multipleSelection = false;

		if (parameterMap.containsKey(ParameterType.AJAX_URL.name())
				&& StringUtils.isNotBlank(parameterMap.get(ParameterType.AJAX_URL.name()).iterator().next()))
			ajaxUrl = ConfigurationUtil.replaceText(parameterMap.get(ParameterType.AJAX_URL.name()).iterator().next());

	}

	public Selectable getSelectableEmptyOption() {
		SelectBaseString selectable = new SelectBaseString();
		selectable.setLabel(this.getEmptyOption());
		selectable.setValue("");
		return selectable;
	}

	public String getEmptyOption() {
		return getGenericWidgetDictionaryType(WidgetDictionaryType.EMPTY);
	}

	public void setOptions(List<Selectable> options) {
		this.options = options;
	}

	public List<Selectable> getOptions() {
		return options;
	}

	public String getPopulationType() {
		return populationType;
	}

	public void setPopulationType(String populationType) {
		this.populationType = populationType;
	}

	public String getPopulationValue() {
		return populationValue;
	}

	public void setPopulationValue(String populationValue) {
		this.populationValue = populationValue;
	}

	public Boolean getRenderEmptyOption() {
		return renderEmptyOption;
	}

	public void setRenderEmptyOption(Boolean renderEmptyOption) {
		this.renderEmptyOption = renderEmptyOption;
	}

	public String getAjaxUrl() {
		return ajaxUrl;
	}

	public void setAjaxUrl(String ajaxUrl) {
		this.ajaxUrl = ajaxUrl;
	}

	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	public boolean getMultipleSelection() {
		return multipleSelection;
	}

	public void setMultipleSelection(Boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	public List<Selectable> getCollection(HttpServletRequest request) {
		OptionsWidgetPopulateStrategy provider = OptionsWidgetPopulateStrategyFactory.getCommand(this, request);
		if (options != null) {
			return options;
		} else {
			try {
				List<Selectable> options = new ArrayList<Selectable>();
				if (this.getParentWidgetId() == null)
					options = (List<Selectable>) provider.getOptions();
				return options;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return new ArrayList<Selectable>();
	}

	public Boolean getAutoDisplay() {
		return autoDisplay;
	}

	public void setAutoDisplay(Boolean autoDisplay) {
		this.autoDisplay = autoDisplay;
	}
}
