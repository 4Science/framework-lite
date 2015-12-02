package it.cilea.core.widget.model.impl.command;

import it.cilea.core.widget.model.impl.core.SelectWidget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("command-select2")
public class CommandSelect2Widget extends SelectWidget {

	public CommandSelect2Widget() {

	}

}
