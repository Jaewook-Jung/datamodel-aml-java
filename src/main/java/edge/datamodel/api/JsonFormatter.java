package edge.datamodel.api;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonFormatter implements JsonSerializer<Object>, JsonDeserializer<Object>{

		private static final String CLASSNAME = "CLASSNAME";
		private static final String DATA = "DATA";
		
		public Object deserialize(JsonElement jsonElement, Type type,
				JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
			JsonObject jsonObject = jsonElement.getAsJsonObject();
			JsonPrimitive prim = (JsonPrimitive)jsonObject.get(CLASSNAME);
			String className = prim.getAsString();
			
			Class<?> klass = getObjectClass(className);
			
			return jsonDeserializationContext.deserialize(jsonObject.get(DATA), klass);
		}
		
		public JsonElement serialize(Object jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty(CLASSNAME,  jsonElement.getClass().getName());
			jsonObject.add(DATA, jsonSerializationContext.serialize(jsonElement));
			return jsonObject;			
		}
		
		
		public Class<?> getObjectClass(String className) {
			try{
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new JsonParseException(e.getMessage());
			}
		}
}
