package four.group.jahadi.DTO.Region;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

import static four.group.jahadi.Utility.Utility.convertJalaliToGregorianDate;
import static four.group.jahadi.Utility.Utility.persianDateTimePattern;

public class ConvertStringToLongDeserialization extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if(jsonParser == null) return null;
        try {
            return jsonParser.getValueAsLong();
        }
        catch (Exception x) {
            String value = jsonParser.getValueAsString();
            if(!persianDateTimePattern.matcher(value.toString()).matches())
                throw new RuntimeException("field is not valid");

            return convertJalaliToGregorianDate(
                    value.split("-")[0].replace(" ", "").replace("/", "-")
            ).getTime();
        }
    }
}
