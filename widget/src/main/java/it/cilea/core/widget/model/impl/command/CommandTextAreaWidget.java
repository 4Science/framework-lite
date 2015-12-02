package it.cilea.core.widget.model.impl.command;

import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("command-textarea")
public class CommandTextAreaWidget extends Widget {

	@Transient
	private Integer maxLength;

	@Transient
	private Boolean showCounter;

	@Transient
	private Boolean ckEditor;

	@Transient
	private String layoutMode;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.containsKey(ParameterType.SHOW_COUNTER.name()))
			showCounter = Boolean.valueOf(parameterMap.get(ParameterType.SHOW_COUNTER.name()).iterator().next());
		if (showCounter == null)
			showCounter = false;
		if (parameterMap.containsKey(ParameterType.MAX_LENGTH.name()))
			maxLength = Integer.valueOf(parameterMap.get(ParameterType.MAX_LENGTH.name()).iterator().next());

		if (parameterMap.containsKey(ParameterType.CK_EDITOR.name()))
			ckEditor = Boolean.valueOf(parameterMap.get(ParameterType.CK_EDITOR.name()).iterator().next());
		if (ckEditor == null)
			ckEditor = false;

		if (parameterMap.containsKey(ParameterType.LAYOUT_MODE.name()))
			layoutMode = parameterMap.get(ParameterType.LAYOUT_MODE.name()).iterator().next();
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Boolean getShowCounter() {
		return showCounter == null ? false : showCounter;
	}

	public void setShowCounter(Boolean showCounter) {
		this.showCounter = showCounter;
	}

	public Boolean getCkEditor() {
		return ckEditor;
	}

	public void setCkEditor(Boolean ckEditor) {
		this.ckEditor = ckEditor;
	}

	public String getLayoutMode() {
		return layoutMode;
	}

	public void setLayoutMode(String layoutMode) {
		this.layoutMode = layoutMode;
	}

}
