package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import four.group.jahadi.Service.UserService;

import java.io.IOException;

import static four.group.jahadi.Utility.StaticValues.SERVER_ADDR;

public class PicSerialization extends JsonSerializer<String> {

    @Override
    public void serialize(String pic, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(pic == null ? null : SERVER_ADDR + UserService.PICS_FOLDER + "/" + pic);
    }
}
