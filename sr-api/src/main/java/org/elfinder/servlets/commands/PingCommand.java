package org.elfinder.servlets.commands;

import org.elfinder.servlets.ConnectorException;


public class PingCommand extends AbstractCommandOverride {
	

	@Override
	public void execute() throws ConnectorException {
		this.putResponse("result", "pong");
	}

}
