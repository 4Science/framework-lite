package it.cilea.core.serializer;

import it.cilea.core.model.Selectable;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class SelectableSerializer implements JsonSerializer<Selectable> {

	public JsonElement serialize(Selectable selectable, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("displayValue", selectable.getDisplayValue());
		obj.addProperty("identifyingValue", selectable.getIdentifyingValue());
		return obj;
	}

}
