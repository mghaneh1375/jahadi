package four.group.jahadi.DTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import four.group.jahadi.Exception.InvalidFieldsException;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {
    @Override
    public ObjectId deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if(!ObjectId.isValid(p.getValueAsString()))
            throw new InvalidFieldsException("invalid oId");

        return new ObjectId(p.getValueAsString());
    }
}