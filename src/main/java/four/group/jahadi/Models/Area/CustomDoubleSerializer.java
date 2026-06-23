package four.group.jahadi.Models.Area;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Locale;

public class CustomDoubleSerializer extends JsonSerializer<Double> {
    @Override
    public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        try {
            if (value == null) {
                gen.writeNull();
            } else {
                // تبدیل به استرینگ با فرمت دو رقم اعشار
                gen.writeNumber(Double.parseDouble(String.format(Locale.US, "%.2f", value)));
            }
        }
        catch (Exception ignore) {}
    }
}