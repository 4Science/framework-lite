package it.cilea.core.widget.model.impl.command;

import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.model.OptionsWidget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

@Entity
@DiscriminatorValue("command-select-move")
public class CommandSelectAndMoveWidget extends OptionsWidget {

	@Transient
	private Integer size;

	@Transient
	private Boolean renderAsHiddenIfOnlyOneOption;

	@Transient
	private String positioning = "horizontal";

	@Override
	public void init() throws Exception {
		super.init();

		if (multipleSelection == null)
			multipleSelection = false;

		if (parameterMap.containsKey(ParameterType.RENDER_AS_HIDDEN_IF_ONE_OPTION.name()))
			renderAsHiddenIfOnlyOneOption = Boolean.valueOf(parameterMap
					.get(ParameterType.RENDER_AS_HIDDEN_IF_ONE_OPTION.name()).iterator().next());

		if (parameterMap.containsKey(ParameterType.VISIBLE_OPTIONS.name()))
			size = Integer.valueOf(parameterMap.get(ParameterType.VISIBLE_OPTIONS.name()).iterator().next());

		if (parameterMap.containsKey(ParameterType.POSITIONING.name()))
			positioning = parameterMap.get(ParameterType.POSITIONING.name()).iterator().next();

		if (renderAsHiddenIfOnlyOneOption == null)
			renderAsHiddenIfOnlyOneOption = true;

		// if there are no parameter name (whose values are in request or
		// session scope), no roles and no parent associated with this widget
		// then all options related to this widget can be loaded at startup and
		// reused without
		// reloading at every request.
		if (CollectionUtils.isEmpty(getParameterNameSet()) && getParentWidgetId() == null) {
			// TODO: batch loading....
		}

	}

	public void setMultipleSelection(boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	public boolean getMultipleSelection() {
		if (multipleSelection == null)
			multipleSelection = false;
		return multipleSelection;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Boolean getRenderAsHiddenIfOnlyOneOption() {
		return renderAsHiddenIfOnlyOneOption;
	}

	public void setRenderAsHiddenIfOnlyOneOption(Boolean renderAsHiddenIfOnlyOneOption) {
		this.renderAsHiddenIfOnlyOneOption = renderAsHiddenIfOnlyOneOption;
	}

	public Integer getSize() {
		return size;
	}

	public void setMultipleSelection(Boolean multipleSelection) {
		this.multipleSelection = multipleSelection;
	}

	public String getPositioning() {
		return positioning;
	}

	public void setPositioning(String positioning) {
		this.positioning = positioning;
	}

	public String getWidgetType() {
		if (StringUtils.isBlank(positioning))
			return "command-selectAndMove-horizontal";
		return "command-selectAndMove-" + positioning;
	}

}
