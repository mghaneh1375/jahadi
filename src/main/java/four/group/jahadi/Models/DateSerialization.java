package four.group.jahadi.Models;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import four.group.jahadi.Utility.Utility;

import java.io.IOException;
import java.util.Date;

public class DateSerialization extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(Utility.convertUTCDateToJalali(date));
    }
}
