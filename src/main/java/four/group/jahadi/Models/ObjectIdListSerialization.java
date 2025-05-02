package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ObjectIdListSerialization extends JsonSerializer<List<ObjectId>> {

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void serialize(List<ObjectId> objectIds, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {

        if(objectIds == null || objectIds.size() == 0) {
            try {
                jsonGenerator.writeNull();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        String[] ids = new String[objectIds.size()];
        int i = 0;
        for (ObjectId id : objectIds) {
            ids[i++] = id.toString();
        }

        try {
            jsonGenerator.writeArray(ids, 0, objectIds.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
