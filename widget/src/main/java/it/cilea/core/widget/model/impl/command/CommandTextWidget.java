package it.cilea.core.widget.model.impl.command;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.model.Widget;

@Entity
@DiscriminatorValue("command-text")
public class CommandTextWidget extends Widget {

	@Transient
	private Integer maxLength;
	@Transient
	private Boolean showCounter;
	@Transient
	protected Boolean rangeSearch = false;
	@Transient
	protected Boolean showI18n;
	@Transient
	protected String modeType;
	@Transient
	protected String suffixIdOnly;
	@Transient
	protected Boolean isMoney;
	@Transient
	protected Boolean allowMultiple;
	@Transient
	protected Integer multipleIndexToShow;

	@Override
	public void init() throws Exception {
		super.init();
		if (parameterMap.containsKey(ParameterType.RANGE_SEARCH.name()))
			rangeSearch = Boolean.valueOf(parameterMap.get(ParameterType.RANGE_SEARCH.name()).iterator().next());
		if (rangeSearch == null)
			rangeSearch = false;
		if (parameterMap.containsKey(ParameterType.SHOW_COUNTER.name()))
			showCounter = Boolean.valueOf(parameterMap.get(ParameterType.SHOW_COUNTER.name()).iterator().next());
		if (showCounter == null)
			showCounter = false;
		if (parameterMap.containsKey(ParameterType.MAX_LENGTH.name()))
			maxLength = Integer.valueOf(parameterMap.get(ParameterType.MAX_LENGTH.name()).iterator().next());
		if (parameterMap.containsKey(ParameterType.SUFFIX_ID_ONLY.name()))
			suffixIdOnly = parameterMap.get(ParameterType.SUFFIX_ID_ONLY.name()).iterator().next();
		if (parameterMap.containsKey(ParameterType.SHOW_I18N.name()))
			showI18n = Boolean.valueOf(parameterMap.get(ParameterType.SHOW_I18N.name()).iterator().next());
		if (parameterMap.containsKey(ParameterType.MODE_TYPE.name())) {
			modeType = parameterMap.get(ParameterType.MODE_TYPE.name()).iterator().next();
		}
		if (parameterMap.containsKey(ParameterType.IS_MONEY.name()))
			isMoney = Boolean.valueOf(parameterMap.get(ParameterType.IS_MONEY.name()).iterator().next());
		if (parameterMap.containsKey(ParameterType.ALLOW_MULTIPLE.name()))
			allowMultiple = Boolean.valueOf(parameterMap.get(ParameterType.ALLOW_MULTIPLE.name()).iterator().next());
		if (allowMultiple == null)
			allowMultiple = false;
		if (parameterMap.containsKey(ParameterType.MULTIPLE_INDEX_TO_SHOW.name()))
			multipleIndexToShow = Integer.valueOf(parameterMap.get(ParameterType.MULTIPLE_INDEX_TO_SHOW.name()).iterator().next());
	}

	public Integer getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}

	public Boolean getRangeSearch() {
		return rangeSearch;
	}

	public void setRangeSearch(Boolean rangeSearch) {
		this.rangeSearch = rangeSearch;
	}

	public Boolean getShowCounter() {
		return showCounter == null ? false : showCounter;
	}

	public void setShowCounter(Boolean showCounter) {
		this.showCounter = showCounter;
	}

	public Boolean getShowI18n() {
		return showI18n;
	}

	public void setShowI18n(Boolean showI18n) {
		this.showI18n = showI18n;
	}

	public String getModeType() {
		return modeType;
	}

	public void setModeType(String modeType) {
		this.modeType = modeType;
	}

	public String getSuffixIdOnly() {
		return suffixIdOnly;
	}

	public void setSuffixIdOnly(String suffixIdOnly) {
		this.suffixIdOnly = suffixIdOnly;
	}

	public Boolean getIsMoney() {
		return isMoney;
	}

	public void setIsMoney(Boolean isMoney) {
		this.isMoney = isMoney;
	}

	public Boolean getAllowMultiple() {
		return allowMultiple;
	}

	public void setAllowMultiple(Boolean allowMultiple) {
		this.allowMultiple = allowMultiple;
	}
	public Integer getMultipleIndexToShow() {
		return multipleIndexToShow;
	}
	public void setMultipleIndexToShow(Integer multipleIndexToShow) {
		this.multipleIndexToShow = multipleIndexToShow;
	}
}
