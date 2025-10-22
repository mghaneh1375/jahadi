package four.group.jahadi.DTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class PersianNumberDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String value = p.getText();

        if (value != null) {
            value = value.replace("۰", "0")
                         .replace("۱", "1")
                         .replace("۲", "2")
                         .replace("۳", "3")
                         .replace("۴", "4")
                         .replace("۵", "5")
                         .replace("۶", "6")
                         .replace("۷", "7")
                         .replace("۸", "8")
                         .replace("۹", "9");
        }
        return value;
    }
}