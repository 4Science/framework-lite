package it.cilea.core.widget.model.impl.command;

import it.cilea.core.CoreConstant;
import it.cilea.core.widget.WidgetConstant.ParameterType;
import it.cilea.core.widget.model.impl.core.TextWidget;

import java.text.Format;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
@DiscriminatorValue("command-date")
public class CommandDateWidget extends TextWidget {

	@Transient
	protected String suffixIdOnly;

	@Override
	public void init() throws Exception {
		super.init();
		if (cssClass == null)
			cssClass = "date";
		if ("currentDate".equals(super.getDefaultValue())) {
			Format dateFormat = CoreConstant.dateFormat;
			Date date = new Date();
			super.setDefaultValue(dateFormat.format(date));

		}
		if (parameterMap.containsKey(ParameterType.SUFFIX_ID_ONLY.name()))
			suffixIdOnly = parameterMap.get(ParameterType.SUFFIX_ID_ONLY.name()).iterator().next();
	}

	public String getSuffixIdOnly() {
		return suffixIdOnly;
	}

	public void setSuffixIdOnly(String suffixIdOnly) {
		this.suffixIdOnly = suffixIdOnly;
	}

}
