package it.cilea.core.spring.factory;

import it.cilea.core.spring.model.ControllerLogic;

public interface ControllerLogicFactory {
	ControllerLogic getControllerLogic(String url);
}
