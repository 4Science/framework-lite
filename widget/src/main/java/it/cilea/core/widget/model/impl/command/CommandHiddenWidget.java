package it.cilea.core.widget.model.impl.command;

import it.cilea.core.widget.model.Widget;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("command-hidden")
public class CommandHiddenWidget extends Widget {

}
