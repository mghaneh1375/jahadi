package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import java.io.IOException;

public class OwnerSerialization extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeString(new JSONObject()
                .put("name", user.getName())
                .put("pic", user.getPic())
                .put("color", user.getColor())
                .put("phone", user.getPhone())
                .toString()
        );
    }
}
