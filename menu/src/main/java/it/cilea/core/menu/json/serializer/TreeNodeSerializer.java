package it.cilea.core.menu.json.serializer;

import it.cilea.core.menu.model.TreeNode;

import java.lang.reflect.Type;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class TreeNodeSerializer implements JsonSerializer<TreeNode> {

	@Override
	public JsonElement serialize(TreeNode tree, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.addProperty("state", "close");
		obj.addProperty("position", tree.getBrotherOrder());

		JsonObject attributeList = new JsonObject();

		attributeList.addProperty("id", tree.getIdentifyingValue());
		attributeList.addProperty("identifier", tree.getIdentifier());
		attributeList.addProperty("rel", tree.getItemType());
		attributeList.addProperty("title", tree.getDisplayValue());
		attributeList.addProperty("onclick", tree.getOnClick());
		attributeList.addProperty("link", tree.getLink());

		JsonObject dataList = new JsonObject();

		dataList.addProperty("title", tree.getDisplayValue());

		// JsonObject attributeDataList = new JsonObject();
		// attributeDataList.addProperty("onclick", tree.getOnClick());
		// TODO: deve essere scommentato???
		// attributeDataList.addProperty("onMouseOver", tree.getOnMouseOver());
		// dataList.add("attr", attributeDataList);

		obj.add("data", dataList);

		obj.add("attr", attributeList);
		JsonArray childrenList = new JsonArray();

		for (TreeNode _tree : tree.getChildrenList()) {
			childrenList.add(serialize(_tree, typeOfSrc, context));
		}
		obj.add("children", childrenList);
		return obj;
	}

}
