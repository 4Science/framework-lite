package it.cilea.core.serializer;

import it.cilea.core.model.Selectable;

import java.lang.reflect.Type;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SelectableAllI18nSerializer implements JsonSerializer<Selectable> {

	// TODO: extend interface Selectable to include method
	// getAllI18nDisplayValue?
	public JsonElement serialize(Selectable selectable, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		String allI18nDisplayValue = null;
		try {
			allI18nDisplayValue = (String) BeanUtils.getProperty(selectable, "allI18nDisplayValue");
		} catch (Exception e) {
			allI18nDisplayValue = selectable.getDisplayValue();
		}
		obj.addProperty("displayValue", allI18nDisplayValue);
		obj.addProperty("identifyingValue", selectable.getIdentifyingValue());
		return obj;
	}

}
