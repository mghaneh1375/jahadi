package four.group.jahadi.DTO;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        try {
            long timestamp = p.getLongValue();
            return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        catch (Exception x) {
            String date = p.getText();
            // parse as Instant
            Instant instant = Instant.parse(date);
            // convert to LocalDateTime at system default zone (or UTC if preferred)
            return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
    }
}