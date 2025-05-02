package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import four.group.jahadi.Enums.Color;

import java.io.IOException;

public class ColorSerialization extends JsonSerializer<Color> {

    @Override
@four.group.jahadi.Utility.KeepMethodName
    public void serialize(Color color, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(color == null ? null : color.getName().toLowerCase());
    }
}
