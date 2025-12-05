package four.group.jahadi.DTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectIdListDeserializer extends JsonDeserializer<List<ObjectId>> {
    @Override
    public List<ObjectId> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.readValueAsTree();
        List<ObjectId> result = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode element : node) {
                if (element.isTextual()) {
                    String id = element.asText();
                    if (ObjectId.isValid(id)) {
                        result.add(new ObjectId(id));
                    } else {
                        throw new IOException("Invalid ObjectId: " + id);
                    }
                }
            }
        } else {
            throw new IOException("Expected JSON array for List<ObjectId>");
        }

        return result;
    }
}