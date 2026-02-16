package four.group.jahadi.Models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import four.group.jahadi.Enums.Drug.DrugType;

import java.io.IOException;

public class DrugTypeDeserialization extends JsonDeserializer<DrugType> {

    @Override
    public DrugType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        if(jsonParser == null) return null;

        return DrugType.valueOf(jsonParser.getValueAsString().toUpperCase());
    }
}
