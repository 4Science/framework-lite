package it.cilea.core.widget.model.impl.core;

import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

@Entity
@DiscriminatorValue("tab")
public class TabWidget extends Widget {

	public String[] getRequestValues(HttpServletRequest request) {
		return null;
	}

}
