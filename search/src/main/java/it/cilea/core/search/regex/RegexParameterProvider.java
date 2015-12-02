package it.cilea.core.search.regex;

import it.cilea.core.model.Selectable;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface RegexParameterProvider {
	public List<? extends Selectable> getValues(String parameterName, String searchBuilderIdentifier,
			HttpServletRequest request);
}
