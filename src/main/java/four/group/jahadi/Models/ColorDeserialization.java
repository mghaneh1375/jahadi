package four.group.jahadi.Models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import four.group.jahadi.Enums.Color;

import java.io.IOException;

public class ColorDeserialization extends JsonDeserializer<Color> {

    @Override
    public Color deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        if(jsonParser == null) return null;

        return Color.valueOf(jsonParser.getValueAsString().toUpperCase());
    }
}
