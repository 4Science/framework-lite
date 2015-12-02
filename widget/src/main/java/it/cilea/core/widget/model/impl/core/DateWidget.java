package it.cilea.core.widget.model.impl.core;

import it.cilea.core.CoreConstant;

import java.text.Format;
import java.util.Date;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("date")
public class DateWidget extends TextWidget {

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
	}

}
