package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserDigestSerialization extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", user.getId().toString());
        jsonGenerator.writeStringField("name", user.getName());
        jsonGenerator.writeStringField("phone", user.getPhone());
        jsonGenerator.writeStringField("NID", user.getNid());
        jsonGenerator.writeStringField("sex", user.getSex().getName());;
        jsonGenerator.writeEndObject();
    }
}
