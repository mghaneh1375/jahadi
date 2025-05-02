package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import four.group.jahadi.Service.UserService;

import java.io.IOException;

import static four.group.jahadi.Utility.StaticValues.SERVER_ADDR;

public class OwnerSerialization extends JsonSerializer<User> {

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", user.getId().toString());
        jsonGenerator.writeStringField("name", user.getName());
        jsonGenerator.writeStringField("tel", user.getTel());
        jsonGenerator.writeStringField("phone", user.getPhone());
        jsonGenerator.writeStringField("field", user.getField());
        jsonGenerator.writeStringField("NID", user.getNid());
        jsonGenerator.writeStringField("sex", user.getSex().getName());
        jsonGenerator.writeStringField("pic", user.getPic() == null ? null : SERVER_ADDR + UserService.PICS_FOLDER + "/" + user.getPic());

        if(user.getStatus() != null)
            jsonGenerator.writeStringField("status", user.getStatus().getName());

        if(user.getLodgment() != null)
            jsonGenerator.writeStringField("lodgment", user.getLodgment().getName());

        jsonGenerator.writeEndObject();
    }
}
