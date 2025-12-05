package four.group.jahadi.Models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import four.group.jahadi.Utility.Utility;

import java.io.IOException;
import java.time.LocalDateTime;

public class JustDateSerialization extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(Utility.convertUTCDateToJalaliDate(date));
    }
}
