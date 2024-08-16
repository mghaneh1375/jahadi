package four.group.jahadi.Models;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import four.group.jahadi.Service.Area.PatientServiceInArea;
import four.group.jahadi.Service.UserService;

import java.io.IOException;

import static four.group.jahadi.Utility.StaticValues.SERVER_ADDR;

public class PatientDocSerialization extends JsonSerializer<String> {

    @Override
    public void serialize(String filename, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(filename == null ? null : SERVER_ADDR + PatientServiceInArea.UPLOAD_FOLDER + "/" + filename);
    }
}
