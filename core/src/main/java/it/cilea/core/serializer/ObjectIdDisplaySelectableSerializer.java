package it.cilea.core.serializer;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ObjectIdDisplaySelectableSerializer implements JsonSerializer<Object[]> {

	public JsonElement serialize(Object[] object, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("identifyingValue", (String) object[0]);
		obj.addProperty("displayValue", (String) object[1]);
		return obj;
	}

}
