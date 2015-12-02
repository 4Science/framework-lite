package it.cilea.core.widget.model.impl.core;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("multi-select")
public class MultiSelectWidget extends SelectWidget {

	public MultiSelectWidget() {

	}

}
