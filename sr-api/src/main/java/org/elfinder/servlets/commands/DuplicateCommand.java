package org.elfinder.servlets.commands;

import org.elfinder.servlets.ConnectorException;


public class DuplicateCommand extends AbstractCommandOverride {
	

	@Override
	public void execute() throws ConnectorException {
		this.putResponse("error", "not yet implemented");
	}

}
