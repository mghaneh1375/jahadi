package four.group.jahadi.DTO.Region;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static four.group.jahadi.Utility.Utility.*;

public class ConvertStringToLongDeserialization extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if(jsonParser == null) return null;
        try {
            return jsonParser.getLongValue();
        }
        catch (Exception x) {
            String value = jsonParser.getValueAsString().replace(" ", "").split("-")[0].replace("/", "-");
            if(
                    !persianDateTimePattern.matcher(value).matches() &&
                    !persianDateTimePattern2.matcher(value).matches() &&
                    !datePattern.matcher(value).matches() &&
                    !datePattern2.matcher(value).matches()
            )
                throw new RuntimeException("field is not valid");

            return convertJalaliToGregorianDate(value).getTime();
        }
    }
}
