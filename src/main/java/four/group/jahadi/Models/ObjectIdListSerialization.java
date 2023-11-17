package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.List;

public class ObjectIdListSerialization extends JsonSerializer<List<ObjectId>> {

    @Override
    public void serialize(List<ObjectId> objectIds, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        objectIds.forEach(objectId -> {
            try {
                jsonGenerator.writeString(objectId.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
