package it.cilea.core.widget.model.impl.core;

import it.cilea.core.model.Selectable;
import it.cilea.core.widget.model.OptionsWidget;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

@Entity
@DiscriminatorValue("server-side")
public class ServerSideWidget extends OptionsWidget {

	@Override
	public void init() throws Exception {
		super.init();
		// ServerSideWidget estende OptionsWidget ma non è un effettivo
		// OptionsWidget
		// il default per il renderEmptyOption deve essere false in modo tale
		// che non
		// venga gestito l'options vuota in fase di invocazione dei metodi che
		// ritornano
		// i valori
		renderEmptyOption = false;
	}

	public List<Selectable> getCollection(HttpServletRequest request) {
		return new ArrayList<Selectable>();
	}
}
